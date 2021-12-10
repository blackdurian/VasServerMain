package com.fyp.vasclinicserver.controller;

import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.service.AuthService;
import com.fyp.vasclinicserver.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
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
        authService.signup(registerRequest);

        return new ResponseEntity<>("User Registration Successful, activation email sent!!",
                OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
       User user = authService.verifyAccount(token);
        Optional<Role> role = user.getRoles().stream().findFirst();
        String loginUrl = "";
        if (role.isPresent()){
            switch (role.get().getName()){
                case ROLE_CLINIC_DOCTOR:
                case ROLE_CLINIC_ADMIN:
                    loginUrl= clinicUrl+ "/api/auth/login";
                    break;
                case ROLE_GOVT_AGENCY:
                    loginUrl= govtagencyUrl+ "/api/auth/login";
                    break;
                case ROLE_RECIPIENT:
                default:
                    loginUrl= recipientUrl+ "/api/auth/login";
                    break;
            }
        }

       return ResponseEntity.status(OK).body(String.format("Account Activated Successfully, please click <a href=\"%s\">here</a> to login", loginUrl));
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
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

}