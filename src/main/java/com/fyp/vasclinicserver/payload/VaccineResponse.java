package com.fyp.vasclinicserver.payload;

import com.fyp.vasclinicserver.model.Disease;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineResponse {
    private String id;
    private String name;
    private List<Disease> diseases;
    private Integer doseRequire;
    private Integer dosesPerVial;
    private Double storageTempUpperBound;
    private Double  storageTempLowerBound;
    private Double  maxStorageDays;
    private String mfgCompany;
    private Integer gapDays;
}
