package com.fyp.vasclinicserver.payload.shiftboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftBoardPutRequest {
    private Long id;
    private String name;
    private String status;
}
