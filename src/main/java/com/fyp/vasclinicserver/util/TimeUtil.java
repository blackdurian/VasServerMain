package com.fyp.vasclinicserver.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtil {
    //Todo: Get Timezone from properties file
    public static final DateTimeFormatter BOD_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault());
    public static final DateTimeFormatter MFG_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault());
    public static final DateTimeFormatter EXPIRY_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault());
    public static final DateTimeFormatter OFFSET_DATE_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE.withZone(ZoneId.systemDefault());
    public static final DateTimeFormatter OFFSET_DATE_TIME_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());

    public static Instant convertStringDateTimeToInstant(String date, DateTimeFormatter formatter){
       return LocalDateTime.parse(date, formatter)
               .atZone(ZoneId.systemDefault())
               .toInstant();

    }
    public static Instant convertStringDateToInstant(String date, DateTimeFormatter formatter){
        return LocalDate.parse(date, formatter)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant();

    }
    public static String convertInstantToStringDateTime(Instant date, DateTimeFormatter formatter){
        return formatter.format(date);

    }

}
