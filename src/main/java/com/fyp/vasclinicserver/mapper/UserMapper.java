package com.fyp.vasclinicserver.mapper;


import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.payload.ProfileResponse;
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
    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "bod", expression = "java(mapBod(user.getBod()))")
    @Mapping(target = "gender", expression = "java(user.getGender().getLabel())")
    @InheritInverseConfiguration
    ProfileResponse  userToProfileResponse(User user);

    default String mapRoles(Set<Role> roles) {
        return roles.stream().map(role -> role.getName().getLabel()).collect(Collectors.joining(" | "));
    }
    default String mapBod(Instant bod) {
        return TimeUtil.convertInstantToStringDate(bod, TimeUtil.BOD_FORMAT);
    }
}
