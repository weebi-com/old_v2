package com.weebinatidi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.Boutique;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.Invoice;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.Item;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.OperationClientRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCustomerInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCustomerTotalInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printItemFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printThankMsgFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printTotalAmountFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCustomerInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCustomerTotalInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printItemFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printThankMsgFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printTotalAmountFor80Mm;
import static com.weebinatidi.ui.print.PrintDemo.textToBePrinted;
import static com.weebinatidi.utils.ValidatorUtils.cutAStringAndAddSpaceAfterText;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor58Mm;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor80Mm;

public class FacturesSelect extends BaseActivity {
    public static final String EXTRA_CLIENT_PHONE = "CLIENT_PHONE";
    public static final String EXTRA_CLIENT_ARCHIVE = "CLIENT_ARCHIVE";
    public InvoiceItemDetailsAdapter mAdapter;
    RealmQuery<ItemRepository> invoices;
    ArrayList<Item> lesitems = new ArrayList<Item>();
    ArrayList<Item> listinv = new ArrayList<>();
    int total = 0;
    int idfact = -1;
    String solde = "";
    String idfacte = "";
    private ArrayList<String> lesdatess = new ArrayList<>();
    private ArrayList<Integer> lesprisescon = new ArrayList<>();
    private ArrayList<Integer> lesidfactures = new ArrayList<>();
    private String lenom = "";
    private String lenum = "";
    private String mPhone;
    private String archived;
    private int invoiceId = -1;
    private Realm realm;
    //private DepositAdapter mAdapter;
    private ListView invoiceList;
    private ClientRepository thisClient;
    private TextView nom, num, totalee, soldeactu, totalfactselect, soldeafter;
    private Button imprime, partage, yup;
    private RelativeLayout linearLayout;
    private String textapartager = "";
    int soldeaftercalc=0;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper=new DbHelper(this);

        linearLayout = (RelativeLayout) findViewById(R.id.roote);
        //lesprisescon = getIntent().getIntegerArrayListExtra("lesiditems");
        lesidfactures = getIntent().getIntegerArrayListExtra("lesidfact");
        lesdatess = getIntent().getStringArrayListExtra("lesdates");
        solde = getIntent().getStringExtra("lesolde");
        // Toast.makeText(getApplicationContext(),""+solde,Toast.LENGTH_SHORT).show();

        //lenum=getIntent().getStringExtra("numcli");
        mPhone = getIntent().getStringExtra(EXTRA_CLIENT_PHONE);
        archived = getIntent().getStringExtra(EXTRA_CLIENT_ARCHIVE);

        // nom = (TextView) findViewById(R.id.client_name);
        // num = (TextView) findViewById(R.id.client_phone);
        soldeactu = (TextView) findViewById(R.id.soldeactu);
        totalfactselect = (TextView) findViewById(R.id.totalinvoiceselect);
        soldeafter = (TextView) findViewById(R.id.soldeafterdepot);
        //totalee = (TextView) findViewById(R.id.clienttotal);
        imprime = (Button) findViewById(R.id.print_invoice_action);
        yup = (Button) findViewById(R.id.yup);
        partage = (Button) findViewById(R.id.send_invoice_action);

        //lesprisescon=Integer.valueOf(lesprises);
        //mDepotList = (ListView) findViewById(R.id.listeselect);
        invoiceList = (ListView) findViewById(R.id.listeselect);
        realm = Realm.getDefaultInstance();


        mAdapter = new InvoiceItemDetailsAdapter(listinv);
        invoiceList.setAdapter(mAdapter);


        for (int i = 0; i < lesidfactures.size(); i++) {
            idfact = lesidfactures.get(i);
            // Toast.makeText(getApplicationContext(), "" + idfact, Toast.LENGTH_SHORT).show();
            RealmResults<InvoiceRepository> invoiceRepositories = realm.where(InvoiceRepository.class).findAll();
            InvoiceRepository p = invoiceRepositories.where().equalTo("id", idfact).findFirst();
            RealmList<ItemRepository> listitem = p.getItems();
            RealmResults<ItemRepository> resultsitem = listitem.where().findAll();
            //Toast.makeText(getApplicationContext(),""+idfacte,Toast.LENGTH_SHORT).show();
            for (ItemRepository iv : resultsitem) {
                Item item = new Item();
                item.setId(iv.getId());
                item.setName(iv.getName());
                item.setQuantity(iv.getQuantity());
                item.setUnitPrice(iv.getUnitPrice());
                item.setTotalPrice(iv.getTotalPrice());
                //invoice.setItems(lesitems);
                total = total + iv.getTotalPrice();
                listinv.add(item);
                mAdapter.notifyDataSetChanged();
            }

           /* for (int j = 0; j < listinv.size(); j++) {
                total = total + listinv.get(i).getTotalPrice();
            }*/
           // totalee.setText(formaterSolde(total));


        /*Iterator<String> iter = lesprises.iterator();
        while (iter.hasNext()) {
            String value = iter.next();
            //System.out.println( value );
            Toast.makeText(getApplicationContext(),""+value,Toast.LENGTH_SHORT).show();
        }*/

            getSupportActionBar().setTitle(archived + "  " + mPhone);
        }

        totalfactselect.setText("" + formaterSolde(total));
        int soldeent = Integer.valueOf(solde);
        soldeactu.setText("" + formaterSolde(soldeent));

        int totalent = Integer.valueOf(total);
        // soldeaftercalc = soldeent + totalent;
        soldeafter.setText("" + formaterSolde(soldeent));

        imprime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimer();
            }
        });


        partage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // saveInvoiceAsImage(true);
                int totals = total;
                int lesolde = Integer.valueOf(solde);

                String shopNumber = "";
                String shopName ="";
                Cursor cursor = dbHelper.getBoutique("1");
                if (cursor.moveToFirst() == true){
                    shopNumber = cursor.getColumnName(1);
                    shopName = cursor.getColumnName(2);
                }

                textToBePrinted = printCustomerTotalInvoiceHeaderFor80Mm(shopName, shopNumber, archived, mPhone,lesolde, soldeaftercalc);
                for (int i = 0; i < listinv.size(); i++) {
                    String nom = listinv.get(i).getName();
                    String prixunit = String.valueOf(listinv.get(i).getUnitPrice());
                    String quantite = String.valueOf(listinv.get(i).getQuantity());
                    textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                }
                textToBePrinted += printTotalAmountFor80Mm(total);
                textToBePrinted += printThankMsgFor80Mm();
                Log.i("Printing", "Printing for 80mm printer");
                shareString(textapartager);
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_factures_select;
    }

    private void imprimer() {
        //String total = thisInvoice.getTotal() + " "+MADEVISE;

        int totals = total;
        int lesolde = Integer.valueOf(solde);

        // Get the boutique information
        //Boutique boutique = realm.where(Boutique.class).findFirst();
        String shopNumber = "";
        String shopName ="";
        Cursor cursor = dbHelper.getBoutique("1");
        if (cursor.moveToFirst() == true){
            shopNumber = cursor.getColumnName(1);
            shopName = cursor.getColumnName(2);
        }

        SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
        String nameBT = prefs.getString("BT_DEVICE_NAME", null);

        if (nameBT.contains("58")) {
            textToBePrinted = printCustomerTotalInvoiceHeaderFor58Mm(shopName, shopNumber, archived, mPhone,lesolde, soldeaftercalc);
            for (int i = 0; i < listinv.size(); i++) {
                String nom = listinv.get(i).getName();
                String prixunit = String.valueOf(listinv.get(i).getUnitPrice());
                String quantite = String.valueOf(listinv.get(i).getQuantity());
                textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
            }
            textToBePrinted += printTotalAmountFor58Mm(total);
            textToBePrinted += printThankMsgFor58Mm();
            Log.i("Printing", "Pinting for 58mm paper");

        } else if (nameBT.contains("80")) {
            textToBePrinted = printCustomerTotalInvoiceHeaderFor80Mm(shopName, shopNumber, archived, mPhone,lesolde, soldeaftercalc);
            for (int i = 0; i < listinv.size(); i++) {
                String nom = listinv.get(i).getName();
                String prixunit = String.valueOf(listinv.get(i).getUnitPrice());
                String quantite = String.valueOf(listinv.get(i).getQuantity());
                textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
            }
            textToBePrinted += printTotalAmountFor80Mm(total);
            textToBePrinted += printThankMsgFor80Mm();
            Log.i("Printing", "Printing for 80mm printer");
        } else {
            Log.i("Not Printing", "No printer found !");
        }


        if (!TextUtils.isEmpty(textToBePrinted)) {
//                        //first print...
//                        mService.sendMessage(textToBePrinted+"\n", "GBK");
//                        //send again the message to print it again... TEST
//                        mService.sendMessage(textToBePrinted+"\n", "GBK");

            Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
            printer.putExtra("text",textToBePrinted);
            startActivity(printer);


           /* Intent printerIntent = new Intent(getApplicationContext(), com.weebinatidi.ui.print.PrintDemo.class);
            printerIntent.putExtra("goprint", "print");
                        //printerIntent.putExtra(EXTRA_INVOICE_ID,invoiceId);
                                //printerIntent.putExtra(EXTRA_ISARCHIVE,isArchive);
                                        //printerIntent.putExtra(EXTRA_IS_BILL_OR_DEPOSIT,true);

                                            startActivity(printerIntent);*/
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

    private class DepositAdapter extends BaseAdapter {

        private List<OperationClientRepository> mOperationClients = null;

        public DepositAdapter(List<OperationClientRepository> list) {
            mOperationClients = list;
        }

        @Override
        public int getCount() {
            if (mOperationClients != null)
                return mOperationClients.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return mOperationClients.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_deposit, parent, false);
            }

            TextView depotType = (TextView) convertView.findViewById(R.id.deposit);
            //TextView depotDate = (TextView) convertView.findViewById(R.id.deposit_date);
            TextView depotMontant = (TextView) convertView.findViewById(R.id.deposit_amount);
            //TextView id = (TextView) convertView.findViewById(R.id.id);


            int montant = mOperationClients.get(position).getMontant();

            InvoiceRepository invoice = thisClient.getOperationClients().get(position).getInvoiceRepository();

            //id.setText(String.valueOf(invoice.getId()));

            String lemontant = formaterSolde(montant);
            depotMontant.setText(getString(R.string.montant_egale) + String.valueOf(lemontant));
            depotType.setText(getString(R.string.facture_of)
                    + Config.getFormattedDate(mOperationClients.get(position).getDate()));


            return convertView;
        }
    }

    private class InvoiceListAdapter extends BaseAdapter {

        private List<Invoice> invoices = null;

        public InvoiceListAdapter(List<Invoice> invoiceList) {
            invoices = invoiceList;
        }

        @Override
        public int getCount() {
            if (invoices != null)
                return invoices.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return invoices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_invoice, parent, false);
            }

            TextView invoiceTitle = (TextView) convertView.findViewById(R.id.invoice_item_title);
            TextView invoiceTotal = (TextView) convertView.findViewById(R.id.invoice_item_total);
            TextView invoiceId = (TextView) convertView.findViewById(R.id.invoice_item_id);
            TextView invoiceDate = (TextView) convertView.findViewById(R.id.invoice_item_date);
            LinearLayout mDetailList = (LinearLayout) convertView.findViewById(R.id.invoice_details_list);

            //idfact=invoices.get(i).getId();
            invoiceId.setText(getString(R.string.invoice_number) + invoices.get(i).getId());
            invoiceTotal.setText(String.valueOf(formaterSolde(invoices.get(i).getTotal())));

            if (invoices.get(i).getClient() != null) {
                invoiceTitle.setText(invoices.get(i).getClient().getNom());
            } else {
                invoiceTitle.setText(getString(R.string.defaultclient));
            }

           /* if(invoices.get(i).getItems() == null)
            {
                convertView.setBackgroundColor(Color.BLUE);
            }*/

            invoiceDate.setText(Config.getFormattedDate(invoices.get(i).getDate()));
            ArrayList<Item> itemsid = new ArrayList<>();

            RealmQuery<InvoiceRepository> invoices = realm.where(InvoiceRepository.class).equalTo("id", idfact);
            RealmResults<InvoiceRepository> invoicess = invoices.findAll();
            for (InvoiceRepository iv : invoicess) {
                Invoice invoice = new Invoice();
                invoice.setId(iv.getId());
                invoice.setDate(iv.getDate());
                invoice.setTotal(iv.getTotal());

                RealmList<ItemRepository> listitem = iv.getItems();
                RealmResults<ItemRepository> resultsitem = listitem.where().findAll();

                for (ItemRepository it : resultsitem) {
                    Item item = new Item();
                    item.setId(it.getId());
                    item.setName(it.getName());
                    item.setQuantity(it.getQuantity());
                    item.setUnitPrice(it.getUnitPrice());
                    item.setTotalPrice(it.getTotalPrice());
                    itemsid.add(item);

                    if (itemsid != null && itemsid.size() > 0) {
                        for (int j = 0; j < itemsid.size(); j++) {

                            View view = getLayoutInflater().inflate(R.layout.invoice_item_details, mDetailList, false);

                            TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
                            TextView itemPriceView = (TextView) view.findViewById(R.id.item_unit_price);
                            TextView itemQuantityView = (TextView) view.findViewById(R.id.item_quantity);

                            itemTitleView.setText(itemsid.get(j).getName());
                            itemPriceView.setText(String.valueOf(itemsid.get(j).getUnitPrice()));
                            itemQuantityView.setText(String.valueOf(itemsid.get(j).getQuantity()));

                            mDetailList.addView(view);
                        }
                    }
                }

            }


            return convertView;
        }
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

            TextView itemTitleView = (TextView) convertView.findViewById(R.id.item_title);
            TextView itemPriceView = (TextView) convertView.findViewById(R.id.item_unit_price);
            TextView itemQuantityView = (TextView) convertView.findViewById(R.id.item_quantity);

            itemTitleView.setText(items.get(i).getName());
            itemPriceView.setText(formaterSolde(items.get(i).getUnitPrice()));
            itemQuantityView.setText(String.valueOf(items.get(i).getQuantity()));


            return convertView;
        }
    }
}
