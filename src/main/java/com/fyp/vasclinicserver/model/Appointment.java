package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import com.fyp.vasclinicserver.model.enums.AppointmentStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment extends UserBaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private User recipient;

    private String vaccinationId;

    @OneToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccines_id", referencedColumnName = "id")
    private Vaccine vaccine;

    private Integer doseNumber;//1,2,3,4

    private String remark;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
