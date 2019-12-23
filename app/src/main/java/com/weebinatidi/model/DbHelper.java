package com.weebinatidi.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.weebinatidi.ui.weebi2.Itemstring;
import com.weebinatidi.utils.DateUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mbp on 06/09/2017.
 * A chaque fois qu'on met à jour la base de donnée, on doit incrementer le numero de la version et on
 * doit changer le onCreate et le onUpgrade...
 * si on crée une nouvelle table :
 * - on doit le faire dans le oncreate pour que les nouveaux utilisateurs puisse
 * avoir la structure complete de la base
 * - on doit le faire dans le onupgrade pour que les utilisateurs qui ont déjà installés l'application puisse avoir
 * la nouvelle structure de la bdd
 * pareille quand on doit ajouter une colonne ou la supprimer dans une table
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBWeebi.db";


    public static final String TABLE_REFERENCE = "referenc";
    public static final String COLUMN_ID = "idref";
    public static final String COLUMN_NAME = "nomref";
    public static final String COLUMN_PRIX = "prixref";
    public static final String COLUMN_QTE = "qterefe";
    public static final String COLUMN_IMG = "img";
    public static final String COLUMN_DATEER = "dateer";
    public static final String COLUMN_CONTEXTE = "contexte";
    public static final String COLUMN_PRIORITE = "priorite";

    public static final String COLUMN_CATEG = "categorie";


    public static final String TABLE_CLIENT = "clients";
    public static final String COLUMN_ID_CLIENT = "idclient";
    public static final String COLUMN_NAME_CLIENT = "nomclient";
    public static final String COLUMN_NUMERO = "numero";
    public static final String COLUMN_SOLDE = "solde";

    public static final String TABLE_BOUTIQUE = "boutique";
    public static final String BOUTIQUE_ID = "idbout";
    public static final String BOUTIQUE_NAME = "nomboutique";
    public static final String BOUTIQUE_NUMERO = "numboutique";
    public static final String BOUTIQUE_MAIL = "mail";
    public static final String BOUTIQUE_CODE = "code";
    public static final String BOUTIQUE_ADRES = "adresse";
    // public static final String BOUTIQUE_LOGO = "image";

    public static final String TABLE_FACTURE = "facture";
    public static final String COLUMN_ID_FACT = "idfact";
    public static final String COLUMN_TOTAL_FACT = "totalfact";
    public static final String COLUMN_DATE_FACT = "datefact";
    public static final String COLUMN_IDCLI_FACT = "idclient";
    public static final String COLUMN_TYPE_FACT = "typefact";

    public static final String TABLE_FACTURER = "facturer";
    public static final String COLUMN_ID_FACTR = "idfactr";
    public static final String COLUMN_TOTAL_FACTR = "totalfactr";
    public static final String COLUMN_DATE_FACTR = "datefactr";
    public static final String COLUMN_IDCLI_FACTR = "idclientr";
    public static final String COLUMN_TYPE_FACTR = "typefactr";

    public static final String TABLE_OPERATION = "operations";
    public static final String COLUMN_MONT = "montop";
    public static final String COLUMN_DATE = "dateop";
    public static final String COLUMN_CLIENT_OP = "clientidop";
    public static final String COLUMN_NOM_OP = "nomop";
    public static final String COLUMN_OP_ID = "idop";

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID_ITEM = "iditem";
    public static final String COLUMN_NOM_ITEM = "nomitem";
    public static final String COLUMN_PU = "prixunit";
    public static final String COLUMN_QTE_CHOISI = "qtechoisit";
    public static final String COLUMN_IDFACT = "idfact";
    public static final String COLUMN_SOUSTOT = "soustotal";

    public static final String TABLE_DEPOTS = "depots";
    public static final String COLUMN_ID_DEPOT = "iddepot";
    public static final String COLUMN_MONT_DEPOT = "montantdepot";
    public static final String COLUMN_ID_CLI = "idclient";
    public static final String COLUMN_DATEDEP = "datedepot";
    public static final String COLUMN_TYPE_DEP = "typedep";

    public static final String TABLE_REFWEEBI = "refcharges";
    public static final String TABLE_REFSERV = "refserveur";
    public static final String COLUMN_ID_RW = "idrefw";
    public static final String COLUMN_NOM_RW = "nomrefw";
    public static final String COLUMN_IMGW = "lienimg";
    public static final String COLUMN_PRICE = "prixw";
    public static final String COLUMN_IDW = "idkeyrw";

    public static final String TABLE_SUIVI_REF = "suiviprixqte";
    public static final String COLUMN_ID_SUI = "idsuiv";
    public static final String COLUMN_DATE_SUI = "datesuiv";
    public static final String COLUMN_PRIX_S = "prixsuiv";
    public static final String COLUMN_QTE_S = "qtesuiv";
    public static final String COLUMN_IDREF_S = "idrefsuivi";
    public static final String COLUMN_CONT_S = "contexte";

    public static final String COLUMN_RED_S = "reduction";

    public static final String COLUMN_TVA_S = "tvaapplique";

    public static final String TABLE_VENTES_OUBLIER = "venteoublier";
    public static final String COLUMN_ID_VENTE_OUBLIER = "idventeoublier";
    public static final String COLUMN_PRIX_OUBLIER = "prixoublier";
    public static final String COLUMN_DATE_AJOUT = "dateajout";

    public static final String TABLE_FACTS = "facturessupp";
    public static final String COLUMN_ID_FACTSS = "idfaS";
    public static final String COLUMN_ID_FACTS = "idfacts";
    public static final String COLUMN_TOTAL_FACTS = "totalfacts";
    public static final String COLUMN_DATE_FACTS = "datefacts";
    public static final String COLUMN_DATE_SUPP = "datedesup";
    public static final String COLUMN_NOMCLI = "nomclient";
    public static final String COLUMN_NUMCLI = "numclient";
    public static final String COLUMN_TYPE_FACTS = "typefact";

    public static final String TABLE_ITEMSS = "itemss";
    public static final String COLUMN_ID_ITEMS = "iditems";
    public static final String COLUMN_NOM_ITEMS = "nomitems";
    public static final String COLUMN_PUS = "prixunits";
    public static final String COLUMN_QTE_SUPP = "qtechoisits";
    public static final String COLUMN_IDFACTS = "idfacts";
    public static final String COLUMN_SOUSTOTS = "soustotals";
    private static final Object FILE_DIR = "Weebi/Fichier";


    /*public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 14 );
    }*/

    public DbHelper(final Context context) {
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR
                + File.separator + DATABASE_NAME, null, 34);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REFERENCES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_REFERENCE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_PRIX + " TEXT,"
                + COLUMN_QTE + " TEXT,"
                + COLUMN_IMG + " TEXT,"
                + COLUMN_DATEER + " TEXT,"
                + COLUMN_CONTEXTE + " TEXT,"
                + COLUMN_CATEG + " TEXT,"
                + COLUMN_PRIORITE + " TEXT" + ")";


        String CREATE_CLIENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENT + "("
                + COLUMN_ID_CLIENT + " INTEGER PRIMARY KEY,"
                + COLUMN_NUMERO + " TEXT,"
                + COLUMN_NAME_CLIENT + " TEXT,"
                + COLUMN_SOLDE + " TEXT" + ")";

        String CREATE_BOUTIQUE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BOUTIQUE + "("
                + BOUTIQUE_ID + " INTEGER PRIMARY KEY,"
                + BOUTIQUE_NUMERO + " TEXT,"
                + BOUTIQUE_NAME + " TEXT,"
                + BOUTIQUE_MAIL + " TEXT,"
                + BOUTIQUE_CODE + " TEXT,"
                + BOUTIQUE_ADRES + " TEXT" + ")";

        String CREATE_OPERATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OPERATION + "("
                + COLUMN_OP_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NOM_OP + " TEXT,"
                + COLUMN_DATE + " TEXT," +
                COLUMN_MONT + " TEXT," +
                COLUMN_CLIENT_OP + " TEXT" + ")";

        String CREATE_FACTURE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FACTURE + "("
                + COLUMN_ID_FACT + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE_FACT + " TEXT,"
                + COLUMN_TOTAL_FACT + " TEXT,"
                + COLUMN_IDCLI_FACT + " TEXT,"
                + COLUMN_TYPE_FACT + " TEXT," +
                " FOREIGN KEY (" + COLUMN_IDCLI_FACT + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))";

        String CREATE_FACTURER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FACTURER + "("
                + COLUMN_ID_FACTR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE_FACTR + " TEXT,"
                + COLUMN_TOTAL_FACTR + " TEXT,"
                + COLUMN_IDCLI_FACTR + " TEXT,"
                + COLUMN_TYPE_FACTR + " TEXT," +
                " FOREIGN KEY (" + COLUMN_IDCLI_FACTR + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))";

        String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "("
                + COLUMN_ID_ITEM + " INTEGER PRIMARY KEY,"
                + COLUMN_NOM_ITEM + " TEXT,"
                + COLUMN_PU + " TEXT,"
                + COLUMN_QTE_CHOISI + " TEXT,"
                + COLUMN_SOUSTOT + " TEXT,"
                + COLUMN_IDFACT + " TEXT" + ")";

        String CREATE_DEPOTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DEPOTS + "("
                + COLUMN_ID_DEPOT + " INTEGER PRIMARY KEY,"
                + COLUMN_MONT_DEPOT + " TEXT,"
                + COLUMN_DATEDEP + " TEXT,"
                + COLUMN_TYPE_DEP + " TEXT,"
                + COLUMN_ID_CLI + " TEXT," +
                " FOREIGN KEY (" + COLUMN_ID_CLI + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))";


        String CREATE_VENTES_OUBLIER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_VENTES_OUBLIER + "("
                + COLUMN_ID_VENTE_OUBLIER + " INTEGER PRIMARY KEY,"
                + COLUMN_PRIX_OUBLIER + " TEXT ,"
                + COLUMN_DATE_AJOUT + " TEXT " +
                ")";

        String CREATE_REFW_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_REFWEEBI + "("
                + COLUMN_ID_RW + " INTEGER PRIMARY KEY,"
                + COLUMN_NOM_RW + " TEXT," + COLUMN_IMGW + " TEXT," + COLUMN_PRICE + " TEXT," + COLUMN_IDW + " TEXT" + ")";


        String CREATE_FACTURES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FACTS + "("
                + COLUMN_ID_FACTS + " INTEGER PRIMARY KEY,"
                + COLUMN_ID_FACTSS + " TEXT,"
                + COLUMN_DATE_FACTS + " TEXT,"
                + COLUMN_TOTAL_FACTS + " TEXT,"
                + COLUMN_DATE_SUPP + " TEXT,"
                + COLUMN_NOMCLI + " TEXT,"
                + COLUMN_NUMCLI + " TEXT,"
                + COLUMN_TYPE_FACTS + " TEXT" + ")";


        String CREATE_ITEMSS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMSS + "("
                + COLUMN_ID_ITEMS + " INTEGER PRIMARY KEY,"
                + COLUMN_NOM_ITEMS + " TEXT,"
                + COLUMN_PUS + " TEXT,"
                + COLUMN_QTE_SUPP + " TEXT,"
                + COLUMN_IDFACTS + " TEXT,"
                + COLUMN_SOUSTOTS + " TEXT" + ")";

        String CREATE_TABLE_SUIVR = "CREATE TABLE IF NOT EXISTS " + TABLE_SUIVI_REF + "("
                + COLUMN_ID_SUI + " INTEGER PRIMARY KEY,"
                + COLUMN_DATE_SUI + " TEXT,"
                + COLUMN_PRIX_S + " TEXT,"
                + COLUMN_QTE_S + " TEXT,"
                + COLUMN_IDREF_S + " TEXT,"
                + COLUMN_CONT_S + " TEXT,"
                + COLUMN_TVA_S + " TEXT" + ")";


        db.execSQL(CREATE_REFERENCES_TABLE);
        db.execSQL(CREATE_CLIENT_TABLE);
        db.execSQL(CREATE_BOUTIQUE_TABLE);
        db.execSQL(CREATE_OPERATION_TABLE);
        db.execSQL(CREATE_FACTURE_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_DEPOTS_TABLE);
        db.execSQL(CREATE_VENTES_OUBLIER_TABLE);
        db.execSQL(CREATE_REFW_TABLE);
        db.execSQL(CREATE_FACTURES_TABLE);
        db.execSQL(CREATE_ITEMSS_TABLE);
        db.execSQL(CREATE_TABLE_SUIVR);
        db.execSQL(CREATE_FACTURER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        if (oldVersion == 30 ){
//            db.execSQL("ALTER TABLE " + TABLE_BOUTIQUE + " ADD COLUMN " + BOUTIQUE_LOGO + " TEXT "); // Ajout
//        }

        if (oldVersion == 27) {
            db.execSQL("ALTER TABLE " + TABLE_REFERENCE + " ADD COLUMN " + COLUMN_CONTEXTE + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_BOUTIQUE + " ADD COLUMN " + BOUTIQUE_CODE + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_REFERENCE + " ADD COLUMN " + COLUMN_CATEG + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_REFERENCE + " ADD COLUMN " + COLUMN_PRIORITE + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_REFWEEBI + " RENAME TO " + TABLE_REFSERV);
            db.execSQL("ALTER TABLE " + TABLE_BOUTIQUE + " ADD COLUMN " + BOUTIQUE_ADRES + " TEXT ");


            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ITEMSS + "("
                    + COLUMN_ID_ITEMS + " INTEGER PRIMARY KEY,"
                    + COLUMN_NOM_ITEMS + " TEXT,"
                    + COLUMN_PUS + " TEXT,"
                    + COLUMN_QTE_SUPP + " TEXT,"
                    + COLUMN_IDFACTS + " TEXT,"
                    + COLUMN_SOUSTOTS + " TEXT" + ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTS + "("
                    + COLUMN_ID_FACTS + " INTEGER PRIMARY KEY,"
                    + COLUMN_ID_FACTSS + " TEXT,"
                    + COLUMN_DATE_FACTS + " TEXT,"
                    + COLUMN_TOTAL_FACTS + " TEXT,"
                    + COLUMN_DATE_SUPP + " TEXT,"
                    + COLUMN_NOMCLI + " TEXT,"
                    + COLUMN_NUMCLI + " TEXT,"
                    + COLUMN_TYPE_FACTS + " TEXT" + ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUIVI_REF + "("
                    + COLUMN_ID_SUI + " INTEGER PRIMARY KEY,"
                    + COLUMN_DATE_SUI + " TEXT,"
                    + COLUMN_PRIX_S + " TEXT,"
                    + COLUMN_QTE_S + " TEXT,"
                    + COLUMN_IDREF_S + " TEXT,"
                    + COLUMN_CONT_S + " TEXT,"
                    + COLUMN_TVA_S + " TEXT" + ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DATE_FACTR + " TEXT,"
                    + COLUMN_TOTAL_FACTR + " TEXT,"
                    + COLUMN_IDCLI_FACTR + " TEXT,"
                    + COLUMN_TYPE_FACTR + " TEXT," +
                    " FOREIGN KEY (" + COLUMN_IDCLI_FACTR + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))");

            db.execSQL("INSERT INTO " +
                    TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " , "
                    + COLUMN_DATE_FACTR + " , "
                    + COLUMN_TOTAL_FACTR + " , "
                    + COLUMN_IDCLI_FACTR + " , "
                    + COLUMN_TYPE_FACTR + ")" +
                    " SELECT "
                    + COLUMN_ID_FACT + " , "
                    + COLUMN_DATE_FACT + " , "
                    + COLUMN_TOTAL_FACT + " , "
                    + COLUMN_IDCLI_FACT + " , "
                    + COLUMN_TYPE_FACT +
                    " FROM " + TABLE_FACTURE);
        }

        if (oldVersion == 28) {
            db.execSQL("ALTER TABLE " + TABLE_REFERENCE + " ADD COLUMN " + COLUMN_CATEG + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_REFERENCE + " ADD COLUMN " + COLUMN_PRIORITE + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_REFWEEBI + " RENAME TO " + TABLE_REFSERV);
            db.execSQL("ALTER TABLE " + TABLE_FACTS + " ADD COLUMN " + COLUMN_ID_FACTSS + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_BOUTIQUE + " ADD COLUMN " + BOUTIQUE_ADRES + " TEXT ");
            //db.execSQL("ALTER TABLE " +TABLE_FACTURE+" MODIFY  COLUMN "+BOUTIQUE_ADRES );
            // ALTER TABLE document MODIFY COLUMN document_id INT auto_increment

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUIVI_REF + "("
                    + COLUMN_ID_SUI + " INTEGER PRIMARY KEY,"
                    + COLUMN_DATE_SUI + " TEXT,"
                    + COLUMN_PRIX_S + " TEXT,"
                    + COLUMN_QTE_S + " TEXT,"
                    + COLUMN_IDREF_S + " TEXT,"
                    + COLUMN_CONT_S + " TEXT,"
                    + COLUMN_TVA_S + " TEXT" + ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DATE_FACTR + " TEXT,"
                    + COLUMN_TOTAL_FACTR + " TEXT,"
                    + COLUMN_IDCLI_FACTR + " TEXT,"
                    + COLUMN_TYPE_FACTR + " TEXT," +
                    " FOREIGN KEY (" + COLUMN_IDCLI_FACTR + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))");

            db.execSQL("INSERT INTO " +
                    TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " , "
                    + COLUMN_DATE_FACTR + " , "
                    + COLUMN_TOTAL_FACTR + " , "
                    + COLUMN_IDCLI_FACTR + " , "
                    + COLUMN_TYPE_FACTR + ")" +
                    " SELECT "
                    + COLUMN_ID_FACT + " , "
                    + COLUMN_DATE_FACT + " , "
                    + COLUMN_TOTAL_FACT + " , "
                    + COLUMN_IDCLI_FACT + " , "
                    + COLUMN_TYPE_FACT +
                    " FROM " + TABLE_FACTURE);
        }

        if (oldVersion == 29) {
            db.execSQL("ALTER TABLE " + TABLE_REFERENCE + " ADD COLUMN " + COLUMN_CATEG + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_SUIVI_REF + " ADD COLUMN " + COLUMN_TVA_S + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_REFERENCE + " ADD COLUMN " + COLUMN_PRIORITE + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_REFWEEBI + " RENAME TO " + TABLE_REFSERV);
            db.execSQL("ALTER TABLE " + TABLE_FACTS + " ADD COLUMN " + COLUMN_ID_FACTSS + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_BOUTIQUE + " ADD COLUMN " + BOUTIQUE_ADRES + " TEXT ");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DATE_FACTR + " TEXT,"
                    + COLUMN_TOTAL_FACTR + " TEXT,"
                    + COLUMN_IDCLI_FACTR + " TEXT,"
                    + COLUMN_TYPE_FACTR + " TEXT," +
                    " FOREIGN KEY (" + COLUMN_IDCLI_FACTR + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))");

            db.execSQL("INSERT INTO " +
                    TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " , "
                    + COLUMN_DATE_FACTR + " , "
                    + COLUMN_TOTAL_FACTR + " , "
                    + COLUMN_IDCLI_FACTR + " , "
                    + COLUMN_TYPE_FACTR + ")" +
                    " SELECT "
                    + COLUMN_ID_FACT + " , "
                    + COLUMN_DATE_FACT + " , "
                    + COLUMN_TOTAL_FACT + " , "
                    + COLUMN_IDCLI_FACT + " , "
                    + COLUMN_TYPE_FACT +
                    " FROM " + TABLE_FACTURE);
        }
        if (oldVersion == 30) {
            db.execSQL("ALTER TABLE " + TABLE_REFERENCE + " ADD COLUMN " + COLUMN_PRIORITE + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_REFWEEBI + " RENAME TO " + TABLE_REFSERV);
            db.execSQL("ALTER TABLE " + TABLE_FACTS + " ADD COLUMN " + COLUMN_ID_FACTSS + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_BOUTIQUE + " ADD COLUMN " + BOUTIQUE_ADRES + " TEXT ");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DATE_FACTR + " TEXT,"
                    + COLUMN_TOTAL_FACTR + " TEXT,"
                    + COLUMN_IDCLI_FACTR + " TEXT,"
                    + COLUMN_TYPE_FACTR + " TEXT," +
                    " FOREIGN KEY (" + COLUMN_IDCLI_FACTR + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))");

            db.execSQL("INSERT INTO " +
                    TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " , "
                    + COLUMN_DATE_FACTR + " , "
                    + COLUMN_TOTAL_FACTR + " , "
                    + COLUMN_IDCLI_FACTR + " , "
                    + COLUMN_TYPE_FACTR + ")" +
                    " SELECT "
                    + COLUMN_ID_FACT + " , "
                    + COLUMN_DATE_FACT + " , "
                    + COLUMN_TOTAL_FACT + " , "
                    + COLUMN_IDCLI_FACT + " , "
                    + COLUMN_TYPE_FACT +
                    " FROM " + TABLE_FACTURE);
        }
        if (oldVersion == 31) {
            db.execSQL("ALTER TABLE " + TABLE_REFWEEBI + " RENAME TO " + TABLE_REFSERV);
            db.execSQL("ALTER TABLE " + TABLE_FACTS + " ADD COLUMN " + COLUMN_ID_FACTSS + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_BOUTIQUE + " ADD COLUMN " + BOUTIQUE_ADRES + " TEXT ");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DATE_FACTR + " TEXT,"
                    + COLUMN_TOTAL_FACTR + " TEXT,"
                    + COLUMN_IDCLI_FACTR + " TEXT,"
                    + COLUMN_TYPE_FACTR + " TEXT," +
                    " FOREIGN KEY (" + COLUMN_IDCLI_FACTR + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))");

            db.execSQL("INSERT INTO " +
                    TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " , "
                    + COLUMN_DATE_FACTR + " , "
                    + COLUMN_TOTAL_FACTR + " , "
                    + COLUMN_IDCLI_FACTR + " , "
                    + COLUMN_TYPE_FACTR + ")" +
                    " SELECT "
                    + COLUMN_ID_FACT + " , "
                    + COLUMN_DATE_FACT + " , "
                    + COLUMN_TOTAL_FACT + " , "
                    + COLUMN_IDCLI_FACT + " , "
                    + COLUMN_TYPE_FACT +
                    " FROM " + TABLE_FACTURE);
        }
        if (oldVersion == 32) {
            db.execSQL("ALTER TABLE " + TABLE_FACTS + " ADD COLUMN " + COLUMN_ID_FACTSS + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_BOUTIQUE + " ADD COLUMN " + BOUTIQUE_ADRES + " TEXT ");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DATE_FACTR + " TEXT,"
                    + COLUMN_TOTAL_FACTR + " TEXT,"
                    + COLUMN_IDCLI_FACTR + " TEXT,"
                    + COLUMN_TYPE_FACTR + " TEXT," +
                    " FOREIGN KEY (" + COLUMN_IDCLI_FACTR + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))");

            db.execSQL("INSERT INTO " +
                    TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " , "
                    + COLUMN_DATE_FACTR + " , "
                    + COLUMN_TOTAL_FACTR + " , "
                    + COLUMN_IDCLI_FACTR + " , "
                    + COLUMN_TYPE_FACTR + ")" +
                    " SELECT "
                    + COLUMN_ID_FACT + " , "
                    + COLUMN_DATE_FACT + " , "
                    + COLUMN_TOTAL_FACT + " , "
                    + COLUMN_IDCLI_FACT + " , "
                    + COLUMN_TYPE_FACT +
                    " FROM " + TABLE_FACTURE);
        }

        if (oldVersion == 33) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DATE_FACTR + " TEXT,"
                    + COLUMN_TOTAL_FACTR + " TEXT,"
                    + COLUMN_IDCLI_FACTR + " TEXT,"
                    + COLUMN_TYPE_FACTR + " TEXT," +
                    " FOREIGN KEY (" + COLUMN_IDCLI_FACTR + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID_CLIENT + "))");

            db.execSQL("INSERT INTO " +
                    TABLE_FACTURER + "("
                    + COLUMN_ID_FACTR + " , "
                    + COLUMN_DATE_FACTR + " , "
                    + COLUMN_TOTAL_FACTR + " , "
                    + COLUMN_IDCLI_FACTR + " , "
                    + COLUMN_TYPE_FACTR + ")" +
                    " SELECT "
                    + COLUMN_ID_FACT + " , "
                    + COLUMN_DATE_FACT + " , "
                    + COLUMN_TOTAL_FACT + " , "
                    + COLUMN_IDCLI_FACT + " , "
                    + COLUMN_TYPE_FACT +
                    " FROM " + TABLE_FACTURE);
        }

        onCreate(db);

        Log.d("laversion de la base", "" + db.getVersion());
    }


    /***
     *  Enregistre une vente oublier.
     *  @param prixOublier
     *  @param date
     *  @author birante sy
     *  birantesy@gmail.com
     * */
    public boolean createVentesOublier(String prixOublier, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("prixoublier", prixOublier);
        contentValues.put("dateajout", date);
        db.insert("" + TABLE_VENTES_OUBLIER, null, contentValues);
        return true;
    }


    /***
     *  Verifie si nous avons une vente oublier aujourd'hui.
     *  @author birante sy
     *  birantesy@gmail.com
     * */
    public boolean checkIfWeHaveForgetSaleToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from venteoublier where date(dateajout) = date('now')";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // record found
            return true;
        } else {
            // record not found
            return false;
        }
    }


    /***
     *  Recupere l'identifiant de la derniere vente oublier.
     *  @author birante sy
     *  @return id
     *  birantesy@gmail.com
     * */
    public int getForgetSalesId() {
        SQLiteDatabase db = this.getWritableDatabase();
        final String QUERY = "select idventeoublier  from venteoublier where date(dateajout) = date('now')";
        Cursor cur = db.rawQuery(QUERY, null);
        cur.moveToFirst();
        int id = cur.getInt(0);
        cur.close();
        return id;
    }

    /***
     *  Recupere l'identifiant de la derniere vente oublier.
     *  @author birante sy
     *  @return id
     *  birantesy@gmail.com
     * */
    public int getLastForgetSalesId() {
        SQLiteDatabase db = this.getWritableDatabase();
        final String QUERY = "select idventeoublier  from venteoublier order by idventeoublier desc limit 1";
        Cursor cur = db.rawQuery(QUERY, null);
        cur.moveToFirst();
        int id = cur.getInt(0);
        cur.close();
        return id;
    }


    /***
     *  Met a jour (Modifie) la derniere vente oublier.
     *  @author birante sy
     *  birantesy@gmail.com
     *
     * */
    public void updateLastForgetSale(String id, String prix, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("prixoublier", prix);
        contentValues.put("dateajout", date);
        db.update(TABLE_VENTES_OUBLIER, contentValues, " idventeoublier=" + id, null);
    }


    public boolean insertBoutique(String name, String numero, String mail, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("numboutique", numero);
        contentValues.put("nomboutique", name);
        contentValues.put("mail", mail);
        contentValues.put("code", code);

        db.insert("" + TABLE_BOUTIQUE, null, contentValues);
        //db.delete(TABLE_BOUTIQUE,null,null);
        return true;
    }

    public boolean insertSuivRQ(String date, String qte, String idref, String cont, String prix) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("datesuiv", date);
        contentValues.put("prixsuiv", prix);
        contentValues.put("qtesuiv", qte);
        contentValues.put("idrefsuivi", idref);
        contentValues.put("contexte", cont);

        db.insert("" + TABLE_SUIVI_REF, null, contentValues);
        //db.delete(TABLE_BOUTIQUE,null,null);
        return true;
    }

    /*public boolean insertSuivRTVA(String date, String idref, String cont, String prix, String tva) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("datesuiv", date);
        contentValues.put("prixsuiv", prix);
        contentValues.put("tvaapplique", tva);
        contentValues.put("idrefsuivi", idref);
        contentValues.put("contexte", cont);

        db.insert("" + TABLE_SUIVI_REF, null, contentValues);
        //db.delete(TABLE_BOUTIQUE,null,null);
        return true;
    }*/


    public Cursor getREFSUIV(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_SUIVI_REF + " where idrefsuivi=" + id + " AND contexte LIKE '%Entree%'", null);
        return res;
    }


    public Cursor getREFSUIVLast() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_SUIVI_REF + " ORDER BY idsuiv DESC LIMIT 1 ", null);
        return res;
    }


    public ArrayList<Invoice> getAllSuivRef() {
        ArrayList<Invoice> array_list = new ArrayList<Invoice>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_SUIVI_REF, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Invoice invoice = new Invoice();
            invoice.setId(res.getInt(res.getColumnIndex(COLUMN_ID_FACT)));
            invoice.setDates(res.getString(res.getColumnIndex(COLUMN_DATE_FACT)));
            invoice.setTotal(res.getInt(res.getColumnIndex(COLUMN_TOTAL_FACT)));
            invoice.setNumidcli(res.getString(res.getColumnIndex(COLUMN_IDCLI_FACT)));
            invoice.setType(res.getString(res.getColumnIndex(COLUMN_TYPE_FACT)));
            array_list.add(invoice);
            res.moveToNext();
        }
        return array_list;
    }


    public boolean majBout(String name, String numero, String idbou, String mail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nomboutique", name);
        contentValues.put("numboutique", numero);
        contentValues.put("mail", mail);
        // db.update(""+TABLE_CLIENT, null, contentValues);
        db.update(TABLE_BOUTIQUE, contentValues, " idbout=" + idbou, null);
        return true;
    }


    public boolean majcode(String code, String idbou) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("code", code);

        // db.update(""+TABLE_CLIENT, null, contentValues);
        db.update(TABLE_BOUTIQUE, contentValues, " idbout=" + idbou, null);
        return true;
    }

    public boolean majcodecorrect(String code, String mail, String idbou) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("code", code);
        contentValues.put("mail", mail);

        // db.update(""+TABLE_CLIENT, null, contentValues);
        db.update(TABLE_BOUTIQUE, contentValues, " idbout=" + idbou, null);
        return true;
    }


    public boolean insertReference(String name, String prix, String quantite, String img, String date, String categorie, String priorite) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nomref", name);
        contentValues.put("prixref", prix);
        contentValues.put("qterefe", quantite);
        contentValues.put("img", img);
        contentValues.put("dateer", date);
        contentValues.put("categorie", categorie);
        contentValues.put("priorite", priorite);
        db.insert("" + TABLE_REFERENCE, null, contentValues);
        return true;
    }

    public boolean insertReferencedg(String id, String name, String prix, String quantite, String img, String date, String categorie, String priorite) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idref", id);
        contentValues.put("nomref", name);
        contentValues.put("prixref", prix);
        contentValues.put("qterefe", quantite);
        contentValues.put("img", img);
        contentValues.put("dateer", date);
        contentValues.put("categorie", categorie);
        contentValues.put("priorite", priorite);
        db.insert("" + TABLE_REFERENCE, null, contentValues);
        return true;
    }

    public boolean insertReferenceW(String name, String lienphoto, String prix, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nomrefw", name);
        contentValues.put("lienimg", lienphoto);
        contentValues.put("prixw", prix);
        contentValues.put("idkeyrw", id);

        db.insert("" + TABLE_REFWEEBI, null, contentValues);
        return true;
    }


    public boolean updateReference(String name, String idref) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nomref", name);
        // contentValues.put("prixref", prix);
        //contentValues.put("qterefe", quantite);

        db.update(TABLE_REFERENCE, contentValues, " idref=" + idref, null);
        return true;
    }

    public boolean updateRefprice(String prix, String idref) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("prixref", prix);
        db.update(TABLE_REFERENCE, contentValues, " idref=" + idref, null);
        return true;
    }

    public boolean updateRefcat(String categ, String idref) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categorie", categ);
        db.update(TABLE_REFERENCE, contentValues, " idref=" + idref, null);
        return true;
    }

    public boolean updateRefqte(String qte, String dateenre, String idref, String contex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("qterefe", qte);
        contentValues.put("dateer", dateenre);
        contentValues.put("contexte", contex);
        db.update(TABLE_REFERENCE, contentValues, " idref=" + idref, null);
        return true;
    }

    public boolean updateRefqteall(String qte, String dateenre) {
        //String qtee='';
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("qterefe", qte);
        contentValues.put("dateer", dateenre);
        db.update(TABLE_REFERENCE, contentValues, " qterefe=" + "\'\'", null);
        //db.update(TABLE_REFERENCE,contentValues," qterefe=1",null);
        return true;
    }

    public boolean refqtevendu(String nomref, String qte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("qterefe", qte);
        db.update(TABLE_REFERENCE, contentValues, " nomref=" + nomref, null);
        return true;
    }

    public boolean faireDepot(String montant, String date, String numcli, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("montantdepot", montant);
        contentValues.put("datedepot", date);
        contentValues.put("idclient", numcli);
        contentValues.put("typedep", type);

        db.insert("" + TABLE_DEPOTS, null, contentValues);
        return true;
    }


    public boolean insertClient(String name, String numero, String solde) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nomclient", name);
        contentValues.put("numero", numero);
        contentValues.put("solde", solde);
        db.insert("" + TABLE_CLIENT, null, contentValues);
        return true;
    }

    public boolean majClient(String idcli, String numero, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nomclient", name);
        contentValues.put("numero", numero);
        // db.update(""+TABLE_CLIENT, null, contentValues);
        db.update(TABLE_CLIENT, contentValues, " idclient=" + idcli, null);
        return true;
    }

    //utilise pour la restauration
    /*public Integer suppClient (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("clients", "numero ="+id ,null);
    }*/

    public Integer suppReftablesuiv() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("suiviprixqte", null, null);
    }

    public Integer suppClient(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("clients", "idclient =" + id, null);
    }


    public Integer suppReference(String idref) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("referenc", "idref=" + idref, null);
    }

    public Integer suppReferenceW() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("refcharges", null, null);
    }

    public Integer suppAllReference() {
        String pri = "";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("referenc", "priorite=2", null);
    }

    /*public Integer suppInvoice(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("facture",
                "idfact = ? ",
                new String[]{Integer.toString(id)});
    }*/

    public Integer suppInvoicer(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("facturer",
                "idfactr = ? ",
                new String[]{Integer.toString(id)});
    }

    public Integer suppItemInvoice(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("items",
                "idfact = ? ",
                new String[]{Integer.toString(id)});
    }


    public boolean majsoldecli(String idclient, String solde) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("solde", solde);
        // db.update(""+TABLE_CLIENT, null, contentValues);
        db.update(TABLE_CLIENT, contentValues, " idclient=" + idclient, null);
        return true;
    }

    /*public boolean insertFact(String datefact, String totalfact, String idcli, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("datefact", datefact);
        contentValues.put("totalfact", totalfact);
        contentValues.put("idclient", idcli);
        contentValues.put("typefact", type);

        db.insert("" + TABLE_FACTURE, null, contentValues);
        return true;
    }*/
    public boolean insertFactr(String datefact, String totalfact, String idcli, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("datefactr", datefact);
        contentValues.put("totalfactr", totalfact);
        contentValues.put("idclientr", idcli);
        contentValues.put("typefactr", type);

        db.insert("" + TABLE_FACTURER, null, contentValues);
        return true;
    }


    public boolean insertFactS(String idfaS, String datefacts, String totalfacts, String datedesup, String nomcli, String numcli, String typefac) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("datefacts", datefacts);
        contentValues.put("datedesup", datedesup);
        contentValues.put("totalfacts", totalfacts);
        contentValues.put("typefact", typefac);
        contentValues.put("nomclient", nomcli);
        contentValues.put("numclient", numcli);
        contentValues.put("idfaS", idfaS);

        db.insert("" + TABLE_FACTS, null, contentValues);
        return true;
    }


    public boolean insertItems(String nomitem, String prixunit, String soustotal, String qtechoisit, String idfact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nomitem", nomitem);
        contentValues.put("prixunit", prixunit);
        contentValues.put("soustotal", soustotal);
        contentValues.put("qtechoisit", qtechoisit);
        contentValues.put("idfact", idfact);

        db.insert("" + TABLE_ITEMS, null, contentValues);
        return true;
    }


    public boolean insertItemsS(String nomitems, String prixunits, String soustotals, String qtesupp, String idfacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nomitems", nomitems);
        contentValues.put("prixunits", prixunits);
        contentValues.put("soustotals", soustotals);
        contentValues.put("qtechoisits", qtesupp);
        contentValues.put("idfacts", idfacts);

        db.insert("" + TABLE_ITEMSS, null, contentValues);
        return true;
    }


    public Cursor getReferences() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select nomref from " + TABLE_REFERENCE, null);
        return res;
    }

    public Cursor getReferenceW(String nom) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFWEEBI + " where nomrefw LIKE \'" + nom + "\'", null);
        return res;
    }

    public Cursor getREF(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFERENCE + " where idref=" + id + "", null);
        return res;
    }

    public Cursor getrefname(String nom) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFERENCE + " where nomref LIKE \'" + nom + "\'", null);
        return res;
    }

    public Cursor getrefnametabitem(String nom) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ITEMS + " where nomitem LIKE \'" + nom + "\'", null);
        return res;
    }

    /*public Cursor getInvoice(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTURE + " where idfact=" + id + "", null);
        return res;
    }*/
    public Cursor getInvoicer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTURER + " where idfactr=" + id + "", null);
        return res;
    }

    public Cursor getInvoiceS(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTS + " where idfaS=" + id + "", null);
        return res;
    }


    public Cursor getDepot(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_DEPOTS + " where iddepot=" + id + "", null);
        return res;
    }

    /*public Cursor getLastInvoice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTURE + " ORDER BY " + COLUMN_ID_FACT + " DESC LIMIT 1 ", null);
        return res;
    }*/
    public Cursor getLastInvoiceR() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTURER + " ORDER BY " + COLUMN_ID_FACTR + " DESC LIMIT 1 ", null);
        return res;
    }

    public Cursor getLastRef() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFERENCE + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1 ", null);
        return res;
    }

    public Cursor getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ITEMS + " where iditem=" + id + "", null);
        return res;
    }

    public Cursor getItemIdInvoice(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ITEMS + " where idfact=" + id + "", null);
        return res;
    }

    public Cursor getItemIdInvoiceS(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ITEMSS + " where idfacts=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_REFERENCE);
        return numRows;
    }


    public Cursor getBoutique(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_BOUTIQUE + " where idbout=" + id + "", null);
        return res;
    }


    public ArrayList<String> getAllRefName() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFERENCE, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }


    /*public ArrayList<Invoice> getAllInvoice() {
        ArrayList<Invoice> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTURE, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Invoice invoice = new Invoice();
            invoice.setId(res.getInt(res.getColumnIndex(COLUMN_ID_FACT)));
            invoice.setDates(res.getString(res.getColumnIndex(COLUMN_DATE_FACT)));
            invoice.setTotal(res.getInt(res.getColumnIndex(COLUMN_TOTAL_FACT)));
            invoice.setNumidcli(res.getString(res.getColumnIndex(COLUMN_IDCLI_FACT)));
            invoice.setType(res.getString(res.getColumnIndex(COLUMN_TYPE_FACT)));
            array_list.add(invoice);
            res.moveToNext();
        }
        return array_list;
    }*/

    public ArrayList<Invoice> getAllInvoiceR() {
        ArrayList<Invoice> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTURER, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Invoice invoice = new Invoice();
            invoice.setId(res.getInt(res.getColumnIndex(COLUMN_ID_FACTR)));
            invoice.setDates(res.getString(res.getColumnIndex(COLUMN_DATE_FACTR)));
            invoice.setTotal(res.getInt(res.getColumnIndex(COLUMN_TOTAL_FACTR)));
            invoice.setNumidcli(res.getString(res.getColumnIndex(COLUMN_IDCLI_FACTR)));
            invoice.setType(res.getString(res.getColumnIndex(COLUMN_TYPE_FACTR)));
            array_list.add(invoice);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Invoice> getAllInvoiceS() {
        ArrayList<Invoice> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTS, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Invoice invoice = new Invoice();
            invoice.setId(res.getInt(res.getColumnIndex(COLUMN_ID_FACTSS)));
            invoice.setDates(res.getString(res.getColumnIndex(COLUMN_DATE_FACTS)));
            invoice.setTotal(res.getInt(res.getColumnIndex(COLUMN_TOTAL_FACTS)));
            invoice.setNomcli(res.getString(res.getColumnIndex(COLUMN_NOMCLI)));
            invoice.setNumcli(res.getString(res.getColumnIndex(COLUMN_NUMCLI)));
            invoice.setType(res.getString(res.getColumnIndex(COLUMN_TYPE_FACT)));
            array_list.add(invoice);
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList<Depots> getAllDepots() {
        ArrayList<Depots> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_DEPOTS, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Depots depots = new Depots();
            depots.setId(res.getString(res.getColumnIndex(COLUMN_ID_DEPOT)));
            depots.setDate(res.getString(res.getColumnIndex(COLUMN_DATEDEP)));
            depots.setMontant(res.getString(res.getColumnIndex(COLUMN_MONT_DEPOT)));
            depots.setNumcli(res.getString(res.getColumnIndex(COLUMN_ID_CLI)));
            depots.setType(res.getString(res.getColumnIndex(COLUMN_TYPE_DEP)));
            array_list.add(depots);
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList<ForgetSale> getTotalForgetSales() {
        ArrayList<ForgetSale> array_list = new ArrayList<ForgetSale>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_VENTES_OUBLIER, null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {
            ForgetSale forgetSale = new ForgetSale();
            forgetSale.setId(res.getInt(res.getColumnIndex(COLUMN_ID_VENTE_OUBLIER)));
            forgetSale.setPrix(res.getInt(res.getColumnIndex(COLUMN_PRIX_OUBLIER)));
            forgetSale.setDateAjout(res.getString(res.getColumnIndex(COLUMN_DATE_AJOUT)));
            array_list.add(forgetSale);
            res.moveToNext();
        }
        return array_list;
    }


   /* public ArrayList<Invoice> getAllInvoiceforOneClient(String id) {
        ArrayList<Invoice> array_list = new ArrayList<Invoice>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTURE + " where idclient=" + id + " ORDER BY idfact DESC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Invoice invoice = new Invoice();
            invoice.setId(res.getInt(res.getColumnIndex(COLUMN_ID_FACT)));
            invoice.setDates(res.getString(res.getColumnIndex(COLUMN_DATE_FACT)));
            invoice.setTotal(res.getInt(res.getColumnIndex(COLUMN_TOTAL_FACT)));
            invoice.setNumidcli(res.getString(res.getColumnIndex(COLUMN_IDCLI_FACT)));
            invoice.setType(res.getString(res.getColumnIndex(COLUMN_TYPE_FACT)));
            array_list.add(invoice);
            res.moveToNext();
        }
        return array_list;
    }*/

    public ArrayList<Invoice> getAllInvoiceforOneClientR(String id) {
        ArrayList<Invoice> array_list = new ArrayList<Invoice>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FACTURER + " where idclientr=" + id + " ORDER BY idfactr DESC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Invoice invoice = new Invoice();
            invoice.setId(res.getInt(res.getColumnIndex(COLUMN_ID_FACTR)));
            invoice.setDates(res.getString(res.getColumnIndex(COLUMN_DATE_FACTR)));
            invoice.setTotal(res.getInt(res.getColumnIndex(COLUMN_TOTAL_FACTR)));
            invoice.setNumidcli(res.getString(res.getColumnIndex(COLUMN_IDCLI_FACTR)));
            invoice.setType(res.getString(res.getColumnIndex(COLUMN_TYPE_FACTR)));
            array_list.add(invoice);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<LaReffb> getAllSortiforOneRef(String id) {
        ArrayList<LaReffb> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_SUIVI_REF + " where idrefsuivi=" + id + " AND contexte NOT LIKE '%Entree%' ORDER BY idrefsuivi DESC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            LaReffb laReffb = new LaReffb();
            laReffb.setId(res.getString(res.getColumnIndex(COLUMN_ID_SUI)));
            laReffb.setDateen(res.getString(res.getColumnIndex(COLUMN_DATE_SUI)));
            laReffb.setLarefprice(res.getString(res.getColumnIndex(COLUMN_PRIX_S)));
            laReffb.setLarefqte(res.getString(res.getColumnIndex(COLUMN_QTE_S)));
            laReffb.setContexts(res.getString(res.getColumnIndex(COLUMN_CONT_S)));
            array_list.add(laReffb);
            res.moveToNext();

        }
        return array_list;
    }


    // =======================================
    //           BILAN CASH / CREDIT
    // =======================================

    // Recupere la somme des ventes a credit durant ces dates.
    public int getTotalCreditAmountOfThisPeriod(String startDate, String endDate)
    {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(totalfactr as decimal)) AS total FROM facturer WHERE date(datefactr) >= date(?) AND date(datefactr) <= date(?)  AND typefactr='credit'";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate, endDate});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    // Recupere la somme des ventes a credit durant ces dates.
    public int getTotalCashAmountOfThisPeriod(String startDate, String endDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(totalfactr as decimal)) AS total FROM facturer WHERE date(datefactr) >= date(?) AND date(datefactr) <= date(?)  AND typefactr=''";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate, endDate});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    // Recupere la somme des ventes a credit durant ces dates.
    public int getTotalCustomerCashAmountOfThisPeriod(String startDate, String endDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(totalfactr as decimal)) AS total FROM facturer WHERE date(datefactr) >= date(?) AND date(datefactr) <= date(?)  AND typefactr='cash'";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate, endDate});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    // =======================================
    //           SALES QUERY
    // =======================================

    /***
     * Retourne le montant total des factures
     * du jours.
     * @author birantesy
     * birantesy@gmail.com
     * @return le montant total des factures*
     */
    public int getTodayTotalAmountOfInvoice() {

        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(totalfactr as decimal)) AS total FROM facturer WHERE date(datefactr) == date('now')";
        Cursor res = db.rawQuery(QUERY, null);

        res.moveToFirst();

        while (!res.isAfterLast()) {

            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }

            res.moveToNext();
        }
        res.close();
        return total;
    }


    /***
     *
     * Retourne le montant total des ventes oubliers
     * du jours.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @return le montant total des ventes oubliers
     *
     * Work Well...
     *
     */
    public int getTodayTotalAmountOfForgetSales() {

        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(prixoublier as decimal)) AS total FROM venteoublier WHERE date(dateajout) == date('now')";
        Cursor res = db.rawQuery(QUERY, null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {

            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }

            res.moveToNext();
        }
        res.close();
        return total;
    }


    /***
     *
     * Retourne le montant total des ventes a credit
     * du jours.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @return le montant total des ventes a credit
     *
     * Work Well...
     *
     */
    public int getTodayTotalCreditAmount() {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(totalfactr as decimal)) AS total FROM facturer WHERE date(datefactr) == date('now') AND typefactr='credit'";
        Cursor res = db.rawQuery(QUERY, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    /***
     *
     * Retourne le montant total
     * des ventes des clients fideles
     * du jours.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @return le montant total des ventes
     * cash au clients fideles.
     *
     * Work Well...
     *
     */
    public int getTodayTotalCashClientAmount() {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(totalfactr as decimal)) AS total FROM facturer WHERE date(datefactr) == date('now') AND typefactr='cash'";
        Cursor res = db.rawQuery(QUERY, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    /***
     *
     * Retourne le montant total
     * des ventes cash
     * du jours.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @return le montant total des ventes
     * cash .
     *
     * Work Well...
     *
     */
    public int getTodayTotalCashAmount() {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(totalfactr as decimal)) AS total FROM facturer WHERE date(datefactr) == date('now') AND idclientr='1'";
        Cursor res = db.rawQuery(QUERY, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    // ======== // ======== // ======== // ======== // ========

    public int getTotalCreditAmountOfInvoiceInThatDay(String startDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String QUERY = "select * , sum(CAST(soustotal as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr and date(facturer.datefactr) = ? AND typefactr='credit'";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    public int getTotalCashClientAmountOfInvoiceInThatDay(String startDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String QUERY = "select * , sum(CAST(soustotal as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr and date(facturer.datefactr) = ? AND typefactr='cash'";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    public int getTotalCashAmountOfInvoiceInThatDay(String startDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String QUERY = "select * , sum(CAST(soustotal as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr and date(facturer.datefactr) = ? AND typefactr=''";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    // ======== // ======== // 2 Dates ======== // ======== // ========

    public int getTotalCreditAmountOfInvoiceInThatDay(String date, String dateEnd) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(CAST(soustotal as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr AND  date(facturer.datefactr) >= ? AND  date(facturer.datefactr) <= ? AND typefactr='credit'";
        Cursor res = db.rawQuery(QUERY, new String[]{date, dateEnd});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    public int getTotalCashClientAmountOfInvoiceInThatDay(String date, String dateEnd) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(CAST(soustotal as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr AND  date(facturer.datefactr) >= ? AND  date(facturer.datefactr) <= ? AND typefactr='cash'";
        Cursor res = db.rawQuery(QUERY, new String[]{date, dateEnd});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    public int getTotalCashAmountOfInvoiceInThatDay(String date, String dateEnd) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(CAST(soustotal as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr AND  date(facturer.datefactr) >= ? AND  date(facturer.datefactr) <= ? AND typefactr=''";
        Cursor res = db.rawQuery(QUERY, new String[]{date, dateEnd});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    // ======== // ======== // 2 Dates et 2 Heures ======== // ======== // ========


    public int getTotalCreditAmountOfInvoiceInThatDay(String startTime, String endTime, String startDate, String endDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(cast(soustotal as decimal)) AS total from items, facturer where facturer.datefactr between date(?)  and date(?) and time(facturer.datefactr) between time(?) and  time(?) and  items.idfact = facturer.idfactr AND typefactr='credit'";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate, endDate, startTime, endTime});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    public int getTotalCashClientAmountOfInvoiceInThatDay(String startTime, String endTime, String startDate, String endDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(cast(soustotal as decimal)) AS total from items, facturer where facturer.datefactr between date(?)  and date(?) and time(facturer.datefactr) between time(?) and  time(?) and  items.idfact = facturer.idfactr AND typefactr='cash'";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate, endDate, startTime, endTime});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    public int getTotalCashAmountOfInvoiceInThatDay(String startTime, String endTime, String startDate, String endDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(cast(soustotal as decimal)) AS total from items, facturer where facturer.datefactr between date(?)  and date(?) and time(facturer.datefactr) between time(?) and  time(?) and  items.idfact = facturer.idfactr AND typefactr=''";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate, endDate, startTime, endTime});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    // ======== // ======== // ======== // ======== // ========


    /***
     *
     *
     * @author birantesy
     * birantesy@gmail.com
     * @param date la premiere date
     * @return le montant total des factures
     *
     * Work Well...
     *
     */
    public int getTotalAmountOfInvoiceInThatDay(String date) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String QUERY = "select * , sum(CAST(soustotal as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr and date(facturer.datefactr) = ?";
        Cursor res = db.rawQuery(QUERY, new String[]{date});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    /***
     *
     * Retourne le montant total des factures
     * entre deux jours.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @param date la premiere date
     * @param dateEnd la deuxieme date
     * @return le montant total des factures
     *
     * Work Well...
     *
     */
    public int getTotalAmountOfInvoice(String date, String dateEnd) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(CAST(soustotal as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr AND  date(facturer.datefactr) >= ? AND  date(facturer.datefactr) <= ?";
        Cursor res = db.rawQuery(QUERY, new String[]{date, dateEnd});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    /***
     *
     * Retourne le montant total des factures
     * entre deux jours.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @return le montant total des factures
     *
     * Work Well...
     *
     */
    public int getTotalAmountOfInvoiceWithTime(String startTime, String endTime, String startDate, String endDate) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(cast(soustotal as decimal)) AS total from items, facturer where facturer.datefactr between date(?)  and date(?) and time(facturer.datefactr) between time(?) and  time(?) and  items.idfact = facturer.idfactr";
        Cursor res = db.rawQuery(QUERY, new String[]{startDate, endDate, startTime, endTime});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }


    /***
     *
     * Retourne le montant total des ventes oubliers
     * sur un jour donnee.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @param date
     * @return le montant total des ventes oublier.
     *
     * Working Well...
     *
     */
    public int getTotalAmountOfForgetSalesInThatDay(String date) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(prixoublier as decimal)) AS total FROM venteoublier WHERE date(dateajout)=?";
        Cursor res = db.rawQuery(QUERY, new String[]{date});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        return total;
    }


    /***
     *
     * Retourne le montant total des ventes oubliers
     * entre deux jours.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @param date la premiere date
     * @param dateEnd la deuxieme date
     * @return le montant total des ventes oublier.
     *
     * Working Well...
     *
     */
    public int getTotalAmountOfForgetSales(String date, String dateEnd) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * , sum(CAST(prixoublier as decimal)) AS total FROM venteoublier WHERE date(dateajout) >= ? AND date(dateajout) <= ?";
        Cursor res = db.rawQuery(QUERY, new String[]{date, dateEnd});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                total = Integer.parseInt(res.getString(res.getColumnIndex("total")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return total;
    }

    /***
     * Retourne la liste des produits
     * (Nom du Produits + Quantite total) vendus.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @param date date de la recherche
     * @return le montant total des articles
     * Working Well...
     */

    public ArrayList<LaReffb> getSalesListOfTheDay(String date) {

        ArrayList<LaReffb> references = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(CAST(qtechoisit as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr and date(facturer.datefactr) = ? group by nomitem order by total desc";

        Cursor res = db.rawQuery(QUERY, new String[]{date});

        res.moveToFirst();
        while (!res.isAfterLast()) {
            LaReffb reference = new LaReffb();
            reference.setLarefqte(res.getString(res.getColumnIndex("total")));
            reference.setLaref(res.getString(res.getColumnIndex(COLUMN_NOM_ITEM)));
            references.add(reference);
            res.moveToNext();
        }
        res.close();
        return references;
    }


    /***
     * Retourne la liste des produits
     * (Nom du Produits + Quantite total) vendus
     * dans une periode.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @param date date de la recherche
     * @param dateEnd date de la recherche
     * @return le montant total des articles
     * Working Well...
     */
    public ArrayList<LaReffb> getTheListOfSalesForAPeriod(String date, String dateEnd) {

        ArrayList<LaReffb> references = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select * , sum(CAST(qtechoisit as decimal)) AS total from items , facturer where items.idfact = facturer.idfactr AND  date(facturer.datefactr) >= ? AND  (facturer.datefactr) <= ? group by nomitem order by total desc";

        Cursor res = db.rawQuery(QUERY, new String[]{date, dateEnd});

        res.moveToFirst();
        while (!res.isAfterLast()) {
            LaReffb reference = new LaReffb();
            reference.setLarefqte(res.getString(res.getColumnIndex("total")));
            reference.setLaref(res.getString(res.getColumnIndex(COLUMN_NOM_ITEM)));
            references.add(reference);
            res.moveToNext();
        }
        res.close();
        return references;
    }


    /***
     * Retourne le maximun de quantite
     *  des produits vendus.
     *
     * @author birantesy
     * @param date date de la recherche
     * @return le montant total des articles
     * Work Well
     */
    public int getMaxOfItemsQuantity(String date) {
        int max = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select max(total) as max " +
                "from ( select sum(CAST(qtechoisit as decimal)) AS total " +
                "from items , facturer " +
                "where items.idfact = facturer.idfactr " +
                "AND  date(facturer.datefactr) = ? group by nomitem)";

        Cursor res = db.rawQuery(QUERY, new String[]{date});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            try {
                max = Integer.parseInt(res.getString(res.getColumnIndex("max")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return max;
    }


    /***
     * Retourne le maximun de quantite
     *  des produits vendus.
     *
     * @author birantesy
     * @param date date de la recherche
     * @param dateEnd date de la recherche
     * @return le montant total des articles
     * Work Well
     */
    public int getMaxOfItemsQuantity(String date, String dateEnd) {
        int max = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select max(total) as max " +
                "from ( select sum(CAST(qtechoisit as decimal)) AS total " +
                "from items , facturer " +
                "where items.idfact = facturer.idfactr " +
                "AND  date(facturer.datefactr) >= ? AND  (facturer.datefactr) <= ? group by nomitem)";
        Cursor res = db.rawQuery(QUERY, new String[]{date, dateEnd});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            try {
                max = Integer.parseInt(res.getString(res.getColumnIndex("max")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return max;
    }

    /***
     * Retourne le maximun de quantite
     *  des produits vendus.
     *
     * @author birantesy
     * @param startDate date de la recherche
     * @param endTime date de la recherche
     * @param startDate date de la recherche
     * @param endDate date de la recherche
     * @return le montant total des articles
     * Work Well
     */
    public int getMaxOfItemsQuantity(String startTime, String endTime, String startDate, String endDate) {
        int max = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "select max(total) as max " +
                "from (select sum(CAST(qtechoisit as decimal)) AS total " +
                "from items , facturer " +
                "where items.idfact = facturer.idfactr " +
                "AND  time(facturer.datefactr) between time(?)  and  time(?)" +
                "AND  facturer.datefactr between date(?)  and date(?)" +
                "group by nomitem)";


        Cursor res = db.rawQuery(QUERY, new String[]{startTime, endTime, startDate, endDate});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            try {
                max = Integer.parseInt(res.getString(res.getColumnIndex("max")));
            } catch (NumberFormatException e) {
                e.getMessage();
            }
            res.moveToNext();
        }
        res.close();
        return max;
    }


    /***
     * Retourne la liste des produits
     * (Nom du Produits + Quantite total) vendus
     * dans une periode.
     *
     * @author birantesy
     * birantesy@gmail.com
     * @return la liste
     * Working Well...
     */
    public ArrayList<LaReffb> getTheListOfSalesForAPeriodWithTime(String startTime, String endTime, String startDate, String endDate) {
        ArrayList<LaReffb> references = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String QUERTY = "select * , sum(cast(qtechoisit as decimal)) AS total " +
                "from items, facturer " +
                "where time(facturer.datefactr) " +
                "between time(?)  and  time(?) and facturer.datefactr " +
                "between date(?)  and date(?) and  items.idfact = facturer.idfactr " +
                "group by nomitem order by total desc";

        Cursor res = db.rawQuery(QUERTY, new String[]{startTime, endTime, startDate, endDate});
        res.moveToFirst();
        while (!res.isAfterLast()) {
            LaReffb reference = new LaReffb();
            reference.setLarefqte(res.getString(res.getColumnIndex("total")));
            reference.setLaref(res.getString(res.getColumnIndex(COLUMN_NOM_ITEM)));
            references.add(reference);
            res.moveToNext();
        }
        res.close();
        return references;
    }


    public ArrayList<LaReffb> getAllENTERforOneRef(String id) {
        ArrayList<LaReffb> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_SUIVI_REF + " where idrefsuivi=" + id + " AND contexte LIKE '%Entree%'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            LaReffb laReffb = new LaReffb();
            laReffb.setId(res.getString(res.getColumnIndex(COLUMN_ID_SUI)));
            laReffb.setDateen(res.getString(res.getColumnIndex(COLUMN_DATE_SUI)));
            laReffb.setLarefprice(res.getString(res.getColumnIndex(COLUMN_PRIX_S)));
            laReffb.setLarefqte(res.getString(res.getColumnIndex(COLUMN_QTE_S)));
            laReffb.setContexts(res.getString(res.getColumnIndex(COLUMN_CONT_S)));
            array_list.add(laReffb);
            res.moveToNext();

        }
        return array_list;
    }


    public ArrayList<Depots> getDepotforOneClient(String id) {
        ArrayList<Depots> array_list = new ArrayList<Depots>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_DEPOTS + " where idclient=" + id + " ORDER BY iddepot DESC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Depots depots = new Depots();
            depots.setId(res.getString(res.getColumnIndex(COLUMN_ID_DEPOT)));
            depots.setDate(res.getString(res.getColumnIndex(COLUMN_DATEDEP)));
            depots.setMontant(res.getString(res.getColumnIndex(COLUMN_MONT_DEPOT)));
            depots.setNumcli(res.getString(res.getColumnIndex(COLUMN_ID_CLI)));
            depots.setType(res.getString(res.getColumnIndex(COLUMN_TYPE_DEP)));
            array_list.add(depots);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Itemstring> getAllItems() {
        ArrayList<Itemstring> array_list = new ArrayList<Itemstring>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ITEMS, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Itemstring itemstring = new Itemstring();
            itemstring.setName(res.getString(res.getColumnIndex(COLUMN_NOM_ITEM)));
            itemstring.setUnitPrice(res.getString(res.getColumnIndex(COLUMN_PU)));
            itemstring.setTotalPrice(res.getString(res.getColumnIndex(COLUMN_SOUSTOT)));
            itemstring.setQuantity(res.getString(res.getColumnIndex(COLUMN_QTE_CHOISI)));
            itemstring.setIdfact(res.getString(res.getColumnIndex(COLUMN_IDFACT)));
            array_list.add(itemstring);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Itemstring> getAllItemsS() {
        ArrayList<Itemstring> array_list = new ArrayList<Itemstring>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_ITEMSS, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Itemstring itemstring = new Itemstring();
            itemstring.setName(res.getString(res.getColumnIndex(COLUMN_NOM_ITEMS)));
            itemstring.setUnitPrice(res.getString(res.getColumnIndex(COLUMN_PUS)));
            itemstring.setTotalPrice(res.getString(res.getColumnIndex(COLUMN_SOUSTOTS)));
            itemstring.setQuantity(res.getString(res.getColumnIndex(COLUMN_QTE_SUPP)));
            itemstring.setIdfact(res.getString(res.getColumnIndex(COLUMN_IDFACTS)));
            array_list.add(itemstring);
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList<Client> getAllClient() {
        ArrayList<Client> array_list = new ArrayList<Client>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CLIENT + " ORDER BY nomclient ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Client client = new Client();
            client.setNom(res.getString(res.getColumnIndex(COLUMN_NAME_CLIENT)));
            client.setNumero(res.getString(res.getColumnIndex(COLUMN_NUMERO)));
            client.setSolde(res.getInt(res.getColumnIndex(COLUMN_SOLDE)));
            client.setId(res.getString(res.getColumnIndex(COLUMN_ID_CLIENT)));
            array_list.add(client);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Client> getAllClientwithoutcash() {
        ArrayList<Client> array_list = new ArrayList<Client>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CLIENT + " where numero != 0  ORDER BY nomclient ASC", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Client client = new Client();
            client.setNom(res.getString(res.getColumnIndex(COLUMN_NAME_CLIENT)));
            client.setNumero(res.getString(res.getColumnIndex(COLUMN_NUMERO)));
            client.setSolde(res.getInt(res.getColumnIndex(COLUMN_SOLDE)));
            client.setId(res.getString(res.getColumnIndex(COLUMN_ID_CLIENT)));
            array_list.add(client);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<Client> getAllClientwithoutcashBySolde() {
        ArrayList<Client> array_list = new ArrayList<Client>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CLIENT + " where numero != 0  ORDER BY solde ASC", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Client client = new Client();
            client.setNom(res.getString(res.getColumnIndex(COLUMN_NAME_CLIENT)));
            client.setNumero(res.getString(res.getColumnIndex(COLUMN_NUMERO)));
            client.setSolde(res.getInt(res.getColumnIndex(COLUMN_SOLDE)));
            client.setId(res.getString(res.getColumnIndex(COLUMN_ID_CLIENT)));
            array_list.add(client);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public Cursor getClient(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CLIENT + " where idclient=" + id + "", null);
        return res;
    }

    public Cursor getClientnum(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CLIENT + " where numero=" + id + "", null);
        return res;
    }

    public ArrayList<LaReffb> getAllReference() {
        ArrayList<LaReffb> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFERENCE + " ORDER BY nomref ASC", null);
        //Cursor res = db.rawQuery("select * from " + TABLE_REFERENCE+" ORDER BY nomref ASC", null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {
            LaReffb laReffb = new LaReffb();
            laReffb.setLaref(res.getString(res.getColumnIndex(COLUMN_NAME)));
            laReffb.setLarefprice(res.getString(res.getColumnIndex(COLUMN_PRIX)));
            laReffb.setLarefqte(res.getString(res.getColumnIndex(COLUMN_QTE)));
            laReffb.setId(res.getString(res.getColumnIndex(COLUMN_ID)));
            laReffb.setDateen(res.getString(res.getColumnIndex(COLUMN_DATEER)));
            laReffb.setLienimg(res.getString(res.getColumnIndex(COLUMN_IMG)));
            array_list.add(laReffb);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<LaReffb> getAllReferenceById() {
        ArrayList<LaReffb> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFERENCE + " ORDER BY idref DESC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            LaReffb laReffb = new LaReffb();
            laReffb.setLaref(res.getString(res.getColumnIndex(COLUMN_NAME)));
            laReffb.setLarefprice(res.getString(res.getColumnIndex(COLUMN_PRIX)));
            laReffb.setLarefqte(res.getString(res.getColumnIndex(COLUMN_QTE)));
            laReffb.setId(res.getString(res.getColumnIndex(COLUMN_ID)));
            laReffb.setDateen(res.getString(res.getColumnIndex(COLUMN_DATEER)));
            laReffb.setLienimg(res.getString(res.getColumnIndex(COLUMN_IMG)));
            array_list.add(laReffb);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<LaReffb> getAllReferenceCat(String categ) {
        ArrayList<LaReffb> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFERENCE + " where categorie LIKE '" + categ + "\'", null);
        // Cursor res =  db.rawQuery( "select * from "+ TABLE_REFERENCE+" where categorie LIKE '"+categ+"\' ORDER BY nomref ASC", null );
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            LaReffb laReffb = new LaReffb();
            laReffb.setLaref(res.getString(res.getColumnIndex(COLUMN_NAME)));
            laReffb.setLarefprice(res.getString(res.getColumnIndex(COLUMN_PRIX)));
            laReffb.setLarefqte(res.getString(res.getColumnIndex(COLUMN_QTE)));
            laReffb.setId(res.getString(res.getColumnIndex(COLUMN_ID)));
            laReffb.setDateen(res.getString(res.getColumnIndex(COLUMN_DATEER)));
            array_list.add(laReffb);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<LaReffb> getAllReferenceW() {
        ArrayList<LaReffb> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REFWEEBI, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            LaReffb laReffb = new LaReffb();
            laReffb.setLaref(res.getString(res.getColumnIndex(COLUMN_NOM_RW)));
            laReffb.setLienimg(res.getString(res.getColumnIndex(COLUMN_IMGW)));
            array_list.add(laReffb);
            res.moveToNext();
        }
        return array_list;
    }


    // ==========================================
    //        Modification des dates dans la bd
    // ==========================================

    /***
     *
     *  Enleve la lettre "a AM PM"
     *  dans la date des ventes oublier
     *  @author birante sy
     *  birantesy@gmail.com
     *
     * */
    public void updateVenteOublierDate() {
        String newDate;
        String finalDate = null;
        SQLiteDatabase database = this.getWritableDatabase();
        final String QUERY = "SELECT * from venteoublier";
        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null || cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(0);
                String prixoublier = cursor.getString(1);
                String date = cursor.getString(2);

                if (date.contains("a")) {
                    newDate = DateUtils.removeDateSeparator(date);
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        finalDate = myFormat.format(fromUser.parse(newDate));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    finalDate = date;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("prixoublier", prixoublier);
                contentValues.put("dateajout", finalDate);
                database.update(TABLE_VENTES_OUBLIER, contentValues, "idventeoublier=" + id, null);
                cursor.moveToNext();

            }

        } else {


        }
        cursor.close();
    }


    /***
     *
     *  Enleve la lettre "a AM PM"
     *  dans la table suivixprixqte
     *  @author birante sy
     *  birantesy@gmail.com
     *
     * */
    public void updateSuiviPrixDate() {

        String newDate;
        String finalDate = null;
        SQLiteDatabase database = this.getWritableDatabase();
        final String QUERY = "SELECT * from suiviprixqte";
        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null || cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String prix = cursor.getString(2);
                String qtesuiv = cursor.getString(3);
                String idrefsuivi = cursor.getString(4);
                String tvaApplique = cursor.getString(5);
                String contexte = cursor.getString(6);
                if (date.contains("a")) {
                    newDate = DateUtils.removeDateSeparator(date);
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        finalDate = myFormat.format(fromUser.parse(newDate));

                    } catch (ParseException e) {
                        e.printStackTrace();

                    }

                } else {
                    finalDate = date;

                }
                // Met a Jour des informations dans la bd.
                ContentValues contentValues = new ContentValues();
                contentValues.put("datesuiv", finalDate);
                contentValues.put("prixsuiv", prix);
                contentValues.put("qtesuiv", qtesuiv);
                contentValues.put("idrefsuivi", idrefsuivi);
                contentValues.put("tvaapplique", tvaApplique);
                contentValues.put("contexte", contexte);
                database.update(TABLE_SUIVI_REF, contentValues, "idsuiv=" + id, null);
                cursor.moveToNext();

            }

        } else {

        }
        cursor.close();

    }

    /***
     *
     *  Enleve la lettre "a AM PM"
     *  dans la table depots
     *  @author birante sy
     *  birantesy@gmail.com
     *
     * */


    public void updateDepotDate() {

        String newDate;
        String finalDate = null;
        SQLiteDatabase database = this.getWritableDatabase();
        final String QUERY = "SELECT * from depots";
        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null || cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                int id = cursor.getInt(0);
                String montant = cursor.getString(1);
                String date = cursor.getString(2);
                String type = cursor.getString(3);
                String idClient = cursor.getString(4);

                if (date.contains("a")) {
                    newDate = DateUtils.removeDateSeparator(date);
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        finalDate = myFormat.format(fromUser.parse(newDate));

                    } catch (ParseException e) {
                        e.printStackTrace();

                    }

                } else {
                    finalDate = date;

                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("montantdepot", montant);
                contentValues.put("datedepot", finalDate);
                contentValues.put("typedep", type);
                contentValues.put("idclient", idClient);
                database.update(TABLE_DEPOTS, contentValues, "iddepot=" + id, null);
                cursor.moveToNext();

            }

        } else {

        }
        cursor.close();
    }



    /***
     *
     *  Enleve la lettre "a AM PM"
     *  dans la table facturer
     *  @author birante sy
     *  birantesy@gmail.com
     *
     * */
    public void updateFacturerDate() {

        String newDate;
        String finalDate = null;
        SQLiteDatabase database = this.getWritableDatabase();
        final String QUERY = "SELECT * from facturer";
        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null || cursor.getCount() > 0) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String total = cursor.getString(2);
                String idclient = cursor.getString(3);
                String type = cursor.getString(4);

                if (date.contains("a")) {
                    newDate = DateUtils.removeDateSeparator(date);
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        finalDate = myFormat.format(fromUser.parse(newDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    finalDate = date;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("datefactr", finalDate);
                contentValues.put("totalfactr", total);
                contentValues.put("idclientr", idclient);
                contentValues.put("typefactr", type);
                database.update(TABLE_FACTURER, contentValues, "idfactr=" + id, null);
                cursor.moveToNext();
            }

        } else {
        }
        cursor.close();
    }



    /***
     *
     *  Enleve la lettre "a AM PM"
     *  dans la table facture supprime
     *  @author birante sy
     *  birantesy@gmail.com
     *
     * */
    public void updateDateFactureSupp() {

        String finalDate = null;
        String finalDateSupp = null;
        SQLiteDatabase database = this.getWritableDatabase();
        final String QUERY = "SELECT * from facturessupp";
        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null || cursor.getCount() > 0) {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String total = cursor.getString(2);
                String dateDeSupp = cursor.getString(3);
                String nomClient = cursor.getString(4);
                String numClient = cursor.getString(5);
                String type = cursor.getString(6);
                String idFactureSupp = cursor.getString(7);

                if (date.contains("a")) {
                    String newDate = DateUtils.removeDateSeparator(date);
                    String newDateSupp = DateUtils.removeDateSeparator(dateDeSupp);
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    try {
                        finalDate = myFormat.format(fromUser.parse(newDate));
                        finalDateSupp = myFormat.format(fromUser.parse(newDateSupp));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    finalDate = date;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("datefacts", finalDate);
                contentValues.put("totalfacts", total);
                contentValues.put("datedesup", finalDateSupp);
                contentValues.put("nomclient", nomClient);
                contentValues.put("numclient", numClient);
                contentValues.put("typefact", type);
                contentValues.put("idfaS", idFactureSupp);
                database.update(TABLE_FACTS, contentValues, "idfacts=" + id, null);
                cursor.moveToNext();
            }

        } else {

        }

        cursor.close();
    }



    /***
     *
     *  Enleve la lettre "a AM PM"
     *  dans la table operations
     *  @author birante sy
     *  birantesy@gmail.com
     *
     * */
    public void updateDateOp() {

        String finalDate = null;
        SQLiteDatabase database = this.getWritableDatabase();
        final String QUERY = "SELECT * from operations";
        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null || cursor.getCount() > 0) {

            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                int id = cursor.getInt(0);
                String nom = cursor.getString(1);
                String date = cursor.getString(2);
                String montant = cursor.getString(3);
                String client = cursor.getString(4);

                if (date.contains("a")) {
                    String newDate = DateUtils.removeDateSeparator(date);
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        finalDate = myFormat.format(fromUser.parse(newDate));

                    } catch (ParseException e) {
                        e.printStackTrace();

                    }

                } else {
                    finalDate = date;

                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("nomop", nom);
                contentValues.put("dateop", finalDate);
                contentValues.put("montop", montant);
                contentValues.put("clientidop", client);
                database.update(TABLE_OPERATION, contentValues, "idop=" + id, null);
                cursor.moveToNext();

            }

        } else {


        }
        cursor.close();
    }



    /***
     *
     *  Enleve la lettre "a AM PM"
     *  dans la table reference
     *  @author birante sy
     *  birantesy@gmail.com
     *
     * */
    public void updateDateRef() {

        String finalDate = null;
        SQLiteDatabase database = this.getWritableDatabase();
        final String QUERY = "SELECT * from referenc";
        Cursor cursor = database.rawQuery(QUERY, null);

        if (cursor != null || cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String nom = cursor.getString(1);
                String prix = cursor.getString(2);
                String qte = cursor.getString(3);
                String img = cursor.getString(4);
                String date = cursor.getString(5);
                String contexte = cursor.getString(6);
                String categorie = cursor.getString(7);
                String priorite = cursor.getString(8);

                if (date.contains("a")) {
                    String newDate = DateUtils.removeDateSeparator(date);
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        finalDate = myFormat.format(fromUser.parse(newDate));

                    } catch (ParseException e) {
                        e.printStackTrace();

                    }

                } else {
                    finalDate = date;

                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("nomref", nom);
                contentValues.put("prixref", prix);
                contentValues.put("qterefe", qte);
                contentValues.put("img", img);
                contentValues.put("dateer", finalDate);
                contentValues.put("contexte", contexte);
                contentValues.put("categorie", categorie);
                contentValues.put("priorite", priorite);
                database.update(TABLE_REFERENCE, contentValues, "idref=" + id, null);
                cursor.moveToNext();

            }

        } else {

        }
        cursor.close();
    }

    // ==========================================
    //              FIN
    // ==========================================

}