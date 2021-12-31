package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.payload.ApiResponse;
import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardPostRequest;
import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardPutRequest;
import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardResponse;
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

    //TODO:  rbac implement, validate role
    @PostMapping
    public ResponseEntity<?> createShiftBoard(@RequestBody ShiftBoardPostRequest shiftBoardPostRequest){
        Clinic clinic = clinicService.getCurrentClinic();
        if(shiftBoardRepository.existsByNameAndClinic(shiftBoardPostRequest.getName(),clinic)){
            return new ResponseEntity<>(new ApiResponse(false, "ShiftBoard is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(shiftBoardPostRequest.getName().trim().isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false, "Name is required."),
                    HttpStatus.BAD_REQUEST);
        }
        ShiftBoardResponse response = shiftBoardService.save(shiftBoardPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    //TODO:  rbac implement, validate role
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

    //TODO:  rbac implement, validate role
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneShiftBoard(@PathVariable Long id) {
            ShiftBoardResponse response = shiftBoardService.getOneShiftBoards(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShiftBoard(@RequestBody ShiftBoardResponse shiftBoardPutRequest, @PathVariable Long id) {
        System.out.println(shiftBoardPutRequest.getId());
        System.out.println(shiftBoardPutRequest.getStatus());
        //TODO: valid ShiftBoard Status
        ShiftBoardResponse response = shiftBoardService.updateShiftBoards(shiftBoardPutRequest,id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        ShiftBoardResponse response = shiftBoardService.deleteShiftBoards(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
