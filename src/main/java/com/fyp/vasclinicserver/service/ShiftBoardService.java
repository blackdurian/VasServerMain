package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.ShiftBoard;

import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;

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

    public void save(String name) {
        Clinic clinic = clinicService.getCurrentClinic();
        ShiftBoard shiftBoard = new ShiftBoard(name,clinic, ShiftBoardStatus.DRAFT);
        shiftBoardRepository.save(shiftBoard);
    }

    public Page<ShiftBoard> getClinicShiftBoards(String sort, String range, String filter)  throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort, range);
        Map<String, Object> filterNode =  PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        Clinic clinic = clinicService.getCurrentClinic();
        // TODO improve Map<String,Object> filter method involved
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if(key.equals("status")&& value instanceof String){
                ShiftBoardStatus status = ShiftBoardStatus.valueOf( ((String) value).toUpperCase());
                return shiftBoardRepository.findByClinicAndStatus(clinic, status,paging);
            }
        }
        return shiftBoardRepository.findByClinic(clinic,paging);
    }


}
