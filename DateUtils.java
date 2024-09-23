/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oodj_assignment;

/**
 *
 * @author jackson
 */
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
    private static final Map<String, Integer> daysInMonth = new HashMap<>();

    static {
        daysInMonth.put("January", 31);
        daysInMonth.put("February", 28); // Assuming no leap year handling
        daysInMonth.put("March", 31);
        daysInMonth.put("April", 30);
        daysInMonth.put("May", 31);
        daysInMonth.put("June", 30);
        daysInMonth.put("July", 31);
        daysInMonth.put("August", 31);
        daysInMonth.put("September", 30);
        daysInMonth.put("October", 31);
        daysInMonth.put("November", 30);
        daysInMonth.put("December", 31);
    }

    public static int getDaysInMonth(String month, int year) {
        int days = daysInMonth.get(month);
        if ("February".equals(month) && isLeapYear(year)) {
            days = 29;
        }
        return days;
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static boolean isWeekday(int year, int monthIndex, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }
}

