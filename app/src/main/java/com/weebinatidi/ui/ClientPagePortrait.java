package com.weebinatidi.ui;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import com.weebinatidi.R;
import com.weebinatidi.model.ClientRepository;

import io.realm.Realm;

import static com.weebinatidi.ui.Calculator.isSmart;
import static com.weebinatidi.ui.Calculator.isSmartsmall;
import static com.weebinatidi.ui.Calculator.isTablet;
import static com.weebinatidi.ui.Calculator.isTablet7;

public class ClientPagePortrait extends BaseActivity implements Facturesportrait.OnFragmentInteractionListener, Depots.OnFragmentInteractionListener {

    public static final String EXTRA_CLIENT_PHONE = "CLIENT_PHONE";
    public static final String EXTRA_CLIENT_ARCHIVE = "CLIENT_ARCHIVE";

    private String mPhone;
    private String archived;
    private String solde;
    private Realm realm;
    private ClientRepository clientRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            Facturesportrait firstFragment = new Facturesportrait();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
        mPhone = getIntent().getStringExtra(EXTRA_CLIENT_PHONE);
        archived = getIntent().getStringExtra(EXTRA_CLIENT_ARCHIVE);
        solde = getIntent().getStringExtra("lesolde");
        realm = Realm.getDefaultInstance();

        clientRepository = realm.where(ClientRepository.class).equalTo("numero", mPhone).findFirst();

        getSupportActionBar().setTitle(clientRepository.getNom() + "  " + mPhone);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_client_page_portrait;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
