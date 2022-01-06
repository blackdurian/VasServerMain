package com.fyp.vasclinicserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    //TODO: separate index id and UUID
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private String id;

    private String name;

    @OneToOne
    @JoinColumn(name = "admin_user_id", referencedColumnName = "id")
    private User admin;

    @ManyToMany(fetch = LAZY)
    @JoinTable(
            name = "clinic_Employees",
            joinColumns = @JoinColumn(name = "clinic_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_user_id"))
    private Set<User> clinicEmployees = new HashSet<>();

    private String suite;

    private String street;

    private String city;

    private String zipcode;

    private double longitude;

    private double latitude;

    @JsonIgnore
    private Boolean deleted = Boolean.FALSE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clinic clinic = (Clinic) o;

        return id.equals(clinic.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
