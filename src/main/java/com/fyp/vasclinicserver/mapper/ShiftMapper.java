package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.Shift;
import com.fyp.vasclinicserver.payload.shift.ShiftBasic;
import com.fyp.vasclinicserver.payload.shift.ShiftResponse;
import com.fyp.vasclinicserver.util.TimeUtil;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface ShiftMapper {

    @Mapping(target = "start", expression = "java(mapToStringDateTime(shift.getStart()))")
    @Mapping(target = "end", expression = "java(mapToStringDateTime(shift.getEnd()))")
    @Mapping(target = "doctor", source = "doctor.username")
    @Mapping(target = "shiftBoard", source = "shiftBoard.id")
    @InheritInverseConfiguration
    ShiftResponse mapToShiftResponse(Shift shift);

    @Mapping(target = "start", expression = "java(mapToStringTime(shift.getStart()))")
    @Mapping(target = "end", expression = "java(mapToStringTime(shift.getEnd()))")
    @InheritInverseConfiguration
    ShiftBasic mapToShiftBasic(Shift shift);

    default String mapToStringDateTime(Instant date) {
        return TimeUtil.convertInstantToStringDateTime(date, TimeUtil.ISO_INSTANT_FORMAT);
    }
    default String mapToStringTime(Instant date) {
        return TimeUtil.convertInstantToStringDateTime(date, TimeUtil.SHIFT_TIME_FORMAT);
    }
}
