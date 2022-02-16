package com.fyp.vasclinicserver.payload.shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftBasic {
    private Long id;
    private String start;
    private String end;
}
