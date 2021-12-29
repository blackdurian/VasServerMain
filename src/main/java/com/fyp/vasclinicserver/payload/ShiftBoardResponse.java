package com.fyp.vasclinicserver.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftBoardResponse {
    private Long id;
    private String name;
    private String clinicId;
    private String status;
    private String createdAt;
}
