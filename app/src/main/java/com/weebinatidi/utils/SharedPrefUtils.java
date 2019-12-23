package com.weebinatidi.utils;

import android.content.Context;

import com.weebinatidi.WeebiApplication;

import java.io.File;

/**
 * Cette classe contient
 * toute les methodes utiles au sharepref.
 * Created by birante sy on 20/02/2018.
 */

public class SharedPrefUtils {


    /**
    * Renvoie vrai si le sharedpref exist.
     * @author birante sy
    * @param fileName
     * param context
     * return true
    * */
    public static boolean isPreferenceFileExist(String fileName , Context context) {
        File f = new File(context.getApplicationInfo().dataDir + "/shared_prefs/"
                + fileName + ".xml");
        return f.exists();
    }


}
