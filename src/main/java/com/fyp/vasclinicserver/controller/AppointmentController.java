package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.ResourceNotFoundException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    //create appointment
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest appointmentRequest){
        try {
            AppointmentResponse response = appointmentService.save(appointmentRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (ResourceNotFoundException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    //view appointment
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_CLINIC_DOCTOR','ROLE_CLINIC_ADMIN')")
    public ResponseEntity<?> getCurrentClinicAppointments(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<AppointmentResponse> pageResult = appointmentService.getCurrentClinicAppointments(sort,range,filter);
            List<AppointmentResponse> responses = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("appointments",range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(responses);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAppointmentById(@PathVariable("id")String id ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appointmentService.getAppointmentById(id));
    }

    @GetMapping("recipient")
    @PreAuthorize("hasRole('ROLE_RECIPIENT')")
    public ResponseEntity<?> getCurrentRecipientAppointments(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<AppointmentResponse> pageResult = appointmentService.getCurrentRecipientAppointments(sort,range,filter);
            List<AppointmentResponse> responses = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("appointments",range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(responses);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@RequestBody Map<String,String> body, @PathVariable String id) {
        //TODO: valid Appointment Status
        if(body.containsKey("status")||body.containsKey("remark")){
            //TODO: new payload
            AppointmentResponse response = appointmentService.updateAppointmentStatusAndRemark(body,id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,"key not found"));
        }

    }
}
