package com.weebinatidi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weebinatidi.R;

import java.util.List;

/**
 * Created by mbp on 07/07/2017.
 */

public class DeviceListDialogAdapter extends BaseAdapter {
    private final Context context;
    private final List<Device_Data> list;
    private ViewHolder holder;

    public DeviceListDialogAdapter(Context context, List<Device_Data> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_devicelistdialog, null);
            holder.deviceName = (TextView) convertView.findViewById(R.id.tv_devicename);
            holder.deviceAddress = (TextView) convertView.findViewById(R.id.tv_deviceaddress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.deviceName.setText(list.get(position).getDeviceName());
        holder.deviceAddress.setText(list.get(position).getDeviceAddress());
        return convertView;
    }

    public void clear() {
        list.clear();
    }

    class ViewHolder {
        public TextView deviceName, deviceAddress;
    }

}
