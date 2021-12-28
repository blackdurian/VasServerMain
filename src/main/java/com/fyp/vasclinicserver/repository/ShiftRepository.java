package com.fyp.vasclinicserver.repository;


import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Shift;
import com.fyp.vasclinicserver.model.ShiftBoard;
import com.fyp.vasclinicserver.model.enums.ShiftBoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShiftRepository  extends JpaRepository<Shift, Long> {

    List<Shift> findByShiftBoard_ClinicAndShiftBoard_Status(final Clinic clinic,final ShiftBoardStatus status); // paging cannot use find by property of a nested object

    List<Shift> findByShiftBoard_Clinic(final Clinic clinic);
    //  @Query("SELECT s FROM Shift s WHERE s.shiftBoard.clinic.id = :#{#clinic.id} ")
}
