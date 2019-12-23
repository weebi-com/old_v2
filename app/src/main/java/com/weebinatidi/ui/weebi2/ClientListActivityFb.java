package com.weebinatidi.ui.weebi2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.Client;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.OperationClientRepository;
import com.weebinatidi.ui.BaseActivity;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmsearchview.RealmSearchView;
import io.realm.Realm;
import io.realm.RealmQuery;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;

public class ClientListActivityFb extends BaseActivity {

    private static final String TAG = ClientListActivityFb.class.getSimpleName();
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    final DatabaseReference databaseReference1 = databaseReference.child(user.getUid()).child("Weebi2").child("Clients");


    private RealmSearchView mClientList;
    private Realm realm;
    private boolean sortingname=true;
    private boolean sortbalance=true;
    private Button rechercher, menu, ajout, client_balance_sort;
    private ListView listeclient;
    private AdapterClient adapterClient;
    private List<Integer> lesnumeros=new ArrayList<>();
    private List<Client> lesclientsfb=new ArrayList<>();

    private String lenumero="";
    BroadcastReceiver broadcastReceiver;


    AlertDialog.Builder RegisterClientbuilder;
    AlertDialog RegisterClientDialog;
    AlertDialog.Builder MenuClientbuilder;
    AlertDialog MenuClientDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listeclient=(ListView)findViewById(R.id.search_client_view2);

        adapterClient=new AdapterClient(this,lesclientsfb);
        listeclient.setAdapter(adapterClient);

        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_LONG).show();
                String lenom=dataSnapshot.child("nom").getValue(String.class);
                String lenum=dataSnapshot.child("numero").getValue(String.class);

                int numconv=Integer.parseInt(lenum);
                lesnumeros.add(numconv);
                Log.d("les numeros dans fb ", ""+lesnumeros);

                Client client=new Client();
                client.setNumero(lenum);
                client.setNom(lenom);
                lesclientsfb.add(client);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // testerconnexion();
        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
        if (isTablet(this)||isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else if(isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(getApplicationContext(), "bdfirebase "+databaseReference, Toast.LENGTH_SHORT).show();
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    //Toast.makeText(getApplicationContext(), "pas de user ", Toast.LENGTH_SHORT).show();
                    //finish();
                }
                else {
                    //Toast.makeText(getApplicationContext(), " user "+user.getUid(), Toast.LENGTH_SHORT).show();
                }

            }
        };



        listeclient=(ListView)findViewById(R.id.search_client_view2);

        ajout=(Button)findViewById(R.id.add_cliente);
        client_balance_sort=(Button)findViewById(R.id.client_balance_sort);


        adapterClient.notifyDataSetChanged();

        /*listeclient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE){
                    String lenum=((TextView)view.findViewById(R.id.client_phone)).getText().toString();
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ClientListActivityFb.this, ClientPageActivityNew2.class);
                    intent.putExtra(ClientPageActivityNew.EXTRA_CLIENT_PHONE, lenum);
                    intent.putExtra(ClientPageActivityNew.EXTRA_CLIENT_ARCHIVE, "nonarchived");
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else {
                    String lenum=((TextView)view.findViewById(R.id.client_phone)).getText().toString();
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ClientListActivityFb.this, ClientPagePortrait.class);
                    intent.putExtra(ClientPageActivityNew.EXTRA_CLIENT_PHONE, lenum);
                    intent.putExtra(ClientPageActivityNew.EXTRA_CLIENT_ARCHIVE, "nonarchived");
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }


            }
        });*/



        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClient();
            }


        });

        /*client_balance_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sortbalance)
                {
                    listeclient.setVisibility(View.INVISIBLE);
                    mClientList = (RealmSearchView) findViewById(R.id.search_client_view);

                    mAdapter = new ClientRecyclerViewAdapter(getApplicationContext(), realm, "numero","solde", Sort.DESCENDING);
                    mClientList.setAdapter(mAdapter);
                    mClientList.setVisibility(View.VISIBLE);
                }
                else
                {
                    listeclient.setVisibility(View.INVISIBLE);
                    mClientList = (RealmSearchView) findViewById(R.id.search_client_view);
                    mAdapter = new ClientRecyclerViewAdapter(getApplicationContext(), realm, "numero","solde", Sort.ASCENDING);
                    mClientList.setAdapter(mAdapter);
                    mClientList.setVisibility(View.VISIBLE);
                }
                sortbalance=!sortbalance;
            }
        });*/



        rechercher=(Button)findViewById(R.id.search);
        menu=(Button)findViewById(R.id.menu);

        /*rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on cache la listview et on montre la listview avec possibilité de recherche
                listeclient.setVisibility(View.INVISIBLE);
                mClientList = (RealmSearchView) findViewById(R.id.search_client_view);

                ClearableEditText Edit = (ClearableEditText)mClientList.findViewById(R.id.search_bar);
                Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                Edit.setSingleLine(true);


                mAdapter = new ClientRecyclerViewAdapter(getApplicationContext(), realm, "numero","nom", Sort.DESCENDING);
                mClientList.setAdapter(mAdapter);
                mClientList.setVisibility(View.VISIBLE);
            }
        });*/

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menus();
            }
        });
    }

    public  void ajoutclientfb(final String nom, final String numero, final AlertDialog dialog){

        boolean contain = false;

        int valeurATrouver = Integer.parseInt(numero);


        for (int i = 0; i < lesnumeros.size(); i++) {
            if (lesnumeros.get(i) == valeurATrouver) {
                contain = true;
            }
        }

        if (contain) {
            Toast.makeText(getApplicationContext(),R.string.numalreadyused,Toast.LENGTH_SHORT).show();

        } else {
            //Toast.makeText(getApplicationContext()," pas trouvé ",Toast.LENGTH_SHORT).show();

            Client client=new Client();
            client.setNom(nom);
            client.setNumero(numero);
            databaseReference1.push().setValue(client);
            dialog.dismiss();
        }


    }

    private void testerconnexion() {

        if (broadcastReceiver == null) {

            broadcastReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();

                    NetworkInfo info = (NetworkInfo) extras
                            .getParcelable("networkInfo");

                    NetworkInfo.State state = info.getState();
                    Log.d("Internal", info.toString()+""+ state.toString());

                    if (state == NetworkInfo.State.CONNECTED) {

                        Toast.makeText(getApplicationContext(),"connecté",Toast.LENGTH_LONG).show();
                        //startService(new Intent(getBaseContext(), ServiceSauvegardata.class));

                    } else {

                        Toast.makeText(getApplicationContext(),"non connecté à internet",Toast.LENGTH_LONG).show();

                    }

                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(broadcastReceiver, intentFilter);
        }
    }



    public void menus() {
        MenuClientbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_menu_client_liste, null, false);

        final Button balance = (Button) customView.findViewById(R.id.client_balance_sort);
        final Button alphabetic = (Button) customView.findViewById(R.id.client_alphabetical_sort);
        final Button addclient = (Button) customView.findViewById(R.id.add_cliente);

        addclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClient();
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

        solde.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        RegisterClientbuilder.setCancelable(true);
        RegisterClientbuilder.setView(customView);
        RegisterClientbuilder.setTitle(getString(R.string.ajouter_un_client));


        RegisterClientDialog = RegisterClientbuilder.create();


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernames=username.getText().toString();
                String phones=phone.getText().toString();

                if(phones.equals("")|| usernames.equals("")){
                    Toast.makeText(getApplicationContext(),"veuillez remplir tous les champs merci",Toast.LENGTH_SHORT).show();
                }
                else {
                    ajoutclientfb(usernames,phones,RegisterClientDialog);
                    //RegisterClientDialog.dismiss();
                }

                //RegisterProcess(username,phone,phone,email,solde,RegisterClientDialog);

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

                RealmQuery<ClientRepository> query = realm.where(ClientRepository.class);
                ClientRepository tmp = query.equalTo("numero",phone.getText().toString()).findFirst();


                if(tmp == null)
                {
                    String clientName = username.getText().toString();
                    String clientPhone = phone.getText().toString();
                    String clientSolde = solde.getText().toString();
//                    String clientmail = email.getText().toString();

                    realm.beginTransaction();
                    // Add the client
                    ClientRepository client = realm.createObject(ClientRepository.class);
                    client.setNom(clientName);
                    client.setNumero(clientPhone);
//                    client.setMail(clientmail);
                    realm.commitTransaction();

                    //realm.beginTransaction();

                    ClientRepository clienttmp = query.equalTo("numero",phone.getText().toString()).findFirst();
                    // Add the deposit to the client account
                    if(clienttmp != null)
                    {
                        if (!TextUtils.isEmpty(clientSolde)) {
                            //  client.setSolde(Integer.valueOf(clientSolde));

                            //OperationClientRepository depot = realm.createObject(OperationClientRepository.class);
                            //depot.setClient(client);
                            // depot.setMontant(Integer.valueOf(clientSolde));
                            //depot.setDate(Calendar.getInstance().getTime());
                            //TODO should include the total invoices here and add it to this operation
                            //client.getOperationClients().add(depot);

                            ProcessToClientDeposit(realm,clienttmp,clientSolde);

                        }

//                    realm.commitTransaction();

                        /*Snackbar.make(mDisplayView, R.string.Clientwithpositivebalancecreated,
                                Snackbar.LENGTH_SHORT).show();*/
                        //Toast.makeText(getApplicationContext(),""+R.string.Clientwithpositivebalancecreated,Toast.LENGTH_SHORT).show();
                    }


                    alertDialog.dismiss();
                    Intent intent=new Intent(getApplicationContext(),ClientListActivityFb.class);
                    startActivity(intent);
                    finish();
//                    RegisterClientDialog.dismiss();
                    // check the validity of the entry
                    // register the user in the database
                    // send confirmation message to the operator
                }
                else
                {
                    phone2.setError(getString(R.string.numalreadyused),getResources().getDrawable(R.drawable.add_user));
                    Snackbar.make(phone,getString(R.string.numalreadyused), Snackbar.LENGTH_SHORT).show();
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

        int invoicegoid= Config.getLastInvoiceId(ClientListActivityFb.this)+1;
//                            InvoiceRepository invoicedepot= new InvoiceRepository();
        InvoiceRepository invoicedepot= new InvoiceRepository();
        invoicedepot.setId(invoicegoid);
        Config.setLastInvoiceId(ClientListActivityFb.this, invoicegoid);
        invoicedepot.setDate(Calendar.getInstance().getTime());
        invoicedepot.setTotal(Integer.valueOf(soldes));
        invoicedepot.setClient(client);
        invoicedepot.setInvoiceRepo(false);
        int itemgoid= Config.getLastItemId(ClientListActivityFb.this)+1;
        ItemRepository itemdepot= new ItemRepository();
        itemdepot.setId(itemgoid);
        Config.setLastItemId(ClientListActivityFb.this,itemgoid);
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
        return 0;
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


    public class AdapterClient extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;
        private List<ClientRepository> clientRepositories;
        private List<Client> lesclientsfb;
        private Context context;

        public AdapterClient(Activity activity, List<Client> lesclientsfb){
            this.activity=activity;
            this.lesclientsfb=lesclientsfb;
        }
        @Override
        public int getCount() {
            return lesclientsfb.size();
        }

        @Override
        public Object getItem(int position) {
            return lesclientsfb.get(position);
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
                convertView = inflater.inflate(R.layout.client_item_view2, null);


            TextView nom=(TextView)convertView.findViewById(R.id.client_name);
            TextView phone=(TextView)convertView.findViewById(R.id.client_phone);
            //TextView solde=(TextView)convertView.findViewById(R.id.client_soldee);

            Client client=lesclientsfb.get(position);
            nom.setText(client.getNom());
            phone.setText(client.getNumero());
            // solde.setText(clientRepository.getSolde());
            return convertView;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        realm.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }
}
