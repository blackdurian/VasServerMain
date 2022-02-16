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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private final AppointmentMapper appointmentMapper;

    public AppointmentResponse save(AppointmentRequest appointmentRequest) {
        //TODO: check user appointment time conflict
        //TODO: check user vaccine conflict
        User recipient = userRepository.findByUsername(appointmentRequest.getRecipient())
                .orElseThrow(()->new ResourceNotFoundException("recipient","Username",appointmentRequest.getRecipient()));
        List<Appointment> appointments = appointmentRepository.findByRecipientAndStatus(recipient,AppointmentStatus.SCHEDULED);

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
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(appointmentMapper::mapToAppointmentResponse)
                .collect(Collectors.toList());
        //filter
        Map<String, Object> filterNode = PagingMapper.mapToFilterNode(filter);
        Set<String> keys = filterNode.keySet();
        if (keys.size() > 0) {
            String[] qKeys = new String[]{"q", "id","status"};
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
            if (keys.contains(qKeys[2])) {
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    appointmentResponses = appointmentResponses.stream()
                            .filter(vaccine -> vaccine.getStatus().contains((String)value)
                            ).collect(Collectors.toList());
                }
            }
        }
        return PagingMapper.mapToPage(appointmentResponses,sort,range);
    }


    public AppointmentResponse getAppointmentById(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Appointment","id",id));
        return appointmentMapper.mapToAppointmentResponse(appointment);
    }
}
