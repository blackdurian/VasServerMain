package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Disease;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DiseaseRepository  extends JpaRepository<Disease, Long>{
    Page<Disease> findById(Long id, Pageable pageable);
}
