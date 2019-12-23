package com.weebinatidi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.weebinatidi.WeebiApplication;

/**
 * Created by macbookpro on 07/02/2018.
 */

public class PrinterUtils {

    private static WeebiApplication context;

    private static String [] mListDevice = {"RG-P58D","RG-P80B"};


    public static void findNameOfPrinter(){
        String nameBT = context.getName();
        System.out.println("\n\n\n\n\n=================================\n\n\n\n\n");
        System.out.println("NAME : "+nameBT);
        System.out.println("\n\n\n\n\n=================================\n\n\n\n\n");


    }



}
