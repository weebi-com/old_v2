package com.weebinatidi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;

import com.weebinatidi.model.Boutique;
import com.weebinatidi.ui.print.DeviceListActivity;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

import static com.weebinatidi.ui.weebi2.NewInterface.isSmartsmall;
import static com.weebinatidi.ui.weebi2.NewInterface.isTablet;
import static com.weebinatidi.ui.weebi2.NewInterface.isTablet7;

/**
 * Created by martial on 10/11/2015.
 */
public class Config {

    public static final String PREF_INVOICE_LAST_ID = "invoice_last_id";
    public static final String PREF_ARCHIVED_INVOICE_LAST_ID = "invoice_last_id";
    public static final String PREF_ITEM_LAST_ID = "item_last_id";
    public static final String SHAREDPREF_WEEBINAME = "weebi";
    public static final String EXPORT_FILE_PATH = "weebiexportpath";
    public static final String weebiemail = "hello@weebi.com";
    public static final String Boutik_url_photo = "photourl";
    public static final String printer = "blueprinter";
    public static final String TelegramappName = "org.telegram.messenger";


    public static final int REQUEST_CONNECT_PRINT_DEVICE = 1;

    public static int getLastInvoiceId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_INVOICE_LAST_ID, -1);
    }

    public static void setLastInvoiceId(Context context, int id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_INVOICE_LAST_ID, id).apply();
    }


    public static int getLastArchiveInvoiceId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_ARCHIVED_INVOICE_LAST_ID, -1);
    }

    public static void setLastArchiveInvoiceId(Context context, int id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_ARCHIVED_INVOICE_LAST_ID, id).apply();
    }

    public static int getLastItemId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_ITEM_LAST_ID, -1);
    }

    public static void setLastItemId(Context context, int id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_ITEM_LAST_ID, id).apply();
    }


    public static String getFormattedDate(Date date) {
        // Avant Modification : SimpleDateFormat format = new SimpleDateFormat(" dd-MM-yyyy 'a' hh:mm:ss a");
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat format = new SimpleDateFormat(" yyyy-MM-dd 'a' HH:mm:ss");

        return format.format(date);
    }


    public static String getFormattedTime(Date time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(time);
    }


    public static String getFormattedDateon(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String formaterSolde(int nombre) {

        DecimalFormat format = new DecimalFormat("###,###,###"); // c'est pas necessaire de mettre 3 blocs mais je me rappelle plus la syntaxe exacte
        DecimalFormatSymbols s = format.getDecimalFormatSymbols();
        s.setGroupingSeparator(' ');
        format.setDecimalFormatSymbols(s);
        String lemontant = (format.format(nombre));
        return lemontant;
    }

    public static String formaterVirgule(double nombre) {

        DecimalFormat df = new DecimalFormat("###,###");
        String dx=df.format(nombre);
        nombre=Double.valueOf(dx);
        String laqte=String.valueOf(nombre);
        return laqte;
    }

    public static String formaterQte(String nombre) {
        Double d=Double.valueOf(nombre);
        d=Math.floor(d * 100) /100;
        String format=String.valueOf(d);
        return format;
    }

    public static void WriteExportPath(Context context, String filePath, int mode) {
        SharedPreferences preferences = context.getSharedPreferences(SHAREDPREF_WEEBINAME, mode);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(EXPORT_FILE_PATH, filePath);
        edit.commit();
    }


    public static void ShowAlertDialogtosetupprinter(final Context ctx, String title, String msg) {
        new LovelyStandardDialog(ctx)
                .setTopColorRes(R.color.pad_advanced_background_color)
                .setButtonsColorRes(R.color.pad_button_text_color)
                .setIcon(R.drawable.weebilogo)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(ctx, "positive clicked", Toast.LENGTH_SHORT).show();
                        Intent serverIntent = new Intent(ctx, DeviceListActivity.class);
//                        ctx.startActivity(i);
                        ((Activity) ctx).startActivityForResult(serverIntent, REQUEST_CONNECT_PRINT_DEVICE);
//                        Intent serverIntent = new Intent(PrintDemo.this,DeviceListActivity.class);
                    }
                })
//                .setNegativeButton(android.R.string.no, null)
                .show();
    }


    public static void ShowAlertDialogtosetupTelegram(final Context ctx, String title, String msg, final String weebibottitlte, final String weebibotmsg) {
        new LovelyStandardDialog(ctx)
                .setTopColorRes(R.color.pad_advanced_background_color)
                .setButtonsColorRes(R.color.pad_button_text_color)
                .setIcon(R.drawable.weebilogo)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://play.google.com/store/apps/details?id=org.telegram.messenger";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        ctx.startActivity(i);

                        //set up weebi bot ...
                        ShowAlertDialogtosetupWeebiBot(ctx, weebibottitlte, weebibotmsg);

                    }
                })
//                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public static void ShowAlertDialogtosetupWeebiBot(final Context ctx, String title, String msg) {
        new LovelyStandardDialog(ctx)
                .setTopColorRes(R.color.pad_advanced_background_color)
                .setButtonsColorRes(R.color.pad_button_text_color)
                .setIcon(R.drawable.weebilogo)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://telegram.me/weebibot";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        ctx.startActivity(i);

                    }
                })
//                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public static String ReadExportPath(Context context, int mode) {
        SharedPreferences preferences = context.getSharedPreferences(SHAREDPREF_WEEBINAME, mode);

        String data = preferences.getString(EXPORT_FILE_PATH, "");
        return data;
    }

    public static void WritetoBase(Context context, String data, String field, int mode) {
        SharedPreferences preferences = context.getSharedPreferences(SHAREDPREF_WEEBINAME, mode);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(field, data);
        edit.commit();
    }

    public static String ReadFromBase(Context context, String field, int mode) {
        SharedPreferences preferences = context.getSharedPreferences(SHAREDPREF_WEEBINAME, mode);

        String data = preferences.getString(field, "");
        return data;
    }

    public static void SendSomethingViaEmail(String whattoSend, String filetosend, Context ctx) {
//        Realm realm =Realm.getInstance(ctx);
        Realm realm = Realm.getDefaultInstance();

        Boutique maboutique = realm.where(Boutique.class).findFirst();


        File filepath = new File("", ReadExportPath(ctx, ctx.MODE_PRIVATE));

        Uri contentUri = Uri.fromFile(filepath);

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("application/octet-stream");
        email.putExtra(Intent.EXTRA_STREAM, contentUri);
        email.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        email.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{Config.weebiemail});
        if (maboutique != null) {
            email.putExtra(Intent.EXTRA_SUBJECT, " this email has been sent by \n\t Nom : " + maboutique.getNom() + " \n\t Numero : " + maboutique.getNumero());
        }

        ctx.startActivity(Intent.createChooser(email, "Send mail..."));

    }


    public static void SendSomethingViaTelegram(Context ctx) {
//        Realm realm =Realm.getInstance(ctx);
//        Realm realm =Realm.getDefaultInstance();

//        Boutique maboutique= realm.where(Boutique.class).findFirst();


        final File filepath = new File("", ReadExportPath(ctx, ctx.MODE_PRIVATE));

        Uri contentUri = Uri.fromFile(filepath);

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("application/octet-stream");
        email.putExtra(Intent.EXTRA_STREAM, contentUri);
        email.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        email.setPackage(Config.TelegramappName);
        email.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        email.putExtra(Intent.EXTRA_EMAIL,new String[]{Config.weebiemail});
//        if(maboutique != null)
//        {
//            email.putExtra(Intent.EXTRA_SUBJECT," this email has been sent by \n\t Nom : "+maboutique.getNom()+" \n\t Numero : "+maboutique.getNumero());
////        }
//
//        ctx.startActivity(Intent.createChooser(email, "Send mail..."));
        ctx.startActivity(email);

    }


    /**
     * Indicates whether the specified app ins installed and can used as an intent. This
     * method checks the package manager for installed packages that can
     * respond to an intent with the specified app. If no suitable package is
     * found, this method returns false.
     *
     * @param context The application's environment.
     * @param appName The name of the package you want to check
     * @return True if app is installed
     */
    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
