package com.fyp.vasclinicserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fyp.vasclinicserver.model.audit.BaseEntity;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vaccines",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name"})
        }
)
public class Vaccine extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @NotBlank(message = "Vaccine name is required")
    private String name;
    //TODO: Refactor diseases Name to new table
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "vaccine_disease",
            joinColumns = @JoinColumn(name = "vaccine_id"),
            inverseJoinColumns = @JoinColumn(name = "disease_id"))
    private Set<Disease> diseases = new HashSet<>();
    @Positive
    private Integer doseRequire;
    @Positive
    private Integer dosesPerVial;
    private Double storageTempUpperBound;
    private Double storageTempLowerBound;
    @Positive
    private Long maxStorageDays;
    @NotBlank(message = "Manufacturer company is required")
    private String mfgCompany;
    @Positive
    private Integer gapDays;
    @JsonIgnore
    private Boolean deleted = Boolean.FALSE;
    //TODO: Vaccine dependency
}
