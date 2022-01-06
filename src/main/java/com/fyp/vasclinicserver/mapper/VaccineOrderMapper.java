package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.VaccineOrder;
import com.fyp.vasclinicserver.payload.VaccineOrderResponse;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VaccineOrderMapper {
        @Mapping(target = "clinicId", source = "clinic.id")
        @Mapping(target = "vaccineId", source = "vaccine.id")
        @InheritInverseConfiguration
        VaccineOrderResponse mapToVaccineOrderResponse(VaccineOrder vaccineOrder);
}
