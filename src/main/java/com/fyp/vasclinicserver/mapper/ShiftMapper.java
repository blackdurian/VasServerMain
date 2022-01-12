package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.Shift;
import com.fyp.vasclinicserver.payload.shift.ShiftResponse;
import com.fyp.vasclinicserver.util.TimeUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface ShiftMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "start", expression = "java(mapToStringDate(shift.getStart()))")
    @Mapping(target = "end", expression = "java(mapToStringDate(shift.getEnd()))")
    @Mapping(target = "doctor", source = "doctor.username")
    @Mapping(target = "shiftBoard", source = "shiftBoard.id")
    @Mapping(target = "enabled", source = "enabled")
    ShiftResponse mapToShiftResponse(Shift shift);

    default String mapToStringDate(Instant date) {
        return TimeUtil.convertInstantToStringDateTime(date, TimeUtil.OFFSET_DATE_TIME_FORMAT);
    }

}
