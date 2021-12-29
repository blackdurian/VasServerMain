package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.ShiftBoardMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.ShiftBoard;

import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;

import com.fyp.vasclinicserver.payload.ShiftBoardRequest;
import com.fyp.vasclinicserver.payload.ShiftBoardResponse;
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

    public ShiftBoardResponse save(ShiftBoardRequest shiftBoardRequest) {
        Clinic clinic = clinicService.getCurrentClinic();
        ShiftBoard shiftBoard = new ShiftBoard(shiftBoardRequest.getName(),clinic, ShiftBoardStatus.DRAFT);
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


}
