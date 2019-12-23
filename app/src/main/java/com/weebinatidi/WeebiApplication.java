package com.weebinatidi;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.weebinatidi.ui.preDefiniation;
import com.weebinatidi.ui.weebi2.LruBitmapCache;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rego.printlib.export.regoPrinter;

/**
 * Created by DadjaBASSOU on 9/9/16.
 */
public class WeebiApplication extends Application {

    private regoPrinter printer;
    private int myState = 0;
    private String printName;
    private preDefiniation.TransferMode printmode = preDefiniation.TransferMode.TM_NONE;
    private boolean labelmark = true;
    private boolean connectFlag = false;


    public static final String TAG = WeebiApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static WeebiApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public regoPrinter getObject() {
        return printer;
    }

    public void setObject() {
        printer = new regoPrinter(this);
    }

    public String getName() {
        return printName;
    }

    public void setName(String name) {
        printName = name;
    }

    public int getState() {
        return myState;
    }

    public void setState(int state) {
        myState = state;
    }

    public int getPrintway() {
        return printmode.getValue();
    }

    public void setPrintway(int printway) {
        switch (printway) {
            case 0:
                printmode = preDefiniation.TransferMode.TM_NONE;
                break;
            case 1:
                printmode = preDefiniation.TransferMode.TM_DT_V1;
                break;
            default:
                printmode = preDefiniation.TransferMode.TM_DT_V2;
                break;
        }
    }

    public boolean getlabel() {
        return labelmark;
    }

    public void setlabel(boolean labelprint) {
        labelmark = labelprint;
    }

    public boolean getConnectFlag() {
        return connectFlag;
    }

    public void setConnectFlag(boolean connectFlag) {
        this.connectFlag = connectFlag;
    }

    public static synchronized WeebiApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
