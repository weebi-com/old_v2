package com.weebinatidi.utils;

import android.content.SharedPreferences;

import android.content.Context;

/**
 * Cette classe nous permet de verifier
 * le premier lancement de l'application
 *
 * @author Birante SY (birantesy@gmail.com)
 * @version 1.0
 */

public class PrefManager {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "weebi-first";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String KEY_DEVICE_NAME = "KEY_DEVICE_NAME";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void addDeviceName(String name) {
        editor.putString(KEY_DEVICE_NAME, name);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


}
