package com.fyp.vasclinicserver.payload.shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftOptions {
    private Map<String,List<ShiftBasic>> shifts;
}
