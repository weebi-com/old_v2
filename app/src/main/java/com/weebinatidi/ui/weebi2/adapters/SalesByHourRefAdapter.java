package com.weebinatidi.ui.weebi2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weebinatidi.R;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.LaReffb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */

public class SalesByHourRefAdapter extends ArrayAdapter<LaReffb> {


    DbHelper dbHelper;
    String refName;
    String refImage;
    String refQte;


    public SalesByHourRefAdapter(Context context, List<LaReffb> references) {
        super(context, 0,references);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        LaReffb reference = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reference, parent, false);
        }


        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tv_ref_name);
        TextView tvQuantity = convertView.findViewById(R.id.tv_ref_qtity);
        ImageView imageView = convertView.findViewById(R.id.iv_ref);

        dbHelper=new DbHelper(getContext());



        //Recuperons l'id de la reference

        String idRef = reference.getLaref();
        Cursor cur=dbHelper.getREF(Integer.valueOf(idRef));

        if(cur.moveToFirst()==true){
           refName = cur.getString(1);
           refImage = cur.getColumnName(5);
        }


        // Populate the data into the template view using the data object
        tvName.setText(refName);
        tvQuantity.setText(reference.getLarefqte());
        String Images = ".photos";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Weebi/" + Images + "/" + refName + ".png");
        Picasso.with(getContext()).load(myDir).resize(100, 100).centerCrop().into(imageView);




        // Return the completed view to render on screen
        return convertView;
    }

}
