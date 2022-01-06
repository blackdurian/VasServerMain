package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import com.fyp.vasclinicserver.model.enums.VaccineOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VaccineOrder extends UserBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccines_id", referencedColumnName = "id")
    private Vaccine vaccine;

    private Integer unit;

    @Enumerated(EnumType.STRING)
    private VaccineOrderStatus status;
}
