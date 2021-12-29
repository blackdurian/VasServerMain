package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.ShiftBoard;
import com.fyp.vasclinicserver.payload.ApiResponse;
import com.fyp.vasclinicserver.payload.ShiftBoardRequest;
import com.fyp.vasclinicserver.payload.ShiftBoardResponse;
import com.fyp.vasclinicserver.repository.ShiftBoardRepository;
import com.fyp.vasclinicserver.service.ClinicService;
import com.fyp.vasclinicserver.service.ShiftBoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shift/board")
@AllArgsConstructor
public class ShiftBoardController {
    private final ShiftBoardService shiftBoardService;
    private final ShiftBoardRepository shiftBoardRepository;
    private final ClinicService clinicService;
    @PostMapping
    public ResponseEntity<?> createShiftBoard(@RequestBody ShiftBoardRequest shiftBoardRequest){
        Clinic clinic = clinicService.getCurrentClinic();
        if(shiftBoardRepository.existsByNameAndClinic(shiftBoardRequest.getName(),clinic)){
            return new ResponseEntity<>(new ApiResponse(false, "ShiftBoard is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(shiftBoardRequest.getName().trim().isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false, "Name is required."),
                    HttpStatus.BAD_REQUEST);
        }
        ShiftBoardResponse response = shiftBoardService.save(shiftBoardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getClinicShiftBoards(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<ShiftBoardResponse> pageResult = shiftBoardService.getClinicShiftBoards(sort,range,filter);
            List<ShiftBoardResponse> shiftBoards = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("clinic", range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(shiftBoards);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }



}
