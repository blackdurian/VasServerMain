package com.fyp.vasclinicserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String suite;
    private String  street;
    private String city;
    private String zipcode;
    private double longitude;
    private double latitude;
}
