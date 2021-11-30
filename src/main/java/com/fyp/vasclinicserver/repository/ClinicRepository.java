package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, String> {

}
