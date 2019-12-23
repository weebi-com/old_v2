package com.weebinatidi.ui.weebi2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kobakei.ratethisapp.RateThisApp;
import com.squareup.picasso.Picasso;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;
import com.weebinatidi.bluetooth.AddPrinterActivity;
import com.weebinatidi.model.Client;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.Invoice;
import com.weebinatidi.model.LaReffb;
import com.weebinatidi.ui.ClientListActivitySQL;
import com.weebinatidi.ui.ClientPageActivityNew2SQL;
import com.weebinatidi.ui.ImprimeText;
import com.weebinatidi.ui.Usbimprime;
import com.weebinatidi.utils.PrefManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.view.View.VISIBLE;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCashInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printCustomerInvoiceHeaderFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printItemFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printThankMsgFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor58Mm.printTotalAmountFor58Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCashInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printCustomerInvoiceHeaderFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printItemFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printThankMsgFor80Mm;
import static com.weebinatidi.ui.print.InvoiceTemplateFor80Mm.printTotalAmountFor80Mm;
import static com.weebinatidi.ui.print.PrintDemo.textToBePrinted;
import static com.weebinatidi.utils.Notification.showAlertConfigureShop;
import static com.weebinatidi.utils.SharedPrefUtils.isPreferenceFileExist;
import static com.weebinatidi.utils.ValidatorUtils.cutAStringAndAddSpaceAfterText;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor58Mm;
import static com.weebinatidi.utils.ValidatorUtils.nameWidthLimitFor80Mm;

public class NewInterface extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String EXTRA_MESSAGE = "LastIdInvoice";
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    public String urlpis = "https://www.weebi.com/piscoandroid/lecturedatajson.php";
    FilePicker filepicker;
    FilePickerCallback filePickercallback;
    Boolean SDcard ;
    PrefManager prefManager;
    String idcli;
    List<String> references = new ArrayList<>();
    List<String> resultats = new ArrayList<>();
    List<LaReffb> referencesadapt = new ArrayList<>();
    List<LaReffb> resultatsadapt = new ArrayList<>();
    List<Client> lesclientsadap = new ArrayList<>();
    List<Invoice> lesfact = new ArrayList<>();
    List<Itemstring> lesitems = new ArrayList<>();
    List<Client> lesclientliste = new ArrayList<>();
    ArrayList<String> referencesitems = new ArrayList<>();
    List<String> referencesserv = new ArrayList<>();
    ListView listeref;
    Switch drawerSwitch;
    RelativeLayout facturer;
    AlertDialog.Builder RegisterSupbuilder;
    AlertDialog RegisterSupDialog;
    AlertDialog.Builder RegisterItembuilder;
    AlertDialog RegisterItemDialog;
    AlertDialog.Builder RegisterClientbuilder;
    AlertDialog RegisterClientDialog;
    AlertDialog.Builder RegisterCodebuilder;
    AlertDialog RegisterCodeDialog;
    DbHelper dbHelper;
    ArrayAdapter<String> arrayAdapter;
    //List<String> clients;
    AlertDialog.Builder Listebuilder;
    AlertDialog Listedialog;
    AlertDialog.Builder Okbuilder;
    AlertDialog Okdialog;
    AlertDialog.Builder Okbuilderclient;
    AlertDialog Okdialogclient;
    AlertDialog.Builder Okbuildercash;
    AlertDialog Okdialogcash;
    AlertDialog.Builder Reductbuilder;
    AlertDialog Reductdialog;
    ImageView cahier, refs, param, imprime;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    SharedPreferences sharedPreferences;
    String idlast = "";
    int lesolde;
    String file_url_path = null;
    ArrayList<String> clientsrestaure = new ArrayList<>();
    ArrayList<LaReffb> results = new ArrayList<>();
    String email = "";
    String passe = "";
    DrawerLayout drawer;
    String Images=".photos";
    //Button bleu,vert, jaune,rouge,blanc;
    private WeebiApplication context;
    //private AdapterClient adapterClient;
    private String lenumero = "";
    private String leclient = "";
    // private String recuclicash="";
    private ListView listeitem;
    private ListView listeclient;
    private ListView listeclientaffiche;
    private AdapterReference adapterReference;
    private AdapterReferenceItems adapterReferenceitems;
    private List<Integer> lesnumeros = new ArrayList<>();
    private List<String> lesnomscli = new ArrayList<>();
    private List<String> lesrefsearch = new ArrayList<>();
    private AutoCompleteTextView textView;
    private EditText textView2, textview3;
    private List<Itemstring> items = new ArrayList<>();
    private List<String> itemsstring = new ArrayList<>();
    private LinearLayout choixcliline;
    private TextView txtResult, totalapayer, leclientchoisi, modepaiement, nok, choixcli, reduire; // Reference to EditText of result
    //private double result = 0;     // Result of computation
    private String inStr = "0";
    private Button zero, un, deux, trois, quatre, cinq, six, sept, huit, neuf, doublezero, virgule, clear, ok, valider;
    private Button a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, unl, deuxl, troisl, quatrel, cinql, sixl, septl, huitl, neufl, zerol;
    private Button blisteclient, cleartext, espace, blistereference, bstatistique, bparam, tiret8;
    private String laref = "", prix = "", quantite = "", decimal = ".";
    private boolean clic = false;
    private int lanote = 0;
    private List<Client> lesclients = new ArrayList<>();
    private String langue = "";
    private String leboutiqnum;
    private String leboutiqnam;
    private String leboutiqcode;
    private String leboutiqmail;
    private String boutiqcode;
    private List<Itemstring> itemslast = new ArrayList<>();
    private FirebaseAuth auth;
    //String uniqueID;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String IDLOT = null;
    private static final String PREF_IDLOT = "PREF_IDLOT";

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

    @SuppressLint({"WrongConstant", "WrongViewCast"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apres 3 jour d'utilisation et 5 lancement demandez 5 etoiles au client.
        final String appPackageName = getPackageName(); // package name of the app
        RateThisApp.Config configRating = new RateThisApp.Config(3, 5);
        configRating.setUrl("https://play.google.com/store/apps/details?id=" + appPackageName);
        RateThisApp.init(configRating);

        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this);
        // Show a dialog if criteria is satisfied
        RateThisApp.showRateDialogIfNeeded(this);

        //uniqueID = UUID.randomUUID().toString();
        //Toast.makeText(getApplicationContext(), ""+androidId, Toast.LENGTH_LONG).show();
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = this.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }

        if (IDLOT == null) {
            SharedPreferences sharedPrefs = this.getSharedPreferences(
                    PREF_IDLOT, Context.MODE_PRIVATE);
            IDLOT = sharedPrefs.getString(PREF_IDLOT, null);
            if (IDLOT == null) {
                IDLOT = "001";
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_IDLOT, IDLOT);
                editor.commit();
            }
        }

        WeebiApplication.getInstance().getApplicationContext();
        context = (WeebiApplication) getApplicationContext();


        //fixer l'activité dans reverse de l'application
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);


        //super.onBackPressed();
        //on vérifie la taille, si c'est une tablette on passe en paysage sinon on reste en portrait
        /*if (isTablet(this) || isTablet7(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        } else if (isSmart(this) || isSmartsmall(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(getApplicationContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
        }*/

        //Toast.makeText(getApplicationContext(),""+referencesserv,Toast.LENGTH_SHORT).show();

        // CONTROLE DE LA VERSION ANDROID
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
        } else {

            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
            }
        }

        //création du répertoire de weebi pour la base de donnée et la sauvegarde
        File file,file1 = null;
        String filePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), "Weebi/Fichier");
            file1 = new File(Environment.getExternalStorageDirectory(), "Weebi/"+Images);
            if (!file.exists()) {
                file.mkdirs();
                file1.mkdirs();
            }
        }


        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // bleu=findViewById(R.id.bleu);
        //vert=findViewById(R.id.vert);
        //jaune=findViewById(R.id.jaune);
       // rouge=findViewById(R.id.rouge);
       // blanc=findViewById(R.id.blanc);



        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // initiate a Switch
        drawerSwitch = findViewById(R.id.drawer_switch);

        auth = FirebaseAuth.getInstance();
        /*Account[] accounts = AccountManager.get(getApplicationContext()).getAccountsByType("com.google");
        for (Account account : accounts) {

            email = account.name.toString();
            //Toast.makeText(getApplicationContext(),""+email,Toast.LENGTH_SHORT).show();
        }*/

        passe = "weebiéà&è";
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {


            /*auth.signInWithEmailAndPassword(email, passe)
                    .addOnCompleteListener(NewInterface.this, new OnCompleteListener<AuthResult>() {
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


                            }
                        }
                    });

            auth.createUserWithEmailAndPassword(email, passe)
                    .addOnCompleteListener(NewInterface.this, new OnCompleteListener<AuthResult>() {
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

                            }
                        }
                    });*/
        }

        final String codePattern = "[a-zA-Z]";
        dbHelper = new DbHelper(this);

        Cursor bouti=dbHelper.getBoutique("1");
        if(bouti.moveToFirst()){
            //Toast.makeText(getApplicationContext(),""+bouti.getString(4),Toast.LENGTH_LONG).show();
            leboutiqnum=bouti.getString(1);
            leboutiqnam=bouti.getString(2);
            leboutiqmail=bouti.getString(3);
            leboutiqcode=bouti.getString(4);
            //Toast.makeText(getApplicationContext(),"le code "+bouti.getString(3),Toast.LENGTH_SHORT).show();
            if (leboutiqcode.equals("") || leboutiqcode.equals(null)){
                Toast.makeText(getApplicationContext(),"le code par défaut est 0000",Toast.LENGTH_LONG).show();
                dbHelper.majcode("0000","1");
            }
            if(leboutiqcode.contains("@") || codePattern.matches(leboutiqcode)){
                Toast.makeText(getApplicationContext(),"code pas bon : "+leboutiqcode,Toast.LENGTH_SHORT).show();
                dbHelper.majcode("0000","1");
            }
        }



        //Crashlytics.getInstance().crash();
        //Fabric.with(fabric);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        facturer = findViewById(R.id.relative);
        choixcli = findViewById(R.id.choixcli);
        reduire = findViewById(R.id.reduire);
        choixcliline = findViewById(R.id.choixcliline);

        //permettre de mettre toutes les références à zéro
        final String ladate = Config.getFormattedDate(Calendar.getInstance().getTime());
        dbHelper.updateRefqteall("0", ladate);
        resultats = dbHelper.getAllRefName();
        resultatsadapt = dbHelper.getAllReference();
        //lesrefsearch=dbHelper.getAllRefName();
        lesclients = dbHelper.getAllClient();
        // lesclientsadap=dbHelper.getAllClient();
        //clients=dbHelper.getAllCliName();

        //cahier=(ImageView)findViewById(R.id.list_client1);
        //refs=(ImageView)findViewById(R.id.lesrefs);
        //param=(ImageView)findViewById(R.id.shop1);
        imprime = findViewById(R.id.print1id);
        // heure=(TextView)findViewById(R.id.dateheureder);
        // totallast=(TextView)findViewById(R.id.totaldernierefact);

        modepaiement = findViewById(R.id.modepayement);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getBaseContext().getSharedPreferences("prefes", MODE_PRIVATE);

        filepicker = new FilePicker(NewInterface.this);

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

        if (sharedPreferences.contains("preflangue")) {
            langue = sharedPreferences.getString("preflangue", null);
            Configuration config = new Configuration();
            Locale locale;
            locale = new Locale(langue);
            config.locale = locale;
            getResources().updateConfiguration(config, null);

        } else {
        }



        imprime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getLastInvoiceR();
                if (cursor.moveToFirst()) {
                    String idrecup = cursor.getString(0);
                    String date = cursor.getString(1);
                    String totale = cursor.getString(2);
                    String idclie = cursor.getString(3);
                    String invoiceType = cursor.getString(4); // Recupere le type de facture.

                    String nomclient = "";
                    String numclient = "";
                    String solde = "";
                    Cursor cursor1 = dbHelper.getClient(idclie);
                    if (cursor1.moveToFirst()) {
                        nomclient = cursor1.getString(2);
                        numclient = cursor1.getString(1);
                        solde = cursor1.getString(3);
                    }
                    int total = Integer.valueOf(totale);
                    Cursor bouti = dbHelper.getBoutique("1");
                    if (bouti.moveToFirst() == true) {
                        String nomboutique = bouti.getString(2);
                        String numboutique = bouti.getString(1);

                        if (isPreferenceFileExist("regoprintdemo",getApplicationContext())) {

                            SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
                            String nameBT = prefs.getString("BT_DEVICE_NAME", null);

                            if (invoiceType.equals("cash") || invoiceType.equals("credit")) {
                                if (nameBT.contains("58")) {
                                    textToBePrinted = printCustomerInvoiceHeaderFor58Mm(nomboutique, numboutique, nomclient, numclient, Integer.parseInt(solde), date, Integer.parseInt(idrecup));
                                    Cursor cur = dbHelper.getItemIdInvoice(Integer.valueOf(idrecup));
                                    while (cur.moveToNext()) {
                                        Itemstring item = new Itemstring();
                                        item.setName(cur.getString(1));
                                        item.setUnitPrice(cur.getString(2));
                                        item.setQuantity(cur.getString(3));
                                        itemslast.add(item);
                                    }
                                    for (int i = 0; i < itemslast.size(); i++) {
                                        String nom = itemslast.get(i).getName();
                                        String prixunit = itemslast.get(i).getUnitPrice();
                                        String quantite = itemslast.get(i).getQuantity();
                                        textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                    }
                                    textToBePrinted += printTotalAmountFor58Mm(total);
                                    textToBePrinted += printThankMsgFor58Mm();
                                } else if (nameBT.contains("80")) {
                                    textToBePrinted = printCustomerInvoiceHeaderFor80Mm(nomboutique, numboutique, nomclient, numclient, date, Integer.parseInt(solde), Integer.parseInt(idrecup));
                                    Cursor cur = dbHelper.getItemIdInvoice(Integer.valueOf(idrecup));
                                    while (cur.moveToNext()) {
                                        Itemstring item = new Itemstring();
                                        item.setName(cur.getString(1));
                                        item.setUnitPrice(cur.getString(2));
                                        item.setQuantity(cur.getString(3));
                                        itemslast.add(item);
                                    }
                                    for (int i = 0; i < itemslast.size(); i++) {
                                        String nom = itemslast.get(i).getName();
                                        String prixunit = itemslast.get(i).getUnitPrice();
                                        String quantite = itemslast.get(i).getQuantity();
                                        textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                                    }
                                    textToBePrinted += printTotalAmountFor80Mm(total);
                                    textToBePrinted += printThankMsgFor80Mm();
                                }
                            } else {

                                if (nameBT.contains("58")) {

                                    textToBePrinted = printCashInvoiceHeaderFor58Mm(nomboutique, numboutique, nomclient, numclient, date, Integer.parseInt(idrecup));
                                    Cursor cur = dbHelper.getItemIdInvoice(Integer.valueOf(idrecup));
                                    while (cur.moveToNext()) {
                                        Itemstring item = new Itemstring();
                                        item.setName(cur.getString(1));
                                        item.setUnitPrice(cur.getString(2));
                                        item.setQuantity(cur.getString(3));
                                        itemslast.add(item);
                                    }
                                    for (int i = 0; i < itemslast.size(); i++) {
                                        String nom = itemslast.get(i).getName();
                                        String prixunit = itemslast.get(i).getUnitPrice();
                                        String quantite = itemslast.get(i).getQuantity();
                                        textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                    }
                                    textToBePrinted += printTotalAmountFor58Mm(total);
                                    textToBePrinted += printThankMsgFor58Mm();

                                } else if (nameBT.contains("80")) {

                                    textToBePrinted = printCashInvoiceHeaderFor80Mm(nomboutique, numboutique, nomclient, numclient, date, Integer.parseInt(idrecup));
                                    Cursor cur = dbHelper.getItemIdInvoice(Integer.valueOf(idrecup));
                                    while (cur.moveToNext()) {
                                        Itemstring item = new Itemstring();
                                        item.setName(cur.getString(1));
                                        item.setUnitPrice(cur.getString(2));
                                        item.setQuantity(cur.getString(3));
                                        itemslast.add(item);
                                    }
                                    for (int i = 0; i < itemslast.size(); i++) {
                                        String nom = itemslast.get(i).getName();
                                        String prixunit = itemslast.get(i).getUnitPrice();
                                        String quantite = itemslast.get(i).getQuantity();
                                        textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                                    }
                                    textToBePrinted += printTotalAmountFor80Mm(total);
                                    textToBePrinted += printThankMsgFor80Mm();
                                }
                            }

                        } else {
                            if (invoiceType.equals("cash") || invoiceType.equals("credit")) {
                                textToBePrinted = printCustomerInvoiceHeaderFor58Mm(nomboutique, numboutique, nomclient, numclient, Integer.parseInt(solde), date, Integer.parseInt(idrecup));
                                Cursor cur = dbHelper.getItemIdInvoice(Integer.valueOf(idrecup));
                                while (cur.moveToNext()) {
                                    Itemstring item = new Itemstring();
                                    item.setName(cur.getString(1));
                                    item.setUnitPrice(cur.getString(2));
                                    item.setQuantity(cur.getString(3));
                                    itemslast.add(item);
                                }
                                for (int i = 0; i < itemslast.size(); i++) {
                                    String nom = itemslast.get(i).getName();
                                    String prixunit = itemslast.get(i).getUnitPrice();
                                    String quantite = itemslast.get(i).getQuantity();
                                    textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                }
                                textToBePrinted += printTotalAmountFor58Mm(total);
                                textToBePrinted += printThankMsgFor58Mm();
                            } else {
                                textToBePrinted = printCashInvoiceHeaderFor58Mm(nomboutique, numboutique, nomclient, numclient, date, Integer.parseInt(idrecup));
                                Cursor cur = dbHelper.getItemIdInvoice(Integer.valueOf(idrecup));
                                while (cur.moveToNext()) {
                                    Itemstring item = new Itemstring();
                                    item.setName(cur.getString(1));
                                    item.setUnitPrice(cur.getString(2));
                                    item.setQuantity(cur.getString(3));
                                    itemslast.add(item);
                                }
                                for (int i = 0; i < itemslast.size(); i++) {
                                    String nom = itemslast.get(i).getName();
                                    String prixunit = itemslast.get(i).getUnitPrice();
                                    String quantite = itemslast.get(i).getQuantity();
                                    textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                }
                                textToBePrinted += printTotalAmountFor58Mm(total);
                                textToBePrinted += printThankMsgFor58Mm();
                            }
                        }

                        Log.v("TAG","Re printing ...."+textToBePrinted);
                        if (!TextUtils.isEmpty(textToBePrinted)) {
                            Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
                            printer.putExtra("text", textToBePrinted);
                            itemslast.clear();
                            startActivity(printer);
                        }
                    }
                }
            }
        });


        Cursor cur = dbHelper.getClient("1");
        if (!cur.moveToFirst()) {
            //si pas de client, on crée le client cash et la reference reduction
            dbHelper.insertClient("Cash", "0", "0");
            dbHelper.insertReferencedg("0000","Reduction","0","1","","","","1000");
            Toast.makeText(getApplicationContext(), "Client cash créé ", Toast.LENGTH_LONG).show();
        }

        Cursor bouti1 = dbHelper.getBoutique("1");
        if (bouti1.moveToFirst()) {
            leboutiqnum = bouti1.getString(1);
            leboutiqnam = bouti1.getString(2);
        }

        if (!bouti1.moveToFirst()) {
            Toast.makeText(getApplicationContext(), "Veuillez configurer votre boutique merci", Toast.LENGTH_LONG).show();
            showAlertConfigureShop(getApplicationContext());
        }


        reduire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.size() == 0) {
                    Toast.makeText(getApplicationContext(), "veuillez faire une vente d'abord ", Toast.LENGTH_LONG).show();
                }
                else{

                        Reductbuilder = new AlertDialog.Builder(NewInterface.this);
                        View customView = getLayoutInflater().inflate(R.layout.dialog_reduire, null, false);

                        final FormEditText priceref = (FormEditText) customView.findViewById(R.id.montreduc);

                        final LinearLayout numero=customView.findViewById(R.id.clavn);

                        final ImageView okbtn = (ImageView) customView.findViewById(R.id.okmontreduc);

                        final Button cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;

                        un=(Button)customView.findViewById(R.id.btnNum1Id);
                        deux=(Button)customView.findViewById(R.id.btnNum2Id);
                        trois=(Button)customView.findViewById(R.id.btnNum3Id);
                        quatre=(Button)customView.findViewById(R.id.btnNum4Id);
                        cinq=(Button)customView.findViewById(R.id.btnNum5Id);
                        six=(Button)customView.findViewById(R.id.btnNum6Id);
                        sept=(Button)customView.findViewById(R.id.btnNum7Id);
                        huit=(Button)customView.findViewById(R.id.btnNum8Id);
                        neuf=(Button)customView.findViewById(R.id.btnNum9Id);
                        zero=(Button)customView.findViewById(R.id.btnNum0Id);
                        cleardn=(Button)customView.findViewById(R.id.btnClearId);

                        priceref.setInputType(0);

                        priceref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                numero.setVisibility(View.VISIBLE);

                                zero.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+0);
                                        }
                                        else {
                                            priceref.append(""+0);
                                        }
                                    }
                                });

                                un.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+1);
                                        }
                                        else {
                                            priceref.append(""+1);
                                        }

                                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                                    }
                                });

                                deux.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+2);
                                        }
                                        else {
                                            priceref.append(""+2);
                                        }

                                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                    }
                                });

                                trois.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+3);
                                        }
                                        else {
                                            priceref.append(""+3);
                                        }

                                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                    }
                                });

                                quatre.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+4);
                                        }
                                        else {
                                            priceref.append(""+4);
                                        }

                                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                    }
                                });

                                cinq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+5);
                                        }
                                        else {
                                            priceref.append(""+5);
                                        }

                                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                    }
                                });

                                six.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+6);
                                        }
                                        else {
                                            priceref.append(""+6);
                                        }

                                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                    }
                                });

                                sept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+7);
                                        }
                                        else {
                                            priceref.append(""+7);
                                        }

                                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                                    }
                                });

                                huit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+8);
                                        }
                                        else {
                                            priceref.append(""+8);
                                        }


                                    }
                                });

                                neuf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String chaine=priceref.getText().toString();

                                        if(chaine.contains("")){
                                            priceref.append(""+9);
                                        }
                                        else {
                                            priceref.append(""+9);
                                        }

                                    }
                                });



                                cleardn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String chaine=priceref.getText().toString();
                                        if (chaine.equals("")){

                                        }else {
                                            chaine = chaine.substring(0, chaine.length()-1);
                                            priceref.setText(""+chaine);
                                        }

                                    }
                                });
                            }
                        });



                        Reductbuilder.setCancelable(true);
                        Reductbuilder.setView(customView);
                        Reductbuilder.setTitle("Mettre le montant de la reduction");
                        Reductdialog = Reductbuilder.create();
                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String lemontant=priceref.getText().toString();
                                if(lemontant.equals("")){
                                    Toast.makeText(getApplicationContext(),"Merci de renseigner le code",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    dbHelper.insertReferencedg("0000","Reduction","0","1","","","","1000");
                                    ajoutvente("Reduction","-"+lemontant,"1");
                                    Reductdialog.dismiss();
                                }
                            }
                        });
                        Reductdialog.show();

                }
            }
        });

        facturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.size() == 0) {
                    Toast.makeText(getApplicationContext(), "veuillez faire une vente d'abord ", Toast.LENGTH_LONG).show();
                } else {
                    String recuptotal = totalapayer.getText().toString().trim();
                    final int total = Integer.valueOf(recuptotal);

                    leclient = choixcli.getText().toString();

                    //gestion client cash
                    if (leclient.equals("") || lenumero.equals("")) {
                            /*
                            //Toast.makeText(getApplicationContext(),"Veuillez renseigner le client merci", Toast.LENGTH_LONG).show();
                            */
                        //Toast.makeText(getApplicationContext(),"cash \n"+lenom.getText(),Toast.LENGTH_LONG).show();
                        Okbuildercash = new AlertDialog.Builder(NewInterface.this);
                        View customView = getLayoutInflater().inflate(R.layout.dialog_ok_cash, null, false);

                        //final ImageView okbtn = (ImageView) customView.findViewById(R.id.okconf);
                        //final TextView rendre = (TextView) customView.findViewById(R.id.rendre);
                        //final EditText reçu = (EditText) customView.findViewById(R.id.recu);
                        final TextView fact = customView.findViewById(R.id.lafact);
                        final ImageView ok = customView.findViewById(R.id.okconfcash);
                        final ImageView annule = customView.findViewById(R.id.annule);
                        //nom.setText(lenom.getText());
                        //num.setText(phone.getText());
                        fact.setText(totalapayer.getText());


                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //String recucash=reçu.getText().toString();
                                Cursor bouti = dbHelper.getBoutique("1");
                                //String nomboutique=bouti.getString(1);
                               // String numboutique=bouti.getString(0);
                                Cursor cur = dbHelper.getClient("1");
                                List<Itemstring> ite = dbHelper.getAllItems();
                                //Toast.makeText(getApplicationContext(),""+ite.toString(),Toast.LENGTH_LONG).show();
                                if (cur.moveToFirst() == true) {
                                    String idcash = cur.getString(0);
                                    String numerocash = cur.getString(1);
                                    String clientcash = cur.getString(2);
                                    String soldecash = cur.getString(3);
                                    //int totalenreg=total+Integer.valueOf(soldecash);
                                    //Boutique boutique = realm.where(Boutique.class).findFirst();
                                    int idfacture = 0;
                                    String datefact = "";
                                    Cursor curid = dbHelper.getLastInvoiceR();
                                    if (curid.moveToFirst()) {
                                        String idfact = curid.getString(0);
                                        datefact = curid.getString(1);
                                        idfacture = Integer.valueOf(idfact) + 1;
                                        //Toast.makeText(getApplicationContext(),""+ids,Toast.LENGTH_SHORT).show();
                                    }

                                    if (isPreferenceFileExist("regoprintdemo",getApplicationContext())){

                                        SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
                                        String nameBT = prefs.getString("BT_DEVICE_NAME", null);

                                        if (nameBT.contains("58")) {
                                            textToBePrinted = printCashInvoiceHeaderFor58Mm(leboutiqnam, leboutiqnum, clientcash, numerocash,datefact, idfacture);
                                            for (int i = 0; i < items.size(); i++) {
                                                String nom = items.get(i).getName();
                                                String prixunit = items.get(i).getUnitPrice();
                                                String quantite = items.get(i).getQuantity();
                                                textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                            }
                                            textToBePrinted += printTotalAmountFor58Mm(total);
                                            textToBePrinted += printThankMsgFor58Mm();

                                        } else if (nameBT.contains("80")) {
                                            textToBePrinted = printCashInvoiceHeaderFor80Mm(leboutiqnam, leboutiqnum, clientcash, numerocash, datefact, idfacture);
                                            for (int i = 0; i < items.size(); i++) {
                                                String nom = items.get(i).getName();
                                                String prixunit = items.get(i).getUnitPrice();
                                                String quantite = items.get(i).getQuantity();
                                                textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                                            }
                                            textToBePrinted += printTotalAmountFor80Mm(total);
                                            textToBePrinted += printThankMsgFor80Mm();
                                        }
                                    } else {
                                        textToBePrinted = printCashInvoiceHeaderFor58Mm(leboutiqnam, leboutiqnum, clientcash, numerocash,datefact, idfacture);
                                        for (int i = 0; i < items.size(); i++) {
                                            String nom = items.get(i).getName();
                                            String prixunit = items.get(i).getUnitPrice();
                                            String quantite = items.get(i).getQuantity();
                                            textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                        }
                                        textToBePrinted += printTotalAmountFor58Mm(total);
                                        textToBePrinted += printThankMsgFor58Mm();
                                    }


                                   // Log.d("Impression In Cash", "" + textToBePrinted);

                                    if (!TextUtils.isEmpty(textToBePrinted)) {
                                        modepaiement.setText("Mode de paiement");
                                        choixcli.setText("Choisir le client");
                                        totalapayer.setText("");
                                        //dbHelper.insertFact(Config.getFormattedDate(Calendar.getInstance().getTime()), String.valueOf(total), idcash, "");
                                        dbHelper.insertFactr(Config.getFormattedDate(Calendar.getInstance().getTime()), String.valueOf(total), idcash, "");

                                        //retrieve last invoice id
                                        Cursor cursor = dbHelper.getLastInvoiceR();
                                        if (cursor.moveToFirst() == true) {
                                            idlast = cursor.getString(0);
                                            //Toast.makeText(getApplicationContext(),"dernier facture "+cursor.getString(0),Toast.LENGTH_LONG).show();
                                            for (int i = 0; i < items.size(); i++) {
                                                String nom = items.get(i).getName();
                                                String prixunit = items.get(i).getUnitPrice();
                                                String quantite = items.get(i).getQuantity();
                                                String soustot = items.get(i).getTotalPrice();
                                                dbHelper.insertItems(nom, prixunit, soustot, quantite, idlast);
                                                double qteactu = 0;
                                                String lid = "";
                                                String ladate = Config.getFormattedDate(Calendar.getInstance().getTime());
                                                Cursor cursor1 = dbHelper.getrefname(nom);
                                                if (cursor1.moveToFirst()) {
                                                    lid = cursor1.getString(0);
                                                    qteactu = Double.valueOf(cursor1.getString(3));
                                                    qteactu -= Double.valueOf(quantite);
                                                    //Toast.makeText(NewInterface.this, ""+qteactu, Toast.LENGTH_SHORT).show();
                                                }

                                                dbHelper.updateRefqte(String.valueOf(qteactu), ladate, lid, "vente");
                                                dbHelper.insertSuivRQ(ladate, quantite, lid, " $ Vente", prix);
                                            }

                                            items.removeAll(items);
                                            adapterReferenceitems.notifyDataSetChanged();
                                            Intent intent = new Intent(NewInterface.this, NewInterface.class);
                                            intent.putExtra(NewInterface.EXTRA_MESSAGE, idlast);
                                            startActivity(intent);
                                            Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
                                            printer.putExtra("text", textToBePrinted);
                                            startActivity(printer);
                                        }
                                        Okdialogcash.dismiss();
                                        finish();
                                    }
                                }
                            }

                        });


                        annule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Okdialogcash.dismiss();
                            }
                        });

                        Okbuildercash.setCancelable(true);
                        Okbuildercash.setView(customView);
                        Okbuildercash.setTitle("Confirmer la facture - vente cash");
                        Okdialogcash = Okbuildercash.create();
                        Okdialogcash.show();
                    }

                    // client abonne
                    if (!leclient.equals("") && !lenumero.equals("")) {

                        if (modepaiement.getText().equals("Cash")) {
                            //Toast.makeText(NewInterface.this, "cash", Toast.LENGTH_SHORT).show();
                            Okbuilder = new AlertDialog.Builder(NewInterface.this);
                            View customView = getLayoutInflater().inflate(R.layout.dialog_ok_client_cash, null, false);

                            //final ImageView okbtn = (ImageView) customView.findViewById(R.id.okconf);
                            final TextView nom = customView.findViewById(R.id.lenom);
                            final TextView num = customView.findViewById(R.id.lenum);
                            final TextView fact = customView.findViewById(R.id.lafact);
                            //final EditText recu = (EditText) customView.findViewById(R.id.recu);
                            final ImageView ok = customView.findViewById(R.id.okconfcash);
                            final ImageView annule = customView.findViewById(R.id.annulecashcli);
                            nom.setText(leclient);
                            num.setText(lenumero);
                            fact.setText(totalapayer.getText());


                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int idfacture = 0;
                                    String invoiceDate = "";
                                    Cursor curid = dbHelper.getLastInvoiceR();
                                    if (curid.moveToFirst()) {
                                        String idfact = curid.getString(0);
                                        invoiceDate = curid.getString(1);
                                        idfacture = Integer.valueOf(idfact) + 1;
                                        //Toast.makeText(getApplicationContext(),""+ids,Toast.LENGTH_SHORT).show();
                                    }


                                    if (isPreferenceFileExist("regoprintdemo",getApplicationContext())) {
                                        SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
                                        String nameBT = prefs.getString("BT_DEVICE_NAME", null);

                                        if (nameBT.contains("58")) {
                                            textToBePrinted = printCustomerInvoiceHeaderFor58Mm(leboutiqnam, leboutiqnum, leclient, lenumero, lesolde, invoiceDate, idfacture);
                                            for (int i = 0; i < items.size(); i++) {
                                                String nom = items.get(i).getName();
                                                String prixunit = items.get(i).getUnitPrice();
                                                String quantite = items.get(i).getQuantity();
                                                textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                            }
                                            textToBePrinted += printTotalAmountFor58Mm(total);
                                            textToBePrinted += printThankMsgFor58Mm();

                                        } else if (nameBT.contains("80")) {
                                            textToBePrinted = printCustomerInvoiceHeaderFor80Mm(leboutiqnam, leboutiqnum, leclient, lenumero, invoiceDate, lesolde, idfacture);
                                            for (int i = 0; i < items.size(); i++) {
                                                String nom = items.get(i).getName();
                                                String prixunit = items.get(i).getUnitPrice();
                                                String quantite = items.get(i).getQuantity();
                                                textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                                            }
                                            textToBePrinted += printTotalAmountFor80Mm(total);
                                            textToBePrinted += printThankMsgFor80Mm();
                                        }
                                    } else {
                                        textToBePrinted = printCustomerInvoiceHeaderFor58Mm(leboutiqnam, leboutiqnum, leclient, lenumero, lesolde, invoiceDate, idfacture);
                                        for (int i = 0; i < items.size(); i++) {
                                            String nom = items.get(i).getName();
                                            String prixunit = items.get(i).getUnitPrice();
                                            String quantite = items.get(i).getQuantity();
                                            textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                        }
                                        textToBePrinted += printTotalAmountFor58Mm(total);
                                        textToBePrinted += printThankMsgFor58Mm();
                                    }

                                    Log.d("Print Cash", "finished" + textToBePrinted);

                                    if (!TextUtils.isEmpty(textToBePrinted)) {
                                        modepaiement.setText("Mode de paiement");
                                        choixcli.setText("Choisir le client");
                                        totalapayer.setText("");

                                        //dbHelper.insertFact(Config.getFormattedDate(Calendar.getInstance().getTime()), String.valueOf(total), idcli, "cash");
                                        dbHelper.insertFactr(Config.getFormattedDate(Calendar.getInstance().getTime()), String.valueOf(total), idcli, "cash");

                                        //retrieve last invoice id
                                        Cursor cursor = dbHelper.getLastInvoiceR();
                                        if (cursor.moveToFirst() == true) {
                                            idlast = cursor.getString(0);
                                            for (int i = 0; i < items.size(); i++) {
                                                String noms = items.get(i).getName();
                                                String prixunit = items.get(i).getUnitPrice();
                                                String quantite = items.get(i).getQuantity();
                                                String soustot = items.get(i).getTotalPrice();
                                                dbHelper.insertItems(noms, prixunit, soustot, quantite, idlast);
                                                double qteactu = 0;
                                                String lid = "";
                                                String ladate = Config.getFormattedDate(Calendar.getInstance().getTime());
                                                Cursor cursor1 = dbHelper.getrefname(noms);
                                                if (cursor1.moveToFirst()) {
                                                    lid = cursor1.getString(0);
                                                    qteactu = Double.valueOf(cursor1.getString(3));
                                                    qteactu -= Double.valueOf(quantite);
                                                }
                                                dbHelper.updateRefqte(String.valueOf(qteactu), ladate, lid, "vente");
                                                dbHelper.insertSuivRQ(ladate, quantite, lid, " $ Vente", prix);
                                            }
                                            lesitems = dbHelper.getAllItems();
                                            items.removeAll(items);
                                            adapterReferenceitems.notifyDataSetChanged();
                                            Intent intent = new Intent(NewInterface.this, NewInterface.class);
                                            intent.putExtra(NewInterface.EXTRA_MESSAGE, idlast);
                                            //Toast.makeText(getApplicationContext(),"id "+idlast,Toast.LENGTH_LONG).show();
                                            startActivity(intent);
                                            Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
                                            printer.putExtra("text", textToBePrinted);
                                            startActivity(printer);
                                        }
                                    }
                                    Okdialog.dismiss();
                                    finish();
                                }
                            });

                            annule.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Okdialog.dismiss();
                                }
                            });

                            Okbuilder.setCancelable(true);
                            Okbuilder.setView(customView);
                            Okbuilder.setTitle("Confirmer la facture - vente client cash");
                            Okdialog = Okbuilder.create();
                            Okdialog.show();
                        }


                        if (modepaiement.getText().equals("Crédit")) {

                            Okbuilderclient = new AlertDialog.Builder(NewInterface.this);
                            View customView = getLayoutInflater().inflate(R.layout.dialog_ok_client_credit, null, false);

                            //final ImageView okbtn = (ImageView) customView.findViewById(R.id.okconf);
                            final TextView noms = customView.findViewById(R.id.lenom);
                            final TextView num = customView.findViewById(R.id.lenum);
                            final TextView fact = customView.findViewById(R.id.lafact);
                            final TextView solde = customView.findViewById(R.id.solde1);
                            final TextView newsolde = customView.findViewById(R.id.newsolde);
                            final ImageView ok = customView.findViewById(R.id.okconfcredit);
                            final ImageView annule = customView.findViewById(R.id.annule);
                            noms.setText(leclient);
                            num.setText(lenumero);


                            Cursor cursor = dbHelper.getClient(idcli);
                            fact.setText(totalapayer.getText());
                            if (cursor.moveToFirst()) {
                                String lesolde1 = cursor.getString(3);
                                lesolde = Integer.valueOf(lesolde1);
                                int soldeint = Integer.valueOf(lesolde1);
                                solde.setText(lesolde1);
                                int totalap = Integer.valueOf(totalapayer.getText().toString());

                                int soldeafter = (-totalap) + soldeint;

                                newsolde.setText(String.valueOf(soldeafter));
                            }


                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //RegisterRef(nameref, RegisterRefdialog);
                                    //faToast.makeText(getApplicationContext(),"test",Toast.LENGTH_LONG).show();
                                    Cursor cur = dbHelper.getClient(idcli);
                                    if (cur.moveToFirst() == true) {

                                        lesolde = Integer.valueOf(cur.getString(3));
                                        lesolde -= total;
                                    }

                                    //dbHelper.insertFact(Config.getFormattedDate(Calendar.getInstance().getTime()), String.valueOf(total), idcli, "credit");
                                    dbHelper.insertFactr(Config.getFormattedDate(Calendar.getInstance().getTime()), String.valueOf(total), idcli, "credit");

                                    dbHelper.majsoldecli(idcli, String.valueOf(lesolde));

                                    lesfact = dbHelper.getAllInvoiceR();
                                    //Toast.makeText(getApplicationContext(),""+lesolde,Toast.LENGTH_LONG).show();


                                    Cursor boutiq = dbHelper.getBoutique("1");
                                    if (boutiq.moveToFirst()) {

                                        int idfacture = 0;
                                        String date ="";
                                        Cursor curid = dbHelper.getLastInvoiceR();
                                        if (curid.moveToFirst()) {
                                            String idfact = curid.getString(0);
                                            date = curid.getString(1);
                                            idfacture = Integer.valueOf(idfact);
                                            //Toast.makeText(getApplicationContext(), "" + idfact, Toast.LENGTH_SHORT).show();
                                        }

                                        if (isPreferenceFileExist("regoprintdemo",getApplicationContext())) {
                                            SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
                                            String nameBT = prefs.getString("BT_DEVICE_NAME", null);

                                            if (nameBT.contains("58")) {
                                                textToBePrinted = printCustomerInvoiceHeaderFor58Mm(leboutiqnam, leboutiqnum, leclient, lenumero,lesolde,date, idfacture);
                                                for (int i = 0; i < items.size(); i++) {
                                                    String nom = items.get(i).getName();
                                                    String prixunit = items.get(i).getUnitPrice();
                                                    String quantite = items.get(i).getQuantity();
                                                    textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                                }
                                                textToBePrinted += printTotalAmountFor58Mm(total);
                                                textToBePrinted += printThankMsgFor58Mm();

                                            } else if (nameBT.contains("80")) {
                                                textToBePrinted = printCustomerInvoiceHeaderFor80Mm(leboutiqnam, leboutiqnum, leclient, lenumero,date, lesolde, idfacture);
                                                for (int i = 0; i < items.size(); i++) {
                                                    String nom = items.get(i).getName();
                                                    String prixunit = items.get(i).getUnitPrice();
                                                    String quantite = items.get(i).getQuantity();
                                                    textToBePrinted += printItemFor80Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor80Mm), quantite, prixunit);
                                                }
                                                textToBePrinted += printTotalAmountFor80Mm(total);
                                                textToBePrinted += printThankMsgFor80Mm();
                                            }

                                        } else {
                                            textToBePrinted = printCustomerInvoiceHeaderFor58Mm(leboutiqnam, leboutiqnum, leclient, lenumero,lesolde,date, idfacture);
                                            for (int i = 0; i < items.size(); i++) {
                                                String nom = items.get(i).getName();
                                                String prixunit = items.get(i).getUnitPrice();
                                                String quantite = items.get(i).getQuantity();
                                                textToBePrinted += printItemFor58Mm(cutAStringAndAddSpaceAfterText(nom, nameWidthLimitFor58Mm), quantite, prixunit);
                                            }
                                            textToBePrinted += printTotalAmountFor58Mm(total);
                                            textToBePrinted += printThankMsgFor58Mm();
                                        }
                                        Log.d("Printing Credit", "Client " + textToBePrinted);

                                        if (!TextUtils.isEmpty(textToBePrinted)) {
                                            modepaiement.setText("Mode de paiement");
                                            choixcli.setText("Choisir le client");
                                            totalapayer.setText("");
                                            Cursor cursor1 = dbHelper.getLastInvoiceR();
                                            if (cursor1.moveToFirst() == true) {
                                                idlast = cursor1.getString(0);
                                                for (int i = 0; i < items.size(); i++) {
                                                    String noms = items.get(i).getName();
                                                    String prixunit = items.get(i).getUnitPrice();
                                                    String quantite = items.get(i).getQuantity();
                                                    String soustot = items.get(i).getTotalPrice();
                                                    dbHelper.insertItems(noms, prixunit, soustot, quantite, idlast);

                                                    double qteactu = 0;
                                                    String lid = "";
                                                    String ladate = Config.getFormattedDate(Calendar.getInstance().getTime());
                                                    Cursor cursor2 = dbHelper.getrefname(noms);
                                                    if (cursor2.moveToFirst()) {
                                                        lid = cursor2.getString(0);
                                                        qteactu = Double.valueOf(cursor2.getString(3));
                                                        qteactu -= Double.valueOf(quantite);
                                                    }
                                                    dbHelper.updateRefqte(String.valueOf(qteactu), ladate, lid, "vente");
                                                    dbHelper.insertSuivRQ(ladate, quantite, lid, " $ Vente", prix);
                                                }

                                                lesitems = dbHelper.getAllItems();
                                                items.removeAll(items);
                                                adapterReferenceitems.notifyDataSetChanged();

                                                Intent intent = new Intent(NewInterface.this, NewInterface.class);
                                                intent.putExtra(NewInterface.EXTRA_MESSAGE, idlast);
                                                startActivity(intent);
                                                Intent printer = new Intent(getApplicationContext(), ImprimeText.class);
                                                printer.putExtra("text", textToBePrinted);
                                                startActivity(printer);
                                            }

                                        }
                                    }
                                    Okdialogclient.dismiss();
                                    finish();
                                }
                            });

                            annule.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Okdialogclient.dismiss();
                                }
                            });

                            Okbuilderclient.setCancelable(true);
                            Okbuilderclient.setView(customView);
                            Okbuilderclient.setTitle("Confirmer la facture - vente client à crédit");


                            Okdialogclient = Okbuilderclient.create();
                            Okdialogclient.show();

                        }
                    }

                }
            }
        });

        choixcliline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.size() == 0) {
                    Toast.makeText(getApplicationContext(), "veuillez faire une vente d'abord ", Toast.LENGTH_LONG).show();
                } else {


                    List<Client> clientsbd = new ArrayList<>();
                    clientsbd = dbHelper.getAllClientwithoutcash();


                    Listebuilder = new AlertDialog.Builder(NewInterface.this);
                    View customView = getLayoutInflater().inflate(R.layout.dialog_liste_v2, null, false);

                    final Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,ri,s,t,u,vi,w,x,y,z,
                            espace,clear,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero, clearnum
                            ,arobase, point, tiret8;

                    a= customView.findViewById(R.id.a); b= customView.findViewById(R.id.b);c= customView.findViewById(R.id.c);
                    d= customView.findViewById(R.id.d); e= customView.findViewById(R.id.e); f= customView.findViewById(R.id.f);
                    g= customView.findViewById(R.id.g); h= customView.findViewById(R.id.h); i= customView.findViewById(R.id.i);
                    j= customView.findViewById(R.id.j); k= customView.findViewById(R.id.k); l= customView.findViewById(R.id.l);
                    m= customView.findViewById(R.id.m); n= customView.findViewById(R.id.n); o= customView.findViewById(R.id.o);
                    p= customView.findViewById(R.id.p); ri= customView.findViewById(R.id.r); s= customView.findViewById(R.id.s);
                    t= customView.findViewById(R.id.t); u= customView.findViewById(R.id.u); vi= customView.findViewById(R.id.v);
                    w= customView.findViewById(R.id.w); x= customView.findViewById(R.id.x); y= customView.findViewById(R.id.y);
                    q= customView.findViewById(R.id.q); z= customView.findViewById(R.id.z); arobase= customView.findViewById(R.id.arobase);
                    point= customView.findViewById(R.id.point);
                    tiret8= customView.findViewById(R.id.tiret8);

                    un= customView.findViewById(R.id.btnNum1Il);
                    deux= customView.findViewById(R.id.btnNum2Il);
                    trois= customView.findViewById(R.id.btnNum3Il);
                    quatre= customView.findViewById(R.id.btnNum4Il);
                    cinq= customView.findViewById(R.id.btnNum5Il);
                    six= customView.findViewById(R.id.btnNum6Il);
                    sept= customView.findViewById(R.id.btnNum7Il);
                    huit= customView.findViewById(R.id.btnNum8Il);
                    neuf= customView.findViewById(R.id.btnNum9Il);
                    clearnum= customView.findViewById(R.id.btnClearIl);
                    //doublezero=(Button)findViewById(R.id.btn00);
                    zero= customView.findViewById(R.id.btnNum0Il);
                    clear= customView.findViewById(R.id.btnClearAl);
                    espace= customView.findViewById(R.id.espaced);

                    arobase.setVisibility(View.GONE);
                    point.setVisibility(View.GONE);
                    tiret8.setVisibility(View.GONE);

                    final ListView laliste = customView.findViewById(R.id.lalisteV2);
                    textview3 = customView.findViewById(R.id.search_client);
                    textview3.setInputType(0);
                    textview3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            clear.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chaine=textview3.getText().toString();
                                    if (chaine.equals("")){

                                    }else {
                                        chaine = chaine.substring(0, chaine.length()-1);
                                        textview3.setText(""+chaine);
                                    }

                                }
                            });
                            espace.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append(" ");
                                }
                            });

                            a.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("A");
                                }
                            });

                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("B");
                                }
                            });

                            c.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("C");
                                }
                            });

                            d.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("D");
                                }
                            });

                            e.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("E");
                                }
                            });

                            f.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("F");
                                }
                            });

                            g.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("G");
                                }
                            });

                            h.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("H");
                                }
                            });

                            i.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("I");
                                }
                            });
                            j.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("J");
                                }
                            });
                            k.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("K");
                                }
                            });
                            l.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("L");
                                }
                            });
                            m.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("M");
                                }
                            });
                            n.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("N");
                                }
                            });
                            o.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("O");
                                }
                            });
                            p.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("P");
                                }
                            });
                            q.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("Q");
                                }
                            });
                            ri.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("R");
                                }
                            });
                            s.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("S");
                                }
                            });
                            t.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("T");
                                }
                            });
                            u.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("U");
                                }
                            });
                            vi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("V");
                                }
                            });
                            w.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("W");
                                }
                            });
                            x.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("X");
                                }
                            });
                            y.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("Y");
                                }
                            });
                            z.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("Z");
                                }
                            });

                            un.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("1");
                                }
                            });
                            deux.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("2");
                                }
                            });
                            trois.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("3");
                                }
                            });
                            quatre.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("4");
                                }
                            });
                            cinq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("5");
                                }
                            });
                            six.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("6");
                                }
                            });
                            sept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("7");
                                }
                            });
                            huit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("8");
                                }
                            });
                            neuf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("9");
                                }
                            });
                            zero.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textview3.append("0");
                                }
                            });
                        }
                    });


                    //final List<ClientRepository> clientRepositories = new ArrayList<>();
                    final List<Client> clients = new ArrayList<>();
                    final List<Client> clientse = new ArrayList<>();
                    //final ImageView lebout=(ImageView)customView.findViewById(R.id.lbtn);
                    final AdapterClient adapterClient;
                    for (Client client : clientsbd) {
                        Client lecli = new Client(client.getNom(), client.getNumero(), client.getId(), client.getSolde());

                        clientse.add(lecli);
                    }


                    adapterClient = new AdapterClient(NewInterface.this, clientse);
                    laliste.setAdapter(adapterClient);

                    for (Client r : clientsbd) {
                        Client cr = new Client();
                        cr.setNom(r.getNom());
                        cr.setNumero(r.getNumero());
                        cr.setSolde(r.getSolde());
                        cr.setId(r.getId());
                        clients.add(cr);

                    }
                    adapterClient.notifyDataSetChanged();


                    //textview3.setEnabled(false);

                    textview3.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                            // When user changed the Text
                            // NewInterface.this.adapterReference.filter(cs);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                      int arg3) {
                        }

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            String text = textview3.getText().toString().toLowerCase(Locale.getDefault());
                            adapterClient.filter(text);
                        }
                    });

                    if (clients.size() == 0) {

                        Toast.makeText(getApplicationContext(), getString(R.string.client_need_tobecreated), Toast.LENGTH_LONG).show();
                    } else {

                        Listebuilder.setCancelable(true);
                        Listebuilder.setView(customView);
                        Listebuilder.setTitle((getString(R.string.select_customer)));

                        laliste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final String lenum = ((TextView) view.findViewById(R.id.client_phone)).getText().toString();
                                final String lenom = ((TextView) view.findViewById(R.id.client_name)).getText().toString();
                                //final Button cash = ((Button)view.findViewById(R.id.cash1));
                                //final Button credit = ((Button)view.findViewById(R.id.credit1));

                            }
                        });
                        Listedialog = Listebuilder.create();
                        Listedialog.show();
                    }
                }
            }
        });

        for (int i = 0; i < resultats.size(); i++) {
            references.add(resultats.get(i));
        }

        for (LaReffb laReffb : resultatsadapt) {
            LaReffb lar = new LaReffb(laReffb.getLaref(), laReffb.getLarefprice(), laReffb.getLarefqte(), laReffb.getId(), laReffb.getLienimg());
            referencesadapt.add(lar);
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        blisteclient = findViewById(R.id.Blist_clientb);
        blistereference = findViewById(R.id.Blist_ref);
        bstatistique = findViewById(R.id.Bstats);
        bparam = findViewById(R.id.Bparam);

        un = findViewById(R.id.btnNum1Id);
        deux = findViewById(R.id.btnNum2Id);
        trois = findViewById(R.id.btnNum3Id);
        quatre = findViewById(R.id.btnNum4Id);
        cinq = findViewById(R.id.btnNum5Id);
        six = findViewById(R.id.btnNum6Id);
        sept = findViewById(R.id.btnNum7Id);
        huit = findViewById(R.id.btnNum8Id);
        neuf = findViewById(R.id.btnNum9Id);
        //doublezero=(Button)findViewById(R.id.btn00);
        zero = findViewById(R.id.btnNum0Id);
        virgule = findViewById(R.id.btnvirgule);
        clear = findViewById(R.id.btnClearId);
        cleartext = findViewById(R.id.btnClearAl);
        espace = findViewById(R.id.espace);

        unl = findViewById(R.id.btnNum1Il);
        deuxl = findViewById(R.id.btnNum2Il);
        troisl = findViewById(R.id.btnNum3Il);
        quatrel = findViewById(R.id.btnNum4Il);
        cinql = findViewById(R.id.btnNum5Il);
        sixl = findViewById(R.id.btnNum6Il);
        septl = findViewById(R.id.btnNum7Il);
        huitl = findViewById(R.id.btnNum8Il);
        neufl = findViewById(R.id.btnNum9Il);
        zerol = findViewById(R.id.btnNum0Il);

        //valider=(Button)findViewById(R.id.valider);

        //okok=(Button)findViewById(R.id.btnplus);

        nok= findViewById(R.id.btnmoin);

        a= findViewById(R.id.a); b= findViewById(R.id.b);c= findViewById(R.id.c);
        d= findViewById(R.id.d); e= findViewById(R.id.e); f= findViewById(R.id.f);
        g= findViewById(R.id.g); h= findViewById(R.id.h); i= findViewById(R.id.i);
        j= findViewById(R.id.j); k= findViewById(R.id.k); l= findViewById(R.id.l);
        m= findViewById(R.id.m); n= findViewById(R.id.n); o= findViewById(R.id.o);
        p= findViewById(R.id.p); r= findViewById(R.id.r); s= findViewById(R.id.s);
        t= findViewById(R.id.t); u= findViewById(R.id.u); v= findViewById(R.id.v);
        w= findViewById(R.id.w); x= findViewById(R.id.x); y= findViewById(R.id.y);
        q= findViewById(R.id.q); z= findViewById(R.id.z);tiret8= findViewById(R.id.tiret8);

        txtResult= findViewById(R.id.txtResultId);
        totalapayer= findViewById(R.id.resultat);
        listeref = findViewById(R.id.listeref);
        adapterReference = new AdapterReference(this, referencesadapt);
        listeref.setAdapter(adapterReference);


        textView2 = findViewById(R.id.search_ref);
        textView2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                // NewInterface.this.adapterReference.filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = textView2.getText().toString().toLowerCase(Locale.getDefault());
                adapterReference.filter(text);
            }
        });

       /* bleu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultatsadapt.clear();
                referencesadapt.clear();
                resultatsadapt=dbHelper.getAllReferenceCat("bleu");
                for(LaReffb laReffb : resultatsadapt){
                    LaReffb lar=new LaReffb(laReffb.getLaref(),laReffb.getLarefprice(),laReffb.getLarefqte(), laReffb.getId(), laReffb.getLienimg());

                    referencesadapt.add(lar);
                }
                adapterReference.notifyDataSetChanged();

            }
        });

        vert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"vert",Toast.LENGTH_SHORT).show();
                resultatsadapt.clear();
                referencesadapt.clear();
                resultatsadapt=dbHelper.getAllReferenceCat("vert");
                for(LaReffb laReffb : resultatsadapt){
                    LaReffb lar=new LaReffb(laReffb.getLaref(),laReffb.getLarefprice(),laReffb.getLarefqte(), laReffb.getId(), laReffb.getLienimg());

                    referencesadapt.add(lar);
                }
                adapterReference.notifyDataSetChanged();
            }
        });

        jaune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"jaune",Toast.LENGTH_SHORT).show();
                resultatsadapt.clear();
                referencesadapt.clear();
                resultatsadapt=dbHelper.getAllReferenceCat("jaune");
                for(LaReffb laReffb : resultatsadapt){
                    LaReffb lar=new LaReffb(laReffb.getLaref(),laReffb.getLarefprice(),laReffb.getLarefqte(), laReffb.getId(), laReffb.getLienimg());

                    referencesadapt.add(lar);
                }
                adapterReference.notifyDataSetChanged();

            }
        });
        rouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"rouge",Toast.LENGTH_SHORT).show();
                resultatsadapt.clear();
                referencesadapt.clear();
                resultatsadapt=dbHelper.getAllReferenceCat("rouge");
                for(LaReffb laReffb : resultatsadapt){
                    LaReffb lar=new LaReffb(laReffb.getLaref(),laReffb.getLarefprice(),laReffb.getLarefqte(), laReffb.getId(), laReffb.getLienimg());

                    referencesadapt.add(lar);
                }
                adapterReference.notifyDataSetChanged();

            }
        });
        blanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"blanc",Toast.LENGTH_SHORT).show();
                resultatsadapt.clear();
                referencesadapt.clear();
                resultatsadapt=dbHelper.getAllReferenceCat("blanc");
                for(LaReffb laReffb : resultatsadapt){
                    LaReffb lar=new LaReffb(laReffb.getLaref(),laReffb.getLarefprice(),laReffb.getLarefqte(), laReffb.getId(), laReffb.getLienimg());

                    referencesadapt.add(lar);
                }
                adapterReference.notifyDataSetChanged();

            }
        });*/

        listeref.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LaReffb laref = (LaReffb) parent.getItemAtPosition(position);
                String r = laref.getLaref();
                String pu = laref.getLarefprice();
                //String qtetext=laref.getLarefqte();
                // Toast.makeText(getApplicationContext(),""+r+"\n"+ pu+"\n"+qtetext,Toast.LENGTH_LONG).show();
                String qtetext = txtResult.getText().toString();

                if (qtetext.equals("")) {
                    //Toast.makeText(getApplicationContext(), ""+uniqueID, Toast.LENGTH_LONG).show();
                    ajoutvente(r, pu, "1");
                }

                if (!qtetext.equals("")) {
                    ajoutvente(r, pu, qtetext);
                }


            }
        });


        textView2.setEnabled(false);

        // textView2 = (AutoCompleteTextView) findViewById(R.id.search_ref);
        //textView2.setAdapter(arrayAdapter);


        adapterReferenceitems = new AdapterReferenceItems(this, items);
        listeitem = findViewById(R.id.v2_list_item);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //permet de ne pas ouvrir le clavier par défaut au démarrage de l'application
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


        /*ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_dropdown_item_1line, lesnumeros);*/

        ArrayAdapter<String> adapterref = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, lesrefsearch);

        /*final ArrayAdapter<String> listeitemstring = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, itemsstring);*/
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, lesnomscli);

        //listeref.setAdapter(adapterReference);

        //listeitem.setAdapter(listeitemstring);
        listeitem.setAdapter(adapterReferenceitems);
        //listeclientaffiche.setAdapter(adapter);

        listeitem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                RegisterItembuilder = new AlertDialog.Builder(v.getContext());


                Itemstring litem = (Itemstring) parent.getItemAtPosition(position);
                final String nomitem = litem.getName();
                String qte = litem.getQuantity();
                final String price = litem.getUnitPrice();
                View customView = getLayoutInflater().inflate(R.layout.dialog_modif_item_princ, null, false);

                final ImageView okbtn = customView.findViewById(R.id.mqteitem);
                final Button anul = customView.findViewById(R.id.annuledial);
                final Button moin = customView.findViewById(R.id.moins);
                final Button plu = customView.findViewById(R.id.plus);
                final TextView textqte = customView.findViewById(R.id.qteit);

                textqte.setText(qte);

                moin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Double laqte = Double.valueOf(textqte.getText().toString());
                        //soustraire(laqte);
                        if (laqte > 0) {
                            laqte -= 1;
                        }
                        textqte.setText(String.valueOf(laqte));
                    }
                });

                plu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Double laqte = Double.valueOf(textqte.getText().toString());
                        //soustraire(laqte);

                        laqte += 1;

                        textqte.setText(String.valueOf(laqte));
                    }
                });

                RegisterItembuilder.setCancelable(true);
                RegisterItembuilder.setView(customView);
                RegisterItembuilder.setTitle(nomitem);


                RegisterItemDialog = RegisterItembuilder.create();


                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(getApplicationContext(),""+textqte.getText().toString(),Toast.LENGTH_SHORT).show();
                        Itemstring item = new Itemstring();
                        double prix = Double.parseDouble(price);
                        double qte = Double.parseDouble(textqte.getText().toString());
                        double total = qte * prix;
                        item.setName(nomitem);
                        item.setUnitPrice(price);
                        item.setTotalPrice(String.valueOf(total));
                        item.setQuantity(textqte.getText().toString());

                        items.set(position, item);
                        adapterReferenceitems.notifyDataSetChanged();

                        lanote = 0;
                        for (int i = 0; i < items.size(); i++) {
                            String item4 = items.get(i).getTotalPrice();
                            double price = Double.valueOf(item4);
                            int prixe = (int) price;
                            lanote = lanote + prixe;
                        }

                        //totalapayer.setText(""+Config.formaterSolde(lanote));
                        totalapayer.setText("" + lanote);
                        txtResult.setText("1");
                        textView2.setText("");
                        RegisterItemDialog.dismiss();

                    }
                });

                anul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        items.remove(position);
                        adapterReferenceitems.notifyDataSetChanged();

                        lanote = 0;
                        for (int i = 0; i < items.size(); i++) {
                            String item4 = items.get(i).getTotalPrice();
                            double price = Double.valueOf(item4);
                            int prixe = (int) price;
                            lanote = lanote + prixe;
                        }

                        //totalapayer.setText(""+Config.formaterSolde(lanote));
                        totalapayer.setText("" + lanote);
                        txtResult.setText("1");
                        textView2.setText("");
                        RegisterItemDialog.dismiss();
                    }
                });


                RegisterItemDialog.show();

                //Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        nok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taille = items.size();

                if (taille == 0) {
                    Toast.makeText(getApplicationContext(), "Aucun produit enregistré", Toast.LENGTH_LONG).show();
                } else {
                    items.remove(items.size() - 1);
                    adapterReferenceitems.notifyDataSetChanged();
                    lanote = 0;
                    for (int i = 0; i < items.size(); i++) {
                        String item4 = items.get(i).getTotalPrice();

                        double price = Double.valueOf(item4);
                        int prixe = (int) price;

                        lanote = lanote + prixe;
                    }
                    totalapayer.setText("" + lanote);
                    txtResult.setText("1");
                    textView2.setText("");
                }
            }
        });

        nok.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RegisterSupbuilder = new AlertDialog.Builder(v.getContext());
                View customView = getLayoutInflater().inflate(R.layout.dialog_delete_invoice, null, false);

                final TextView textView = customView.findViewById(R.id.text);

                final ImageView okbtn = customView.findViewById(R.id.okdelinv);

                RegisterSupbuilder.setCancelable(true);
                RegisterSupbuilder.setView(customView);
                RegisterSupbuilder.setTitle("Confirmer l'annulation de la facture");


                RegisterSupDialog = RegisterSupbuilder.create();


                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //supprimer();
                        items.removeAll(items);
                        totalapayer.setText("");
                        RegisterSupDialog.dismiss();
                        adapterReferenceitems.notifyDataSetChanged();
                    }
                });


                RegisterSupDialog.show();
                return true;
            }


        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine = txtResult.getText().toString();
                if (chaine.equals("")) {

                } else {
                    chaine = chaine.substring(0, chaine.length() - 1);
                    txtResult.setText("" + chaine);
                }

            }
        });

        cleartext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine = textView2.getText().toString();
                if (chaine.equals("")) {

                } else {
                    chaine = chaine.substring(0, chaine.length() - 1);
                    textView2.setText("" + chaine);
                }

            }
        });


        un.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 1);
                } else {
                    txtResult.append("" + 1);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        deux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 2);
                } else {
                    txtResult.append("" + 2);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        trois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 3);
                } else {
                    txtResult.append("" + 3);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        quatre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 4);
                } else {
                    txtResult.append("" + 4);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        cinq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 5);
                } else {
                    txtResult.append("" + 5);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 6);
                } else {
                    txtResult.append("" + 6);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        sept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 7);
                } else {
                    txtResult.append("" + 7);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        huit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 8);
                } else {
                    txtResult.append("" + 8);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        neuf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 9);
                } else {
                    txtResult.append("" + 9);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("" + 0);
                } else {
                    txtResult.append("" + 0);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });

        /*doublezero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chaine=txtResult.getText().toString();

                if(chaine.contains(decimal)){
                    txtResult.append("");
                }
                else {
                    txtResult.append(""+00);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


            }
        });*/

        virgule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaine = txtResult.getText().toString();

                if (chaine.contains(decimal)) {
                    txtResult.append("");
                } else {
                    txtResult.append("" + decimal);
                }

                //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
        espace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append(" ");
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("A");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("B");
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("C");
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("D");
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("E");
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("F");
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("G");
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("H");
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("I");
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("J");
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("K");
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("L");
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("M");
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("N");
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("O");
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("P");
            }
        });
        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("Q");
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("R");
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("S");
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("T");
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("U");
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("V");
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("W");
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("X");
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("Y");
            }
        });
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("Z");
            }
        });
        tiret8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("_");
            }
        });

        zerol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("0");
            }
        });
        unl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("1");
            }
        });
        deuxl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("2");
            }
        });
        troisl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("3");
            }
        });
        quatrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("4");
            }
        });
        cinql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("5");
            }
        });
        sixl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("6");
            }
        });
        septl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("7");
            }
        });
        huitl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("8");
            }
        });
        neufl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.append("9");
            }
        });

    }

    public void ajoutvente(String r, String pu, String qtetext) {

        //String chaine[]=r.split("     ");
        String laref = r;
        String leprix = pu;

        if (leprix.equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez renseigner le prix de cette référence merci", Toast.LENGTH_LONG).show();

        }

        if (!leprix.equals("")) {

            double prix = Double.parseDouble(leprix);
            double qte = Double.parseDouble(qtetext);
            double total = qte * prix;
            String laqte = String.valueOf(qte);
            String letotal = String.valueOf(total);
            Itemstring item = new Itemstring();
            item.setName(laref);
            item.setUnitPrice(leprix);
            item.setTotalPrice(letotal);
            item.setQuantity(laqte);
            //itemsstring.add(qte+" x "+laref+" ("+leprix+") "+leprix);
            items.add(item);
            //Toast.makeText(getApplicationContext(),""+items,Toast.LENGTH_LONG).show();
            adapterReferenceitems.notifyDataSetChanged();
            //listeitemstring.notifyDataSetChanged();

            lanote = 0;
            for (int i = 0; i < items.size(); i++) {
                String item4 = items.get(i).getTotalPrice();
                double price = Double.valueOf(item4);
                int prixe = (int) price;
                lanote = lanote + prixe;
            }

            //totalapayer.setText(""+Config.formaterSolde(lanote));
            totalapayer.setText("" + lanote);
            txtResult.setText("");
            textView2.setText("");
        }

    }

    public void reduction(String r, String pu, String qtetext) {

        //String chaine[]=r.split("     ");
        String laref = r;
        String leprix = pu;

        if (leprix.equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez renseigner le prix de cette référence merci", Toast.LENGTH_LONG).show();
        }

        if (!leprix.equals("")) {

            double prix = Double.parseDouble(leprix);
            double qte = Double.parseDouble(qtetext);
            double total = qte * prix;
            String laqte = String.valueOf(qte);
            String letotal = String.valueOf(total);
            Itemstring item = new Itemstring();
            item.setName(laref);
            item.setUnitPrice(leprix);
            item.setTotalPrice(letotal);
            item.setQuantity(laqte);
            //itemsstring.add(qte+" x "+laref+" ("+leprix+") "+leprix);
            items.add(item);
            //Toast.makeText(getApplicationContext(),""+items,Toast.LENGTH_LONG).show();
            adapterReferenceitems.notifyDataSetChanged();
            //listeitemstring.notifyDataSetChanged();

            lanote = 0;
            for (int i = 0; i < items.size(); i++) {
                String item4 = items.get(i).getTotalPrice();
                double price = Double.valueOf(item4);
                int prixe = (int) price;
                lanote = lanote + prixe;
            }

            //totalapayer.setText(""+Config.formaterSolde(lanote));
            totalapayer.setText("" + lanote);
            txtResult.setText("");
            textView2.setText("");
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewInterface.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.weebilogo);
        builder.setMessage("Voulez vous sortir de l'application ?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.caisse) {
            Intent intent = new Intent(NewInterface.this, ClientPageActivityNew2SQL.class);
            intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ID, "1");
            intent.putExtra(ClientPageActivityNew2SQL.EXTRA_CLIENT_ARCHIVE, "nonarchived");
            startActivity(intent);
        } else if (id == R.id.export_csv_by_day) {
            Intent intent = new Intent(NewInterface.this, ExportSalesDataCsvByDayActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.clients) {
            Intent intent = new Intent(NewInterface.this, ClientListActivitySQL.class);
            startActivity(intent);

        } else if (id == R.id.databaseversion) {
            SQLiteDatabase db = new DbHelper(context).getWritableDatabase();
            db.getVersion(); // what value do you get here?
            DisplayMetrics metrics = getResources().getDisplayMetrics();

            Toast.makeText(getApplicationContext(),"la version de la base "+db.getVersion()+"\n la dpi : "+metrics.densityDpi,Toast.LENGTH_LONG).show();

        } else if (id == R.id.references) {
            Intent intent = new Intent(NewInterface.this, ReferencesSQL.class);
            startActivity(intent);
        } else if (id == R.id.sales_of_day) {
            Intent intent = new Intent(NewInterface.this, SalesOfDayActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_sales_by_period) {
            Intent intent = new Intent(NewInterface.this, SalesByPeriodActivity.class);
            startActivity(intent);
        }  else if (id == R.id.update_database) {

            Toast.makeText(this,"Veuillez patientez svp ...",Toast.LENGTH_LONG).show();

            dbHelper.updateFacturerDate();
            dbHelper.updateVenteOublierDate();
            dbHelper.updateSuiviPrixDate();
            dbHelper.updateDepotDate();
            dbHelper.updateDateFactureSupp();
            dbHelper.updateDateOp();
            dbHelper.updateDateRef();

            Toast.makeText(this,"Operation Terminer",Toast.LENGTH_LONG).show();

        } /* else if (id == R.id.referencesgros) {
            Intent intent = new Intent(NewInterface.this, ReferencesSQLGrossiste.class);
            startActivity(intent);
        }*/ else if (id == R.id.parametres) {
            Intent intent = new Intent(NewInterface.this, Parametres2.class);
            startActivity(intent);
        } else if (id == R.id.config_imprimante) {
            Intent intent = new Intent(NewInterface.this,  Usbimprime.class);
            startActivity(intent);
        } else if (id == R.id.add_printer) {
            Intent intent = new Intent(NewInterface.this, AddPrinterActivity.class);
            startActivity(intent);
        } else if (id == R.id.facturessup) {
            Intent intent=new Intent(NewInterface.this, DeleteInvoicesListActivitySQL.class);
            startActivity(intent);
        } else if (id == R.id.sauvegarder) {
            File file = null;
            String filePath = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory(), "Weebi/Fichier");
                filePath = file.getAbsolutePath() + File.separator + "MyDBWeebi.db";
                shareGeneratedInvoice(filePath);
            }
        } else if (id == R.id.password) {

            final Button clear,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero, clearnum;

            RegisterCodebuilder = new AlertDialog.Builder(NewInterface.this);
            View customView = getLayoutInflater().inflate(R.layout.dialog_update_code, null, false);

            final FormEditText code = customView.findViewById(R.id.codee);
            final FormEditText codemaj = customView.findViewById(R.id.codeemaj);

            final ImageView okbtn = customView.findViewById(R.id.oknewcode);
            un= customView.findViewById(R.id.btnNum1Id);
            deux= customView.findViewById(R.id.btnNum2Id);
            trois= customView.findViewById(R.id.btnNum3Id);
            quatre= customView.findViewById(R.id.btnNum4Id);
            cinq= customView.findViewById(R.id.btnNum5Id);
            six= customView.findViewById(R.id.btnNum6Id);
            sept= customView.findViewById(R.id.btnNum7Id);
            huit= customView.findViewById(R.id.btnNum8Id);
            neuf= customView.findViewById(R.id.btnNum9Id);
            clearnum= customView.findViewById(R.id.btnClearId);
            zero= customView.findViewById(R.id.btnNum0Id);

            code.setInputType(0);
            codemaj.setInputType(0);

            code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    zero.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+0);
                            }
                            else {
                                code.append(""+0);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                    un.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+1);
                            }
                            else {
                                code.append(""+1);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                    deux.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+2);
                            }
                            else {
                                code.append(""+2);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    trois.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+3);
                            }
                            else {
                                code.append(""+3);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    quatre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+4);
                            }
                            else {
                                code.append(""+4);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    cinq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+5);
                            }
                            else {
                                code.append(""+5);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    six.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+6);
                            }
                            else {
                                code.append(""+6);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    sept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+7);
                            }
                            else {
                                code.append(""+7);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    huit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+8);
                            }
                            else {
                                code.append(""+8);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    neuf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=code.getText().toString();

                            if(chaine.contains("")){
                                code.append(""+9);
                            }
                            else {
                                code.append(""+9);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    clearnum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String chaine=code.getText().toString();
                            if (chaine.equals("")){

                            }else {
                                chaine = chaine.substring(0, chaine.length()-1);
                                code.setText(""+chaine);
                            }

                        }
                    });
                }
            });

            codemaj.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    code.setVisibility(View.GONE);
                    zero.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+0);
                            }
                            else {
                                codemaj.append(""+0);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                    un.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+1);
                            }
                            else {
                                codemaj.append(""+1);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                    deux.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+2);
                            }
                            else {
                                codemaj.append(""+2);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    trois.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+3);
                            }
                            else {
                                codemaj.append(""+3);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    quatre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+4);
                            }
                            else {
                                codemaj.append(""+4);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    cinq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+5);
                            }
                            else {
                                codemaj.append(""+5);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    six.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+6);
                            }
                            else {
                                codemaj.append(""+6);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    sept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+7);
                            }
                            else {
                                codemaj.append(""+7);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    huit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+8);
                            }
                            else {
                                codemaj.append(""+8);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    neuf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String chaine=codemaj.getText().toString();

                            if(chaine.contains("")){
                                codemaj.append(""+9);
                            }
                            else {
                                codemaj.append(""+9);
                            }

                            //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                    clearnum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String chaine=codemaj.getText().toString();
                            if (chaine.equals("")){

                            }else {
                                chaine = chaine.substring(0, chaine.length()-1);
                                codemaj.setText(""+chaine);
                            }

                        }
                    });
                }
            });

            RegisterCodebuilder.setCancelable(true);
            RegisterCodebuilder.setView(customView);
            RegisterCodebuilder.setTitle(("Changer le code"));


            RegisterCodeDialog = RegisterCodebuilder.create();

            // nameref.setText(lareffe);


            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //RegisterUpdateRef(nameref, RegisterRefdialog);
                    String lecode=code.getText().toString();

                    Cursor bouti=dbHelper.getBoutique("1");
                    if(bouti.moveToFirst()){
                        boutiqcode=bouti.getString(4);
                    }

                    if(!lecode.equals("")){
                        if(boutiqcode.equals("")){
                            codemaj.setVisibility(VISIBLE);
                            String codemise=codemaj.getText().toString();
                            if(codemise.equals("")){
                                Toast.makeText(getApplicationContext(),"Mettre le nouveau code s'il vous plait",Toast.LENGTH_LONG).show();
                            }
                            if(!codemise.equals("")){
                                RegisterCodeDialog.dismiss();//terminer l'update et relancer l'activité
                                Toast.makeText(getApplicationContext(),"Code enregistré",Toast.LENGTH_LONG).show();
                                dbHelper.majcode(codemise,"1");
                            }

                        }

                        if(lecode.equals(boutiqcode)){
                            codemaj.setVisibility(VISIBLE);
                            String codemise=codemaj.getText().toString();
                            if(codemise.equals("")){
                                Toast.makeText(getApplicationContext(),"Mettre le nouveau code s'il vous plait",Toast.LENGTH_SHORT).show();
                            }
                            if(!codemise.equals("")){
                                RegisterCodeDialog.dismiss();//terminer l'update et relancer l'activité
                                Toast.makeText(getApplicationContext(),"Code enregistré",Toast.LENGTH_LONG).show();
                                dbHelper.majcode(codemise,"1");
                            }
                        }
                        if(!lecode.equals(boutiqcode)){
                            RegisterCodeDialog.dismiss();//terminer l'update et relancer l'activité
                            Toast.makeText(getApplicationContext(),"Code incorrect",Toast.LENGTH_LONG).show();
                        }
                    }

                    if(lecode.equals("")){
                        Toast.makeText(getApplicationContext(),"Merci de renseigner le code",Toast.LENGTH_LONG).show();
                    }

                }
            });

            RegisterCodeDialog.show();

        }else if (id == R.id.impsd) {
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
            }*/

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
                                from.renameTo(to);*/
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
            }
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //declaration des permissions que l'application à besoin pour que l'utilisateur valide les permissions
    private boolean checkAndRequestPermissions() {
        int permissionLecture = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLecture != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                }
                break;
        }
    }

    public void onActionClick(View view) {
        switch (view.getId()) {
            case R.id.Blist_clientb:

                break;
            case R.id.Blist_ref:
                Intent intent1 = new Intent(this, ReferencesSQL.class);
                startActivity(intent1);
                break;
            case R.id.Bstats:
                //Toast.makeText(getApplicationContext(),"liste",Toast.LENGTH_SHORT).show();
                // launch the activity to view client list.

                break;
            case R.id.Bparam:
                // onRegisterBoutik(false);

                break;
            default:
                break;
        }
    }

    private void shareGeneratedInvoice(String filePath) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(new File(filePath));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("text/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Envoyer"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        resultatsadapt.clear();
        referencesadapt.clear();
        resultatsadapt=dbHelper.getAllReference();
        for(LaReffb laReffb : resultatsadapt){
            LaReffb lar=new LaReffb(laReffb.getLaref(),laReffb.getLarefprice(),laReffb.getLarefqte(), laReffb.getId(), laReffb.getLienimg());

            referencesadapt.add(lar);
        }
        adapterReference.notifyDataSetChanged();
    }

    public class AdapterReference extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;

        private List<LaReffb> references;
        private ArrayList<LaReffb> arraylist;
        private Context context;

        public AdapterReference(Activity activity, List<LaReffb> references) {
            this.activity = activity;
            this.references = references;
            this.arraylist = new ArrayList<>();
            this.arraylist.addAll(references);
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
                convertView = inflater.inflate(R.layout.client_item_view_reference, null);


            TextView nomref = convertView.findViewById(R.id.refname);
            TextView prixref = convertView.findViewById(R.id.prixref);
            TextView qteref = convertView.findViewById(R.id.qteref);
            ImageView imageView = convertView.findViewById(R.id.imagee);

            LaReffb laReffb = references.get(position);
            nomref.setText(laReffb.getLaref());
            prixref.setText(laReffb.getLarefprice());
            qteref.setText(Config.formaterQte(laReffb.getLarefqte())/*+" : : "+laReffb.getId()*/);

            String Images = ".photos";
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Weebi/Images/" + laReffb.getId() + ".png");
            //File myDir = new File(root + "/Weebi/" + Images + "/" + laReffb.getId() + ".png");
            //File myDir = new File(root + "/Weebi/images/" + laReffb.getLaref() + ".png");
            Picasso.with(getBaseContext()).load(myDir).resize(100, 100).centerCrop().into(imageView);

            return convertView;
        }

        public void filter(String cs) {
            cs = cs.toLowerCase(Locale.getDefault());
            references.clear();
            if (cs.length() == 0) {
                references.addAll(arraylist);
            } else {
                for (LaReffb wp : arraylist) {
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

    public class AdapterReferenceItems extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;

        private List<Itemstring> items;
        private Context context;

        public AdapterReferenceItems(Activity activity, List<Itemstring> items) {
            this.activity = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
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
                convertView = inflater.inflate(R.layout.client_item_view_item, null);


            TextView nomref = convertView.findViewById(R.id.itemname);
            //TextView prixref=(TextView)convertView.findViewById(R.id.prixitem);
            TextView qte = convertView.findViewById(R.id.qte);
            TextView soustot = convertView.findViewById(R.id.soustotal);

            Itemstring item = items.get(position);
            double unit = Double.valueOf(item.getUnitPrice());
            double qtte = Double.valueOf(item.getQuantity());
            double tot = unit * qtte;
            int tote = (int) tot;

            // testtaille(item.getName());

            nomref.setText(item.getName());
            //prixref.setText(item.getUnitPrice());
            qte.setText(item.getQuantity());
            soustot.setText(String.valueOf(Config.formaterSolde(tote)));


            return convertView;
        }
    }

    public class AdapterClient extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;
        private List<Client> clients;
        private ArrayList<Client> arrayliste;
        private Context context;

        public AdapterClient(Activity activity, List<Client> clientRepositories) {
            this.activity = activity;
            this.clients = clientRepositories;
            this.arrayliste = new ArrayList<>();
            this.arrayliste.addAll(clientRepositories);
        }

        @Override
        public int getCount() {
            return clients.size();
        }

        @Override
        public Object getItem(int position) {
            return clients.get(position);
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
                convertView = inflater.inflate(R.layout.client_item_view2, null);


            final TextView lenom = convertView.findViewById(R.id.client_name);
            final TextView phone = convertView.findViewById(R.id.client_phone);
            Button cash = convertView.findViewById(R.id.cash1);
            Button credit = convertView.findViewById(R.id.credit1);
            final TextView id = convertView.findViewById(R.id.clientidl);

            Client clientRepository = clients.get(position);
            lenom.setText(clientRepository.getNom());
            phone.setText(clientRepository.getNumero());
            id.setText(clientRepository.getId());
            final String idc = clientRepository.getId();


            credit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor cursor = dbHelper.getClient(idc);

                    idcli = id.getText().toString();
                    //Toast.makeText(getApplicationContext(),""+idcli,Toast.LENGTH_LONG).show();

                    choixcli.setText(lenom.getText());
                    modepaiement.setText("Crédit");
                    lenumero = phone.getText().toString();
                    Listedialog.dismiss();

                }
            });

            cash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor cursor = dbHelper.getClient(idc);
                    if (cursor.moveToFirst()) {
                        String lesolde1 = cursor.getString(3);
                        lesolde = Integer.valueOf(lesolde1);
                    }
                    idcli = id.getText().toString();
                    //Toast.makeText(getApplicationContext(),""+idcli,Toast.LENGTH_LONG).show();

                    choixcli.setText(lenom.getText());
                    modepaiement.setText("Cash");
                    lenumero = phone.getText().toString();

                    Listedialog.dismiss();

                }
            });


            return convertView;
        }

        public void filter(String cs) {
            cs = cs.toLowerCase(Locale.getDefault());
            clients.clear();
            if (cs.length() == 0) {
                clients.addAll(arrayliste);
            } else {
                for (Client wp : arrayliste) {
                    if (wp.getNom().toLowerCase(Locale.getDefault())
                            .startsWith(cs)) {
                        clients.add(wp);
                    }
                    if (wp.getNumero().toLowerCase(Locale.getDefault())
                            .startsWith(cs)) {
                        clients.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}