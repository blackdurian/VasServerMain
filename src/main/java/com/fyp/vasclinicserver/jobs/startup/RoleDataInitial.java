package com.fyp.vasclinicserver.jobs.startup;

import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.enums.RoleName;
import com.fyp.vasclinicserver.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
@Order(0)
public class RoleDataInitial implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(!roleRepository.existsByName(RoleName.ROLE_RECIPIENT)) roleRepository.save(new Role(RoleName.ROLE_RECIPIENT));
        if(!roleRepository.existsByName(RoleName.ROLE_CLINIC_ADMIN)) roleRepository.save(new Role(RoleName.ROLE_CLINIC_ADMIN));
        if(!roleRepository.existsByName(RoleName.ROLE_CLINIC_DOCTOR)) roleRepository.save(new Role(RoleName.ROLE_CLINIC_DOCTOR));
        if(!roleRepository.existsByName(RoleName.ROLE_GOVT_AGENCY)) roleRepository.save(new Role(RoleName.ROLE_GOVT_AGENCY));
    }

}
