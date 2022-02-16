package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.payload.EmployeeResponse;
import com.fyp.vasclinicserver.payload.shift.ShiftBasic;
import com.fyp.vasclinicserver.payload.shift.ShiftRequest;
import com.fyp.vasclinicserver.payload.shift.ShiftResponse;
import com.fyp.vasclinicserver.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shift")
@AllArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;
    @PostMapping
    public ResponseEntity<ShiftResponse> createShift(@RequestBody ShiftRequest shiftRequest){
        //validate start end time conflict
        ShiftResponse response = shiftService.save(shiftRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //TODO: create recurring shift

    @GetMapping()
    public ResponseEntity<?> getCurrentClinicShifts(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<ShiftResponse> pageResult = shiftService.getCurrentClinicShifts(sort,range,filter);
            List<ShiftResponse> shifts = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("shifts", range, pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(shifts);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }

    @GetMapping("/available/options")
    public ResponseEntity<?>getShiftOptionsByClinic(String clinic){
        try {
            Map<String,List<ShiftBasic>> shiftOptions= shiftService.getShiftOptionsByClinic(clinic);
            return ResponseEntity.status(HttpStatus.OK).body(shiftOptions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }
}
