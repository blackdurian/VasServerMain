package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.ClinicMapper;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.UserMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.payload.ClinicRequest;
import com.fyp.vasclinicserver.payload.EmployeeResponse;
import com.fyp.vasclinicserver.repository.ClinicRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final ClinicMapper clinicMapper;
    private final UserMapper userMapper;

    public void save(ClinicRequest clinicRequest) {
        User admin = userRepository.findById(clinicRequest.getAdminId())
                .orElseThrow(() -> new VasException(clinicRequest.getName() + " admin can't found"));
        Clinic clinic = clinicMapper.mapToClinic(clinicRequest, admin);
        clinicRepository.save(clinic);
    }

    public Page<Clinic> getAllClinics(String sort, String range, String filter) throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort, range);
        Map<String, Object> filterNode = PagingMapper.mapToFilterNode(filter);
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

    public Page<EmployeeResponse> getAllEmployeesByClinic(String sort, String range, String filter, String username) throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort, range);
        Map<String, Object> filterNode = PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        User admin = userRepository.findByUsername(username).orElseThrow(() -> new VasException("Invalid request"));
        Clinic clinic = clinicRepository.findByDeletedFalseAndAdmin(admin).orElseThrow(() -> new VasException("Clinic not found"));
        List<User> clinicEmployee = new ArrayList<>(clinic.getClinicStaff());
        clinicEmployee.add(admin);
        //TODO : refactor to mapper
        List<EmployeeResponse> employees = clinicEmployee.stream()
                .map((employee) -> {
                    String roles = employee.getRoles().stream()
                            .map(role -> role.getName().getLabel())
                            .collect(Collectors.joining(" | "));
                    return new EmployeeResponse(
                            employee.getUsername(),
                            employee.getName(), employee.getEmail(),
                            employee.getGender().getLabel(),
                            userMapper.mapBod(employee.getBod()),
                            roles,
                            clinic.getId());
                })
                .collect(Collectors.toList());
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if (key.equals("role") && value instanceof String) {
               employees = employees.stream().filter(c->c.getRoles().contains((String)value)).collect(Collectors.toList());
            }
        }
        return new PageImpl<>(employees, paging, employees.size());
    }
}
