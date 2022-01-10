package com.fyp.vasclinicserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private String id;
    private String recipient;
    private ShiftResponse shift;
    private ClinicResponse clinic;
    private VaccineResponse vaccine;
    private String remark;
    private String status;
}
