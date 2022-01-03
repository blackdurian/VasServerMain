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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vaccines")
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
    private String name; //TODO: Refactor Vaccine Name to new table
    @Positive
    private Integer doseRequire;
    @Positive
    private Integer dosesPerVial;
    private Double storageTempUpperBound;
    private Double  storageTempLowerBound;
    @Positive
    private Long  maxStorageDays;
    @NotBlank(message = "Manufacturer company is required")
    private String mfgCompany;
    @Positive
    private Integer gapDays;
    @JsonIgnore
    private Boolean deleted = Boolean.FALSE;
    //TODO: Vaccine dependency
}
