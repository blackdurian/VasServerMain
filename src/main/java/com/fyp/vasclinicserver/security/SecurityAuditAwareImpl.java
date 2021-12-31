package com.fyp.vasclinicserver.security;

import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public class SecurityAuditAwareImpl implements AuditorAware<String> {

    private final UserRepository userRepository;

    public SecurityAuditAwareImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      //   user =  userRepository.findByUsername(userDetails.getUsername()).orElseThrow(RuntimeException::new);
        return Optional.ofNullable(userDetails.getUsername());
    }
}
