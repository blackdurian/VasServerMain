package com.fyp.vasclinicserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftResponse {
    private Long id;
    private String start;
    private String end;
    private String doctor; //username
    private Long shiftBoard;
    private boolean enabled;
}
