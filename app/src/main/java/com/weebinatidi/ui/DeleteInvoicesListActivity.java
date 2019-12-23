package com.weebinatidi.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.ArchiveInvoiceRepository;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DeleteInvoicesListActivity extends BaseActivity {

    boolean filterbydateascordesc = true;
    private ListView invoiceList;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_delete_invoices_list);
        invoiceList = (ListView) findViewById(R.id.archiveinvoice_list);

        realm = Realm.getDefaultInstance();
//TODO Sort by date already done but add by most recent to the latest... Q12
        RealmResults<ArchiveInvoiceRepository> invoices = realm.where(ArchiveInvoiceRepository.class).findAll();
        if (filterbydateascordesc) {
            invoices.sort("date", Sort.DESCENDING); // sort the list by date
        } else {
            invoices.sort("date", Sort.ASCENDING); // sort the list by date
        }


        //TODO  Add a filter to search invoices by date  the filter button will be shown on an actionbar Q13
        //should drop a realsearchview here instead...
        InvoiceListAdapter mAdapter = new InvoiceListAdapter(invoices);
        invoiceList.setAdapter(mAdapter);

        invoiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //TODO the access to deleted invoices detaisl has beeen canceleed
                // show the invoice details.
//                ArchiveInvoiceRepository invoice = (ArchiveInvoiceRepository) adapterView.getAdapter().getItem(i);
//
//                Intent intent = new Intent(DeleteInvoicesListActivity.this, InvoiceDetailsActivity.class);
//                intent.putExtra(InvoiceDetailsActivity.EXTRA_INVOICE_ID, invoice.getId());
//                intent.putExtra(InvoiceDetailsActivity.EXTRA_ISARCHIVE,true);
//                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_delete_invoices_list;
    }

//    @Override
//    public int getLayoutResourceId() {
//        return R.layout.activity_delete_invoices_list;
//    }


    @Override
    protected void onResume() {
        super.onResume();
        setTitle(getString(R.string.archives_factures));
    }

    private class InvoiceListAdapter extends BaseAdapter {

        private List<ArchiveInvoiceRepository> invoices = null;

        public InvoiceListAdapter(List<ArchiveInvoiceRepository> invoiceList) {
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

            invoiceId.setText(R.string.facture/* #" + invoices.get(i).getId()*/);
            invoiceTotal.setText(String.valueOf(invoices.get(i).getTotal()));

            if (invoices.get(i).getClient() != null) {
                invoiceTitle.setText(invoices.get(i).getClient().getNom());
            } else {
                invoiceTitle.setText(R.string.facture_supprimee);
            }

            invoiceDate.setText(Config.getFormattedDate(invoices.get(i).getDate()));

            return convertView;
        }
    }
}
