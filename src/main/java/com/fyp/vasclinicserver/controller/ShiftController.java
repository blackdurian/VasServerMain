package com.fyp.vasclinicserver.controller;

import com.fyp.vasclinicserver.payload.ClinicRequest;
import com.fyp.vasclinicserver.payload.ShiftRequest;
import com.fyp.vasclinicserver.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shift")
@AllArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;
    @PostMapping
    public ResponseEntity<Void> createShift(@RequestBody ShiftRequest shiftRequest){
        shiftService.save(shiftRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
