package com.example.chatlogs.common;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Utils {

    public static LocalDateTime getLocalDateTimeFromEpochTime(Long epochTime){
        LocalDateTime localDateTime = new Timestamp(epochTime).toLocalDateTime();
        return localDateTime;
    }

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
