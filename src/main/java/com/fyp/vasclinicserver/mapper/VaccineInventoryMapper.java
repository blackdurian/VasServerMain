package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.model.VaccineInventory;
import com.fyp.vasclinicserver.payload.VaccineInventoryRequest;
import com.fyp.vasclinicserver.payload.VaccineInventoryResponse;
import com.fyp.vasclinicserver.util.TimeUtil;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface VaccineInventoryMapper {

    @Mapping(target = "vaccineId", source = "vaccine.id")
    @Mapping(target = "mfgDate", expression = "java(mapToStringDate(vaccineInventory.getMfgDate()))")
    @Mapping(target = "expiryDate", expression = "java(mapToStringDate(vaccineInventory.getExpiryDate()))")
    @InheritInverseConfiguration
    VaccineInventoryResponse mapToVaccineInventoryResponse(VaccineInventory vaccineInventory);

    default String mapToStringDate(Instant mfgDate) {
        return TimeUtil.convertInstantToStringDateTime(mfgDate, TimeUtil.OFFSET_DATE_FORMAT);
    }

}
