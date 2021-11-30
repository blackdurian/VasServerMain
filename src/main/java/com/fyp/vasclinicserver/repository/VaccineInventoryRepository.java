package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.VaccineInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineInventoryRepository extends JpaRepository<VaccineInventory,String> {
}
