package com.weebinatidi.ui.weebi2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.ui.BaseActivity;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import static android.view.View.VISIBLE;

public class AjouterRefSQL extends BaseActivity {

    DbHelper dbHelper;
    private String nomreference = "";
    private String pricereference = "";
    private String qtereference = "";
    private String tvareference = "";
    private String refreduction="";
    private String text="";
    private String couleur="";

    AlertDialog.Builder Okbuilder;
    AlertDialog Okdialog;


    private EditText nomref, prixref, qteref, tvaref, refreduc;
    private ImageView imageView;
    Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,vi,w,x,y,z,
            espace,clear,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero, clearnum,
            bleu,vert, jaune,rouge,blanc, tiret8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nomref=findViewById(R.id.refname);
        prixref=findViewById(R.id.refprice);
        qteref=findViewById(R.id.refqte);
        tvaref=findViewById(R.id.reftva);
        imageView=findViewById(R.id.okb);
       // refreduc=findViewById(R.id.refreduc);

        text=getIntent().getStringExtra("laref");
        nomref.setText(text.toUpperCase());

        dbHelper=new DbHelper(this);

        final LinearLayout lettre=findViewById(R.id.clavl);
        LinearLayout numeriq=findViewById(R.id.clavn);

        bleu=findViewById(R.id.bleu);
        vert=findViewById(R.id.vert);
        jaune=findViewById(R.id.jaune);
        rouge=findViewById(R.id.rouge);
        blanc=findViewById(R.id.blanc);

        bleu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur="bleu";
                Toast.makeText(getApplicationContext(),""+couleur,Toast.LENGTH_SHORT).show();
            }
        });

        vert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur="vert";
                Toast.makeText(getApplicationContext(),""+couleur,Toast.LENGTH_SHORT).show();
            }
        });

        jaune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur="jaune";
                Toast.makeText(getApplicationContext(),""+couleur,Toast.LENGTH_SHORT).show();
            }
        });
        rouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur="rouge";
                Toast.makeText(getApplicationContext(),""+couleur,Toast.LENGTH_SHORT).show();
            }
        });
        blanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur="blanc";
                Toast.makeText(getApplicationContext(),""+couleur,Toast.LENGTH_SHORT).show();
            }
        });

        a=(Button)findViewById(R.id.a); b=(Button)findViewById(R.id.b);c=(Button)findViewById(R.id.c);
        d=(Button)findViewById(R.id.d); e=(Button)findViewById(R.id.e); f=(Button)findViewById(R.id.f);
        g=(Button)findViewById(R.id.g); h=(Button)findViewById(R.id.h); i=(Button)findViewById(R.id.i);
        j=(Button)findViewById(R.id.j); k=(Button)findViewById(R.id.k); l=(Button)findViewById(R.id.l);
        m=(Button)findViewById(R.id.m); n=(Button)findViewById(R.id.n); o=(Button)findViewById(R.id.o);
        p=(Button)findViewById(R.id.p); r=(Button)findViewById(R.id.r); s=(Button)findViewById(R.id.s);
        t=(Button)findViewById(R.id.t); u=(Button)findViewById(R.id.u); vi=(Button)findViewById(R.id.v);
        w=(Button)findViewById(R.id.w); x=(Button)findViewById(R.id.x); y=(Button)findViewById(R.id.y);
        q=(Button)findViewById(R.id.q); z=(Button)findViewById(R.id.z);tiret8=(Button)findViewById(R.id.tiret8);
        un=(Button)findViewById(R.id.btnNum1Id);
        deux=(Button)findViewById(R.id.btnNum2Id);
        trois=(Button)findViewById(R.id.btnNum3Id);
        quatre=(Button)findViewById(R.id.btnNum4Id);
        cinq=(Button)findViewById(R.id.btnNum5Id);
        six=(Button)findViewById(R.id.btnNum6Id);
        sept=(Button)findViewById(R.id.btnNum7Id);
        huit=(Button)findViewById(R.id.btnNum8Id);
        neuf=(Button)findViewById(R.id.btnNum9Id);
        clearnum=(Button)findViewById(R.id.btnClearId);
        //doublezero=(Button)findViewById(R.id.btn00);
        zero=(Button)findViewById(R.id.btnNum0Id);
        clear=(Button)findViewById(R.id.btnClearAl);
        espace=(Button)findViewById(R.id.espaced);

        nomref.setInputType(0);
        prixref.setInputType(0);
        qteref.setInputType(0);
        //tvaref.setInputType(0);
       // refreduc.setInputType(0);

        nomref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(VISIBLE);

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            nomref.setText(""+chaine);
                        }

                    }
                });
                espace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append(" ");
                    }
                });

                a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("A");
                    }
                });

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("B");
                    }
                });

                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("C");
                    }
                });

                d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("D");
                    }
                });

                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("E");
                    }
                });

                f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("F");
                    }
                });

                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("G");
                    }
                });

                h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("H");
                    }
                });

                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("I");
                    }
                });
                j.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("J");
                    }
                });
                k.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("K");
                    }
                });
                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("L");
                    }
                });
                m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("M");
                    }
                });
                n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("N");
                    }
                });
                o.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("O");
                    }
                });
                p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("P");
                    }
                });
                q.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("Q");
                    }
                });
                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("R");
                    }
                });
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("S");
                    }
                });
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("T");
                    }
                });
                u.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("U");
                    }
                });
                vi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("V");
                    }
                });
                w.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("W");
                    }
                });
                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("X");
                    }
                });
                y.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("Y");
                    }
                });
                z.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("Z");
                    }
                });
                tiret8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nomref.append("_");
                    }
                });


                zero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+0);
                        }
                        else {
                            nomref.append(""+0);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                un.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+1);
                        }
                        else {
                            nomref.append(""+1);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                deux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+2);
                        }
                        else {
                            nomref.append(""+2);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                trois.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+3);
                        }
                        else {
                            nomref.append(""+3);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                quatre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+4);
                        }
                        else {
                            nomref.append(""+4);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                cinq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+5);
                        }
                        else {
                            nomref.append(""+5);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+6);
                        }
                        else {
                            nomref.append(""+6);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                sept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+7);
                        }
                        else {
                            nomref.append(""+7);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                huit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+8);
                        }
                        else {
                            nomref.append(""+8);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                neuf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=nomref.getText().toString();

                        if(chaine.contains("")){
                            nomref.append(""+9);
                        }
                        else {
                            nomref.append(""+9);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                clearnum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=nomref.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            nomref.setText(""+chaine);
                        }

                    }
                });
            }
        });

        prixref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.INVISIBLE);
                zero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+0);
                        }
                        else {
                            prixref.append(""+0);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                un.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+1);
                        }
                        else {
                            prixref.append(""+1);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                deux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+2);
                        }
                        else {
                            prixref.append(""+2);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                trois.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+3);
                        }
                        else {
                            prixref.append(""+3);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                quatre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+4);
                        }
                        else {
                            prixref.append(""+4);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                cinq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+5);
                        }
                        else {
                            prixref.append(""+5);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+6);
                        }
                        else {
                            prixref.append(""+6);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                sept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+7);
                        }
                        else {
                            prixref.append(""+7);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                huit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+8);
                        }
                        else {
                            prixref.append(""+8);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                neuf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=prixref.getText().toString();

                        if(chaine.contains("")){
                            prixref.append(""+9);
                        }
                        else {
                            prixref.append(""+9);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                clearnum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=prixref.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            prixref.setText(""+chaine);
                        }

                    }
                });
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
                espace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                j.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                k.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                o.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                q.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                u.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                vi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                w.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                y.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                z.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

        qteref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.INVISIBLE);
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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


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

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                clearnum.setOnClickListener(new View.OnClickListener() {
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
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
                espace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                j.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                k.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                o.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                q.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                u.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                vi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                w.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                y.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                z.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });







        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomreference = nomref.getText().toString();
                pricereference = prixref.getText().toString();
                qtereference = qteref.getText().toString();
               // tvareference = tvaref.getText().toString();
               // refreduction = refreduc.getText().toString();
                 if(nomreference.equals("") || pricereference.equals("")){
                    Toast.makeText(getApplicationContext(),"Veuillez remplir les deux premiers champs merci",Toast.LENGTH_SHORT).show();
                }
                else {
                     if(qtereference.equals("") ){
                         qtereference="0";
                     }
                     /*if(tvareference.equals("")){
                         tvareference="0";
                     }*/

                     Okbuilder = new AlertDialog.Builder(AjouterRefSQL.this);
                     View customView = getLayoutInflater().inflate(R.layout.dialog_ok_ajoutref, null, false);

                     final TextView reference = (TextView) customView.findViewById(R.id.lenom);
                     final TextView prix = (TextView) customView.findViewById(R.id.leprix);
                     final TextView stock = (TextView) customView.findViewById(R.id.lestock);
                     //final TextView tva = (TextView) customView.findViewById(R.id.latva);
                     final TextView prixvente = (TextView) customView.findViewById(R.id.prixdevente);
                     final Button coul = (Button) customView.findViewById(R.id.lacouleur);
                    // final TextView prixred = (TextView) customView.findViewById(R.id.lared);
                     //final EditText recu = (EditText) customView.findViewById(R.id.recu);
                     final ImageView ok = (ImageView) customView.findViewById(R.id.valide);
                     final ImageView annule = (ImageView) customView.findViewById(R.id.annule);

                     if(couleur.equals("bleu")){
                         coul.setBackgroundColor(Color.BLUE);
                     }
                     if(couleur.equals("vert")){
                         coul.setBackgroundColor(Color.GREEN);
                     }
                     if(couleur.equals("jaune")){
                         coul.setBackgroundColor(Color.YELLOW);
                     }
                     if(couleur.equals("rouge")){
                         coul.setBackgroundColor(Color.RED);
                     }
                     if(couleur.equals("blanc")){
                         coul.setBackgroundColor(Color.WHITE);
                     }
                     if(couleur.equals("")){
                         coul.setBackgroundColor(Color.BLACK);
                     }

                     reference.setText(nomreference);
                     prix.setText(pricereference);
                     stock.setText(qtereference);
                     //tva.setText(tvareference);
                     //prixred.setText(refreduction);

                     //int tvar=0;
                     int tred=0;
                     int montred=0;
                     //int monttva=0;
                     int montapresreduc=0;

                        /* if(tva.getText().toString()!="0"){
                             tvar=Integer.valueOf(tva.getText().toString());
                             //Toast.makeText(getApplicationContext(),"la tva"+ tvar,Toast.LENGTH_SHORT).show();
                             int pricess=Integer.valueOf(prix.getText().toString());
                             monttva=(pricess*tvar)/100;
                             //Toast.makeText(getApplicationContext(),"la tva "+ monttva,Toast.LENGTH_SHORT).show();
                             montapresreduc=pricess+monttva;
                             //Toast.makeText(getApplicationContext(),"prix aprés tva "+ montapresreduc,Toast.LENGTH_SHORT).show();
                             prixvente.setText(""+montapresreduc);
                         }*/

                             prixvente.setText(""+prix.getText().toString());




                     ok.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             String nom=reference.getText().toString();
                             String prixe=prixvente.getText().toString();
                             String qte=stock.getText().toString();
                            // Toast.makeText(getApplicationContext(),"les montants\n"+ nom+"\n" + prixe+"\n"+ qte+"\n",Toast.LENGTH_SHORT).show();
                             String ladate= Config.getFormattedDate(Calendar.getInstance().getTime());
                             dbHelper.insertReference(nom,prixe,qte,"",ladate,couleur,"1");
                             //dbHelper.insertSuivRQ(ladate,qteref,idref," $ Entree",prixe);
                             Okdialog.dismiss();

                             Cursor cursor=dbHelper.getLastRef();
                             if(cursor.moveToFirst()){
                                 String lid=cursor.getString(0);
                                // dbHelper.insertSuivRTVA(ladate,lid,"$ Entree",prixe,"");
                                 Toast.makeText(getApplicationContext(),"les montants\n"+ cursor.getString(0)+"\n"
                                         + cursor.getString(1)+"\n"+ cursor.getString(7)+"\n",Toast.LENGTH_SHORT).show();
                                 dbHelper.insertSuivRQ(ladate,qte,lid," $ Entree",prixe);
                             }
                             Intent intent=new Intent(getApplicationContext(),ReferencesSQL.class);
                             startActivity(intent);
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
                     Okbuilder.setTitle("Confirmer ajout référence");


                     Okdialog = Okbuilder.create();

                     Okdialog.show();
                }



            }


        });


    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_ajoutrefsql;
    }
}
