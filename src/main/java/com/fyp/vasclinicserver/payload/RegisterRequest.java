package com.fyp.vasclinicserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String name;
    private String username;
    private String password;
    private String gender; //M, F, UNKNOWN
    private String bod; // ISO_DATE '2011-12-03'
}
