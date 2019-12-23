package com.weebinatidi.ui.print;

import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;

import java.util.Date;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.Config.getFormattedDate;
import static com.weebinatidi.utils.ValidatorUtils.isStringNullOrEmpty;

/**
 *  Classe ayant tout les
 *  modeles de factures de taille 58Mm a imprimer.
 * @author Birante SY (birantesy@gmail.com)
 * @version 1.0
 */

public class InvoiceTemplateFor58Mm {

    public static WeebiApplication context;

    /***
     *@author Birante SY (birantesy@gmail.com)
     * Modele d'entete de facture  Cash
     * ayant les parametres =>
     * @param shopName
     * @param shopNumber
     * @param customerName
     * @param customerNumber
     * @param date
     * @param invoiceNumber
     * et
     * @return textToBePrint
     * le shema de la facture.
     *
     * */
    public static String printCashInvoiceHeaderFor58Mm(String shopName,String shopNumber,String customerName,String customerNumber,String date ,int invoiceNumber){
        context = (WeebiApplication.getInstance());
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Date : "+date
                        +"\n Facture : "+invoiceNumber
                        +"\n ------------------------------"
                        +"\n  Article          Qt    Prix "
                        +"\n ------------------------------";
        return textToBePrint;
    }



    /***
     *@author Birante SY (birantesy@gmail.com)
     * Modele d'entete de facture  Cash
     * ayant les parametres =>
     * @param shopName
     * @param shopNumber
     * @param customerName
     * @param customerNumber
     * @param solde
     * @param date
     * @param invoiceNumber
     * et
     * @return textToBePrint
     * le shema de la facture.
     *
     * */
    public static String printCustomerInvoiceHeaderFor58Mm(String shopName, String shopNumber, String customerName, String customerNumber, int solde,String date, int invoiceNumber){
        context = (WeebiApplication.getInstance()) ;
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Date : "+date
                        +"\n Solde : "+formaterSolde(solde)
                        +"\n Facture : "+invoiceNumber
                        +"\n ------------------------------"
                        +"\n  Article          Qt    Prix "
                        +"\n ------------------------------";
        return textToBePrint;
    }


    /***
     *@author Birante SY (birantesy@gmail.com)
     * Modele d'entete de facture Client du total des
     * ayant les parametres =>
     * @param shopName
     * @param shopNumber
     * @param customerName
     * @param customerNumber
     * @param invoiceNumber
     * et
     * @return textToBePrint
     * le shema de la facture.
     *
     * */
    public static String printCustomerTotalInvoiceHeaderFor58Mm(String shopName, String shopNumber, String customerName, String customerNumber , int solde , int invoiceNumber){
        Date date = new Date();
        context = (WeebiApplication.getInstance()) ;
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Solde : "+formaterSolde(solde)
                        +"\n Total Facture : "+invoiceNumber
                        +"\n Date : "+getFormattedDate(date) // Ajout de la date et l'heure du ticket imprimer
                        +"\n ------------------------------"
                        +"\n  Article          Qt    Prix "
                        +"\n ------------------------------";
        return textToBePrint;
    }


    /***
     *@author Birante SY (birantesy@gmail.com)
     * Modele d'entete de facture Client du total des
     * ayant les parametres =>
     * @param shopName
     * @param shopNumber
     * @param customerName
     * @param customerNumber
     * @param date
     * @param invoiceNumber
     * et
     * @return textToBePrint
     * le shema de la facture.
     *
     * */
    public static String printCustomerTotalInvoiceHeaderFor58Mm(String shopName, String shopNumber, String customerName, String customerNumber , String date, int solde , int invoiceNumber){
        context = (WeebiApplication.getInstance());
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Date : "+date
                        +"\n Solde : "+formaterSolde(solde)
                        +"\n Total Facture : "+invoiceNumber
                        +"\n ------------------------------"
                        +"\n  Article          Qt    Prix "
                        +"\n ------------------------------";
        return textToBePrint;
    }


    /***
     *@author Birante SY (birantesy@gmail.com)
     * Modele d'entete de facture Client du total des
     * ayant les parametres =>
     * @param shopName
     * @param shopNumber
     * @param customerName
     * @param customerNumber
     * @return textToBePrint
     * le shema de la facture.
     *
     * */
    public static String printCustomerTotalInvoiceHeaderFor58Mm(String shopName, String shopNumber, String customerName, String customerNumber , int solde , int total , int totalAfterDepot){
        context = (WeebiApplication.getInstance());
        Date date = new Date();
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Solde : "+formaterSolde(solde)
                        +"\n Total Facture : "+total
                        +"\n Solde apres depots : "+totalAfterDepot
                        +"\n Date : "+getFormattedDate(date) // Ajout de la date et l'heure du ticket imprimer
                        +"\n ------------------------------"
                        +"\n  Article          Qt    Prix "
                        +"\n ------------------------------";
        return textToBePrint;
    }


    /***
     *@author Birante SY (birantesy@gmail.com)
     * Prend les informations de
     * la facture Cash Ou Credit ( Header )
     * et met le dans une chaine de caractere.
     *
     * */
    public static String printCustomerInvoiceHeaderFor58Mm(String shopName, String shopNumber, String customerName, String customerNumber,String date , int solde ){
        context = (WeebiApplication.getInstance());
        String textToBePrint =
                "\n "+context.getResources().getString(R.string.shop_name) +" : "+isStringNullOrEmpty(shopName)
                        +"\n "+context.getResources().getString(R.string.shop_call) +" : "+isStringNullOrEmpty(shopNumber)
                        +"\n Nom du client : "+isStringNullOrEmpty(customerName)
                        +"\n Numero du client : "+isStringNullOrEmpty(customerNumber)
                        +"\n Date : "+date
                        +"\n Solde : "+formaterSolde(solde)
                        +"\n ------------------------------"
                        +"\n  Article          Qt    Prix "
                        +"\n ------------------------------";
        return textToBePrint;
    }


    /***
     * @author Birante SY (birantesy@gmail.com)
     * Prend les items de la facture
     * @param name
     * @param quantity
     * @param unitPrice
     * @return textToBePrint
     * */
    public static String printItemFor58Mm(String name,String quantity, String unitPrice){
        String textToBePrint = "\n "+name.toUpperCase()+"  "+quantity+"  "+unitPrice;
        return textToBePrint;
    }


    /***
     * @author Birante SY
     * @param amount
     * Affiche le solde total.
     * */
    public static String printTotalAmountFor58Mm(int amount){
        String textToBePrint =
                "\n ------------------------------"
                +"\n Total :          "+ formaterSolde(amount);
        return textToBePrint;
    }


    /***
     * @author Birante SY
     * @param amount
     * Affiche le solde total.
     * */
    public static String printTotalDepotAmountFor58Mm(int amount){
        String textToBePrint =
                        "\n Montant du depot :   "+ formaterSolde(amount);
        return textToBePrint;
    }


    /***
     * @author Birante SY
     * @param amount
     * Affiche le solde total.
     * */
    public static String printTotalAmountInvoiceFor58Mm(int amount){
        String textToBePrint =
                "\n ------------------------------"
                        +"\n Total Factures :   "+ formaterSolde(amount);
        return textToBePrint;
    }


    /***
     *
     * @author Birante SY
     * Affiche un Message
     * de Bienvenue.
     * ex : Merci de Votre Fidelite.
     *
     * */
    public static String printThankMsgFor58Mm(){
        String textToBePrint =
                "\n ------------------------------"
                +"\n    "+WeebiApplication.getInstance().getResources().getString(R.string.merci);
        return textToBePrint;
    }


}
