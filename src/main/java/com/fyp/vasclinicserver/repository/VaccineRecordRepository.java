package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.VaccineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRecordRepository extends JpaRepository<VaccineRecord,String> {
}
