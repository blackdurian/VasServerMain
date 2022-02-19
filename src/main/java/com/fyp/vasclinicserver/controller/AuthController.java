package com.fyp.vasclinicserver.controller;

import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.enums.RoleName;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.repository.RoleRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.service.AuthService;
import com.fyp.vasclinicserver.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    @Value("${vas.cors.recipient.url}")
    private String recipientUrl;
    @Value("${vas.cors.clinic.url}")
    private String clinicUrl;
    @Value("${vas.cors.govtagency.url}")
    private String govtagencyUrl;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        Role role = roleRepository.findByName(RoleName.ROLE_RECIPIENT) .orElseThrow(() -> new VasException("User Role not set."));
        authService.signup(registerRequest,role);
        return new ResponseEntity<>(new ApiResponse(true, "User Registration Successful, activation email sent!!"),
                OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
       User user = authService.verifyAccount(token);
        Optional<Role> role = user.getRoles().stream().findFirst();
        String loginUrl = "";
        if (role.isPresent()){
            switch (role.get().getName()){
                case ROLE_CLINIC_DOCTOR:
                case ROLE_CLINIC_ADMIN:
                    loginUrl= clinicUrl+ "/login";
                    break;
                case ROLE_GOVT_AGENCY:
                    loginUrl= govtagencyUrl+ "/login";
                    break;
                case ROLE_RECIPIENT:
                default:
                    loginUrl= recipientUrl+ "/login";
                    break;
            }
        }

       return ResponseEntity.status(OK).body(String.format("Account Activated Successfully, please click <a href=\"%s\">here</a> to login", loginUrl));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthenticationResponse response = authService.login(loginRequest);
            return ResponseEntity.status(OK).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }

    @PostMapping("/user/role")
    public ResponseEntity<?> getCurrentUserRole(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity
                .status(OK)
                .body(authService.getCurrentUserRole(refreshTokenRequest));
    }

}
