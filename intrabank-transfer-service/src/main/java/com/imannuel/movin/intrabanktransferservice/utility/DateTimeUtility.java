package com.imannuel.movin.intrabanktransferservice.utility;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeUtility {

    // Result example: 2024-12-10T18:50:53+07:00
    public static String getCurrentIsoDateTimeUTC7(){
        OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.ofHours(7));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        return offsetDateTime.format(dateTimeFormatter);
    }

   public static Long findDifferenceTimeFromNow(LocalDateTime fromLocalDateTime){
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(fromLocalDateTime, now);

        return duration.getSeconds();
    }

}
