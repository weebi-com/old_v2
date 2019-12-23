package com.weebinatidi.ui;

/**
 * Created by mbp on 07/07/2017.
 */

public class Device_Data {
    private String deviceName;
    private String deviceAddress;

    public Device_Data(String deviceName, String deviceAddress) {
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
