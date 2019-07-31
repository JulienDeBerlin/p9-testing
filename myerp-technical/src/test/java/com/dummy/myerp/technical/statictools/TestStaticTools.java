package com.dummy.myerp.technical.statictools;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

import static java.sql.Date.valueOf;
class TestStaticTools {

    @Test
    void convertDateToCalendar() {

        LocalDate localDate = LocalDate.of( 2014 , 1, 11 );
        Date date = valueOf(localDate);

        Object dateConverted = StaticTools.convertDateToCalendar(date);

        assertEquals(GregorianCalendar.class,  dateConverted.getClass());

        Calendar dateCalendar = (Calendar) dateConverted;

        System.out.println(localDate);
        System.out.println(date);
        System.out.println(dateConverted);
        System.out.println(dateCalendar);

        assertEquals (2014, dateCalendar.get(Calendar.YEAR), "année");
        assertEquals (11, dateCalendar.get(Calendar.DAY_OF_MONTH), "jour");

//       MONTH =  Field number for get and set indicating the month. This is a calendar-specific value. The first month of the year in the Gregorian and Julian calendars is JANUARY which is 0; the last depends on the number of months in a year.
        assertEquals (0, dateCalendar.get(Calendar.MONTH), "mois");

    }
}