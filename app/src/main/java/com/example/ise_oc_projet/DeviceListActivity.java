package com.example.ise_oc_projet;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static final int ACTIVITY_RESULT_BT_ENBALE = 542104;
    public static final int RSLT_CODE_ACCESS_FINE_LOCATION = 24875136;
    public static final int RSLT_CODE_ACCESS_BACKGROUND_LOCATION = 26598451;
    public static final int RSLT_CODE_BLUETOOTH_SCAN = 7418514;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private ArrayAdapter<String> mDevicesArrayAdapter;
    private ListView newDevicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        newDevicesListView = (ListView) findViewById(R.id.available_devices);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void show_available_device() {

        mDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_item, R.id.device_name);
        Log.println(Log.INFO, "DeviceListActivity", "Show_available_device is starting");

        newDevicesListView.setAdapter(mDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(itemlistener);


        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        // Start discovery
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
            Log.println(Log.ERROR, "DeviceListActivity", "BLUETOOTH_SCAN must be granted for bluetooth scanning.");
            return;
        }

        bluetoothAdapter.startDiscovery();
    }

    private final AdapterView.OnItemClickListener itemlistener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object o = newDevicesListView.getItemAtPosition(position);
            String name = (String)o; //As you are using Default String Adapter
            Log.println(Log.INFO, "DeviceListActivity", "item selected : "+name);
        }
    };

    // The BroadcastReceiver that listens for discovered devices
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.println(Log.INFO, "DeviceListActivity", "Device has been find");
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                //if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                String deviceName = device.getName();
                if(deviceName != null){
                    Log.println(Log.INFO, "DeviceListActivity", "device found : "+deviceName);

                    mDevicesArrayAdapter.add(deviceName);
                    mDevicesArrayAdapter.notifyDataSetChanged();
                }else{
                    Log.println(Log.INFO, "DeviceListActivity", "device found but null, error has been handle correctly");

                }

               // }
            }
        }
    };


    private void init() {
        bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, RSLT_CODE_ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeviceListActivity.this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, RSLT_CODE_ACCESS_FINE_LOCATION);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DeviceListActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_SCAN}, RSLT_CODE_ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DeviceListActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, RSLT_CODE_ACCESS_FINE_LOCATION);
            }
        }else {
            Log.println(Log.INFO, "DeviceListActivity", "current build version ("+Build.VERSION.SDK_INT+") is < VERSION_CODES.S ");
        }

        check_permission_log();


        if(ask_for_BT()){
            show_available_device();
        }



    }

    private void check_permission_log(){
        Log.println(Log.INFO, "DeviceListActivity", "ACCESS_BACKGROUND_LOCATION is "+(( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)?"granted":"denied"));
        Log.println(Log.INFO, "DeviceListActivity", "BLUETOOTH_SCAN is "+(( ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED)?"granted":"denied"));
        Log.println(Log.INFO, "DeviceListActivity", "ACCESS_FINE_LOCATION is "+(( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)?"granted":"denied"));
        Log.println(Log.INFO, "DeviceListActivity", "BLUETOOTH_CONNECT is "+(( ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)?"granted":"denied"));

    }

    private boolean ask_for_BT(){
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ACTIVITY_RESULT_BT_ENBALE);
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ACTIVITY_RESULT_BT_ENBALE){
               show_available_device();
        }
        else if(resultCode == RSLT_CODE_ACCESS_FINE_LOCATION){
            check_permission_log();
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

}
