package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.payload.ApiResponse;
import com.fyp.vasclinicserver.payload.EmployeeRequest;
import com.fyp.vasclinicserver.payload.GovtAgencyAdminResponse;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/govtagency")
@AllArgsConstructor
public class GovtAgencyController
{
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/admins")
    public ResponseEntity<?> getAllGovtAgencyAdmins(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<GovtAgencyAdminResponse> pageResult = userService.getAllGovtAgencyAdmins(sort,range,filter);
            List<GovtAgencyAdminResponse> responses = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("admins", range, pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(responses);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }

    @PostMapping("/employees")
    @PreAuthorize("hasRole('ROLE_GOVT_AGENCY')")
    public ResponseEntity<?> createGovtAgencyEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        if(userRepository.existsByUsername(employeeRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(employeeRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        GovtAgencyAdminResponse response = userService.createGovtAgencyEmployee(employeeRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
