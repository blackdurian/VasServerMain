package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.Shift;
import com.fyp.vasclinicserver.payload.ShiftResponse;
import com.fyp.vasclinicserver.util.TimeUtil;
import org.mapstruct.Mapping;

import java.time.Instant;

public interface ShiftMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "start", expression = "java(mapStringDate(shift.getStart()))")
    @Mapping(target = "end", expression = "java(mapStringDate(shift.getEnd()))")
    @Mapping(target = "doctor", expression = "doctor.username")
    @Mapping(target = "shiftBoard", expression = "shiftBoard.id")
    @Mapping(target = "enabled", expression = "enabled")
    ShiftResponse mapToShiftResponse(Shift shift);

    default String mapStringDate(Instant date) {
        return TimeUtil.convertInstantToStringDate(date, TimeUtil.DATE_TIME_FORMAT);
    }

}