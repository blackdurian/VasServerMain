package com.fyp.vasclinicserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FeatureTest {

    public static void main(String[] args) {
        LocalDate date = LocalDate.parse("20140218", DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(date);

        // In case you need an instance of LocalDateTime
        LocalDateTime ldt = date.atTime(LocalTime.MIN);
        System.out.println(ldt);

    }
}