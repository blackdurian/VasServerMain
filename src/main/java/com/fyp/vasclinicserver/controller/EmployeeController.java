package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.payload.ApiResponse;
import com.fyp.vasclinicserver.payload.EmployeeResponse;
import com.fyp.vasclinicserver.payload.RegisterRequest;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.service.ClinicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {
    private final ClinicService clinicService;
    private final UserRepository userRepository;

    @PostMapping("/doctor")
    public ResponseEntity<?>createDoctor(@RequestBody RegisterRequest registerRequest){

        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        clinicService.createDoctor(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    public ResponseEntity<?>createAdmin(@RequestBody RegisterRequest registerRequest){

        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        clinicService.createAdmin(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clinic")
    public ResponseEntity<?> getCurrentClinicEmployees(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<EmployeeResponse> pageResult = clinicService.getAllEmployeesByClinic(sort,range,filter);
            List<EmployeeResponse> employees = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("employees", range, pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(employees);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }

}
