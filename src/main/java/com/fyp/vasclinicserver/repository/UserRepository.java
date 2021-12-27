package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.model.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Page<User> findByRoles_Name(String name,Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndRoles_Name(String username,String name,Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
