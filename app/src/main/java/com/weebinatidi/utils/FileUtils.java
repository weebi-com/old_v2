package com.weebinatidi.utils;


import java.io.File;

/**
 * Cette classe regroupe l'ensemble des methodes
 * qui permette de pouvoir travailler sur les fichiers et les dossiers
 * Csv.
 * Created by birante sy on 29/06/2018.
 */

public class FileUtils {


    public static final String racineDirectory = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String weebiDatabaseDirectory = "/Weebi/Fichier/";
    public static final String weebiExportCSVDirectory = "/Weebi/Export/";

    public static final String racinePlusExportCSVDirectory = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + weebiExportCSVDirectory;


    /**
     * Methode permettant de verifier
     * si un dossier existe.
     */
    public static boolean createExportDirectoryIfNotExist() {
        boolean isCreated = false;
        File newFile = new File(racineDirectory + weebiExportCSVDirectory);
        if (!newFile.exists()) {
            newFile.mkdirs();
            isCreated = true;
        }
        return isCreated;
    }


}
