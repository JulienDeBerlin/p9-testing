package com.dummy.myerp.technical.statictools;

import java.util.Calendar;
import java.util.Date;

public class StaticTools {

    /**
     * Private constructor that hides the default public constructor
     */
    private StaticTools() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Use to convert a Date into a Calendar object
     * @param date the date to be converted
     * @return a Calendar object
     */
    public static Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }



}
