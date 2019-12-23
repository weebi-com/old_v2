package com.weebinatidi.ui.weebi2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.weebinatidi.R;
import com.weebinatidi.model.LaReffb;
import com.weebinatidi.ui.CalculatorGB;

import java.util.ArrayList;
import java.util.List;

import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;

public class Referencesfb extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    final DatabaseReference databaseReference1 = databaseReference.child(user.getUid()).child("Weebi2").child("References");


    //List<String> references;
    List<LaReffb> references=new ArrayList<>();
    ListView listView;
    private AdapterReference adapterReference;
    AlertDialog.Builder RegisterRefbuilder;
    AlertDialog RegisterRefdialog;
    AlertDialog.Builder MenuRefbuilder;
    AlertDialog MenuRefDialog;
    private Button addref;
    private Button menu, retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_references);
        retour = (Button) findViewById(R.id.retourref);
        addref = (Button) findViewById(R.id.addref);
        menu = (Button) findViewById(R.id.menu);

        references = new ArrayList<>();
        adapterReference=new AdapterReference(this,references);
        listView = (ListView) findViewById(R.id.listeref);

        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_LONG).show();
                String lareff=dataSnapshot.child("laref").getValue(String.class);
                String leprix=dataSnapshot.child("larefprice").getValue(String.class);


                LaReffb laReffb=new LaReffb();
                laReffb.setLaref(lareff);
                laReffb.setLarefprice(leprix);
                references.add(laReffb);

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




        listView.setAdapter(adapterReference);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String laref = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(),""+laref,Toast.LENGTH_LONG).show();
                RealmQuery<ReferenceRepository> query = realm.where(ReferenceRepository.class);
                ReferenceRepository tmp = query.equalTo("nomref", laref).findFirst();
                //Toast.makeText(getApplicationContext(),""+tmp.getNomref(),Toast.LENGTH_LONG).show();
                updateRef(tmp.getNomref());

            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class AdapterReference extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;

        private List<LaReffb> references;
        private Context context;

        public AdapterReference(Activity activity, List<LaReffb> references){
            this.activity=activity;
            this.references=references;
        }
        @Override
        public int getCount() {
            return references.size();
        }

        @Override
        public Object getItem(int position) {
            return references.get(position);
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
                convertView = inflater.inflate(R.layout.client_item_view_reference, null);


            TextView nomref=(TextView)convertView.findViewById(R.id.refname);
            TextView prixref=(TextView)convertView.findViewById(R.id.prixref);
            //TextView solde=(TextView)convertView.findViewById(R.id.client_soldee);

            LaReffb laReffb=references.get(position);
            nomref.setText(laReffb.getLaref());
            prixref.setText(laReffb.getLarefprice());
            // solde.setText(clientRepository.getSolde());
            return convertView;
        }
    }


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

    public  void ajoutreffb(final String nomref, final String prixref, final AlertDialog dialog){


        LaReffb laReffb=new LaReffb();
        laReffb.setLaref(nomref);
        laReffb.setLarefprice(prixref);
        databaseReference1.push().setValue(laReffb);
        dialog.dismiss();

    }

    private void onAddRef() {

        RegisterRefbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.v2dialog_add_new_reference, null, false);

        final FormEditText nameref = (FormEditText) customView.findViewById(R.id.refname);
        final FormEditText nameprice = (FormEditText) customView.findViewById(R.id.refprice);
        final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewref);

        RegisterRefbuilder.setCancelable(true);
        RegisterRefbuilder.setView(customView);
        RegisterRefbuilder.setTitle((getString(R.string.add_reference)));


        RegisterRefdialog = RegisterRefbuilder.create();


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RegisterRef(nameref, RegisterRefdialog);
                String nomref=nameref.getText().toString();
                String prixref=nameprice.getText().toString();

                if(prixref.equals("")|| nomref.equals("")){
                    Toast.makeText(getApplicationContext(),"veuillez remplir tous les champs merci",Toast.LENGTH_SHORT).show();
                }
                else {
                    ajoutreffb(nomref,prixref,RegisterRefdialog);
                    //RegisterClientDialog.dismiss();
                }
            }
        });

        RegisterRefdialog.show();
    }



    /*private void updateRef(final String lareffe) {

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
                Intent intent = new Intent(getApplicationContext(), Referencesfb.class);
                startActivity(intent);
                finish();
            }
        });

        RegisterRefdialog.show();
    }*/


}
