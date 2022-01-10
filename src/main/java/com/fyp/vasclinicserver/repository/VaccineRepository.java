package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineRepository extends  JpaRepository<Vaccine,String> {
    Page<Vaccine> findByDeletedFalse(Pageable pageable);
    List<Vaccine> findByDeletedFalse();
    Page<Vaccine> findByDeletedFalseAndId(String id, Pageable pageable);

}
