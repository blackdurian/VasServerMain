package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
}
