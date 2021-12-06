package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, String> {
    Page<Clinic> findByDeletedFalse(Pageable pageable);

    Page<Clinic> findByDeletedFalseAndId(String id, Pageable pageable);

    Optional<Clinic> findByDeletedFalseAndAdmin(User admin);
}
