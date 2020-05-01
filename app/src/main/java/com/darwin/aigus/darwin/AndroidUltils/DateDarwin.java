package com.darwin.aigus.darwin.AndroidUltils;

import java.text.DateFormat;
import java.util.Date;


public class DateDarwin {
    public static String getDate(){
        Date date = new Date();
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
    }
}
