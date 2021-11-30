package com.fyp.vasclinicserver.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private String id; // username in font end
    private String name;
    private String email;
    private String gender;
    private String bod;
    private String roles;
}
