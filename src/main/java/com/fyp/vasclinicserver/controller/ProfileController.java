package com.fyp.vasclinicserver.controller;

import com.fyp.vasclinicserver.payload.ProfileResponse;
import com.fyp.vasclinicserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/profile")
@AllArgsConstructor
public class ProfileController {
    private final UserService userService;

    //TODO: move to UserController
    @GetMapping("/{username}")
    public ResponseEntity<?> getCurrentProfile(@PathVariable String username, Principal principal) {
        if (Objects.equals(username, principal.getName())){
           ProfileResponse profile = userService.getProfile(username);
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Username");
        }

    }

    //TODO: move to UserController
    @PreAuthorize("hasAnyRole('ROLE_CLINIC_DOCTOR','ROLE_CLINIC_ADMIN','ROLE_GOVT_AGENCY')")
    @GetMapping("/roles")
    public ResponseEntity<?>  getRoles(Authentication auth) {
        List<String> authorities =   auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return  ResponseEntity.status(HttpStatus.OK).body(authorities);
    }

    //TODO: upload avatar
}
