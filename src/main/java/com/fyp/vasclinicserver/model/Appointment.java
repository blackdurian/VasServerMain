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

    @OneToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    private Shift shift;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "app_enquiry_vaccines",
            joinColumns = @JoinColumn(name = "vaccines_id"),
            inverseJoinColumns = @JoinColumn(name = "app_id"))
    private Set<Vaccine> enquiryVaccines = new HashSet<>();

    private String remark;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
