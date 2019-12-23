package com.weebinatidi.export;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;

import com.weebinatidi.model.DbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

import static com.weebinatidi.utils.DateUtils.getDateFromDatetime;
import static com.weebinatidi.utils.FileUtils.racineDirectory;
import static com.weebinatidi.utils.FileUtils.weebiExportCSVDirectory;

/**
 * Cette classe regroupe l'ensemble des methodes
 * qui permette de pouvoir exporter
 * les donnees de la base SQLite au format
 * Csv.
 * Created by birante sy on 19/04/2018.
 */

public class ExportData {

    /**
     * Cette classe permet d'exporter
     * un fichier au format CSV contenant
     * la liste des clients de l'application.
     * @param context
     * @return true si le fichier a ete generer.
     */
    public static Boolean exportAllCustomersInCsv(Context context) {
        boolean isOk = false;
        DbHelper dbhelper = new DbHelper(context);

        String csvFileName = "ListeDeClient.csv";
        String filePath = racineDirectory + File.separator + weebiExportCSVDirectory + csvFileName;

        File file = new File(filePath);
        String[] columnsForHeader = new String[]{"Identifiant Client", "Numero de Telephone", "Nom Complet", "Solde"};

        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM clients", null);
            csvWrite.writeNext(columnsForHeader);
            while (curCSV.moveToNext()) {
                String arrStr[] = {
                        curCSV.getString(0),
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3)
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            isOk = true;
        } catch (Exception e) {
            Log.e("Message d'erreur ", e.getMessage(), e);
        }
        return isOk;
    }

    /**
     * Cette classe permet d'exporter
     * un fichier au format CSV contenant
     * la liste des produits de l'application.
     *
     * @param context
     * @return true si le fichier a ete generer.
     */
    public static Boolean exportAllProductInCsv(Context context) {
        boolean isOk = false;
        DbHelper dbhelper = new DbHelper(context);

        String csvFileName = "Produits.csv";
        String filePath = racineDirectory + File.separator + weebiExportCSVDirectory + csvFileName;

        File file = new File(filePath);

        String[] columnsForHeader = new String[]{"ID", "NOM PRODUIT", "PRIX", "QT STOCK"};

        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM referenc", null);
            csvWrite.writeNext(columnsForHeader);
            while (curCSV.moveToNext()) {
                String arrStr[] = {
                        curCSV.getString(0),
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3)
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            isOk = true;
        } catch (Exception e) {
            Log.e("Message d'erreur ", e.getMessage(), e);
        }
        return isOk;
    }

    /**
     * Cette classe permet d'exporter
     * un fichier au format CSV contenant
     * la liste des factures de l'application.
     *
     * @param context
     * @return true si le fichier a ete generer.
     */
    public static Boolean exportAllInvoicesInCsv(Context context) {
        boolean isOk = false;
        DbHelper dbhelper = new DbHelper(context);

        String csvFileName = "ListeDeFacture.csv";
        String filePath = racineDirectory + File.separator + weebiExportCSVDirectory + csvFileName;


        File file = new File(filePath);
        String[] columnsFacture = new String[]{"Numero Facture", "Date", "Montant", "Identifiant Client", "Type Facture"};

        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM facturer", null);
            csvWrite.writeNext(columnsFacture);

            while (curCSV.moveToNext()) {

                String arrStr[] = {
                        curCSV.getString(0),
                        getDateFromDatetime(curCSV.getString(1)),
                        replacePointWithComma(curCSV.getString(2)),
                        curCSV.getString(3),
                        curCSV.getString(4)
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            isOk = true;
        } catch (Exception e) {
            Log.e("Message", e.getMessage(), e);
        }
        return isOk;
    }

    /**
     * Cette fonction permet d'exporter
     * un fichier au format CSV contenant
     * la liste des articles vendu de l'application.
     *
     * @param context
     * @return true si le fichier a ete generer.
     */
    public static Boolean exportAllItemsSoldInCsv(Context context) {
        boolean isGenerate = false;
        DbHelper dbhelper = new DbHelper(context);

        String csvFileName = "ListeDeProduitVendu.csv";
        String filePath = racineDirectory + File.separator + weebiExportCSVDirectory + csvFileName;

        File file = new File(filePath);
        String[] columns = new String[]{"Article", "Prix Unitaire", "Quantite", "Prix", "Numero Facture"};
        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM items", null);
            csvWrite.writeNext(columns);
            while (curCSV.moveToNext()) {
                String arrStr[] = {
                        curCSV.getString(1),
                        replacePointWithComma(curCSV.getString(2)),
                        replacePointWithComma(curCSV.getString(3)),
                        replacePointWithComma(curCSV.getString(4)),
                        curCSV.getString(5)
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            isGenerate = true;
        } catch (Exception e) {
            Log.e("Message", e.getMessage(), e);
        }
        return isGenerate;
    }

    /**
     * Cette fonction permet d'exporter
     * un fichier au format CSV contenant
     * la liste des articles vendues par periode de l'application.
     *
     * @param date
     * @param endDate
     * @param context
     * @return true si le fichier a ete generer.
     */
    public static Boolean exportAllItemsSoldPerPeriodInCsv(Context context, String date, String endDate) {
        boolean isGenerate = false;
        DbHelper dbhelper = new DbHelper(context);


        String csvFileName = "ListeDeProduitVenduParPeriode.csv";
        String filePath = racineDirectory + File.separator + weebiExportCSVDirectory + csvFileName;


        File file = new File(filePath);
        String[] columns = new String[]{"NOM PRODUIT", "PRIX", "QT", "TOTAL", "DATE", "Identifiant Client", "Type de la Facture"};
        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("select *  from items , facturer where items.idfact = facturer.idfactr and date(facturer.datefactr) >= date(?) and date(facturer.datefactr) <= date(?)", new String[]{date, endDate});
            csvWrite.writeNext(columns);
            while (curCSV.moveToNext()) {
                String arrStr[] = {
                        curCSV.getString(1),
                        replacePointWithComma(curCSV.getString(2)),
                        replacePointWithComma(curCSV.getString(3)),
                        replacePointWithComma(curCSV.getString(4)),
                        getDateFromDatetime(curCSV.getString(7)),
                        curCSV.getString(9),
                        curCSV.getString(10)
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            isGenerate = true;
        } catch (Exception e) {
            Log.e("Message", e.getMessage(), e);
        }
        return isGenerate;
    }

    /**
     * Cette fonction permet d'exporter
     * un fichier au format CSV contenant
     * la liste des totaux  d'articles
     * vendu par periode et grouper par nom de l'application.
     *
     * @param context
     * @param date
     * @param endDate
     * @return true si le fichier a ete generer.
     */
    public static Boolean exportTotalSaleItemsDetailByDayInCsv(Context context, String date, String endDate) {
        boolean isGenerate = false;
        DbHelper dbhelper = new DbHelper(context);

        String csvFileName = "ListeDesTotauxDeProduitVenduParPeriode.csv";
        String filePath = racineDirectory + File.separator + weebiExportCSVDirectory + csvFileName;

        File file = new File(filePath);
        String[] columns = new String[]{"NOM PRODUIT", "PRIX", "QT VENDUE", "MONTANT TOTAL VENDUE"};
        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("select * , sum(cast(qtechoisit as decimal)) AS quantitetotale , sum(cast(soustotal as decimal)) AS totalvendu from items , facturer where items.idfact = facturer.idfactr and date(facturer.datefactr) >= date(?) and date(facturer.datefactr) <= date(?) group by nomitem", new String[]{date, endDate});
            csvWrite.writeNext(columns);
            while (curCSV.moveToNext()) {
                //String value = replacePointWithComma(curCSV.getString(2));

                String arrStr[] = {
                        curCSV.getString(1),
                        replacePointWithComma(curCSV.getString(2)),
                        replacePointWithComma(curCSV.getString(11)),
                        replacePointWithComma(curCSV.getString(12))
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            isGenerate = true;
        } catch (Exception e) {
            Log.e("Message", e.getMessage(), e);
        }
        return isGenerate;
    }

    private static boolean isNumberContainsAPoint(String number) {
        return number.contains(".");
    }

    private static String replacePointWithComma(String number) {
        String newValue;
        if (isNumberContainsAPoint(number)) {
            newValue = number.replace('.', ',');
        } else {
            newValue = number;
        }
        return newValue.trim();
    }
}
