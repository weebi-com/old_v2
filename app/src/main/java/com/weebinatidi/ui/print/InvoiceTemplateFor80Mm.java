package com.weebinatidi.ui.print;

import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;

import java.util.Calendar;
import java.util.Date;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.Config.getFormattedDate;
import static com.weebinatidi.utils.ValidatorUtils.isStringNullOrEmpty;

/**
 *  Classe ayant tout les
 *  modeles de factures de taille 80Mm a imprimer
 * @author Birante SY (birantesy@gmail.com)
 * @version 1.0
 */

public class InvoiceTemplateFor80Mm {

    public static WeebiApplication context;

    /***
     * @author Birante SY
     * Prend les informations de la facture En Cash ( Header )
     * et met le dans une chaine de caractere.
     * */
    public static String printCashInvoiceHeaderFor80Mm(String shopName,String shopNumber,String customerName,String customerNumber,String date ,int invoiceNumber){
        context = (WeebiApplication.getInstance());
        String textToBePrint =
                        "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Date : "+date
                        +"\n Facture : "+invoiceNumber
                        +"\n --------------------------------------------"
                        +"\n  Article                    Qt         Prix "
                        +"\n --------------------------------------------";
        return textToBePrint;
    }


    /***
     * @author Birante SY
     * Prend les informations de
     * la facture Cash Ou Credit ( Header )
     * et met le dans une chaine de caractere.
     *
     * */
    public static String printCustomerInvoiceHeaderFor80Mm(String shopName, String shopNumber, String customerName, String customerNumber,String date, int solde ){
        context = (WeebiApplication.getInstance());
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Date : "+date
                        +"\n Solde : "+formaterSolde(solde)
                        +"\n --------------------------------------------"
                        +"\n  Article                    Qt         Prix "
                        +"\n --------------------------------------------";
        return textToBePrint;
    }




    /***
     * @author Birante SY
     * Prend les informations de
     * la facture Cash Ou Credit ( Header )
     * et met le dans une chaine de caractere.
     *
     * */
    public static String printCustomerInvoiceHeaderFor80Mm(String shopName, String shopNumber, String customerName, String customerNumber,String date , int solde , int invoiceNumber){
        context = (WeebiApplication.getInstance());
        String textToBePrint =
        "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
        +"\n Date : "+date
        +"\n Solde : "+formaterSolde(solde)
        +"\n Facture : "+invoiceNumber
        +"\n --------------------------------------------"
        +"\n  Article                    Qt         Prix "
        +"\n --------------------------------------------";
        return textToBePrint;
    }



    /***
     * @author Birante SY
     * Prend les informations de
     * la facture Cash Ou Credit ( Header )
     * et met le dans une chaine de caractere.
     *
     * */
    public static String printCustomerTotalInvoiceHeaderFor80Mm(String shopName, String shopNumber, String customerName, String customerNumber, int solde , int invoiceNumber){
        Date date = new Date();
        context = (WeebiApplication.getInstance());
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Solde : "+formaterSolde(solde)
                        +"\n Total Factures : "+invoiceNumber
                        +"\n Date : "+getFormattedDate(date) // Ajout de la date et l'heure du ticket imprimer
                        +"\n --------------------------------------------"
                        +"\n  Article                    Qt         Prix "
                        +"\n --------------------------------------------";
        return textToBePrint;
    }

    /***
     * @author Birante SY
     * Prend les informations de
     * la facture Cash Ou Credit ( Header )
     * et met le dans une chaine de caractere.
     *
     * */
    public static String printCustomerTotalInvoiceHeaderFor80Mm(String shopName, String shopNumber, String customerName, String customerNumber, int solde , int total , int totalAfterDepot){
        Date date = new Date();
        context = (WeebiApplication.getInstance());
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Solde : "+formaterSolde(solde)
                        +"\n Total Factures : "+total
                        +"\n Solde apres depots : "+totalAfterDepot
                        +"\n Date : "+getFormattedDate(date) // Ajout de la date et l'heure du ticket imprimer
                        +"\n --------------------------------------------"
                        +"\n  Article                    Qt         Prix "
                        +"\n --------------------------------------------";
        return textToBePrint;
    }


    /***
     * @author Birante SY
     * Prend les items de la facture
     * */
    public static String printItemFor80Mm(String name,String quantity, String unitPrice){
        String textToBePrint = "\n "+name.toUpperCase()+"   "+quantity+"   "+unitPrice;
        return textToBePrint;
    }


    /***
     * @author Birante SY
     * Affiche le solde total.
     * */
    public static String printTotalAmountFor80Mm(int amount){
        String textToBePrint =
                "\n --------------------------------------------"
               +"\n Total :          "+ formaterSolde(amount);
        return textToBePrint;
    }

    /***
     * @author Birante SY
     * Affiche le solde total.
     * */
    public static String printTotalDepotAmountFor80Mm(int amount){
        String textToBePrint =
                        "\n Montant du depot :   "+ formaterSolde(amount);
        return textToBePrint;
    }


    /***
     * @author Birante SY
     * @param amount
     * Affiche le solde total.
     * */
    public static String printTotalAmountInvoiceFor80Mm(int amount){
        String textToBePrint =
                "\n --------------------------------------------"
                        +"\n Total Factures :   "+ formaterSolde(amount);
        return textToBePrint;
    }

    /***
     * @author Birante SY
     * Affiche un Message
     * de Bienvenue.
     * ex : Merci de Votre Fidelite.
     *
     * */
    public static String printThankMsgFor80Mm(){
        String textToBePrint =
                        "\n --------------------------------------------\n"
                        +"     "+context.getResources().getString(R.string.merci);
        return textToBePrint;
    }


}
