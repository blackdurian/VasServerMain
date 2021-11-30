package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
}
