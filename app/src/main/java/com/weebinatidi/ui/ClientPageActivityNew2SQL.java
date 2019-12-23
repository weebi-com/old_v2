package com.weebinatidi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.*;
import com.weebinatidi.model.Depots;
import com.weebinatidi.ui.weebi2.DepotDetailsActivitySQL;
import com.weebinatidi.ui.weebi2.FacturesSelectSQL;
import com.weebinatidi.ui.weebi2.InvoiceDetailsActivitySQL;
import com.weebinatidi.ui.weebi2.NewInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;

public class ClientPageActivityNew2SQL extends BaseActivity  {

    public static final String EXTRA_CLIENT_PHONE = "CLIENT_PHONE";
    public static final String EXTRA_CLIENT_ID = "CLIENT_ID";
    public static final String EXTRA_CLIENT_ARCHIVE = "CLIENT_ARCHIVE";
    public static final String EXTRA_MESSAGE = "LastIdInvoice";
    public static final String EXTRA_SOLDE = "SOLDE";

    private ListView mDepotList ,mDepotList1;

    private TextView nombrefacture, textsolde;
    private Realm realm;
    private String mPhone;
    private String solde;
    private String archived;
    private DepositAdapter mAdapter;
    private DepotAdapter depotAdapter;
    private OperationClientRepository operationClientRepository;
    private ArrayList<String> lesdates = new ArrayList<>();
    //private ArrayList<Integer>lesprisescon=new ArrayList<>();
    private ArrayList<Integer> lesidfactures = new ArrayList<>();
    private RealmChangeListener reamlListener;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isInvoice = true;
    private int taille = 0;
    private Button depot, valide, edit, supp;
    private TextView textView;
    private boolean isInvoice1 = false;
    DbHelper dbHelper;
    List<Invoice> lesfact=new ArrayList<>();
    ArrayList<Depots> lesdepots=new ArrayList<>();
    int lesolde;
    String idcli;
    AlertDialog.Builder Okbuilder;
    AlertDialog Okdialog;

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

        textsolde = (TextView) findViewById(R.id.solde);
        depot = (Button) findViewById(R.id.client_deposit);
        valide = (Button) findViewById(R.id.prise);
        edit = (Button) findViewById(R.id.edit_client);
        supp = (Button) findViewById(R.id.suppclient);
        dbHelper=new DbHelper(this);
        idcli = getIntent().getStringExtra(EXTRA_CLIENT_ID);
        //mPhone = getIntent().getStringExtra(EXTRA_CLIENT_PHONE);
        archived = getIntent().getStringExtra(EXTRA_CLIENT_ARCHIVE);
        //Toast.makeText(getApplicationContext(), ""+idcli, Toast.LENGTH_SHORT).show();

        if(idcli.equals("1")){
            edit.setVisibility(View.GONE);
            valide.setVisibility(View.GONE);
            depot.setVisibility(View.GONE);
            supp.setVisibility(View.GONE);
        }

        /**
         * debut traitement des factures
         * */
        // get the actual client info.

        //get operationclient
        //results = op.where().equalTo("isInvoice", isInvoice).findAll();
        lesfact=dbHelper.getAllInvoiceforOneClientR(idcli);
        mAdapter = new DepositAdapter(lesfact);

        mDepotList = (ListView)findViewById(R.id.deposit_liste);
        mDepotList1 = (ListView) findViewById(R.id.depot_liste);
        nombrefacture = (TextView)findViewById(R.id.nbrefacture);


        mDepotList.setAdapter(mAdapter);

        taille=lesfact.size();

        nombrefacture.setText("" + taille);

        lesdepots=dbHelper.getDepotforOneClient(idcli);

        depotAdapter = new DepotAdapter(lesdepots);
        mDepotList1.setAdapter(depotAdapter);

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClientDeposit();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onEditClient();
                                    }
                                }
        );

        valide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=dbHelper.getClient(idcli);
                if(cursor.moveToFirst()) {
                    String lenom = cursor.getString(2);
                    String lenum = cursor.getString(1);
                    String lesolde = cursor.getString(3);
                    //solde = cursor.getString(2);

                   // int soldeint = lesolde;

                    //solde = String.valueOf(lesolde);
                    //Toast.makeText(getActivity(),""+"\n"+lenom+"\n"+lenum+"\n"+solde,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), FacturesSelectSQL.class);

                    intent.putIntegerArrayListExtra("lesidfact", lesidfactures);
                    intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ARCHIVE, lenom);
                    intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_PHONE, lenum);
                    intent.putExtra(ClientPageActivityNew2SQL.EXTRA_SOLDE, lesolde);
                    //intent.putExtra("lesolde", lesolde);
                    startActivity(intent);
                }
            }
        });

        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Okbuilder = new AlertDialog.Builder(ClientPageActivityNew2SQL.this);
                View customView = getLayoutInflater().inflate(R.layout.dialog_prevenir_suppclient, null, false);
                final ImageView ok = customView.findViewById(R.id.okconfcash);
                final ImageView annule = customView.findViewById(R.id.annule);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHelper.suppClient(idcli);
                        Intent intent=new Intent(ClientPageActivityNew2SQL.this, ClientListActivitySQL.class);
                        startActivity(intent);
                        Okdialog.dismiss();
                        finish();
                    }
                });

                annule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Okdialog.dismiss();
                    }
                });
                Okbuilder.setCancelable(true);
                Okbuilder.setView(customView);
                Okbuilder.setTitle("Confirmer la facture - vente cash");
                Okdialog = Okbuilder.create();
                Okdialog.show();
            }
        });

        Cursor cur=dbHelper.getClient(idcli);
        if(cur.moveToFirst()==true){
                                    /*Toast.makeText(getApplicationContext(),""+cur.getString(0)+"\n"
                                            +cur.getString(1)+"\n"+cur.getString(2)+"\n",Toast.LENGTH_LONG).show();*/
                                    String solde=cur.getString(3).trim();
            lesolde=Integer.valueOf(solde);
            textsolde.setText(""+Config.formaterSolde(lesolde));

            getSupportActionBar().setTitle(cur.getString(2) + "  " + cur.getString(1));
        }



    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_client_page_new2sql;
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
                checkBox.setVisibility(View.VISIBLE);
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
                    Intent intent = new Intent(getApplicationContext(), InvoiceDetailsActivitySQL.class);
//                    Log.d(TAG,"idfacture"+invoice.getItems().size());
                    intent.putExtra(ClientPageActivityNew2SQL.EXTRA_MESSAGE, String.valueOf(lid));
                    startActivity(intent);
                }
            });



            boolean contain = false;
            int valeurATrouver = lid;


            for (int i = 0; i < lesidfactures.size(); i++) {
                if (lesidfactures.get(i) == valeurATrouver) {
                    contain = true;
                }
            }
            if (contain) {
                checkBox.setChecked(true);
                if (checkBox.isChecked()) {
                    //lesprisescon=new ArrayList<>();
                    //Toast.makeText(getApplicationContext(),""+lid,Toast.LENGTH_SHORT).show();
                    lesidfactures.add(lid);
                    int taille = lesidfactures.size();
                }

            } else {
                checkBox.setChecked(false);

            }
            //lors de la click sur le checkbox
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checkBox.setChecked(true);
                    if (checkBox.isChecked()) {


                        //on ajoute lid de la facture à la liste des factures choisies
                        lesidfactures.add(lid);
                        int taille = lesidfactures.size();

                        //Toast.makeText(getApplicationContext(),""+lesidfactures+"\n taille "+taille,Toast.LENGTH_SHORT).show();
                    } else if (!checkBox.isChecked()) {
                        int taille = lesidfactures.size();

                        Iterator<Integer> iterator = lesidfactures.iterator();
                        while (iterator.hasNext()) {
                            Integer o = iterator.next();
                            if (o == lid) {
                                // On supprime l'élément courant de la liste
                                //Toast.makeText(getApplicationContext(),""+lesidfactures+"\n taille "+taille,Toast.LENGTH_SHORT).show();
                                iterator.remove();
                            }
                        }
                    }

                }


            });


            return convertView;
        }
    }

    private class DepotAdapter extends BaseAdapter {

        private List<Depots> depots = null;

        public DepotAdapter(List<Depots> list) {
            depots = list;
        }

        @Override
        public int getCount() {
            if (depots != null)
                return depots.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return depots.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_depot, parent, false);
            }

            TextView depotType = (TextView) convertView.findViewById(R.id.deposit);
            //TextView depotDate = (TextView) convertView.findViewById(R.id.deposit_date);
            TextView depotMontant = (TextView) convertView.findViewById(R.id.deposit_amount);
            Button voirdep = (Button) convertView.findViewById(R.id.voirdep);
            final String lid = depots.get(position).getId();
            final Depots depotss = depots.get(position);

            voirdep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),""+lid,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), DepotDetailsActivitySQL.class);
//                    Log.d(TAG,"idfacture"+invoice.getItems().size());
                    intent.putExtra(ClientPageActivityNew2SQL.EXTRA_MESSAGE, lid);
                    startActivity(intent);
                }
            });

            String montant = depots.get(position).getMontant();
            String commente=depots.get(position).getType();

            String lemontant = formaterSolde(Integer.valueOf(montant));
            depotMontant.setText(getString(R.string.montant_egale) + String.valueOf(lemontant));
            depotType.setText(commente+" : "+depots.get(position).getDate());



//
            return convertView;
        }
    }

    private void onEditClient() {


        final AlertDialog RegisterClientDialog;
        // launch a dialog to register a client.
//        RegisterClientbuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        AlertDialog.Builder RegisterClientbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_new_client, null, false);
        RegisterClientbuilder.setView(customView);
        //TODO Should be used where its asked to select a client to do something..

//        RegisterClientbuilder.setSingleChoiceItems()

        final FormEditText username = (FormEditText) customView.findViewById(R.id.client_name);
        final FormEditText phone = (FormEditText) customView.findViewById(R.id.client_number);
        //final FormEditText phone2 = (FormEditText) customView.findViewById(R.id.client_number2);
        final FormEditText solde = (FormEditText) customView.findViewById(R.id.client_solde);
        final FormEditText email = (FormEditText) customView.findViewById(R.id.client_mail);

        final LinearLayout numero=customView.findViewById(R.id.clavn);
        final LinearLayout lettre=customView.findViewById(R.id.clavl);
        email.setVisibility(View.GONE);
        solde.setVisibility(View.GONE);
        Cursor cur=dbHelper.getClient(idcli);
        if(cur.moveToFirst()){
            idcli=cur.getString(0);
            username.setText(cur.getString(2));
            phone.setText(cur.getString(1));
        }


        Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;
        a=(Button)customView.findViewById(R.id.a); b=(Button)customView.findViewById(R.id.b);c=(Button)customView.findViewById(R.id.c);
        d=(Button)customView.findViewById(R.id.d); e=(Button)customView.findViewById(R.id.e); f=(Button)customView.findViewById(R.id.f);
        g=(Button)customView.findViewById(R.id.g); h=(Button)customView.findViewById(R.id.h); i=(Button)customView.findViewById(R.id.i);
        j=(Button)customView.findViewById(R.id.j); k=(Button)customView.findViewById(R.id.k); l=(Button)customView.findViewById(R.id.l);
        m=(Button)customView.findViewById(R.id.m); n=(Button)customView.findViewById(R.id.n); o=(Button)customView.findViewById(R.id.o);
        p=(Button)customView.findViewById(R.id.p); r=(Button)customView.findViewById(R.id.r); s=(Button)customView.findViewById(R.id.s);
        t=(Button)customView.findViewById(R.id.t); u=(Button)customView.findViewById(R.id.u); v=(Button)customView.findViewById(R.id.v);
        w=(Button)customView.findViewById(R.id.w); x=(Button)customView.findViewById(R.id.x); y=(Button)customView.findViewById(R.id.y);
        q=(Button)customView.findViewById(R.id.q); z=(Button)customView.findViewById(R.id.z);
        espaced=(Button)customView.findViewById(R.id.espaced);
        cleard=(Button)customView.findViewById(R.id.btnClearAl);


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

        username.setInputType(0);
        phone.setInputType(0);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                numero.setVisibility(View.GONE);
                lettre.setVisibility(View.VISIBLE);
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.GONE);
                numero.setVisibility(View.VISIBLE);
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+0);
                }
                else {
                    phone.append(""+0);
                }
            }
        });

        un.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+1);
                }
                else {
                    phone.append(""+1);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        deux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+2);
                }
                else {
                    phone.append(""+2);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        trois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+3);
                }
                else {
                    phone.append(""+3);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        quatre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+4);
                }
                else {
                    phone.append(""+4);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        cinq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+5);
                }
                else {
                    phone.append(""+5);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+6);
                }
                else {
                    phone.append(""+6);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        sept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+7);
                }
                else {
                    phone.append(""+7);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        huit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+8);
                }
                else {
                    phone.append(""+8);
                }


            }
        });

        neuf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=phone.getText().toString();

                if(chaine.contains("")){
                    phone.append(""+9);
                }
                else {
                    phone.append(""+9);
                }

            }
        });



        cleardn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=phone.getText().toString();
                if (chaine.equals("")){

                }else {
                    chaine = chaine.substring(0, chaine.length()-1);
                    phone.setText(""+chaine);
                }

            }
        });

        cleard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=username.getText().toString();
                if (chaine.equals("")){

                }else {
                    chaine = chaine.substring(0, chaine.length()-1);
                    username.setText(""+chaine);
                }

            }
        });



        espaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append(" ");
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("A");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("B");
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("C");
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("D");
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("E");
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("F");
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("G");
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("H");
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("I");
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("J");
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("K");
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("L");
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("M");
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("N");
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("O");
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("P");
            }
        });
        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("Q");
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("R");
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("S");
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("T");
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("U");
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("V");
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("W");
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("X");
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("Y");
            }
        });
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.append("Z");
            }
        });

        RegisterClientbuilder.setCancelable(true);

//        RegisterClientbuilder.setInverseBackgroundForced(true);
        RegisterClientbuilder.setTitle(getString(R.string.edit_client));
        RegisterClientDialog = RegisterClientbuilder.create();

        RegisterClientDialog.show();
        final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewclient);


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessEditclient(username, phone, RegisterClientDialog);

            }
        });


    }

    public void ProcessEditclient(FormEditText username, FormEditText phone, AlertDialog alertDialog) {
//        FormEditText[] allFields = {username, phone,phone2,email};
        FormEditText[] allFields = {username, phone};
        boolean allvalid = true;

        for (FormEditText field : allFields) {
            allvalid = field.testValidity() && allvalid;
        }

        if (allvalid)
        {
            if(phone.getText().toString().equals(phone.getText().toString()))
            {

                String clientName = username.getText().toString();
                String clientPhone = phone.getText().toString();

                dbHelper.majClient(idcli,clientPhone,clientName);
                //Toast.makeText(getApplicationContext(),""+clientName+"\n"+clientPhone,Toast.LENGTH_LONG).show();

                alertDialog.dismiss();
                Intent intent=new Intent(getApplicationContext(),ClientListActivitySQL.class);
                startActivity(intent);
                finish();
//                    RegisterClientDialog.dismiss();
                // check the validity of the entry
                // register the user in the database
                // send confirmation message to the operator
            }

        }

//                if (isEntryValid(username, phone,phone2))


    }

    private void onClientDeposit() {

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_new_deposit, null, false);
//        final Spinner clientSpinner = (Spinner) customView.findViewById(R.id.select_client);
//        ImageButton addClient = (ImageButton) customView.findViewById(R.id.add_client);
        final EditText comment = (EditText) customView.findViewById(R.id.comment);
        final EditText clientSolde = (EditText) customView.findViewById(R.id.client_solde);
        final EditText clientSoldee = (EditText) customView.findViewById(R.id.client_solderewrite);
        final ImageView okbtn = (ImageView) customView.findViewById(R.id.okdepositclient);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(clientSolde, InputMethodManager.SHOW_IMPLICIT);
        // get all the clients
        final LinearLayout lettre=customView.findViewById(R.id.clavl);
        final LinearLayout numeriq=customView.findViewById(R.id.clavn);

        final Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;
        a=(Button)customView.findViewById(R.id.a); b=(Button)customView.findViewById(R.id.b);c=(Button)customView.findViewById(R.id.c);
        d=(Button)customView.findViewById(R.id.d); e=(Button)customView.findViewById(R.id.e); f=(Button)customView.findViewById(R.id.f);
        g=(Button)customView.findViewById(R.id.g); h=(Button)customView.findViewById(R.id.h); i=(Button)customView.findViewById(R.id.i);
        j=(Button)customView.findViewById(R.id.j); k=(Button)customView.findViewById(R.id.k); l=(Button)customView.findViewById(R.id.l);
        m=(Button)customView.findViewById(R.id.m); n=(Button)customView.findViewById(R.id.n); o=(Button)customView.findViewById(R.id.o);
        p=(Button)customView.findViewById(R.id.p); r=(Button)customView.findViewById(R.id.r); s=(Button)customView.findViewById(R.id.s);
        t=(Button)customView.findViewById(R.id.t); u=(Button)customView.findViewById(R.id.u); v=(Button)customView.findViewById(R.id.v);
        w=(Button)customView.findViewById(R.id.w); x=(Button)customView.findViewById(R.id.x); y=(Button)customView.findViewById(R.id.y);
        q=(Button)customView.findViewById(R.id.q); z=(Button)customView.findViewById(R.id.z);
        espaced=(Button)customView.findViewById(R.id.espaced);
        cleard=(Button)customView.findViewById(R.id.btnClearAl);

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

        builder.setView(customView);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.registerdepot));
        builder.setMessage(getString(R.string.client_deposit));

        final AlertDialog depositDialog = builder.create();

        comment.setInputType(0);
        clientSolde.setInputType(0);
        clientSoldee.setInputType(0);



        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.VISIBLE);
                numeriq.setVisibility(View.GONE);


            }
        });

        clientSolde.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.GONE);
                numeriq.setVisibility(View.VISIBLE);

                zero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+0);
                        }
                        else {
                            clientSolde.append(""+0);
                        }
                    }
                });

                un.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+1);
                        }
                        else {
                            clientSolde.append(""+1);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                deux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+2);
                        }
                        else {
                            clientSolde.append(""+2);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                trois.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+3);
                        }
                        else {
                            clientSolde.append(""+3);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                quatre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+4);
                        }
                        else {
                            clientSolde.append(""+4);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                cinq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+5);
                        }
                        else {
                            clientSolde.append(""+5);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+6);
                        }
                        else {
                            clientSolde.append(""+6);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                sept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+7);
                        }
                        else {
                            clientSolde.append(""+7);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                huit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+8);
                        }
                        else {
                            clientSolde.append(""+8);
                        }


                    }
                });

                neuf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSolde.getText().toString();

                        if(chaine.contains("")){
                            clientSolde.append(""+9);
                        }
                        else {
                            clientSolde.append(""+9);
                        }

                    }
                });

                cleardn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=clientSolde.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            clientSolde.setText(""+chaine);
                        }

                    }
                });
            }
        });

        clientSoldee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.GONE);
                numeriq.setVisibility(View.VISIBLE);

                zero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+0);
                        }
                        else {
                            clientSoldee.append(""+0);
                        }
                    }
                });

                un.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+1);
                        }
                        else {
                            clientSoldee.append(""+1);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                deux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+2);
                        }
                        else {
                            clientSoldee.append(""+2);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                trois.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+3);
                        }
                        else {
                            clientSoldee.append(""+3);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                quatre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+4);
                        }
                        else {
                            clientSoldee.append(""+4);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                cinq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+5);
                        }
                        else {
                            clientSoldee.append(""+5);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+6);
                        }
                        else {
                            clientSoldee.append(""+6);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                sept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+7);
                        }
                        else {
                            clientSoldee.append(""+7);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                huit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+8);
                        }
                        else {
                            clientSoldee.append(""+8);
                        }


                    }
                });

                neuf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=clientSoldee.getText().toString();

                        if(chaine.contains("")){
                            clientSoldee.append(""+9);
                        }
                        else {
                            clientSoldee.append(""+9);
                        }

                    }
                });

                cleardn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=clientSoldee.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            clientSoldee.setText(""+chaine);
                        }

                    }
                });
            }
        });

        cleard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=comment.getText().toString();
                if (chaine.equals("")){

                }else {
                    chaine = chaine.substring(0, chaine.length()-1);
                    comment.setText(""+chaine);
                }

            }
        });

        espaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append(" ");
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("a");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("b");
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("c");
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("d");
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("e");
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("f");
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("g");
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("h");
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("i");
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("j");
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("k");
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("l");
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("m");
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("n");
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("o");
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("p");
            }
        });
        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("q");
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("r");
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("s");
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("t");
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("u");
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("v");
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("w");
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("x");
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("y");
            }
        });
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.append("z");
            }
        });


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String solde = clientSolde.getText().toString();
                String soldee = clientSoldee.getText().toString();
                String comente=comment.getText().toString();

                if(comente.equals("")){
                    comente="depot";
                }


                if (!TextUtils.isEmpty(solde)) {
                    if (idcli != null) {

                        if (solde.equals(soldee)) {

                            dbHelper.faireDepot(solde, Config.getFormattedDate(Calendar.getInstance().getTime()) , idcli,comente);
                            Snackbar.make(mDepotList, getString(R.string.solderegistersuccess), Snackbar.LENGTH_SHORT).show();
                            depositDialog.dismiss();
                            Cursor cur=dbHelper.getClient(idcli);
                            if(cur.moveToFirst()==true){
                                   // Toast.makeText(getApplicationContext(),""+idcli,Toast.LENGTH_LONG).show();
                                lesolde=Integer.valueOf(cur.getString(3));
                                lesolde+=Integer.valueOf(solde);
                            }
                            dbHelper.majsoldecli(idcli, String.valueOf(lesolde));

                            //finish();
                            Intent intent=new Intent(ClientPageActivityNew2SQL.this, ClientPageActivityNew2SQL.class);
                            intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ID, idcli);
                            //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                            //Toast.makeText(getApplicationContext(),""+lesdepots.toString(),Toast.LENGTH_SHORT).show();
                        } else {
                            clientSoldee.setError("les deux montants ne corresspondent pas");
                        }

                    } else {

                        Snackbar.make(clientSolde, getString(R.string.select_customer), Snackbar.LENGTH_SHORT).show();

                    }
                } else {
                    clientSolde.setError(getString(R.string.emptysolde));
                }
            }

        });
        depositDialog.show();

    }



    public void onBackPressed(){

        if(idcli.equals("1")){
            Intent intent=new Intent(this, NewInterface.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent=new Intent(this, ClientListActivitySQL.class);
            startActivity(intent);
            finish();
        }


    }
}
