package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice extends UserBaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "appId", referencedColumnName = "id")
    private Appointment appointment;

    @ManyToMany
    @JoinTable(
            name = "invoice_injected_vaccines",
            joinColumns = @JoinColumn(name = "vaccines_id"),
            inverseJoinColumns = @JoinColumn(name = "app_id"))
    private Set<Vaccine> injectedVaccines;

    private Double price;

    private String remark;

}
