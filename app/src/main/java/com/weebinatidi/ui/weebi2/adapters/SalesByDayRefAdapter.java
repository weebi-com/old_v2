package com.weebinatidi.ui.weebi2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weebinatidi.R;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.LaReffb;

import java.io.File;
import java.util.List;

/**
 * @author by Birante Sy
 */

public class SalesByDayRefAdapter extends ArrayAdapter<LaReffb> {

    private int itemQty;

    public SalesByDayRefAdapter(Context context, List<LaReffb> references , int quantity) {
        super(context, 0, references);
        itemQty = quantity;
    }


    public int calculateProgressVariation(int maxQuantity) {
        int progress = 0;

        if (maxQuantity < 100) {
            progress = 100;
        } else if (maxQuantity < 1000){
            progress = 1000;
        } else if (maxQuantity < 10000){
            progress = 10000;
        } else if (maxQuantity < 100000){
            progress = 100000;
        } else if (maxQuantity < 1000000){
            progress = 1000000;
        }
        return progress;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LaReffb reference = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reference, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tv_ref_name);
        TextView tvQuantity = convertView.findViewById(R.id.tv_ref_qtity);
        ImageView imageView = convertView.findViewById(R.id.iv_ref);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        tvName.setText(reference.getLaref());
        tvQuantity.setText(reference.getLarefqte());
        String Images = ".photos";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Weebi/" + Images + "/" + reference.getLaref() + ".png");
        Picasso.with(getContext()).load(myDir).resize(100, 100).centerCrop().into(imageView);

        progressBar.setMax(calculateProgressVariation(itemQty)); // Configure la progression Maximale
        progressBar.setProgress(whatIsTheTypeOfValue(reference.getLarefqte()));
        return convertView;
    }

    private int whatIsTheTypeOfValue(String number){
        int intNumber;
        boolean isNumberIsInteger = isInteger(number);

        if (isNumberIsInteger) { // Si c'est un nombre entier Ex : 2
            intNumber = Integer.parseInt(number);
        } else { // Sinon si c'est un nombre a virgule Ex : 2.5 => on recupere juste la valeur entiere
            Double doubleNumber = Double.valueOf(number);
            intNumber = doubleNumber.intValue();
        }
        return intNumber;
    }

    /**
     *  Verifie si la chaine de caractere retourner est de type entier.
     *  @param str
     *  @return isValidInteger
     */
    private static boolean isInteger(String str) {
        boolean isValidInteger = false;
        try {
            Integer.parseInt(str);
            isValidInteger =  true;
        } catch (NumberFormatException e) {
            isValidInteger = false;
        }
        return isValidInteger;
    }
}
