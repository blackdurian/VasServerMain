package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
