package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.payload.AvailableClinic;
import com.fyp.vasclinicserver.payload.ClinicRequest;
import com.fyp.vasclinicserver.payload.ClinicResponse;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "clinicRequest.name")
    @Mapping(target = "clinicEmployees", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "suite", source = "clinicRequest.suite")
    @Mapping(target = "street", source = "clinicRequest.street")
    @Mapping(target = "city", source = "clinicRequest.city")
    @Mapping(target = "zipcode", source = "clinicRequest.zipcode")
    @Mapping(target = "longitude", source = "clinicRequest.longitude")
    @Mapping(target = "latitude", source = "clinicRequest.latitude")
    @Mapping(target = "admin", source = "admin")
    Clinic mapToClinic(ClinicRequest clinicRequest, User admin);

    @Mapping(target = "admin", source = "admin.username")
    @InheritInverseConfiguration
    ClinicResponse mapToClinicResponse(Clinic clinic);

    @Mapping(target = "id", source = "clinic.id")
    @Mapping(target = "name", source = "clinic.name")
    @Mapping(target = "suite", source = "clinic.suite")
    @Mapping(target = "street", source = "clinic.street")
    @Mapping(target = "city", source = "clinic.city")
    @Mapping(target = "zipcode", source = "clinic.zipcode")
    @Mapping(target = "longitude", source = "clinic.longitude")
    @Mapping(target = "latitude", source = "clinic.latitude")
    @Mapping(target = "vaccinePrice", source = "vaccinePrice")
    AvailableClinic mapToAvailableClinic(Clinic clinic, double vaccinePrice);
}
