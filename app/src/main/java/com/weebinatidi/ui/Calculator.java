/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.weebinatidi.ui;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.Boutique;
import com.weebinatidi.model.Client;
import com.weebinatidi.model.ClientRepository;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.Invoice;
import com.weebinatidi.model.InvoiceRepository;
import com.weebinatidi.model.Item;
import com.weebinatidi.model.ItemRepository;
import com.weebinatidi.model.LaReffb;
import com.weebinatidi.model.OperationClientFb;
import com.weebinatidi.model.OperationClientRepository;
import com.weebinatidi.model.ReferenceRepository;
import com.weebinatidi.model.Utilisateur;
import com.weebinatidi.ui.weebi2.Main4Activity;
import com.weebinatidi.ui.weebi2.NewInterface;
import com.weebinatidi.ui.weebi2.ReferencesSQL;
import com.weebinatidi.ui.widget.CalculatorEditText;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.view.View.VISIBLE;
import static com.weebinatidi.Config.isAppAvailable;
import static com.weebinatidi.ui.CalculatorExpressionEvaluator.EvaluateCallback;
import static com.weebinatidi.ui.widget.CalculatorEditText.OnClickListener;
import static com.weebinatidi.ui.widget.CalculatorEditText.OnTextSizeChangeListener;

public abstract class Calculator extends Activity
        implements OnTextSizeChangeListener, EvaluateCallback, OnLongClickListener {

    /**
     * Constant for an invalid resource id.
     */
    public static final int INVALID_RES_ID = -1;
    public static final String DATABASE_PREFERENCES_LABEL = "databaselabel";
    public static final String DATABASE_PREFERENCES_WEEBIPATH = "weebipath";
    // TODO: Mettre en place la page de visualisation des invoice.
    // TODO: Rendre l'interface adaptable aux differentes vue.
    private static final String NAME = Calculator.class.getName();
    private static final String TAG = Calculator.class.getSimpleName();
    // instance state keys
    private static final String KEY_CURRENT_STATE = NAME + "_currentState";
    private static final String KEY_CURRENT_EXPRESSION = NAME + "_currentExpression";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    protected LinearLayout mDisplayView;
    protected CalculatorEditText mFormulaEditText;
    protected CalculatorEditText mResultEditText;
    ArrayList<String> listref;
    private List<Invoice>results=new ArrayList<>();
    String lesdonnees=" Id, Date, heure, Nom client, Numero client, Solde client, Reference, PrixUnitaire, Quantite, PrixTotal\n";
    String clients="Nom, numero\n";
    String referencessauvegarde="Nom\n";
    long compteur = 0;
    int keyfirebase = 0;
    String uid = "";
    String email = "";
    String passe = "";
    SharedPreferences sharedPreferences;
    ArrayList<String> listreferences=new ArrayList<>();
    //image picker
    ImagePicker imagePicker;
    //Camera image picker
    CameraImagePicker cameraimagePicker;
    //FilePicker filepicker;
    //FilePickerCallback filePickercallback;
    ImagePickerCallback imagePickerCallback;
    ImagePickerCallback camimagePickerCallback;
    //Photourl
    String local_url_image = null;
    String file_url_path = null;
    ClientRepository selectedClient;
    AlertDialog.Builder RegisterClientbuilder;
    AlertDialog RegisterClientDialog;
    AlertDialog.Builder Listebuilder;
    AlertDialog Listedialog;
    AlertDialog.Builder Inscriptbuilder;
    AlertDialog Inscriptdialog;
    AlertDialog.Builder Okbuilder;
    AlertDialog Okdialog;
    List<String> references;
    ArrayAdapter<String> arrayAdapter;
    //Florentio <code></code>
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    ArrayList<String>clientsrestaure=new ArrayList<>();
    ArrayList<String>referencesrest=new ArrayList<>();
    ArrayList<String>facturerest=new ArrayList<>();
    // database content as string array
    FilePicker filepicker;
    FilePickerCallback filePickercallback;
    ArrayList<String> arrayString;
    DbHelper dbHelper;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button mPlusFab;
    private Button mClearFab;
    private Button mDelFab;
    private Button mOpMulFab;
    private Button mRegisterInv;
    private String madevise = "";
    private String langue = "";
    private Handler mHandler;
    private CalculatorState mCurrentState;
    private CalculatorExpressionTokenizer mTokenizer;
    private final Editable.Factory mFormulaEditableFactory = new Editable.Factory() {
        @Override
        public Editable newEditable(CharSequence source) {
            final boolean isEdited = mCurrentState == CalculatorState.INPUT
                    || mCurrentState == CalculatorState.ERROR;
            Log.d("FormulaEditable", "Source = " + source);
            return new CalculatorExpressionBuilder(source, mTokenizer, isEdited);
        }
    };
    private CalculatorExpressionEvaluator mEvaluator;
    private final TextWatcher mFormulaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.d("Calculator", "Formula text changed");
            setState(CalculatorState.INPUT);
            mEvaluator.evaluate(editable, Calculator.this);
        }
    };
    private final OnKeyListener mFormulaOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_NUMPAD_ENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        onEquals();
                    }
                    // ignore all other actions
                    return true;
            }
            return false;
        }
    };
    //private View mEqualButton;
    private View mAddClient;
    private View mDepositClient;
    private View mListClient;
    private Realm realm;
    private List<ClientRepository> clientRepositories = new ArrayList<>();
    private ListView listerefcal;

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static boolean isTablet7(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isSmart(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_NORMAL;
    }

    public static boolean isSmartsmall(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                <= Configuration.SCREENLAYOUT_SIZE_SMALL;
    }

    private String readFromFile(String path) {

        String ret = "";

        try {
            InputStream inputStream = Calculator.this.openFileInput(path);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private List<Client>listecli=new ArrayList<>();
    private List<String>lesnums=new ArrayList<>();
    boolean contain=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
        if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }
        sharedPreferences = getBaseContext().getSharedPreferences("prefes", MODE_PRIVATE);

        //cette fonction a été ajouté afin de sauvegarder avec incrementation du compteur dans firebase
        //sinon la sauvegarde se fait sur le même compteur
        if (sharedPreferences.contains("lecompteur")) {
            compteur = sharedPreferences.getLong("lecompteur", 0);
            // Toast.makeText(getApplicationContext(),""+compteur,Toast.LENGTH_LONG).show();

        }
        dbHelper=new DbHelper(this);

        listecli=dbHelper.getAllClient();
        for(Client r: listecli){
            lesnums.add(r.getNumero());
            //Toast.makeText(getApplicationContext(),""+r.getNumero(), Toast.LENGTH_LONG).show();
        }
        //see if we are first client cash added pisco
        if(lesnums.size() != 0){
            if(lesnums.contains("0")){
                //Toast.makeText(getApplicationContext(),"trouvé", Toast.LENGTH_LONG).show();
                Cursor cli=dbHelper.getClient("0");
                if(cli.moveToFirst()==true){
                    //Toast.makeText(getApplicationContext(),""+cli.getString(1), Toast.LENGTH_LONG).show();
                }

            }
            if(!lesnums.contains("0")){
                Toast.makeText(getApplicationContext(),"pas encore de client cash \n configuration en cours", Toast.LENGTH_LONG).show();
                dbHelper.insertClient("Cash","0","0");
            }
        }

        //creation du client cash au premier lancement de l'applicationrea
        if (listecli.size()==0){
            dbHelper.insertClient("Cash","0","0");
        }



        auth = FirebaseAuth.getInstance();
        realm = Realm.getDefaultInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sauvegarder();
        } else {
            sauvegarderlollipop();
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String temp = prefs.getString(DATABASE_PREFERENCES_WEEBIPATH, "notweebi");
        String tempdata = prefs.getString(DATABASE_PREFERENCES_LABEL, "");

        if (!TextUtils.isEmpty(temp) && !temp.equals("notweebi")) {
//            String data = readFromFile(temp);
            Log.d("path", "where it is  " + temp);

            arrayString = new ArrayList<String>();

        } else {
            // Load defautl stringressources to the preference
            prefEditor = prefs.edit();

            String[] stringsdatabase = getResources().getStringArray(R.array.database);

            String buildString = "";
            for (String s : stringsdatabase) {
                buildString += s + "|";

            }
            //  Log.e("Test", buildString);
            buildString = buildString.substring(0, buildString.length() - 1);

            prefEditor.putString(DATABASE_PREFERENCES_LABEL, buildString);
            prefEditor.commit();
        }


        if (sharedPreferences.contains("preflangue")) {
            langue = sharedPreferences.getString("preflangue", null);
            //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

            Configuration config = new Configuration();
            Locale locale;
            locale = new Locale(langue);
            config.locale = locale;
            getResources().updateConfiguration(config, null);

        } else {
            //Toast.makeText(getApplicationContext()," rien "+langue,Toast.LENGTH_LONG).show();
        }


        //if (Utils.hasLollipop())
        setContentView(R.layout.activity_calculator);
        mDisplayView = (LinearLayout) findViewById(R.id.display);
        mFormulaEditText = (CalculatorEditText) findViewById(R.id.formula);
        mResultEditText = (CalculatorEditText) findViewById(R.id.result);
        mAddClient = findViewById(R.id.add_client);
        mDepositClient = findViewById(R.id.deposit_client);
        mListClient = findViewById(R.id.list_client);
        mPlusFab = (Button) findViewById(R.id.op_add);
        mDelFab = (Button) findViewById(R.id.del);
        mClearFab = (Button) findViewById(R.id.clr);
        mOpMulFab = (Button) findViewById(R.id.op_mul);
        //mRegisterInv = (Button) findViewById(R.id.register_invoice);

        listerefcal = (ListView) findViewById(R.id.listerefcalcu);
        references = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, references);
        RealmResults<ReferenceRepository> results = realm.where(ReferenceRepository.class).findAll();

        for (ReferenceRepository r : results) {
            //Toast.makeText(getApplicationContext(), ""+r.getNomref(),Toast.LENGTH_SHORT).show();
            references.add(r.getNomref());
        }

//        listerefcal.setAdapter(arrayAdapter);


        //8***************************************************************
        //set up image selection...
        imagePicker = new ImagePicker(Calculator.this);
        imagePickerCallback = new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if (!TextUtils.isEmpty(list.get(0).getOriginalPath().toString())) {
                    local_url_image = list.get(0).getOriginalPath().toString();

//                    if(!TextUtils.isEmpty(local_url_image))
//                    {
//                        Toast.makeText(Calculator.this, R.string.photoboutikajoute,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(Calculator.this, R.string.photoboutiknonajoute,
//                                Toast.LENGTH_SHORT).show();
//                    }
                }
            }

            @Override
            public void onError(String s) {

            }
        };
        imagePicker.setImagePickerCallback(imagePickerCallback);

        // imagePicker.allowMultiple(); // Default is false
        // imagePicker.shouldGenerateMetadata(false); // Default is true
        // imagePicker.shouldGenerateThumbnails(false); // Default is true


        //set up image taken by camera..
        cameraimagePicker = new CameraImagePicker(Calculator.this);
        camimagePickerCallback = new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                Log.d("photo taken", " " + list.get(0).getOriginalPath().toString());
                if (!TextUtils.isEmpty(list.get(0).getOriginalPath().toString())) {
                    local_url_image = list.get(0).getOriginalPath().toString();

//                    if(!TextUtils.isEmpty(local_url_image))
//                    {
//                        Toast.makeText(Calculator.this, R.string.photoboutikajoute,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(Calculator.this, R.string.photoboutiknonajoute,
//                                Toast.LENGTH_SHORT).show();
//                    }
                }

            }

            @Override
            public void onError(String s) {

            }
        };

        cameraimagePicker.setImagePickerCallback(camimagePickerCallback);

        filepicker = new FilePicker(Calculator.this);

        filePickercallback = new FilePickerCallback() {
            @Override
            public void onFilesChosen(List<ChosenFile> list) {
                Log.d("file choosen ", " " + list.get(0).getOriginalPath().toString());
                if (!TextUtils.isEmpty(list.get(0).getOriginalPath().toString())) {
                    file_url_path = list.get(0).getOriginalPath().toString();
                }
            }

            @Override
            public void onError(String s) {

            }
        };

        filepicker.setFilePickerCallback(filePickercallback);

        //8***************************************************************

        // open the default realm for the UI thread
        //realm = Realm.getDefaultInstance();


        mHandler = new Handler();

        mTokenizer = new CalculatorExpressionTokenizer(this);
        mEvaluator = new CalculatorExpressionEvaluator(mTokenizer);

        savedInstanceState = savedInstanceState == null ? Bundle.EMPTY : savedInstanceState;
        setState(CalculatorState.values()[
                savedInstanceState.getInt(KEY_CURRENT_STATE, CalculatorState.INPUT.ordinal())]);
        String keyCurrentExpr = savedInstanceState.getString(KEY_CURRENT_EXPRESSION);
        mFormulaEditText.setText(mTokenizer.getLocalizedExpression(
                keyCurrentExpr == null ? "" : keyCurrentExpr));
        mEvaluator.evaluate(mFormulaEditText.getText(), this);

        mFormulaEditText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                return null;
            }
        }});

        mFormulaEditText.setEditableFactory(mFormulaEditableFactory);
        mFormulaEditText.addTextChangedListener(mFormulaTextWatcher);
        mFormulaEditText.setOnKeyListener(mFormulaOnKeyListener);
        mDelFab.setOnLongClickListener(this);


        //set up boutik infos on first start ...
        SharedPreferences preferences = getSharedPreferences("weebi", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        boolean gosetup = preferences.getBoolean(SplashActivity.Start, false);

        if (gosetup == true) {
            onRegisterBoutik(true);
            /*Config.ShowAlertDialogtosetupprinter(Calculator.this,getString(R.string.configure_imprimante),getString(R.string.texte1) +
                                                               getString(R.string.texte2) +
                                                               getString(R.string.text3));*/
            edit.putBoolean(SplashActivity.Start, false);
            edit.commit();

            SetpWeebiTelegram();
        }


    }

    public void ecrire(String donnees){

        Boutique maboutique = realm.where(Boutique.class).findFirst();

            /*String nomboutique = maboutique.getNom();
            String tel=maboutique.getNumero();
            String date=Config.getFormattedDate(Calendar.getInstance().getTime());*/



        RealmResults<ClientRepository> resultsclient = realm.where(ClientRepository.class).findAll();
        for (ClientRepository r : resultsclient) {
            //final String referenceRealm=r.getNomref().toString();
            Client client = new Client();
            client.setNom(r.getNom());
            client.setNumero(r.getNumero());
            client.setSolde(r.getSolde());


            //lesdonnees+=r.getNom()+","+r.getNumero()+","+r.getSolde();

            RealmList<OperationClientRepository> listop = r.getOperationClients();
            //recuperation de toutes les opérations faites par un client
            RealmResults<OperationClientRepository> lesop = listop.where().findAll();

            ArrayList<OperationClientFb> operas = new ArrayList<OperationClientFb>();
            for (OperationClientRepository op : lesop) {
                OperationClientFb opfb = new OperationClientFb();
                opfb.setDate(op.getDate());
                opfb.setMontant(op.getMontant());

                final Date ladate=op.getDate();
                final String datestring=Config.getFormattedDate(ladate);
                String tabdate[]=datestring.split("a");
                String dater=tabdate[0];
                String heurer=tabdate[1];


                InvoiceRepository invoicerep = op.getInvoiceRepository();

                Invoice invoice = new Invoice();
                invoice.setTotal(invoicerep.getTotal());
                invoice.setId(invoicerep.getId());

                int idfact=invoicerep.getId();



                RealmList<ItemRepository> listitem = invoicerep.getItems();
                RealmResults<ItemRepository> resultsitem = listitem.where().findAll();

                ArrayList<Item> lesitems = new ArrayList<Item>();
                for (ItemRepository it : resultsitem) {
                    Item item = new Item();
                    item.setId(it.getId());
                    item.setName(it.getName());
                    item.setQuantity(it.getQuantity());
                    item.setUnitPrice(it.getUnitPrice());
                    item.setTotalPrice(it.getTotalPrice());
                    lesitems.add(item);
                    donnees+=idfact+","+dater+","+heurer+","+r.getNom()+","+r.getNumero()+","+r.getSolde()+","+it.getName()+","+it.getUnitPrice()+","+it.getQuantity()+","+it.getTotalPrice()+"\n";
                }

                invoice.setItems(lesitems);
                opfb.setInvoice(invoice);

                operas.add(opfb);

                Log.d("l'invoice", "" + invoicerep);
                //opfb.setInvoice();
            }

        }
        //donnees+="boutique "+nomboutique+" telephone "+tel+" date synchronisation "+date+"\n";
        //donnees="Test de fichier";
        File file, f = null;
        String filePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), "Weebi/Fichier");
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath = file.getAbsolutePath() + File.separator + "fichier.csv";
            f = new File(filePath);
        }
        FileOutputStream ostream = null;
        try {
            //String userName = "Test de fichier";
            ostream = new FileOutputStream(f);
            ostream.write(donnees.getBytes());
            ostream.close();
            //shareString(donnees);
            shareGeneratedInvoice(filePath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareString(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Envoyer"));
    }

    private void shareGeneratedInvoice(String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(new File(path));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("text/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Envoyer"));
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sauvegarder() {


        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
        }
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }


        Account[] accounts = AccountManager.get(getApplicationContext()).getAccountsByType("com.google");
        for (Account account : accounts) {

            email = account.name.toString();
            //Toast.makeText(getApplicationContext(),""+email,Toast.LENGTH_SHORT).show();
        }

        passe = "weebiéà&è";
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user == null) {


            auth.signInWithEmailAndPassword(email, passe)
                    .addOnCompleteListener(Calculator.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            //progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                // there was an error

                                //Toast.makeText(Calculator.this,R.string.connectionechec , Toast.LENGTH_LONG).show();

                            } else {

                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //Toast.makeText(Calculator.this, getString(R.string.connectionreussi)+user.getUid(), Toast.LENGTH_LONG).show();
                                //finish();
                                uid = user.getUid();
                                ajouterdatafirebase();

                            }
                        }
                    });

            auth.createUserWithEmailAndPassword(email, passe)
                    .addOnCompleteListener(Calculator.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Toast.makeText(Calculator.this, getString(R.string.createuserfbgood) + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                //Toast.makeText(Calculator.this, getString(R.string.authenticatefailed) + task.getException(),
                                //   Toast.LENGTH_SHORT).show();
                                Log.d("Erreur", "" + task.getException());
                            } else {

                                //Inscriptdialog.dismiss();
                                //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                // Toast.makeText(getApplicationContext(), R.string.inscriptionreussi, Toast.LENGTH_LONG).show();
                                ajouterdatafirebase();
                            }
                        }
                    });
        } else {
            //Toast.makeText(getApplicationContext(), ""+user.getUid(), Toast.LENGTH_LONG).show();

            ajouterdatafirebase();
        }

    }

    public void sauvegarderlollipop() {

        Account[] accounts = AccountManager.get(getApplicationContext()).getAccountsByType("com.google");
        for (Account account : accounts) {

            email = account.name.toString();
            //Toast.makeText(getApplicationContext(),""+email,Toast.LENGTH_SHORT).show();
        }

        passe = "weebiéà&è";
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user == null) {


            auth.signInWithEmailAndPassword(email, passe)
                    .addOnCompleteListener(Calculator.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            //progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                // there was an error

                                //Toast.makeText(Calculator.this,R.string.connectionechec , Toast.LENGTH_LONG).show();

                            } else {

                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //Toast.makeText(Calculator.this, getString(R.string.connectionreussi)+user.getUid(), Toast.LENGTH_LONG).show();
                                //finish();
                                uid = user.getUid();
                                ajouterdatafirebase();

                            }
                        }
                    });

            auth.createUserWithEmailAndPassword(email, passe)
                    .addOnCompleteListener(Calculator.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Toast.makeText(Calculator.this, getString(R.string.createuserfbgood) + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                //Toast.makeText(Calculator.this, getString(R.string.authenticatefailed) + task.getException(),
                                //   Toast.LENGTH_SHORT).show();
                                Log.d("Erreur", "" + task.getException());
                            } else {

                                //Inscriptdialog.dismiss();
                                //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                // Toast.makeText(getApplicationContext(), R.string.inscriptionreussi, Toast.LENGTH_LONG).show();
                                ajouterdatafirebase();
                            }
                        }
                    });
        } else {
            //Toast.makeText(getApplicationContext(), ""+user.getUid(), Toast.LENGTH_LONG).show();

            ajouterdatafirebase();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
            /*    onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);*
        }*/
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //photo.setImageBitmap(bm);
    }

    public void SetpWeebiTelegram() {
        if (isAppAvailable(Calculator.this, Config.TelegramappName) == false) {
            Config.ShowAlertDialogtosetupTelegram(Calculator.this, getString(R.string.telegrambuildertitle),
                    getString(R.string.telegrambuildermsg), getString(R.string.weebibotsettitle), getString(R.string.weebibotsetmsg));
        } else {
            Config.ShowAlertDialogtosetupWeebiBot(Calculator.this, getString(R.string.weebibotsettitle), getString(R.string.weebibotsetmsg));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_image_path", local_url_image);
        outState.putString("file_picker_path", file_url_path);


        // If there's an animation in progress, end it immediately to ensure the state is
        // up-to-date before it is serialized.
        cancelAnimation();

        super.onSaveInstanceState(outState);

        outState.putInt(KEY_CURRENT_STATE, mCurrentState.ordinal());
        outState.putString(KEY_CURRENT_EXPRESSION,
                mTokenizer.getNormalizedExpression(mFormulaEditText.getText().toString()));
    }

    protected void setState(CalculatorState state) {
        if (mCurrentState != state) {
            mCurrentState = state;

            if (state == CalculatorState.RESULT || state == CalculatorState.ERROR) {
                mDelFab.setVisibility(View.GONE);
                mClearFab.setVisibility(VISIBLE);
                Log.d("Calculator", "disable register");
                //mRegisterInv.setEnabled(false);
            } else {
                mDelFab.setVisibility(VISIBLE);
                mClearFab.setVisibility(View.GONE);
                Log.d("Calculator", "enable register");
                //mRegisterInv.setEnabled(true);
            }

            if (state == CalculatorState.ERROR) {
                final int errorColor = getResources().getColor(R.color.calculator_error_color);
                mFormulaEditText.setTextColor(errorColor);
                mResultEditText.setTextColor(errorColor);
                Utils.setStatusBarColorCompat(getWindow(), errorColor);
                Log.d("Calculator", "disable register");
                //mRegisterInv.setEnabled(false);
            } else {
                mFormulaEditText.setTextColor(
                        getResources().getColor(R.color.display_formula_text_color));
                mResultEditText.setTextColor(
                        getResources().getColor(R.color.display_result_text_color));
                Utils.setStatusBarColorCompat(getWindow(), getResources().getColor(R.color.calculator_accent_color));
                Log.d("Calculator", "enable register");
                //mRegisterInv.setEnabled(true);
            }
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(imagePicker == null) {
                    imagePicker = new ImagePicker(Calculator.this);
                    imagePicker.setImagePickerCallback(imagePickerCallback);
                }
                imagePicker.submit(data);

            }
            if (requestCode == Picker.PICK_FILE){

//                String FilePath = data.getData().getPath();
                file_url_path = data.getData().getPath();

                Log.d("url_file"," "+file_url_path);

//                if(!TextUtils.isEmpty(FilePath))
//                {
//                    prefEditor.putString(DATABASE_PREFERENCES_WEEBIPATH, FilePath);
//                    prefEditor.commit();
//                }

                if(!TextUtils.isEmpty(file_url_path))
                {
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString(DATABASE_PREFERENCES_WEEBIPATH, file_url_path);
                    edit.commit();
                }

                Uri uri = data.getData();

                readFile(uri);

            }
            if(requestCode == Picker.PICK_IMAGE_CAMERA) {
                if(cameraimagePicker == null) {
                    cameraimagePicker = new CameraImagePicker(Calculator.this);
                    cameraimagePicker.setImagePickerCallback(camimagePickerCallback);
                }
                cameraimagePicker.submit(data);
            }
        }
    }*/

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    public void readFile(Uri fileUri) {
        BufferedReader br;
        String prefContent = "";
        try {
            br = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(fileUri)));

            String line = null;
            while ((line = br.readLine()) != null) {

                if (!prefContent.toLowerCase().contains(line.toLowerCase())) {
                    prefContent = prefContent + "|" + line;
                    // Log.e("TEST", line);
                }
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(DATABASE_PREFERENCES_LABEL, prefContent);
        edit.commit();

//        prefEditor.putString(DATABASE_PREFERENCES_LABEL, prefContent);
//        prefEditor.commit();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-initialize these
        // two values to be able to re-initialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_image_path")) {
                local_url_image = savedInstanceState.getString("picker_image_path");
            } else {
                if (savedInstanceState.containsKey("file_picker_path")) {
                    file_url_path = savedInstanceState.getString("file_picker_path");
                }
            }

        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        //désactivation du bouton retour dans l'activité calculator
        //super.onBackPressed();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        // If there's an animation in progress, end it immediately to ensure the state is
        // up-to-date before the pending user interaction is handled.
        cancelAnimation();
    }

    public void onActionClick(View view) {
        switch (view.getId()) {
            case R.id.add_client:
                // launch a dialog to register a client.
                onRegisterClient();
                break;
            case R.id.deposit_client:
                // launch a dialog to enter a client deposit.
                onClientDeposit();
                break;
            case R.id.list_client:
                // launch the activity to view client list.
                Intent intent = new Intent(this, ClientListActivitySQL.class);
                startActivity(intent);
                break;
            case R.id.lesrefs:
                // launch the activity to view invoice list.
                /*Intent invoiceIntent  = new Intent(this, InvoiceListActivity.class);
                startActivity(invoiceIntent);*/
                Intent intent1 = new Intent(this, ReferencesSQL.class);
                startActivity(intent1);
                break;
            case R.id.shop:
                onRegisterBoutik(false);
                /*Intent param=new Intent(this, Parametres.class);
                startActivity(param);*/
                break;
            case R.id.export_data:
                //Toast.makeText(getApplicationContext(),"Contactez-nous via hello@weebi.com",Toast.LENGTH_LONG).show();
                /*Intent param=new Intent(this, Contacts.class);
                startActivity(param);*/
                //startService(new Intent(getBaseContext(), ServiceSauvegardata.class));
                //Export Happens here..

                //sauvegarder();

                break;
            default:
                break;
        }
    }

    private boolean verifappinstalleeoupas(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void onClientDeposit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_new_deposit, null, false);
        final EditText clientSolde = (EditText) customView.findViewById(R.id.client_solde);
        final EditText clientSoldee = (EditText) customView.findViewById(R.id.client_solderewrite);

        // get all the clients
        RealmResults<ClientRepository> clients = realm.where(ClientRepository.class).findAll();

        ClientSingleChoiceListAdapter adapter = new ClientSingleChoiceListAdapter(clients);


        builder.setView(customView);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.enregistrer_acompte));
        builder.setNegativeButton(getString(R.string.dropit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just dismiss the dialog
            }
        });

        builder.setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Will be handle after the show() method of the dialog
                // for custom behavior.
            }
        });


    }

    private void onRegisterClient() {
        //TODO Q20 Done


        // launch a dialog to register a client.
//        RegisterClientbuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        RegisterClientbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_new_client, null, false);


        final FormEditText username = (FormEditText) customView.findViewById(R.id.client_name);
        final FormEditText phone = (FormEditText) customView.findViewById(R.id.client_number);
        // final FormEditText phone2 = (FormEditText) customView.findViewById(R.id.client_number2);
        final FormEditText solde = (FormEditText) customView.findViewById(R.id.client_solde);
        final FormEditText email = (FormEditText) customView.findViewById(R.id.client_mail);
        final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewclient);

        solde.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        RegisterClientbuilder.setCancelable(true);
        RegisterClientbuilder.setView(customView);
        RegisterClientbuilder.setTitle(getString(R.string.ajouter_un_client));


        RegisterClientDialog = RegisterClientbuilder.create();


        okbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterProcess(username, phone, phone, email, solde, RegisterClientDialog);
            }
        });

        RegisterClientDialog.show();

    }


    public void RegisterProcess(FormEditText username, FormEditText phone, FormEditText phone2, FormEditText email, FormEditText solde, AlertDialog alertDialog) {

//        FormEditText[] allFields = {username, phone,phone2,email};
        FormEditText[] allFields = {username, phone, phone2};
        boolean allvalid = true;

        for (FormEditText field : allFields) {
            allvalid = field.testValidity() && allvalid;
        }

//                if (isEntryValid(username, phone,phone2))
        if (allvalid) {
            if (phone.getText().toString().equals(phone.getText().toString())) {

                RealmQuery<ClientRepository> query = realm.where(ClientRepository.class);
                ClientRepository tmp = query.equalTo("numero", phone.getText().toString()).findFirst();


                if (tmp == null) {
                    String clientName = username.getText().toString();
                    String clientPhone = phone.getText().toString();
                    String clientSolde = solde.getText().toString();
//                    String clientmail = email.getText().toString();

                    realm.beginTransaction();
                    // Add the client
                    ClientRepository client = realm.createObject(ClientRepository.class);
                    client.setNom(clientName);
                    client.setNumero(clientPhone);
//                    client.setMail(clientmail);
                    realm.commitTransaction();

                    //realm.beginTransaction();

                    ClientRepository clienttmp = query.equalTo("numero", phone.getText().toString()).findFirst();
                    // Add the deposit to the client account
                    if (clienttmp != null) {
                        if (!TextUtils.isEmpty(clientSolde)) {
                            //  client.setSolde(Integer.valueOf(clientSolde));

                            //OperationClientRepository depot = realm.createObject(OperationClientRepository.class);
                            //depot.setClient(client);
                            // depot.setMontant(Integer.valueOf(clientSolde));
                            //depot.setDate(Calendar.getInstance().getTime());
                            //TODO should include the total invoices here and add it to this operation
                            //client.getOperationClients().add(depot);

                            ProcessToClientDeposit(realm, clienttmp, clientSolde);
                        }

//                    realm.commitTransaction();

                        Snackbar.make(mDisplayView, R.string.Clientwithpositivebalancecreated,
                                Snackbar.LENGTH_SHORT).show();
                    }


                    alertDialog.dismiss();
//                    RegisterClientDialog.dismiss();
                    // check the validity of the entry
                    // register the user in the database
                    // send confirmation message to the operator
                } else {
                    phone2.setError(getString(R.string.numalreadyused), getResources().getDrawable(R.drawable.add_user));
                    Snackbar.make(phone, getString(R.string.numalreadyused), Snackbar.LENGTH_SHORT).show();
                }


            } else {
//                        phone.setError("les numeros ne correspondent pas",getResources().getDrawable(R.drawable.add_user));
                phone2.setError(getString(R.string.numnomatch), getResources().getDrawable(R.drawable.add_user));
                Snackbar.make(phone, getString(R.string.numnomatch), Snackbar.LENGTH_LONG).show();

            }
        }
    }


    public void ProcessToClientDeposit(Realm realms, ClientRepository client, String soldes) {
        // Add the transaction to the deposit
        //realms.beginTransaction();
        OperationClientRepository depot = realms.createObject(OperationClientRepository.class);
        depot.setClient(client);
        depot.setMontant(Integer.valueOf(soldes));
        depot.setDate(Calendar.getInstance().getTime());
        depot.setInvoice(false);
        //********************TEMP********************//

        int invoicegoid = Config.getLastInvoiceId(Calculator.this) + 1;
//                            InvoiceRepository invoicedepot= new InvoiceRepository();
        InvoiceRepository invoicedepot = new InvoiceRepository();
        invoicedepot.setId(invoicegoid);
        Config.setLastInvoiceId(Calculator.this, invoicegoid);
        invoicedepot.setDate(Calendar.getInstance().getTime());
        invoicedepot.setTotal(Integer.valueOf(soldes));
        invoicedepot.setClient(client);
        invoicedepot.setInvoiceRepo(false);
        int itemgoid = Config.getLastItemId(Calculator.this) + 1;
        ItemRepository itemdepot = new ItemRepository();
        itemdepot.setId(itemgoid);
        Config.setLastItemId(Calculator.this, itemgoid);
        itemdepot.setName("Depot du " + Config.getFormattedDate(Calendar.getInstance().getTime()));
//        itemdepot.setQuantity(1);
        itemdepot.setQuantity(Integer.valueOf(soldes));
//        itemdepot.setUnitPrice(Integer.valueOf(soldes));
//        itemdepot.setTotalPrice(Integer.valueOf(soldes));
        itemdepot = realms.copyToRealmOrUpdate(itemdepot);
        invoicedepot.getItems().add(itemdepot);
        invoicedepot = realms.copyToRealmOrUpdate(invoicedepot);
        depot.setInvoiceRepository(invoicedepot);


        //*********************END TEMP********************//
        // Add the depot to the client
        client.getOperationClients().add(depot);
        int newSolde = client.getSolde() + Integer.valueOf(soldes);
        client.setSolde(newSolde);

        realms.commitTransaction();
    }


    public void onRegisterBoutik(boolean first) {


        // launch a dialog to register a client.
        RegisterClientbuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_boutique, null, false);

//        customView.setBackgroundColor(Color.BLACK);


        final FormEditText username = (FormEditText) customView.findViewById(R.id.boutik_name);
        final FormEditText phone = (FormEditText) customView.findViewById(R.id.boutik_number);
        //final FormEditText phone2 = (FormEditText) customView.findViewById(R.id.boutik_number2);
        //final Spinner devise = (Spinner) customView.findViewById(R.id.devise);
        final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewboutik);
        final Button addLibele = (Button) customView.findViewById(R.id.list_invoice);
        final Button imprim_btm = (Button) customView.findViewById(R.id.config_imprimante);
        //final Button rweebi_btm = (Button) customView.findViewById(R.id.robotweebi);
        final Spinner spinner = (Spinner) customView.findViewById(R.id.choixlangue);
        //final Button testacti = (Button) customView.findViewById(R.id.testact);
        //final Button donnees = (Button) customView.findViewById(R.id.telegram);
        final Button sauvergarde = (Button) customView.findViewById(R.id.sauvegarder);
        //final Button clientsql = (Button) customView.findViewById(R.id.clientsql);
         //final Button restaurer = (Button) customView.findViewById(R.id.restaurer);
        final Button nombout = (Button) customView.findViewById(R.id.nombout);

        /*clientsql.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Calculator.this, ReferencesSQL.class);
                startActivity(intent);
            }
        });*/

        nombout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Calculator.this, Main4Activity.class);
                startActivity(intent);
                //Boutique maboutique = realm.where(Boutique.class).findFirst();

                //dbHelper.insertBoutique(nomboutique,tel,"weebi");
                /*Cursor bouti=dbHelper.getBoutique("1");
                if(bouti.moveToFirst()==true){
                    Toast.makeText(Calculator.this, "\n" + bouti.getString(0)+"\n" + bouti.getString(1), Toast.LENGTH_SHORT).show();
                }*/


            }
        });

        /*restaurer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //restaurerdonnees();

                try{

                    File csvfile = new File(Environment.getExternalStorageDirectory(), "Weebi/Fichier");
                    CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()+File.separator+"fichier.csv"));
                    String [] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        // nextLine[] is an array of values from the line
                        //System.out.println(nextLine[0] + nextLine[1] + "");
                        //Toast.makeText(Calculator.this, " "+nextLine[2]+" "+nextLine[3], Toast.LENGTH_SHORT).show();
                        String nomclient=nextLine[3];
                        String numclient=nextLine[4];
                        String lareff=nextLine[6];
                        String id=nextLine[0];
                        String heure=nextLine[2];
                        String solde=nextLine[5];
                        String pu=nextLine[7];
                        String qte=nextLine[8];
                        String date=nextLine[1];
                        String total=nextLine[9];
                        clientsrestaure.add(nomclient+","+numclient+","+solde+"\n");
                        referencesrest.add(lareff);
                        facturerest.add(id+","+date+","+heure+","+total+","+numclient);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                    //Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
                }
                //suppression des entetes nom client et numero client
                String premier=clientsrestaure.get(0);
                String lid=premier;
                Iterator<String> iterator = clientsrestaure.iterator();
                while (iterator.hasNext()) {
                    String o = iterator.next();
                    if (o == lid) {
                       iterator.remove();
                    }
                }
                //suppresion de l'entete reference
                String deux=referencesrest.get(0);
                String lidr=deux;
                Iterator<String> iteratorref = referencesrest.iterator();
                while (iteratorref.hasNext()) {
                    String o = iteratorref.next();
                    if (o == lidr) {
                        iteratorref.remove();
                    }
                }

                //suppresion de l'entete facture
                String trois=facturerest.get(0);
                String lidf=deux;
                Iterator<String> iteratorfact = facturerest.iterator();
                while (iteratorfact.hasNext()) {
                    String o = iteratorfact.next();
                    if (o == lidf) {
                        iteratorfact.remove();
                    }
                }
                //élimination des doublons dans l'arraylist clientrestaure
                Set<String> mySet = new HashSet<String>(clientsrestaure);
                List<String> restaureclientbon = new ArrayList<String>(mySet);
                //Toast.makeText(Calculator.this, ""+restaureclientbon.toString(), Toast.LENGTH_SHORT).show();

                for(int i=0; i<restaureclientbon.size();i++){
                    //dbHelper.insertClient()
                    String valeurs=restaureclientbon.get(i);

                    String tab[]=valeurs.split(",");
                    String nom=tab[0];
                    String num=tab[1];
                    String solde=tab[2];
                    //Toast.makeText(Calculator.this, ""+nom, Toast.LENGTH_SHORT).show();
                    Cursor cursor=dbHelper.getClient(num);
                    if(!cursor.moveToFirst()){
                        dbHelper.insertClient(nom,num,solde);
                        Toast.makeText(Calculator.this, ""+dbHelper.getAllCliName(), Toast.LENGTH_SHORT).show();
                    }

                }

                //élimination des doublons dans l'arraylist reference
                Set<String> mySet1 = new HashSet<String>(referencesrest);
                List<String> restaurerefbon = new ArrayList<String>(mySet1);
                //Toast.makeText(Calculator.this, ""+restaurerefbon.toString(), Toast.LENGTH_SHORT).show();

                //parcour de la liste pour ajouter les clients
                for(int i=0; i<restaurerefbon.size();i++){
                //dbHelper.insertClient()
                    String valeurs=restaurerefbon.get(i);
                    //Toast.makeText(Calculator.this, ""+valeurs, Toast.LENGTH_SHORT).show();
                    dbHelper.insertReference(valeurs,"100","21");
                }

                //élimination des doublons dans l'arraylist facture
                Set<String> mySet2 = new HashSet<String>(facturerest);
                List<String> restaurefactbon = new ArrayList<String>(mySet2);

                //Toast.makeText(Calculator.this, ""+restaurefactbon.toString(), Toast.LENGTH_SHORT).show();

                for(int i=0; i<restaurefactbon.size();i++){
                    //dbHelper.insertClient()
                    String valeurs=restaurefactbon.get(i);

                    String tab[]=valeurs.split(",");
                    String id=tab[0];
                    String date=tab[1];
                    String heure=tab[2];
                    String total=tab[3];
                    String num=tab[4];
                    String dateheure=date+","+heure;
                    //Toast.makeText(Calculator.this, ""+nom, Toast.LENGTH_SHORT).show();
                    dbHelper.insertFact(dateheure,total,num,"");


                }
                results=dbHelper.getAllInvoice();
                Toast.makeText(Calculator.this, ""+results, Toast.LENGTH_SHORT).show();

                listreferences=dbHelper.getAllRefName();
                for(int i=0;i<listreferences.size();i++){
                    Toast.makeText(getApplicationContext(),""+listreferences.get(i),Toast.LENGTH_LONG).show();
                }


            }
        });

        testacti.setOnClickListener(new OnClickListener() {
            @Override
           public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NewInterface.class);
                startActivity(intent);
                           }
       });*/

        sauvergarde.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // startService(new Intent(getBaseContext(), ServiceSauvegardata.class));
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sauvegarder();
                }
                else {
                    sauvegarderlollipop();
                }
                ecrire(lesdonnees);
                Toast.makeText(getApplicationContext(),"sauvegardé",Toast.LENGTH_LONG).show();*/


            }
        });


        /*donnees.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Boutique maboutique = realm.where(Boutique.class).findFirst();
                if (maboutique != null) {
                    exportData();
                } else {
                    Snackbar.make(mFormulaEditText, R.string.save_info_first, Snackbar.LENGTH_LONG).show();
                }
            }


        });*/

        //spinner.setPrompt("select language");

        String[] languages = {getString(R.string.choix_langue), "Anglais", "Français", "Amharic", "Wolof", "ضضض", "India"};

        String[] devises = {"Selection devise", "cfa"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Configuration config = new Configuration();
                Locale locale;
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        langue = "en";
                        locale = new Locale(langue);
                        config.locale = locale;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("preflangue", langue).apply();
                        //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                        break;
                    case 2:
                        langue = "fr";
                        locale = new Locale(langue);
                        config.locale = locale;
                        editor = sharedPreferences.edit();
                        editor.putString("preflangue", langue).apply();
                        //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                        break;
                    case 3:
                        langue = "am";
                        locale = new Locale(langue);
                        config.locale = locale;
                        editor = sharedPreferences.edit();
                        editor.putString("preflangue", langue).apply();
                        //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                        break;
                    case 4:
                        langue = "wo";
                        locale = new Locale(langue);
                        config.locale = locale;
                        editor = sharedPreferences.edit();
                        editor.putString("preflangue", langue).apply();
                        //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                        break;
                    case 5:
                        langue = "ar";
                        locale = new Locale(langue);
                        config.locale = locale;
                        editor = sharedPreferences.edit();
                        editor.putString("preflangue", langue).apply();
                        //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                        break;
                    case 6:
                        langue = "hi";
                        locale = new Locale(langue);
                        config.locale = locale;
                        editor = sharedPreferences.edit();
                        editor.putString("preflangue", langue).apply();
                        //Toast.makeText(getApplicationContext(),""+langue,Toast.LENGTH_LONG).show();

                        break;

                    default:
                        config.locale = Locale.ENGLISH;
                        break;
                }
                getResources().updateConfiguration(config, null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (first == false) {
            imprim_btm.setVisibility(VISIBLE);
            imprim_btm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent serverIntent = new Intent(Calculator.this, com.weebinatidi.ui.print.DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_PRINT_DEVICE);*/
                    Intent intent=new Intent(Calculator.this, Usbimprime.class);
                    startActivity(intent);
                }
            });
        }

        final ImageView photo = (ImageView) customView.findViewById(R.id.boutik_image);


       /* rweebi_btm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://telegram.me/weebibot";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });*/


        addLibele.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadDataFromFile();
                Intent invoiceIntent = new Intent(Calculator.this, InvoiceListActivity.class);
                startActivity(invoiceIntent);
                //filepicker.pickFile();


            }
        });



        //en attente de selectionner son logo
       /* photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Calculator.this);

                // set title
                alertDialogBuilder.setTitle(getString(R.string.logo));

                // set dialog message
                alertDialogBuilder
                        //.setMessage(getString(R.string.select_option))
                        .setPositiveButton(getString(R.string.Select_picture),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked
                                //launch the selection of the picture process
                                //local_url_image = cameraimagePicker.pickImage();
                                //imagePicker.pickImage();

                                galleryIntent();
//

                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });*/


        //ajout d'un nouveau boutiquier dans la base
        okbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FormEditText[] allFields = {username, phone, phone};
                boolean allvalid = true;


                for (FormEditText field : allFields) {
                    allvalid = field.testValidity() && allvalid;
                }

//                if (isEntryValid(username, phone,phone2))
                if (TextUtils.isEmpty(local_url_image)) {
                    //Snackbar.make(phone, getString(R.string.needphoto), Snackbar.LENGTH_LONG).show();
                }
                if (allvalid) {
                    Config.WritetoBase(Calculator.this, local_url_image, Config.Boutik_url_photo, MODE_PRIVATE);
                    if (phone.getText().toString().equals(phone.getText().toString())) {

                        RealmQuery<Boutique> query = realm.where(Boutique.class);
                        Boutique tmp = query.findFirst();

                        String clientName = username.getText().toString();
                        String clientPhone = phone.getText().toString();

//                        String clientSolde = solde.getText().toString();



                        if (tmp == null) {
//                            String clientName = username.getText().toString();
//                            String clientPhone = phone.getText().toString();
//                            String clientSolde = solde.getText().toString();



                            realm.beginTransaction();
                            // Add the shop
                            Boutique boutique = new Boutique();
                            boutique.setNom(clientName);
                            boutique.setNumero(clientPhone);

                            //create or replace on the base...
                            realm.copyToRealmOrUpdate(boutique);
                            realm.commitTransaction();


                            RegisterClientDialog.dismiss();
                            Snackbar.make(mFormulaEditText, R.string.businessinfosetup,
                                    Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), StartActivity.class));
                            // check the validity of the entry
                            // register the user in the database
                            // send confirmation message to the operator
                        } else {

                            realm.beginTransaction();
                            // Update the shop
                            tmp.setNom(clientName);
                            tmp.setNumero(clientPhone);

                            //create or replace on the base...
                            realm.copyToRealmOrUpdate(tmp);
                            realm.commitTransaction();

                            RegisterClientDialog.dismiss();
                            Snackbar.make(phone, R.string.businessinfoupdated,
                                    Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), StartActivity.class));
                        }

                    }
                        /*else
                        {
//                        phone.setError("les numeros ne correspondent pas",getResources().getDrawable(R.drawable.add_user));
                            phone2.setError(getString(R.string.numnomatch),getResources().getDrawable(R.drawable.add_user));
                            Snackbar.make(phone,getString(R.string.numnomatch), Snackbar.LENGTH_LONG).show();

                        }*/
                }

            }

        });

        //look for the shop in the database...
        RealmQuery<Boutique> query = realm.where(Boutique.class);
        RealmResults<Boutique> size = query.findAll();
        Log.d(" taille ", " " + size.size());
        Boutique tmp = query.findFirst();

        if (tmp != null) {
            username.setText(tmp.getNom());
            phone.setText(tmp.getNumero());
            //phone2.setText(tmp.getNumero());

        }

        RegisterClientbuilder.setCancelable(true);
        RegisterClientbuilder.setView(customView);
        RegisterClientbuilder.setTitle(getString(R.string.user_info));


        RegisterClientDialog = RegisterClientbuilder.create();
        RegisterClientDialog.show();

    }

    private void restaurerdonnees() {
        Boutique maboutique = realm.where(Boutique.class).findFirst();
        String nomboutique = maboutique.getNom();

        //final DatabaseReference databaseReference2 = databaseReference1.child("References");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference1 = databaseReference.child(user.getUid()).child("WeebiBoutiques").child("" + nomboutique).child("" + compteur);
        // Toast.makeText(Calculator.this, "Connexion bon : \n" + databaseReference1, Toast.LENGTH_LONG).show();
        final DatabaseReference databaseReference2 = databaseReference1.child("References");
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String email = dataSnapshot.getKey();
                //LaReffb laref=dataSnapshot.getValue(LaReffb.class);
                String reff = dataSnapshot.child("laref").getValue(String.class);
                // Toast.makeText(Calculator.this, "Connexion bon : \n" + reff, Toast.LENGTH_SHORT).show();
                //Log.d("une ref",""+laref.getLaref());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void ajouterdatafirebase() {

       /* RealmQuery<Boutique> query = realm.where(Boutique.class);

        Boutique tmp = query.findFirst();*/

        Boutique maboutique = realm.where(Boutique.class).findFirst();


        if (maboutique == null) {

            Toast.makeText(Calculator.this, "Veuillez configurer votre boutique merci", Toast.LENGTH_LONG).show();
        } else {


            String nomboutique = maboutique.getNom();
            String tel=maboutique.getNumero();
            String date=Config.getFormattedDate(Calendar.getInstance().getTime());

            //lesdonnees+=nomboutique+" "+tel+" "+date+"\n";
            //ecrire(lesdonnees);

            //Toast.makeText(Calculator.this, "le nom de la boutique: \n" + nomboutique, Toast.LENGTH_LONG).show();

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //final String email=user.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("https://api-project-464791921096/database/data/y6M8gETM9ra4fA7dYMH48HProv72/WeebiBoutiques/poissonnerie%20estelle/4/References");
            //Toast.makeText(Calculator.this, "Connexion bon : \n" + databaseReference, Toast.LENGTH_LONG).show();
            DatabaseReference databaseReference1 = databaseReference.child(user.getUid()).child("WeebiBoutiques").child("" + compteur);
            final DatabaseReference databaseReference2 = databaseReference1.child("References");
            final DatabaseReference databaseReference3 = databaseReference1.child("Clients");
            final DatabaseReference databaseReference4 = databaseReference1.child("Factures");
            final DatabaseReference databaseReference5 = databaseReference1.child("Utilisateur ");
            final DatabaseReference databaseReference6 = databaseReference1.child("Operations");
            //databaseReference5.push();



            //compteur = compteur + 1;

            //tant que le compteur est compris en 0 et 4 on fait un nouvel ajout dans firebase
            if (compteur >= 2) {

                SharedPreferences.Editor editore = sharedPreferences.edit();
                editore.putLong("lecompteur", compteur).apply();


                Utilisateur utilisateur=new Utilisateur();
                utilisateur.setNomboutique(nomboutique);
                utilisateur.setNumtel(tel);
                utilisateur.setDatesynchro(date);
                databaseReference5.setValue(utilisateur);

                //Toast.makeText(getApplicationContext()," "+compteur,Toast.LENGTH_SHORT).show();

                //on recupere les references dans la base realm
                RealmResults<ReferenceRepository> results = realm.where(ReferenceRepository.class).findAll();
                for (ReferenceRepository r : results) {
                    //final String referenceRealm=r.getNomref().toString();
                    final LaReffb laReffb = new LaReffb(r.getNomref());
                    databaseReference2.push().setValue(laReffb);

                }

                //}

                //traitement des donnees clients


                //on recupere les clients et on les ajoute dans firebase
                RealmResults<ClientRepository> resultsclient = realm.where(ClientRepository.class).findAll();
                for (ClientRepository r : resultsclient) {
                    //final String referenceRealm=r.getNomref().toString();
                    Client client = new Client();
                    client.setNom(r.getNom());
                    client.setNumero(r.getNumero());
                    client.setSolde(r.getSolde());


                    //lesdonnees+=r.getNom()+","+r.getNumero()+","+r.getSolde();

                    RealmList<OperationClientRepository> listop = r.getOperationClients();
                    //recuperation de toutes les opérations faites par un client
                    RealmResults<OperationClientRepository> lesop = listop.where().findAll();

                    ArrayList<OperationClientFb> operas = new ArrayList<OperationClientFb>();
                    for (OperationClientRepository op : lesop) {
                        OperationClientFb opfb = new OperationClientFb();
                        opfb.setDate(op.getDate());
                        opfb.setMontant(op.getMontant());


                        InvoiceRepository invoicerep = op.getInvoiceRepository();

                        Invoice invoice = new Invoice();
                        invoice.setTotal(invoicerep.getTotal());
                        invoice.setId(invoicerep.getId());



                        RealmList<ItemRepository> listitem = invoicerep.getItems();
                        RealmResults<ItemRepository> resultsitem = listitem.where().findAll();

                        ArrayList<Item> lesitems = new ArrayList<Item>();
                        for (ItemRepository it : resultsitem) {
                            Item item = new Item();
                            item.setId(it.getId());
                            item.setName(it.getName());
                            item.setQuantity(it.getQuantity());
                            item.setUnitPrice(it.getUnitPrice());
                            item.setTotalPrice(it.getTotalPrice());
                            lesitems.add(item);
                            //lesdonnees+=r.getNom()+","+r.getNumero()+","+r.getSolde()+","+it.getName()+","+it.getUnitPrice()+","+it.getQuantity()+","+it.getTotalPrice()+"\n";
                        }

                        invoice.setItems(lesitems);
                        opfb.setInvoice(invoice);

                        operas.add(opfb);

                        Log.d("l'invoice", "" + invoicerep);
                        //opfb.setInvoice();
                    }
                    client.setOperationClientsfb(operas);

                    databaseReference3.push().setValue(client);
                }
                //}


                //on recupere les references dans la base realm
                RealmResults<OperationClientRepository> resultsop = realm.where(OperationClientRepository.class).findAll();
                for (OperationClientRepository r : resultsop) {
                    String client = r.getClient().getNumero();
                    int idinvoice = r.getInvoiceRepository().getId();
                    Invoice invoice = new Invoice();
                    invoice.setId(idinvoice);
                    Log.d("client", "" + client);
                    Client client1 = new Client();
                    client1.setNumero(client);
                    //final String referenceRealm=r.getNomref().toString();
                    OperationClientFb operationClient = new OperationClientFb();
                    operationClient.setClient(client1);
                    operationClient.setMontant(r.getMontant());
                    operationClient.setDate(r.getDate());
                    operationClient.setInvoice(invoice);
                    //operationClient.isInvoice();
                    databaseReference6.push().setValue(operationClient);
                }

                //}

                //traitement des donnees invoices

                //on recupere les references dans la base realm
                RealmResults<InvoiceRepository> resultsinv = realm.where(InvoiceRepository.class).findAll();
                for (InvoiceRepository r : resultsinv) {
                    String client = r.getClient().getNumero();
                    Client client1 = new Client();
                    client1.setNumero(client);

                    RealmList<ItemRepository> rlistitem = r.getItems();
                    RealmResults<ItemRepository> lesitems = rlistitem.where().findAll();

                    ArrayList<Item> item1 = new ArrayList<Item>();

                    for (ItemRepository item : lesitems) {
                        Item item2 = new Item();
                        item2.setId(item.getId());
                        item2.setName(item.getName());
                        item2.setQuantity(item.getQuantity());
                        item2.setTotalPrice(item.getTotalPrice());
                        item2.setUnitPrice(item.getUnitPrice());
                        //databaseReference5.push().setValue(item1);
                        item1.add(item2);
                    }
                    Invoice invoice = new Invoice();
                    invoice.setClientm(client1);
                    invoice.setId(r.getId());
                    invoice.setTotal(r.getTotal());
                    invoice.setItems(item1);
                    databaseReference4.push().setValue(invoice);

                }

                //}
                compteur = 2;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("lecompteur", compteur).apply();
            } else {


                Utilisateur utilisateur=new Utilisateur();
                utilisateur.setNomboutique(nomboutique);
                utilisateur.setNumtel(tel);
                utilisateur.setDatesynchro(date);
                databaseReference5.setValue(utilisateur);

               // Toast.makeText(getApplicationContext(),""+compteur,Toast.LENGTH_SHORT).show();

                //on recupere les references dans la base realm
                RealmResults<ReferenceRepository> results = realm.where(ReferenceRepository.class).findAll();
                for (ReferenceRepository r : results) {
                    //final String referenceRealm=r.getNomref().toString();
                    LaReffb laReffb = new LaReffb(r.getNomref());
                    databaseReference2.setValue(laReffb);

                }

                //}

                //traitement des donnees clients


                //on recupere les clients et on les ajoute dans firebase
                RealmResults<ClientRepository> resultsclient = realm.where(ClientRepository.class).findAll();
                for (ClientRepository r : resultsclient) {
                    //final String referenceRealm=r.getNomref().toString();
                    Client client = new Client();
                    client.setNom(r.getNom());
                    client.setNumero(r.getNumero());
                    client.setSolde(r.getSolde());
                    final String numero = r.getNumero();


                    RealmList<OperationClientRepository> listop = r.getOperationClients();
                    //recuperation de toutes les opérations faites par un client
                    RealmResults<OperationClientRepository> lesop = listop.where().findAll();

                    ArrayList<OperationClientFb> operas = new ArrayList<OperationClientFb>();
                    for (OperationClientRepository op : lesop) {
                        OperationClientFb opfb = new OperationClientFb();
                        opfb.setDate(op.getDate());
                        opfb.setMontant(op.getMontant());


                        InvoiceRepository invoicerep = op.getInvoiceRepository();

                        Invoice invoice = new Invoice();
                        invoice.setTotal(invoicerep.getTotal());
                        invoice.setId(invoicerep.getId());

                        RealmList<ItemRepository> listitem = invoicerep.getItems();
                        RealmResults<ItemRepository> resultsitem = listitem.where().findAll();

                        ArrayList<Item> lesitems = new ArrayList<Item>();
                        for (ItemRepository it : resultsitem) {
                            Item item = new Item();
                            item.setId(it.getId());
                            item.setName(it.getName());
                            item.setQuantity(it.getQuantity());
                            item.setUnitPrice(it.getUnitPrice());
                            item.setTotalPrice(it.getTotalPrice());
                            lesitems.add(item);
                        }

                        invoice.setItems(lesitems);
                        opfb.setInvoice(invoice);

                        operas.add(opfb);

                        Log.d("l'invoice", "" + invoicerep);
                        //opfb.setInvoice();
                    }
                    client.setOperationClientsfb(operas);

                    databaseReference3.push().setValue(client);
                }
                //}


                //on recupere les references dans la base realm
                RealmResults<OperationClientRepository> resultsop = realm.where(OperationClientRepository.class).findAll();
                for (OperationClientRepository r : resultsop) {
                    String client = r.getClient().getNumero();
                    int idinvoice = r.getInvoiceRepository().getId();
                    Invoice invoice = new Invoice();
                    invoice.setId(idinvoice);
                    Log.d("client", "" + client);
                    Client client1 = new Client();
                    client1.setNumero(client);
                    //final String referenceRealm=r.getNomref().toString();
                    OperationClientFb operationClient = new OperationClientFb();
                    operationClient.setClient(client1);
                    operationClient.setMontant(r.getMontant());
                    operationClient.setDate(r.getDate());
                    operationClient.setInvoice(invoice);
                    //operationClient.isInvoice();
                    databaseReference6.push().setValue(operationClient);
                }

                //}

                //traitement des donnees invoices

                //on recupere les references dans la base realm
                RealmResults<InvoiceRepository> resultsinv = realm.where(InvoiceRepository.class).findAll();
                for (InvoiceRepository r : resultsinv) {
                    String client = r.getClient().getNumero();
                    Client client1 = new Client();
                    client1.setNumero(client);

                    RealmList<ItemRepository> rlistitem = r.getItems();
                    RealmResults<ItemRepository> lesitems = rlistitem.where().findAll();

                    ArrayList<Item> item1 = new ArrayList<Item>();

                    for (ItemRepository item : lesitems) {
                        Item item2 = new Item();
                        item2.setId(item.getId());
                        item2.setName(item.getName());
                        item2.setQuantity(item.getQuantity());
                        item2.setTotalPrice(item.getTotalPrice());
                        item2.setUnitPrice(item.getUnitPrice());
                        //databaseReference5.push().setValue(item1);
                        item1.add(item2);
                    }
                    Invoice invoice = new Invoice();
                    invoice.setClientm(client1);
                    invoice.setId(r.getId());
                    invoice.setTotal(r.getTotal());
                    invoice.setItems(item1);
                    databaseReference4.push().setValue(invoice);

                }

                compteur = 2; //compteur + 1;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("lecompteur", compteur).apply();
            }

        }
    }

    private boolean isEntryValid(EditText... views) {
        boolean isValid = true;
        View errorView = null;
        for (int i = 0; i < views.length; i++) {
            if (TextUtils.isEmpty(views[i].getText().toString())) {
                isValid = false;
                errorView = views[i];
                views[i].setError(getString(R.string.error_empty_field_msg));
            }
        }


        if (errorView != null) {
            errorView.requestFocus();
        }

        return isValid;
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.del:
                onDelete();
                break;
            case R.id.clr:
                onClear();
                break;

            case R.id.op_add:
                //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
                mFormulaEditText.append(CalculatorExpressionTokenizer.ADD_ESC);
                break;
            case R.id.op_mul:
                mFormulaEditText.append(getString(R.string.op_mul));
                break;
            case R.id.op_ok:
                onEquals();
                break;
//triple 000
            case R.id.export_data:
//                exportData();
                mFormulaEditText.append(getString(R.string.triple_zero));
                break;
            case R.id.digit_00:
                mFormulaEditText.append(getString(R.string.double_zero));
                break;
            default:
                mFormulaEditText.append(((Button) view).getText());
                break;
        }
    }

    private void inscription(String emaile) {

        Inscriptbuilder = new AlertDialog.Builder(this);
        final View customView = getLayoutInflater().inflate(R.layout.dialog_sinscrire, null, false);

        final EditText mail = (EditText) customView.findViewById(R.id.email);
        final EditText pass = (EditText) customView.findViewById(R.id.password);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mail.setText(emaile);
        mail.setEnabled(false);

        final ImageView okbtn = (ImageView) customView.findViewById(R.id.oknewboutiq);

        Inscriptbuilder.setCancelable(true);
        Inscriptbuilder.setView(customView);
        Inscriptbuilder.setTitle((getString(R.string.sinscrire)));


        Inscriptdialog = Inscriptbuilder.create();


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String passe = pass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    // Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passe)) {
                    //Toast.makeText(getApplicationContext(), "Entrer le mot de passe!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // progressBar.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(),""+email+" "+passe,Toast.LENGTH_LONG).show();
                    auth.createUserWithEmailAndPassword(email, passe)
                            .addOnCompleteListener(Calculator.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Toast.makeText(Calculator.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    // progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        // Toast.makeText(Calculator.this, "Authentication failed." + task.getException(),
                                        // Toast.LENGTH_SHORT).show();
                                    } else {

                                        Inscriptdialog.dismiss();
                                    }
                                }
                            });
                }


            }
        });

        Inscriptdialog.show();
    }


    //TODO EXPORT DATA TO JSON
    //here is a copy of all the realm objects saved in a single string as JSON...

    private void exportData() {
        //realm.allObjects()
        // Get all realm object and save as json file
        RealmResults<InvoiceRepository> invoiceList = realm.where(InvoiceRepository.class).findAll();

        Invoice[] invoiceArray = new Invoice[invoiceList.size()];
        Log.d("Calculator", "------> Invoice Data Size = " + invoiceList.size());

        for (int i = 0; i < invoiceArray.length; i++) {
            invoiceArray[i] = new Invoice(invoiceList.get(i));
        }
        Log.d("Calculator", "------> Invoice Data Array = " + invoiceArray.length);
////        String json = new Gson().toJson(invoiceArray, Invoice[].class);

//        String json = invoiceArray[0].toJSON();

//        Log.d("Calculator", "------> Invoice Data JSON = " + json);

//        JSONObject main = new JSONObject();
        JSONArray submain = new JSONArray();

        for (int i = 0; i < invoiceArray.length; i++) {
            String json = invoiceArray[i].toJSON();

            submain.put(invoiceArray[i].toJSONObject());

//            Log.d("Calculator", "Invoice json = " + json);
        }
//        main.put(submain);
        Log.d("Calculator Finally ", "Invoice json = " + submain.toString());

        if (submain != null) {
            SaveExportFileto(submain.toString());
        } else {
            //Snackbar.make(mFormulaEditText,getString(R.string.nothingtoexport), Snackbar.LENGTH_SHORT).show();
        }
    }


    private void SaveExportFileto(String datatosave) {


        File file, f = null;
        String filePath = null;

        //use this one instead as its public lets go for it getExternalStoragePublicDirectory()
//        if (android.os.Environment.getExternalStoragePublicDirectory(EnvironmentCompat.MEDIA_UNKNOWN).equals(Environment.MEDIA_MOUNTED))
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), "Weebi/Exports");
            //file = new File(android.os.Environment.getExternalStorageDirectory(), "Weebi/Exports");
            if (!file.exists()) {
                file.mkdirs();
            }

            Realm realm = Realm.getDefaultInstance();
            Boutique maboutique = realm.where(Boutique.class).findFirst();


            filePath = file.getAbsolutePath() + File.separator + "Export_" + maboutique.getNom() + "_" + maboutique.getNumero() + ".json";
            Log.d("local path", " " + filePath);
//            filePath = file.getAbsolutePath() + File.separator + "Export_N_00" + ".json";

            //store the path of the file for later usage ..
//            SharedPreferences preferences=getSharedPreferences(Config.SHAREDPREF_WEEBINAME,MODE_PRIVATE);
//            SharedPreferences.Editor edit = preferences.edit();
//            edit.putString(Config.EXPORT_FILE_PATH,filePath);
//            edit.commit();

            Config.WriteExportPath(Calculator.this, filePath, MODE_PRIVATE);
//             Config.WriteExportPath(Calculator.this,"Export_N_00.txt",MODE_PRIVATE);

            f = new File(filePath);


        } else {
//            Snackbar.make();
        }

        FileOutputStream ostream = null;
        try {
            ostream = new FileOutputStream(f);
            //write on the file
            ostream.write(datatosave.getBytes());

            //time to send the email..
//        Config.SendSomethingViaEmail(filePath,f,Calculator.this);
            Config.SendSomethingViaTelegram(Calculator.this);
//            CanWeSendIt(filePath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void CanWeSendIt(final String path) {
        final AlertDialog.Builder alertbuilder = new AlertDialog.Builder(Calculator.this);
        //alertbuilder.setTitle(getString(R.string.senddata));
        //alertbuilder.setMessage("Ce fichier vous sera envoye par mail");
        alertbuilder.setView(R.layout.dialog_export_view);
        alertbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Config.SendSomethingViaEmail("", path, Calculator.this);
//               shareDocument(new File(path));
//                    Snackbar.make(mFormulaEditText,getString(R.string.exportsucess),Snackbar.LENGTH_SHORT).show();
            }
        }).setIcon(R.drawable.stocks);
        AlertDialog dialog = alertbuilder.create();
        dialog.show();


        //Reptx mute this..
        Button subs = (Button) dialog.findViewById(R.id.subscription);
        subs.setVisibility(View.GONE);
        subs.setText(R.string.telegrambuildertitle);
        subs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://play.google.com/store/apps/details?id=org.telegram.messenger";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
//               Toast.makeText(Calculator.this, "Bientôt disponible!", Toast.LENGTH_LONG).show();
            }
        });

        Button subs2 = (Button) dialog.findViewById(R.id.subscription2);
        subs2.setText(R.string.ajouter_robot_weebi);
        subs.setVisibility(View.GONE);
        subs2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://telegram.me/weebibot";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        //Reptx end mute this...
    }


    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.del) {
            onClear();
            return true;
        }
        return false;
    }

    @Override
    public void onEvaluate(String expr, final String result, int errorResourceId) {
        if (mCurrentState == CalculatorState.INPUT) {
            Log.d("Calculator", "CalculatorState.INPUT");
            mResultEditText.setText(result);
//            if (!mRegisterInv.isEnabled())
//                mRegisterInv.setEnabled(true);
        } else if (errorResourceId != INVALID_RES_ID) {
            Log.d("Calculator", "Invalid_RES_ID");
            onError(errorResourceId);
        } else if (!TextUtils.isEmpty(result)) {
            onResult(result);
            registerInvoice(expr, result);
            Log.d("Calculator", "Result");
        } else if (mCurrentState == CalculatorState.EVALUATE) {
            // The current expression cannot be evaluated -> return to the input state.
            setState(CalculatorState.INPUT);
            if (result != null) {
                registerInvoice(expr, result);
                Log.d("Calculator", "CalculatorState.EVALUATE");
            }
        }

        mFormulaEditText.requestFocus();
    }

// Remove n last characters
// System.out.println(removeLast("Hello!!!333",3));

    public String removeLast(String mes, int n) {
        return mes != null && !mes.isEmpty() && mes.length() > n
                ? mes.substring(0, mes.length() - n) : mes;
    }

    private void registerInvoice(String expr, final String result) {

       /* RealmQuery<Boutique> query = realm.where(Boutique.class);
        Boutique tmp = query.findFirst();

        if(tmp == null){
            //Toast.makeText(getApplicationContext(),"Veuillez ajouter votre devise dans parametres de la boutique",Toast.LENGTH_LONG).show();
        }
        else {
           // madevise=tmp.getDevise().toString();
        }*/


        // deactivate the button to register invoice.
//        mRegisterInv.setEnabled(false);

        // TODO: Rendre les checkings dans le dialog invoice plus precis et l'enregistrement des données
        // aussi
//        Log.d(TAG,"expresion before remove last "+expr);
//        if(expr.endsWith("+") || expr.endsWith("*"))
//        {
//            expr=removeLast(expr,0);
//            Log.d(TAG,"expresion after remove last "+expr);
//
//        }
        String[] parts = expr.split("\\+");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replace(" ", "");
        }

        final View customView = getLayoutInflater().inflate(R.layout.dialog_invoice, null, false);
        final LinearLayout invoiceItemList = (LinearLayout) customView.findViewById(R.id.invoice_item_list);
        final TextView invoiceTotal = (TextView) customView.findViewById(R.id.invoice_total);

        Double dResult = Double.valueOf(result);
        invoiceTotal.setText("");
        //String Total = Utils.getCurrencyFormatter().format(dResult).replace("", "");
        Log.d("result somme", " == " + dResult);
//        invoiceTotal.setText(Utils.getCurrencyFormatter().format(dResult)+" FCFA");

        //RealmQuery<ReferenceRepository> query = realm.where(ReferenceRepository.class).distinct("devise");



       /* if(tmp == null){
            //Toast.makeText(getApplicationContext(),"pas encore de devise",Toast.LENGTH_LONG).show();
        }

        else {*/
        invoiceTotal.setText(dResult + " ");

        computeInvoiceItemList(parts, invoiceItemList);
        /*}*/


        //TODO Q20
        //TODO Add icons Q4 Done
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(customView);
        //builder.setTitle(getString(R.string.Type_references));


        //TODO HERE IS VENTE CASH BUTTON

        builder.setNegativeButton(getString(R.string.select_customer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Only setting up client button label.

            }
        });//.setIcon(R.drawable.add_male_user_128);

        final AlertDialog mainDialog = builder.create();


        mainDialog.show();


        // this is the way to go to add icons to the AlertdialogButtons..
//        Button bPos = mainDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button bNeg = mainDialog.getButton(AlertDialog.BUTTON_NEGATIVE);


        Drawable drawableneg = getResources().getDrawable(R.drawable.id_card);

        // set the bounds to place the drawable a bit right
        drawableneg.setBounds(
                (int) (drawableneg.getIntrinsicWidth() * -0.05),
                0,
                (int) (drawableneg.getIntrinsicWidth() * 0.5),
                (int) (drawableneg.getIntrinsicHeight() * 0.5));

//        bPos.setCompoundDrawables(null,drawablepos,  null, null);
        bNeg.setCompoundDrawables(null, drawableneg, null, null);


        // Cash payment
        mainDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO now we have set up a default client for the

                //TODO HERE IS THE FIX....

                ArrayList<ItemRepository> items = getInvoiceItemList(invoiceItemList);
                realm.beginTransaction();
                InvoiceRepository invoice = new InvoiceRepository();
                int invoiceId = Config.getLastInvoiceId(Calculator.this) + 1;
                Config.setLastInvoiceId(Calculator.this, invoiceId);
                invoice.setId(invoiceId);


                int invoiceTotal = 0;
                for (ItemRepository item : items) {
                    invoiceTotal = invoiceTotal + (item.getQuantity() * item.getUnitPrice());
                }
                invoice = realm.copyToRealmOrUpdate(invoice);
                invoice.setTotal(invoiceTotal);
                invoice.getItems().addAll(items);
                invoice.setDate(Calendar.getInstance().getTime());
                invoice.setInvoiceRepo(true);
                realm.commitTransaction();

                Snackbar.make(mFormulaEditText, R.string.invoice_saved,
                        Snackbar.LENGTH_SHORT).show();


                //TODO Confirmation Vente Cash Q5

                //TODO  Corriger les erreurs Q6

                mainDialog.dismiss();


                //TODO First Pierre Add on
                Intent intent = new Intent(Calculator.this, InvoiceDetailsActivity.class);
                intent.putExtra(InvoiceDetailsActivity.EXTRA_INVOICE_ID, invoice.getId());
                intent.putExtra(InvoiceDetailsActivity.EXTRA_ISARCHIVE, false);
                startActivity(intent);
            }
        });
/*
        mainDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });// ClientRepository payment
*/
        mainDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                RealmResults<ClientRepository> results = realm.where(ClientRepository.class).findAll();


                Listebuilder = new AlertDialog.Builder(Calculator.this);
                View customView = getLayoutInflater().inflate(R.layout.dialog_liste, null, false);

                final ListView laliste = (ListView) customView.findViewById(R.id.laliste);
                final List<ClientRepository> clientRepositories = new ArrayList<>();
                //final ImageView lebout=(ImageView)customView.findViewById(R.id.lbtn);
                AdapterClient adapterClient;


                adapterClient = new AdapterClient(Calculator.this, clientRepositories);
                laliste.setAdapter(adapterClient);

                for (ClientRepository r : results) {
                    ClientRepository cr = new ClientRepository();
                    cr.setNom(r.getNom());
                    cr.setNumero(r.getNumero());
                    cr.setSolde(r.getSolde());
                    clientRepositories.add(cr);

                }
                adapterClient.notifyDataSetChanged();

                if (clientRepositories.size() == 0) {

                    Toast.makeText(getApplicationContext(), getString(R.string.client_need_tobecreated), Toast.LENGTH_LONG).show();
                } else {

                    Listebuilder.setCancelable(true);
                    Listebuilder.setView(customView);
                    Listebuilder.setTitle((getString(R.string.select_customer)));

                    laliste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final String lenum = ((TextView) view.findViewById(R.id.client_phone)).getText().toString();
                            String lenom = ((TextView) view.findViewById(R.id.client_name)).getText().toString();

                            Okbuilder = new AlertDialog.Builder(Calculator.this);
                            View customView = getLayoutInflater().inflate(R.layout.dialog_ok, null, false);

                            final ImageView okbtn = (ImageView) customView.findViewById(R.id.okconf);
                            final TextView nom = (TextView) customView.findViewById(R.id.lenom);
                            final TextView num = (TextView) customView.findViewById(R.id.lenum);
                            nom.setText(lenom);
                            num.setText(lenum);

                            Okbuilder.setCancelable(true);
                            Okbuilder.setView(customView);
                            Okbuilder.setTitle("Confirmer le client");


                            Okdialog = Okbuilder.create();


                            okbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //RegisterRef(nameref, RegisterRefdialog);
                                    ClientRepository selectedClient = realm.where(ClientRepository.class).equalTo("numero", lenum).findFirst();

                                    final ArrayList<ItemRepository> items = getInvoiceItemList(invoiceItemList);
                                    realm.beginTransaction();
                                    InvoiceRepository invoice = new InvoiceRepository();
                                    int invoiceId = Config.getLastInvoiceId(Calculator.this) + 1;

//                                invoice.setId(realm.where(InvoiceRepository.class).max("id").intValue() + 1);

                                    Config.setLastInvoiceId(Calculator.this, invoiceId);
                                    invoice.setId(invoiceId);

                                    int invoiceTotal = 0;
                                    for (ItemRepository item : items) {
                                        invoiceTotal = invoiceTotal + (item.getQuantity() * item.getUnitPrice());
                                    }
                                    invoice = realm.copyToRealmOrUpdate(invoice);
                                    invoice.setTotal(invoiceTotal);
                                    invoice.getItems().addAll(items);
                                    invoice.setInvoiceRepo(true);
                                    invoice.setDate(Calendar.getInstance().getTime());

                                    selectedClient = realm.copyToRealmOrUpdate(selectedClient);
                                    selectedClient.setSolde(selectedClient.getSolde() + (-1 * invoiceTotal));

                                    OperationClientRepository credit = realm.createObject(OperationClientRepository.class);
                                    credit.setClient(selectedClient);
                                    credit.setDate(Calendar.getInstance().getTime());
                                    //here it is a debt so we ad minus 1 to apply it ..
                                    credit.setMontant(-1 * invoiceTotal);
                                    credit.setInvoiceRepository(invoice);
                                    credit.setInvoice(true);
                                    selectedClient.getOperationClients().add(credit);
                                    Log.d(TAG, " nbr operation =" + selectedClient.getOperationClients().size() + " \n\t item "
                                            + invoice.getItems().get(0).getUnitPrice());

                                    invoice.setClient(selectedClient);
                                    realm.commitTransaction();
                                    //rdialog.dismiss();

                                    mainDialog.dismiss();

                                    //TODO  Corriger les erreurs Q6
                                    Snackbar.make(mFormulaEditText, "Facture enregistrée", Snackbar.LENGTH_SHORT).show();

//TODO First Pierre Add on

                                    Intent intent = new Intent(Calculator.this, InvoiceDetailsActivity.class);
                                    intent.putExtra(InvoiceDetailsActivity.EXTRA_INVOICE_ID, invoice.getId());
                                    intent.putExtra(InvoiceDetailsActivity.EXTRA_ISARCHIVE, false);
                                    startActivity(intent);

                                    Okdialog.dismiss();
                                }
                            });

                            Okdialog.show();

                            //Toast.makeText(getApplicationContext(),""+lenum,Toast.LENGTH_LONG).show();

                        /**/

                            Listedialog.dismiss();
                        }
                    });


                    Listedialog = Listebuilder.create();


                    Listedialog.show();


                    //TODO VENTE CLIENT


                    //TODO Confirmation Vente ClientRepository Q5
               /* final int[] clientPosition = {-1};
                final ArrayList<ItemRepository> items = getInvoiceItemList(invoiceItemList);
                final List<ClientRepository> clients = realm.where(ClientRepository.class).findAll();
                final int clientposition;

                AlertDialog.Builder selectUserBuilder = new AlertDialog.Builder(Calculator.this);
                selectUserBuilder.setTitle(R.string.select_customer);

                if (clients.size() > 0)
                {


                    //TODO  Selection et choix ClientRepository Q7
                    View customView = getLayoutInflater().inflate(R.layout.client_list_with, null, false);
                    //View contactListView = getLayoutInflater().inflate(R.layout.client_list_with_search, null);
                    //View contactListView = getLayoutInflater().inflate(R.layout.client_list_with, null);
                   // RealmSearchView mContactList = (RealmSearchView)contactListView.findViewById(R.id.search_client_view);
                    //ListView mContactList = (ListView)contactListView.findViewById(R.id.search_client_view);
                    final ListView mContactList = (ListView)customView.findViewById(R.id.search_client_view);
                    final List<ClientRepository>clientRepositories=new ArrayList<>();
                   /* mContactList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    mContactList.setFocusableInTouchMode(true);

//little bugged keyboard
                    ClearableEditText Edit = (ClearableEditText)mContactList.findViewById(R.id.search_bar);
                    Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    Edit.setMaxLines(1);
                    Edit.setSingleLine(true);*/

                    // ImageView img=(ImageView)customView.findViewById(R.id.lbtn);


                    //TODO reput everything  checked state of all clients to false
                    //ReinitClientCheckedStatus();
                    //TODO end reinitialisation...

                   /* final ClientRecyclerViewAdapter mAdapter =
                            new ClientRecyclerViewAdapter(Calculator.this, realm, "numero");
                    mContactList.setAdapter(mAdapter);
                    RealmResults<ClientRepository> results = realm.where(ClientRepository.class).findAll();

                     AdapterClient adapterClient=new AdapterClient(Calculator.this,clientRepositories);
                    mContactList.setAdapter(adapterClient);

                    for(ClientRepository r: results){
                        ClientRepository cr=new ClientRepository();
                        cr.setNom(r.getNom());
                        cr.setNumero(r.getNumero());
                        cr.setSolde(r.getSolde());
                        clientRepositories.add(cr);

                    }
                    adapterClient.notifyDataSetChanged();


                    Edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            //before anything is changed we first reinit the prev position to -1
                            //and we clear all prev checked elements...
                            mAdapter.ReinitPreviousSelectedClientPos();
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    //selectUserBuilder.setView(contactListView);

                    final AlertDialog rdialog=selectUserBuilder.create();
                    rdialog.show();

                    img.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO lets update this...
////
                           /* ClientRepository selectedClient = adapterClient.getSelectedClient(); //clients.get(clientPosition[0]);

                            if (selectedClient != null)
                            {
                                realm.beginTransaction();
                                InvoiceRepository invoice = new InvoiceRepository();
                                int invoiceId = Config.getLastInvoiceId(Calculator.this) + 1;

//                                invoice.setId(realm.where(InvoiceRepository.class).max("id").intValue() + 1);

                                Config.setLastInvoiceId(Calculator.this, invoiceId);
                                invoice.setId(invoiceId);

                                int invoiceTotal = 0;
                                for (ItemRepository item : items) {
                                    invoiceTotal = invoiceTotal + (item.getQuantity() * item.getUnitPrice());
                                }
                                invoice = realm.copyToRealmOrUpdate(invoice);
                                invoice.setTotal(invoiceTotal);
                                invoice.getItems().addAll(items);
                                invoice.setInvoiceRepo(true);
                                invoice.setDate(Calendar.getInstance().getTime());

                                selectedClient = realm.copyToRealmOrUpdate(selectedClient);
                                selectedClient.setSolde(selectedClient.getSolde() + (-1 * invoiceTotal));

                                OperationClientRepository credit = realm.createObject(OperationClientRepository.class);
                                credit.setClient(selectedClient);
                                credit.setDate(Calendar.getInstance().getTime());
                                //here it is a debt so we ad minus 1 to apply it ..
                                credit.setMontant(-1 * invoiceTotal);
                                credit.setInvoiceRepository(invoice);
                                credit.setInvoice(true);
                                selectedClient.getOperationClients().add(credit);
                                Log.d(TAG," nbr operation ="+selectedClient.getOperationClients().size()+" \n\t item "
                                        +invoice.getItems().get(0).getUnitPrice());

                                invoice.setClient(selectedClient);
                                realm.commitTransaction();
                                rdialog.dismiss();

                                mainDialog.dismiss();

                                //TODO  Corriger les erreurs Q6
                                Snackbar.make(mFormulaEditText, R.string.facture_enregistree, Snackbar.LENGTH_SHORT).show();

//TODO First Pierre Add on

                                Intent intent = new Intent(Calculator.this, InvoiceDetailsActivity.class);
                                intent.putExtra(InvoiceDetailsActivity.EXTRA_INVOICE_ID, invoice.getId());
                                intent.putExtra(InvoiceDetailsActivity.EXTRA_ISARCHIVE,false);
                                startActivity(intent);


                            }
                            else
                            //TODO This snack bar does not appear on the right alert dialog
                            {
                                Snackbar.make(invoiceItemList, R.string.select_customer, Snackbar.LENGTH_SHORT).show();
                            }


                        }
                    });


                    //TODO this is the way to go to add icons to the AlertdialogButtons..

                } else
                {
                    Snackbar.make(invoiceItemList,getString(R.string.client_need_tobecreated),
                            Snackbar.LENGTH_SHORT).show();
                   // Toast.makeText(Calculator.this,getString(R.string.client_need_tobecreated),Toast.LENGTH_SHORT).show();




                }*/

                }
            }
        });

        mainDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (realm.isInTransaction())
                    realm.cancelTransaction();
            }
        });


    }


    public void ReinitClientCheckedStatus() {
        // get all clients ...
        RealmResults<ClientRepository> tmpclients = realm.where(ClientRepository.class).findAll();

        realm.beginTransaction();

        for (int i = 0; i < tmpclients.size(); i++) {
            tmpclients.get(i).setChecked(false);
        }

        realm.commitTransaction();

    }


    //update database by loading from file

    private void loadDataFromFile() {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);

        // fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType("*/*");
        startActivityForResult(fileIntent, Picker.PICK_FILE);
    }

    private void loadDataFromFile(String path) {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);

        // fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType("*/*");
        startActivityForResult(fileIntent, Picker.PICK_FILE);
    }

    //this method help extract the right items ...
    private void computeInvoiceItemList(String[] parts, final LinearLayout invoiceItemList) {
        Symbols mSymbols = new Symbols();
        arrayString = new ArrayList<>();
        listref = new ArrayList<>();
        // read the content of the preferences database
        String preferenceContent = prefs.getString(DATABASE_PREFERENCES_LABEL, "");
        if (!preferenceContent.equals("")) {
            StringTokenizer s = new StringTokenizer(preferenceContent, "|");
            while (s.hasMoreTokens()) {
                arrayString.add(s.nextToken());
            }

            //  Log.e("TEST", arrayString.toString());

        }


        //  ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(Calculator.this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.database));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Calculator.this, android.R.layout.simple_spinner_dropdown_item, arrayString);

        for (int i = 0; i < parts.length; i++) {
            ItemRepository item = new ItemRepository();
            String part = parts[i];
            Double unitPrice = 0.0;
            Double quantity = 1.0;
            if (!TextUtils.isEmpty(part)) {
                if (part.contains("*")) {
                    String[] subPart = part.split("\\*", 2);
                    if (subPart.length == 2) {
                        if (subPart[0].contains("+") || subPart[0].contains("-") || subPart[0].contains("/")) {
                            try {
                                unitPrice = mSymbols.eval(subPart[0]);
                            } catch (SyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            unitPrice = Double.valueOf(subPart[0]);
                        }

                        if (subPart[1].contains("+") || subPart[1].contains("-") || subPart[1].contains("/")) {
                            try {
                                quantity = mSymbols.eval(subPart[1]);
                            } catch (SyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            quantity = Double.valueOf(subPart[1]);
                        }
                    }
                } else if (part.contains("-") || part.contains("/")) {
                    try {
                        unitPrice = mSymbols.eval(part);
                    } catch (SyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    unitPrice = Double.valueOf(part);
                }
            }

            item.setUnitPrice(unitPrice.intValue());
            item.setQuantity(quantity.intValue());
            item.setTotalPrice(unitPrice.intValue() * quantity.intValue());
            int itemId = Config.getLastItemId(Calculator.this) + 1;
            item.setId(itemId);
            Config.setLastItemId(Calculator.this, itemId);
            //Item realmItem = realm.copyToRealmOrUpdate(item);


            //TODO here we deal with the total of invoice update depending on the unit price changes or price changes..
            //TODO redo calculation...
            View itemView = getLayoutInflater().inflate(R.layout.invoice_item, null, false);
            //TODO automcomplete text...
            // Spinner spinneref = (Spinner) itemView.findViewById(R.id.choixref);


            //pisco recuperation de la liste des références et ajout dans arraylist listref
            RealmResults<ReferenceRepository> results = realm.where(ReferenceRepository.class).findAll();

            listref = new ArrayList<>();

            for (ReferenceRepository r : results) {

                //Toast.makeText(getApplicationContext(), ""+r.getNomref(),Toast.LENGTH_LONG).show();
                listref.add(r.getNomref());


            }
            //Toast.makeText(getApplicationContext(), ""+listref,Toast.LENGTH_LONG).show();


            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listref);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // spinneref.setAdapter(adapter1);
            //Toast.makeText(getApplicationContext(), ""+text,Toast.LENGTH_LONG).show();

            AutoCompleteTextView itemLabel = (AutoCompleteTextView) itemView.findViewById(R.id.item_label);
            itemLabel.setAdapter(adapter1);
            EditText itemUnitPrice = (EditText) itemView.findViewById(R.id.item_unit_price);
            //add listener here
            EditText itemQuantity = (EditText) itemView.findViewById(R.id.item_quantity);
            //add another item "object" one over here too
            itemView.setTag(item);

            itemQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

//                    invoiceItemList.getChildCount();
//                    invoiceItemList.findViewById(R.id.)
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            itemUnitPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            itemUnitPrice.setText(String.valueOf(item.getUnitPrice()));
            itemQuantity.setText(String.valueOf(item.getQuantity()));
            invoiceItemList.addView(itemView);
        }
    }

    // this method help feed the items builds before ..
    private ArrayList<ItemRepository> getInvoiceItemList(final LinearLayout invoiceItemList) {
        //Invoice invoice = new Invoice();
        //Invoice realmInvoice = null;
        //int total = 0;
        ArrayList<ItemRepository> items = new ArrayList<>();

        Log.d("Calculator", "Invoice Items = " + invoiceItemList.getChildCount());
        for (int i = 0; i < invoiceItemList.getChildCount(); i++) {
            View itemView = invoiceItemList.getChildAt(i);
            ItemRepository item = (ItemRepository) itemView.getTag();
            String itemLabel = ((EditText) itemView.findViewById(R.id.item_label))
                    .getText().toString();

            // Spinner spinneref = (Spinner) itemView.findViewById(R.id.choixref);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listref);
            //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
            // spinneref.setAdapter(adapter1);

            int itemUnitPrice = Integer.parseInt(((EditText) itemView.findViewById(R.id.item_unit_price))
                    .getText().toString());


            int itemQuantity = Integer.parseInt(((EditText) itemView.findViewById(R.id.item_quantity))
                    .getText().toString());


            int itemPrice = itemUnitPrice * itemQuantity;

            //String string=spinneref.getSelectedItem().toString();


            //itemLabel=string;

            item.setName(itemLabel);
            //item.setLaref(string);
            item.setUnitPrice(itemUnitPrice);
            item.setQuantity(itemQuantity);
            item.setTotalPrice(itemPrice);

            items.add(item);
        }
        return items;
    }

    @Override
    public void onTextSizeChanged(final TextView textView, float oldSize) {
        if (mCurrentState != CalculatorState.INPUT) {
            // Only animate text changes that occur from user input.
            return;
        }

        // Calculate the values needed to perform the scale and translation animations,
        // maintaining the same apparent baseline for the displayed text.
        final float textScale = oldSize / textView.getTextSize();
        final float translationX = (1.0f - textScale) *
                (textView.getWidth() / 2.0f - ViewCompat.getPaddingEnd(textView));
        final float translationY = (1.0f - textScale) *
                (textView.getHeight() / 2.0f - textView.getPaddingBottom());

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(textView, "scaleX", textScale, 1.0f),
                ObjectAnimator.ofFloat(textView, "scaleY", textScale, 1.0f),
                ObjectAnimator.ofFloat(textView, "translationX", translationX, 0.0f),
                ObjectAnimator.ofFloat(textView, "translationY", translationY, 0.0f));
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    private void onEquals() {
        //if (mCurrentState == CalculatorState.INPUT) {
        Log.d("Calculator", "OnEquals expr = " + mFormulaEditText.getText());
        setState(CalculatorState.EVALUATE);
        mEvaluator.evaluate(mFormulaEditText.getText(), this);
        //}
    }

    private void onDelete() {
        // Delete works like backspace; remove the last character from the expression.
        final Editable formulaText = mFormulaEditText.getEditableText();
        final int formulaLength = formulaText.length();
        // verifier si le dernier text est un espace vide
        if (formulaLength > 0) {
            if (formulaText.charAt(formulaLength - 1) == ' ') {
                int position = formulaLength - 1;
                while (position > 0 && (formulaText.charAt(position) == ' ' || formulaText.charAt(position) == '\n')) {
                    position--;
                }
                formulaText.delete(position, formulaLength);
            } else {
                formulaText.delete(formulaLength - 1, formulaLength);
            }
        }
    }

    abstract void cancelAnimation();

    abstract void reveal(View sourceView, int colorRes, AnimatorListenerWrapper listener);

    private void onClear() {
        if (TextUtils.isEmpty(mFormulaEditText.getText())) {
            return;
        }

        final View sourceView = mClearFab.getVisibility() == VISIBLE
                ? mClearFab : mDelFab;
        reveal(sourceView, R.color.calculator_accent_color, new AnimatorListenerWrapper() {
            @Override
            public void onAnimationStart() {
                mFormulaEditText.getEditableText().clear();
            }
        });
    }

    private void onError(final int errorResourceId) {
        mRegisterInv.setEnabled(false);
        if (mCurrentState != CalculatorState.EVALUATE) {
            // Only animate error on evaluate.
            mResultEditText.setText(errorResourceId);
            return;
        }

        reveal(mRegisterInv, R.color.calculator_error_color, new AnimatorListenerWrapper() {
            @Override
            public void onAnimationStart() {
                setState(CalculatorState.ERROR);
                mResultEditText.setText(errorResourceId);
            }
        });
    }

    abstract void onResult(final String result);

    private void shareDocument(final File inFile) {
        // let the FileProvider generate an URI for this private file
        final Uri uri = FileProvider.getUriForFile(this, "com.weebi.fileprovider", inFile);
        // create an intent, so the user can choose which application he/she wants to use to share this file
        final Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setSubject("subject")
                .setStream(uri)
                .setChooserTitle("which one to choose")
                .createChooserIntent()
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        this.startActivity(intent);
    }

    //telegram
    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {
                    share.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    share.putExtra(Intent.EXTRA_TEXT, "your text");
//                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(myPath)) ); // Optional, just if you wanna share an image.
//                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;

            startActivity(Intent.createChooser(share, "Select"));
        }
    }

    void intentMessageTelegram(String msg) {
        final String appName = "org.telegram.messenger";
        final boolean isAppInstalled = isAppAvailable(Calculator.this, Config.TelegramappName);
        if (isAppInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.setPackage(appName);
            myIntent.putExtra(Intent.EXTRA_TEXT, msg);//
            Calculator.this.startActivity(Intent.createChooser(myIntent, "Share with"));
        } else {
            Toast.makeText(Calculator.this, R.string.telegram_not_installed, Toast.LENGTH_SHORT).show();
        }
    }

    protected enum CalculatorState {
        INPUT, EVALUATE, RESULT, ERROR
    }

    private class ClientSingleChoiceListAdapter extends BaseAdapter {

        private List<ClientRepository> clients = null;
        private String mSelectedClientNumero = null;
        private ClientRepository mSelectedClient;

        public ClientSingleChoiceListAdapter(List<ClientRepository> clientList) {
            clients = clientList;
        }

        @Override
        public int getCount() {
            if (clients != null)
                return clients.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return clients.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public ClientRepository getSelectedClient() {
            return mSelectedClient;
        }

        public void setSelectedClient(String numero) {
            mSelectedClientNumero = numero;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.spinner_item_client, null, false);
            }

            TextView clientName = (TextView) convertView.findViewById(R.id.client_name);
            TextView clientPhone = (TextView) convertView.findViewById(R.id.client_phone);
            RadioButton clientSelected = (RadioButton) convertView.findViewById(R.id.client_selected_button);
//            CheckBox clientSelected = (CheckBox) convertView.findViewById(R.id.client_selected_button);

            clientName.setText(clients.get(i).getNom());
            clientPhone.setText(clients.get(i).getNumero());

            clientSelected.setChecked(clients.get(i).getNumero().equals(mSelectedClientNumero));
            if (clientSelected.isChecked())
                mSelectedClient = clients.get(i);
            return convertView;
        }
    }



   /* public static void SendSomethingViaEmail(String whattoSend,String filetosend,Context ctx)
    {
        Realm realm = Realm.getDefaultInstance();

        Boutique maboutique= realm.where(Boutique.class).findFirst();

        File imagePath = new File(ctx.getExternalCacheDir(), "exportpath");
        File newFile = new File(imagePath, filetosend);
        Uri contentUri = FileProvider.getUriForFile(ctx, "com.weebi.fileprovider", newFile);
//        Uri contentUri = getUriForFile(getContext(), "com.mydomain.fileprovider", newFile);




        final String appName = "org.telegram.messenger";
        Uri destinationUri = Uri.parse("https://telegram.me/weebibot");
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("application/octet-stream");
        email.putExtra(Intent.EXTRA_STREAM, contentUri);
        email.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        email.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        email.setData(destinationUri);
        email.setPackage(appName);
        email.putExtra(Intent.ACTION_VIEW, destinationUri);
        if(maboutique != null)
        {
            email.putExtra(Intent.EXTRA_SUBJECT,ctx.getString(R.string.data_chart) +
                    ctx.getString(R.string.mail_sent_by)+maboutique.getNom()+ ctx.getString(R.string.phone_number)+maboutique.getNumero());
        }

//        ctx.startActivity(Intent.createChooser(email, "Choisissez weebibot dans Telegram pour une analyse sur mesure."));
        ctx.startActivity(email);
    }*/

    public class ClientRecyclerViewAdapter extends RealmSearchAdapter<ClientRepository, ClientRecyclerViewAdapter.ViewHolder> {

        int previousSelectedClientPos = -1;

        public ClientRecyclerViewAdapter(Context context, Realm realm, String filterColumnName) {
            super(context, realm, filterColumnName);

        }

        //this is to help us reinit the values...
        public void SetItemList(ClientRepository client) {
            int size = ReinitClientCheckedStatus();

            if (client != null) {
                realm.beginTransaction();
                client.setChecked(true);
                realm.commitTransaction();
            }

            // get all clients ...
//            RealmResults<ClientRepository> setclients =  realm.where(ClientRepository.class).findAll();

            notifyItemRangeChanged(0, size);
        }

        public void ReinitPreviousSelectedClientPos() {
            ReinitClientCheckedStatus();
            previousSelectedClientPos = -1;
        }

        public int ReinitClientCheckedStatus() {
            // get all clients ...
            RealmResults<ClientRepository> tmpclients = realm.where(ClientRepository.class).findAll();

            if ((tmpclients != null) && (tmpclients.size() > 0)) {
                realm.beginTransaction();
                for (int i = 0; i < tmpclients.size(); i++) {
                    tmpclients.get(i).setChecked(false);
                }

                realm.commitTransaction();


                return tmpclients.size();
            }

            return 0;

        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
            ViewHolder vh = new ViewHolder(new ClientItemView(viewGroup.getContext()));
            return vh;
        }

        public ClientRepository getSelectedClient() {
            if (previousSelectedClientPos != -1)
                return realmResults.get(previousSelectedClientPos);
            return null;
        }

        @Override
        public void onBindRealmViewHolder(final ViewHolder viewHolder, final int pos) {
            final ClientRepository client = realmResults.get(pos);
            viewHolder.clientItemView.bind(client);


            viewHolder.clientItemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    ClientRepository prevclient = null;

                    if (previousSelectedClientPos != -1) {
                        prevclient = realmResults.get(previousSelectedClientPos);
                    }

                    ClientRepository actualclient = realmResults.get(pos);

                    Log.d(TAG, "Clicked on a contact at pos = " + pos);

                    if (previousSelectedClientPos != -1) {

                        Log.d(TAG, "previous selected pos = " + previousSelectedClientPos);

                        //if it was already selected...
                        if (realmResults.get(previousSelectedClientPos).isChecked() == true) {
                            //if it's the same pos
                            if (previousSelectedClientPos == pos) {
//                                realmResults.get(previousSelectedClientPos).isChecked = false;

                                // get the actual client info.
                                ClientRepository tmpactualclient = realm.where(ClientRepository.class).equalTo("numero", actualclient.getNumero()).findFirst();

                                realm.beginTransaction();
//                                    actualclient.setChecked(false);
                                tmpactualclient.setChecked(false);
//                                    realm.copyToRealmOrUpdate(actualclient);
                                realm.copyToRealmOrUpdate(tmpactualclient);
                                realm.commitTransaction();
//                                    notifyItemRangeChanged(0,realmResults.size());
                            }
                            //if it's not the same pos

                            else {
//                                realmResults.get(previousSelectedClientPos).isChecked = false;
//                                realmResults.get(pos).isChecked = true;

                                // get the actual client info.
                                ClientRepository tmpactualclient = realm.where(ClientRepository.class).equalTo("numero", actualclient.getNumero()).findFirst();
                                ClientRepository tmpprevclient = realm.where(ClientRepository.class).equalTo("numero", prevclient.getNumero()).findFirst();

                                realm.beginTransaction();
//                                    prevclient.setChecked(false);
                                tmpprevclient.setChecked(false);
//                                    actualclient.setChecked(true);
                                tmpactualclient.setChecked(true);
//                                    realm.copyToRealmOrUpdate(actualclient);
                                realm.copyToRealmOrUpdate(tmpactualclient);
//                                    realm.copyToRealmOrUpdate(prevclient);
                                realm.copyToRealmOrUpdate(tmpprevclient);
                                realm.commitTransaction();

                                previousSelectedClientPos = pos;
                            }

                            notifyItemRangeChanged(0, realmResults.size());

                        }

                    } else {
                        //first time we choose
                        previousSelectedClientPos = pos;
                        // get the actual client info.
                        ClientRepository tmpactualclient = realm.where(ClientRepository.class).equalTo("numero", actualclient.getNumero()).findFirst();
//                        ClientRepository tmpprevclient =  realm.where(ClientRepository.class).equalTo("numero", prevclient.getNumero()).findFirst();

                        realm.beginTransaction();
//                        actualclient.setChecked(true);
                        tmpactualclient.setChecked(true);
                        realm.copyToRealmOrUpdate(tmpactualclient);
//                        realm.copyToRealmOrUpdate(actualclient);
                        realm.commitTransaction();
//                        realmResults.get(pos).isChecked = true;
//TODO we are supposed to take action here...
                        SetItemList(tmpactualclient);
//                        notifyItemRangeChanged(0,realmResults.size());
                    }


//TODO DO REDO IF INSIDE NIT WORKING
//                    notifyItemRangeChanged(0,realmResults.size());


                }
            });


        }

        public class ViewHolder extends RealmSearchViewHolder {
            private final ClientItemView clientItemView;

            public ViewHolder(ClientItemView itemView) {
                super(itemView);
                this.clientItemView = itemView;
            }
        }
    }

    public class ClientItemView extends RelativeLayout {

        @Bind(R.id.client_name)
        TextView name;

        @Bind(R.id.client_solde)
        TextView solde;

        @Bind(R.id.client_phone)
        TextView phone;

        @Bind(R.id.client_selected_button)
//        CheckBox client_radio_selected_button;
                RadioButton client_radio_selected_button;

        public ClientItemView(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            inflate(context, R.layout.spinner_item_client, this);
            ButterKnife.bind(this);
        }

        public void bind(ClientRepository client) {
            name.setText(client.getNom());
            solde.setText(String.valueOf(client.getSolde()));
            phone.setText(client.getNumero());

            if (client.isChecked()) {
                client_radio_selected_button.setChecked(true);
                Log.d("redo work", "client is checked");
            } else {
                client_radio_selected_button.setChecked(false);
                Log.d("redo work", "client is not checked");
            }

        }
    }


    // private String[] languages = {getString(R.string.choix_langue),getString(R.string.anglais), getString(R.string.français), getString(R.string.arabe)};
}

