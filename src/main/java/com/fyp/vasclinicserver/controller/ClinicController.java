package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Clinic;

import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.repository.RoleRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.service.ClinicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clinic")
@AllArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @PostMapping
    public ResponseEntity<?> createClinic(@RequestBody ClinicRequest clinicRequest){
        ClinicResponse response = clinicService.save(clinicRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //GET http://my.api.url/posts?sort=["title","ASC"]&range=[0, 24]&filter={"title":"bar"}
    @GetMapping
    public ResponseEntity<?> getAllClinics(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<ClinicResponse> pageResult = clinicService.getAllClinics(sort,range,filter);
            List<ClinicResponse> clinics = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("clinic", range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(clinics);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }


/*    @GetMapping("/roles")
    public ResponseEntity<?> getAllClinicRoles(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {

    }*/

    @GetMapping("/employees")
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

    //TODO add role
    @PostMapping("/employees")
    @PreAuthorize("hasRole('ROLE_CLINIC_ADMIN')")
    public ResponseEntity<?> createClinicEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        if(userRepository.existsByUsername(employeeRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(employeeRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        EmployeeResponse response = clinicService.createEmployee(employeeRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/admins")
    public ResponseEntity<?>createAdmin(@RequestBody EmployeeRequest employeeRequest){
        if(userRepository.existsByUsername(employeeRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(employeeRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        EmployeeResponse response = clinicService.createAdmin(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ROLE_GOVT_AGENCY')")
    public ResponseEntity<?>getAllClinicAdmins(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ){
        try {
            Page<EmployeeResponse> pageResult = clinicService.getAllClinicAdmins(sort,range,filter);
            List<EmployeeResponse> employees = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("employees", range, pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(employees);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }

    @GetMapping("/admins/selectInput")
    @PreAuthorize("hasRole('ROLE_GOVT_AGENCY')")
    public ResponseEntity<?>getAdminsSelectInput(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ){
        try {
            Page<EmployeeResponse> pageResult = clinicService.getAdminsSelectInput(sort,range,filter);
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
