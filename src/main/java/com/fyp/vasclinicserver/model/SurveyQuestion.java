package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import com.fyp.vasclinicserver.model.enums.SurveyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SurveyQuestion extends UserBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Vaccine vaccine;
    @Enumerated(EnumType.STRING)
    private SurveyType surveyType;
    @OneToMany
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private List<Question> question;
}
