package com.fyp.vasclinicserver.service;

import com.fyp.vasclinicserver.repository.AppointmentRepository;
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

    private final ClinicService clinicService;


}
