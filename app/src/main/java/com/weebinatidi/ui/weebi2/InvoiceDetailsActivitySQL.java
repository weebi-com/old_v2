package com.weebinatidi.ui.weebi2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.squareup.picasso.Picasso;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.ArchiveInvoiceRepository;
import com.weebinatidi.model.Boutique;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.OperationClientRepository;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.ClientPageActivityNew2SQL;
import com.weebinatidi.ui.ClientPagePortraitSql;
import com.weebinatidi.ui.Facturesportrait;
import com.weebinatidi.ui.ImprimeText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCashInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCustomerInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printItemFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printThankMsgFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printTotalAmountFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCashInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCustomerInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printItemFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printThankMsgFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printTotalAmountFor80Mm;
import static com.weebinatidi.ui.print.PrintDemo.textToBePrinted;
import static com.weebinatidi.utils.SharedPrefUtils.isPreferenceFileExist;
import static com.weebinatidi.utils.ValidatorUtils.cutAStringAndAddSpaceAfterText;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor58Mm;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor80Mm;

public class InvoiceDetailsActivitySQL extends BaseActivity {

    public static final String EXTRA_INVOICE_ID = "INVOICE_ID";
    //public static final String EXTRA_CLIENT_PHONE = "CLIENT_PHONE";

    public static final String TAG = InvoiceDetailsActivitySQL.class.getSimpleName();
    public String MADEVISE = "";
    ArchiveInvoiceRepository tmparch;
    Boutique boutique;
    private String leclient="";
    AlertDialog.Builder RegisterTicketbuilder;
    AlertDialog RegisterTicketDialog;
    AlertDialog.Builder RegisterSupbuilder;
    AlertDialog RegisterSupDialog;
    private LinearLayout mDetailList;
    private RelativeLayout mRootLayout;
    private LinearLayout mSecRootLayout;
    //private Realm realm;
    private InvoiceRepository thisInvoice;
    private InvoiceItemDetailsAdapter mAdapter;
    private boolean isArchive;
    private Button menu, corbeil, imprime, partage;
    private String textapartager = "";
    private List<Itemstring> items=new ArrayList<>();
    private int lesolde;
    DbHelper dbHelper;
    private String recupinvoiceId, recupinvoiceId1, recupinvoiceId2, recupinvoiceId3;
    private int invoiceId;
    private int idclient;
    private String nomcli, numcli, total, date, type, totale;
    String nomboutique="";
    String numboutique="";

    //TODO Add an icont button to offer the possibility to delete an invoice... Q15

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
       /* if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }*/
        //realm = Realm.getDefaultInstance();
        dbHelper=new DbHelper(this);

        recupinvoiceId1 = getIntent().getStringExtra(NewInterface.EXTRA_MESSAGE);
        recupinvoiceId2 = getIntent().getStringExtra(ClientPageActivityNew2SQL.EXTRA_MESSAGE);
        recupinvoiceId3 = getIntent().getStringExtra(Facturesportrait.EXTRA_MESSAGE);

        if(!recupinvoiceId1.equals("")){
            recupinvoiceId = recupinvoiceId1;
        }

        if (!recupinvoiceId2.equals("")){
            recupinvoiceId=recupinvoiceId2;
        }

        if(!recupinvoiceId1.equals("") || !recupinvoiceId2.equals("")){
            recupinvoiceId=recupinvoiceId3;
        }

        invoiceId=Integer.valueOf(recupinvoiceId);
       // Toast.makeText(getApplicationContext(),"l'id "+recupinvoiceId,Toast.LENGTH_LONG).show();

        menu = (Button) findViewById(R.id.menu);
        corbeil = (Button) findViewById(R.id.delete_invoice_action);
        imprime = (Button) findViewById(R.id.print_invoice_action);
        partage = (Button) findViewById(R.id.partage);

        Cursor cursor=dbHelper.getInvoicer(invoiceId);
        if(cursor.moveToFirst()==true){
            /*Toast.makeText(getApplicationContext(),""+cursor.getString(0)+"\n"
                    +cursor.getString(1)+"\n"+cursor.getString(2)+"\n"
                    +cursor.getString(3)+"\n",Toast.LENGTH_LONG).show();*/
            total=cursor.getString(2);
            date=cursor.getString(1);
            type=cursor.getString(4);
            //Toast.makeText(getApplicationContext(),""+total+"\n" +date,Toast.LENGTH_LONG).show();
            totale=total;
        }

        Cursor bouti=dbHelper.getBoutique("1");
        if(bouti.moveToFirst()==true) {
            //Toast.makeText(NewInterface.this, "\n" + bouti.getString(1)+"\n" + bouti.getString(2), Toast.LENGTH_SHORT).show();
             nomboutique = bouti.getString(2);
             numboutique = bouti.getString(1);
        }


        corbeil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterSupbuilder = new AlertDialog.Builder(v.getContext());
                View customView = getLayoutInflater().inflate(R.layout.dialog_delete_invoice, null, false);

                final TextView textView = (TextView) customView.findViewById(R.id.text);
                final FormEditText codever = (FormEditText) customView.findViewById(R.id.verifcodee);
                final LinearLayout numeriq=customView.findViewById(R.id.clavn);
                final ImageView okbtn = (ImageView) customView.findViewById(R.id.okdelinv);

                final Button espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;
                un=(Button)customView.findViewById(R.id.btnNum1Id);
                deux=(Button)customView.findViewById(R.id.btnNum2Id);
                trois=(Button)customView.findViewById(R.id.btnNum3Id);
                quatre=(Button)customView.findViewById(R.id.btnNum4Id);
                cinq=(Button)customView.findViewById(R.id.btnNum5Id);
                six=(Button)customView.findViewById(R.id.btnNum6Id);
                sept=(Button)customView.findViewById(R.id.btnNum7Id);
                huit=(Button)customView.findViewById(R.id.btnNum8Id);
                neuf=(Button)customView.findViewById(R.id.btnNum9Id);
                zero=(Button)customView.findViewById(R.id.btnNum0Id);
                cleardn=(Button)customView.findViewById(R.id.btnClearId);

                codever.setInputType(0);
                codever.setTransformationMethod(PasswordTransformationMethod.getInstance());

                codever.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        numeriq.setVisibility(View.VISIBLE);

                        zero.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+0);
                                }
                                else {
                                    codever.append(""+0);
                                }
                            }
                        });

                        un.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+1);
                                }
                                else {
                                    codever.append(""+1);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                        deux.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+2);
                                }
                                else {
                                    codever.append(""+2);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        trois.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+3);
                                }
                                else {
                                    codever.append(""+3);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        quatre.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+4);
                                }
                                else {
                                    codever.append(""+4);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        cinq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+5);
                                }
                                else {
                                    codever.append(""+5);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        six.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+6);
                                }
                                else {
                                    codever.append(""+6);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        sept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+7);
                                }
                                else {
                                    codever.append(""+7);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        huit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+8);
                                }
                                else {
                                    codever.append(""+8);
                                }


                            }
                        });

                        neuf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+9);
                                }
                                else {
                                    codever.append(""+9);
                                }

                            }
                        });

                        cleardn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();
                                if (chaine.equals("")){

                                }else {
                                    chaine = chaine.substring(0, chaine.length()-1);
                                    codever.setText(""+chaine);
                                }

                            }
                        });
                    }
                });

                RegisterSupbuilder.setCancelable(true);
                RegisterSupbuilder.setView(customView);
                RegisterSupbuilder.setTitle((getString(R.string.confirmer_suppressio_facture)));


                RegisterSupDialog = RegisterSupbuilder.create();


                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String lecode=codever.getText().toString();
                        String leboutiqcode="";
                        if(lecode.equals("")){
                            Toast.makeText(getApplicationContext(),"Merci de renseigner le code",Toast.LENGTH_SHORT).show();
                        }

                        if(!lecode.equals("")){
                            Cursor bouti=dbHelper.getBoutique("1");
                            if(bouti.moveToFirst()){
                                leboutiqcode=bouti.getString(4);
                                if(!leboutiqcode.equals(lecode)){
                                    Toast.makeText(getApplicationContext(),"Code incorrect ",Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(getApplicationContext(),"le code "+bouti.getString(3),Toast.LENGTH_SHORT).show();
                                if (leboutiqcode.equals(lecode)){
                                    Toast.makeText(getApplicationContext(),"Suppression effectué ",Toast.LENGTH_SHORT).show();

                                    String lid="";
                                    // String totale="";
                                    String typefact="";
                                    int solde=0;
                                    Cursor cursor=dbHelper.getInvoicer(invoiceId);
                                    if(cursor.moveToFirst()==true){

                                        //totale=cursor.getString(2);
                                        lid=cursor.getString(3);
                                        typefact=cursor.getString(4);

                                    }
                                    Cursor cur=dbHelper.getClient(String.valueOf(lid));
                                    if(type.equals("credit")){

                                        if(cur.moveToFirst()==true){
                                            /* Toast.makeText(getApplicationContext(),"\n"+invoiceId+"\n"+date+"\n"+total+"\n"+cur.getString(2)
                                                     +"\n"+date+"\n"+date,Toast.LENGTH_LONG).show();*/
                                            dbHelper.insertFactS(String.valueOf(invoiceId),date,totale,Config.getFormattedDate(Calendar.getInstance().getTime()),cur.getString(2),cur.getString(1),typefact);
                                            remettre();
                                            solde=Integer.valueOf(cur.getString(3));
                                            solde+=Integer.valueOf(totale);
                                            dbHelper.majsoldecli(lid, String.valueOf(solde));
                                            dbHelper.suppItemInvoice(invoiceId);
                                            dbHelper.suppInvoicer(invoiceId);
                                            Intent intent=new Intent(InvoiceDetailsActivitySQL.this, ClientPageActivityNew2SQL.class);
                                            intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ID, lid);
                                            startActivity(intent);
                                        }

                                    }

                                    if(type.equals("cash")){
                                        if(cur.moveToFirst()){
                                            dbHelper.insertFactS(String.valueOf(invoiceId),date,total,Config.getFormattedDate(Calendar.getInstance().getTime()),cur.getString(2),cur.getString(1),typefact);
                                            remettre();
                                            dbHelper.suppItemInvoice(invoiceId);
                                            dbHelper.suppInvoicer(invoiceId);
                                            Intent intent=new Intent(InvoiceDetailsActivitySQL.this, ClientPageActivityNew2SQL.class);
                                            intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ID, lid);
                                            startActivity(intent);
                                        }

                                    }

                                    if(type.equals("")){
                                        if(cur.moveToFirst()){
                                            dbHelper.insertFactS(String.valueOf(invoiceId),date,total,Config.getFormattedDate(Calendar.getInstance().getTime()),cur.getString(2),cur.getString(1),"");
                                            remettre();
                                            dbHelper.suppItemInvoice(invoiceId);
                                            dbHelper.suppInvoicer(invoiceId);
                                            Intent intent=new Intent(InvoiceDetailsActivitySQL.this, ClientPageActivityNew2SQL.class);
                                            intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ID, lid);
                                            startActivity(intent);
                                        }

                                    }

                                }
                            }
                        }

                    }
                });


                RegisterSupDialog.show();
            }
        });

        imprime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimer();
            }
        });

        partage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveInvoiceAsImage(true);
                // Get the boutique information
                //int total = thisInvoice.getTotal();
                //Boutique boutique = realm.where(Boutique.class).findFirst();

                Cursor cur=dbHelper.getClient(numcli);
                if(cur.moveToFirst()==true){
                    lesolde=Integer.valueOf(cur.getString(2));
                }

                textToBePrinted = printCashInvoiceHeaderFor80Mm(nomboutique, numboutique, nomcli, numcli,date,invoiceId);
                for (int i = 0; i < items.size(); i++) {
                    String nom = items.get(i).getName();
                    String prixunit = items.get(i).getUnitPrice();
                    String quantite = items.get(i).getQuantity();
                    textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                }
                textToBePrinted += printTotalAmountFor80Mm(Integer.parseInt(total));
                textToBePrinted += printThankMsgFor80Mm();
                shareString(textToBePrinted);
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrir();
            }
        });




        mDetailList = (LinearLayout) findViewById(R.id.invoice_details_list);
        mRootLayout = (RelativeLayout) findViewById(R.id.root);
        //l'erreur ici ne fait pas bugger l'application
        mSecRootLayout = (LinearLayout) findViewById(R.id.mserootlayout);
        setupHeader();



        Cursor cur=dbHelper.getItemIdInvoice(invoiceId);
        while (cur.moveToNext()==true){
            //Toast.makeText(getApplicationContext(),""+cur.getString(1),Toast.LENGTH_LONG).show();
            View view = getLayoutInflater().inflate(R.layout.invoice_item_details, mDetailList, false);

            TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
            TextView itemPriceView = (TextView) view.findViewById(R.id.item_unit_price);
            TextView itemQuantityView = (TextView) view.findViewById(R.id.item_quantity);
            TextView itemsoustotView = (TextView) view.findViewById(R.id.item_soustot);

            itemTitleView.setText(cur.getString(1));
            itemPriceView.setText(cur.getString(2));
            itemQuantityView.setText(cur.getString(3));
            itemsoustotView.setText(cur.getString(4));

            Itemstring item=new Itemstring();
            item.setName(cur.getString(1));
            item.setUnitPrice(cur.getString(2));
            item.setQuantity(cur.getString(3));
            item.setTotalPrice(cur.getString(4));
            items.add(item);

            mDetailList.addView(view);
        }



    }

    public void remettre(){

        for (int i = 0; i < items.size(); i++) {
            String nom = items.get(i).getName();
            String prixunit = items.get(i).getUnitPrice();
            String quantite = items.get(i).getQuantity();
            Double prix=Double.valueOf(prixunit);
            Double qte=Double.valueOf(quantite);
            Double tot=prix*qte;
            //Toast.makeText(getApplicationContext(),"les items "+"\n " + nom  + "" + " " + quantite + " x " + prixunit,Toast.LENGTH_LONG).show();
            dbHelper.insertItemsS(nom,prixunit,String.valueOf(tot),quantite,String.valueOf(invoiceId));
            double qteactu=0;
            Cursor cursor=dbHelper.getrefname(nom);
            if(cursor.moveToFirst()){
                //Toast.makeText(getApplicationContext(),""+cursor.getString(0),Toast.LENGTH_LONG).show();
                qteactu=Double.valueOf(cursor.getString(3));
                qteactu+=Double.valueOf(quantite);
                dbHelper.updateRefqte(String.valueOf(qteactu),"",cursor.getString(0),"remis apres suppression facture");
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.print_invoice_action:
                //TODO Add Signature to the invoice detail file Q14...
                //saveInvoiceAsImage(false);
                Log.d(TAG, "print invoice and send it");
                break;
            case R.id.send_invoice_action:
                saveInvoiceAsImage(true);
                break;
            //TODO Delete invoice and back it up on archive Done....
            case R.id.delete_invoice_action:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void ouvrir() {
        RegisterTicketbuilder = new AlertDialog.Builder(this);
        final View customView = getLayoutInflater().inflate(R.layout.dialog_menu_ticket, null, false);
        final Button supp = (Button) customView.findViewById(R.id.delete_invoice_action);
        final Button imprime = (Button) customView.findViewById(R.id.print_invoice_action);
        final Button partager = (Button) customView.findViewById(R.id.send_invoice_action);
        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //supprimer();
                RegisterSupbuilder = new AlertDialog.Builder(customView.getContext());
                View customView = getLayoutInflater().inflate(R.layout.dialog_delete_invoice, null, false);
                final TextView textView = (TextView) customView.findViewById(R.id.text);
                final ImageView okbtn = (ImageView) customView.findViewById(R.id.okdelinv);
                RegisterSupbuilder.setCancelable(true);
                RegisterSupbuilder.setView(customView);
                RegisterSupbuilder.setTitle((getString(R.string.confirmer_suppressio_facture)));
                RegisterSupDialog = RegisterSupbuilder.create();
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //supprimer();
                    }
                });
                RegisterSupDialog.show();
            }
        });

        imprime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimer();
            }
        });

    }


    private void imprimer() {
        Cursor cur=dbHelper.getClient(numcli);
        if(cur.moveToFirst()==true){
            lesolde=Integer.valueOf(cur.getString(3));
        }

        if (isPreferenceFileExist("regoprintdemo",getApplicationContext())) {

            SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
            String nameBT = prefs.getString("BT_DEVICE_NAME", null);

            if (nameBT.contains("58")) {
                textToBePrinted = printCashInvoiceHeaderFor58Mm(nomboutique, numboutique, nomcli, numcli, date, invoiceId);
                for (int i = 0; i < items.size(); i++) {
                    String nom = items.get(i).getName();
                    String prixunit = items.get(i).getUnitPrice();
                    String quantite = items.get(i).getQuantity();
                    textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                }
                textToBePrinted += printTotalAmountFor58Mm(Integer.parseInt(total));
                textToBePrinted += printThankMsgFor58Mm();
            } else if (nameBT.contains("80")) {
                textToBePrinted = printCashInvoiceHeaderFor80Mm(nomboutique, numboutique, nomcli, numcli, date, invoiceId);
                for (int i = 0; i < items.size(); i++) {
                    String nom = items.get(i).getName();
                    String prixunit = items.get(i).getUnitPrice();
                    String quantite = items.get(i).getQuantity();
                    textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                }
                textToBePrinted += printTotalAmountFor80Mm(Integer.parseInt(total));
                textToBePrinted += printThankMsgFor80Mm();
            }

        } else {

            textToBePrinted = printCashInvoiceHeaderFor58Mm(nomboutique, numboutique, nomcli, numcli, date, invoiceId);
            for (int i = 0; i < items.size(); i++) {
                String nom = items.get(i).getName();
                String prixunit = items.get(i).getUnitPrice();
                String quantite = items.get(i).getQuantity();
                textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
            }
            textToBePrinted += printTotalAmountFor58Mm(Integer.parseInt(total));
            textToBePrinted += printThankMsgFor58Mm();
        }
        Log.i("Printing", "Imprimer"+textToBePrinted);

        if (!TextUtils.isEmpty(textToBePrinted)) {
                 Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
            printer.putExtra("text",textToBePrinted);
            startActivity(printer);
        }
    }

    private void shareString(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Envoyer"));
    }


    public void ProcessToClientDeposit(Realm realms, ClientRepository client, String soldes) {
        // Add the transaction to the deposit
        OperationClientRepository depot = realms.createObject(OperationClientRepository.class);
        depot.setClient(client);
        depot.setMontant(Integer.valueOf(soldes));
        depot.setDate(Calendar.getInstance().getTime());
        depot.setInvoice(false);
        //********************TEMP********************//

        int invoicegoid = Config.getLastInvoiceId(InvoiceDetailsActivitySQL.this) + 1;
//                            InvoiceRepository invoicedepot= new InvoiceRepository();
        InvoiceRepository invoicedepot = new InvoiceRepository();
        invoicedepot.setId(invoicegoid);
        Config.setLastInvoiceId(InvoiceDetailsActivitySQL.this, invoicegoid);
        invoicedepot.setDate(Calendar.getInstance().getTime());
        invoicedepot.setTotal(Integer.valueOf(soldes));
        invoicedepot.setClient(client);
        invoicedepot.setInvoiceRepo(false);

        int itemgoid = Config.getLastItemId(InvoiceDetailsActivitySQL.this) + 1;
        ItemRepository itemdepot = new ItemRepository();
        itemdepot.setId(itemgoid);
        Config.setLastItemId(InvoiceDetailsActivitySQL.this, itemgoid);
        itemdepot.setName(getString(R.string.remboursement) + Config.getFormattedDate(Calendar.getInstance().getTime()));
//        itemdepot.setQuantity(1);
        itemdepot.setQuantity(Integer.valueOf(soldes));
//        itemdepot.setUnitPrice(Integer.valueOf(soldes));
//        itemdepot.setTotalPrice(Integer.valueOf(soldes));
        itemdepot = realms.copyToRealmOrUpdate(itemdepot);
        invoicedepot.getItems().add(itemdepot);
        invoicedepot = realms.copyToRealmOrUpdate(invoicedepot);
        depot.setInvoiceRepository(invoicedepot);


        //*********************END TEMP********************//
        // Add the depot to the client
        client.getOperationClients().add(depot);
        int newSolde = client.getSolde() + Integer.valueOf(soldes);
        client.setSolde(newSolde);
    }

    private void saveInvoiceAsImage(boolean isSent) {


        mSecRootLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = mSecRootLayout.getDrawingCache();


//        Bitmap bitmap = mSecRootLayout.getRootView().getDrawingCache();
//        Bitmap bitmap = getBitmapFromView(this.getWindow().getDecorView().findViewById(R.id.mserootlayout).getRootView());

/*
        Bitmap bm1= getBitmapFromView(this.getWindow().getDecorView().findViewById(R.id.mserootlayout).getRootView());*/
//        mSecRootLayout.setDrawingCacheEnabled(true);
//        Bitmap bm1= getBitmapFromView(mSecRootLayout.getRootView());
//        Bitmap bitmap =TrimBitmap(bm1);

        File file, f = null;
        String filePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), "Weebi/Factures");
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath = file.getAbsolutePath() + File.separator + "Facture_N_" + thisInvoice.getId() + ".png";
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


    public Bitmap getBitmapFromView(View v) {
        v.setLayoutParams(new AppBarLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.RGB_565);

        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }


    //this is to print the image we got ...

    private void doPhotoPrint(Bitmap bmp) {
        PrintHelper photoPrinter = new PrintHelper(InvoiceDetailsActivitySQL.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//      Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.droids);
        Bitmap bitmap = bmp;
        photoPrinter.printBitmap("weebibill.png - test print", bitmap);
    }


    //*****************************************************************************


    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

//    public static Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
//
//        int height = Math.min(MAX_HEIGHT, totalHeight);
//        float percent = height / (float)totalHeight;
//
//        Bitmap canvasBitmap = Bitmap.createBitmap((int)(totalWidth*percent),(int)(totalHeight*percent), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(canvasBitmap);
//
//        Drawable bgDrawable = view.getBackground();
//        if (bgDrawable != null)
//            bgDrawable.draw(canvas);
//        else
//            canvas.drawColor(Color.WHITE);
//
//        canvas.save();
//        canvas.scale(percent, percent);
//        view.draw(canvas);
//        canvas.restore();
//
//        return canvasBitmap;
//    }

    //*****************************************************************************

    private void shareGeneratedInvoice(String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(new File(path));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Envoyer"));
    }

    private void setupHeader() {

        //look for the shop in the database...
       // RealmQuery<Boutique> query = realm.where(Boutique.class);
       // boutique = query.findFirst();

        Cursor boutique=dbHelper.getBoutique("1");

        TextView invoiceToView = (TextView) findViewById(R.id.invoice_to);
        TextView invoiceFromView = (TextView) findViewById(R.id.invoice_from);
        TextView invoiceDateView = (TextView) findViewById(R.id.invoice_date);
        TextView invoiceIdView = (TextView) findViewById(R.id.invoice_id);
        TextView invoicePriceView = (TextView) findViewById(R.id.invoice_totalprice);
        ImageView invoiceimgPriceView = (ImageView) findViewById(R.id.detailboutikimage);

        String imgurl = Config.ReadFromBase(InvoiceDetailsActivitySQL.this, Config.Boutik_url_photo, MODE_PRIVATE);
        if (!TextUtils.isEmpty(imgurl)) {
            Picasso.with(InvoiceDetailsActivitySQL.this).load(new File(imgurl)).resize(200, 200).centerInside().error(R.drawable.purchase_order_copie).into(invoiceimgPriceView);
            //Log.d(" image yet!!!!!!!!!!!!!!"," i "+imgurl);
        } else {
            Log.d("no image yet", "no iage yet");
        }
        float prixtotal = 0;
        invoiceIdView.setText(String.valueOf(invoiceId));


        if (boutique.moveToFirst()) {
            if (!TextUtils.isEmpty(boutique.getString(2)) && !TextUtils.isEmpty(boutique.getString(1))) {
                invoiceFromView.setText(boutique.getString(2) + "  :  " + boutique.getString(1));

            }
        } else {
            //Snackbar.make(invoiceFromView, getString(R.string.remembertosetupyourshopinfo), Snackbar.LENGTH_LONG).show();
        }
        Cursor cursor=dbHelper.getInvoicer(invoiceId);
        if(cursor.moveToFirst()==true){
            /*Toast.makeText(getApplicationContext(),""+cursor.getString(0)+"\n"
                    +cursor.getString(1)+"\n"+cursor.getString(2)+"\n"
                    +cursor.getString(3)+"\n",Toast.LENGTH_LONG).show();*/
            total=cursor.getString(2);
            date=cursor.getString(1);
            idclient=cursor.getInt(3);
            invoiceDateView.setText(date);
            invoicePriceView.setText(total);

            Cursor cli=dbHelper.getClient(String.valueOf(idclient));
            if(cli.moveToFirst()==true){

                invoiceToView.setText(cli.getString(2)+"   "+cli.getString(1));
                nomcli=cli.getString(2);
                numcli=cli.getString(1);
            }
        }



        /*if (thisInvoice.getClient() != null) {
            invoiceToView.setText(thisInvoice.getClient().getNom() + " "
                    + thisInvoice.getClient().getNumero());

//            invoicePriceView.setText(""+thisInvoice.getTotal());
//            Log.d(TAG," prixtotal ="+thisInvoice.getTotal());
        } else {
            invoiceToView.setText(R.string.defaultclient);
//            invoicePriceView.setText(""+thisInvoice.getTotal());
        }*/

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_invoice_details_sql;
    }

    private class InvoiceItemDetailsAdapter extends BaseAdapter {

        private List<ItemRepository> items = null;

        public InvoiceItemDetailsAdapter(List<ItemRepository> itemList) {
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
                convertView = getLayoutInflater().inflate(R.layout.invoice_item_details, parent, false);
            }

            TextView itemTitleView = (TextView) convertView.findViewById(R.id.item_title);
            TextView itemPriceView = (TextView) convertView.findViewById(R.id.item_unit_price);
            TextView itemQuantityView = (TextView) convertView.findViewById(R.id.item_quantity);

            itemTitleView.setText(items.get(i).getName());
            itemPriceView.setText(String.valueOf(items.get(i).getUnitPrice()));
            itemQuantityView.setText(String.valueOf(items.get(i).getQuantity()));

            return convertView;
        }
    }

}