package com.fyp.vasclinicserver.service;

import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.model.Shift;
import com.fyp.vasclinicserver.model.ShiftBoard;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.payload.ShiftRequest;
import com.fyp.vasclinicserver.repository.ShiftBoardRepository;
import com.fyp.vasclinicserver.repository.ShiftRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final ShiftBoardRepository shiftBoardRepository;

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


}

