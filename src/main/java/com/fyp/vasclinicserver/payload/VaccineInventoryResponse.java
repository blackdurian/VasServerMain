package com.fyp.vasclinicserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineInventoryResponse {
    private String id;
    private String vaccineId;
    private Integer stock;
    private String mfgDate;
    private String expiryDate;
    private Double unitPrice;
}

