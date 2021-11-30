package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.BaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VaccineInventory extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "vaccine_id", referencedColumnName = "id")
    private Vaccine vaccine;
    private Integer stock;
    private Instant mfgDate;
    private Instant expiryDate;
    private Double unitPrice;
}
