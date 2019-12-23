package com.weebinatidi.ui;

//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;
import com.weebinatidi.ui.weebi2.Autorisations;
import com.weebinatidi.ui.weebi2.NewInterface;
import com.weebinatidi.ui.weebi2.Parametres;

public class SplashActivity extends Activity {

    public static final String TAG = SplashActivity.class.getSimpleName();
    public static final String Intro = "canintro";
    public static final String Start = "first";
    MediaPlayer mediaPlayer;
    ImageView img;
    ImageView imgtxt;
    //AnimatorSet set;
    //AnimatorSet settxt;
    int width;
    int height;

    //public WeebiApplication context;
    //private static boolean flag = true;

    private PendingIntent mPermissionIntent;

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        //context = (WeebiApplication) getApplicationContext();



        //getPairedDevices();

        img = (ImageView) findViewById(R.id.splashimage);
        //imgtxt =(ImageView)findViewById(R.id.splashimagetxt);

        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;


        mediaPlayer = MediaPlayer.create(SplashActivity.this, R.raw.weebianthem);
        //mediaPlayer = MediaPlayer.create(SplashActivity.this, null);

        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                SharedPreferences preferences = getSharedPreferences("weebi", MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                boolean gointro = preferences.getBoolean(Intro, true);
//                boolean gointro = false;

               /* if(gointro == true)
                {
                    edit.putBoolean(Intro,false);
                    edit.putBoolean(Start,true);
                    edit.commit();
                    //Intent gotostart=new Intent(SplashActivity.this,IntroActivity.class);
                    //startActivity(gotostart);

                }
                else
                {}*/
                Intent gotostart = new Intent(SplashActivity.this, Parametres.class);
                startActivity(gotostart);
            }
        }.start();


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
            mediaPlayer.start();
        }
    }

}
