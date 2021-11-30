package com.fyp.vasclinicserver.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    //Todo: Get Timezone from properties file
 public static final DateTimeFormatter BOD_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault());

    public static Instant convertStringDateToInstant(String date, DateTimeFormatter formatter){
       return LocalDate.parse(date, formatter)
               .atStartOfDay(ZoneId.systemDefault())//TODO: get timezone from properties file/ env file
               .toInstant();

    }
    public static String convertInstantToStringDate(Instant date, DateTimeFormatter formatter){
        return formatter.format(date);

    }
}
