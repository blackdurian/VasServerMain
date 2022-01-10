package com.fyp.vasclinicserver.service;

import com.fyp.vasclinicserver.exceptions.ResourceNotFoundException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.AppointmentMapper;
import com.fyp.vasclinicserver.model.*;
import com.fyp.vasclinicserver.model.enums.AppointmentStatus;
import com.fyp.vasclinicserver.payload.AppointmentRequest;
import com.fyp.vasclinicserver.payload.AppointmentResponse;
import com.fyp.vasclinicserver.payload.NotificationEmail;
import com.fyp.vasclinicserver.payload.ShiftResponse;
import com.fyp.vasclinicserver.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final AppointmentMapper appointmentMapper;

    public AppointmentResponse save(AppointmentRequest appointmentRequest) {
        User recipient = userRepository.findByUsername(appointmentRequest.getRecipient())
                .orElseThrow(()->new ResourceNotFoundException("recipient","Username",appointmentRequest.getRecipient()));
        Shift shift = shiftRepository.findById(appointmentRequest.getShiftId())
                .orElseThrow(()->new ResourceNotFoundException("shift","id",appointmentRequest.getShiftId()));
        Clinic clinic = clinicRepository.findById(appointmentRequest.getClinicId())
                .orElseThrow(()->new ResourceNotFoundException("clinic","id",appointmentRequest.getClinicId()));
        Vaccine vaccine = vaccineRepository.findById(appointmentRequest.getVaccineId())
                .orElseThrow(()->new ResourceNotFoundException("vaccine","id",appointmentRequest.getVaccineId()));
        Appointment appointment = new Appointment();
        appointment.setRecipient(recipient);
        if(shift.isEnabled()){
            appointment.setShift(shift);
        }else {
            throw new VasException("invalid time shift");
        }
        appointment.setClinic(clinic);
        appointment.setVaccine(vaccine);
        appointment.setRemark(appointmentRequest.getRemark());
        appointment.setStatus(AppointmentStatus.PENDING);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        mailService.sendMail(new NotificationEmail("Vaccine Appointment from VAS",
                recipient.getEmail(), "Thank you for appointment request to " + clinic.getName()+
                ", your appointment is on processing. Please wait for confirmation."));
    return appointmentMapper.mapToAppointmentResponse(savedAppointment);
    }
}
