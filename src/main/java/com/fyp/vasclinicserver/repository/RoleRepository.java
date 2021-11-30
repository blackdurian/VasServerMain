package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Role;
import com.fyp.vasclinicserver.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
    Boolean existsByName(RoleName roleName);
}
