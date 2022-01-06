package com.fyp.vasclinicserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineOrderRequest {
    private String vaccineId;
    private Integer unit;
}
