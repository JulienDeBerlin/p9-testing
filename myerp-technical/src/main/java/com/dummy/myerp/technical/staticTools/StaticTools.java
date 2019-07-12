package com.dummy.myerp.technical.staticTools;

import java.util.Calendar;
import java.util.Date;

public class StaticTools {

    /**
     * Use to convert a Date into a Calendar object
     * @param date
     * @return
     */
    public static Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }



}
