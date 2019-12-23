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

import com.squareup.picasso.Picasso;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.ArchiveInvoiceRepository;
import com.weebinatidi.model.Boutique;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.Depots;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.OperationClientRepository;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.ClientPageActivityNew2SQL;
import com.weebinatidi.ui.ImprimeText;

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
import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCashInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCustomerInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printItemFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printThankMsgFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printTotalAmountFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printTotalDepotAmountFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCashInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCustomerInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printItemFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printThankMsgFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printTotalAmountFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printTotalDepotAmountFor80Mm;
import static com.weebinatidi.ui.print.PrintDemo.textToBePrinted;
import static com.weebinatidi.utils.SharedPrefUtils.isPreferenceFileExist;
import static com.weebinatidi.utils.ValidatorUtils.cutAStringAndAddSpaceAfterText;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor58Mm;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor80Mm;

public class DepotDetailsActivitySQL extends BaseActivity {

    public static final String EXTRA_INVOICE_ID = "INVOICE_ID";

    public static final String TAG = DepotDetailsActivitySQL.class.getSimpleName();
    public String MADEVISE = "";
    ArchiveInvoiceRepository tmparch;
    Boutique boutique;
    AlertDialog.Builder RegisterTicketbuilder;
    AlertDialog RegisterTicketDialog;
    AlertDialog.Builder RegisterSupbuilder;
    AlertDialog RegisterSupDialog;
    private LinearLayout mDetailList;
    private RelativeLayout mRootLayout;
    private LinearLayout mSecRootLayout;
    private InvoiceRepository thisInvoice;
    private InvoiceItemDetailsAdapter mAdapter;
    private boolean isArchive;
    private Button menu, corbeil, imprime, partage;
    private String textapartager = "";
    private List<Depots> items=new ArrayList<>();

    DbHelper dbHelper;
    private String recupinvoiceId, recupinvoiceId1, recupinvoiceId2;
    private int invoiceId;
    private int idclient;
    private String nomcli, numcli, total, date, typed;
    private int lesolde;

    String nomboutique="";
    String numboutique="";
    //TODO Add an icont button to offer the possibility to delete an invoice... Q15

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        /*if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }*/

        dbHelper=new DbHelper(this);

        recupinvoiceId1 = getIntent().getStringExtra(NewInterface.EXTRA_MESSAGE);
        recupinvoiceId2 = getIntent().getStringExtra(ClientPageActivityNew2SQL.EXTRA_MESSAGE);



        if(!recupinvoiceId1.equals("")){
            recupinvoiceId = recupinvoiceId1;
        }

        if (!recupinvoiceId2.equals("")){
            recupinvoiceId=recupinvoiceId2;
        }

        invoiceId=Integer.valueOf(recupinvoiceId);
        //Toast.makeText(getApplicationContext(),"l'id "+recupinvoiceId,Toast.LENGTH_LONG).show();

        menu = findViewById(R.id.menu);
        corbeil = findViewById(R.id.delete_invoice_action);
        imprime = findViewById(R.id.print_invoice_action);
        partage = findViewById(R.id.partage);

        Cursor cursor=dbHelper.getDepot(invoiceId);
        if(cursor.moveToFirst()==true){
            /*Toast.makeText(getApplicationContext(),""+cursor.getString(0)+"\n"
                    +cursor.getString(1)+"\n"+cursor.getString(2)+"\n"
                    +cursor.getString(3)+"\n",Toast.LENGTH_LONG).show();*/
            total=cursor.getString(1);
            date=cursor.getString(3);

            //numcli=cursor.getString(3);
            //Toast.makeText(getApplicationContext(),""+total+"\n" +date,Toast.LENGTH_LONG).show();
        }

        Cursor bouti=dbHelper.getBoutique("1");
        if(bouti.moveToFirst()==true) {
            //Toast.makeText(NewInterface.this, "\n" + bouti.getString(1)+"\n" + bouti.getString(2), Toast.LENGTH_SHORT).show();
            nomboutique = bouti.getString(2);
            numboutique = bouti.getString(1);
        }


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
                    lesolde=Integer.valueOf(cur.getString(3));
                }

                textToBePrinted = printCustomerInvoiceHeaderFor80Mm(nomboutique, numboutique, nomcli, numcli,date,lesolde);
                textToBePrinted += printTotalDepotAmountFor80Mm(Integer.parseInt(total));
                textToBePrinted += printThankMsgFor80Mm();
                shareString(textToBePrinted);
            }
        });



        mDetailList = findViewById(R.id.invoice_details_list);
        mRootLayout = findViewById(R.id.root);
        //l'erreur ici ne fait pas bugger l'application
        mSecRootLayout = findViewById(R.id.mserootlayout);
        setupHeader();

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
                Log.d(TAG, "print invoice and send it");
                break;
            case R.id.send_invoice_action:
                break;
            //TODO Delete invoice and back it up on archive Done....
            case R.id.delete_invoice_action:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void imprimer(){
        {
            Cursor cur=dbHelper.getClient(numcli);
            if(cur.moveToFirst()==true){
                lesolde=Integer.valueOf(cur.getString(3));
            }

            if (isPreferenceFileExist("regoprintdemo",getApplicationContext())) {

                SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
                String nameBT = prefs.getString("BT_DEVICE_NAME", null);
                if (nameBT.contains("58")) {
                    textToBePrinted = printCustomerInvoiceHeaderFor58Mm(nomboutique, numboutique, nomcli, numcli,date,lesolde);
                    textToBePrinted += printTotalDepotAmountFor58Mm(Integer.parseInt(total));
                    textToBePrinted += printThankMsgFor58Mm();
                } else if (nameBT.contains("80")) {
                    textToBePrinted = printCustomerInvoiceHeaderFor80Mm(nomboutique, numboutique, nomcli, numcli,date,lesolde);
                    textToBePrinted += printTotalDepotAmountFor80Mm(Integer.parseInt(total));
                    textToBePrinted += printThankMsgFor80Mm();
                }

            } else {
                textToBePrinted = printCustomerInvoiceHeaderFor58Mm(nomboutique, numboutique, nomcli, numcli,date,lesolde);
                textToBePrinted += printTotalDepotAmountFor58Mm(Integer.parseInt(total));
                textToBePrinted += printThankMsgFor58Mm();
                Log.v("Printing","Depotsit"+textToBePrinted);
            }

            if (!TextUtils.isEmpty(textToBePrinted)) {
                Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
                printer.putExtra("text",textToBePrinted);
                startActivity(printer );
            }
        }
    }



    private void shareString(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Envoyer"));
    }

    /**/


    public void ProcessToClientDeposit(Realm realms, ClientRepository client, String soldes) {
        // Add the transaction to the deposit
        OperationClientRepository depot = realms.createObject(OperationClientRepository.class);
        depot.setClient(client);
        depot.setMontant(Integer.valueOf(soldes));
        depot.setDate(Calendar.getInstance().getTime());
        depot.setInvoice(false);
        //********************TEMP********************//

        int invoicegoid = Config.getLastInvoiceId(DepotDetailsActivitySQL.this) + 1;
//                            InvoiceRepository invoicedepot= new InvoiceRepository();
        InvoiceRepository invoicedepot = new InvoiceRepository();
        invoicedepot.setId(invoicegoid);
        Config.setLastInvoiceId(DepotDetailsActivitySQL.this, invoicegoid);
        invoicedepot.setDate(Calendar.getInstance().getTime());
        invoicedepot.setTotal(Integer.valueOf(soldes));
        invoicedepot.setClient(client);
        invoicedepot.setInvoiceRepo(false);

        int itemgoid = Config.getLastItemId(DepotDetailsActivitySQL.this) + 1;
        ItemRepository itemdepot = new ItemRepository();
        itemdepot.setId(itemgoid);
        Config.setLastItemId(DepotDetailsActivitySQL.this, itemgoid);
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
        PrintHelper photoPrinter = new PrintHelper(DepotDetailsActivitySQL.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.droids);
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
        //RealmQuery<Boutique> query = realm.where(Boutique.class);
        //boutique = query.findFirst();

        Cursor boutique=dbHelper.getBoutique("1");

        TextView invoiceToView = findViewById(R.id.invoice_to);
        TextView invoiceTotype = findViewById(R.id.invoice_to_dep);
        TextView invoiceTotypeview = findViewById(R.id.invoice_todep);
        TextView invoiceFromView = findViewById(R.id.invoice_from);
        TextView invoiceDateView = findViewById(R.id.invoice_date);
        TextView invoiceIdView = findViewById(R.id.invoice_id);
        TextView invoicePriceView = findViewById(R.id.invoice_totalprice);
        ImageView invoiceimgPriceView = findViewById(R.id.detailboutikimage);

        String imgurl = Config.ReadFromBase(DepotDetailsActivitySQL.this, Config.Boutik_url_photo, MODE_PRIVATE);
        if (!TextUtils.isEmpty(imgurl)) {
            Picasso.with(DepotDetailsActivitySQL.this).load(new File(imgurl)).resize(200, 200).centerInside().error(R.drawable.purchase_order_copie).into(invoiceimgPriceView);
            //Log.d(" image yet!!!!!!!!!!!!!!"," i "+imgurl);
        } else {
            Log.d("no image yet", "no iage yet");
        }
        float prixtotal = 0;
        invoiceIdView.setText(String.valueOf(invoiceId));

//        if(thisInvoice.getItems().size() >1)
//        {
//
//        }
//        else
//        {
//            prixtotal=thisInvoice.getItems().get(0).getUnitPrice()*thisInvoice.getItems().get(0).getQuantity();
//        }
//        invoiceFromView.setText("xxx");
        /*if (boutique.moveToFirst()==true) {
            if (!TextUtils.isEmpty(boutique.getString(1)) && !TextUtils.isEmpty(boutique.getString(2))) {
                invoiceFromView.setText(boutique.getString(1) + "  :  " + boutique.getString(2));

            }
        } else {
            //Snackbar.make(invoiceFromView, getString(R.string.remembertosetupyourshopinfo), Snackbar.LENGTH_LONG).show();
        }*/
        if (boutique.moveToFirst()) {
            if (!TextUtils.isEmpty(boutique.getString(2)) && !TextUtils.isEmpty(boutique.getString(1))) {
                invoiceFromView.setText(boutique.getString(2) + "  :  " + boutique.getString(1));

            }
        } else {
            //Snackbar.make(invoiceFromView, getString(R.string.remembertosetupyourshopinfo), Snackbar.LENGTH_LONG).show();
        }
        Cursor cursor=dbHelper.getDepot(invoiceId);
        if(cursor.moveToFirst()==true){
            /*Toast.makeText(getApplicationContext(),""+cursor.getString(0)+"\n"
                    +cursor.getString(1)+"\n"+cursor.getString(2)+"\n"
                    +cursor.getString(3)+"\n",Toast.LENGTH_LONG).show();*/
            total=cursor.getString(1);
            date=cursor.getString(2);
            typed=cursor.getString(3);
            idclient=cursor.getInt(4);
            invoiceDateView.setText(date);
            invoicePriceView.setText(total);
            invoiceTotypeview.setText(typed);

            Cursor cli=dbHelper.getClient(String.valueOf(idclient));
            if(cli.moveToFirst()==true){

                invoiceToView.setText(cli.getString(2)+"   "+cli.getString(1));
                nomcli=cli.getString(1);
                numcli=cli.getString(0);
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
        return R.layout.activity_invoice_details_depsql;
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

            TextView itemTitleView = convertView.findViewById(R.id.item_title);
            TextView itemPriceView = convertView.findViewById(R.id.item_unit_price);
            TextView itemQuantityView = convertView.findViewById(R.id.item_quantity);

            itemTitleView.setText(items.get(i).getName());
            itemPriceView.setText(String.valueOf(items.get(i).getUnitPrice()));
            itemQuantityView.setText(String.valueOf(items.get(i).getQuantity()));

            return convertView;
        }
    }

}
