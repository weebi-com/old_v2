package com.weebinatidi.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weebinatidi.R;
import com.weebinatidi.model.ClientRepository;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by mbp on 09/04/2017.
 */

public class AdapterClient extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ClientRepository> clientRepositories;
    private Context context;

    public AdapterClient(Activity activity, List<ClientRepository> clientRepositories) {
        this.activity = activity;
        this.clientRepositories = clientRepositories;
    }

    public AdapterClient(Activity activity, RealmResults<ClientRepository> clientRepositories) {
        this.activity = activity;
        this.clientRepositories = clientRepositories;
    }

    @Override
    public int getCount() {
        return clientRepositories.size();
    }

    @Override
    public Object getItem(int position) {
        return clientRepositories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.client_item_view2, null);


        TextView nom = (TextView) convertView.findViewById(R.id.client_name);
        TextView phone = (TextView) convertView.findViewById(R.id.client_phone);
        //TextView solde=(TextView)convertView.findViewById(R.id.client_soldee);

        ClientRepository clientRepository = clientRepositories.get(position);
        nom.setText(clientRepository.getNom());
        phone.setText(clientRepository.getNumero());
        //solde.setText(clientRepository.getSolde());
        return convertView;
    }
}
