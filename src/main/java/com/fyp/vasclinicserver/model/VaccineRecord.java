package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VaccineRecord extends UserBaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "vaccine_id", referencedColumnName = "id")
    private  Vaccine vaccines;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "doctor_user_id", referencedColumnName = "id")
    private User doctor;
    private Instant vaccineDate;

}
