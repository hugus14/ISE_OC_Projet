package com.example.ise_oc_projet;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class BluetoothConnector {

    public static final int ACTIVITY_RESULT_BT_ENBALE = 542104;
    private Activity current_activity;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;


    public BluetoothConnector(Activity currentContext) {
        current_activity = currentContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init() {
        bluetoothManager = current_activity.getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }

        if(ask_for_BT()){
            show_BT_discovery();
        }
        //else do it after ActivityResult

    }


    private boolean ask_for_BT(){
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            current_activity.startActivityForResult(enableBtIntent, ACTIVITY_RESULT_BT_ENBALE);
            return false;
        }
        return true;
    }

    private void show_BT_discovery(){
        Intent dIntent =  new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        dIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        current_activity.startActivity(dIntent);
    }


}
