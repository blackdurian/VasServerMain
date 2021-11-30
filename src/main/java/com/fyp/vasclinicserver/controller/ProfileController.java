package com.fyp.vasclinicserver.controller;

import com.fyp.vasclinicserver.payload.ProfileResponse;
import com.fyp.vasclinicserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Objects;


@RestController
@RequestMapping("/api/profile")
@AllArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> verifyAccount(@PathVariable String username, Principal principal) {
        if (Objects.equals(username, principal.getName())){
           ProfileResponse profile = userService.getProfile(username);
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Username");
        }

    }
    //TODO: upload avatar
}
