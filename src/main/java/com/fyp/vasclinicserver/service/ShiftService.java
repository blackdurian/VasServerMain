package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.ShiftMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Shift;
import com.fyp.vasclinicserver.model.ShiftBoard;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;
import com.fyp.vasclinicserver.payload.ShiftRequest;
import com.fyp.vasclinicserver.payload.ShiftResponse;
import com.fyp.vasclinicserver.repository.ShiftBoardRepository;
import com.fyp.vasclinicserver.repository.ShiftRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.util.TimeUtil;
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
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final ShiftBoardRepository shiftBoardRepository;
    private final ShiftMapper shiftMapper;
    private final ClinicService clinicService;

    public void save(ShiftRequest shiftRequest){
        User doctor = userRepository.findByUsername(shiftRequest.getDoctor())
                .orElseThrow(() -> new VasException(shiftRequest.getDoctor()+ " doctor no found"));
        ShiftBoard shiftBoard = shiftBoardRepository.findById(shiftRequest.getShiftBoard())
                .orElseThrow(() -> new VasException(shiftRequest.getDoctor()+ " shiftBoard no found"));
        Shift shift = new Shift();
        shift.setStart(TimeUtil.convertStringDateToInstant(shiftRequest.getStart(), TimeUtil.DATE_TIME_FORMAT));
        shift.setEnd(TimeUtil.convertStringDateToInstant(shiftRequest.getEnd(), TimeUtil.DATE_TIME_FORMAT));
        shift.setDoctor(doctor);
        shift.setEnabled(true);
        shift.setShiftBoard(shiftBoard);
        shiftRepository.save(shift);
    }

    public Page<ShiftResponse> getCurrentClinicShifts(String sort, String range, String filter) throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort, range);
        Map<String, Object> filterNode =  PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        Clinic clinic = clinicService.getCurrentClinic();
        // TODO improve Map<String,Object> filter method involved
        Page<Shift> shifts = shiftRepository.findByClinic(clinic,paging);
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if(key.equals("status")&& value instanceof String){
                ShiftBoardStatus status = ShiftBoardStatus.valueOf( ((String) value).toUpperCase());
                shifts = shiftRepository.findByClinicAndStatus(clinic, status,paging);
            }
        }
        return shifts.map(shiftMapper::mapToShiftResponse);
    }

}

