package com.weebinatidi.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.ArchiveInvoiceRepository;
import com.weebinatidi.model.ArchiveOperationClient;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.OperationClientRepository;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.weebinatidi.Config.formaterSolde;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Archives.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Archives#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Archives extends Fragment {
    public static final String EXTRA_CLIENT_PHONE = "CLIENT_PHONE";
    public static final String EXTRA_CLIENT_ARCHIVE = "CLIENT_ARCHIVE";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RealmResults<OperationClientRepository> results;
    RealmResults<ArchiveInvoiceRepository> rembourse;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView mDepotList;
    private TextView textView;
    private Realm realm;
    private String mPhone;
    private String archived;
    private DepositAdapter mAdapter;
    private ClientRepository thisClient;
    private ArchiveOperationClient archiveOperationClient;
    private boolean isInvoice = false;

    private OnFragmentInteractionListener mListener;

    public Archives() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Archives.
     */
    // TODO: Rename and change types and number of parameters
    public static Archives newInstance(String param1, String param2) {
        Archives fragment = new Archives();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mPhone = getActivity().getIntent().getStringExtra(EXTRA_CLIENT_PHONE);
        archived = getActivity().getIntent().getStringExtra(EXTRA_CLIENT_ARCHIVE);
        realm = Realm.getDefaultInstance();
        thisClient = realm.where(ClientRepository.class).equalTo("numero", mPhone).findFirst();
        rembourse = realm.where(ArchiveInvoiceRepository.class).findAll();
        //get operationclient

        RealmList<OperationClientRepository> op = thisClient.getOperationClients();
        RealmList<OperationClientRepository> ope = thisClient.getOperationClients();
        results = op.where().equalTo("isInvoice", isInvoice).findAll();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archives, container, false);
        // Inflate the layout for this fragment
        mDepotList = (ListView) view.findViewById(R.id.deposit_liste);
        //textView=(TextView)view.findViewById(R.id.solde);
        textView = (TextView) view.findViewById(R.id.text);
        textView.setText("" + thisClient.getSolde());
        //textView.setText(""+rembourse);
        mAdapter = new DepositAdapter(rembourse);
        mDepotList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class DepositAdapter extends BaseAdapter {

        private List<ArchiveInvoiceRepository> mOperationClients = null;

        public DepositAdapter(List<ArchiveInvoiceRepository> list) {
            mOperationClients = list;
        }

        @Override
        public int getCount() {
            if (mOperationClients != null)
                return mOperationClients.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return mOperationClients.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_depot, parent, false);
            }

            TextView depotType = (TextView) convertView.findViewById(R.id.deposit);
            //TextView depotDate = (TextView) convertView.findViewById(R.id.deposit_date);
            TextView depotMontant = (TextView) convertView.findViewById(R.id.deposit_amount);
            final int lid = mOperationClients.get(position).getId();

            RealmResults<InvoiceRepository> invoiceRepository = realm.where(InvoiceRepository.class).findAll();
            InvoiceRepository invoiceRepository1 = invoiceRepository.where().equalTo("id", lid).findFirst();


            int montant = mOperationClients.get(position).getTotal();


            String lemontant = formaterSolde(montant);
            depotMontant.setText(getString(R.string.montant_egale) + String.valueOf(lemontant));
            depotType.setText(getString(R.string.remboursement) + " " + Config.getFormattedDate(mOperationClients.get(position).getDate()));
            Config.getFormattedDate(mOperationClients.get(position).getDate());




            /*if(mOperationClients.get(position).getInvoiceRepository() != null)
            {
                if(mOperationClients.get(position).isInvoice() == false ){
                    /*convertView.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    depotType.setTextColor(Color.WHITE);
                    depotMontant.setTextColor(Color.WHITE);
                    depotType.setText(" "+mOperationClients.get(position).getInvoiceRepository().getItems().get(0).getName());
                    String lemontant=formaterSolde(montant);
                    depotMontant.setText(getString(R.string.montant_egale) + String.valueOf(lemontant));
                    depotType.setText(" "+mOperationClients.get(position).getInvoiceRepository().getItems().get(0).getName());
                             Config.getFormattedDate(mOperationClients.get(position).getDate());
                }
            }*/
//
            return convertView;
        }
    }
}
