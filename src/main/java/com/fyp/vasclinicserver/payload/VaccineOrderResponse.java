package com.fyp.vasclinicserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineOrderResponse {
    private Long id;
    private String uuid;
    private String clinicId;
    private String vaccineId;
    private Integer unit;
    private String status;
}
