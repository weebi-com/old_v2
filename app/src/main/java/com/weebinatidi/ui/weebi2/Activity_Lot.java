package com.weebinatidi.ui.weebi2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.weebinatidi.R;
import com.weebinatidi.model.DbHelper;

public class Activity_Lot extends AppCompatActivity {

    private String nomreff;
    private String numlot;
    private String qtee;
    private String prix;
    private String total;
    private String date;

    private TextView tnomreff, tnumlot, tqtee, tprix, ttotal, tdate;
    private Button partage;
    private String texttosend="";

    DbHelper dbHelper;
    private String leboutiqnum;
    private String leboutiqnam;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__lot);
        dbHelper=new DbHelper(this);
        String  idrefe="";
        String  datesss="";


        Cursor cursor1=dbHelper.getREFSUIVLast();
        if(cursor1.moveToFirst()){
            numlot=cursor1.getString(0);
            date=cursor1.getString(1);
            prix=cursor1.getString(2);
            qtee=cursor1.getString(3);
        }
        cursor1.close();

        Cursor bouti=dbHelper.getBoutique("1");
        if(bouti.moveToFirst()){
            //Toast.makeText(getApplicationContext(),""+bouti.getString(0)
            //        +"\n"+bouti.getString(1)+"\n"+bouti.getString(2)+"\n"+bouti.getString(3),Toast.LENGTH_LONG).show();
            leboutiqnum=bouti.getString(1);
            leboutiqnam=bouti.getString(2);

        }


        Intent intent = getIntent();
        nomreff = intent.getStringExtra("nomref");
        //numlot = intent.getStringExtra("lid");
        //qtee = intent.getStringExtra("qt");
        //prix = intent.getStringExtra("prix");
        total = String.valueOf(Double.valueOf(qtee)*Double.valueOf(prix));
        //date = intent.getStringExtra("date");

        SharedPreferences sharedPrefs = this.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

        partage=findViewById(R.id.partagewhatsapp);

        tnomreff=findViewById(R.id.nomref);
        tnumlot=findViewById(R.id.numlot);
        tqtee=findViewById(R.id.qtelot);
        tprix=findViewById(R.id.prix);
        ttotal=findViewById(R.id.total);
        tdate=findViewById(R.id.datelot);

        tnomreff.setText(nomreff);
        tnumlot.setText(numlot);
        tqtee.setText(qtee);
        tprix.setText(prix);
        ttotal.setText(total);
        tdate.setText(date);

        partage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageManager pm = getApplicationContext().getPackageManager();
                isPackageInstalled("com.whatsapp",pm);
            }
        });
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            //Toast.makeText(getApplicationContext(),"installé",Toast.LENGTH_SHORT).show();
            texttosend+="De : "+leboutiqnam+"\n";
            texttosend+="Numero : "+leboutiqnum+"\n";
            texttosend+="ID : "+uniqueID+"\n";
            texttosend+="Lot n° : "+numlot+"\n";
            texttosend+="Date : "+date+"\n\n";
            texttosend+="Articles            | Qt | Prix | Valeur\n";
            texttosend+="------------------------------------------\n";
            texttosend+=nomreff+"  | "+qtee+" | "+prix+" | "+total+"\n";
            texttosend+="------------------------------------------\n";
            texttosend+="                        Total(CFA) : "+Double.valueOf(qtee)*Double.valueOf(prix)+"\n";
            /*texttosend+="Commander : \n";
            texttosend+="fiu.weebi.com/"+numlot+"-bdp@gmail.com-"+Double.valueOf(qtee)*Double.valueOf(prix)+"\n";*/
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, texttosend);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
            return true;

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplicationContext(),"installez l'application whatsapp d'abord merci",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
            return false;
        }
    }
}
