package com.fyp.vasclinicserver.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicRequest {
    private String name;
    private String adminId;

    private String suite;

    private String street;

    private String city;

    private String zipcode;

    private double longitude;

    private double latitude;

}
