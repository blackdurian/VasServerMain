package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.payload.ApiResponse;
import com.fyp.vasclinicserver.payload.VaccineInventoryRequest;
import com.fyp.vasclinicserver.payload.VaccineInventoryResponse;
import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardPostRequest;
import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardResponse;
import com.fyp.vasclinicserver.service.VaccineInventoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccines/inventory")
@AllArgsConstructor
public class VaccineInventoryController {
    private final VaccineInventoryService vaccineInventoryService;
    //create
    @PostMapping
    public ResponseEntity<?> createVaccineInventory(@RequestBody VaccineInventoryRequest vaccineInventoryRequest){
        VaccineInventoryResponse response = vaccineInventoryService.save(vaccineInventoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //view
    @GetMapping
    public ResponseEntity<?> getClinicVaccineInventory(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<VaccineInventoryResponse> pageResult = vaccineInventoryService.getClinicVaccineInventory(sort,range,filter);
            List<VaccineInventoryResponse> responses = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("VaccineInventory", range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(responses);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }


}
