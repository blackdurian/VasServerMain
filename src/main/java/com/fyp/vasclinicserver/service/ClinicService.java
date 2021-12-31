package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.ClinicMapper;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.UserMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.enums.RoleName;
import com.fyp.vasclinicserver.payload.ClinicRequest;
import com.fyp.vasclinicserver.payload.EmployeeRequest;
import com.fyp.vasclinicserver.payload.EmployeeResponse;
import com.fyp.vasclinicserver.payload.RegisterRequest;
import com.fyp.vasclinicserver.repository.ClinicRepository;
import com.fyp.vasclinicserver.repository.RoleRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class ClinicService {
    private final AuthService authService;

    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final ClinicMapper clinicMapper;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public void save(ClinicRequest clinicRequest) {
        User admin = userRepository.findById(clinicRequest.getAdminId())
                .orElseThrow(() -> new VasException(clinicRequest.getName() + " admin can't found"));
        Clinic clinic = clinicMapper.mapToClinic(clinicRequest, admin);
        clinicRepository.save(clinic);
    }

    public Page<Clinic> getAllClinics(String sort, String range, String filter) throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort,range);
        Map<String, Object> filterNode = getFilterNote(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        // TODO improve Map<String,Object> filter method involved
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if (key.equals("id") && value instanceof String) {
                return clinicRepository.findByDeletedFalseAndId((String) value, paging);
            }
        }
        return clinicRepository.findByDeletedFalse(paging);
    }

    public Page<EmployeeResponse> getAllEmployeesByClinic(String sort, String range, String filter) throws JsonProcessingException {
        User admin = authService.getCurrentUser();
        Clinic clinic = getCurrentClinic();
        List<User> clinicEmployee = new ArrayList<>(clinic.getClinicEmployees());
        clinicEmployee.add(admin);
        List<EmployeeResponse> employees = clinicEmployee.stream()
                .map((User user) -> userMapper.mapToEmployeeResponse(user,clinic))
                .collect(Collectors.toList());
        //filter
        Map<String, Object> filterNode = getFilterNote(filter);
        Set<String> keys = filterNode.keySet();
        if (keys.size()>0) {
            String[] qKeys = new String[] { "q", "roles"};
            if(keys.contains(qKeys[0])){
                Object value = filterNode.get(qKeys[0]);
            if (value instanceof String&& !(((String) value).trim()).isEmpty()){
                    employees = employees.stream()
                            .filter(c -> c.getRoles().contains((String) value)
                                    || c.getEmail().contains((String) value)
                                    || c.getName().contains((String) value)
                                    || c.getId().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            if(keys.contains(qKeys[1])){
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof String&& !(((String) value).trim()).isEmpty()){
                    employees = employees.stream()
                            .filter(c -> c.getRoles().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }

        }
        return PagingMapper.mapToPage(employees,sort,range);
    }

    private Map<String, Object> getFilterNote(String filter) throws JsonProcessingException {
        return PagingMapper.mapToFilterNode(filter);
    }


    public void createDoctor(RegisterRequest registerRequest) {
        Clinic clinic = getCurrentClinic();
        Role role = roleRepository.findByName(RoleName.ROLE_CLINIC_DOCTOR) .orElseThrow(() -> new VasException("User Role not set."));
        User user = authService.signup(registerRequest,role);
        clinic.getClinicEmployees().add(user);
        clinicRepository.save(clinic);
    }

    public Clinic getCurrentClinic() {
        User admin = authService.getCurrentUser();
        return clinicRepository.findByDeletedFalseAndAdmin(admin).orElseThrow(() -> new VasException("Clinic not found"));
    }

    public void createAdmin(RegisterRequest registerRequest) {
        Role role = roleRepository.findByName(RoleName.ROLE_CLINIC_ADMIN) .orElseThrow(() -> new VasException("User Role not set."));
        authService.signup(registerRequest, role);
    }

    //TODO: add role to exist User
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        RegisterRequest registerRequest = userMapper.mapToRegisterRequest(employeeRequest);
        Role role = roleRepository.findByName(RoleName.valueOf(employeeRequest.getRole())) .orElseThrow(() -> new VasException("User Role not set."));
        System.out.println(employeeRequest.getGender());
        authService.signup(registerRequest, role);
        User user = userRepository.findByUsername(employeeRequest.getUsername()).orElseThrow(() -> new VasException("Internal error: unable to save user"));
        Clinic clinic = getCurrentClinic();
        clinic.getClinicEmployees().add(user);
        clinicRepository.save(clinic);
        return userMapper.mapToEmployeeResponse(user,clinic);
    }


}
