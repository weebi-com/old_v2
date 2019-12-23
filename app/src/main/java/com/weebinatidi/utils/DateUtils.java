package com.weebinatidi.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Cette classe nous permet de gerer
 * toutes les problemes specifiques aux dates.
 *
 * @author Birante SY (birantesy@gmail.com)
 * @version 1.0
 */

public class DateUtils {

    /**
     * @param date Remove the letters [a AM PM] in the string.
     * @return a formated date without letter "a AM PM"
     * @author birante sy
     */
    public static String removeDateSeparator(String date) {
        String formatDate = date.replaceAll("a|[AM]|[PM]", "").trim();
        return formatDate;
    }

    /**
     * Retourne la date
     * @param datetime
     * @return a formated date without time
     * @author birante sy
     */
    public static String getDateFromDatetime(String datetime) {
        String date[] = datetime.split("\\s+");
        String newDate =  date[0].trim();
        return newDate;
    }


    //Convert Date to Calendar
    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    //Convert Calendar to Date
    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }


    /**
     * @author Birante Sy
     * birantesy@gmail.com
     * Methode qui permet de retourner des heures , minutes  et secondes.
     * dans ce format "HH:mm:ss".
     */
    public static String assembleTime(String hour, String minute) {
        String result = "";
        String second = "00";
        result += hour + ":" + minute + ":" + second;
        return result;
    }


    /**
     * @author Birante Sy
     * birantesy@gmail.com
     * Retourne une heure ses minutes et secondes.
     * dans ce format "HH:mm:ss".
     */
    public static String returnTime(String hour, String minute, String second) {

        String result = "";
        result += hour + ":" + minute + ":" + second;
        return result;
    }


    /**
     * @author Birante Sy
     * birantesy@gmail.com
     * Retourne une date
     */
    public static String returnDate(int year, int month, int day) {
        String result = "";
        result += String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
        return result;
    }


    //2018-0-7 => 2018-01-17
    public static String addLeadingZerosToDate(String date) {

        return "";
    }


    /**
     * @author Birante Sy
     * birantesy@gmail.com
     * Methode qui permet de retourner une date complete
     * de format "yyyy-MM-dd HH:mm:ss".
     */
    public static String assembleDateAndTime(String date, String time) {

        String dateTime = "";
        dateTime += date + " " + time;
        return dateTime;
    }


}
