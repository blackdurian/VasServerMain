package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public ResponseEntity<?> getCurrentClinicAppointments(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<AppointmentResponse> pageResult = appointmentService.getCurrentClinicAppointments(sort,range,filter);
            List<AppointmentResponse> responses = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("vaccines",range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(responses);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

}
