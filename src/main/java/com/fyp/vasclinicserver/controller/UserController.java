package com.fyp.vasclinicserver.controller;

import com.fyp.vasclinicserver.payload.UserSummary;
import com.fyp.vasclinicserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserSummary response = userService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
