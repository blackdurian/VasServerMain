package com.fyp.vasclinicserver.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.model.VaccineOrder;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.service.VaccineOrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccines/orders")
@AllArgsConstructor
public class VaccineOrderController {
    private final VaccineOrderService vaccineOrderService;

    @GetMapping
    public ResponseEntity<?> getAllVaccineOrders(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<VaccineOrderResponse> pageResult = vaccineOrderService.getAllVaccineOrders(sort,range,filter);
            List<VaccineOrderResponse> vaccineOrders = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("VaccineOrder",range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(vaccineOrders);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/clinic")
    public ResponseEntity<?> getCurrentClinicVaccineOrders(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<VaccineOrderResponse> pageResult = vaccineOrderService.getCurrentClinicVaccineOrders(sort,range,filter);
            List<VaccineOrderResponse> vaccineOrders = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("VaccineOrder",range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(vaccineOrders);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/clinic")
    public ResponseEntity<VaccineOrderResponse> createVaccineOrder(@RequestBody VaccineOrderRequest vaccineOrderRequest){
        //validate start end time conflict
        VaccineOrderResponse response = vaccineOrderService.save(vaccineOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
