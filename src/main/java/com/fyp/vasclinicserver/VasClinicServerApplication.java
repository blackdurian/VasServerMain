package com.fyp.vasclinicserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VasClinicServerApplication {
    private static final Logger log = LoggerFactory.getLogger(VasClinicServerApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(VasClinicServerApplication.class, args);
    }


}
