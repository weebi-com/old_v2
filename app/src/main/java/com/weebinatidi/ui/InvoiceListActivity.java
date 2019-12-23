package com.weebinatidi.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.InvoiceRepository;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;

public class InvoiceListActivity extends BaseActivity {

    public static final String TAG = InvoiceListActivity.class.getSimpleName();
    public InvoiceListAdapter mAdapter;
    boolean filterbydateascordesc = true;
    boolean filterbybalanceascordesc = true;
    RealmResults<InvoiceRepository> invoices;
    AlertDialog.Builder MenuClientbuilder;
    AlertDialog MenuClientDialog;
    private ListView invoiceList;
    private Realm realm;
    private Button menus, bybalance, bydateascordesc, delete_invoices;

    //TODO Add a section to show the delete invoices Q16. Done
    // TODO Add a filter to filter list of invoices by balance (In Desc or Asc Mode ) Done

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
        if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }

        bybalance = (Button) findViewById(R.id.filter_invoices_bybalance);
        bydateascordesc = (Button) findViewById(R.id.filter_invoices_bydateascordesc);
        delete_invoices = (Button) findViewById(R.id.show_delete_invoices);


        bybalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterbybalanceascordesc == true) {
                    Log.d(TAG, " filter invoices by balance DESCENDING");

                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "total", Sort.DESCENDING);

                    Log.d("total", " " + invoices.get(0).getTotal());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);

                } else {
                    Log.d(TAG, " filter invoices by balance ASCENDING");

                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "total", Sort.ASCENDING);

                    Log.d("total", " " + invoices.get(0).getTotal());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);
                }

                filterbybalanceascordesc = !filterbybalanceascordesc;
            }
        });

        bydateascordesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterbydateascordesc == true) {
                    Log.d(TAG, " sort by date DESCENDING");

                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "date", Sort.DESCENDING);

                    Log.d("date", invoices.get(0).getDate().toString());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);

                } else {
                    Log.d(TAG, " sort by date ASCENDING");
                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "date", Sort.ASCENDING);
                    Log.d("date", invoices.get(0).getDate().toString());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);
                }

                filterbydateascordesc = !filterbydateascordesc;
            }
        });

        delete_invoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoarchivedinvoices = new Intent(InvoiceListActivity.this, DeleteInvoicesListActivity.class);
                startActivity(gotoarchivedinvoices);
            }
        });


        invoiceList = (ListView) findViewById(R.id.invoice_list);

        menus = (Button) findViewById(R.id.menu);

        menus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menus();
            }
        });


        realm = Realm.getDefaultInstance();


        //TODO Sort by date already done but add by most recent to the latest... Q12 Done
//        invoices = realm.where(InvoiceRepository.class).findAll();
//        if(filterbydateascordesc)
//        {
//        invoices.sort("date", Sort.DESCENDING); // sort the list by date
//        }
//        else
//        {
//            invoices.sort("date", Sort.ASCENDING); // sort the list by date
//        }

        invoices = realm.where(InvoiceRepository.class).findAllSorted(
                "date", Sort.DESCENDING);


        //TODO  Add a filter to search invoices by date  the filter button will be shown on an actionbar Q13
        //should drop a realsearchview here instead...
        mAdapter = new InvoiceListAdapter(invoices);
        invoiceList.setAdapter(mAdapter);

        invoiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //if (invoices.get(i).isInvoiceRepo() == true) {
                    // show the invoice details.
                    InvoiceRepository invoice = (InvoiceRepository) adapterView.getAdapter().getItem(i);

                    Intent intent = new Intent(InvoiceListActivity.this, InvoiceDetailsActivity.class);
                    intent.putExtra(InvoiceDetailsActivity.EXTRA_INVOICE_ID, invoice.getId());
                    intent.putExtra(InvoiceDetailsActivity.EXTRA_ISARCHIVE, false);
                    startActivity(intent);
               // }

            }
        });

    }

    public void menus() {
        MenuClientbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_menu_invoice_liste, null, false);

        final Button balance = (Button) customView.findViewById(R.id.filter_invoices_bybalance);
        final Button dates = (Button) customView.findViewById(R.id.filter_invoices_bydateascordesc);
        final Button supinvoice = (Button) customView.findViewById(R.id.show_delete_invoices);


        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterbybalanceascordesc == true) {
                    Log.d(TAG, " filter invoices by balance DESCENDING");

                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "total", Sort.DESCENDING);

                    Log.d("total", " " + invoices.get(0).getTotal());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);

                } else {
                    Log.d(TAG, " filter invoices by balance ASCENDING");

                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "total", Sort.ASCENDING);

                    Log.d("total", " " + invoices.get(0).getTotal());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);
                }

                filterbybalanceascordesc = !filterbybalanceascordesc;
                MenuClientDialog.dismiss();
            }
        });

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterbydateascordesc == true) {
                    Log.d(TAG, " sort by date DESCENDING");

                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "date", Sort.DESCENDING);

                    Log.d("date", invoices.get(0).getDate().toString());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);

                } else {
                    Log.d(TAG, " sort by date ASCENDING");
                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "date", Sort.ASCENDING);
                    Log.d("date", invoices.get(0).getDate().toString());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);
                }

                filterbydateascordesc = !filterbydateascordesc;
                MenuClientDialog.dismiss();
            }
        });

        supinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoarchivedinvoices = new Intent(InvoiceListActivity.this, DeleteInvoicesListActivity.class);
                startActivity(gotoarchivedinvoices);
                MenuClientDialog.dismiss();
            }
        });

        MenuClientbuilder.setCancelable(true);
        MenuClientbuilder.setView(customView);
        // RegisterClientbuilder.setTitle(getString(R.string.ajouter_un_client));


        MenuClientDialog = MenuClientbuilder.create();
        MenuClientDialog.show();


    }


    @Override
    protected void onResume() {
        super.onResume();

//        //TODO Sort by date already done but add by most recent to the latest... Q12 Done
//        invoices = realm.where(InvoiceRepository.class).findAll();
////        if(filterbydateascordesc)
////        {
//        invoices.sort("date", Sort.DESCENDING); // sort the list by date
////        }
////        else
////        {
////            invoices.sort("date", Sort.ASCENDING); // sort the list by date
////        }
//
//
//        //TODO  Add a filter to search invoices by date  the filter button will be shown on an actionbar Q13
//        //should drop a realsearchview here instead...
//        mAdapter = new InvoiceListAdapter(invoices);
//        invoiceList.setAdapter(mAdapter);
//
//        invoiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if(invoices.get(i).isInvoiceRepo() == true)
//                {
//                    // show the invoice details.
//                    InvoiceRepository invoice = (InvoiceRepository) adapterView.getAdapter().getItem(i);
//
//                    Intent intent = new Intent(InvoiceListActivity.this, InvoiceDetailsActivity.class);
//                    intent.putExtra(InvoiceDetailsActivity.EXTRA_INVOICE_ID, invoice.getId());
//                    intent.putExtra(InvoiceDetailsActivity.EXTRA_ISARCHIVE,false);
//                    startActivity(intent);
//                }
//
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invoice_list, menu);

//        MenuItem itemdate=menu.findItem(R.id.filter_invoices_bydateascordesc);
//
//        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            //TODO Sort by date already done but add by most recent to the latest... Q12 Done
            /*case R.id.filter_invoices_bydateascordesc:
//                Log.d(TAG," sort by date");

                if(filterbydateascordesc == true)
                {
                    Log.d(TAG," sort by date DESCENDING");

                  invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "date", Sort.DESCENDING);

                    Log.d("date",invoices.get(0).getDate().toString());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);

                }
                else
                {
                    Log.d(TAG," sort by date ASCENDING");
                  invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "date", Sort.ASCENDING);
                    Log.d("date",invoices.get(0).getDate().toString());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);
                }

                filterbydateascordesc=!filterbydateascordesc;

                break;

            case R.id.show_delete_invoices:
                Log.d(TAG," show archived invoices");
                Intent gotoarchivedinvoices=new Intent(InvoiceListActivity.this,DeleteInvoicesListActivity.class);
                startActivity(gotoarchivedinvoices);
                break;

            case R.id.filter_invoices_bybalance:
//                filterbybalanceascordesc= true;
//                Log.d(TAG," filter invoices by balance");
//TODO replace with adequate filters to sort things up here...

                if(filterbybalanceascordesc == true)
                {
                    Log.d(TAG," filter invoices by balance DESCENDING");

                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "total", Sort.DESCENDING);

                    Log.d("total"," "+invoices.get(0).getTotal());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);

                }
                else
                {
                    Log.d(TAG," filter invoices by balance ASCENDING");

                    invoices = realm.where(InvoiceRepository.class).findAllSorted(
                            "total", Sort.ASCENDING);

                    Log.d("total"," "+invoices.get(0).getTotal());

                    mAdapter = new InvoiceListAdapter(invoices);
                    invoiceList.setAdapter(mAdapter);
                }

                filterbybalanceascordesc=!filterbybalanceascordesc;

                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_invoice_list;
    }

    private class InvoiceListAdapter extends BaseAdapter {

        private List<InvoiceRepository> invoices = null;

        public InvoiceListAdapter(List<InvoiceRepository> invoiceList) {
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

            invoiceId.setText(getString(R.string.invoice_number) + invoices.get(i).getId());
            invoiceTotal.setText(String.valueOf(formaterSolde(invoices.get(i).getTotal())));

            if (invoices.get(i).getClient() != null) {
                invoiceTitle.setText(invoices.get(i).getClient().getNom());
            } else {
                invoiceTitle.setText(getString(R.string.defaultclient));
            }

            if (invoices.get(i).getItems() == null) {
                convertView.setBackgroundColor(Color.BLUE);
            }

            if (invoices.get(i).isInvoiceRepo() == false) {
                convertView.setBackgroundColor(getResources().getColor(R.color.lightgray));
                invoiceTitle.setTextColor(Color.WHITE);
                invoiceTotal.setTextColor(Color.WHITE);
                invoiceId.setText((getString(R.string.invoice_depot_number) + invoices.get(i).getId()));
            }

            invoiceDate.setText(Config.getFormattedDate(invoices.get(i).getDate()));

            return convertView;
        }
    }


//    public class InvoiceRecyclerViewAdapter extends RealmSearchAdapter<InvoiceRepository, InvoiceListActivity.InvoiceRecyclerViewAdapter.ViewHolder> {
}
