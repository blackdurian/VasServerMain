package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.ResourceNotFoundException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.ShiftBoardMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.ShiftBoard;

import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;

import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardPostRequest;
import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardPutRequest;
import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardResponse;
import com.fyp.vasclinicserver.repository.ShiftBoardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class ShiftBoardService {
    private final ShiftBoardRepository shiftBoardRepository;
    private final ClinicService clinicService;
    private final ShiftBoardMapper shiftBoardMapper;

    public ShiftBoardResponse save(ShiftBoardPostRequest shiftBoardPostRequest) {
        Clinic clinic = clinicService.getCurrentClinic();
        ShiftBoard shiftBoard = new ShiftBoard(shiftBoardPostRequest.getName(),clinic, ShiftBoardStatus.DRAFT);
        shiftBoardRepository.save(shiftBoard);
        return shiftBoardMapper.mapToShiftBoardResponse(shiftBoard);
    }

    public Page<ShiftBoardResponse> getClinicShiftBoards(String sort, String range, String filter)  throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort, range);
        Map<String, Object> filterNode =  PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        Clinic clinic = clinicService.getCurrentClinic();
        Page<ShiftBoard> shiftBoards = shiftBoardRepository.findByClinic(clinic,paging);
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if(key.equals("status")&& value instanceof String){
                ShiftBoardStatus status = ShiftBoardStatus.valueOf( ((String) value).toUpperCase());
                shiftBoards = shiftBoardRepository.findByClinicAndStatus(clinic, status,paging);
            }
        }
        return shiftBoards.map(shiftBoardMapper::mapToShiftBoardResponse);
    }

    public ShiftBoardResponse getOneShiftBoards(Long id) {
        //TODO: validate current ROLE and staff from related clinic
        ShiftBoard shiftBoard = shiftBoardRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("ShiftBoards","id",id));
        return shiftBoardMapper.mapToShiftBoardResponse(shiftBoard);
    }

    public ShiftBoardResponse deleteShiftBoards(Long id) {
        //TODO: Soft delete
        return null;

    }

    @Transactional
    public ShiftBoardResponse updateShiftBoards(ShiftBoardResponse shiftBoardPutRequest, Long id) {
        ShiftBoard shiftBoard = shiftBoardRepository.getById(id);
        // TODO: wrap to mapper.updateShiftBoards
        shiftBoard.setName(shiftBoardPutRequest.getName());
        shiftBoard.setStatus(ShiftBoardStatus.valueOf(shiftBoardPutRequest.getStatus()));
        ShiftBoard updatedShiftBoard = shiftBoardRepository.save(shiftBoard);
        return shiftBoardMapper.mapToShiftBoardResponse(updatedShiftBoard);
    }
}
