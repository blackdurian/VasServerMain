package com.fyp.vasclinicserver.payload.shiftboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftBoardPostRequest {
    private String name;
}
