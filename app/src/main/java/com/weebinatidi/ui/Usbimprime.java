package com.weebinatidi.ui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;
import com.weebinatidi.ui.dummy.ButtonDialog;
import com.weebinatidi.ui.dummy.EditTextDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class Usbimprime extends BaseActivity implements View.OnClickListener, ButtonDialog.onYesOnclickListener, ButtonDialog.onNoOnclickListener, EditTextDialog.onYesOnclickListener, EditTextDialog.onNoOnclickListener {
    public static final String ACTION_DEVICE_PERMISSION = "com.linc.USB_PERMISSION";
    private final static int mRows = 3;
    private static int[][] mListDevice =
            {
                    {1659, 8963},
                    {4070, 33054},
                    {1155, 22304}
            };
    private static boolean flag = true;

    /**
     * 选择
     */
    private final int RESULT_CHOOSE = 1;
    public WeebiApplication context;
    /**
     * 声明控件
     */
    private Spinner printerName, connectType, transferType;
    private TextView connect, test, deviceName, deviceAddress;
    private ButtonDialog buttonDialog;
    private TitleLayout cusTitle;
    /**
     * 监听在usb框用户的点击操作
     * 非usb连接可不写
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_DEVICE_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            connectDevices(printerName.getSelectedItem().toString(), "usb");
                        }
                    }
                }
            }
        }
    };
    private UsbDevice mDevice = null;
    private UsbManager mUsbManager;
    private PendingIntent mPermissionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (WeebiApplication) getApplicationContext();
        if (flag) {
            context.setObject();
            flag = false;
        }
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_usbimprime;
    }

    /**
     * 初始化控件
     * init view
     */
    private void initView() {
        connect = (TextView) findViewById(R.id.tv_connect);
        test = (TextView) findViewById(R.id.tv_test);
        deviceName = (TextView) findViewById(R.id.tv_devicename);
        deviceAddress = (TextView) findViewById(R.id.tv_deviceaddress);
        connect.setOnClickListener(this);
        test.setOnClickListener(this);
        printerName = (Spinner) findViewById(R.id.sv_printername);
        connectType = (Spinner) findViewById(R.id.sv_connecttype);
        transferType = (Spinner) findViewById(R.id.sv_transfertype);
        //liste des imprimantes supportés
        printerName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (ArrayList<String>) context.getObject().CON_GetSupportPrinters()));
        connectType.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.connecttype)));
        transferType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, context.getObject().CON_GetSupportPageMode()));
        if (context.getConnectFlag()) {
            connect.setText("Close Connect");
        } else {
            connect.setText("connect");
        }
        cusTitle = (TitleLayout) findViewById(R.id.cus_title);
        cusTitle.setTitle("Text Print");
        SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
        switch (prefs.getString("TYPE", "")) {

            case "USB":
                String portUSB = prefs.getString("USB_DEVICE_ADDRESS", null);
                String nameUSB = prefs.getString("USB_DEVICE_NAME", null);
                deviceName.setText(nameUSB);
                printerName.setSelection(prefs.getInt("USB_POSITION_NAME", 0));
                connectType.setSelection(prefs.getInt("USB_CONNECT_TYPE", 0));
                transferType.setSelection(prefs.getInt("USB_TRANSFER_TYPE", 0));
                deviceAddress.setText(portUSB);
                break;
            case "Bluetooth":
                String portBT = prefs.getString("BT_DEVICE_ADDRESS", null);
                String nameBT = prefs.getString("BT_DEVICE_NAME", null);
                deviceName.setText(nameBT);
                printerName.setSelection(prefs.getInt("BT_POSITION_NAME", 0));
                connectType.setSelection(prefs.getInt("BT_CONNECT_TYPE", 0));
                transferType.setSelection(prefs.getInt("BT_TRANSFER_TYPE", 0));
                deviceAddress.setText(portBT);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (context.getConnectFlag()) {
            connect.setText("close");
            cusTitle.setStatus("connecté");
        } else {
            connect.setText("connect");
            cusTitle.setStatus("pas connecté");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_DEVICE_PERMISSION), 0);
        IntentFilter permissionFilter = new IntentFilter(ACTION_DEVICE_PERMISSION);
        registerReceiver(mUsbReceiver, permissionFilter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_connect:
                if (context.getConnectFlag()) {
                    context.getObject().CON_CloseDevices(context.getState());
                    connect.setText("connect");
                    context.setConnectFlag(false);
                } else {
                    popUp();
                }
                break;
            case R.id.tv_test:
                if (context.getConnectFlag()) {
                    context.getObject().CON_PageStart(context.getState(), false, 0, 0);
                    context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "à propos", "gb2312");
                    context.getObject().ASCII_CtrlReset(context.getState());
                    context.getObject().ASCII_Print2DBarcode(context.getState(), preDefiniation.BarcodeType.BT_QRcode.getValue(), "www.rgprt.com", 2, 76, 6);
                    context.getObject().CON_PageEnd(context.getState(), context.getPrintway());
                } else {
                    Toast.makeText(Usbimprime.this, "connecter l'imprimante", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 弹窗，该弹窗为自定义弹窗
     * popupwindow
     */
    private void popUp() {
        if (connectType.getSelectedItem().toString().equals("")) {
            //Intent intent = new Intent(ConnectActivity.this, ConnectWIFIActivity.class);
            //startActivityForResult(intent, RESULT_CHOOSE);
        } else if (connectType.getSelectedItem().toString().equals("USB")) {
            mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            if (checkValidPrinterDevice()) {
                if (mUsbManager.hasPermission(mDevice)) {
                    connectDevices(printerName.getSelectedItem().toString(), "usb");
                } else {
                    mUsbManager.requestPermission(mDevice, mPermissionIntent);
                }
            } else {
                Toast.makeText(Usbimprime.this, "No scan to our company equipment", Toast.LENGTH_SHORT).show();
            }
        } else if (connectType.getSelectedItem().toString().equals("Bluetooth") || connectType.getSelectedItem().toString().equals("Serial")) {
            buttonDialog = new ButtonDialog(Usbimprime.this);
            buttonDialog.setMessage("Do you want to connect to the last device？");
            buttonDialog.setYesOnclickListener("Research", this);
            buttonDialog.setNoOnclickListener("Confirm Connection", this);
            buttonDialog.show();
        }
    }

    /**
     * @param VendorID
     * @param ProductID
     * @return
     */
    public boolean getValidUSB(int VendorID, int ProductID) {
        boolean bRet = false;
        for (int i = 0; i < mRows; ++i) {
            if ((mListDevice[i][0] == VendorID) && (mListDevice[i][1] == ProductID)) {
                bRet = true;
                break;
            }
        }
        return bRet;
    }

    /**
     * 判断产品是不是我们公司的产品，usb连接必用
     *
     * @return
     */
    private boolean checkValidPrinterDevice() {
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        boolean bRet = false;
        if (!deviceList.isEmpty()) {
            for (UsbDevice device : deviceList.values()) {
                if (getValidUSB(device.getVendorId(), device.getProductId())) {
                    mDevice = device;
                    bRet = true;
                    break;
                }
            }
        }
        return bRet;
    }

    @Override
    public void onYesClick() {
//        if (connectType.getSelectedItem().toString().equals("WIFI")) {
//            String port = editTextDialog.getIpAddressOneStr() + "." + editTextDialog.getIpAddressTwoStr() +  "." + editTextDialog.getIpAddressThreeStr() +  "." + editTextDialog.getIpAddressFourStr() + ":9100";
//            editTextDialog.dismiss();
//            connectDevices(null,port);
//        } else {
        buttonDialog.dismiss();
        Intent intent = new Intent(Usbimprime.this, DeviceListDialogActivity.class);
        intent.putExtra("Connect_Type", connectType.getSelectedItem().toString());
        startActivityForResult(intent, RESULT_CHOOSE);
//        }
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
        int state = context.getObject().CON_ConnectDevices(printerName.getSelectedItem().toString().trim(), port, 200);
        if (state > 0) {
            context.setState(state);
            deviceName.setText(devName);
            deviceAddress.setText(port);
            context.setConnectFlag(true);
            Toast.makeText(Usbimprime.this, "connecté", Toast.LENGTH_SHORT).show();
            context.setName(printerName.getSelectedItem().toString().trim());
            context.setPrintway(transferType.getSelectedItemPosition());
            connect.setText("fermer con");
            cusTitle.setStatus("connecté");

            SharedPreferences.Editor editor = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE).edit();
            editor.putString("BT_DEVICE_NAME", devName);
            editor.putString("BT_DEVICE_ADDRESS", port);
            editor.apply();

            finish();
        } else {
            Toast.makeText(Usbimprime.this, "échec", Toast.LENGTH_SHORT).show();
            context.setConnectFlag(false);
            connect.setText("connect");
            cusTitle.setStatus("pas connecté");
        }
    }


    /**
     * 点击确认连接时触发
     */
    @Override
    public void onNoClick() {
        SharedPreferences prefs = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE);
        switch (connectType.getSelectedItem().toString()) {
            case "Bluetooth":
                String portBT = prefs.getString("BT_DEVICE_ADDRESS", null);
                String nameBT = prefs.getString("BT_DEVICE_NAME", null);
                deviceName.setText(nameBT);
                printerName.setSelection(prefs.getInt("BT_POSITION_NAME", 0));
                connectType.setSelection(prefs.getInt("BT_CONNECT_TYPE", 0));
                transferType.setSelection(prefs.getInt("BT_TRANSFER_TYPE", 0));
                if (!TextUtils.isEmpty(portBT)) {
                    deviceAddress.setText(portBT);
                    buttonDialog.dismiss();
                    connectDevices(nameBT, portBT);
                } else {
                    Toast.makeText(Usbimprime.this, "first connection", Toast.LENGTH_SHORT).show();
                }
                break;

            case "USB":
                String portUSB = prefs.getString("USB_DEVICE_ADDRESS", null);
                String nameUSB = prefs.getString("USB_DEVICE_NAME", null);
                deviceName.setText(nameUSB);
                printerName.setSelection(prefs.getInt("USB_POSITION_NAME", 0));
                connectType.setSelection(prefs.getInt("USB_CONNECT_TYPE", 0));
                transferType.setSelection(prefs.getInt("USB_TRANSFER_TYPE", 0));
                if (!TextUtils.isEmpty(portUSB)) {
                    deviceAddress.setText(portUSB);
                    buttonDialog.dismiss();
                    connectDevices(nameUSB, portUSB);
                } else {
                    Toast.makeText(Usbimprime.this, "first connection", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                Toast.makeText(Usbimprime.this, "default", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     * 本地存储
     * 根据连接类型的不同去本地存储
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("regoprintdemo", Context.MODE_PRIVATE).edit();
        editor.putString("TYPE", connectType.getSelectedItem().toString());
        Log.v("TAG", "type类型onpause" + connectType.getSelectedItem().toString());
        switch (connectType.getSelectedItem().toString()) {
            case "Bluetooth":
                editor.putInt("BT_POSITION_NAME", printerName.getSelectedItemPosition());
                editor.putInt("BT_CONNECT_TYPE", connectType.getSelectedItemPosition());
                editor.putInt("BT_TRANSFER_TYPE", transferType.getSelectedItemPosition());
                editor.putString("BT_DEVICE_NAME", deviceName.getText().toString());
                editor.putString("BT_DEVICE_ADDRESS", deviceAddress.getText().toString());
                editor.commit();
                break;

            case "USB":
                editor.putInt("USB_POSITION_NAME", printerName.getSelectedItemPosition());
                editor.putInt("USB_CONNECT_TYPE", connectType.getSelectedItemPosition());
                editor.putInt("USB_TRANSFER_TYPE", transferType.getSelectedItemPosition());
                editor.putString("USB_DEVICE_NAME", deviceName.getText().toString());
                editor.putString("USB_DEVICE_ADDRESS", deviceAddress.getText().toString());
                editor.commit();
                break;
            case "Serial":
                editor.putInt("Serial_POSITION_NAME", printerName.getSelectedItemPosition());
                editor.putInt("Serial_CONNECT_TYPE", connectType.getSelectedItemPosition());
                editor.putInt("Serial_TRANSFER_TYPE", transferType.getSelectedItemPosition());
                editor.putString("Serial_DEVICE_NAME", deviceName.getText().toString());
                editor.putString("Serial_DEVICE_ADDRESS", deviceAddress.getText().toString());
                editor.commit();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
    }
}