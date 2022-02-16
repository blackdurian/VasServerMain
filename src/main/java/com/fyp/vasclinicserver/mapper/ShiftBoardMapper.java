package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.ShiftBoard;
import com.fyp.vasclinicserver.payload.shiftboard.ShiftBoardResponse;
import com.fyp.vasclinicserver.util.TimeUtil;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface ShiftBoardMapper {
    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "createdAt", expression = "java(mapToStringDate(shiftBoard.getCreatedAt()))")
    @InheritInverseConfiguration
    ShiftBoardResponse mapToShiftBoardResponse(ShiftBoard shiftBoard);

    default String mapToStringDate(Instant date) {
        return TimeUtil.convertInstantToStringDateTime(date, TimeUtil.ISO_INSTANT_FORMAT);
    }
}
