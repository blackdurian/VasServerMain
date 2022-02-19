package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.UserMapper;
import com.fyp.vasclinicserver.model.Appointment;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.enums.RoleName;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.repository.AppointmentRepository;
import com.fyp.vasclinicserver.repository.ClinicRepository;
import com.fyp.vasclinicserver.repository.RoleRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class UserService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final AuthService authService;
    private final ClinicService clinicService;


    public ProfileResponse getProfile(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            return userMapper.mapToProfileResponse(user.get());
        }else {
            throw new VasException("User's profile cannot be found");
        }
    }

    public Page<RecipientResponse> getAllRecipients(String sort, String range, String filter) throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort, range);
        Map<String, Object> filterNode = getFilterNote(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        RoleName roleName = RoleName.ROLE_RECIPIENT;
        Page<User> recipients = userRepository.findByRoles_Name(roleName,paging);
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if (key.equals("username") && value instanceof String) {
                recipients = userRepository.findByUsernameContainingIgnoreCaseAndRoles_Name((String) value,roleName,paging);
            }
        }

        return recipients.map(userMapper::mapToRecipientResponse);
    }

    private Map<String, Object> getFilterNote(String filter) throws JsonProcessingException {
        return PagingMapper.mapToFilterNode(filter);
    }

    public Page<RecipientResponse> getCurrentClinicRecipients(String sort, String range, String filter)  throws JsonProcessingException {
        Map<String, Object> filterNode = getFilterNote(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        Clinic clinic = clinicService.getCurrentClinic();
        List<Appointment> appointments = appointmentRepository.findByClinic(clinic);
        List<RecipientResponse> recipientList = appointments.stream()
                .map(Appointment::getRecipient)
                .distinct()
                .map(userMapper::mapToRecipientResponse)
                .collect(Collectors.toList());
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if (key.equals("username") && value instanceof String) {
                recipientList =  recipientList.stream().filter(r->r.getId().contains((String)value)).collect(Collectors.toList());
            }
        }
        return PagingMapper.mapToPage(recipientList,sort,range);
    }

    public UserSummary getCurrentUser() {
        User user =  authService.getCurrentUser();
        return new UserSummary(user.getId(),user.getUsername(),user.getName());
    }

    public Page<GovtAgencyAdminResponse> getAllGovtAgencyAdmins(String sort, String range, String filter) throws JsonProcessingException {
        List<User> govtAgencyAdmins = userRepository.findByRoles_Name(RoleName.ROLE_GOVT_AGENCY);
        List<GovtAgencyAdminResponse> admins = govtAgencyAdmins.stream()
                .map(userMapper::mapToGovtAgencyAdminResponse)
                .collect(Collectors.toList());
        //filter
        Map<String, Object> filterNode = getFilterNote(filter);
        Set<String> keys = filterNode.keySet();
        if (keys.size()>0) {
            String[] qKeys = new String[] { "q", "roles"};
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

    public GovtAgencyAdminResponse createGovtAgencyEmployee(EmployeeRequest employeeRequest) {
        RegisterRequest registerRequest = userMapper.mapToRegisterRequest(employeeRequest);
        Role role = roleRepository.findByName(RoleName.valueOf(employeeRequest.getRole())).orElseThrow(() -> new VasException("User Role not set."));
        User user = authService.signup(registerRequest, role);
        return userMapper.mapToGovtAgencyAdminResponse(user);
    }

}
