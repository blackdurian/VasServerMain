package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.payload.RecipientResponse;
import com.fyp.vasclinicserver.service.ClinicService;
import com.fyp.vasclinicserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipients")
@AllArgsConstructor
public class RecipientController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllRecipients(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ){
        try {
            Page<RecipientResponse> pageResult = userService.getAllRecipients(sort,range,filter);
            List<RecipientResponse> responses = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("recipients", range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(responses);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }


    @GetMapping("/clinic")
    public ResponseEntity<?> getCurrentClinicRecipients(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<RecipientResponse> pageResult = userService.getCurrentClinicRecipients(sort,range,filter);
            List<RecipientResponse> vaccines = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("recipients", range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(vaccines);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }



}
