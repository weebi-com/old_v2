package com.weebinatidi.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;

public class ImprimeText extends BaseActivity  implements View.OnClickListener{

    private WeebiApplication context;
    private TextView print;
    private EditText printText;
    private TitleLayout cusTitle;
    private String texte="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = (WeebiApplication) getApplicationContext();
        texte=getIntent().getStringExtra("text");
        printText(texte);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (context.getConnectFlag()) {
            cusTitle.setStatus("connecté");
        } else {
            cusTitle.setStatus("non connecté");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_print:
                printText(texte);
                break;
            default:
                break;
        }
    }

    private void printText(String letexte) {
        if (context.getConnectFlag()) {
            context.getObject().CON_PageStart(context.getState(), false, 0, 0);
            context.getObject().ASCII_CtrlOppositeColor(context.getState(),false);
            context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "" + letexte + "\n", "gb2312");
            int status = context.getObject().CON_PageEnd(context.getState(), context.getPrintway());
        } else {
           // Toast.makeText(ImprimeText.this, "Veuillez configurer l'imprimante depuis le menu paramètres ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_imprime_text;
    }
}
