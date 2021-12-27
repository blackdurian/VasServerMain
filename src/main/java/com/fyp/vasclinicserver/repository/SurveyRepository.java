package com.fyp.vasclinicserver.repository;


import com.fyp.vasclinicserver.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
