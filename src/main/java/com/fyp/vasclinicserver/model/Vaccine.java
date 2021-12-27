package com.fyp.vasclinicserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fyp.vasclinicserver.model.audit.BaseEntity;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vaccines", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        })
})

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
    @NotBlank(message = "Number of dose is required")
    private Integer doseRequire;
    @NotBlank(message = "Doses Per Vial is required")
    private Integer dosesPerVial;
    @NotBlank(message = "Storage Temperature Upper Bound in Celsius is required")
    private Integer storageTempUpperBound;
    @NotBlank(message = "Storage Temperature Lower Bound in Celsius is required")
    private Double  storageTempLowerBound;
    @NotBlank(message = "Max Storage Days is required")
    private Double  maxStorageDays;
    @NotBlank(message = "Manufacturer company is required")
    private String mfgCompany;
    private Integer GapDays;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_survey_id", referencedColumnName = "id")
    private Survey preSurvey;
    @JsonIgnore
    private Boolean deleted = Boolean.FALSE;
}
