package com.weebinatidi.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.filter;

import java.util.ArrayList;


/**
 *
 * */
public class AddPrinterActivity extends BaseActivity  {

    private static boolean flag = true;
    private final int RESULT_CHOOSE = 1;
    public WeebiApplication context;
    private TextView mStatusTv;
    private Button mActivateBtn;
    private Button mScanBtn;
    private ProgressDialog mProgressDlg;
    private int numberOfPrinterDetect = 0;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");
                    showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                mDeviceList = new ArrayList<>();
                mProgressDlg.show();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                mProgressDlg.dismiss();
                Intent newIntent = new Intent(AddPrinterActivity.this, BTPairedPrinterListActivity.class);
                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
                startActivity(newIntent);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device);
                numberOfPrinterDetect++;
                showToast("Found device " + device.getName());

                if (numberOfPrinterDetect == 1 || numberOfPrinterDetect > 8) {
                    mBluetoothAdapter.cancelDiscovery();
                }

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStatusTv = findViewById(R.id.tv_status);
        mActivateBtn = findViewById(R.id.btn_enable);
        mScanBtn = findViewById(R.id.btn_scan);

        context = (WeebiApplication) getApplicationContext();
        if (flag) {
            context.setObject();
            flag = false;
        }



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Recherche...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mBluetoothAdapter.cancelDiscovery();
            }
        });


        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            mScanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mBluetoothAdapter.startDiscovery();

                    new CountDownTimer(10000, 100) {
                        public void onTick(long millisUntilFinished) {
                        }
                        public void onFinish() {
                            if (mBluetoothAdapter.isDiscovering()) {
                                mBluetoothAdapter.cancelDiscovery();
                                Toast.makeText(getBaseContext(),"Aucun peripherique trouver !",Toast.LENGTH_LONG).show();
                            }
                        }
                    }.start();
                }
            });

            mActivateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                        showDisabled();
                    } else {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, 1000);
                    }
                }
            });

            if (mBluetoothAdapter.isEnabled()) {
                showEnabled();
            } else {
                showDisabled();
            }
        }



        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_CHOOSE:
                    connectDevices(data.getStringExtra("DEVICE_NAME"), data.getStringExtra("DEVICE_ADDRESS"));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 连接设备
     * connect printer
     * private
     *
     * @param devName 设备名称 device name
     * @param port    地址 address
     */
    public void connectDevices(String devName, String port) {
        Log.v("TAG", "port的值是" + port);

        int state = context.getObject().CON_ConnectDevices(devName.toString().trim(), port.toString().trim(), 200);
        if (state > 0) {
            context.setState(state);
            context.setConnectFlag(true);
            Toast.makeText(AddPrinterActivity.this, "connecté", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE).edit();
            editor.putString("BT_DEVICE_NAME", devName);
            editor.commit();
            finish();
        } else {
            Toast.makeText(AddPrinterActivity.this, "échec", Toast.LENGTH_SHORT).show();
            context.setConnectFlag(false);
        }
    }



    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void showEnabled() {
        mActivateBtn.setText("Desactiver LE BLuetooth");
        mActivateBtn.setTextColor(Color.WHITE);
        mActivateBtn.setBackgroundColor(Color.BLUE);
        mActivateBtn.setEnabled(true);
        mScanBtn.setEnabled(true);
    }

    private void showDisabled() {
        mActivateBtn.setText("Activer LE BLuetooth");
        mActivateBtn.setTextColor(Color.BLACK);
        mActivateBtn.setBackgroundColor(Color.WHITE);
        mActivateBtn.setEnabled(true);
        mScanBtn.setEnabled(false);

    }

    private void showUnsupported() {
        mStatusTv.setText("Bluetooth non supporter par ce peripherique");
        mActivateBtn.setText("Activer");
        mActivateBtn.setEnabled(false);
        mScanBtn.setEnabled(false);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_add_printer;
    }
}
