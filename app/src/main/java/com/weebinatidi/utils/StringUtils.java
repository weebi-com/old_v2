package com.weebinatidi.utils;

/**
 * Created by birantesy on 14/02/2018.
 */

public class StringUtils {


    /**
     *@author birantesy
     * @param s The application's environment.
     * @return True if app is empty or null
     */
    public static boolean isNullOrBlank(String s)
    {
        return (s==null || s.trim().equals(""));
    }


}
