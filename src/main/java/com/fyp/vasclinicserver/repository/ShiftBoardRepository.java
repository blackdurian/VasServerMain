package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.ShiftBoard;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShiftBoardRepository  extends JpaRepository<ShiftBoard, Long> {
    Optional<ShiftBoard> findById(Long id);

    Page<ShiftBoard> findByClinic(Clinic clinic, Pageable pageable);

    Page<ShiftBoard> findByClinicAndStatus(Clinic clinic, ShiftBoardStatus status, Pageable pageable);

    boolean existsByNameAndClinic(String name, Clinic clinic);

}
