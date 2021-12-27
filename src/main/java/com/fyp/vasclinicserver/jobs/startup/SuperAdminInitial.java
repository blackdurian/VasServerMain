package com.fyp.vasclinicserver.jobs.startup;

import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.enums.Gender;
import com.fyp.vasclinicserver.model.enums.RoleName;
import com.fyp.vasclinicserver.repository.RoleRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(1)
public class SuperAdminInitial implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${vas.admin.super.username}")
    private String username;
    @Value("${vas.admin.super.password}")
    private String password;
    @Value("${vas.admin.super.email}")
    private String email;
    @Value("${vas.admin.super.name}")
    private String name;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(!(userRepository.existsByUsername(username) || userRepository.existsByEmail(email))){
            List<Role> roles = roleRepository.findAll();
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setName(name);
            user.setBod(Instant.now());
            user.setGender(Gender.UNKNOWN);
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            user.setEnabled(true);
            user.setRoles(new HashSet<>(roles));
            userRepository.save(user);
        }
    }
}
