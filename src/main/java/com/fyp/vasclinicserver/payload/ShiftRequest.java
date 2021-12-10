package com.fyp.vasclinicserver.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftRequest {
    private String start;
    private String end;
    private String doctor;
    private Long shiftBoard;
}
