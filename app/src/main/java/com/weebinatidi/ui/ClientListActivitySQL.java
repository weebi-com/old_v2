package com.weebinatidi.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.crashlytics.android.Crashlytics;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.Client;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.OperationClientRepository;
import com.weebinatidi.ui.weebi2.NewInterface;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.Sort;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;


public class ClientListActivitySQL extends BaseActivity {

    private static final String TAG = ClientListActivitySQL.class.getSimpleName();


    private boolean sortingname=true;
    private boolean sortbalance=true;
    private Button rechercher, menu, ajout, triname, trisolde;
    private ListView listeclient;
    private AdapterClient adapterClient;
    private EditText recherclient;

    private List<Client>lesclients=new ArrayList<>();
    private List<Client>results=new ArrayList<>();
    //List<String> clients;
    private ListView listeclientaffiche;

    private String lenumero="";
    BroadcastReceiver broadcastReceiver;
    DbHelper dbHelper;
    ArrayAdapter<String> arrayAdapter;


    AlertDialog.Builder RegisterClientbuilder;
    AlertDialog RegisterClientDialog;
    AlertDialog.Builder MenuClientbuilder;
    AlertDialog MenuClientDialog;

    Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,
            tiret8,espace,clear,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.customers);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
       // testerconnexion();
        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
       /* if (isTablet(this)||isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else if(isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }*/
        a=(Button)findViewById(R.id.a); b=(Button)findViewById(R.id.b);c=(Button)findViewById(R.id.c);
        d=(Button)findViewById(R.id.d); e=(Button)findViewById(R.id.e); f=(Button)findViewById(R.id.f);
        g=(Button)findViewById(R.id.g); h=(Button)findViewById(R.id.h); i=(Button)findViewById(R.id.i);
        j=(Button)findViewById(R.id.j); k=(Button)findViewById(R.id.k); l=(Button)findViewById(R.id.l);
        m=(Button)findViewById(R.id.m); n=(Button)findViewById(R.id.n); o=(Button)findViewById(R.id.o);
        p=(Button)findViewById(R.id.p); r=(Button)findViewById(R.id.r); s=(Button)findViewById(R.id.s);
        t=(Button)findViewById(R.id.t); u=(Button)findViewById(R.id.u); v=(Button)findViewById(R.id.v);
        w=(Button)findViewById(R.id.w); x=(Button)findViewById(R.id.x); y=(Button)findViewById(R.id.y);
        q=(Button)findViewById(R.id.q); z=(Button)findViewById(R.id.z);

        un=(Button)findViewById(R.id.btnNum1Id);
        deux=(Button)findViewById(R.id.btnNum2Id);
        trois=(Button)findViewById(R.id.btnNum3Id);
        quatre=(Button)findViewById(R.id.btnNum4Id);
        cinq=(Button)findViewById(R.id.btnNum5Id);
        six=(Button)findViewById(R.id.btnNum6Id);
        sept=(Button)findViewById(R.id.btnNum7Id);
        huit=(Button)findViewById(R.id.btnNum8Id);
        neuf=(Button)findViewById(R.id.btnNum9Id);
        //doublezero=(Button)findViewById(R.id.btn00);
        zero=(Button)findViewById(R.id.btnNum0);
        clear=(Button)findViewById(R.id.btnClearAl);
        espace=(Button)findViewById(R.id.espace);
        tiret8=(Button)findViewById(R.id.tiret8);
        tiret8.setVisibility(View.GONE);

        triname=findViewById(R.id.sortingalpha);
        trisolde=findViewById(R.id.sortingnumber);


        dbHelper=new DbHelper(this);
        results=dbHelper.getAllClientwithoutcash();
        //clients=dbHelper.getAllCliName();
        listeclientaffiche=(ListView)findViewById(R.id.listec);

        recherclient=findViewById(R.id.champdesaisie);

        for(Client r: results){
            Client cr=new Client();
            cr.setNom(r.getNom());
            cr.setNumero(r.getNumero());
            cr.setSolde(r.getSolde());
            cr.setId(r.getId());
            lesclients.add(cr);

            // Toast.makeText(getApplicationContext(),""+r.getNumero(), Toast.LENGTH_LONG).show();
        }
        adapterClient=new AdapterClient(this,lesclients);
        listeclientaffiche.setAdapter(adapterClient);

        trisolde.setVisibility(View.VISIBLE);

        trisolde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                lesclients.clear();
                results=dbHelper.getAllClientwithoutcashBySolde();
                for(Client r: results){
                    Client cr=new Client();
                    cr.setNom(r.getNom());
                    cr.setNumero(r.getNumero());
                    cr.setSolde(r.getSolde());
                    cr.setId(r.getId());
                    lesclients.add(cr);

                    // Toast.makeText(getApplicationContext(),""+r.getNumero(), Toast.LENGTH_LONG).show();
                }
                adapterClient.notifyDataSetChanged();
                trisolde.setVisibility(View.GONE);
                triname.setVisibility(View.VISIBLE);
            }
        });

        triname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                lesclients.clear();
                results=dbHelper.getAllClientwithoutcash();
                for(Client r: results){
                    Client cr=new Client();
                    cr.setNom(r.getNom());
                    cr.setNumero(r.getNumero());
                    cr.setSolde(r.getSolde());
                    cr.setId(r.getId());
                    lesclients.add(cr);

                    // Toast.makeText(getApplicationContext(),""+r.getNumero(), Toast.LENGTH_LONG).show();
                }
                adapterClient.notifyDataSetChanged();
                trisolde.setVisibility(View.VISIBLE);
                triname.setVisibility(View.GONE);
            }
        });

        //adapterClient.notifyDataSetChanged();
        recherclient.setInputType(0);

        recherclient.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                // NewInterface.this.adapterReference.filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = recherclient.getText().toString().toLowerCase(Locale.getDefault());
                adapterClient.filter(text);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=recherclient.getText().toString();
                if (chaine.equals("")){

                }else {
                    chaine = chaine.substring(0, chaine.length()-1);
                    recherclient.setText(""+chaine);
                }

            }
        });
        espace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append(" ");
            }
        });
        /*tiret8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("_");
            }
        });*/


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("A");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("B");
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("C");
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("D");
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("E");
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("F");
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("G");
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("H");
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("I");
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("J");
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("K");
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("L");
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("M");
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("N");
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("O");
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("P");
            }
        });
        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("Q");
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("R");
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("S");
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("T");
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("U");
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("V");
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("W");
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("X");
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("Y");
            }
        });
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherclient.append("Z");
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+0);
                }
                else {
                    recherclient.append(""+0);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        un.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+1);
                }
                else {
                    recherclient.append(""+1);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        deux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+2);
                }
                else {
                    recherclient.append(""+2);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        trois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+3);
                }
                else {
                    recherclient.append(""+3);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        quatre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+4);
                }
                else {
                    recherclient.append(""+4);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        cinq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+5);
                }
                else {
                    recherclient.append(""+5);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+6);
                }
                else {
                    recherclient.append(""+6);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        sept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+7);
                }
                else {
                    recherclient.append(""+7);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        huit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+8);
                }
                else {
                    recherclient.append(""+8);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        neuf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherclient.getText().toString();

                if(chaine.contains("")){
                    recherclient.append(""+9);
                }
                else {
                    recherclient.append(""+9);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });
        //listeclient.setAdapter(adapterClient);

        //arrayAdapter = new ArrayAdapter<String>(ClientListActivitySQL.this, android.R.layout.simple_list_item_1, clients);
        //listeclientaffiche.setAdapter(arrayAdapter);

       /* for(int i=0; i<lesclients.size(); i++){
            Toast.makeText(getApplicationContext(), ""+lesclients.get(i), Toast.LENGTH_SHORT).show();
        }*/


        //listeclient=(ListView)findViewById(R.id.search_client_view2);
        ajout=(Button)findViewById(R.id.add_cliente);
        //client_balance_sort=(Button)findViewById(R.id.client_balance_sort);

        //adapterClient=new AdapterClient(this,clientRepositories);
       // listeclient.setAdapter(adapterClient);


//        adapterClient.notifyDataSetChanged();

        listeclientaffiche.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //si l'orientation est en paysage on va chercher ClientPageActivityNew2SQL sinon on va dans le Clientpageportrait
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    //String lenum=((TextView)view.findViewById(R.id.client_phone)).getText().toString();
                    String lid=((TextView)view.findViewById(R.id.clientid)).getText().toString();
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ClientListActivitySQL.this, ClientPageActivityNew2SQL.class);
                    intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ID, lid);
                    intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ARCHIVE, "nonarchived");
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
                else {
                    String lid=((TextView)view.findViewById(R.id.clientid)).getText().toString();
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ClientListActivitySQL.this, ClientPagePortraitSql.class);
                    intent.putExtra(ClientPagePortraitSql.EXTRA_CLIENT_ID, lid);
                    intent.putExtra(ClientPagePortraitSql.EXTRA_CLIENT_ARCHIVE, "nonarchived");
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }



            }
        });



        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClient();
            }
        });

        /*client_balance_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

       // rechercher=(Button)findViewById(R.id.search);
        menu=(Button)findViewById(R.id.menu);

        /*rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recherclient.setVisibility(View.VISIBLE);
            }
        });*/



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menus();
            }
        });
    }





    public void menus() {
        MenuClientbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_menu_client_liste, null, false);

        //final Button balance = (Button) customView.findViewById(R.id.client_balance_sort);
        final Button alphabetic = (Button) customView.findViewById(R.id.client_alphabetical_sort);
        final Button addclient = (Button) customView.findViewById(R.id.add_cliente);

        addclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClient();
                MenuClientDialog.dismiss();
            }
        });


       /* balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        alphabetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        MenuClientbuilder.setCancelable(true);
        MenuClientbuilder.setView(customView);
        // RegisterClientbuilder.setTitle(getString(R.string.ajouter_un_client));

        MenuClientDialog = MenuClientbuilder.create();
        MenuClientDialog.show();


    }


    /*final Fabric fabric = new Fabric.Builder(this.getApplicationContext())
            .kits(new Crashlytics())
            .debuggable(true)           // Enables Crashlytics debugger
            .build();*/
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId())
        {



            case android.R.id.home:
                onBackPressed();
                break;


        }

        return true;
    }


    private boolean isEntryValid(EditText...views) {
        boolean isValid = true;
        View errorView = null;
        for (int i = 0; i < views.length; i++) {
            if (TextUtils.isEmpty(views[i].getText().toString())) {
                isValid = false;
                errorView = views[i];
                views[i].setError(getString(R.string.error_empty_field_msg));
            }
        }

        if (errorView != null) {
            errorView.requestFocus();
        }

        return isValid;
    }

    private void onRegisterClient() {
        //TODO Q20 Done


        // launch a dialog to register a client.
//        RegisterClientbuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        RegisterClientbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_new_client, null, false);


        final FormEditText username = (FormEditText) customView.findViewById(R.id.client_name);
        final FormEditText phone = (FormEditText) customView.findViewById(R.id.client_number);
        //final FormEditText phone2 = (FormEditText) customView.findViewById(R.id.client_number2);
        final FormEditText solde = (FormEditText) customView.findViewById(R.id.client_solde);
        final FormEditText email = (FormEditText) customView.findViewById(R.id.client_mail);
        final ImageView okbtn = (ImageView)customView.findViewById(R.id.oknewclient);
        final LinearLayout numero=customView.findViewById(R.id.clavn);
        final LinearLayout lettre=customView.findViewById(R.id.clavl);

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
        solde.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        RegisterClientbuilder.setCancelable(true);
        RegisterClientbuilder.setView(customView);
        RegisterClientbuilder.setTitle(getString(R.string.ajouter_un_client));

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


        RegisterClientDialog = RegisterClientbuilder.create();


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterProcess(username,phone,phone,email,solde,RegisterClientDialog);
            }
        });

        RegisterClientDialog.show();

    }

    public void RegisterProcess(FormEditText username, FormEditText phone, FormEditText phone2, FormEditText email, FormEditText solde, AlertDialog alertDialog)
    {

//        FormEditText[] allFields = {username, phone,phone2,email};
        FormEditText[] allFields = {username, phone,phone};
        boolean allvalid=true;

        for (FormEditText field: allFields) {
            allvalid = field.testValidity() && allvalid;
        }

//                if (isEntryValid(username, phone,phone2))
        if (allvalid)
        {
            if(phone.getText().toString().equals(phone.getText().toString()))
            {

                    String clientName = username.getText().toString();
                    String clientPhone = phone.getText().toString();
                    String clientSolde = "0";//solde.getText().toString();
//                    String clientmail = email.getText().toString();

                Cursor cur=dbHelper.getClientnum(clientPhone);
                if(cur.moveToFirst()){
                    Toast.makeText(getApplicationContext(),"ce numero est déjà enregistré",Toast.LENGTH_LONG).show();
                }

                if(!cur.moveToFirst()){
                    dbHelper.insertClient(clientName,clientPhone,clientSolde);

                    alertDialog.dismiss();
                    Intent intent=new Intent(getApplicationContext(),ClientListActivitySQL.class);
                    startActivity(intent);
                    finish();
                }

            }
            else
            {
//                        phone.setError("les numeros ne correspondent pas",getResources().getDrawable(R.drawable.add_user));
                phone2.setError(getString(R.string.numnomatch),getResources().getDrawable(R.drawable.add_user));
                Snackbar.make(phone,getString(R.string.numnomatch), Snackbar.LENGTH_LONG).show();

            }
        }
    }

    public void ProcessToClientDeposit(Realm realms, ClientRepository client, String soldes)
    {
        // Add the transaction to the deposit
        //realms.beginTransaction();
        OperationClientRepository depot = realms.createObject(OperationClientRepository.class);
        depot.setClient(client);
        depot.setMontant(Integer.valueOf(soldes));
        depot.setDate(Calendar.getInstance().getTime());
        depot.setInvoice(false);
        //********************TEMP********************//

        int invoicegoid= Config.getLastInvoiceId(ClientListActivitySQL.this)+1;
//                            InvoiceRepository invoicedepot= new InvoiceRepository();
        InvoiceRepository invoicedepot= new InvoiceRepository();
        invoicedepot.setId(invoicegoid);
        Config.setLastInvoiceId(ClientListActivitySQL.this, invoicegoid);
        invoicedepot.setDate(Calendar.getInstance().getTime());
        invoicedepot.setTotal(Integer.valueOf(soldes));
        invoicedepot.setClient(client);
        invoicedepot.setInvoiceRepo(false);
        int itemgoid= Config.getLastItemId(ClientListActivitySQL.this)+1;
        ItemRepository itemdepot= new ItemRepository();
        itemdepot.setId(itemgoid);
        Config.setLastItemId(ClientListActivitySQL.this,itemgoid);
        itemdepot.setName("Depot du "+ Config.getFormattedDate(Calendar.getInstance().getTime()));
//        itemdepot.setQuantity(1);
        itemdepot.setQuantity(Integer.valueOf(soldes));
//        itemdepot.setUnitPrice(Integer.valueOf(soldes));
//        itemdepot.setTotalPrice(Integer.valueOf(soldes));
        itemdepot=realms.copyToRealmOrUpdate(itemdepot);
        invoicedepot.getItems().add(itemdepot);
        invoicedepot=realms.copyToRealmOrUpdate(invoicedepot);
        depot.setInvoiceRepository(invoicedepot);


        //*********************END TEMP********************//
        // Add the depot to the client
        client.getOperationClients().add(depot);
        int newSolde = client.getSolde() + Integer.valueOf(soldes);
        client.setSolde(newSolde);

        //realms.commitTransaction();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_client_list_sql;
    }


    public class ClientItemView extends RelativeLayout {

        @Bind(R.id.client_name)
        TextView name;

        @Bind(R.id.client_solde)
        TextView solde;

        @Bind(R.id.client_phone)
        TextView phone;

//        @Bind(R.id.client_delete)
//        ImageView deletebtn;

        public ClientItemView(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            inflate(context, R.layout.client_item_view, this);
            ButterKnife.bind(this);
        }

        public void bind(ClientRepository client) {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            name.setText(client.getNom());

            solde.setText(String.valueOf(formaterSolde(client.getSolde())));

            phone.setText(client.getNumero());
        }

        public void  setSoldecolor(int color)
        {
            solde.setTextColor(color);
        }
    }


    public class AdapterClient extends BaseAdapter{

        private Activity activity;
        private LayoutInflater inflater;
        private List<Client> clients;
        private ArrayList<Client> arrayliste;
        public AdapterClient(Activity activity, List<Client> clients){
            this.activity=activity;
            this.clients=clients;
            this.arrayliste = new ArrayList<>();
            this.arrayliste.addAll(clients);
        }
        @Override
        public int getCount() {
            return clients.size();
        }

        @Override
        public Object getItem(int position) {
            return clients.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.client_item_view_v2, null);


            TextView nom=(TextView)convertView.findViewById(R.id.client_name);
            TextView phone=(TextView)convertView.findViewById(R.id.client_phone);
            TextView solde=(TextView)convertView.findViewById(R.id.client_soldee);
            TextView id=(TextView)convertView.findViewById(R.id.clientid);

            Client client=clients.get(position);
            nom.setText(client.getNom());
            phone.setText(client.getNumero());
            solde.setText(String.valueOf(client.getSolde()));
            id.setText(client.getId());
            return convertView;
        }

        public void filter(String cs) {
            cs = cs.toLowerCase(Locale.getDefault());
            clients.clear();
            if (cs.length() == 0) {
                clients.addAll(arrayliste);
            } else {
                for (Client wp : arrayliste) {
                    if (wp.getNom().toLowerCase(Locale.getDefault())
                            .contains(cs)) {
                        clients.add(wp);
                    }
                    if (wp.getNumero().toLowerCase(Locale.getDefault())
                            .contains(cs)) {
                        clients.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /*public void onBackPressed(){

        Intent gotostart = new Intent(this, NewInterface.class);
        startActivity(gotostart);
        finish();
    }*/
}
