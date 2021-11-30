package com.fyp.vasclinicserver.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineRequest {
    private List<String> sort;
    private List<Integer> range;
    private Map<String,String> filter;

}
