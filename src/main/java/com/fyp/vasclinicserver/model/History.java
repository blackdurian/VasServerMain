package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.BaseEntity;
import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "history")
public class History extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "doctor_user_id", referencedColumnName = "id")
    private List<VaccineRecord> vaccineRecord;
}
