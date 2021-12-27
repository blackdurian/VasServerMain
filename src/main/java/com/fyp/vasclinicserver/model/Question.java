package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private QuestionType type;
    private String title;
    @ElementCollection
    private List<String> choices;
    private boolean isRequired;
    private boolean hasSelectAll;
    private boolean hasNone;
    private String noneText;
    private int colCount;

}
