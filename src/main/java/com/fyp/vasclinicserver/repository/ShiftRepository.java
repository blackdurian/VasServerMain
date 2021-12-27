package com.fyp.vasclinicserver.repository;


import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Shift;
import com.fyp.vasclinicserver.model.ShiftBoard;
import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShiftRepository  extends JpaRepository<Shift, Long> {

    @Query("SELECT s FROM Shift s JOIN s.shiftBoard sb WHERE sb.clinic = ?1 ")
    Page<Shift> findByClinic(Clinic clinic, Pageable pageable);

    @Query("SELECT s FROM Shift s JOIN s.shiftBoard sb WHERE sb.clinic = ?1 AND sb.status = ?2 ")
    Page<Shift> findByClinicAndStatus(Clinic clinic, ShiftBoardStatus status, Pageable pageable);
}
