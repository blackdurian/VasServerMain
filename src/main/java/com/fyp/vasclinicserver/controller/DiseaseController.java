package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Disease;
import com.fyp.vasclinicserver.payload.ApiResponse;
import com.fyp.vasclinicserver.payload.DiseaseRequest;

import com.fyp.vasclinicserver.service.DiseaseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diseases")
@AllArgsConstructor
public class DiseaseController {
    private final DiseaseService diseaseService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_GOVT_AGENCY')")
    public ResponseEntity<Disease> createDisease(@RequestBody DiseaseRequest diseaseRequest){
        Disease response = diseaseService.save(diseaseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //GET http://my.api.url/posts?sort=["title","ASC"]&range=[0, 24]&filter={"title":"bar"}
    @GetMapping
    public ResponseEntity<?> getAllDiseases(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<Disease> pageResult = diseaseService.getAllDiseases(sort,range,filter);
            List<Disease> diseases = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("diseases",range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(diseases);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disease> findDiseaseById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diseaseService.getDisease(id));
    }

}
