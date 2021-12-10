package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.ShiftBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShiftBoardRepository  extends JpaRepository<ShiftBoard, Long> {
    Optional<ShiftBoard> findById(Long id);
}
