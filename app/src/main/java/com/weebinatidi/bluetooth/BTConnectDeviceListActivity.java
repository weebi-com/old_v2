package com.weebinatidi.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.weebinatidi.R;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.DeviceListDialogAdapter;
import com.weebinatidi.ui.Device_Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BTConnectDeviceListActivity extends BaseActivity implements  AdapterView.OnItemClickListener {

    BluetoothAdapter mBluetoothAdapter;
    private List<Device_Data> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_btconnect_device_list;
    }


    /**
     * bluetooth data
     */
    private void getBlueToothData() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(BTConnectDeviceListActivity.this, "No supports bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
            } else {
                Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
                if (devices.size() > 0) {
                    for (BluetoothDevice device : devices) {
                        list.add(new Device_Data(device.getName(), device.getAddress()));
                    }
                } else {
                    Toast.makeText(BTConnectDeviceListActivity.this, "No device", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /**
     * set data
     * @return  return a list to memory data
     */
    private List<Device_Data> initData() {
        getBlueToothData();
        return list;
    }


    public void initView(){
        ListView lv = findViewById(R.id.lv);
        DeviceListDialogAdapter lAdapter = new DeviceListDialogAdapter(BTConnectDeviceListActivity.this, initData());
        lv.setAdapter(lAdapter);
        lv.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("DEVICE_NAME", initData().get(position).getDeviceName());
        intent.putExtra("DEVICE_ADDRESS", initData().get(position).getDeviceAddress());
        setResult(RESULT_OK, intent);
        finish();
    }


}
