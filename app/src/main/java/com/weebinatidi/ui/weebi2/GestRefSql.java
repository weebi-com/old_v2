package com.weebinatidi.ui.weebi2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.Invoice;
import com.weebinatidi.model.LaReffb;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.Calculator;
import com.weebinatidi.ui.ClientPageActivityNew2SQL;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

import static com.weebinatidi.ui.weebi2.NewInterface.isSmart;
import static com.weebinatidi.ui.weebi2.NewInterface.isSmartsmall;
import static com.weebinatidi.ui.weebi2.NewInterface.isTablet;
import static com.weebinatidi.ui.weebi2.NewInterface.isTablet7;

public class GestRefSql extends BaseActivity {


    public static final String EXTRA_REF_ID = "idref";
    String idref,prix,qte,nomref;
    double laqte;
    DbHelper dbHelper;
    private Button prixupdate, qteupdate, qtedim, supprefe, nameupdate, fiu;
    private TextView stocke;
    private StockSortiAdapter sortiAdapter;
    private StockSortiEntreeAdapter entreeAdapter;
    List<Invoice> lesfactures=new ArrayList<>();
    List<String> lesidfact=new ArrayList<>();
    AlertDialog.Builder RegisterRefbuilder;
    AlertDialog RegisterRefdialog;
    AlertDialog.Builder RegisterSupbuilder;
    AlertDialog RegisterSupDialog;
    private ListView listesorti, listeentre;

    ArrayList<LaReffb> lesentrees=new ArrayList<>();
    ArrayList<LaReffb> lessorties=new ArrayList<>();

    String texttosend="";

    private String couleur="";
    final String qtePattern = "[0-9.0-9]";
    final String qtePattern1 = "[0-9]";
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String IDLOT = null;
    private static final String PREF_IDLOT = "PREF_IDLOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
        /*if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }*/

        dbHelper=new DbHelper(this);


        //dbHelper.suppReftablesuiv();
        prixupdate=(Button)findViewById(R.id.price);
        fiu=(Button)findViewById(R.id.fiu);
        qteupdate=(Button)findViewById(R.id.addqte);
        qtedim=(Button)findViewById(R.id.dimqte);
        nameupdate=(Button)findViewById(R.id.updatename);
        supprefe=(Button)findViewById(R.id.suppref);
        stocke=(TextView) findViewById(R.id.stock);

        idref = getIntent().getStringExtra(EXTRA_REF_ID);

        listesorti=(ListView) findViewById(R.id.stocksortie);
        listeentre=(ListView) findViewById(R.id.suivistockent);


        Cursor cur=dbHelper.getREF(Integer.valueOf(idref));

        if(cur.moveToFirst()==true){

            getSupportActionBar().setTitle(cur.getString(1) + "/ Prix : " + cur.getString(2)+ "/ Quantité : " + cur.getString(3));
           // getSupportActionBar().setIcon();
            prix=cur.getString(2);
            qte=cur.getString(3);
            nomref=cur.getString(1);
            couleur=cur.getString(7);


            //Toast.makeText(getApplicationContext(),""+cur.getString(3),Toast.LENGTH_SHORT).show();
            if (qte.equals("")){
                //Toast.makeText(getApplicationContext(),"quantité vide",Toast.LENGTH_SHORT).show();
                stocke.setText("0");
                laqte=0;
            }
            if (!qte.equals("")){
                //Toast.makeText(getApplicationContext(),""+qte,Toast.LENGTH_SHORT).show();
                stocke.setText(Config.formaterQte(qte));
                laqte=Double.valueOf(qte);
            }
        }
        cur.close();

        Cursor cursor1=dbHelper.getREFSUIV(Integer.valueOf(idref));
        while (cursor1.moveToNext()){
            /*Toast.makeText(getApplicationContext(),""+cursor1.getString(1)+"\n"+
                    cursor1.getString(2)+"\n"+cursor1.getString(3)+"\n"
                    +cursor1.getString(4)+"\n"
                    +cursor1.getString(5),Toast.LENGTH_SHORT).show();*/
        }
        cursor1.close();


        lessorties=dbHelper.getAllSortiforOneRef(idref);
        sortiAdapter=new StockSortiAdapter(lessorties);
        listesorti.setAdapter(sortiAdapter);

        lesentrees=dbHelper.getAllENTERforOneRef(idref);
        entreeAdapter=new StockSortiEntreeAdapter(lesentrees);
        listeentre.setAdapter(entreeAdapter);

        Cursor cursor=dbHelper.getrefnametabitem(nomref);
        while (cursor.moveToNext()){
            /*Toast.makeText(getApplicationContext(),""+cursor.getString(0)+"\n"+cursor.getString(1)
                    +"\n"+cursor.getString(2)+"\n"+cursor.getString(3),Toast.LENGTH_SHORT).show();*/
            lesidfact.add(cursor.getString(5));
        }
        cursor.close();



        nameupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ModifierRefSQL.class);
                //intent.putExtra("laref",nomref);
                intent.putExtra("lid",idref);
                startActivity(intent);
                finish();
            }
        });
        prixupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    RegisterRefbuilder = new AlertDialog.Builder(GestRefSql.this);
                    View customView = getLayoutInflater().inflate(R.layout.dialog_update_refprice, null, false);


                    final FormEditText priceref = (FormEditText) customView.findViewById(R.id.refprice);
                    final FormEditText codever = (FormEditText) customView.findViewById(R.id.verifcodee);
                    final TextView prixbd =  customView.findViewById(R.id.prixactu);
                    final LinearLayout numero=customView.findViewById(R.id.clavn);
                    final LinearLayout lettre=customView.findViewById(R.id.clavl);

                    final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewref);
                     prixbd.setText("prix actuel : "+prix);

               final Button espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;
                codever.setTransformationMethod(PasswordTransformationMethod.getInstance());

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

                     priceref.setInputType(0);
                     codever.setInputType(0);

                     codever.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                         @Override
                         public void onFocusChange(View v, boolean hasFocus) {
                             numero.setVisibility(View.VISIBLE);

                             zero.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+0);
                                     }
                                     else {
                                         codever.append(""+0);
                                     }
                                 }
                             });

                             un.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+1);
                                     }
                                     else {
                                         codever.append(""+1);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                                 }
                             });

                             deux.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+2);
                                     }
                                     else {
                                         codever.append(""+2);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             trois.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+3);
                                     }
                                     else {
                                         codever.append(""+3);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             quatre.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+4);
                                     }
                                     else {
                                         codever.append(""+4);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             cinq.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+5);
                                     }
                                     else {
                                         codever.append(""+5);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             six.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+6);
                                     }
                                     else {
                                         codever.append(""+6);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             sept.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+7);
                                     }
                                     else {
                                         codever.append(""+7);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             huit.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+8);
                                     }
                                     else {
                                         codever.append(""+8);
                                     }


                                 }
                             });

                             neuf.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=codever.getText().toString();

                                     if(chaine.contains("")){
                                         codever.append(""+9);
                                     }
                                     else {
                                         codever.append(""+9);
                                     }

                                 }
                             });



                             cleardn.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     String chaine=codever.getText().toString();
                                     if (chaine.equals("")){

                                     }else {
                                         chaine = chaine.substring(0, chaine.length()-1);
                                         codever.setText(""+chaine);
                                     }

                                 }
                             });
                         }
                     });

                     priceref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                         @Override
                         public void onFocusChange(View v, boolean hasFocus) {
                             numero.setVisibility(View.VISIBLE);

                             zero.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+0);
                                     }
                                     else {
                                         priceref.append(""+0);
                                     }
                                 }
                             });

                             un.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+1);
                                     }
                                     else {
                                         priceref.append(""+1);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                                 }
                             });

                             deux.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+2);
                                     }
                                     else {
                                         priceref.append(""+2);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             trois.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+3);
                                     }
                                     else {
                                         priceref.append(""+3);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             quatre.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+4);
                                     }
                                     else {
                                         priceref.append(""+4);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             cinq.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+5);
                                     }
                                     else {
                                         priceref.append(""+5);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             six.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+6);
                                     }
                                     else {
                                         priceref.append(""+6);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             sept.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+7);
                                     }
                                     else {
                                         priceref.append(""+7);
                                     }

                                     //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                 }
                             });

                             huit.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+8);
                                     }
                                     else {
                                         priceref.append(""+8);
                                     }


                                 }
                             });

                             neuf.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                     String chaine=priceref.getText().toString();

                                     if(chaine.contains("")){
                                         priceref.append(""+9);
                                     }
                                     else {
                                         priceref.append(""+9);
                                     }

                                 }
                             });



                             cleardn.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     String chaine=priceref.getText().toString();
                                     if (chaine.equals("")){

                                     }else {
                                         chaine = chaine.substring(0, chaine.length()-1);
                                         priceref.setText(""+chaine);
                                     }

                                 }
                             });
                         }
                     });



                    RegisterRefbuilder.setCancelable(true);
                    RegisterRefbuilder.setView(customView);
                    RegisterRefbuilder.setTitle((getResources().getString(R.string.change_price)));
                    RegisterRefdialog = RegisterRefbuilder.create();
                    okbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String lecode=codever.getText().toString();
                            String leboutiqcode="";
                            if(lecode.equals("")){
                                Toast.makeText(getApplicationContext(),"Merci de renseigner le code",Toast.LENGTH_SHORT).show();
                            }
                            if(!lecode.equals("")){
                                Cursor bouti=dbHelper.getBoutique("1");
                                if(bouti.moveToFirst()){
                                    leboutiqcode=bouti.getString(4);
                                    if(!leboutiqcode.equals(lecode)){
                                        Toast.makeText(getApplicationContext(),"Code incorrect ",Toast.LENGTH_SHORT).show();
                                    }
                                    //Toast.makeText(getApplicationContext(),"le code "+bouti.getString(3),Toast.LENGTH_SHORT).show();
                                    if (leboutiqcode.equals(lecode)){
                                        codever.setVisibility(View.GONE);
                                        prixbd.setVisibility(View.VISIBLE);
                                        priceref.setVisibility(View.VISIBLE);
                                        updaterefprix(priceref,RegisterRefdialog);
                                    }
                                }
                                bouti.close();
                            }


                        }
                    });
                    RegisterRefdialog.show();
            }
        });

        fiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterRefbuilder = new AlertDialog.Builder(GestRefSql.this);
                View customView = getLayoutInflater().inflate(R.layout.dialog_reffiu, null, false);

                final FormEditText contexte = (FormEditText) customView.findViewById(R.id.contexqte);
                final FormEditText qteref = (FormEditText) customView.findViewById(R.id.refqte);
                final FormEditText qterefv = (FormEditText) customView.findViewById(R.id.refqtev);
                final FormEditText codever = (FormEditText) customView.findViewById(R.id.verifcodee);
                final TextView infos=customView.findViewById(R.id.infos);
                final LinearLayout numero=customView.findViewById(R.id.clavn);
                final LinearLayout lettre=customView.findViewById(R.id.clavl);
                codever.setTransformationMethod(PasswordTransformationMethod.getInstance());

                final Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,vi,w,x,y,z,espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero, virgule;

                a=(Button)customView.findViewById(R.id.a); b=(Button)customView.findViewById(R.id.b);c=(Button)customView.findViewById(R.id.c);
                d=(Button)customView.findViewById(R.id.d); e=(Button)customView.findViewById(R.id.e); f=(Button)customView.findViewById(R.id.f);
                g=(Button)customView.findViewById(R.id.g); h=(Button)customView.findViewById(R.id.h); i=(Button)customView.findViewById(R.id.i);
                j=(Button)customView.findViewById(R.id.j); k=(Button)customView.findViewById(R.id.k); l=(Button)customView.findViewById(R.id.l);
                m=(Button)customView.findViewById(R.id.m); n=(Button)customView.findViewById(R.id.n); o=(Button)customView.findViewById(R.id.o);
                p=(Button)customView.findViewById(R.id.p); r=(Button)customView.findViewById(R.id.r); s=(Button)customView.findViewById(R.id.s);
                t=(Button)customView.findViewById(R.id.t); u=(Button)customView.findViewById(R.id.u); vi=(Button)customView.findViewById(R.id.v);
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
                virgule=(Button)customView.findViewById(R.id.btnvirgule);

                final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewref);
                qteref.setInputType(0);
                qterefv.setInputType(0);
                codever.setInputType(0);
                contexte.setInputType(0);

                /*contexte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        numero.setVisibility(View.GONE);
                        lettre.setVisibility(View.VISIBLE);

                        cleard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=contexte.getText().toString();
                                if (chaine.equals("")){

                                }else {
                                    chaine = chaine.substring(0, chaine.length()-1);
                                    contexte.setText(""+chaine);
                                }

                            }
                        });

                        espaced.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append(" ");
                            }
                        });

                        a.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("a");
                            }
                        });

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("b");
                            }
                        });

                        c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("c");
                            }
                        });

                        d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("d");
                            }
                        });

                        e.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("e");
                            }
                        });

                        f.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("f");
                            }
                        });

                        g.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("g");
                            }
                        });

                        h.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("h");
                            }
                        });

                        i.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("i");
                            }
                        });
                        j.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("j");
                            }
                        });
                        k.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("k");
                            }
                        });
                        l.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("l");
                            }
                        });
                        m.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("m");
                            }
                        });
                        n.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("n");
                            }
                        });
                        o.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("o");
                            }
                        });
                        p.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("p");
                            }
                        });
                        q.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("q");
                            }
                        });
                        r.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("r");
                            }
                        });
                        s.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("s");
                            }
                        });
                        t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("t");
                            }
                        });
                        u.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("u");
                            }
                        });
                        vi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("v");
                            }
                        });
                        w.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("w");
                            }
                        });
                        x.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("x");
                            }
                        });
                        y.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("y");
                            }
                        });
                        z.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("z");
                            }
                        });
                    }
                });*/

                codever.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        numero.setVisibility(View.VISIBLE);
                        lettre.setVisibility(View.GONE);

                        zero.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+0);
                                }
                                else {
                                    codever.append(""+0);
                                }
                            }
                        });

                        un.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+1);
                                }
                                else {
                                    codever.append(""+1);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                        deux.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+2);
                                }
                                else {
                                    codever.append(""+2);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        trois.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+3);
                                }
                                else {
                                    codever.append(""+3);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        quatre.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+4);
                                }
                                else {
                                    codever.append(""+4);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        cinq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+5);
                                }
                                else {
                                    codever.append(""+5);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        six.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+6);
                                }
                                else {
                                    codever.append(""+6);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        sept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+7);
                                }
                                else {
                                    codever.append(""+7);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        huit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+8);
                                }
                                else {
                                    codever.append(""+8);
                                }


                            }
                        });

                        neuf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+9);
                                }
                                else {
                                    codever.append(""+9);
                                }

                            }
                        });



                        cleardn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();
                                if (chaine.equals("")){

                                }else {
                                    chaine = chaine.substring(0, chaine.length()-1);
                                    codever.setText(""+chaine);
                                }

                            }
                        });

                    }
                });
                qteref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        {
                            numero.setVisibility(View.VISIBLE);
                            lettre.setVisibility(View.GONE);

                            zero.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+0);
                                    }
                                    else {
                                        qteref.append(""+0);
                                    }
                                }
                            });

                            un.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+1);
                                    }
                                    else {
                                        qteref.append(""+1);
                                    }
                                }
                            });

                            deux.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+2);
                                    }
                                    else {
                                        qteref.append(""+2);
                                    }

                                }
                            });

                            trois.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+3);
                                    }
                                    else {
                                        qteref.append(""+3);
                                    }

                                }
                            });

                            quatre.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+4);
                                    }
                                    else {
                                        qteref.append(""+4);
                                    }

                                }
                            });

                            cinq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+5);
                                    }
                                    else {
                                        qteref.append(""+5);
                                    }

                                }
                            });

                            six.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+6);
                                    }
                                    else {
                                        qteref.append(""+6);
                                    }

                                }
                            });

                            sept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+7);
                                    }
                                    else {
                                        qteref.append(""+7);
                                    }

                                }
                            });

                            huit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+8);
                                    }
                                    else {
                                        qteref.append(""+8);
                                    }


                                }
                            });

                            neuf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+9);
                                    }
                                    else {
                                        qteref.append(""+9);
                                    }

                                }
                            });

                            virgule.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        //qterefv.append(".");
                                        //Toast.makeText(getApplicationContext(),"Veuillez écrire une chiffre d'abord", Toast.LENGTH_SHORT).show();
                                    }
                                    if(chaine.length()!=0) {
                                        //
                                        if(chaine.contains(".")){

                                        }
                                        else {
                                            qteref.append(".");
                                        }
                                    }

                                }
                            });



                            cleardn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();
                                    if (chaine.equals("")){

                                    }else {
                                        chaine = chaine.substring(0, chaine.length()-1);
                                        qteref.setText(""+chaine);
                                    }

                                }
                            });

                        }
                    }
                });

                qterefv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        {
                            numero.setVisibility(View.VISIBLE);
                            lettre.setVisibility(View.GONE);

                            zero.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+0);
                                    }
                                    else {
                                        qterefv.append(""+0);
                                    }
                                }
                            });

                            un.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+1);
                                    }
                                    else {
                                        qterefv.append(""+1);
                                    }
                                }
                            });

                            deux.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+2);
                                    }
                                    else {
                                        qterefv.append(""+2);
                                    }

                                }
                            });

                            trois.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+3);
                                    }
                                    else {
                                        qterefv.append(""+3);
                                    }

                                }
                            });

                            quatre.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+4);
                                    }
                                    else {
                                        qterefv.append(""+4);
                                    }

                                }
                            });

                            cinq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+5);
                                    }
                                    else {
                                        qterefv.append(""+5);
                                    }

                                }
                            });

                            six.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+6);
                                    }
                                    else {
                                        qterefv.append(""+6);
                                    }

                                }
                            });

                            sept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+7);
                                    }
                                    else {
                                        qterefv.append(""+7);
                                    }

                                }
                            });

                            huit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+8);
                                    }
                                    else {
                                        qterefv.append(""+8);
                                    }


                                }
                            });

                            neuf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+9);
                                    }
                                    else {
                                        qterefv.append(""+9);
                                    }

                                }
                            });

                            virgule.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();
                                    if(chaine.contains("")){
                                        //qterefv.append(".");
                                        //Toast.makeText(getApplicationContext(),"Veuillez écrire une chiffre d'abord", Toast.LENGTH_SHORT).show();
                                    }
                                    if(chaine.length()!=0) {
                                        //
                                        if(chaine.contains(".")){

                                        }
                                        else {
                                            qterefv.append(".");
                                        }
                                    }

                                }
                            });



                            cleardn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();
                                    if (chaine.equals("")){

                                    }else {
                                        chaine = chaine.substring(0, chaine.length()-1);
                                        qterefv.setText(""+chaine);
                                    }

                                }
                            });

                        }
                    }
                });

                //qteref.setText(qte);
                String ladate= Config.getFormattedDate(Calendar.getInstance().getTime());
                //infos.setText(nomref+"\n"+prix+"\n"+ladate+"\n");
                RegisterRefbuilder.setCancelable(true);
                RegisterRefbuilder.setView(customView);
                RegisterRefbuilder.setTitle(("Créer un lot"));
                RegisterRefdialog = RegisterRefbuilder.create();
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String lecode=codever.getText().toString();
                        String leboutiqcode="";
                        if(lecode.equals("")){
                            Toast.makeText(getApplicationContext(),"Merci de renseigner le code",Toast.LENGTH_SHORT).show();
                        }
                        if(!lecode.equals("")){
                            Cursor bouti=dbHelper.getBoutique("1");
                            if(bouti.moveToFirst()){
                                leboutiqcode=bouti.getString(4);
                                if(!leboutiqcode.equals(lecode)){
                                    Toast.makeText(getApplicationContext(),"Code incorrect ",Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(getApplicationContext(),"le code "+bouti.getString(3),Toast.LENGTH_SHORT).show();
                                if (leboutiqcode.equals(lecode)){
                                    codever.setVisibility(View.GONE);
                                    qteref.setVisibility(View.VISIBLE);
                                    qterefv.setVisibility(View.VISIBLE);
                                    //contexte.setVisibility(View.VISIBLE);
                                    //contexte.requestFocus();
                                    qteref.requestFocus();
                                    updaterefqteFIU(qteref,qterefv,laqte,contexte,prix);
                                }
                            }
                            bouti.close();
                        }
                    }
                });
                RegisterRefdialog.show();
            }
        });

        qteupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterRefbuilder = new AlertDialog.Builder(GestRefSql.this);
                View customView = getLayoutInflater().inflate(R.layout.dialog_update_refqte, null, false);

                final FormEditText contexte = (FormEditText) customView.findViewById(R.id.contexqte);
                final FormEditText qteref = (FormEditText) customView.findViewById(R.id.refqte);
                final FormEditText qterefv = (FormEditText) customView.findViewById(R.id.refqtev);
                final FormEditText codever = (FormEditText) customView.findViewById(R.id.verifcodee);
                final LinearLayout numero=customView.findViewById(R.id.clavn);
                final LinearLayout lettre=customView.findViewById(R.id.clavl);
                codever.setTransformationMethod(PasswordTransformationMethod.getInstance());

                final Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,vi,w,x,y,z,espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero, virgule;

                a=(Button)customView.findViewById(R.id.a); b=(Button)customView.findViewById(R.id.b);c=(Button)customView.findViewById(R.id.c);
                d=(Button)customView.findViewById(R.id.d); e=(Button)customView.findViewById(R.id.e); f=(Button)customView.findViewById(R.id.f);
                g=(Button)customView.findViewById(R.id.g); h=(Button)customView.findViewById(R.id.h); i=(Button)customView.findViewById(R.id.i);
                j=(Button)customView.findViewById(R.id.j); k=(Button)customView.findViewById(R.id.k); l=(Button)customView.findViewById(R.id.l);
                m=(Button)customView.findViewById(R.id.m); n=(Button)customView.findViewById(R.id.n); o=(Button)customView.findViewById(R.id.o);
                p=(Button)customView.findViewById(R.id.p); r=(Button)customView.findViewById(R.id.r); s=(Button)customView.findViewById(R.id.s);
                t=(Button)customView.findViewById(R.id.t); u=(Button)customView.findViewById(R.id.u); vi=(Button)customView.findViewById(R.id.v);
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
                virgule=(Button)customView.findViewById(R.id.btnvirgule);

                final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewref);
                qteref.setInputType(0);
                qterefv.setInputType(0);
                codever.setInputType(0);
                contexte.setInputType(0);

                contexte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        numero.setVisibility(View.GONE);
                        lettre.setVisibility(View.VISIBLE);

                        cleard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=contexte.getText().toString();
                                if (chaine.equals("")){

                                }else {
                                    chaine = chaine.substring(0, chaine.length()-1);
                                    contexte.setText(""+chaine);
                                }

                            }
                        });

                        espaced.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append(" ");
                            }
                        });

                        a.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("a");
                            }
                        });

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("b");
                            }
                        });

                        c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("c");
                            }
                        });

                        d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("d");
                            }
                        });

                        e.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("e");
                            }
                        });

                        f.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("f");
                            }
                        });

                        g.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("g");
                            }
                        });

                        h.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("h");
                            }
                        });

                        i.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("i");
                            }
                        });
                        j.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("j");
                            }
                        });
                        k.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("k");
                            }
                        });
                        l.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("l");
                            }
                        });
                        m.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("m");
                            }
                        });
                        n.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("n");
                            }
                        });
                        o.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("o");
                            }
                        });
                        p.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("p");
                            }
                        });
                        q.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("q");
                            }
                        });
                        r.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("r");
                            }
                        });
                        s.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("s");
                            }
                        });
                        t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("t");
                            }
                        });
                        u.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("u");
                            }
                        });
                        vi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("v");
                            }
                        });
                        w.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("w");
                            }
                        });
                        x.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("x");
                            }
                        });
                        y.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("y");
                            }
                        });
                        z.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("z");
                            }
                        });
                    }
                });

                codever.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        numero.setVisibility(View.VISIBLE);
                        lettre.setVisibility(View.GONE);

                        zero.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+0);
                                }
                                else {
                                    codever.append(""+0);
                                }
                            }
                        });

                        un.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+1);
                                }
                                else {
                                    codever.append(""+1);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                        deux.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+2);
                                }
                                else {
                                    codever.append(""+2);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        trois.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+3);
                                }
                                else {
                                    codever.append(""+3);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        quatre.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+4);
                                }
                                else {
                                    codever.append(""+4);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        cinq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+5);
                                }
                                else {
                                    codever.append(""+5);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        six.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+6);
                                }
                                else {
                                    codever.append(""+6);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        sept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+7);
                                }
                                else {
                                    codever.append(""+7);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        huit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+8);
                                }
                                else {
                                    codever.append(""+8);
                                }


                            }
                        });

                        neuf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+9);
                                }
                                else {
                                    codever.append(""+9);
                                }

                            }
                        });



                        cleardn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();
                                if (chaine.equals("")){

                                }else {
                                    chaine = chaine.substring(0, chaine.length()-1);
                                    codever.setText(""+chaine);
                                }

                            }
                        });

                    }
                });
                qteref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        {
                            numero.setVisibility(View.VISIBLE);
                            lettre.setVisibility(View.GONE);

                            zero.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+0);
                                    }
                                    else {
                                        qteref.append(""+0);
                                    }
                                }
                            });

                            un.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+1);
                                    }
                                    else {
                                        qteref.append(""+1);
                                    }
                                }
                            });

                            deux.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+2);
                                    }
                                    else {
                                        qteref.append(""+2);
                                    }

                                }
                            });

                            trois.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+3);
                                    }
                                    else {
                                        qteref.append(""+3);
                                    }

                                }
                            });

                            quatre.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+4);
                                    }
                                    else {
                                        qteref.append(""+4);
                                    }

                                }
                            });

                            cinq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+5);
                                    }
                                    else {
                                        qteref.append(""+5);
                                    }

                                }
                            });

                            six.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+6);
                                    }
                                    else {
                                        qteref.append(""+6);
                                    }

                                }
                            });

                            sept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+7);
                                    }
                                    else {
                                        qteref.append(""+7);
                                    }

                                }
                            });

                            huit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+8);
                                    }
                                    else {
                                        qteref.append(""+8);
                                    }


                                }
                            });

                            neuf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+9);
                                    }
                                    else {
                                        qteref.append(""+9);
                                    }

                                }
                            });

                            virgule.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        //qterefv.append(".");
                                        //Toast.makeText(getApplicationContext(),"Veuillez écrire une chiffre d'abord", Toast.LENGTH_SHORT).show();
                                    }
                                    if(chaine.length()!=0) {
                                        //
                                        if(chaine.contains(".")){

                                        }
                                        else {
                                            qteref.append(".");
                                        }
                                    }

                                }
                            });



                            cleardn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();
                                    if (chaine.equals("")){

                                    }else {
                                        chaine = chaine.substring(0, chaine.length()-1);
                                        qteref.setText(""+chaine);
                                    }

                                }
                            });

                        }
                    }
                });

                qterefv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        {
                            numero.setVisibility(View.VISIBLE);
                            lettre.setVisibility(View.GONE);

                            zero.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+0);
                                    }
                                    else {
                                        qterefv.append(""+0);
                                    }
                                }
                            });

                            un.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+1);
                                    }
                                    else {
                                        qterefv.append(""+1);
                                    }
                                }
                            });

                            deux.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+2);
                                    }
                                    else {
                                        qterefv.append(""+2);
                                    }

                                }
                            });

                            trois.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+3);
                                    }
                                    else {
                                        qterefv.append(""+3);
                                    }

                                }
                            });

                            quatre.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+4);
                                    }
                                    else {
                                        qterefv.append(""+4);
                                    }

                                }
                            });

                            cinq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+5);
                                    }
                                    else {
                                        qterefv.append(""+5);
                                    }

                                }
                            });

                            six.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+6);
                                    }
                                    else {
                                        qterefv.append(""+6);
                                    }

                                }
                            });

                            sept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+7);
                                    }
                                    else {
                                        qterefv.append(""+7);
                                    }

                                }
                            });

                            huit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+8);
                                    }
                                    else {
                                        qterefv.append(""+8);
                                    }


                                }
                            });

                            neuf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+9);
                                    }
                                    else {
                                        qterefv.append(""+9);
                                    }

                                }
                            });

                            virgule.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        //qterefv.append(".");
                                        //Toast.makeText(getApplicationContext(),"Veuillez écrire une chiffre d'abord", Toast.LENGTH_SHORT).show();
                                    }
                                    if(chaine.length()!=0) {
                                        //
                                        if(chaine.contains(".")){

                                        }
                                        else {
                                            qterefv.append(".");
                                        }
                                    }

                                }
                            });



                            cleardn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();
                                    if (chaine.equals("")){

                                    }else {
                                        chaine = chaine.substring(0, chaine.length()-1);
                                        qterefv.setText(""+chaine);
                                    }

                                }
                            });

                        }
                    }
                });

                //qteref.setText(qte);

                RegisterRefbuilder.setCancelable(true);
                RegisterRefbuilder.setView(customView);
                RegisterRefbuilder.setTitle(("Augmenter la quantité"));
                RegisterRefdialog = RegisterRefbuilder.create();
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String lecode=codever.getText().toString();
                        String leboutiqcode="";
                        if(lecode.equals("")){
                            Toast.makeText(getApplicationContext(),"Merci de renseigner le code",Toast.LENGTH_SHORT).show();
                        }
                        if(!lecode.equals("")){
                            Cursor bouti=dbHelper.getBoutique("1");
                            if(bouti.moveToFirst()){
                                leboutiqcode=bouti.getString(4);
                                if(!leboutiqcode.equals(lecode)){
                                    Toast.makeText(getApplicationContext(),"Code incorrect ",Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(getApplicationContext(),"le code "+bouti.getString(3),Toast.LENGTH_SHORT).show();
                                if (leboutiqcode.equals(lecode)){
                                    codever.setVisibility(View.GONE);
                                    qteref.setVisibility(View.VISIBLE);
                                    qterefv.setVisibility(View.VISIBLE);
                                    contexte.setVisibility(View.VISIBLE);
                                    contexte.requestFocus();
                                    updaterefqte(qteref,qterefv,laqte,contexte);
                                }
                            }
                            bouti.close();
                        }



                    }
                });
                RegisterRefdialog.show();
            }
        });

        qtedim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterRefbuilder = new AlertDialog.Builder(GestRefSql.this);
                View customView = getLayoutInflater().inflate(R.layout.dialog_update_refqtemoin, null, false);


                final FormEditText qteref = (FormEditText) customView.findViewById(R.id.refqte);
                final FormEditText qterefv = (FormEditText) customView.findViewById(R.id.refqtev);
                final FormEditText codever = (FormEditText) customView.findViewById(R.id.verifcodee);
                final FormEditText contexte = (FormEditText) customView.findViewById(R.id.contexqte);

                final LinearLayout numero=customView.findViewById(R.id.clavn);
                final LinearLayout lettre=customView.findViewById(R.id.clavl);
                codever.setTransformationMethod(PasswordTransformationMethod.getInstance());

                final Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,vi,w,x,y,z,espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero, virgule;

                a=(Button)customView.findViewById(R.id.a); b=(Button)customView.findViewById(R.id.b);c=(Button)customView.findViewById(R.id.c);
                d=(Button)customView.findViewById(R.id.d); e=(Button)customView.findViewById(R.id.e); f=(Button)customView.findViewById(R.id.f);
                g=(Button)customView.findViewById(R.id.g); h=(Button)customView.findViewById(R.id.h); i=(Button)customView.findViewById(R.id.i);
                j=(Button)customView.findViewById(R.id.j); k=(Button)customView.findViewById(R.id.k); l=(Button)customView.findViewById(R.id.l);
                m=(Button)customView.findViewById(R.id.m); n=(Button)customView.findViewById(R.id.n); o=(Button)customView.findViewById(R.id.o);
                p=(Button)customView.findViewById(R.id.p); r=(Button)customView.findViewById(R.id.r); s=(Button)customView.findViewById(R.id.s);
                t=(Button)customView.findViewById(R.id.t); u=(Button)customView.findViewById(R.id.u); vi=(Button)customView.findViewById(R.id.v);
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
                virgule=(Button)customView.findViewById(R.id.btnvirgule);


                qteref.setInputType(0);
                qterefv.setInputType(0);
                codever.setInputType(0);
                contexte.setInputType(0);

                contexte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        numero.setVisibility(View.GONE);
                        lettre.setVisibility(View.VISIBLE);

                        cleard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=contexte.getText().toString();
                                if (chaine.equals("")){

                                }else {
                                    chaine = chaine.substring(0, chaine.length()-1);
                                    contexte.setText(""+chaine);
                                }

                            }
                        });

                        espaced.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append(" ");
                            }
                        });

                        a.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("a");
                            }
                        });

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("b");
                            }
                        });

                        c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("c");
                            }
                        });

                        d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("d");
                            }
                        });

                        e.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("e");
                            }
                        });

                        f.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("f");
                            }
                        });

                        g.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("g");
                            }
                        });

                        h.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("h");
                            }
                        });

                        i.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("i");
                            }
                        });
                        j.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("j");
                            }
                        });
                        k.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("k");
                            }
                        });
                        l.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("l");
                            }
                        });
                        m.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("m");
                            }
                        });
                        n.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("n");
                            }
                        });
                        o.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("o");
                            }
                        });
                        p.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("p");
                            }
                        });
                        q.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("q");
                            }
                        });
                        r.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("r");
                            }
                        });
                        s.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("s");
                            }
                        });
                        t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("t");
                            }
                        });
                        u.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("u");
                            }
                        });
                        vi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("v");
                            }
                        });
                        w.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("w");
                            }
                        });
                        x.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("x");
                            }
                        });
                        y.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("y");
                            }
                        });
                        z.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contexte.append("z");
                            }
                        });
                    }
                });

                codever.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        numero.setVisibility(View.VISIBLE);
                        lettre.setVisibility(View.GONE);

                        zero.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+0);
                                }
                                else {
                                    codever.append(""+0);
                                }
                            }
                        });

                        un.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+1);
                                }
                                else {
                                    codever.append(""+1);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                        deux.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+2);
                                }
                                else {
                                    codever.append(""+2);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        trois.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+3);
                                }
                                else {
                                    codever.append(""+3);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        quatre.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+4);
                                }
                                else {
                                    codever.append(""+4);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        cinq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+5);
                                }
                                else {
                                    codever.append(""+5);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        six.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+6);
                                }
                                else {
                                    codever.append(""+6);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        sept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+7);
                                }
                                else {
                                    codever.append(""+7);
                                }

                                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                            }
                        });

                        huit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+8);
                                }
                                else {
                                    codever.append(""+8);
                                }


                            }
                        });

                        neuf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+9);
                                }
                                else {
                                    codever.append(""+9);
                                }

                            }
                        });



                        cleardn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();
                                if (chaine.equals("")){

                                }else {
                                    chaine = chaine.substring(0, chaine.length()-1);
                                    codever.setText(""+chaine);
                                }

                            }
                        });

                    }
                });
                qteref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        {
                            numero.setVisibility(View.VISIBLE);
                            lettre.setVisibility(View.GONE);

                            zero.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+0);
                                    }
                                    else {
                                        qteref.append(""+0);
                                    }
                                }
                            });

                            un.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+1);
                                    }
                                    else {
                                        qteref.append(""+1);
                                    }
                                }
                            });

                            deux.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+2);
                                    }
                                    else {
                                        qteref.append(""+2);
                                    }

                                }
                            });

                            trois.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+3);
                                    }
                                    else {
                                        qteref.append(""+3);
                                    }

                                }
                            });

                            quatre.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+4);
                                    }
                                    else {
                                        qteref.append(""+4);
                                    }

                                }
                            });

                            cinq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+5);
                                    }
                                    else {
                                        qteref.append(""+5);
                                    }

                                }
                            });

                            six.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+6);
                                    }
                                    else {
                                        qteref.append(""+6);
                                    }

                                }
                            });

                            sept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+7);
                                    }
                                    else {
                                        qteref.append(""+7);
                                    }

                                }
                            });

                            huit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+8);
                                    }
                                    else {
                                        qteref.append(""+8);
                                    }


                                }
                            });

                            neuf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        qteref.append(""+9);
                                    }
                                    else {
                                        qteref.append(""+9);
                                    }

                                }
                            });

                            virgule.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qteref.getText().toString();

                                    if(chaine.contains("")){
                                        //qterefv.append(".");
                                        //Toast.makeText(getApplicationContext(),"Veuillez écrire une chiffre d'abord", Toast.LENGTH_SHORT).show();
                                    }
                                    if(chaine.length()!=0) {
                                        //
                                        if(chaine.contains(".")){

                                        }
                                        else {
                                            qteref.append(".");
                                        }
                                    }

                                }
                            });


                            cleardn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qteref.getText().toString();
                                    if (chaine.equals("")){

                                    }else {
                                        chaine = chaine.substring(0, chaine.length()-1);
                                        qteref.setText(""+chaine);
                                    }

                                }
                            });

                        }
                    }
                });

                qterefv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        {
                            numero.setVisibility(View.VISIBLE);
                            lettre.setVisibility(View.GONE);

                            zero.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+0);
                                    }
                                    else {
                                        qterefv.append(""+0);
                                    }
                                }
                            });

                            un.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+1);
                                    }
                                    else {
                                        qterefv.append(""+1);
                                    }
                                }
                            });

                            deux.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+2);
                                    }
                                    else {
                                        qterefv.append(""+2);
                                    }

                                }
                            });

                            trois.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+3);
                                    }
                                    else {
                                        qterefv.append(""+3);
                                    }

                                }
                            });

                            quatre.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+4);
                                    }
                                    else {
                                        qterefv.append(""+4);
                                    }

                                }
                            });

                            cinq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+5);
                                    }
                                    else {
                                        qterefv.append(""+5);
                                    }

                                }
                            });

                            six.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+6);
                                    }
                                    else {
                                        qterefv.append(""+6);
                                    }

                                }
                            });

                            sept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+7);
                                    }
                                    else {
                                        qterefv.append(""+7);
                                    }

                                }
                            });

                            huit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+8);
                                    }
                                    else {
                                        qterefv.append(""+8);
                                    }


                                }
                            });

                            neuf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        qterefv.append(""+9);
                                    }
                                    else {
                                        qterefv.append(""+9);
                                    }

                                }
                            });

                            virgule.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String chaine=qterefv.getText().toString();

                                    if(chaine.contains("")){
                                        //qterefv.append(".");
                                        //Toast.makeText(getApplicationContext(),"Veuillez écrire une chiffre d'abord", Toast.LENGTH_SHORT).show();
                                    }
                                    if(chaine.length()!=0) {
                                        //
                                        if(chaine.contains(".")){

                                        }
                                        else {
                                            qterefv.append(".");
                                        }
                                    }

                                }
                            });

                            cleardn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=qterefv.getText().toString();
                                    if (chaine.equals("")){

                                    }else {
                                        chaine = chaine.substring(0, chaine.length()-1);
                                        qterefv.setText(""+chaine);
                                    }

                                }
                            });

                        }
                    }
                });
                final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewref);
                //qteref.setText(qte);

                RegisterRefbuilder.setCancelable(true);
                RegisterRefbuilder.setView(customView);
                RegisterRefbuilder.setTitle(("Diminuer la quantité"));
                RegisterRefdialog = RegisterRefbuilder.create();
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String lecode=codever.getText().toString();
                        String leboutiqcode="";
                        if(lecode.equals("")){
                            Toast.makeText(getApplicationContext(),"Merci de renseigner le code",Toast.LENGTH_SHORT).show();
                        }
                        if(!lecode.equals("")){
                            Cursor bouti=dbHelper.getBoutique("1");
                            if(bouti.moveToFirst()){
                                leboutiqcode=bouti.getString(4);
                                if(!leboutiqcode.equals(lecode)){
                                    Toast.makeText(getApplicationContext(),"Code incorrect ",Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(getApplicationContext(),"le code "+bouti.getString(3),Toast.LENGTH_SHORT).show();
                                if (leboutiqcode.equals(lecode)){
                                    codever.setVisibility(View.GONE);
                                    qteref.setVisibility(View.VISIBLE);
                                    qterefv.setVisibility(View.VISIBLE);
                                    contexte.setVisibility(View.VISIBLE);
                                    contexte.requestFocus();
                                    updaterefqtemoin(qteref,qterefv,laqte,contexte);
                                }
                            }
                            bouti.close();
                        }



                    }
                });
                RegisterRefdialog.show();
            }
        });


        supprefe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**/

                RegisterSupbuilder = new AlertDialog.Builder(v.getContext());
                View customView = getLayoutInflater().inflate(R.layout.dialog_delete_invoice, null, false);

                final TextView textView = (TextView) customView.findViewById(R.id.text);

                final ImageView okbtn = (ImageView) customView.findViewById(R.id.okdelinv);
                final FormEditText codever = (FormEditText) customView.findViewById(R.id.verifcodee);

                final LinearLayout numero=customView.findViewById(R.id.clavn);

                codever.setTransformationMethod(PasswordTransformationMethod.getInstance());

                final Button espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;

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

                RegisterSupbuilder.setCancelable(true);
                RegisterSupbuilder.setView(customView);
                RegisterSupbuilder.setTitle(("Suppression de la référence"));

                textView.setText("Confirmer la suppression de la référence");

                codever.setInputType(0);

                codever.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        numero.setVisibility(View.VISIBLE);


                        zero.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+0);
                                }
                                else {
                                    codever.append(""+0);
                                }
                            }
                        });

                        un.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+1);
                                }
                                else {
                                    codever.append(""+1);
                                }
                            }
                        });

                        deux.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+2);
                                }
                                else {
                                    codever.append(""+2);
                                }

                            }
                        });

                        trois.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+3);
                                }
                                else {
                                    codever.append(""+3);
                                }

                            }
                        });

                        quatre.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+4);
                                }
                                else {
                                    codever.append(""+4);
                                }

                            }
                        });

                        cinq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+5);
                                }
                                else {
                                    codever.append(""+5);
                                }

                            }
                        });

                        six.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+6);
                                }
                                else {
                                    codever.append(""+6);
                                }

                            }
                        });

                        sept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+7);
                                }
                                else {
                                    codever.append(""+7);
                                }

                            }
                        });

                        huit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+8);
                                }
                                else {
                                    codever.append(""+8);
                                }


                            }
                        });

                        neuf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String chaine=codever.getText().toString();

                                if(chaine.contains("")){
                                    codever.append(""+9);
                                }
                                else {
                                    codever.append(""+9);
                                }

                            }
                        });



                        cleardn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String chaine=codever.getText().toString();
                                if (chaine.equals("")){

                                }else {
                                    chaine = chaine.substring(0, chaine.length()-1);
                                    codever.setText(""+chaine);
                                }

                            }
                        });
                    }
                });

                RegisterSupDialog = RegisterSupbuilder.create();


                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String lecode=codever.getText().toString();
                        String leboutiqcode="";
                        if(lecode.equals("")){
                            Toast.makeText(getApplicationContext(),"Merci de renseigner le code",Toast.LENGTH_SHORT).show();
                        }
                        if(!lecode.equals("")){
                            Cursor bouti=dbHelper.getBoutique("1");
                            if(bouti.moveToFirst()){
                                leboutiqcode=bouti.getString(4);
                                if(!leboutiqcode.equals(lecode)){
                                    Toast.makeText(getApplicationContext(),"Code incorrect ",Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(getApplicationContext(),"le code "+bouti.getString(3),Toast.LENGTH_SHORT).show();
                                if (leboutiqcode.equals(lecode)){

                                    dbHelper.suppReference(idref);
                                    Intent intent=new Intent(GestRefSql.this, ReferencesSQL.class);
                                    startActivity(intent);
                                    finish();                                }
                            }
                            bouti.close();
                        }

                    }
                });


                RegisterSupDialog.show();
            }
        });


    }

   /* private void updaterefnom(FormEditText nomrefe, AlertDialog registerRefdialog) {
        String namereference = nomrefe.getText().toString();
        if ( namereference.equals("")) {

            Toast.makeText(getApplicationContext(),"Veuillez remplir le champ merci",Toast.LENGTH_SHORT).show();
        } else {

            dbHelper.updateReference(namereference,nomref,"",idref);

            RegisterRefdialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), ReferencesSQL.class);
            startActivity(intent);
            finish();
        }
    }*/

    public void updaterefprix( FormEditText priceref, AlertDialog alertDialog) {
        String pricereference = priceref.getText().toString();
        if ( pricereference.equals("")) {

            Toast.makeText(getApplicationContext(),"Veuillez remplir le champ merci",Toast.LENGTH_SHORT).show();
        } else {

            dbHelper.updateRefprice(pricereference,idref);

            RegisterRefdialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), GestRefSql.class);
            intent.putExtra(GestRefSql.EXTRA_REF_ID, idref);
            startActivity(intent);
            finish();
        }

    }

    public void updaterefcateg( String categ, AlertDialog alertDialog) {

        if ( categ.equals("")) {

            Toast.makeText(getApplicationContext(),"Veuillez choisir une couleur merci",Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updateRefcat(categ, idref);

            RegisterRefdialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), GestRefSql.class);
            intent.putExtra(GestRefSql.EXTRA_REF_ID, idref);
            startActivity(intent);
            finish();
        }
    }



    public void updaterefqte( FormEditText qteref,FormEditText qterefv, double laquantite,FormEditText contex) {
        String qtereference = qteref.getText().toString();
        String qtereferencev = qterefv.getText().toString();
        String cont = contex.getText().toString();
        if ( qtereference.equals("") || qtereferencev.equals("")) {

            Toast.makeText(getApplicationContext(),"Veuillez remplir les champs merci",Toast.LENGTH_SHORT).show();
        } else {

            if(!qtereference.equals(qtereferencev)){
                Toast.makeText(getApplicationContext(),"les quantités ne correspondent pas",Toast.LENGTH_SHORT).show();
            }

           /* if(qtereference.equals(qtereferencev) && !qtereference.matches(qtePattern) && !qtereferencev.matches(qtePattern)){
                Toast.makeText(getApplicationContext(),"Quantités saisies incorrects",Toast.LENGTH_SHORT).show();

            }*/

            if(qtereference.equals(qtereferencev)){
                double qtereff=Double.valueOf(qtereference);
                double total=qtereff+laquantite;
                qtereference=String.valueOf(total);
                String ladate= Config.getFormattedDate(Calendar.getInstance().getTime());
                dbHelper.updateRefqte(qtereference,ladate,idref,cont);
                dbHelper.insertSuivRQ(ladate,String.valueOf(qtereff),idref,cont+" $ Entree",prix);

                RegisterRefdialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), GestRefSql.class);
                intent.putExtra(GestRefSql.EXTRA_REF_ID, idref);
                startActivity(intent);
                finish();
            }

        }

    }

    public void updaterefqtemoin( FormEditText qteref,FormEditText qterefv, double laquantite, FormEditText contex) {
        String qtereference = qteref.getText().toString();
        String qtereferencev = qterefv.getText().toString();
        String cont = contex.getText().toString();
        if ( qtereference.equals("") || qtereferencev.equals("")) {

            Toast.makeText(getApplicationContext(),"Veuillez remplir les champs merci",Toast.LENGTH_SHORT).show();
        } else {

            if(!qtereference.equals(qtereferencev)){
                Toast.makeText(getApplicationContext(),"les quantités ne correspondent pas",Toast.LENGTH_SHORT).show();
            }

            if(qtereference.equals(qtereferencev)){
                double qtereff=Double.valueOf(qtereference);
                double qteactu=0;
                Cursor cursor1=dbHelper.getrefname(nomref);
                if(cursor1.moveToFirst()){
                    idref=cursor1.getString(0);
                    qteactu=Double.valueOf(cursor1.getString(3));
                    if(qteactu<0){
                        qteactu+=Double.valueOf(qtereff);
                    }
                    if(qteactu>0){
                        qteactu-=Double.valueOf(qtereff);
                    }

                    //Toast.makeText(NewInterface.this, ""+qteactu, Toast.LENGTH_SHORT).show();
                }
                cursor1.close();
                qte=String.valueOf(qteactu);
                String ladate= Config.getFormattedDate(Calendar.getInstance().getTime());
                dbHelper.updateRefqte(qte,ladate,idref,cont);
                dbHelper.insertSuivRQ(ladate,String.valueOf(qtereff),idref,cont+" $ Sortie",prix);

                RegisterRefdialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), GestRefSql.class);
                intent.putExtra(GestRefSql.EXTRA_REF_ID, idref);
                startActivity(intent);
                finish();
            }

        }

    }

    /*SharedPreferences sharedPrefs = this.getSharedPreferences(
            PREF_UNIQUE_ID, Context.MODE_PRIVATE);
    uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

    SharedPreferences sharedPrefs1 = this.getSharedPreferences(
            PREF_IDLOT, Context.MODE_PRIVATE);
    IDLOT = sharedPrefs1.getString(PREF_IDLOT, null);*/

    public void updaterefqteFIU( FormEditText qteref,FormEditText qterefv, double laquantite, FormEditText contex, String prix) {
        String qtereference = qteref.getText().toString();
        String qtereferencev = qterefv.getText().toString();
        String cont = contex.getText().toString();
        if ( qtereference.equals("") || qtereferencev.equals("")) {

            Toast.makeText(getApplicationContext(),"Veuillez remplir les champs merci",Toast.LENGTH_SHORT).show();
        } else {

            if(!qtereference.equals(qtereferencev)){
                Toast.makeText(getApplicationContext(),"les quantités ne correspondent pas",Toast.LENGTH_SHORT).show();
            }

            if(qtereference.equals(qtereferencev)){

                String ladate= Config.getFormattedDate(Calendar.getInstance().getTime());
                //dbHelper.updateRefqte(qte,ladate,idref,cont);
                dbHelper.insertSuivRQ(ladate,String.valueOf(qtereferencev),idref,cont+" $ FIU",prix);
                /*String  idrefe="";
                String  datesss="";
                String  qtee="";

                Cursor cursor1=dbHelper.getREFSUIVLast();
                if(cursor1.moveToFirst()){
                    idrefe=cursor1.getString(0);
                    datesss=cursor1.getString(1);
                    qtee=cursor1.getString(3);
                    Toast.makeText(GestRefSql.this, ""+idrefe+"\n"+datesss+"\n"+qtee,
                            Toast.LENGTH_LONG).show();
                }
                cursor1.close();*/

                RegisterRefdialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Activity_Lot.class);
                intent.putExtra("nomref", nomref);
                startActivity(intent);
                /*Intent lintent=new Intent(getApplicationContext(),Activity_Lot.class);
                lintent.putExtra("lid",idrefe);
                lintent.putExtra("date",datesss);
                lintent.putExtra("nomref",nomref);
                lintent.putExtra("qt",qtee);
                lintent.putExtra("prix",prix);
                lintent.putExtra("total",String.valueOf(Double.valueOf(qtee)*Double.valueOf(prix)));
                startActivity(lintent);*/
                finish();
            }

        }

    }

    public void registerOnFiu(final String lenom, String idlot, String prix, String qte){
        Dialog d = new AlertDialog.Builder(this)
                .setTitle("Passer sur le MarketPlace")
                .setMessage("Nom : "+lenom+"\nLot : "+idlot+"\nPrix unitaire : "+prix+"\nQuantité : "+qte+"\nSous total : "
                        +Double.valueOf(qte)*Double.valueOf(prix))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // msg.delete();

                            /*Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, lenom);
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, "Envoyer"));*/

                    }
                })
                .create();
        d.setOwnerActivity(this); // why can't the builder do this?
        d.show();

    }



    private class StockSortiAdapter extends BaseAdapter {

        private List<LaReffb> sorties = null;

        public StockSortiAdapter(List<LaReffb> list) {
            sorties = list;
        }


        @Override
        public int getCount() {
            if (sorties != null)
                return sorties.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {

            return sorties.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_sorties, parent, false);
            }

            final TextView contexte = (TextView) convertView.findViewById(R.id.textecont);
            TextView textedate = (TextView) convertView.findViewById(R.id.textedates);
            //TextView depotDate = (TextView) convertView.findViewById(R.id.deposit_date);

            TextView qute = (TextView) convertView.findViewById(R.id.qtee);
            Button voirsortie = (Button) convertView.findViewById(R.id.voires);
            final String lid = sorties.get(position).getId();
            final LaReffb sortiess = sorties.get(position);


            String bontext="";
            final String qt = sorties.get(position).getLarefqte();
            String commente=sorties.get(position).getContexts();
            final String datees=sorties.get(position).getDateen();
            if(!commente.contains("")){
                bontext=commente.substring(0,commente.length()-8);
            }
            if(commente.contains("Vente")){
                voirsortie.setBackgroundColor(Color.GREEN);
                voirsortie.setText("vente");
                voirsortie.setOnClickListener(null);
                textedate.setText("Vente du : "+datees);
            }

            if(commente.contains("Sortie")){
                voirsortie.setBackgroundColor(Color.RED);
                voirsortie.setText("sortie");
                voirsortie.setOnClickListener(null);
                textedate.setText("Sortie du : "+datees);
            }
            if(commente.contains("FIU")){
                voirsortie.setBackgroundColor(Color.rgb(243, 156, 18));
                voirsortie.setText("LOT");
                textedate.setText("Lot du : "+datees);
                voirsortie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(),""+lid+"\n"+nomref,Toast.LENGTH_SHORT).show();
                        //registerOnFiu(nomref,lid,prix,qt);
                       /* texttosend+="Lot n° : "+lid+"\n";
                        texttosend+="Date : "+datees+"\n\n";
                        texttosend+="Articles            | Qt | Prix | Valeur\n";
                        texttosend+="------------------------------------------\n";
                        texttosend+=nomref+"  | "+qt+" | "+prix+" | "+Double.valueOf(qt)*Double.valueOf(prix)+"\n";
                        texttosend+="------------------------------------------\n";
                        texttosend+="                        Total(CFA) : "+Double.valueOf(qt)*Double.valueOf(prix)+"\n";
                        texttosend+="Commander : \n";
                        texttosend+="fiu.weebi.com/"+lid+"-bdp@gmail.com-"+Double.valueOf(qt)*Double.valueOf(prix)+"\n";
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, texttosend);
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp");
                        startActivity(sendIntent);*/
                       Intent lintent=new Intent(getApplicationContext(),Activity_Lot.class);
                       lintent.putExtra("lid",lid);
                        lintent.putExtra("date",datees);
                        lintent.putExtra("nomref",nomref);
                        lintent.putExtra("qt",qt);
                        lintent.putExtra("prix",prix);
                        lintent.putExtra("total",String.valueOf(Double.valueOf(qt)*Double.valueOf(prix)));
                        startActivity(lintent);
                    }
                });
            }

            //String bontext=commente.substring(0,commente.length()-8);
            //String tab[]=commente.split("$");

            //String bontext=tab[0];

            contexte.setText(bontext);
            qute.setText("Quantite = "+qt);
            return convertView;
        }
    }

    private class StockSortiEntreeAdapter extends BaseAdapter {

        private List<LaReffb> entrees = null;

        public StockSortiEntreeAdapter(List<LaReffb> list) {
            entrees = list;
        }


        @Override
        public int getCount() {
            if (entrees != null)
                return entrees.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return entrees.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_entrees, parent, false);
            }

            TextView contexte = (TextView) convertView.findViewById(R.id.textecont);
            TextView textedate = (TextView) convertView.findViewById(R.id.textedate);
            //TextView depotDate = (TextView) convertView.findViewById(R.id.deposit_date);
            TextView qute = (TextView) convertView.findViewById(R.id.qtee);
            Button voirsortie = (Button) convertView.findViewById(R.id.voires);
            final String lid = entrees.get(position).getId();
            final LaReffb entreess = entrees.get(position);


            String qt = entrees.get(position).getLarefqte();
            String commente=entrees.get(position).getContexts();
            String datte=entrees.get(position).getDateen();
           // String tab[]=commente.split("$");
            String bontext=commente.substring(0,commente.length()-8);
           // String bontext=tab[0];

            contexte.setText(bontext);
            textedate.setText("Entree du : "+datte);
            qute.setText("Quantite = "+qt);

            return convertView;
        }
    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_gest_ref_sql;
    }

}
