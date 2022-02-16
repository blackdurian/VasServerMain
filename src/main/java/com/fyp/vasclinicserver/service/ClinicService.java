package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.ClinicMapper;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.UserMapper;
import com.fyp.vasclinicserver.model.*;
import com.fyp.vasclinicserver.model.enums.RoleName;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    private final VaccineRepository vaccineRepository;
    private final VaccineInventoryRepository vaccineInventoryRepository;

    private final ClinicMapper clinicMapper;
    private final UserMapper userMapper;

    public ClinicResponse save(ClinicRequest clinicRequest) {
        User admin = userRepository.findByUsername(clinicRequest.getAdminId())
                .orElseThrow(() -> new VasException(clinicRequest.getName() + " admin can't found"));
        Clinic clinic = clinicMapper.mapToClinic(clinicRequest, admin);
        Clinic savedClinic = clinicRepository.save(clinic);
        return clinicMapper.mapToClinicResponse(savedClinic);
    }

    public Page<ClinicResponse> getAllClinics(String sort, String range, String filter) throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort,range);
        Map<String, Object> filterNode = getFilterNote(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        //TODO : implement filter refer to  getAllEmployeesByClinic()
        Page<Clinic> clinics = clinicRepository.findByDeletedFalse(paging);

        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if (key.equals("id") && value instanceof String) {
                clinics = clinicRepository.findByDeletedFalseAndId((String) value, paging);
            }
        }
        return clinics.map(clinicMapper::mapToClinicResponse);
    }

    public Page<EmployeeResponse> getAllEmployeesByClinic(String sort, String range, String filter) throws JsonProcessingException {
        User admin = authService.getCurrentUser();
        Clinic clinic = getCurrentClinic();
        List<User> clinicEmployee = new ArrayList<>(clinic.getClinicEmployees());
        clinicEmployee.add(admin);
        List<EmployeeResponse> employees = clinicEmployee.stream()
                .map((User user) -> userMapper.mapToEmployeeResponse(user,clinic.getId()))
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

    public Clinic getCurrentClinic() {
        User admin = authService.getCurrentUser();
        return clinicRepository.findByDeletedFalseAndAdmin(admin).orElseThrow(() -> new VasException("Clinic not found"));
    }

    public EmployeeResponse createAdmin(EmployeeRequest employeeRequest) {
        RegisterRequest registerRequest = userMapper.mapToRegisterRequest(employeeRequest);
        Role role = roleRepository.findByName(RoleName.ROLE_CLINIC_ADMIN) .orElseThrow(() -> new VasException("User Role not set."));
        User user = authService.signup(registerRequest, role);
        return userMapper.mapToEmployeeResponse(user,null);
    }

    //TODO: add role to exist User
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        RegisterRequest registerRequest = userMapper.mapToRegisterRequest(employeeRequest);
        Role role = roleRepository.findByName(RoleName.valueOf(employeeRequest.getRole())) .orElseThrow(() -> new VasException("User Role not set."));
        authService.signup(registerRequest, role);
        User user = userRepository.findByUsername(employeeRequest.getUsername()).orElseThrow(() -> new VasException("Internal error: unable to save user"));
        Clinic clinic = getCurrentClinic();
        clinic.getClinicEmployees().add(user);
        clinicRepository.save(clinic);
        return userMapper.mapToEmployeeResponse(user,clinic.getId());
    }


    public Page<EmployeeResponse> getAllClinicAdmins(String sort, String range, String filter) throws JsonProcessingException {
        //admins from clinic table
        List<EmployeeResponse> clinicAdmins =  clinicRepository.findByDeletedFalse().stream()
                .map(clinic -> userMapper.mapToEmployeeResponse(clinic.getAdmin(),clinic.getId()))
                .collect(Collectors.toList());

        //find admins without clinic
        List<EmployeeResponse> admins = userRepository.findByRoles_Name(RoleName.ROLE_CLINIC_ADMIN).stream()
                .filter(a-> clinicAdmins.stream().map(EmployeeResponse::getId).noneMatch(username -> username.equals(a.getUsername())))
                .map(a-> userMapper.mapToEmployeeResponse(a,""))
                .collect(Collectors.toList());
        //union all admin
        List<EmployeeResponse> allAdmin = Stream.concat(clinicAdmins.stream(), admins.stream())
                .distinct()
                .collect(Collectors.toList());
        //filter
        Map<String, Object> filterNode = getFilterNote(filter);
        Set<String> keys = filterNode.keySet();
        if (keys.size()>0) {
            String[] qKeys = new String[] { "q" , "roles"};
            // q: all text search
            if(keys.contains(qKeys[0])){
                Object value = filterNode.get(qKeys[0]);
                if (value instanceof String&& !(((String) value).trim()).isEmpty()){
                    allAdmin = allAdmin.stream()
                            .filter(c -> c.getRoles().contains((String) value)
                                    || c.getEmail().contains((String) value)
                                    || c.getName().contains((String) value)
                                    || c.getId().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            // role: filter by roles
            if(keys.contains(qKeys[1])){
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof String&& !(((String) value).trim()).isEmpty()){
                    allAdmin = allAdmin.stream()
                            .filter(c -> c.getRoles().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }

        }
        return PagingMapper.mapToPage(allAdmin,sort,range);

    }

    // TODO: map to select Input payload instead of EmployeeResponse
    public Page<EmployeeResponse> getAdminsSelectInput(String sort, String range, String filter) throws JsonProcessingException {
        //admins from clinic table
        List<EmployeeResponse> clinicAdmins =  clinicRepository.findByDeletedFalse().stream()
                .map(clinic -> userMapper.mapToEmployeeResponse(clinic.getAdmin(),clinic.getId()))
                .collect(Collectors.toList());

        //find admins without clinic
        List<EmployeeResponse> admins = userRepository.findByRoles_Name(RoleName.ROLE_CLINIC_ADMIN).stream()
                .filter(a-> clinicAdmins.stream().map(EmployeeResponse::getId).noneMatch(username -> username.equals(a.getUsername())))
                .map(a-> userMapper.mapToEmployeeResponse(a,""))
                .collect(Collectors.toList());

        //filter
        Map<String, Object> filterNode = getFilterNote(filter);
        Set<String> keys = filterNode.keySet();
        if (keys.size()>0) {
            String[] qKeys = new String[] { "q" , "roles"};
            // q: all text search
            if(keys.contains(qKeys[0])){
                Object value = filterNode.get(qKeys[0]);
                if (value instanceof String&& !(((String) value).trim()).isEmpty()){
                    admins = admins.stream()
                            .filter(c -> c.getRoles().contains((String) value)
                                    || c.getEmail().contains((String) value)
                                    || c.getName().contains((String) value)
                                    || c.getId().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            // role: filter by roles
            if(keys.contains(qKeys[1])){
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof String&& !(((String) value).trim()).isEmpty()){
                    admins = admins.stream()
                            .filter(c -> c.getRoles().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }

        }
        return PagingMapper.mapToPage(admins,sort,range);
    }

    public List<AvailableClinic> getAllClinicsByVaccineId(String id) {
        Vaccine vaccine = vaccineRepository.getById(id);
        List<VaccineInventory> vaccineInventories = vaccineInventoryRepository.findByVaccine(vaccine);
        return  vaccineInventories.stream()
                .map(vaccineInventory -> clinicMapper.mapToAvailableClinic(vaccineInventory.getClinic(),vaccineInventory.getUnitPrice()))
                .collect(Collectors.toList());
    }

}
