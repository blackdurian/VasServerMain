package com.fyp.vasclinicserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.ShiftBoard;
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

    @PostMapping
    public ResponseEntity<Void> createShiftBoard(@RequestBody String name){
        shiftBoardService.save(name);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getClinicShiftBoards(
            @RequestParam(value = "sort",  defaultValue = "[\"id\",\"ASC\"]" ) String sort,
            @RequestParam(value = "range",  defaultValue = "[0,9]")  String  range,
            @RequestParam(value = "filter",  defaultValue = "{}")  String filter
    ) {
        try {
            Page<ShiftBoard> pageResult = shiftBoardService.getClinicShiftBoards(sort,range,filter);
            List<ShiftBoard> shiftBoards = pageResult.getContent();
            String contextRange = PagingMapper.mapToContextRange("clinic", range,pageResult);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Range",contextRange).body(shiftBoards);
        } catch (JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error Message" + e.getMessage());
        }
    }



}
