package com.weebinatidi.ui.weebi2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.ArchiveInvoiceRepository;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.Invoice;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.ClientPageActivityNew2SQL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.weebinatidi.Config.formaterSolde;

public class DeleteInvoicesListActivitySQL extends BaseActivity {

    public static final String EXTRA_MESSAGE = "LastIdInvoice";

    boolean filterbydateascordesc = true;
    private ListView invoiceList;
    DbHelper dbHelper;
    List<Invoice> lesfacts=new ArrayList<>();
    private DepositAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper=new DbHelper(this);
//         setContentView(R.layout.activity_delete_invoices_list);
        invoiceList = (ListView) findViewById(R.id.archiveinvoice_list);
        lesfacts=dbHelper.getAllInvoiceS();
        mAdapter = new DepositAdapter(lesfacts);
        invoiceList.setAdapter(mAdapter);

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_delete_invoices_list;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private class DepositAdapter extends BaseAdapter {

        private List<Invoice> factures = null;

        public DepositAdapter(List<Invoice> list) {
            factures = list;
        }

        @Override
        public int getCount() {
            if (factures != null)
                return factures.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return factures.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_deposit, parent, false);
            }

            TextView depotType = (TextView) convertView.findViewById(R.id.deposit);
            //TextView depotDate = (TextView) convertView.findViewById(R.id.deposit_date);
            TextView depotMontant = (TextView) convertView.findViewById(R.id.deposit_amount);
            Button voir = (Button) convertView.findViewById(R.id.voir);
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            //final int lid = mOperationClients.get(position).getInvoiceRepository().getId();
            final int lid = factures.get(position).getId();
            final Invoice invoice = factures.get(position);

            int montant = factures.get(position).getTotal();

            final String type=factures.get(position).getType();

            //voir.setBackgroundColor(Color.BLUE);

            if(type.equals("credit")){
                voir.setBackgroundColor(Color.RED);
                voir.setText("credit");
                voir.setTextColor(Color.BLACK);
                checkBox.setVisibility(View.INVISIBLE);
            }

            if(type.equals("cash")){
                checkBox.setVisibility(View.INVISIBLE);
                voir.setBackgroundColor(Color.GREEN);
                voir.setText("cash");
                voir.setTextColor(Color.BLACK);
            }


            String lemontant = formaterSolde(montant);
            depotMontant.setText(getString(R.string.montant_egale) + String.valueOf(lemontant));
            depotType.setText(getString(R.string.facture_of)
                    + factures.get(position).getDates());

            voir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext()," type  "+type,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), InvoiceSuppActivitySQL.class);
//                    Log.d(TAG,"idfacture"+invoice.getItems().size());
                    intent.putExtra(DeleteInvoicesListActivitySQL.EXTRA_MESSAGE, String.valueOf(lid));
                    startActivity(intent);
                }
            });


            return convertView;
        }
    }
}
