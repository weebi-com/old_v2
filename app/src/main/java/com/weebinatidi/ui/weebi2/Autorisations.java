package com.weebinatidi.ui.weebi2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.weebinatidi.R;

import java.util.ArrayList;
import java.util.List;

public class Autorisations extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorisations);

        // CONTROLE DE LA VERSION ANDROID
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
        } else {

            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
               /* Intent gotostart = new Intent(Autorisations.this, NewInterface.class);
                startActivity(gotostart);
                finish();*/
            }
        }
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

    @Override    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent gotostart = new Intent(Autorisations.this, NewInterface.class);
                    startActivity(gotostart);
                    finish();
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(getApplicationContext(),"Si vous refusiez, l'application ne fonctionnera pas",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
