package com.weebinatidi.ui.weebi2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.LaReffb;
import com.weebinatidi.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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

public class ReferencesSQLGrossiste extends BaseActivity {

    private ProgressBar progressBar;
    List<LaReffb> references;
    ArrayList<LaReffb> results=new ArrayList<>();
    ArrayList<LaReffb> refweebi=new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    AlertDialog.Builder RegisterRefbuilder;
    AlertDialog RegisterRefdialog;
    List<LaReffb> referencesserv=new ArrayList<>();
    ArrayList<String> choisies=new ArrayList<>();

    AlertDialog.Builder MenuRefbuilder;
    AlertDialog MenuRefDialog;

    private Button valider, selectrefs;
    private Button menu, retour;
    private AdapterReference adapterReference;
    public String urlpis = "https://www.weebi.com/piscoandroid/lecturedatajson.php";
    public String urlboul="http://www.weebi.com/piscoandroid/jsonboulangeries.php";
    public String urlbout="http://www.weebi.com/piscoandroid/jsonboutiques.php";
    public String urlpizza="";
    public String urlpoise="http://www.weebi.com/piscoandroid/jsonpoisson.php";
    public String urlbois="http://www.weebi.com/piscoandroid/jsonboisson.php";
    public String urlfruits="http://www.weebi.com/piscoandroid/jsonfruitsetlegumes.php";
    public String urlpharma="http://www.weebi.com/piscoandroid/jsonpharmacie.php";

    DbHelper dbHelper;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String Images=".photos";
    String urlchoisi;

    private ProgressDialog progress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

        File file = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), "Weebi/"+Images);
            if (!file.exists()) {
                file.mkdirs();
            }

        }

       // progressBar = (ProgressBar) findViewById(R.id.progressBar);
        valider=findViewById(R.id.validere);
       // addref = (Button) findViewById(R.id.maj);
        //selectrefs = (Button) findViewById(R.id.selecte);
       // majrefsweebi = (Button) findViewById(R.id.selecttags);
        Spinner spinner = (Spinner) findViewById(R.id.choixrefs);
        String[] categories = {"Choix categories", "Boutiques", "Boulangerie", "Poissonnerie","Boissons","Fruits et Legumes","Pharmacies"/*,"Pizzeria"*/};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        dbHelper=new DbHelper(this);
        references = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listerefsql);
        adapterReference =new AdapterReference(this, references);
        listView.setAdapter(adapterReference);

        dbHelper.suppReferenceW();
        results=dbHelper.getAllReferenceW();


       /* if(results.size()>0){
           // Toast.makeText(getApplicationContext(),"base non vide", Toast.LENGTH_LONG).show();
            for (LaReffb laRef : results){
                LaReffb laReffb=new LaReffb();
                laReffb.setLaref(laRef.getLaref());
                laReffb.setLienimg(laRef.getLienimg());
                //picassoImageTarget(getApplicationContext(),imagedir,laRef.getLienimg());
                //imageDownload(getApplicationContext(),laRef.getLienimg());
               // getDownloadimg(laRef.getLienimg());
                references.add(laReffb);
                Log.d("resultats references", results.toString());
            }

            adapterReference.notifyDataSetChanged();
        }*/

       // if(results.size()<=0){
           // Toast.makeText(getApplicationContext(),"table vide", Toast.LENGTH_LONG).show();

        //}

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Configuration config = new Configuration();
                Locale locale;
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        urlchoisi=urlbout;

                        break;
                    case 2:

                        urlchoisi=urlboul;

                        break;

                    case 3:

                        urlchoisi=urlpoise;

                        break;

                    case 4:

                        urlchoisi=urlbois;

                        break;
                    case 5:

                        urlchoisi=urlfruits;

                        break;
                    case 6:

                        urlchoisi=urlpharma;

                        break;
                    /*case 7:

                        urlchoisi=urlpizza;

                        break;*/
                    default:
                        break;
                }
                getResources().updateConfiguration(config, null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),""+urlchoisi,Toast.LENGTH_SHORT).show();
                references.clear();
                adapterReference.notifyDataSetChanged();
                String  tag_string_req1 = "string_req";
                StringRequest strReq1 = new StringRequest(com.android.volley.Request.Method.GET,
                        urlchoisi, new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Log.d("", response.toString());
                        //pDialog.hide();

                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject explrObject = jsonArray.getJSONObject(i);
                                String larefes=explrObject.getString("nomrefg");
                                String lien=explrObject.getString("lienimg");
                                String prix=explrObject.getString("prix");
                                String key=explrObject.getString("idkey");
                                LaReffb laReffb=new LaReffb();
                                laReffb.setLaref(larefes);
                                laReffb.setLienimg(lien);
                                laReffb.setLarefprice(prix);
                                laReffb.setIdbdext(key);
                                //referencesserv.add(laReffb);
                                // imageDownload(getApplicationContext(),lien);
                                //picassoImageTarget(getApplicationContext(),imagedir,lien);
                                //dbHelper.insertReferenceW(larefes,lien,prix,key);
                                String ladate= Config.getFormattedDate(Calendar.getInstance().getTime());
                                dbHelper.insertReferenceW(larefes,lien,prix,key);
                                dbHelper.insertReferencedg(key,larefes,prix, "0","/Weebi/"+Images+"/"+larefes+".png",ladate,"","2");
                                references.add(laReffb);
                                // getDownloadimg(lien);
                                Log.d("resultats references", lien);
                            }

                            adapterReference.notifyDataSetChanged();
                           // download();
                            Toast.makeText(getApplicationContext(),"Veuillez attendre le téléchargement de toutes les photos merci",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.hide();
                        Toast.makeText(getApplicationContext(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
// Adding request to request queue
                WeebiApplication.getInstance().addToRequestQueue(strReq1, tag_string_req1);

            }
        });
        /*selectrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Ajout des références choisies", Toast.LENGTH_SHORT).show();
                for (int i=0; i<choisies.size(); i++){
                    String uneref=choisies.get(i).toString();
                        Cursor cursor=dbHelper.getReferenceW(uneref);
                                       //dbHelper.insertReference(uneref,"", "",);
                    String ladate= Config.getFormattedDate(Calendar.getInstance().getTime());
                    if(cursor.moveToFirst()){

                        String idre=cursor.getString(4);
                        String price=cursor.getString(3);
                        dbHelper.insertReferencedg(idre,uneref,price, "0","/Weebi/"+Images+"/"+uneref+".png",ladate);
                    }
                }
                Intent intent = new Intent(getApplicationContext(),ReferencesSQL.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    public void download(){
        progress=new ProgressDialog(this);
        progress.setMessage("Downloading Images");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 5;
                        progress.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }



    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_references_sql_grossiste;
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
                convertView = inflater.inflate(R.layout.client_item_view_reference_grossiste, null);


            final TextView nomref=(TextView)convertView.findViewById(R.id.refname);
           // final CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.lecheck);

            ImageView imageView=(ImageView)convertView.findViewById(R.id.image);

            final LaReffb laReffb=references.get(position);
            nomref.setText(laReffb.getLaref());
            laReffb.getLarefprice();laReffb.getIdbdext();


            /*checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lenom=nomref.getText().toString();
                    if(checkBox.isChecked()){
                        choisies.add(laReffb.getLaref());
                        //refweebi.add(laReffb);
                        //Toast.makeText(getApplicationContext(),""+choisies, Toast.LENGTH_SHORT).show();
                    }
                    else if (!checkBox.isChecked()) {
// cette méthode me permet de supprimer la reference si on déccoche le checkbox
                        Iterator<String> iterator = choisies.iterator();
                        while (iterator.hasNext()) {
                            String o = iterator.next();
                            if (o == lenom) {
                                // On supprime l'élément courant de la liste
                                iterator.remove();
                                //Toast.makeText(getApplicationContext(),""+choisies, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });*/

            if(laReffb.getLienimg().equals("")){
               // Resources res = getResources();
                //Drawable drawable = res.getDrawable(R.drawable.tag_multiple);
                Picasso.with(getBaseContext()).load(R.drawable.tags)
                        .resize(100,100).centerCrop()
                        .into(imageView);
            }
            else {
                Picasso.with(getBaseContext()).load(laReffb.getLienimg())
                        .resize(100,100).centerCrop()
                        .into(imageView);
               getDownloadimg1(laReffb.getLienimg(),laReffb.getLaref());
            }

            return convertView;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    public void getDownloadimg1(String url, final String nom) {
        Picasso.with(getBaseContext())
                .load(url)
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();

                                  File myDir = new File(root + "/Weebi/"+Images);

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }

                                  String name = nom + ".png";
                                  myDir = new File(myDir, name);
                                  FileOutputStream out = new FileOutputStream(myDir);
                                  bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

                                  out.flush();
                                  out.close();
                              } catch(Exception e){
                                  // some action
                              }
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );
    }

    /**
     *
     * Méthode pour afficher une photo depuis la mémoire du téléphone
     *      String root = Environment.getExternalStorageDirectory().toString();
    File myDir = new File(root + "/Weebi/photos/"+laReffb.getLaref()+".png");
    Picasso.with(getBaseContext()).load(myDir).resize(50,50).into(imageView);
     */

    /*public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), NewInterface.class));
        finish();
    }*/

}
