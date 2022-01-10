package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.model.VaccineInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineInventoryRepository extends JpaRepository<VaccineInventory,String> {
    List<VaccineInventory> findByClinic(Clinic clinic);
    List<VaccineInventory> findByVaccine(Vaccine vaccine);
}
