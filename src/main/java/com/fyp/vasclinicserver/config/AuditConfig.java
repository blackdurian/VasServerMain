package com.fyp.vasclinicserver.config;


import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.security.SecurityAuditAwareImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@AllArgsConstructor
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    private final UserRepository userRepository;
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SecurityAuditAwareImpl(userRepository);
    }
}