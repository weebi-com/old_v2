package com.weebinatidi.ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

/**
 * Created by mbp on 13/04/2017.
 */

public class Realmbackup {


    private final static String TAG = Realmbackup.class.getName();

    private Context context;
    private Realm realm;

    public Realmbackup(Context context) {
        this.realm = Realm.getDefaultInstance();
        this.context = context;
    }

    public void backup() {

        File exportRealmFile = null;

        File exportRealmPATH = context.getExternalFilesDir(null);
        String exportRealmFileName = "default.realm";

        Log.d(TAG, "Realm DB Path = " + realm.getPath());

        // create a backup file
        exportRealmFile = new File(exportRealmPATH, exportRealmFileName);

        // if backup file already exists, delete it
        exportRealmFile.delete();

        // copy current realm to backup file
        try {
            realm.writeCopyTo(exportRealmFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String msg = "File exported to Path: " + context.getExternalFilesDir(null);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Log.d(TAG, msg);


        realm.close();

    }

    public void restore() {

        //Restore
        File exportRealmPATH = context.getExternalFilesDir(null);
        String FileName = "default.realm";

        String restoreFilePath = context.getExternalFilesDir(null) + "/" + FileName;

        Log.d(TAG, "oldFilePath = " + restoreFilePath);

        copyBundledRealmFile(restoreFilePath, FileName);
        Log.d(TAG, "Data restore is done");

    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(context.getFilesDir(), outFileName);

            Log.d(TAG, "context.getFilesDir() = " + context.getFilesDir().toString());
            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String dbPath() {

        return realm.getPath();
    }
}
