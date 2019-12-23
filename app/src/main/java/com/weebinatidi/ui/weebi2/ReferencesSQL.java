package com.weebinatidi.ui.weebi2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;
import com.weebinatidi.model.Client;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.LaReffb;
import com.weebinatidi.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;

public class ReferencesSQL extends BaseActivity {

    List<LaReffb> references;
    ArrayList<LaReffb> results=new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    AlertDialog.Builder RegisterRefbuilder;
    AlertDialog RegisterRefdialog;
    List<String> referencesserv=new ArrayList<>();
    private EditText recherref;

    AlertDialog.Builder MenuRefbuilder;
    AlertDialog MenuRefDialog;

    private Button addref, search, selectrefs, majrefsweebi, trialpha, trinumberid;
    private Button menu, retour, importref;
    private AdapterReference adapterReference;
    DbHelper dbHelper;
    LinearLayout linearLayout,linearclavier;
    String text="";

    //Button bleu,vert, jaune,rouge,blanc;


    Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,
             tiret8,espace,clear,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
       //OkHttpHandler okHttpHandler = new OkHttpHandler();
        //okHttpHandler.execute(urlpis);
        //retour = (Button) findViewById(R.id.retourref);
        addref = (Button) findViewById(R.id.addref);
        //linearLayout = (LinearLayout) findViewById(R.id.lineajout);
        linearclavier = (LinearLayout) findViewById(R.id.clavierref);
         search = (Button) findViewById(R.id.search_ref);
        //majrefsweebi = (Button) findViewById(R.id.selecttags);
        /*bleu=findViewById(R.id.bleu);
        vert=findViewById(R.id.vert);
        jaune=findViewById(R.id.jaune);
        rouge=findViewById(R.id.rouge);
        blanc=findViewById(R.id.blanc);*/
        trialpha=findViewById(R.id.sortingalpha);
        trinumberid=findViewById(R.id.sortingnumber);
        importref=findViewById(R.id.importsd);


        dbHelper=new DbHelper(this);

        references = new ArrayList<>();
        results=dbHelper.getAllReference();

        recherref=findViewById(R.id.champref);

        recherref.setInputType(0);

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



        //dbHelper.suppReference();
        listView = (ListView) findViewById(R.id.listerefsql);
        int taille=results.size();


        //arrayAdapter = new ArrayAdapter<String>(ReferencesSQL.this, android.R.layout.simple_list_item_1, references);


        //listetest=dbHelper.getAllRefName();
        for (LaReffb laRef : results){
            LaReffb laReffb=new LaReffb();
            laReffb.setLaref(laRef.getLaref());
            laReffb.setLarefprice(laRef.getLarefprice());
            laReffb.setLarefqte(laRef.getLarefqte());
            laReffb.setId(laRef.getId());
            laReffb.setDateen(laRef.getDateen());
            references.add(laReffb);
           // Log.d("resultats references", results.toString());
        }

        //ces deux lignes de codes doivent se trouver ici pour que la recherche marche
        adapterReference =new AdapterReference(this, references);
        listView.setAdapter(adapterReference);

        trinumberid.setVisibility(View.VISIBLE);

        trinumberid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                references.clear();
                results=dbHelper.getAllReferenceById();
                for (LaReffb laRef : results){
                    LaReffb laReffb=new LaReffb();
                    laReffb.setLaref(laRef.getLaref());
                    laReffb.setLarefprice(laRef.getLarefprice());
                    laReffb.setLarefqte(laRef.getLarefqte());
                    laReffb.setId(laRef.getId());
                    laReffb.setDateen(laRef.getDateen());
                    references.add(laReffb);
                    // Log.d("resultats references", results.toString());
                }
                adapterReference.notifyDataSetChanged();
                trinumberid.setVisibility(View.GONE);
                trialpha.setVisibility(View.VISIBLE);
            }
        });

        trialpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                references.clear();
                results=dbHelper.getAllReference();
                for (LaReffb laRef : results){
                    LaReffb laReffb=new LaReffb();
                    laReffb.setLaref(laRef.getLaref());
                    laReffb.setLarefprice(laRef.getLarefprice());
                    laReffb.setLarefqte(laRef.getLarefqte());
                    laReffb.setId(laRef.getId());
                    laReffb.setDateen(laRef.getDateen());
                    references.add(laReffb);
                    // Log.d("resultats references", results.toString());
                }
                adapterReference.notifyDataSetChanged();
                trialpha.setVisibility(View.GONE);
                trinumberid.setVisibility(View.VISIBLE);
            }
        });

        importref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getExternalSdCard();
                String Images = ".photos";
                //File test=Environment.getExternalStorageDirectory();
                //Toast.makeText(getApplicationContext(),""+test.getAbsolutePath(),Toast.LENGTH_SHORT).show();
            /*File from = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/123.jpg");
            File to = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Weebi/123.jpg");
            from.renameTo(to);
            Boolean environ=Environment.isExternalStorageRemovable();
            if(environ==true){
                Toast.makeText(getApplicationContext(),"carte mémoire montée "+environ,Toast.LENGTH_SHORT).show();

            }
            if(environ==false){
                Toast.makeText(getApplicationContext(),"pas de carte mémoire "+environ,Toast.LENGTH_SHORT).show();
            }*

                //isExternalStorageWritable();
                File file = null;
                File file1 = null;

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    file1 = new File(Environment.getExternalStorageDirectory(), "Weebi/Fichier");
                    if (file1.exists()) {
                        file = new File(file1.getAbsolutePath(), "fichier.txt");
                        if (!file.exists()) {
                            Toast.makeText(getApplicationContext(),"pas de fichier texte",Toast.LENGTH_SHORT).show();
                        }
                        if (file.exists()) {

                            Toast.makeText(getApplicationContext(),"fichier texte existe",Toast.LENGTH_SHORT).show();
                            dbHelper.suppAllReference();

                            String CSVseparator = ";";
                            String line = "";
                            BufferedReader br = null;
                            String categorie="";
                            try{
                                br = new BufferedReader(new FileReader(file1.getAbsolutePath()+File.separator+"fichier.txt"));
                                while((line = br.readLine()) != null){
                                    String[] params = line.split(CSVseparator);
                                    String nomref=params[0];
                                    String idkey=params[1];
                                    String prix=params[2];
                                    categorie=params[3].substring(0,params[3].length()-1);
                                    String ladate= Config.getFormattedDate(Calendar.getInstance().getTime());
                                    //Toast.makeText(getApplicationContext(),""+idkey,Toast.LENGTH_LONG).show();
                                    dbHelper.insertReferencedg(idkey,nomref,prix, "0",idkey+".png",ladate,categorie,"2");
                                /*File from = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Weebi/photosreference/"+idkey+".png");
                                File to = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Weebi/images/" + nomref + ".png");
                                from.renameTo(to);*
                                    adapterReference.notifyDataSetChanged();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (br != null)
                                        br.close();

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }*/
            }
        });

       /* bleu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                references.clear();
                results=dbHelper.getAllReferenceCat("bleu");
                for (LaReffb laRef : results){
                    LaReffb laReffb=new LaReffb();
                    laReffb.setLaref(laRef.getLaref());
                    laReffb.setLarefprice(laRef.getLarefprice());
                    laReffb.setLarefqte(laRef.getLarefqte());
                    laReffb.setId(laRef.getId());
                    laReffb.setDateen(laRef.getDateen());
                    references.add(laReffb);
                    // Log.d("resultats references", results.toString());
                }
                adapterReference.notifyDataSetChanged();
            }
        });
        vert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                references.clear();
                results=dbHelper.getAllReferenceCat("vert");
                for (LaReffb laRef : results){
                    LaReffb laReffb=new LaReffb();
                    laReffb.setLaref(laRef.getLaref());
                    laReffb.setLarefprice(laRef.getLarefprice());
                    laReffb.setLarefqte(laRef.getLarefqte());
                    laReffb.setId(laRef.getId());
                    laReffb.setDateen(laRef.getDateen());
                    references.add(laReffb);
                    // Log.d("resultats references", results.toString());
                }
                adapterReference.notifyDataSetChanged();
            }
        });

        jaune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                references.clear();
                results=dbHelper.getAllReferenceCat("jaune");
                for (LaReffb laRef : results){
                    LaReffb laReffb=new LaReffb();
                    laReffb.setLaref(laRef.getLaref());
                    laReffb.setLarefprice(laRef.getLarefprice());
                    laReffb.setLarefqte(laRef.getLarefqte());
                    laReffb.setId(laRef.getId());
                    laReffb.setDateen(laRef.getDateen());
                    references.add(laReffb);
                    // Log.d("resultats references", results.toString());
                }
                adapterReference.notifyDataSetChanged();
            }
        });

        rouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                references.clear();
                results=dbHelper.getAllReferenceCat("rouge");
                for (LaReffb laRef : results){
                    LaReffb laReffb=new LaReffb();
                    laReffb.setLaref(laRef.getLaref());
                    laReffb.setLarefprice(laRef.getLarefprice());
                    laReffb.setLarefqte(laRef.getLarefqte());
                    laReffb.setId(laRef.getId());
                    laReffb.setDateen(laRef.getDateen());
                    references.add(laReffb);
                    // Log.d("resultats references", results.toString());
                }
                adapterReference.notifyDataSetChanged();
            }
        });
        blanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                references.clear();
                results=dbHelper.getAllReferenceCat("blanc");
                for (LaReffb laRef : results){
                    LaReffb laReffb=new LaReffb();
                    laReffb.setLaref(laRef.getLaref());
                    laReffb.setLarefprice(laRef.getLarefprice());
                    laReffb.setLarefqte(laRef.getLarefqte());
                    laReffb.setId(laRef.getId());
                    laReffb.setDateen(laRef.getDateen());
                    references.add(laReffb);
                    // Log.d("resultats references", results.toString());
                }
                adapterReference.notifyDataSetChanged();
            }
        });*/

        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
        /*if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearclavier.setVisibility(View.VISIBLE);
            }
        });

        recherref.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                // NewInterface.this.adapterReference.filter(cs);
                text = recherref.getText().toString().toLowerCase(Locale.getDefault());
                final String codePattern = "[0-9]";
                if(text.equals("")){
                    addref.setVisibility(View.INVISIBLE);
                }

                adapterReference.filter(text);

                if(adapterReference.isEmpty()){
                    try{
                        int i = Integer.parseInt(text);
                        addref.setVisibility(View.INVISIBLE);
                    }catch(Exception e){
                        //Toast.makeText(getApplicationContext(), "pas bon", Toast.LENGTH_SHORT).show();
                        addref.setVisibility(View.VISIBLE);
                    }
                }

                if(!adapterReference.isEmpty()){
                    addref.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                text = recherref.getText().toString().toLowerCase(Locale.getDefault());
                adapterReference.filter(text);

                if(adapterReference.isEmpty()){
                    //Toast.makeText(getApplicationContext(), "vide", Toast.LENGTH_SHORT).show();
                    addref.setVisibility(View.VISIBLE);
                    //linearLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                 text = recherref.getText().toString().toLowerCase(Locale.getDefault());
                final String codePattern = "[0-9]";
                adapterReference.filter(text);

                if(text.equals("")){
                    addref.setVisibility(View.INVISIBLE);
                }


                if(adapterReference.isEmpty()){
                    //Toast.makeText(getApplicationContext(), "vide", Toast.LENGTH_SHORT).show();
                    try{
                        int i = Integer.parseInt(text);
                        addref.setVisibility(View.INVISIBLE);
                    }catch(Exception e){
                        //Toast.makeText(getApplicationContext(), "pas bon", Toast.LENGTH_SHORT).show();
                        addref.setVisibility(View.VISIBLE);
                    }

                   // linearLayout.setVisibility(View.VISIBLE);
                }
                if(!adapterReference.isEmpty()){
                    addref.setVisibility(View.INVISIBLE);
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=recherref.getText().toString();
                if (chaine.equals("")){

                }else {
                    chaine = chaine.substring(0, chaine.length()-1);
                    recherref.setText(""+chaine);
                }

            }
        });

        espace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append(" ");
            }
        });
        tiret8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("_");
            }
        });


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("A");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("B");
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("C");
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("D");
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("E");
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("F");
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("G");
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("H");
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("I");
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("J");
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("K");
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("L");
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("M");
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("N");
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("O");
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("P");
            }
        });
        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("Q");
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("R");
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("S");
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("T");
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("U");
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("V");
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("W");
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("X");
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("Y");
            }
        });
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recherref.append("Z");
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+0);
                }
                else {
                    recherref.append(""+0);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        un.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+1);
                }
                else {
                    recherref.append(""+1);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        deux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+2);
                }
                else {
                    recherref.append(""+2);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        trois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+3);
                }
                else {
                    recherref.append(""+3);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        quatre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+4);
                }
                else {
                    recherref.append(""+4);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        cinq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+5);
                }
                else {
                    recherref.append(""+5);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+6);
                }
                else {
                    recherref.append(""+6);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        sept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+7);
                }
                else {
                    recherref.append(""+7);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        huit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+8);
                }
                else {
                    recherref.append(""+8);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        neuf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=recherref.getText().toString();

                if(chaine.contains("")){
                    recherref.append(""+9);
                }
                else {
                    recherref.append(""+9);
                }

            }
        });


        addref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onAddRef();
                Intent intent=new Intent(getApplicationContext(),AjouterRefSQL.class);
                intent.putExtra("laref",text);
                startActivity(intent);
                finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String Adr_Slave = ((TextView) view.findViewById(R.id.row_txt_Adresse)).getText().toString();
                LaReffb laReffb = (LaReffb)parent.getAdapter().getItem(position);
                String lid=laReffb.getId();

                //si l'orientation est en paysage on va chercher ClientPageActivityNew2SQL sinon on va dans le Clientpageportrait
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    //String lenum=((TextView)view.findViewById(R.id.client_phone)).getText().toString();

                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ReferencesSQL.this, GestRefSql.class);
                    intent.putExtra(GestRefSql.EXTRA_REF_ID, lid);
                    //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }


                /*String lid = ((TextView)view.findViewById(R.id.idref)).toString();
                laReffb.setId(lid);
                LaReffb laReffb=(LaReffb) parent.getItemAtPosition(position);
                String lid=laReffb.getId();*/
                //Toast.makeText(getApplicationContext(),"clic "+lid,Toast.LENGTH_LONG).show();
               // updateref(lid);
            }
        });



    }



    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_references_sql;
    }

    public class AdapterReference extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;

        private List<LaReffb> references;
        private ArrayList<LaReffb> arrayliste;
        private Context context;

        public AdapterReference(Activity activity, List<LaReffb> references){
            this.activity=activity;
            this.references=references;
            this.arrayliste = new ArrayList<>();
            this.arrayliste.addAll(references);
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
                convertView = inflater.inflate(R.layout.inventaire, null);


            TextView nomref=convertView.findViewById(R.id.refname);
            TextView prixref=(TextView)convertView.findViewById(R.id.prixref);
            TextView qteref=(TextView)convertView.findViewById(R.id.qteref);
            TextView idref=(TextView)convertView.findViewById(R.id.idref);
            TextView datee=(TextView)convertView.findViewById(R.id.datee);
            ImageView imageView=(ImageView)convertView.findViewById(R.id.imagee);


            LaReffb laReffb=references.get(position);
            nomref.setText(laReffb.getLaref());
            prixref.setText(laReffb.getLarefprice());
            qteref.setText(Config.formaterQte(laReffb.getLarefqte()));
            idref.setText(laReffb.getId());
            String ladate=laReffb.getDateen();
            datee.setText(ladate);
            /*if(!ladate.equals("")){
                 String dateentier[]=ladate.split("a");
            if(!ladate.equals("")){
                String dateentier[]=ladate.split("a");
                String dateun=dateentier[0];
                datee.setText(dateun);
            }


            /*if(laReffb.getLienimg().equals("")){
                // Resources res = getResources();
                //Drawable drawable = res.getDrawable(R.drawable.tag_multiple);
                Picasso.with(getBaseContext()).load(R.drawable.tags)
                        .resize(100,100)
                        .into(imageView);
            }
            else {*/
           // String Images=".Images";
                String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Weebi/Images/" + laReffb.getId() + ".png");
            //File myDir = new File(root + "/Weebi/images/" + laReffb.getLaref() + ".png");

            Picasso.with(getBaseContext()).load(myDir).resize(100,100).centerCrop().into(imageView);
            //}
            return convertView;


        }

        public void filter(String cs) {
            cs = cs.toLowerCase(Locale.getDefault());
            references.clear();
            if (cs.length() == 0) {
                references.addAll(arrayliste);
            } else {
                for (LaReffb wp : arrayliste) {
                    if (wp.getLaref().toLowerCase(Locale.getDefault())
                            .startsWith(cs)) {
                        references.add(wp);
                    }
                    if (wp.getId().toLowerCase(Locale.getDefault())
                            .startsWith(cs)) {
                        references.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /*public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), NewInterface.class));
        finish();
    }*/

}
