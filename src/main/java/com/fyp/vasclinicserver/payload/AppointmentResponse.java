package com.fyp.vasclinicserver.payload;

import com.fyp.vasclinicserver.payload.shift.ShiftResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private String id;
    private String vaccinationId;
    private String recipient;
    private ShiftResponse shift;
    private ClinicResponse clinic;
    private VaccineResponse vaccine;
    private Integer doseNumber;//1,2,3,4
    private String remark;
    private String status;
}
