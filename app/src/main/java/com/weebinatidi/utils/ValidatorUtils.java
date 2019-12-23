package com.weebinatidi.utils;


import android.text.TextUtils;

import com.weebinatidi.Config;
import com.weebinatidi.R;

import java.util.Calendar;

/**
 * Cette classe nous permet de valider les chaines de caracteres
 * et quelques utilitaires.
 *
 * @author Birante SY (birantesy@gmail.com)
 * @version 1.0
 */

public class ValidatorUtils {


    public static final String paperOfWidth80 = "80"; // Papier de taille 80
    public static final String paperOfWidth58 = "58"; // Papier de taille 58

    public static final int nameWidthLimitFor58Mm = 15; // Limite de la taille de la reference.
    public static final int nameWidthLimitFor80Mm = 23; // Limite de la taille de la reference.


    /**
     * Verifie si la chaine de caractere
     * est vide ou null et retourne le resultat.
     * @param text
     * @return result
     * @author Birante Sy
     */
    public static String isStringNullOrEmpty(String text) {
        String result = (text != null && !TextUtils.isEmpty(text)) ? text : "...";
        return result;
    }


    /**
     * Coupe la longueur de cette chaine de caractere
     * et ajoute des espaces apres si la taille depasse la limite
     * et renvoie le resultat.
     * @author Birante Sy
     * @param text
     * @param nameWidthLimit
     * @return result
     */
    public static String cutAStringAndAddSpaceAfterText(String text, int nameWidthLimit) {
        String space = "";
        String result = text.length() > nameWidthLimit ? text = text.substring(0, nameWidthLimit) : text;
        for (int i = 0; i <= nameWidthLimit - text.length(); i++) {
            space += " ";
        }
        result += space;
        return result;
    }


    /**
     * @param text
     * @return result
     * @author Birante Sy
     */
    public static String addSpaceInText(String text,int nameWidthLimit) {
        String space = "";
        for (int i = 0; i <= nameWidthLimit - text.length(); i++) {
            space += " ";
        }
        return space;
    }

    /**
     ** Verifie si l'imprimante a du papier
     * de taille de "58" ou "80".
     *
     *@param name
     * @return paperWidth
     * */
    public static String findDevicePaperWidth(String name){
        String paperWidth ="";
        if (name.contains(paperOfWidth58)){
            paperWidth = paperOfWidth58;
        } else if (name.contains(paperOfWidth80)){
            paperWidth = paperOfWidth80;
        }
        return paperWidth;
    }




}
