package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.payload.VaccineRequest;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VaccineMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @InheritInverseConfiguration
    Vaccine mapToVaccine(VaccineRequest vaccineRequest);
}
