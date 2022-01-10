package com.fyp.vasclinicserver.controller;

import com.fyp.vasclinicserver.payload.AppointmentRequest;
import com.fyp.vasclinicserver.payload.AppointmentResponse;
import com.fyp.vasclinicserver.payload.VaccineRequest;
import com.fyp.vasclinicserver.payload.VaccineResponse;
import com.fyp.vasclinicserver.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointment")
@AllArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    //create appointment
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest appointmentRequest){
        AppointmentResponse response = appointmentService.save(appointmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //view appointment


}
