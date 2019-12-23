package com.weebinatidi.ui.weebi2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;
import com.weebinatidi.model.Boutique;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.Item;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.OperationClientRepository;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.ImprimeText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.ClientPageActivityNew2SQL.EXTRA_SOLDE;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCashInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCustomerTotalInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printItemFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printThankMsgFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printTotalAmountFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCashInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCustomerTotalInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printItemFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printThankMsgFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printTotalAmountFor80Mm;
import static com.weebinatidi.ui.print.PrintDemo.textToBePrinted;
import static com.weebinatidi.utils.SharedPrefUtils.isPreferenceFileExist;
import static com.weebinatidi.utils.ValidatorUtils.cutAStringAndAddSpaceAfterText;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor58Mm;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor80Mm;

public class FacturesSelectSQL extends BaseActivity {
    public static final String EXTRA_CLIENT_PHONE = "CLIENT_PHONE";
    public static final String EXTRA_CLIENT_ARCHIVE = "CLIENT_ARCHIVE";
    public InvoiceItemDetailsAdapter mAdapter;

    ArrayList<Item> listinv = new ArrayList<>();
    int total = 0;
    int idfact = -1;
    String solde = "";
    public final static String PURCHASE = "purchaseref";

    private ArrayList<String> lesdatess = new ArrayList<>();
    private ArrayList<Integer> lesidfactures = new ArrayList<>();
    private String mPhone;
    private String archived;
    private DbHelper dbHelper;
    private ListView invoiceList;
    private TextView totalfactselect, soldeafter, infosbas;
    private Button imprime, partage, yup;
    private RelativeLayout linearLayout;
    private String textapartager = "";
    int soldeaftercalc=0;
    AlertDialog.Builder RegisterNUMbuilder;
    AlertDialog RegisterNUMdialog;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String uniqueID = null;

    String shopNumber = "";
    String shopName ="";
    String shopMail ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        linearLayout = (RelativeLayout) findViewById(R.id.roote);
        lesidfactures = getIntent().getIntegerArrayListExtra("lesidfact");
        lesdatess = getIntent().getStringArrayListExtra("lesdates");
        infosbas=findViewById(R.id.soldeducli);
        mPhone = getIntent().getStringExtra(EXTRA_CLIENT_PHONE);
        archived = getIntent().getStringExtra(EXTRA_CLIENT_ARCHIVE);
        solde=getIntent().getStringExtra(EXTRA_SOLDE);
        totalfactselect = (TextView) findViewById(R.id.totalinvoiceselect);
        soldeafter = (TextView) findViewById(R.id.soldeafterdepot);
        imprime = (Button) findViewById(R.id.print_invoice_action);
        partage = (Button) findViewById(R.id.send_invoice_action);
        yup = findViewById(R.id.yup);
        invoiceList = (ListView) findViewById(R.id.listeselect);
        dbHelper=new DbHelper(this);
        mAdapter = new InvoiceItemDetailsAdapter(listinv);
        invoiceList.setAdapter(mAdapter);

        SharedPreferences sharedPrefs = this.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);


        for (int i = 0; i < lesidfactures.size(); i++) {
            idfact = lesidfactures.get(i);
            Cursor cursor=dbHelper.getInvoicer(idfact);
            while (cursor.moveToNext()){
                Cursor items=dbHelper.getItemIdInvoice(idfact);
                total=total+cursor.getInt(2);
                while (items.moveToNext()){
                    Item item=new Item();
                    //item.setId(items.getInt(0));
                    item.setName(items.getString(1));
                    item.setUnitPrice(items.getInt(2));
                    item.setQuantity(items.getInt(3));
                    item.setDateinv(cursor.getString(1));
                    listinv.add(item);
                    mAdapter.notifyDataSetChanged();
                }
            }

            getSupportActionBar().setTitle("TOLAL DES FACTURES SELECTIONNEES");
            infosbas.setText("SOLDE APRES PAIEMENT DE ("+mPhone+")"+archived+" LE "+Config.getFormattedDate(Calendar.getInstance().getTime())
            +" = ");
        }

        totalfactselect.setText("" + formaterSolde(total));
        int soldeent = Integer.valueOf(solde);

        final int totalent = Integer.valueOf(total);
         soldeaftercalc = soldeent + totalent;
         soldeafter.setText("" + formaterSolde(soldeaftercalc));

        imprime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimer();
            }
        });


        partage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lesolde = Integer.valueOf(solde);
                Cursor cursor = dbHelper.getBoutique("1");
                if (cursor.moveToFirst() == true){
                    shopNumber = cursor.getString(1);
                    shopName = cursor.getString(2);
                }
                textToBePrinted = printCustomerTotalInvoiceHeaderFor80Mm(shopName,shopNumber, archived, mPhone,lesolde, total,soldeaftercalc);
                for (int i = 0; i < listinv.size(); i++) {
                    String nom = listinv.get(i).getName();
                    String prixunit = String.valueOf(listinv.get(i).getUnitPrice());
                    String quantite = String.valueOf(listinv.get(i).getQuantity());
                    textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                }
                textToBePrinted += printTotalAmountFor80Mm(total);
                textToBePrinted += printThankMsgFor80Mm();
                shareString(textToBePrinted);
            }
        });

        if (InternetConnection.checkConnection(getApplicationContext())) {
            //Toast.makeText(getApplicationContext(), "connecté", Toast.LENGTH_SHORT).show();
            yup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegisterNUMbuilder = new AlertDialog.Builder(FacturesSelectSQL.this);
                    View customView = getLayoutInflater().inflate(R.layout.dialog_yup, null, false);


                    /*final FormEditText codever = (FormEditText) customView.findViewById(R.id.verifcodee);
                    final LinearLayout numero=customView.findViewById(R.id.clavn);
                    final LinearLayout lettre=customView.findViewById(R.id.clavl);*/
                    final TextView numboutique=customView.findViewById(R.id.numbout);
                    final TextView totalt=customView.findViewById(R.id.total);
                    final TextView idbout=customView.findViewById(R.id.boutid);
                    final TextView nomboutique=customView.findViewById(R.id.nombout);
                    final TextView mail=customView.findViewById(R.id.mail);
                    final TextView numcli=customView.findViewById(R.id.numclie);
                    final TextView nomcli=customView.findViewById(R.id.nomclie);
                    final TextView date=customView.findViewById(R.id.date);



                    final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewref);

                    Cursor bouti=dbHelper.getBoutique("1");
                    if(bouti.moveToFirst()){
                        shopNumber=bouti.getString(1);
                        shopName = bouti.getString(2);
                        shopMail = bouti.getString(3);
                    }
                    bouti.close();

                    idbout.setText(uniqueID);
                    //codever.setInputType(0);
                    numboutique.setText(shopNumber);
                    nomboutique.setText(shopName);
                    mail.setText(shopMail);
                    nomcli.setText(archived);
                    numcli.setText(mPhone);
                    date.setText(Config.getFormattedDate(Calendar.getInstance().getTime()));
                    totalt.setText(""+total);


                    RegisterNUMbuilder.setCancelable(true);
                    RegisterNUMbuilder.setView(customView);
                    RegisterNUMbuilder.setTitle(("ENVOI PAIEMENT YUP"));
                    RegisterNUMdialog = RegisterNUMbuilder.create();
                    okbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           makerequest(uniqueID,shopName,shopNumber,shopMail,archived,mPhone,String.valueOf(total),date.getText().toString());
                            RegisterNUMdialog.dismiss();
                        }
                    });
                    RegisterNUMdialog.show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Vous n'êtes pas connecté", Toast.LENGTH_SHORT).show();
        }


    }

    private void makerequest(String p1, String p2,String p3,String p4
            ,String p5,String p6,String p7,String p8) {

        String  tag_string_req1 = "string_req";
        StringRequest strReq1 = new StringRequest(com.android.volley.Request.Method.GET,
                "http://www.weebi.com/piscoandroid/insertinfoyup.php?idtab="+p1+"&nombout="+p2+"&numbout="+p3+"&mail="+p4+"" +
                        "&nomcli="+p5+"&numcli="+p6+"&total="+p7+"&datess="+p8
                , new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                 Log.d("", response.toString());
                 Toast.makeText(getApplicationContext(),""+response.toString(),Toast.LENGTH_LONG).show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PURCHASE, ""+response.toString());
                editor.commit();
                 Intent intent=new Intent(FacturesSelectSQL.this, Purchaseref.class);
                 //intent.putExtra("lepurchase",response.toString());
                 startActivity(intent);
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                //pDialog.hide();
                Toast.makeText(getApplicationContext(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
// Adding request to request queue
        WeebiApplication.getInstance().addToRequestQueue(strReq1, tag_string_req1);
    }

    public static class InternetConnection {

        /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                //Toast.makeText(context, "Connecté au "+activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_factures_select;
    }

    private void imprimer() {

        int lesolde = Integer.valueOf(solde);

        Cursor cursor = dbHelper.getBoutique("1");
        if (cursor.moveToFirst() == true){
            shopNumber = cursor.getString(1);
            shopName = cursor.getString(2);
        }

        if (isPreferenceFileExist("regoprintdemo",getApplicationContext())) {


            SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
            String nameBT = prefs.getString("BT_DEVICE_NAME", null);

            if (nameBT.contains("58")) {
                textToBePrinted = printCustomerTotalInvoiceHeaderFor58Mm(shopName, shopNumber, archived, mPhone, lesolde, total, soldeaftercalc);
                for (int i = 0; i < listinv.size(); i++) {
                    String nom = listinv.get(i).getName();
                    String prixunit = String.valueOf(listinv.get(i).getUnitPrice());
                    String quantite = String.valueOf(listinv.get(i).getQuantity());
                    textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                }
                textToBePrinted += printTotalAmountFor58Mm(total);
                textToBePrinted += printThankMsgFor58Mm();

            } else if (nameBT.contains("80")) {
                textToBePrinted = printCustomerTotalInvoiceHeaderFor80Mm(shopName, shopNumber, archived, mPhone, lesolde, total, soldeaftercalc);
                for (int i = 0; i < listinv.size(); i++) {
                    String nom = listinv.get(i).getName();
                    String prixunit = String.valueOf(listinv.get(i).getUnitPrice());
                    String quantite = String.valueOf(listinv.get(i).getQuantity());
                    textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                }
                textToBePrinted += printTotalAmountFor80Mm(total);
                textToBePrinted += printThankMsgFor80Mm();
            }

        } else {

            textToBePrinted = printCustomerTotalInvoiceHeaderFor58Mm(shopName, shopNumber, archived, mPhone, lesolde, total, soldeaftercalc);
            for (int i = 0; i < listinv.size(); i++) {
                String nom = listinv.get(i).getName();
                String prixunit = String.valueOf(listinv.get(i).getUnitPrice());
                String quantite = String.valueOf(listinv.get(i).getQuantity());
                textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
            }
            textToBePrinted += printTotalAmountFor58Mm(total);
            textToBePrinted += printThankMsgFor58Mm();
        }

        Log.v("TAG","Selected Invoice"+textToBePrinted);

        if (!TextUtils.isEmpty(textToBePrinted)) {
            Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
            printer.putExtra("text",textToBePrinted);
            startActivity(printer );
        }
    }

    private void saveInvoiceAsImage(boolean isSent) {


        linearLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = linearLayout.getDrawingCache();


//        Bitmap bitmap = mSecRootLayout.getRootView().getDrawingCache();
//        Bitmap bitmap = getBitmapFromView(this.getWindow().getDecorView().findViewById(R.id.mserootlayout).getRootView());

/*
        Bitmap bm1= getBitmapFromView(this.getWindow().getDecorView().findViewById(R.id.mserootlayout).getRootView());*/
//        mSecRootLayout.setDrawingCacheEnabled(true);
//        Bitmap bm1= getBitmapFromView(mSecRootLayout.getRootView());
//        Bitmap bitmap =TrimBitmap(bm1);

        File file, f = null;
        String filePath = null;
        int compteur = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), "Weebi/Factures");
            compteur = compteur + 1;
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath = file.getAbsolutePath() + File.separator + "TOTAL_FACTURES" + compteur + ".png";
            f = new File(filePath);
        }
        FileOutputStream ostream = null;
        try {
            ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();

            // Todo Share the image generated
//            if (isSent == true) {
            shareGeneratedInvoice(filePath);
//            } else {
//                //TODO Should print the invoice here man
//                doPhotoPrint(bitmap);
//            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void shareGeneratedInvoice(String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(new File(path));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Envoyer"));
    }

    private void shareString(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Envoyer"));
    }




    private class InvoiceItemDetailsAdapter extends BaseAdapter {

        private List<Item> items = null;

        public InvoiceItemDetailsAdapter(List<Item> itemList) {
            items = itemList;
        }

        @Override
        public int getCount() {
            if (items != null)
                return items.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.invoice_item_details_fact_select, parent, false);
            }
            TextView itemdate = (TextView) convertView.findViewById(R.id.itemdate);
            TextView itemTitleView = (TextView) convertView.findViewById(R.id.item_title);
            TextView itemPriceView = (TextView) convertView.findViewById(R.id.item_unit_price);
            TextView itemQuantityView = (TextView) convertView.findViewById(R.id.item_quantity);

            itemTitleView.setText(items.get(i).getName());
            itemPriceView.setText(formaterSolde(items.get(i).getUnitPrice()));
            itemQuantityView.setText(String.valueOf(items.get(i).getQuantity()));
            itemdate.setText("Facture du : "+items.get(i).getDateinv());


            return convertView;
        }
    }
}
