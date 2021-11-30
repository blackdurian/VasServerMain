package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.BaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Clinic extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "vaccine_inventory_id", referencedColumnName = "id")
    private VaccineInventory vaccineInventory;

    private double longitude;

    private double latitude;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "clinic_staffs",
            joinColumns = @JoinColumn(name = "clinic_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_user_id"))
    private Set<User> clinicStaff = new HashSet<>();

    @OneToMany(
            mappedBy = "clinic",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ShiftBoard> boardList = new ArrayList<>();
}
