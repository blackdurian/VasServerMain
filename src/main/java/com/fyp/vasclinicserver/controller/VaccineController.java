package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.payload.ApiResponse;
import com.fyp.vasclinicserver.service.VaccineService;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccines")
@AllArgsConstructor
public class VaccineController {

    private final VaccineService vaccineService;

    //GET http://my.api.url/posts?sort=["title","ASC"]&range=[0, 24]&filter={"title":"bar"}
    @GetMapping
    public ResponseEntity<?> getAllVaccines(
                                    @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
                                    @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
                                    @RequestParam(value = "filter",  defaultValue = "{}")  String filter
                                 ) {
        try {
            Page<Vaccine> pageResult = vaccineService.getAllVaccine(sort,range,filter);
            List<Vaccine> vaccines = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange(range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(vaccines);
        } catch (JsonProcessingException| NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vaccine> findVaccineById(@PathVariable("id") String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(vaccineService.getVaccine(id));
    }
}