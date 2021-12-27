package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.UserMapper;
import com.fyp.vasclinicserver.model.Appointment;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.enums.RoleName;
import com.fyp.vasclinicserver.payload.ProfileResponse;
import com.fyp.vasclinicserver.payload.RecipientResponse;
import com.fyp.vasclinicserver.repository.AppointmentRepository;
import com.fyp.vasclinicserver.repository.ClinicRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class UserService {
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final AuthService authService;
    private final AppointmentRepository appointmentRepository;

    public ProfileResponse getProfile(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            return userMapper.mapToProfileResponse(user.get());
        }else {
            throw new VasException("User's profile cannot be found");
        }
    }

    public Page<RecipientResponse> getAllRecipients(String sort, String range, String filter) throws JsonProcessingException {
        Pageable paging = getPageable(sort, range);
        Map<String, Object> filterNode = getFilterNote(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        String roleName = RoleName.ROLE_RECIPIENT.name();
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

    private Pageable getPageable(String sort, String range) throws JsonProcessingException {
        return PagingMapper.mapToPageable(sort, range);
    }

    public Page<RecipientResponse> getCurrentClinicRecipients(String sort, String range, String filter)  throws JsonProcessingException {
        Pageable paging = getPageable(sort, range);
        Map<String, Object> filterNode = getFilterNote(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        User admin = authService.getCurrentUser();
        Clinic clinic = clinicRepository.findByDeletedFalseAndAdmin(admin).orElseThrow(() -> new VasException("Clinic not found"));
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
        return new PageImpl<>(recipientList, paging, recipientList.size());
    }

}
