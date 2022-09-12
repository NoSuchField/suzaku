package com.omgxy.technote.system.utils;


import java.sql.Timestamp;
import java.util.Date;

public class DateUtil {

    public static Timestamp now() {
        return new Timestamp(new Date().getTime());
    }

}
