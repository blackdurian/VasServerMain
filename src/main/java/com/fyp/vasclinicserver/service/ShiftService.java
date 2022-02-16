package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.ResourceNotFoundException;
import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.ShiftMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Shift;
import com.fyp.vasclinicserver.model.ShiftBoard;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;
import com.fyp.vasclinicserver.payload.shift.ShiftBasic;
import com.fyp.vasclinicserver.payload.shift.ShiftRequest;
import com.fyp.vasclinicserver.payload.shift.ShiftResponse;
import com.fyp.vasclinicserver.repository.ClinicRepository;
import com.fyp.vasclinicserver.repository.ShiftBoardRepository;
import com.fyp.vasclinicserver.repository.ShiftRepository;
import com.fyp.vasclinicserver.repository.UserRepository;
import com.fyp.vasclinicserver.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final ShiftBoardRepository shiftBoardRepository;
    private final ClinicRepository clinicRepository;

    private final ShiftMapper shiftMapper;

    private final AuthService authService;
    private final ClinicService clinicService;

    public ShiftResponse save(ShiftRequest shiftRequest){
        //TODO: validate doctor time slot
        //TODO: validate shiftBoard time conflict
        User doctor = userRepository.findByUsername(shiftRequest.getDoctor())
                .orElseThrow(() -> new VasException(shiftRequest.getDoctor()+ " doctor no found"));
        ShiftBoard shiftBoard = shiftBoardRepository.findById(shiftRequest.getShiftBoard())
                .orElseThrow(() -> new VasException(shiftRequest.getDoctor()+ " shiftBoard no found"));
        Shift shift = new Shift();
        shift.setStart(TimeUtil.convertStringDateTimeToInstant(shiftRequest.getStart(), TimeUtil.ISO_INSTANT_FORMAT));
        shift.setEnd(TimeUtil.convertStringDateTimeToInstant(shiftRequest.getEnd(), TimeUtil.ISO_INSTANT_FORMAT));
        shift.setDoctor(doctor);
        shift.setEnabled(true);
        shift.setShiftBoard(shiftBoard);
        Shift newShift =  shiftRepository.save(shift);
        return shiftMapper.mapToShiftResponse(newShift);
    }

    public Page<ShiftResponse> getCurrentClinicShifts(String sort, String range, String filter) throws JsonProcessingException {
        // TODO: improve Map<String,Object> filter method involved
        Map<String, Object> filterNode =  PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        Clinic clinic = clinicService.getCurrentClinic();
        System.out.println(clinic.getCity());
        List<Shift> shifts = shiftRepository.findByShiftBoard_Clinic(clinic);
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if(key.equals("q")&& value instanceof String){
                ShiftBoardStatus status = ShiftBoardStatus.valueOf( ((String) value).toUpperCase());
                shifts = shiftRepository.findByShiftBoard_ClinicAndShiftBoard_Status(clinic,status);
            }
        }
        List<ShiftResponse> shiftResponses =shifts.stream().map(shiftMapper::mapToShiftResponse).collect(Collectors.toList());
        return PagingMapper.mapToPage(shiftResponses,sort,range);
    }

    public Map<String, List<ShiftBasic>> getShiftOptionsByClinic(String clinicId) {
        Map<String, List<ShiftBasic>> result = new TreeMap<>();
        Clinic clinic = clinicRepository.findById(clinicId).orElseThrow(()->new ResourceNotFoundException("Clinic","id",clinicId));
        List<Shift> shifts = shiftRepository.findByShiftBoard_ClinicAndShiftBoard_Status(clinic,ShiftBoardStatus.PUBLISHED)
                .stream().filter(Shift::isEnabled).collect(Collectors.toList());
        Set<Instant> distinctDate = shifts.stream()
                .map(s -> s.getStart().truncatedTo(ChronoUnit.DAYS))
                .collect(Collectors.toSet());
        distinctDate.forEach( d -> {
            List<ShiftBasic> shiftBasics = shifts.stream()
                    .filter(s-> s.getStart().truncatedTo(ChronoUnit.DAYS).equals(d)) // compare same date
                    .map(shiftMapper::mapToShiftBasic)
                    .collect(Collectors.toList());
            String date = TimeUtil.convertInstantToStringDateTime(d,TimeUtil.ISO_LOCAL_DATE_FORMAT);
            result.put(date,new ArrayList<>(shiftBasics));
        });
        return result;
    }
}

