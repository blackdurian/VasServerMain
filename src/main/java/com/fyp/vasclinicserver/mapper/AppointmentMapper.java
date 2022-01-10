package com.fyp.vasclinicserver.mapper;

import com.fyp.vasclinicserver.model.Appointment;
import com.fyp.vasclinicserver.payload.AppointmentResponse;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
            ShiftMapper.class,
            ClinicMapper.class,
            VaccineMapper.class
        },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AppointmentMapper {

    @Mapping(target = "recipient", source = "recipient.username")
    @InheritInverseConfiguration
    AppointmentResponse mapToAppointmentResponse(Appointment appointment);


}
