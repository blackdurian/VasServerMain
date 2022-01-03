package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.model.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Page<User> findByRoles_Name(RoleName name,Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndRoles_Name(String username,RoleName name,Pageable pageable);

    List<User> findByRoles_Name(RoleName name);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
//    @Query(value = "SELECT emp.* FROM employee emp WHERE MATCH (emp.first_name, emp.address, emp.passport_no) AGAINST (:lastName IN NATURAL LANGUAGE MODE)", nativeQuery = true)
}
