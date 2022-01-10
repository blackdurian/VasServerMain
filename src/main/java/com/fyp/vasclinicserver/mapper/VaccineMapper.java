package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.Disease;
import com.fyp.vasclinicserver.model.Vaccine;

import com.fyp.vasclinicserver.payload.VaccineResponse;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VaccineMapper {

    @InheritInverseConfiguration
    VaccineResponse mapToVaccineResponse(Vaccine vaccine);

    default List<Long> mapToDiseaseLongIds(Set<Disease> diseases){
        return diseases.stream().map(Disease::getId).collect(Collectors.toList());
    }

}
