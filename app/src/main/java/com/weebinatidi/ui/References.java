package com.weebinatidi.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.andreabaccega.widget.FormEditText;
import com.weebinatidi.R;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.ReferenceRepository;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;

public class References extends Activity {

    List<String> references;
    ArrayList<String> listetest=new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    AlertDialog.Builder RegisterRefbuilder;
    AlertDialog RegisterRefdialog;
    AlertDialog.Builder MenuRefbuilder;
    AlertDialog MenuRefDialog;
    private Realm realm;
    private Button addref;
    private Button menu, retour;

    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_references);
        retour = (Button) findViewById(R.id.retourref);
        addref = (Button) findViewById(R.id.addref);
        menu = (Button) findViewById(R.id.menu);

        dbHelper=new DbHelper(this);

        references = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(References.this, android.R.layout.simple_list_item_1, references);
        listView = (ListView) findViewById(R.id.listeref);

        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
        if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(getApplicationContext(), "LANDSCAPE", Toast.LENGTH_SHORT).show();
        retour.setVisibility(View.VISIBLE);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CalculatorGB.class));
            }
        });

        addref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddRef();
            }
        });

        // menu.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menus();
            }
        });


        realm = Realm.getDefaultInstance();
        RealmResults<ReferenceRepository> results = realm.where(ReferenceRepository.class).findAll();

        for (ReferenceRepository r : results) {

            //Toast.makeText(getApplicationContext(), ""+r.getNomref(),Toast.LENGTH_SHORT).show();
            references.add(r.getNomref());
           // dbHelper.insertReference(r.getNomref(),"");

        }


        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String laref = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(),""+laref,Toast.LENGTH_LONG).show();
                RealmQuery<ReferenceRepository> query = realm.where(ReferenceRepository.class);
                ReferenceRepository tmp = query.equalTo("nomref", laref).findFirst();
                //Toast.makeText(getApplicationContext(),""+tmp.getNomref(),Toast.LENGTH_LONG).show();
                updateRef(tmp.getNomref());

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }*/

    /*public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }*/
    public void menus() {
        MenuRefbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_menu_reference, null, false);

        final Button addref = (Button) customView.findViewById(R.id.addref);


        addref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddRef();
                MenuRefDialog.dismiss();
            }
        });


        MenuRefbuilder.setCancelable(true);
        MenuRefbuilder.setView(customView);
        // RegisterClientbuilder.setTitle(getString(R.string.ajouter_un_client));

        MenuRefDialog = MenuRefbuilder.create();
        MenuRefDialog.show();


    }

    private void onAddRef() {

        RegisterRefbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_new_reference, null, false);

        final FormEditText nameref = (FormEditText) customView.findViewById(R.id.refname);

        final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewref);

        RegisterRefbuilder.setCancelable(true);
        RegisterRefbuilder.setView(customView);
        RegisterRefbuilder.setTitle((getString(R.string.add_reference)));


        RegisterRefdialog = RegisterRefbuilder.create();


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterRef(nameref, RegisterRefdialog);
            }
        });

        RegisterRefdialog.show();
    }

    public void RegisterRef(FormEditText nameref, AlertDialog alertDialog) {

        String nomreference = nameref.getText().toString();

        if (nomreference.equals("")) {

            Snackbar.make(nameref, getString(R.string.error_empty_field_msg), Snackbar.LENGTH_LONG).show();
        } else {
            RealmQuery<ReferenceRepository> query = realm.where(ReferenceRepository.class);
            ReferenceRepository tmp = query.equalTo("nomref", nameref.getText().toString()).findFirst();

            if (tmp == null) {
                //Toast.makeText(getApplicationContext(), "bon",Toast.LENGTH_LONG).show();
                realm.beginTransaction();
                // Add the reference
                ReferenceRepository reference = realm.createObject(ReferenceRepository.class);
                reference.setNomref(nomreference);

                alertDialog.dismiss();

                realm.commitTransaction();
                //ajout et rechargement de l'activité
                Intent intent = new Intent(getApplicationContext(), References.class);
                startActivity(intent);
                finish();
            } else {
                Snackbar.make(nameref, getString(R.string.reference_existe), Snackbar.LENGTH_LONG).show();
            }

        }

    }

    private void updateRef(final String lareffe) {

        RegisterRefbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_update_refprice, null, false);

        final FormEditText nameref = (FormEditText) customView.findViewById(R.id.refname);

        final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewref);

        RealmQuery<ReferenceRepository> query = realm.where(ReferenceRepository.class);
        final ReferenceRepository tmp1 = query.equalTo("nomref", lareffe).findFirst();

        RegisterRefbuilder.setCancelable(true);
        RegisterRefbuilder.setView(customView);
        RegisterRefbuilder.setTitle((getString(R.string.modifier_reference)));


        RegisterRefdialog = RegisterRefbuilder.create();

        nameref.setText(lareffe);


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RegisterUpdateRef(nameref, RegisterRefdialog);


                //Toast.makeText(getApplicationContext(), "bon",Toast.LENGTH_LONG).show();
                realm.beginTransaction();
                // update the reference

                tmp1.setNomref(nameref.getText().toString());
                realm.copyToRealmOrUpdate(tmp1);
                //alertDialog.dismiss();

                realm.commitTransaction();
                RegisterRefdialog.dismiss();//terminer l'update et relancer l'activité
                Intent intent = new Intent(getApplicationContext(), References.class);
                startActivity(intent);
                finish();
            }
        });

        RegisterRefdialog.show();
    }

}
