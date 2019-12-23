package com.weebinatidi.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.squareup.picasso.Picasso;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.ArchiveInvoiceRepository;
import com.weebinatidi.model.Boutique;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.OperationClientRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;
import static com.weebinatidi.ui.print.PrintDemo.textToBePrinted;

public class InvoiceDetailsActivity extends BaseActivity {

    public static final String EXTRA_INVOICE_ID = "INVOICE_ID";
    public static final String EXTRA_ISARCHIVE = "ISARCHIVE";
    public static final String EXTRA_PRINTINVOICE = "goprint";
    public static final String EXTRA_IS_BILL_OR_DEPOSIT = "isabilloradeposit";
    public static final String TAG = InvoiceDetailsActivity.class.getSimpleName();
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
    private int invoiceId;
    private Realm realm;
    private InvoiceRepository thisInvoice;
    private InvoiceItemDetailsAdapter mAdapter;
    private boolean isArchive;
    private Button menu, corbeil, imprime, partage;
    private String textapartager = "";

    //TODO Add an icont button to offer the possibility to delete an invoice... Q15

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }

        invoiceId = getIntent().getExtras().getInt(EXTRA_INVOICE_ID);
        //invoiceId=getIntent().getExtras().getInt("lid");
        isArchive = getIntent().getExtras().getBoolean(EXTRA_ISARCHIVE);
        realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);
        menu = (Button) findViewById(R.id.menu);
        corbeil = (Button) findViewById(R.id.delete_invoice_action);
        imprime = (Button) findViewById(R.id.print_invoice_action);
        partage = (Button) findViewById(R.id.partage);


        // Toast.makeText(getApplicationContext(),"l'id"+invoiceId,Toast.LENGTH_LONG).show();


        corbeil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterSupbuilder = new AlertDialog.Builder(v.getContext());
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
                        supprimer();
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
                int total = thisInvoice.getTotal();
                Boutique boutique = realm.where(Boutique.class).findFirst();

                textToBePrinted =
                        ((boutique != null && !TextUtils.isEmpty(boutique.getNom())) ?
                                boutique.getNom() : " ...") +
                                getString(R.string.shop_call) + ((boutique != null && !TextUtils.isEmpty(boutique.getNumero()))
                                ? boutique.getNumero() : "...") +
                                (thisInvoice.getClient() != null ?
                                        getString(R.string.client_name) + thisInvoice.getClient().getNom() + getString(R.string.client_call) + thisInvoice.getClient().getNumero() : getString(R.string.vente)) +
                                getString(R.string.date) + Config.getFormattedDate(thisInvoice.getDate()) + getString(R.string.soldeimpression) + " " + formaterSolde(thisInvoice.getClient().getSolde()) +
                                getString(R.string.ligne);
                //getString(R.string.reference_unit_total);

                for (int i = 0; i < thisInvoice.getItems().size(); i++) {
                    String nom = thisInvoice.getItems().get(i).getName();
                    int prixunit = thisInvoice.getItems().get(i).getUnitPrice();
                    int quantite = thisInvoice.getItems().get(i).getQuantity();
                    String space = "";
                    nom = ((nom.length() <= 16) ? nom : nom.substring(0, 16));
                    for (int p = 0; p <= 17 - nom.length(); p++) {
                        space += " ";
                    }
                    textToBePrinted +=
                            "\n " + nom + space + " " + quantite + " x " + formaterSolde(prixunit);
                }
                textToBePrinted += getString(R.string.espace_total) + formaterSolde(total);
                textToBePrinted += getString(R.string.merci);

                shareString(textToBePrinted);
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrir();
            }
        });


        // Realm realm = Realm.getDefaultInstance();
        /*RealmQuery<Boutique> query = realm.where(Boutique.class);
        Boutique tmp = query.findFirst();

        if(tmp==null){
           // Toast.makeText(getApplicationContext(),"veuillez entrer votre devise merci",Toast.LENGTH_LONG).show();
        }
        else {
            String ladevise=tmp.getDevise().toString();
            MADEVISE=ladevise.toUpperCase();//mettre en majuscule
        }*/

        //MADEVISE=getIntent().getStringExtra("ladevise");

        if (isArchive) {
//            setTitle("Facture archivées");
            setTitle(getString(R.string.deleteinvoices));
        } else {
            setTitle(getString(R.string.details));
        }


        if (isArchive) {
            tmparch = realm.where(ArchiveInvoiceRepository.class).equalTo("id", invoiceId).findFirst();
            thisInvoice = new InvoiceRepository();
            thisInvoice.setTotal(tmparch.getTotal());
            thisInvoice.setItems(tmparch.getItems());
            thisInvoice.setClient(tmparch.getClient());
            thisInvoice.setDate(tmparch.getDate());
        } else {
            thisInvoice = realm.where(InvoiceRepository.class).equalTo("id", invoiceId).findFirst();
            //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
        }

        mDetailList = (LinearLayout) findViewById(R.id.invoice_details_list);
        mRootLayout = (RelativeLayout) findViewById(R.id.root);
        //l'erreur ici ne fait pas bugger l'application
        mSecRootLayout = (LinearLayout) findViewById(R.id.mserootlayout);


        setupHeader();

        Log.d("InvoiceDetailsActivity", "Invoice details = " + thisInvoice.getItems().size());
        if (thisInvoice.getItems() != null && thisInvoice.getItems().size() > 0) {
            for (int i = 0; i < thisInvoice.getItems().size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.invoice_item_details, mDetailList, false);

                TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
                TextView itemPriceView = (TextView) view.findViewById(R.id.item_unit_price);
                TextView itemQuantityView = (TextView) view.findViewById(R.id.item_quantity);

                itemTitleView.setText(thisInvoice.getItems().get(i).getName());
                itemPriceView.setText(String.valueOf(thisInvoice.getItems().get(i).getUnitPrice()));
                itemQuantityView.setText(String.valueOf(thisInvoice.getItems().get(i).getQuantity()));

                mDetailList.addView(view);
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.menu_invoice_detail, menu);

        MenuItem item = menu.findItem(R.id.delete_invoice_action);
        MenuItem itemprint = menu.findItem(R.id.print_invoice_action);
        MenuItem itemshare = menu.findItem(R.id.send_invoice_action);
        if (isArchive) {
            item.setTitle(getString(R.string.restore_invoice));
            item.setIcon(R.drawable.reuse);
            itemprint.setVisible(false);
            item.setVisible(false);
            itemshare.setVisible(false);
        } else {
            item.setTitle(getString(R.string.delete_invoice));
            item.setIcon(R.drawable.trash);
        }*/
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
                        supprimer();
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

        partager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveInvoiceAsImage(true);
                int total = thisInvoice.getTotal();

                // Get the boutique information
                Boutique boutique = realm.where(Boutique.class).findFirst();

                textToBePrinted =
                        ((boutique != null && !TextUtils.isEmpty(boutique.getNom())) ?
                                boutique.getNom() : " ...") +
                                getString(R.string.shop_call) + ((boutique != null && !TextUtils.isEmpty(boutique.getNumero()))
                                ? boutique.getNumero() : "...") +
                                (thisInvoice.getClient() != null ?
                                        getString(R.string.client_name) + thisInvoice.getClient().getNom() + getString(R.string.client_call) + thisInvoice.getClient().getNumero() : getString(R.string.vente)) +
                                getString(R.string.date) + Config.getFormattedDate(thisInvoice.getDate()) + getString(R.string.soldeimpression) + " " + formaterSolde(thisInvoice.getClient().getSolde()) +
                                getString(R.string.ligne);
                //getString(R.string.reference_unit_total);

                for (int i = 0; i < thisInvoice.getItems().size(); i++) {
                    String nom = thisInvoice.getItems().get(i).getName();
                    int prixunit = thisInvoice.getItems().get(i).getUnitPrice();
                    int quantite = thisInvoice.getItems().get(i).getQuantity();
                    String space = "";
                    nom = ((nom.length() <= 16) ? nom : nom.substring(0, 16));
                    for (int p = 0; p <= 17 - nom.length(); p++) {
                        space += " ";
                    }
                    textToBePrinted +=
                            "\n " + nom + space + " " + quantite + " x " + formaterSolde(prixunit);
                }
                textToBePrinted += getString(R.string.espace_total) + formaterSolde(total);
                textToBePrinted += getString(R.string.merci);
                shareString(textToBePrinted);
                RegisterTicketDialog.dismiss();
            }
        });

        RegisterTicketbuilder.setCancelable(true);
        RegisterTicketbuilder.setView(customView);
        // RegisterClientbuilder.setTitle(getString(R.string.ajouter_un_client));


        RegisterTicketDialog = RegisterTicketbuilder.create();
        RegisterTicketDialog.show();
    }

    private void imprimer() {
        //String total = thisInvoice.getTotal() + " "+MADEVISE;

        int total = thisInvoice.getTotal();

        // Get the boutique information
        Boutique boutique = realm.where(Boutique.class).findFirst();

        textToBePrinted =
                ((boutique != null && !TextUtils.isEmpty(boutique.getNom())) ?
                        boutique.getNom() : " ...") +
                        getString(R.string.shop_call) + ((boutique != null && !TextUtils.isEmpty(boutique.getNumero()))
                        ? boutique.getNumero() : "...") +
                        (thisInvoice.getClient() != null ?
                                getString(R.string.client_name) + thisInvoice.getClient().getNom() + getString(R.string.client_call) + thisInvoice.getClient().getNumero() : getString(R.string.vente)) +
                        getString(R.string.date) + Config.getFormattedDate(thisInvoice.getDate()) + getString(R.string.soldeimpression) + " " + formaterSolde(thisInvoice.getClient().getSolde()) +
                        getString(R.string.ligne);
        //getString(R.string.reference_unit_total);

        for (int i = 0; i < thisInvoice.getItems().size(); i++) {
            String nom = thisInvoice.getItems().get(i).getName();
            int prixunit = thisInvoice.getItems().get(i).getUnitPrice();
            int quantite = thisInvoice.getItems().get(i).getQuantity();
            String space = "";
            nom = ((nom.length() <= 16) ? nom : nom.substring(0, 16));
            for (int p = 0; p <= 17 - nom.length(); p++) {
                space += " ";
            }
            textToBePrinted +=
                    "\n " + nom + space + " " + quantite + " x " + formaterSolde(prixunit);
        }
        textToBePrinted += getString(R.string.espace_total) + formaterSolde(total);
        textToBePrinted += getString(R.string.merci);

        //      "\n Lentilles 500g     x2 - 2 000" +
        //    "\n 100g Piments secs  x2 -" +
        //   " 1 000" +
        //  "\n Total :             3 000 CFA" +
        ;

//                if(mService.isAvailable() == true)
//                {
        if (!TextUtils.isEmpty(textToBePrinted)) {
//                        //first print...
//                        mService.sendMessage(textToBePrinted+"\n", "GBK");
//                        //send again the message to print it again... TEST
//                        mService.sendMessage(textToBePrinted+"\n", "GBK");

            /*Intent printerIntent = new Intent(getApplicationContext(), com.weebinatidi.ui.print.PrintDemo.class);
            printerIntent.putExtra(EXTRA_PRINTINVOICE, "print");
            printerIntent.putExtra(EXTRA_INVOICE_ID, invoiceId);
            printerIntent.putExtra(EXTRA_ISARCHIVE, isArchive);
            printerIntent.putExtra(EXTRA_IS_BILL_OR_DEPOSIT, true);
            startActivity(printerIntent);*/
            Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
            printer.putExtra("text",textToBePrinted);
            startActivity(printer );
//                        com.weebipro.ui.print.PrintDemo.GoPrintit();
        }


    }

    private void shareString(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Envoyer"));
    }

    private void supprimer() {
        if (isArchive) {
            Log.d(TAG, "restoring invoice");
            //restore the invoice
            realm.beginTransaction();

            InvoiceRepository invoice = new InvoiceRepository();
            int invoiceIdtorestore = Config.getLastInvoiceId(InvoiceDetailsActivity.this) + 1;
            Config.setLastInvoiceId(InvoiceDetailsActivity.this, invoiceId);
            invoice.setId(invoiceIdtorestore);

            //just to make sure we are removing the archive invoice from there...
            ArchiveInvoiceRepository tmparch2 = realm.where(ArchiveInvoiceRepository.class).equalTo("id", invoiceId).findFirst();


            invoice.setDate(tmparch2.getDate());
//                            invoice.setClient(tmparch2.getClient());
            invoice.setItems(tmparch2.getItems());
            invoice.setTotal(tmparch2.getTotal());
            realm.copyToRealm(invoice);

            realm.commitTransaction();


            //remove the archiveinvoice from the main table...
            realm.beginTransaction();
//                            tmparch2.removeFromRealm();


            RealmResults<ArchiveInvoiceRepository> result = realm.where(ArchiveInvoiceRepository.class).equalTo("id", invoiceId).findAll();
            result.deleteAllFromRealm();

            realm.commitTransaction();



        } else {

            Log.d(TAG, "deleting invoice");
            //delete the invoice
            realm.beginTransaction();
            //first back up the invoice in the other table..
            //Copy the client to the Archives
            ArchiveInvoiceRepository archiveinvoice = realm.createObject(ArchiveInvoiceRepository.class);
//                    ArchiveInvoiceRepository archiveinvoice = new ArchiveInvoiceRepository();

//                    archiveinvoice.setId(realm.where(ArchiveInvoiceRepository.class).max("id").intValue() + 1);
            int key = Config.getLastArchiveInvoiceId(InvoiceDetailsActivity.this) + 1;
            Config.setLastArchiveInvoiceId(InvoiceDetailsActivity.this, key);
  /*                          try {
                                key = realm.where(ArchiveInvoiceRepository.class).max("id").intValue() + 1;
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                key = 0;
                            }
*/

            //no need to set id its already auto incrementing...
            archiveinvoice.setId(key);
            archiveinvoice.setDate(thisInvoice.getDate());
//                        archiveinvoice.setClient(thisInvoice.getClient());
            archiveinvoice.setItems(thisInvoice.getItems());
            archiveinvoice.setTotal(thisInvoice.getTotal());
//                    realm.copyToRealm(archiveinvoice);
            realm.commitTransaction();

            //TODO the task here is to restitute the amount of the invoice deleted as a depot for the client
            //TODO as if there was some cash and it been used once the invoice is deleted it(money used to pay the deleted invoice) should be returned to
            //TODO so we need to check if the invoice is linked to a client and if so restitute that money as a new depot for that..
            if (thisInvoice.getClient() != null) {

                //the best way to refund the user is to redine that back value as a new operation
                //so let's do this...

                realm.beginTransaction();

                ClientRepository client = thisInvoice.getClient();

                ProcessToClientDeposit(realm, client, String.valueOf(thisInvoice.getTotal()));


                realm.commitTransaction();


            }

            //remove the main operation where the invoice was stored too.
            OperationClientRepository operationClientRepository = realm.where(OperationClientRepository.class)
                    .equalTo("invoiceRepository.id", invoiceId).findFirst();
            if (operationClientRepository != null) {
                realm.beginTransaction();
//                                operationClientRepository.removeFromRealm();
                RealmResults<OperationClientRepository> result = realm.where(OperationClientRepository.class).equalTo("invoiceRepository.id", invoiceId).findAll();
                result.deleteAllFromRealm();
                realm.commitTransaction();
            }

            //just incase the invoice still there
            //remove the invoice from the main table...
            InvoiceRepository tmpInvoice = realm.where(InvoiceRepository.class).equalTo("id", invoiceId).findFirst();
            realm.beginTransaction();

//                            tmpInvoice.removeFromRealm();

            RealmResults<InvoiceRepository> result = realm.where(InvoiceRepository.class).equalTo("id", invoiceId).findAll();
            result.deleteAllFromRealm();

            realm.commitTransaction();



        }
    }


    public void ProcessToClientDeposit(Realm realms, ClientRepository client, String soldes) {
        // Add the transaction to the deposit
        OperationClientRepository depot = realms.createObject(OperationClientRepository.class);
        depot.setClient(client);
        depot.setMontant(Integer.valueOf(soldes));
        depot.setDate(Calendar.getInstance().getTime());
        depot.setInvoice(false);
        //********************TEMP********************//

        int invoicegoid = Config.getLastInvoiceId(InvoiceDetailsActivity.this) + 1;
//                            InvoiceRepository invoicedepot= new InvoiceRepository();
        InvoiceRepository invoicedepot = new InvoiceRepository();
        invoicedepot.setId(invoicegoid);
        Config.setLastInvoiceId(InvoiceDetailsActivity.this, invoicegoid);
        invoicedepot.setDate(Calendar.getInstance().getTime());
        invoicedepot.setTotal(Integer.valueOf(soldes));
        invoicedepot.setClient(client);
        invoicedepot.setInvoiceRepo(false);

        int itemgoid = Config.getLastItemId(InvoiceDetailsActivity.this) + 1;
        ItemRepository itemdepot = new ItemRepository();
        itemdepot.setId(itemgoid);
        Config.setLastItemId(InvoiceDetailsActivity.this, itemgoid);
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
        PrintHelper photoPrinter = new PrintHelper(InvoiceDetailsActivity.this);
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
        RealmQuery<Boutique> query = realm.where(Boutique.class);
        boutique = query.findFirst();

        TextView invoiceToView = (TextView) findViewById(R.id.invoice_to);
        TextView invoiceFromView = (TextView) findViewById(R.id.invoice_from);
        TextView invoiceDateView = (TextView) findViewById(R.id.invoice_date);
        TextView invoiceIdView = (TextView) findViewById(R.id.invoice_id);
        TextView invoicePriceView = (TextView) findViewById(R.id.invoice_totalprice);
        ImageView invoiceimgPriceView = (ImageView) findViewById(R.id.detailboutikimage);

        String imgurl = Config.ReadFromBase(InvoiceDetailsActivity.this, Config.Boutik_url_photo, MODE_PRIVATE);
        if (!TextUtils.isEmpty(imgurl)) {
            Picasso.with(InvoiceDetailsActivity.this).load(new File(imgurl)).resize(200, 200).centerInside().error(R.drawable.purchase_order_copie).into(invoiceimgPriceView);
            //Log.d(" image yet!!!!!!!!!!!!!!"," i "+imgurl);
        } else {
            Log.d("no image yet", "no iage yet");
        }
        float prixtotal = 0;
        invoiceIdView.setText(String.valueOf(thisInvoice.getId()));

//        if(thisInvoice.getItems().size() >1)
//        {
//
//        }
//        else
//        {
//            prixtotal=thisInvoice.getItems().get(0).getUnitPrice()*thisInvoice.getItems().get(0).getQuantity();
//        }
//        invoiceFromView.setText("xxx");
        if (boutique != null) {
            if (!TextUtils.isEmpty(boutique.getNom()) && !TextUtils.isEmpty(boutique.getNumero())) {
                invoiceFromView.setText(boutique.getNom() + "  :  " + boutique.getNumero());

            }
        } else {
            //Snackbar.make(invoiceFromView, getString(R.string.remembertosetupyourshopinfo), Snackbar.LENGTH_LONG).show();
        }

        invoiceDateView.setText(Config.getFormattedDate(thisInvoice.getDate()));
        invoicePriceView.setText("" + thisInvoice.getTotal());
        Log.d(TAG, " prixtotal =" + thisInvoice.getTotal());
        if (thisInvoice.getClient() != null) {
            invoiceToView.setText(thisInvoice.getClient().getNom() + " "
                    + thisInvoice.getClient().getNumero());

//            invoicePriceView.setText(""+thisInvoice.getTotal());
//            Log.d(TAG," prixtotal ="+thisInvoice.getTotal());
        } else {
            invoiceToView.setText(R.string.defaultclient);
//            invoicePriceView.setText(""+thisInvoice.getTotal());
        }

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_invoice_details;
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
