package com.weebinatidi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.ArchiveClient;
import com.weebinatidi.model.Boutique;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.Item;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.OperationClientRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.weebinatidi.Config.formaterSolde;
import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;
import static com.weebinatidi.ui.InvoiceDetailsActivity.EXTRA_IS_BILL_OR_DEPOSIT;
import static com.weebinatidi.ui.InvoiceDetailsActivity.EXTRA_PRINTINVOICE;
import static com.weebinatidi.ui.print.PrintDemo.textToBePrinted;

public class ClientPageActivity extends BaseActivity {

    public static final String EXTRA_CLIENT_PHONE = "CLIENT_PHONE";
    public static final String EXTRA_CLIENT_ARCHIVE = "CLIENT_ARCHIVE";
    ArchiveClient tmpclient;
    //ajout pisco
    String montantsolde = "";
    String datesolde = "";
    AlertDialog.Builder RegisterClientbuilder;
    AlertDialog RegisterClientDialog;
    private String TAG = ClientPageActivity.class.getSimpleName();
    private ListView mDepotList;
    private TextView mClientName;
    private TextView mClientSolde;
    private ImageView mSoldeStateIcon;
    private TextView mClientPhone;
    private TextView mClientEmail;
    private Realm realm;
    private String mPhone;
    private String archived;
    private DepositAdapter mAdapter;
    private ClientRepository thisClient;
    private OperationClientRepository operationClientRepository;
    private ArrayList<String> lesdates = new ArrayList<>();
    private ArrayList<Integer> lesprisescon = new ArrayList<>();
    private ArrayList<Integer> lesidfactures = new ArrayList<>();
    private RealmChangeListener reamlListener;
    private Button menu, addcoin, editer, imprimer, prise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }

        // Get the client phone
        mPhone = getIntent().getStringExtra(EXTRA_CLIENT_PHONE);
        archived = getIntent().getStringExtra(EXTRA_CLIENT_ARCHIVE);


        mDepotList = (ListView) findViewById(R.id.deposit_liste);
        menu = (Button) findViewById(R.id.menu);

        // Instantiate the realm object.
        realm = Realm.getDefaultInstance();

        reamlListener = new RealmChangeListener() {
            @Override
            public void onChange(Object element) {

                mAdapter.notifyDataSetChanged();
            }
        };
        realm.addChangeListener(reamlListener);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrir();
            }
        });

        prise = (Button) findViewById(R.id.prises);

        addcoin = (Button) findViewById(R.id.client_deposit);
        addcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClientDeposit();
            }
        });

        editer = (Button) findViewById(R.id.edit_client);
        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClient();
            }
        });

        imprimer = (Button) findViewById(R.id.print_invoice_action);
        imprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomcli = thisClient.getNom();
                String numero = thisClient.getNumero();
                //String montant=thisClient.getOperationClients()operationClientRepository.getMontant().

                RealmQuery<ClientRepository> query = realm.where(ClientRepository.class);
                RealmResults<ClientRepository> cr = query.contains("numero", numero).findAll();//recuperation des infos clients selectionné

                //récupération de tous les soldes du client actuel
               /* RealmQuery<OperationClientRepository> query = realm.where(OperationClientRepository.class);
                RealmResults<OperationClientRepository> ocp = query.distinct(numero);*/
                Log.d(TAG, "" + cr);

                //int taille=ocp.size();
                //Toast.makeText(getApplicationContext(),""+taille,Toast.LENGTH_LONG).show();

                for (ClientRepository c : cr) {
                    Log.d(TAG, "" + c.getOperationClients());

                    RealmList<OperationClientRepository> liste = c.getOperationClients();


                    //Log.d(TAG,""+o.getMontant());

                    Boutique boutique = realm.where(Boutique.class).findFirst();

                    textToBePrinted =
                            ((boutique != null && !TextUtils.isEmpty(boutique.getNom())) ?
                                    getString(R.string.shop_name) + boutique.getNom() : " ...") +
                                    getString(R.string.shop_call) + ((boutique != null && !TextUtils.isEmpty(boutique.getNumero()))
                                    ? boutique.getNumero() : "...") +
                                    (thisClient != null ?
                                            getString(R.string.client_name) + thisClient.getNom() + getString(R.string.client_call) + thisClient.getNumero() : getString(R.string.vente)) +
                                    getString(R.string.soldeimpression) + formaterSolde(thisClient.getSolde()) +
                                    "\n ______________________________\n";


                    //permettre d'imprimer toutes les transactions
                    /*for(OperationClientRepository o:liste){
                        //Toast.makeText(getApplicationContext(),"Facture du : "+c.getDate()+"\nMontant : "+c.getMontant(),Toast.LENGTH_LONG).show();
                        //c.getMontant();
                        textToBePrinted += getString(R.string.facture_of)+o.getDate().toString()+getString(R.string.montant)+formaterSolde(o.getMontant());
                    }*/

                    //Toast.makeText(getApplicationContext(),""+textToBePrinted,Toast.LENGTH_LONG).show();

                    if (!TextUtils.isEmpty(textToBePrinted)) {
//
                        Intent printerIntent = new Intent(getApplicationContext(), com.weebinatidi.ui.print.PrintDemo.class);
                        printerIntent.putExtra(EXTRA_PRINTINVOICE, "print");

                        printerIntent.putExtra(EXTRA_IS_BILL_OR_DEPOSIT, true);
                        startActivity(printerIntent);
//
                    }


                    //Toast.makeText(getApplicationContext(),"Facture du : "+c.getDate()+"\nMontant : "+c.getMontant(),Toast.LENGTH_LONG).show();
                    // RegisterClientDialog.dismiss();
                }
            }

        });


    }


    public void ouvrir() {
        RegisterClientbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_menu_client_page, null, false);

        final Button imprime = (Button) customView.findViewById(R.id.print_invoice_action);
        final Button editer = (Button) customView.findViewById(R.id.edit_client);
        final Button addcoin = (Button) customView.findViewById(R.id.client_deposit);


        addcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClientDeposit();
                RegisterClientDialog.dismiss();
            }
        });


        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClient();
            }
        });

        imprime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomcli = thisClient.getNom();
                String numero = thisClient.getNumero();
                //String montant=thisClient.getOperationClients()operationClientRepository.getMontant().

                RealmQuery<ClientRepository> query = realm.where(ClientRepository.class);
                RealmResults<ClientRepository> cr = query.contains("numero", numero).findAll();//recuperation des infos clients selectionné

                //récupération de tous les soldes du client actuel
               /* RealmQuery<OperationClientRepository> query = realm.where(OperationClientRepository.class);
                RealmResults<OperationClientRepository> ocp = query.distinct(numero);*/
                Log.d(TAG, "" + cr);

                //int taille=ocp.size();
                //Toast.makeText(getApplicationContext(),""+taille,Toast.LENGTH_LONG).show();

                for (ClientRepository c : cr) {
                    Log.d(TAG, "" + c.getOperationClients());

                    RealmList<OperationClientRepository> liste = c.getOperationClients();


                    //Log.d(TAG,""+o.getMontant());

                    Boutique boutique = realm.where(Boutique.class).findFirst();

                    textToBePrinted =
                            ((boutique != null && !TextUtils.isEmpty(boutique.getNom())) ?
                                    getString(R.string.shop_name) + boutique.getNom() : " ...") +
                                    getString(R.string.shop_call) + ((boutique != null && !TextUtils.isEmpty(boutique.getNumero()))
                                    ? boutique.getNumero() : "...") +
                                    (thisClient != null ?
                                            getString(R.string.client_name) + thisClient.getNom() + getString(R.string.client_call) + thisClient.getNumero() : getString(R.string.vente)) +
                                    getString(R.string.soldeimpression) + formaterSolde(thisClient.getSolde()) +
                                    "\n ______________________________\n";


                    //permettre d'imprimer toutes les transactions
                    /*for(OperationClientRepository o:liste){
                        //Toast.makeText(getApplicationContext(),"Facture du : "+c.getDate()+"\nMontant : "+c.getMontant(),Toast.LENGTH_LONG).show();
                        //c.getMontant();
                        textToBePrinted += getString(R.string.facture_of)+o.getDate().toString()+getString(R.string.montant)+formaterSolde(o.getMontant());
                    }*/

                    //Toast.makeText(getApplicationContext(),""+textToBePrinted,Toast.LENGTH_LONG).show();

                    if (!TextUtils.isEmpty(textToBePrinted)) {
//
                        Intent printerIntent = new Intent(getApplicationContext(), com.weebinatidi.ui.print.PrintDemo.class);
                        printerIntent.putExtra(EXTRA_PRINTINVOICE, "print");

                        printerIntent.putExtra(EXTRA_IS_BILL_OR_DEPOSIT, true);
                        startActivity(printerIntent);
//
                    }


                    //Toast.makeText(getApplicationContext(),"Facture du : "+c.getDate()+"\nMontant : "+c.getMontant(),Toast.LENGTH_LONG).show();
                    RegisterClientDialog.dismiss();
                }
            }
        });

        RegisterClientbuilder.setCancelable(true);
        RegisterClientbuilder.setView(customView);
        // RegisterClientbuilder.setTitle(getString(R.string.ajouter_un_client));


        RegisterClientDialog = RegisterClientbuilder.create();
        RegisterClientDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        lesprisescon = getIntent().getIntegerArrayListExtra("lesiditems");

        lesprisescon = new ArrayList<>();


        if (!TextUtils.isEmpty(archived)) {
            if (archived.equals("nonarchived")) {
                // get the actual client info.
                thisClient = realm.where(ClientRepository.class).equalTo("numero", mPhone).findFirst();
            } else {
                // get the actual archive client info and copy it n the client placeholder.

                tmpclient = realm.where(ArchiveClient.class).equalTo("numero", mPhone).findFirst();

                if (tmpclient != null) {
                    thisClient = new ClientRepository();
                    thisClient.setNom(tmpclient.getNom());
                    thisClient.setMail(tmpclient.getMail());
                    thisClient.setNumero(tmpclient.getNumero());
                    thisClient.setMail(tmpclient.getMail());
                    thisClient.setOperationClients(tmpclient.getOperationClients());
                    thisClient.setSolde(tmpclient.getSolde());
                }
            }
        }



            /*if(!TextUtils.isEmpty(archived))
            {
                if(archived.equals("nonarchived"))
                {
                    // get the actual client info.
                    thisClient = realm.where(ClientRepository.class).equalTo("numero", mPhone).findFirst();
                }
                else
                {
                    // get the actual archive client info and copy it n the client placeholder.

                    tmpclient = realm.where(ArchiveClient.class).equalTo("numero", mPhone).findFirst();

                    if(tmpclient != null)
                    {
                        thisClient=new ClientRepository();
                        thisClient.setNom(tmpclient.getNom());
                        thisClient.setMail(tmpclient.getMail());
                        thisClient.setNumero(tmpclient.getNumero());
                        thisClient.setMail(tmpclient.getMail());
                        thisClient.setOperationClients(tmpclient.getOperationClients());
                        thisClient.setSolde(tmpclient.getSolde());
                    }
                }
            }*/


        //getSupportActionBar().setTitle(thisClient.getNom());

        setupHeaderView();


        //Log.d("ClientPageActivity", "ClientRepository depost size : " + thisClient.getOperationClient().size());
        mAdapter = new DepositAdapter(thisClient.getOperationClients());
        mDepotList.setAdapter(mAdapter);


        mDepotList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // show the invoice details.

                Log.d(TAG, " posiinside " + position);

//                Log.d(TAG," size "+mDepotList.getAdapter().getCount());
//                if(position != 0)
//                {
                //because the header has been added the new size is the list size +1 so we need to remove that 1 to make sure
                // we dont get arayboundexception as if not we going out of the array...
//                    Log.d(TAG,"idfacture"+thisClient.getOperationClients().get(0).getInvoiceRepository().getItems().size());
                if ((thisClient.getOperationClients().get(position).getInvoiceRepository() != null) && (thisClient.getOperationClients().get(position).isInvoice() == true)) {
                    Log.d(TAG, "idfacture" + thisClient.getOperationClients().get(position).getInvoiceRepository().getId());

                    InvoiceRepository invoice = thisClient.getOperationClients().get(position).getInvoiceRepository();
                    Intent intent = new Intent(ClientPageActivity.this, InvoiceDetailsActivity.class);
//                    Log.d(TAG,"idfacture"+invoice.getItems().size());
                    intent.putExtra(InvoiceDetailsActivity.EXTRA_INVOICE_ID, invoice.getId());
                    intent.putExtra(InvoiceDetailsActivity.EXTRA_ISARCHIVE, false);
                    startActivity(intent);
                } else {
//                    InvoiceRepository invoice = thisClient.getOperationClients().get(position).getInvoiceRepository();
//                    Intent intent = new Intent(ClientPageActivity.this, InvoiceDetailsActivity.class);
////                    Log.d(TAG,"idfacture"+invoice.getItems().size());
//                    intent.putExtra(InvoiceDetailsActivity.EXTRA_INVOICE_ID, invoice.getId());
//                    intent.putExtra(InvoiceDetailsActivity.EXTRA_ISARCHIVE,false);
//                    startActivity(intent);
                }

//
//                }

            }
        });

        /*mDepotList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkbox);
                checkBox.setChecked(true);
                boolean bon=checkBox.isChecked();
                if(bon==true){
                    Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_LONG).show();
                }
                if((thisClient.getOperationClients().get(position).getInvoiceRepository() != null) &&  (thisClient.getOperationClients().get(position).isInvoice() == true ))
                {

                    InvoiceRepository invoice = thisClient.getOperationClients().get(position).getInvoiceRepository();
                    //String lid=String.valueOf(invoice.getId());

                    int ide=invoice.getId();
                    Date date=invoice.getDate();
                    String ladate=getFormattedDate(date);
                    lesdates.add(ladate);
                    RealmList<ItemRepository> listitem = invoice.getItems();
                    RealmResults<ItemRepository> resultsitem = listitem.where().findAll();
                    for (ItemRepository it : resultsitem) {
                        Item item = new Item();
                        item.setId(it.getId());
                        lesprisescon.add(it.getId());

                    }
                       // lesprisescon.add(ide);
                    Toast.makeText(getApplicationContext(),""+lesdates,Toast.LENGTH_LONG).show();
                    view.setBackgroundColor(Color.parseColor("#F40056"));

                }

                return true;
            }
        });*/

        prise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lenom = thisClient.getNom();
                String lenum = thisClient.getNumero();
                //Toast.makeText(getApplicationContext(),""+maliste+"\n"+lenom+"\n"+lenum,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ClientPageActivity.this, FacturesSelect.class);
                //intent.putExtra("lesid", maliste[]);
                //intent.putStringArrayListExtra("lesid",lesprisescon);
                intent.putIntegerArrayListExtra("lesiditems", lesprisescon);
                intent.putIntegerArrayListExtra("lesidfact", lesidfactures);
                intent.putExtra(ClientPageActivity.EXTRA_CLIENT_ARCHIVE, lenom);
                intent.putExtra(ClientPageActivity.EXTRA_CLIENT_PHONE, lenum);
                startActivity(intent);

            }
        });

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_client_page;
    }

    private void setupHeaderView() {
//        View headerView = getLayoutInflater().inflate(R.layout.client_resume, null, false);
//        mClientName = (TextView) headerView.findViewById(R.id.client_name);
//        mClientSolde = (TextView) headerView.findViewById(R.id.client_solde);
//        mClientPhone = (TextView) headerView.findViewById(R.id.client_phone);
//        mSoldeStateIcon = (ImageView) headerView.findViewById(R.id.solde_icon);

        mClientName = (TextView) findViewById(R.id.client_name);
        mClientSolde = (TextView) findViewById(R.id.client_solde);
        mClientPhone = (TextView) findViewById(R.id.client_phone);
        mClientEmail = (TextView) findViewById(R.id.pclient_mail);
        mSoldeStateIcon = (ImageView) findViewById(R.id.solde_icon);

        mClientEmail.setVisibility(View.INVISIBLE);

//
//        mClientName.setText(thisClient.getNom());
//        mClientPhone.setText(thisClient.getNumero());
//        mClientSolde.setText(String.valueOf(thisClient.getSolde()));
//        mClientEmail.setText(thisClient.getMail());

        CheckStatusAndInsertData(mClientName, thisClient.getNom());
        CheckStatusAndInsertData(mClientPhone, thisClient.getNumero());
        CheckStatusAndInsertData(mClientSolde, String.valueOf(formaterSolde(thisClient.getSolde())));
        CheckStatusAndInsertData(mClientEmail, thisClient.getMail());

        if (thisClient.getSolde() > 0) {
            mSoldeStateIcon.setImageResource(R.drawable.good_quality);
        } else if (thisClient.getSolde() < 0) {
            mSoldeStateIcon.setImageResource(R.drawable.poor_quality);
        } else {
            mSoldeStateIcon.setImageResource(R.drawable.ic_thumbs_up_down_grey600_48dp);
        }

//        mDepotList.addHeaderView(headerView);
    }

    public void CheckStatusAndInsertData(TextView tv, String data) {
        if (!TextUtils.isEmpty(data)) {
            tv.setText(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_page, menu);

//        MenuItem item = menu.findItem(R.id.edit_client_archived);
//        if(!TextUtils.isEmpty(archived))
//        {
//            if(archived.equals("nonarchived"))
//            {
//                item.setTitle("delete client");
//            }
//            else
//            {
//                item.setTitle("restore client");
//            }
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            //TODO now we just edit the client no need to delete him anymore...
           /* case R.id.print_invoice_action:

                String nomcli=thisClient.getNom();
                String numero=thisClient.getNumero();
                 //String montant=thisClient.getOperationClients()operationClientRepository.getMontant().

                RealmQuery<ClientRepository> query=realm.where(ClientRepository.class);
                RealmResults<ClientRepository> cr=query.contains("numero",numero).findAll();//recuperation des infos clients selectionné



                //récupération de tous les soldes du client actuel
               /* RealmQuery<OperationClientRepository> query = realm.where(OperationClientRepository.class);
                RealmResults<OperationClientRepository> ocp = query.distinct(numero);*
                Log.d(TAG,""+cr);



                //int taille=ocp.size();
                //Toast.makeText(getApplicationContext(),""+taille,Toast.LENGTH_LONG).show();

                for(ClientRepository c:cr) {
                    Log.d(TAG,""+c.getOperationClients());

                    RealmList<OperationClientRepository> liste=c.getOperationClients();


                        //Log.d(TAG,""+o.getMontant());

                        Boutique boutique = realm.where(Boutique.class).findFirst();

                        textToBePrinted =
                                ((boutique != null && !TextUtils.isEmpty(boutique.getNom())) ?
                                        getString(R.string.shop_name)+boutique.getNom() : " ...") +
                                        getString(R.string.shop_call) + ((boutique != null && !TextUtils.isEmpty(boutique.getNumero()))
                                        ? boutique.getNumero() : "...") +
                                        (thisClient != null ?
                                                getString(R.string.client_name) + thisClient.getNom() + getString(R.string.client_call) + thisClient.getNumero() : getString(R.string.vente)) +
                                        getString(R.string.date)  +
                                        "\n ______________________________\n" ;

                    for(OperationClientRepository o:liste){
                            //Toast.makeText(getApplicationContext(),"Facture du : "+c.getDate()+"\nMontant : "+c.getMontant(),Toast.LENGTH_LONG).show();
                            //c.getMontant();
                            textToBePrinted += getString(R.string.facture_of)+o.getDate().toString()+getString(R.string.montant)+o.getMontant();
                        }

                        Toast.makeText(getApplicationContext(),""+textToBePrinted,Toast.LENGTH_LONG).show();

                        if(!TextUtils.isEmpty(textToBePrinted))
                        {
//
                            Intent printerIntent = new Intent(this, com.weebinatidi.ui.print.PrintDemo.class);
                            printerIntent.putExtra(EXTRA_PRINTINVOICE,"print");

                            printerIntent.putExtra(EXTRA_IS_BILL_OR_DEPOSIT,true);
                            startActivity(printerIntent);
//
                        }



                    //Toast.makeText(getApplicationContext(),"Facture du : "+c.getDate()+"\nMontant : "+c.getMontant(),Toast.LENGTH_LONG).show();

                }






            break;*/

            //TODO now we just edit the client no need to delete him anymore...
           /* case R.id.edit_client:

                onEditClient();
//                onDeleteClient();
                break;

            case R.id.client_deposit:

                Log.d(TAG," depot solde client");
                //TODO Add a custon icon on action to let the dealer put client deposit Q10  Done
                onClientDeposit();

                break;*/
        }

//        switch (item.getItemId())
//        {
//            case R.id.client_archived:
//
//                break;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void onClientDeposit() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_new_deposit, null, false);
//        final Spinner clientSpinner = (Spinner) customView.findViewById(R.id.select_client);
//        ImageButton addClient = (ImageButton) customView.findViewById(R.id.add_client);
        final EditText clientSolde = (EditText) customView.findViewById(R.id.client_solde);
        final ImageView okbtn = (ImageView) customView.findViewById(R.id.okdepositclient);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(clientSolde, InputMethodManager.SHOW_IMPLICIT);
        // get all the clients


        builder.setView(customView);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.registerdepot));
        builder.setMessage(getString(R.string.client_deposit));

        final AlertDialog depositDialog = builder.create();


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSolde.setError(null);
                ClientRepository selectedClient = thisClient;
                // validate the solde
                String solde = clientSolde.getText().toString();

                InvoiceRepository invoiceRepository = null;
                if (!TextUtils.isEmpty(solde)) {
                    if (selectedClient != null) {
                        realm.beginTransaction();
                        int invoiceidt = ProcessToClientDeposit(realm, selectedClient, solde);

                        invoiceRepository = realm.where(InvoiceRepository.class).equalTo("id", invoiceidt).findFirst();


                        realm.commitTransaction();

                        //print a bill
                        // PrintDepot(selectedClient,invoiceRepository,solde);

                        Snackbar.make(mDepotList, getString(R.string.solderegistersuccess), Snackbar.LENGTH_SHORT).show();
                        depositDialog.dismiss();
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

    public void PrintDepot(ClientRepository client, InvoiceRepository invoice, String solde) {

        Log.d(TAG, "print invoice and send it");

        // Get the boutique information
        Boutique boutique = realm.where(Boutique.class).findFirst();

        com.weebinatidi.ui.print.PrintDemo.textToBePrinted =
                ((boutique != null && !TextUtils.isEmpty(boutique.getNom())) ?
                        boutique.getNom() : " ...") +
                        getString(R.string.name) + ((boutique != null && !TextUtils.isEmpty(boutique.getNumero()))
                        ? boutique.getNumero() : "...") +
                        getString(R.string.client_name) + client.getNom() +
                        getString(R.string.client_call) + client.getNumero() +
                        getString(R.string.depot) +
                        getString(R.string.date) + Config.getFormattedDate(invoice.getDate()) +
                        getString(R.string.solde_ajoute) + solde +
                        "\n ______________________________\n" +
                        getString(R.string.reference_unit_total);


        int totalise = client.getSolde();
        com.weebinatidi.ui.print.PrintDemo.textToBePrinted += getString(R.string.solde_final) + totalise + " CFA";
        com.weebinatidi.ui.print.PrintDemo.textToBePrinted += getString(R.string.merci);


        Log.d("dataprint", " " + com.weebinatidi.ui.print.PrintDemo.textToBePrinted);


        if (!TextUtils.isEmpty(textToBePrinted)) {
//                        //first print...
//                        mService.sendMessage(textToBePrinted+"\n", "GBK");
//                        //send again the message to print it again... TEST
//                        mService.sendMessage(textToBePrinted+"\n", "GBK");

            Intent printerIntent = new Intent(this, com.weebinatidi.ui.print.PrintDemo.class);
            printerIntent.putExtra(EXTRA_PRINTINVOICE, "print");
            printerIntent.putExtra(EXTRA_IS_BILL_OR_DEPOSIT, false);
            printerIntent.putExtra(ClientPageActivity.EXTRA_CLIENT_PHONE, client.getNumero());
            printerIntent.putExtra(ClientPageActivity.EXTRA_CLIENT_ARCHIVE, "nonarchived");
            startActivity(printerIntent);
//                        com.weebipro.ui.print.PrintDemo.GoPrintit();
        }

//        Intent printerIntent = new Intent(this, com.weebipro.ui.print.PrintDemo.class);
//        startActivity(printerIntent);


    }

    public int ProcessToClientDeposit(Realm realms, ClientRepository client, String soldes) {
        // Add the transaction to the deposit
        OperationClientRepository depot = realms.createObject(OperationClientRepository.class);
        depot.setClient(client);
        depot.setMontant(Integer.valueOf(soldes));
        depot.setDate(Calendar.getInstance().getTime());
        depot.setInvoice(false);
        //********************TEMP********************//

        int invoicegoid = Config.getLastInvoiceId(ClientPageActivity.this) + 1;
//                            InvoiceRepository invoicedepot= new InvoiceRepository();
        InvoiceRepository invoicedepot = new InvoiceRepository();
        invoicedepot.setId(invoicegoid);
        Config.setLastInvoiceId(ClientPageActivity.this, invoicegoid);
        invoicedepot.setDate(Calendar.getInstance().getTime());
        invoicedepot.setTotal(Integer.valueOf(soldes));
        invoicedepot.setClient(client);
        invoicedepot.setInvoiceRepo(false);

        int itemgoid = Config.getLastItemId(ClientPageActivity.this) + 1;
        ItemRepository itemdepot = new ItemRepository();
        itemdepot.setId(itemgoid);
        Config.setLastItemId(ClientPageActivity.this, itemgoid);
        itemdepot.setName(getString(R.string.depot_du) + Config.getFormattedDate(Calendar.getInstance().getTime()));
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

        return invoicegoid;
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
        email.setVisibility(View.GONE);
        solde.setVisibility(View.GONE);


//look for the shop in the database...
        RealmQuery<ClientRepository> query = realm.where(ClientRepository.class).equalTo("numero", thisClient.getNumero());
        RealmResults<ClientRepository> size = query.findAll();
        Log.d(" taille ", " " + size.size());
        ClientRepository tmp = query.findFirst();


        if (tmp != null) {
            username.setText(tmp.getNom());
            phone.setText(tmp.getNumero());
            //phone.setText(tmp.getNumero());
//            email.setText(tmp.getMail());

        }


        RegisterClientbuilder.setCancelable(true);

//        RegisterClientbuilder.setInverseBackgroundForced(true);
        RegisterClientbuilder.setTitle(getString(R.string.edit_client));
        RegisterClientDialog = RegisterClientbuilder.create();

        RegisterClientDialog.show();
        final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewclient);


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessEditclient(username, phone, phone, email, solde, RegisterClientDialog);
                /*Intent intent=new Intent(getApplicationContext(),ClientPageActivity.class);
                intent.putExtra(ClientPageActivity.EXTRA_CLIENT_PHONE, ""+phone);
                intent.putExtra(ClientPageActivity.EXTRA_CLIENT_ARCHIVE, "nonarchived");
                startActivity(intent);
                finish();*/
            }
        });


    }

    public void ProcessEditclient(FormEditText username, FormEditText phone, FormEditText phone2, FormEditText email, FormEditText solde, AlertDialog alertDialog) {
//        FormEditText[] allFields = {username, phone,phone2,email};
        FormEditText[] allFields = {username, phone, phone};
        boolean allvalid = true;

        for (FormEditText field : allFields) {
            allvalid = field.testValidity() && allvalid;
        }

//                if (isEntryValid(username, phone,phone2))
        if (allvalid) {
            if (phone.getText().toString().equals(phone.getText().toString())) {
                if (!thisClient.getNumero().equals(phone.getText().toString())) {
                    RealmQuery<ClientRepository> query = realm.where(ClientRepository.class);
                    ClientRepository tmp = query.equalTo("numero", phone.getText().toString()).findFirst();

                    if (tmp == null) {
                        realm.beginTransaction();
                        thisClient.setMail(email.getText().toString());
                        thisClient.setNom(username.getText().toString());
                        thisClient.setNumero(phone.getText().toString());
                        thisClient.setMail(email.getText().toString());

                        realm.copyToRealmOrUpdate(thisClient);
                        realm.commitTransaction();

                        Snackbar.make(phone, getString(R.string.edited_client_ok), Snackbar.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        // check the validity of the entry
                        // register the user in the database
                        // send confirmation message to the operator
                    } else {
                        phone2.setError(getString(R.string.numalreadyused), getResources().getDrawable(R.drawable.add_user));
                        Snackbar.make(phone, getString(R.string.numalreadyused), Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    realm.beginTransaction();
                    thisClient.setMail(email.getText().toString());
                    thisClient.setNom(username.getText().toString());
                    thisClient.setNumero(phone.getText().toString());
                    thisClient.setMail(email.getText().toString());

                    realm.copyToRealmOrUpdate(thisClient);
                    realm.commitTransaction();

                    Snackbar.make(phone, getString(R.string.edited_client_ok), Snackbar.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }

            } else {
//                        phone.setError("les numeros ne correspondent pas",getResources().getDrawable(R.drawable.add_user));
                phone2.setError(getString(R.string.numnomatch), getResources().getDrawable(R.drawable.add_user));
                Snackbar.make(phone, getString(R.string.numnomatch), Snackbar.LENGTH_LONG).show();

            }
        }

    }

    public void ProcessEditclientPrev(FormEditText username, FormEditText phone, FormEditText phone2, FormEditText email, FormEditText solde, AlertDialog alertDialog) {
        FormEditText[] allFields = {username, phone, phone2, email};
        boolean allvalid = true;

        for (FormEditText field : allFields) {
            allvalid = field.testValidity() && allvalid;
        }

//                if (isEntryValid(username, phone,phone2))
        if (allvalid) {
            if (phone.getText().toString().equals(phone2.getText().toString())) {

                RealmQuery<ClientRepository> query = realm.where(ClientRepository.class);
                ClientRepository tmp = query.equalTo("numero", phone.getText().toString()).findFirst();


                if (tmp == null) {
                    Log.d("HuRu!!!!!!!!!", "HURU!!!!!!!!!");
                    String clientName = username.getText().toString();
                    String clientPhone = phone.getText().toString();
                    String clientSolde = solde.getText().toString();
                    String clientemail = email.getText().toString();


                    realm.beginTransaction();
                    // Add the client
                    ClientRepository client = realm.createObject(ClientRepository.class);
                    client.setNom(clientName);
                    client.setNumero(clientPhone);
                    client.setMail(clientemail);

                    // Add the deposit to the client account
                    if (!TextUtils.isEmpty(clientSolde)) {
                        client.setSolde(Integer.valueOf(clientSolde));

                        OperationClientRepository depot = realm.createObject(OperationClientRepository.class);
                        depot.setClient(client);
                        depot.setMontant(Integer.valueOf(clientSolde));
                        depot.setDate(Calendar.getInstance().getTime());
                        //TODO should include the total invoices here and add it to this operation
                        client.getOperationClients().add(depot);
                    }

                    realm.commitTransaction();
                    //Snackbar.make(phone, getString(R.string.client_balance_ok), Snackbar.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    // check the validity of the entry
                    // register the user in the database
                    // send confirmation message to the operator
                }
                //in case tmp is not null it means that our edittext are prefeeded
                //and the user can decide to modify them so we need to be aware of it before going...
                else {
                    phone2.setError(getString(R.string.numalreadyused), getResources().getDrawable(R.drawable.add_user));
                    Snackbar.make(phone, getString(R.string.numalreadyused), Snackbar.LENGTH_SHORT).show();
                }


            } else {
//                        phone.setError("les numeros ne correspondent pas",getResources().getDrawable(R.drawable.add_user));
                phone2.setError(getString(R.string.numnomatch), getResources().getDrawable(R.drawable.add_user));
                Snackbar.make(phone, getString(R.string.numnomatch), Snackbar.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.removeChangeListener(reamlListener);
        realm.close();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_deposit, parent, false);
            }

            TextView depotType = (TextView) convertView.findViewById(R.id.deposit);
            //TextView depotDate = (TextView) convertView.findViewById(R.id.deposit_date);
            TextView depotMontant = (TextView) convertView.findViewById(R.id.deposit_amount);
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            final int lid = mOperationClients.get(position).getInvoiceRepository().getId();
            final InvoiceRepository invoiceRepository = mOperationClients.get(position).getInvoiceRepository();


            boolean contain = false;
            int valeurATrouver = lid;
            //aprés un retour dans la page du client on vérifie s'il y'a des checkbox cochés et si oui on les coches à nouveau
            for (int i = 0; i < lesidfactures.size(); i++) {
                if (lesidfactures.get(i) == valeurATrouver) {
                    contain = true;
                }
            }

            if (contain) {
                checkBox.setChecked(true);
                if (checkBox.isChecked()) {
                    lesprisescon = new ArrayList<>();
                    Toast.makeText(getApplicationContext(), "" + lid, Toast.LENGTH_SHORT).show();
                    //lesidfactures.add(lid);
                    int taille = lesidfactures.size();
                    RealmList<ItemRepository> listitem = invoiceRepository.getItems();
                    RealmResults<ItemRepository> resultsitem = listitem.where().findAll();
                    for (ItemRepository it : resultsitem) {
                        Item item = new Item();
                        item.setId(it.getId());
                        lesprisescon.add(it.getId());
                    }
                    Toast.makeText(getApplicationContext(), "" + lesprisescon + "\n taille " + taille, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "" + lid, Toast.LENGTH_SHORT).show();
                        //on ajoute lid de la facture à la liste des factures choisies
                        lesidfactures.add(lid);
                        int taille = lesidfactures.size();
                        //on recupere tous les items de la facture choisi et on les ajoute dans la liste des items à partager
                        RealmList<ItemRepository> listitem = invoiceRepository.getItems();
                        RealmResults<ItemRepository> resultsitem = listitem.where().findAll();
                        for (ItemRepository it : resultsitem) {
                            Item item = new Item();
                            item.setId(it.getId());
                            lesprisescon.add(it.getId());
                        }
                        Toast.makeText(getApplicationContext(), "" + lesprisescon + "\n taille " + taille, Toast.LENGTH_SHORT).show();
                    } else if (!checkBox.isChecked()) {
                        //si on deselectionne un ou plusieurs checkbox, on le(s) supprime dans les arraylist idfactures et iditems
                        int taille = lesidfactures.size() - 1;
                        Toast.makeText(getApplicationContext(), "pas selectionné  " + lid + "\n taille " + taille, Toast.LENGTH_SHORT).show();
                        if (taille > 0) {
                            lesidfactures.remove(lid);
                            Toast.makeText(getApplicationContext(), " taille aprés" + taille, Toast.LENGTH_SHORT).show();
                            RealmList<ItemRepository> listitem = invoiceRepository.getItems();
                            RealmResults<ItemRepository> resultsitem = listitem.where().findAll();
                            ArrayList<Integer> lesitesm = new ArrayList<Integer>();
                            for (ItemRepository it : resultsitem) {
                                lesitesm.add(it.getId());

                                for (int i = 0; i < lesprisescon.size(); i++) {
                                    if (lesprisescon.get(i) == it.getId()) {
                                        lesprisescon.remove(lesitesm);
                                    }
                                }
                            }
                        }
                        //si la taille des idfactures == 0 on supprime tous les idfactures et les iditems des deux arraylistes
                        else if (taille == 0) {
                            lesidfactures.removeAll(lesidfactures);
                            lesprisescon.removeAll(lesprisescon);
                        }
                        //
                    }

                }


            });
            /**/

            int montant = mOperationClients.get(position).getMontant();

            String lemontant = formaterSolde(montant);
            depotMontant.setText(getString(R.string.montant_egale) + String.valueOf(lemontant));
            depotType.setText(getString(R.string.facture_of)
                    + Config.getFormattedDate(mOperationClients.get(position).getDate()));

            /* Ajout pisco *


            montantsolde=String.valueOf(montant);
            datesolde=Config.getFormattedDate(mOperationClients.get(position).getDate());
            Toast.makeText(getApplicationContext(),""+montant,Toast.LENGTH_LONG).show();
            /*fin ajout*/

            // if(mOperationClients.get(position).getInvoiceRepository() == null)
            /*
                if((mOperationClients.get(position).getInvoiceRepository().getItems().size() == 1)
                        &&
                        mOperationClients.get(position).getInvoiceRepository().getItems().get(0).getName().contains("Depot du"))*/
            if (mOperationClients.get(position).getInvoiceRepository() != null) {
                if (mOperationClients.get(position).isInvoice() == false) {
                    convertView.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    depotType.setTextColor(Color.WHITE);
                    depotMontant.setTextColor(Color.WHITE);
                    depotType.setText(" " + mOperationClients.get(position).getInvoiceRepository().getItems().get(0).getName());
                }
            }
//            else

//            depotDate.setText(Config.getFormattedDate(mOperationClients.get(position).getDate()));
            return convertView;
        }
    }
}
