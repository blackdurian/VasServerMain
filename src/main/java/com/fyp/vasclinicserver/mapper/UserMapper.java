package com.fyp.vasclinicserver.mapper;


import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.payload.*;
import com.fyp.vasclinicserver.util.TimeUtil;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "username")
    @Mapping(target = "gender", expression = "java(user.getGender().getLabel())")
    @Mapping(target = "bod", expression = "java(mapBod(user.getBod()))")
    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @InheritInverseConfiguration
    ProfileResponse mapToProfileResponse(User user);

    @Mapping(target = "id", source = "username")
    @Mapping(target = "gender", expression = "java(user.getGender().getLabel())")
    @Mapping(target = "bod", expression = "java(mapBod(user.getBod()))")
    @InheritInverseConfiguration
    RecipientResponse mapToRecipientResponse(User user);

    @Mapping(target = "id", source = "user.username")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "gender", expression = "java(user.getGender().getLabel())")
    @Mapping(target = "bod", expression = "java(mapBod(user.getBod()))")
    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "verified", source = "user.enabled")
    EmployeeResponse mapToEmployeeResponse(User user, Clinic clinic);

    @InheritInverseConfiguration
    RegisterRequest mapToRegisterRequest(EmployeeRequest employeeRequest);

    default String mapRoles(Set<Role> roles) {
        return roles.stream().map(role -> role.getName().getLabel()).collect(Collectors.joining(" | "));
    }
    default String mapBod(Instant bod) {
        return TimeUtil.convertInstantToStringDate(bod, TimeUtil.BOD_FORMAT);
    }
}
