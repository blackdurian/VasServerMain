package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.ResourceNotFoundException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.AppointmentMapper;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.*;
import com.fyp.vasclinicserver.model.enums.AppointmentStatus;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.repository.*;
import com.fyp.vasclinicserver.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;
    private final VaccineRepository vaccineRepository;
    private final ClinicRepository clinicRepository;

    private final MailService mailService;
    private final ClinicService clinicService;
    private final AuthService authService;

    private final AppointmentMapper appointmentMapper;

    public AppointmentResponse save(AppointmentRequest appointmentRequest) {
        //TODO: check user appointment time conflict
        //TODO: check user vaccine conflict
        User recipient = userRepository.findByUsername(appointmentRequest.getRecipient())
                .orElseThrow(()->new ResourceNotFoundException("recipient","Username",appointmentRequest.getRecipient()));
        Shift shift = shiftRepository.findById(appointmentRequest.getShiftId())
                .orElseThrow(()->new ResourceNotFoundException("shift","id",appointmentRequest.getShiftId()));
        Clinic clinic = clinicRepository.findById(appointmentRequest.getClinicId())
                .orElseThrow(()->new ResourceNotFoundException("clinic","id",appointmentRequest.getClinicId()));
        Vaccine vaccine = vaccineRepository.findById(appointmentRequest.getVaccineId())
                .orElseThrow(()->new ResourceNotFoundException("vaccine","id",appointmentRequest.getVaccineId()));
        //TODO: mapper
        Appointment appointment = new Appointment();
        appointment.setRecipient(recipient);
        if(shift.isEnabled()){
            appointment.setShift(shift);
            shift.setEnabled(false);
        }else {
            throw new VasException("invalid time shift");
        }
        appointment.setClinic(clinic);
        appointment.setVaccine(vaccine);
        appointment.setDoseNumber(appointmentRequest.getDoseNumber());
        appointment.setRemark(appointmentRequest.getRemark());
        appointment.setStatus(AppointmentStatus.PENDING);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        shiftRepository.save(shift);
        mailService.sendMail(new NotificationEmail("Vaccine Appointment from VAS",
                recipient.getEmail(), "Thank you for appointment request to " + clinic.getName()+
                ", your vaccine appointment ("+shift.getStart() +" to "+shift.getEnd()+") is on processing. Please wait for confirmation."));
    return appointmentMapper.mapToAppointmentResponse(savedAppointment);
    }

    public Page<AppointmentResponse> getCurrentClinicAppointments(String sort, String range, String filter) throws JsonProcessingException {
        Clinic clinic = clinicService.getCurrentClinic();
        List<Appointment> appointments = appointmentRepository.findByClinic(clinic);
        return getAppointmentResponses(sort, range, filter, appointments);
    }


    public AppointmentResponse getAppointmentById(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Appointment","id",id));
        return appointmentMapper.mapToAppointmentResponse(appointment);
    }


    public Page<AppointmentResponse> getCurrentRecipientAppointments(String sort, String range, String filter) throws JsonProcessingException {
        User currentRecipient = authService.getCurrentUser();
        List<Appointment> appointments = appointmentRepository.findByRecipient(currentRecipient);
        return getAppointmentResponses(sort, range, filter, appointments);

    }
    //TODO: Complex object Sort, use Redis cache
    private Page<AppointmentResponse> getAppointmentResponses(String sort, String range, String filter, List<Appointment> appointments) throws JsonProcessingException {
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointmentMapper::mapToAppointmentResponse)
                .collect(Collectors.toList());
        //filter
        Map<String, Object> filterNode = PagingMapper.mapToFilterNode(filter);
        Set<String> keys = filterNode.keySet();
        if (keys.size() > 0) {
            String[] qKeys = new String[]{"q", "id", "status","date","vaccine","recipient"};
            // q: all text search
            if (keys.contains(qKeys[0])) {
                Object value = filterNode.get(qKeys[0]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    appointmentResponses = appointmentResponses.stream()
                            .filter(response -> response.getId().contains((String) value)
                                    || response.getRecipient().contains((String) value)
                                    || response.getShift().getDoctor().contains((String) value)
                                    || response.getClinic().getName().contains((String) value)
                                    || response.getRemark().contains((String) value)
                                    || response.getStatus().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            // id: filter by id
            if (keys.contains(qKeys[1])) {
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    appointmentResponses = appointmentResponses.stream()
                            .filter(response -> response.getId().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            //filter status
            if (keys.contains(qKeys[2])) {
                Object value = filterNode.get(qKeys[2]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {

                    Predicate<AppointmentResponse> appointmentResponsePredicate = getAppointmentResponsePredicate(String.valueOf(value));
                    appointmentResponses = appointmentResponses.stream()
                            .filter(appointmentResponsePredicate
                            ).collect(Collectors.toList());
                }
            }
            if (keys.contains(qKeys[3])) {
                Object value = filterNode.get(qKeys[3]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    appointmentResponses = appointmentResponses.stream()
                            .filter(appointment -> appointment.getShift().getStart().equals(value)
                            ).collect(Collectors.toList());
                }
            }
            if (keys.contains(qKeys[4])) {
                Object value = filterNode.get(qKeys[4]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    appointmentResponses = appointmentResponses.stream()
                            .filter(appointment -> appointment.getVaccine().getId().equals(value)
                            ).collect(Collectors.toList());
                }
            }
            if (keys.contains(qKeys[5])) {
                Object value = filterNode.get(qKeys[5]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    appointmentResponses = appointmentResponses.stream()
                            .filter(appointment -> appointment.getRecipient().equals(value)
                            ).collect(Collectors.toList());
                }
            }
        }
        return PagingMapper.mapToPage(appointmentResponses, sort, range);
    }

    //TODO: improve status grouping
    //Grouping Status
    private Predicate<AppointmentResponse> getAppointmentResponsePredicate(String value) {
        Predicate<AppointmentResponse> appointmentResponsePredicate = appointment -> appointment.getStatus().contains(value);
        if(AppointmentStatus.SCHEDULED.toString().compareToIgnoreCase(value)==0){
            appointmentResponsePredicate = appointment -> (
                    AppointmentStatus.valueOf(appointment.getStatus().trim().toUpperCase())==AppointmentStatus.SCHEDULED);
        }
        if(AppointmentStatus.PENDING.toString().compareToIgnoreCase(value)==0){
            appointmentResponsePredicate = appointment -> (
                    AppointmentStatus.valueOf(appointment.getStatus().trim().toUpperCase())==AppointmentStatus.PENDING)||
                    AppointmentStatus.valueOf(appointment.getStatus().trim().toUpperCase())==AppointmentStatus.PROCESSING;
        }
        if(AppointmentStatus.CANCELLED.toString().compareToIgnoreCase(value)==0){
            appointmentResponsePredicate = appointment -> (
                    AppointmentStatus.valueOf(appointment.getStatus().trim().toUpperCase())==AppointmentStatus.CANCELLED)||
                    AppointmentStatus.valueOf(appointment.getStatus().trim().toUpperCase())==AppointmentStatus.CANCELLED_BY_RECIPIENT;
        }
        if(AppointmentStatus.COMPLETED.toString().compareToIgnoreCase(value)==0){
            appointmentResponsePredicate = appointment -> (
                    AppointmentStatus.valueOf(appointment.getStatus().trim().toUpperCase())==AppointmentStatus.COMPLETED)||
                    AppointmentStatus.valueOf(appointment.getStatus().trim().toUpperCase())==AppointmentStatus.DOSE_COMPLETED;
        }
        return appointmentResponsePredicate;
    }

    //TODO: get message in Configuration class or json
    public AppointmentResponse updateAppointmentStatusAndRemark(Map<String,String> body, String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Appointment","id",id));
        appointment.setRemark(body.get("remark"));
        AppointmentStatus aStatus = AppointmentStatus.valueOf(body.get("status").trim());
        if(appointment.getStatus()!=aStatus){
            appointment.setStatus(aStatus);
            appointmentRepository.save(appointment);
            AppointmentResponse appointmentResponse = appointmentMapper.mapToAppointmentResponse(appointment);

            String message = "email from VAS";
            switch (aStatus){
                case SCHEDULED:
                    message="Your vaccination appointment has been scheduled on period of"+appointmentResponse.getShift().getStart() +" to "+appointmentResponse.getShift().getEnd();
                    break;
                case CANCELLED:
                case CANCELLED_BY_RECIPIENT:
                    message="Your vaccination appointment has been cancelled";
                    break;
                case COMPLETED:
                    message="Your vaccination has completed";
                    break;
                case DOSE_COMPLETED:
                    message=String.format( "Your vaccination has completed, please make an appointment for %d %s",appointmentResponse.getDoseNumber()+1, "dose.")   ;
                    break;
                default:
                    message = "email from VAS";
                    break;
            }
            mailService.sendMail(new NotificationEmail("Vaccine Appointment from VAS",
                    appointment.getRecipient().getEmail(), message));
            return appointmentResponse;
        }else {
            return appointmentMapper.mapToAppointmentResponse(appointment);
        }
    }
}
