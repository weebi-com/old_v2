package com.weebinatidi.ui.weebi2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weebinatidi.R;
import com.weebinatidi.model.LaReffb;

import java.util.List;

public class Purchaseref extends AppCompatActivity {
    ListView listView;
    public String urlpis = "https://www.weebi.com/piscoandroid/lecturedatajson.php";

    String purchase="";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchaseref);
        //purchase=getIntent().getStringExtra("lepurchase");
        textView=findViewById(R.id.text);
        //textView.setText(purchase);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        purchase=preferences.getString("purchaseref", null);

        textView.setText("Dernier Purchaseref : "+purchase);

    }

    public class AdapterPurchase extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;

        private List<LaReffb> references;
        private Context context;

        public AdapterPurchase(Activity activity, List<LaReffb> references){
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


            return convertView;


        }

    }
}
