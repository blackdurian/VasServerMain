package com.fyp.vasclinicserver.payload;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineRequest {
    @NotBlank(message = "Vaccine name is required")
    private String name;
    @NotBlank(message = "Diseases Ids is required")
    private List<Long> diseases;
    @NotBlank(message = "Number of dose is required")
    private Integer doseRequire;
    @NotBlank(message = "Doses Per Vial is required")
    private Integer dosesPerVial;
    @NotBlank(message = "Storage Temperature Upper Bound in Celsius is required")
    private Double storageTempUpperBound;
    @NotBlank(message = "Storage Temperature Lower Bound in Celsius is required")
    private Double  storageTempLowerBound;
    @NotBlank(message = "Max Storage Days is required")
    private Double  maxStorageDays;
    @NotBlank(message = "Manufacturer company is required")
    private String mfgCompany;
    @NotBlank(message = "Gap of days between vaccines is required")
    private Integer gapDays;
}
