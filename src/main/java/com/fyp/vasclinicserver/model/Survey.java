package com.fyp.vasclinicserver.model;

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
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private List<Question> question;
    private String jsonResult;

}
