package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.VaccineOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineOrderRepository extends JpaRepository<VaccineOrder,Long> {
    List<VaccineOrder> findByClinic(Clinic clinic);
}
