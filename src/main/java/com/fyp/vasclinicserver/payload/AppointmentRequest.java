package com.fyp.vasclinicserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private String recipient;
    private Long shiftId;
    private String clinicId;
    private String vaccineId;
    private String remark;
}
