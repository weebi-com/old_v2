package com.weebinatidi.ui.weebi2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.Boutique;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.ReferenceRepository;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.References;
import com.weebinatidi.ui.Usbimprime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.RealmQuery;

import static android.view.View.VISIBLE;
import static com.weebinatidi.ui.weebi2.NewInterface.isSmart;
import static com.weebinatidi.ui.weebi2.NewInterface.isSmartsmall;
import static com.weebinatidi.ui.weebi2.NewInterface.isTablet;
import static com.weebinatidi.ui.weebi2.NewInterface.isTablet7;

public class Parametres extends BaseActivity {

    private String langue = "";
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencescode;
    DbHelper dbHelper;
    private String leboutiqnum;
    private String leboutiqnam;
    private String leboutiqcode;
    private String leboutiqmail;
    String Images=".photos";

    AlertDialog.Builder RegisterCodebuilder;
    AlertDialog RegisterCodeDialog;

    String entree="";
    String clientName="";
    String clientPhone="";
    String clientmail="";


    Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,vi,w,x,y,z,
            espace,clear,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero, clearnum,arobase, point;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // CONTROLE DE LA VERSION ANDROID
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
        } else {

            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
            }
        }

        //création du répertoire de weebi pour la base de donnée et la sauvegarde
        File file,file1 = null;
        String filePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), "Weebi/Fichier");
            file1 = new File(Environment.getExternalStorageDirectory(), "Weebi/"+Images);
            if (!file.exists()) {
                file.mkdirs();
                file1.mkdirs();
            }
        }

        dbHelper=new DbHelper(this);

        // CONTROLE DE LA VERSION ANDROID
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
            Cursor bouti=dbHelper.getBoutique("1");
            if(bouti.moveToFirst()){
                //Toast.makeText(getApplicationContext(),""+bouti.getString(0)
                //        +"\n"+bouti.getString(1)+"\n"+bouti.getString(2)+"\n"+bouti.getString(3),Toast.LENGTH_LONG).show();
                leboutiqnum=bouti.getString(1);
                leboutiqnam=bouti.getString(2);
                leboutiqmail=bouti.getString(3);
                leboutiqcode=bouti.getString(4);
                Intent gotostart = new Intent(Parametres.this, NewInterface.class);
                startActivity(gotostart);
                finish();

            }
            if(!bouti.moveToFirst()){
                Toast.makeText(getApplicationContext(),"Veuillez configurer votre boutique merci",Toast.LENGTH_SHORT).show();
            }
        } else {

            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
                /**/
                Cursor bouti=dbHelper.getBoutique("1");
                if(bouti.moveToFirst()){
                    //Toast.makeText(getApplicationContext(),""+bouti.getString(0)
                    //        +"\n"+bouti.getString(1)+"\n"+bouti.getString(2)+"\n"+bouti.getString(3),Toast.LENGTH_LONG).show();
                    leboutiqnum=bouti.getString(1);
                    leboutiqnam=bouti.getString(2);
                    leboutiqmail=bouti.getString(3);
                    leboutiqcode=bouti.getString(4);
                    Intent gotostart = new Intent(Parametres.this, NewInterface.class);
                    startActivity(gotostart);
                    finish();

                }
                if(!bouti.moveToFirst()){
                    Toast.makeText(getApplicationContext(),"Veuillez configurer votre boutique merci",Toast.LENGTH_SHORT).show();
                }
            }
        }

            final FormEditText username = (FormEditText) findViewById(R.id.boutik_name);
            final FormEditText phone = (FormEditText) findViewById(R.id.boutik_number);
            final EditText maiil = (EditText) findViewById(R.id.maill);
            //final Spinner spinner = (Spinner) findViewById(R.id.choixlangue);
           // final Button sauvergarde = findViewById(R.id.sauvegarder);

        final LinearLayout lettre=findViewById(R.id.clavl);
        final LinearLayout numeriq=findViewById(R.id.clavn);


        a=(Button)findViewById(R.id.a); b=(Button)findViewById(R.id.b);c=(Button)findViewById(R.id.c);
        d=(Button)findViewById(R.id.d); e=(Button)findViewById(R.id.e); f=(Button)findViewById(R.id.f);
        g=(Button)findViewById(R.id.g); h=(Button)findViewById(R.id.h); i=(Button)findViewById(R.id.i);
        j=(Button)findViewById(R.id.j); k=(Button)findViewById(R.id.k); l=(Button)findViewById(R.id.l);
        m=(Button)findViewById(R.id.m); n=(Button)findViewById(R.id.n); o=(Button)findViewById(R.id.o);
        p=(Button)findViewById(R.id.p); r=(Button)findViewById(R.id.r); s=(Button)findViewById(R.id.s);
        t=(Button)findViewById(R.id.t); u=(Button)findViewById(R.id.u); vi=(Button)findViewById(R.id.v);
        w=(Button)findViewById(R.id.w); x=(Button)findViewById(R.id.x); y=(Button)findViewById(R.id.y);
        q=(Button)findViewById(R.id.q); z=(Button)findViewById(R.id.z); arobase=(Button)findViewById(R.id.arobase);
        point=(Button)findViewById(R.id.point);

        un=(Button)findViewById(R.id.btnNum1Il);
        deux=(Button)findViewById(R.id.btnNum2Il);
        trois=(Button)findViewById(R.id.btnNum3Il);
        quatre=(Button)findViewById(R.id.btnNum4Il);
        cinq=(Button)findViewById(R.id.btnNum5Il);
        six=(Button)findViewById(R.id.btnNum6Il);
        sept=(Button)findViewById(R.id.btnNum7Il);
        huit=(Button)findViewById(R.id.btnNum8Il);
        neuf=(Button)findViewById(R.id.btnNum9Il);
        clearnum=(Button)findViewById(R.id.btnClearIl);
        //doublezero=(Button)findViewById(R.id.btn00);
        zero=(Button)findViewById(R.id.btnNum0Il);
        clear=(Button)findViewById(R.id.btnClearAl);
        espace=(Button)findViewById(R.id.espaced);

        //cacher le clavier android au démarrage de l'activité
        username.setInputType(0);
        phone.setInputType(0);
        maiil.setInputType(0);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.VISIBLE);
                numeriq.setVisibility(View.GONE);
                clear.setOnClickListener(new View.OnClickListener() {
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
                espace.setOnClickListener(new View.OnClickListener() {
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
                vi.setOnClickListener(new View.OnClickListener() {
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
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.GONE);
                numeriq.setVisibility(View.VISIBLE);

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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                clearnum.setOnClickListener(new View.OnClickListener() {
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
            }
        });

        maiil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.VISIBLE);
                numeriq.setVisibility(View.VISIBLE);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=maiil.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            maiil.setText(""+chaine);
                        }

                    }
                });
                espace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append(" ");
                    }
                });

                point.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append(".");
                    }
                });
                arobase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("@");
                    }
                });



                a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("a");
                    }
                });

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("b");
                    }
                });

                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("c");
                    }
                });

                d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("d");
                    }
                });

                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("e");
                    }
                });

                f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("f");
                    }
                });

                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("g");
                    }
                });

                h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("h");
                    }
                });

                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("i");
                    }
                });
                j.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("j");
                    }
                });
                k.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("k");
                    }
                });
                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("l");
                    }
                });
                m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("m");
                    }
                });
                n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("n");
                    }
                });
                o.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("o");
                    }
                });
                p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("p");
                    }
                });
                q.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("q");
                    }
                });
                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("r");
                    }
                });
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("s");
                    }
                });
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("t");
                    }
                });
                u.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("u");
                    }
                });
                vi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("v");
                    }
                });
                w.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("w");
                    }
                });
                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("x");
                    }
                });
                y.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("y");
                    }
                });
                z.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maiil.append("z");
                    }
                });
                zero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+0);
                        }
                        else {
                            maiil.append(""+0);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                un.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+1);
                        }
                        else {
                            maiil.append(""+1);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                deux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+2);
                        }
                        else {
                            maiil.append(""+2);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                trois.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+3);
                        }
                        else {
                            maiil.append(""+3);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                quatre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+4);
                        }
                        else {
                            maiil.append(""+4);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                cinq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+5);
                        }
                        else {
                            maiil.append(""+5);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+6);
                        }
                        else {
                            maiil.append(""+6);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                sept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+7);
                        }
                        else {
                            maiil.append(""+7);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                huit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+8);
                        }
                        else {
                            maiil.append(""+8);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                neuf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=maiil.getText().toString();

                        if(chaine.contains("")){
                            maiil.append(""+9);
                        }
                        else {
                            maiil.append(""+9);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                clearnum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=maiil.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            maiil.setText(""+chaine);
                        }

                    }
                });
            }
        });



        final ImageView okbtn = findViewById(R.id.oknewboutik);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
        /*if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }*/



        sharedPreferences = getBaseContext().getSharedPreferences("prefes", MODE_PRIVATE);
        sharedPreferencescode = getBaseContext().getSharedPreferences("prefescode", MODE_PRIVATE);

        if (sharedPreferences.contains("preflangue")) {
            langue = sharedPreferences.getString("preflangue", null);
            //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

            Configuration config = new Configuration();
            Locale locale;
            locale = new Locale(langue);
            config.locale = locale;
            getResources().updateConfiguration(config, null);

        } else {
            //Toast.makeText(getApplicationContext()," rien "+langue,Toast.LENGTH_LONG).show();
        }



        username.setText(leboutiqnam);
        phone.setText(leboutiqnum);
        maiil.setText(leboutiqmail);

           /* sauvergarde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = null;
                    String filePath = null;
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        file = new File(Environment.getExternalStorageDirectory(), "Weebi/Fichier");

                        filePath = file.getAbsolutePath() + File.separator + "MyDBWeebi.db";
                        shareGeneratedInvoice(filePath);

                    }
                }
            });*/

            //spinner.setPrompt("select language");

            String[] languages = {getString(R.string.choix_langue), "Anglais", "Français", "Amharic", "Wolof", "ضضض", "India"};

            String[] devises = {"Selection devise", "cfa"};


            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, languages);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    Configuration config = new Configuration();
                    Locale locale;
                    switch (position) {
                        case 0:

                            break;
                        case 1:
                            langue = "en";
                            locale = new Locale(langue);
                            config.locale = locale;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("preflangue", langue).apply();
                            //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                            break;
                        case 2:
                            langue = "fr";
                            locale = new Locale(langue);
                            config.locale = locale;
                            editor = sharedPreferences.edit();
                            editor.putString("preflangue", langue).apply();
                            //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                            break;
                        case 3:
                            langue = "am";
                            locale = new Locale(langue);
                            config.locale = locale;
                            editor = sharedPreferences.edit();
                            editor.putString("preflangue", langue).apply();
                            //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                            break;
                        case 4:
                            langue = "wo";
                            locale = new Locale(langue);
                            config.locale = locale;
                            editor = sharedPreferences.edit();
                            editor.putString("preflangue", langue).apply();
                            //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                            break;
                        case 5:
                            langue = "ar";
                            locale = new Locale(langue);
                            config.locale = locale;
                            editor = sharedPreferences.edit();
                            editor.putString("preflangue", langue).apply();
                            //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                            break;
                        case 6:
                            langue = "hi";
                            locale = new Locale(langue);
                            config.locale = locale;
                            editor = sharedPreferences.edit();
                            editor.putString("preflangue", langue).apply();
                            //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                            break;

                        default:
                            config.locale = Locale.ENGLISH;
                            break;
                    }
                    getResources().updateConfiguration(config, null);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

            final ImageView photo = findViewById(R.id.boutik_image);

            //ajout d'un nouveau boutiquier dans la base
            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FormEditText[] allFields = {username, phone, phone};
                    boolean allvalid = true;


                    for (FormEditText field : allFields) {
                        allvalid = field.testValidity() && allvalid;
                    }

                    if (allvalid) {
                        Config.WritetoBase(Parametres.this, null, Config.Boutik_url_photo, MODE_PRIVATE);
                        if (phone.getText().toString().equals(phone.getText().toString())) {


                             clientName = username.getText().toString();
                             clientPhone = phone.getText().toString();
                             clientmail = maiil.getText().toString();

                            final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                            if (clientmail.matches(emailPattern))
                            {
                                //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                                Cursor bouti=dbHelper.getBoutique("1");
                                if(bouti.moveToFirst()){
                            /*Toast.makeText(getApplicationContext(),""+bouti.getString(0)+
                                    "\n"+bouti.getString(1)+"\n"+bouti.getString(2),Toast.LENGTH_LONG).show();*/
                                    dbHelper.majBout(clientName,clientPhone,"1", clientmail);
                                    startActivity(new Intent(getApplicationContext(), NewInterface.class));
                                }

                                if(!bouti.moveToFirst()){
                                    //Toast.makeText(getApplicationContext(),"Veuillez configurer votre boutique",Toast.LENGTH_LONG).show();
                                    dbHelper.insertBoutique(clientName,clientPhone,clientmail,"0000");
                                    startActivity(new Intent(getApplicationContext(), NewInterface.class));
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Addresse  email invalid ", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                }

            });




    }

    //declaration des permissions que l'application à besoin pour que l'utilisateur valide les permissions
    private boolean checkAndRequestPermissions() {
        int permissionLecture = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLecture != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    /*Intent gotostart = new Intent(Parametres.this, NewInterface.class);
                    startActivity(gotostart);
                    finish();*/
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(getApplicationContext(),"Si vous refusiez, l'application ne fonctionnera pas",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void shareGeneratedInvoice(String filePath) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(new File(filePath));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("text/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Envoyer"));
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_parametres;
    }
}
