package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Appointment;
import com.fyp.vasclinicserver.model.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByClinic(Clinic clinic);
}
