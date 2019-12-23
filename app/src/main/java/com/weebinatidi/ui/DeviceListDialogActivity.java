package com.weebinatidi.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.weebinatidi.R;
import com.weebinatidi.WeebiApplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DeviceListDialogActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button back;
    private Spinner comBaud;
    private ListView lv;
    /**
     * 用来存放数据
     */
    private List<Device_Data> list = new ArrayList<Device_Data>();
    private BluetoothAdapter adapter;
    private DeviceListDialogAdapter lAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_device_list_dialog;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        lAdapter = new DeviceListDialogAdapter(DeviceListDialogActivity.this, initData());
        lv.setAdapter(lAdapter);
        lv.setOnItemClickListener(this);
        comBaud = (Spinner) findViewById(R.id.sv_baud);
        if (getIntent().getStringExtra("Connect_Type").equals("Serial")) {
            comBaud.setVisibility(View.VISIBLE);
        } else {
            comBaud.setVisibility(View.GONE);
        }
        back = (Button) findViewById(R.id.bt_back);
        back.setOnClickListener(this);
    }

    /**
     * 设置数据
     * set data
     *
     * @return 返回一个list用来存放数据 return a list to memory data
     */
    private List<Device_Data> initData() {
        if (getIntent().getStringExtra("Connect_Type").equals("Bluetooth")) {
            getBlueToothData();
        } else if (getIntent().getStringExtra("Connect_Type").equals("Serial")) {
            Log.v("TAG", "串口数据");
            getSerialData();
        }
        return list;
    }

    /**
     * serial数据
     * serial data
     */
    private void getSerialData() {
        for (int i = 0; i < 9; i++) {
            list.add(new Device_Data("/dev/ttyS" + i, null));
        }
        list.add(new Device_Data("/dev/ttyUSB0", null));
        list.add(new Device_Data("/dev/ttyMT1", null));
    }

    /**
     * 蓝牙数据
     * bluetooth data
     */
    private void getBlueToothData() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(DeviceListDialogActivity.this, "not supported bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!adapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
            } else {
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                if (devices.size() > 0) {
                    for (Iterator<BluetoothDevice> it = devices.iterator(); it.hasNext(); ) {
                        BluetoothDevice device = (BluetoothDevice) it.next();
                        list.add(new Device_Data(device.getName(), device.getAddress()));
                    }
                } else {
                    Toast.makeText(DeviceListDialogActivity.this, "pas dappareil", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sv_baud:
                //这里是点击波特率的点击事件
                break;
            case R.id.bt_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(DeviceListDialogActivity.this, "connection", Toast.LENGTH_SHORT).show();
        if (getIntent().getStringExtra("Connect_Type").equals("Bluetooth")) {
            Intent intent = new Intent();
            intent.putExtra("DEVICE_NAME", initData().get(i).getDeviceName());
            intent.putExtra("DEVICE_ADDRESS", initData().get(i).getDeviceAddress());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra("DEVICE_NAME", initData().get(i).getDeviceName());
            intent.putExtra("DEVICE_ADDRESS", initData().get(i).getDeviceName() + ":" + comBaud.getSelectedItem().toString() + ":1:0");
            Log.v("TAG", "返回的port" + initData().get(i).getDeviceAddress() + ":" + comBaud.getSelectedItem().toString() + ":1:0");
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}