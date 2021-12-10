package com.fyp.vasclinicserver.controller;

import com.fyp.vasclinicserver.payload.ClinicRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shift/board")
@AllArgsConstructor
public class ShiftBoardController {

    @PostMapping
    public ResponseEntity<Void> createShiftBoard(@RequestBody String name){
     //   clinicService.save(clinicRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
