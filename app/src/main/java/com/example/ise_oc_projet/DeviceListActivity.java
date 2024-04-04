package com.example.ise_oc_projet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class DeviceListActivity extends AppCompatActivity {
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static final int ACTIVITY_RESULT_BT_ENABLE = 542104;
    public static final int RSLT_CODE_ACCESS_FINE_LOCATION = 24875136;
    public static final int RSLT_CODE_ACCESS_BACKGROUND_LOCATION = 26598451;
    public static final int RSLT_CODE_BLUETOOTH_SCAN = 7418514;
    private static final String TAG = "DeviceListActivity";


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;

    //private ArrayAdapter<String> mDevicesArrayAdapter;
    private DeviceListAdaptater device_adaptater;

    private ArrayList<BluetoothDevice> availableBTDevices;
    private ListView newDevicesListView;
    private ImageView IMV_BT_status;
    private Button button_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        newDevicesListView = (ListView) findViewById(R.id.available_devices);
        IMV_BT_status = (ImageView) findViewById(R.id.IMV_BT_status);
        button_finish = (Button) findViewById(R.id.button_finish);

        AlertDialog normalExitDialog = create_NormalExitDialog();
        AlertDialog noConnectDialog = create_NoConnectDialog();

        button_finish.setOnClickListener(view -> {
            if(!Singleton_BT_interface.isConnected()){
                noConnectDialog.show();
            }else{
                normalExitDialog.show();
            }
        });

        availableBTDevices = new ArrayList<BluetoothDevice>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        check_permission_log();

        if (ask_for_BT()) {
            show_available_device();
        }
    }

    private void show_available_device() {
        //mDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_item, R.id.device_name);
        device_adaptater = new DeviceListAdaptater(this, bluetoothAdapter);
        Log.println(Log.INFO, TAG, "Show_available_device is starting");

        newDevicesListView.setAdapter(device_adaptater);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        newDevicesListView.setOnItemClickListener(clickOnItem);

        // Start discovery
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
            Log.println(Log.ERROR, TAG, "BLUETOOTH_SCAN must be granted for bluetooth scanning.");
            return;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bt : pairedDevices)
            device_adaptater.add(bt);

        startSearching();
    }

    AdapterView.OnItemClickListener clickOnItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            Object object = device_adaptater.getItem(position);
            BluetoothDevice clicked_device = (BluetoothDevice) object;
            BluetoothSocket socket = null;

            Toast.makeText(DeviceListActivity.this, "connecting to device...", Toast.LENGTH_LONG).show();
            Log.println(Log.INFO, TAG, "clicked on device : " + clicked_device.getName());

            try {
                UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                //Arrays.stream(device.getUuids()).forEach(parcelUuid -> Log.d(TAG, String.valueOf(parcelUuid)));
                socket = clicked_device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
                setErrorIcon();
            }

            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
                Singleton_BT_interface.set(socket);
                Toast.makeText(DeviceListActivity.this, "Device Connected", Toast.LENGTH_SHORT).show();
                setConnectedIcon();
            } catch (IOException connectException) {
                Log.e(TAG, "Could not connect the client socket", connectException);
                Toast.makeText(DeviceListActivity.this, "Error to connect Bluetooth device", Toast.LENGTH_SHORT).show();
                setErrorIcon();
                try {
                    socket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                    setErrorIcon();
                }
            }



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
                if (device != null) {
                    Log.d("DeviceListActivity", "device is null");
                } else {
                    if (device.getName() != null && !device_adaptater.doesDeviceExist(device)) {

                        device_adaptater.add(device);
                        //device_adaptater.notifyDataSetChanged();
                    } else {
                        Log.println(Log.INFO, "DeviceListActivity", "device found but null, error has been handle correctly");

                    }
                }
            }
        }
    };

    private void startSearching(){
        bluetoothAdapter.startDiscovery();
        setSearchingIcon();
    }


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


        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeviceListActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_SCAN}, RSLT_CODE_ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeviceListActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, RSLT_CODE_ACCESS_FINE_LOCATION);
        }
        // }else {
        Log.println(Log.INFO, "DeviceListActivity", "current build version (" + Build.VERSION.SDK_INT + ") is < VERSION_CODES.S ");
        //}


    }

    private void check_permission_log() {
        Log.println(Log.INFO, "DeviceListActivity", "ACCESS_BACKGROUND_LOCATION is " + ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) ? "granted" : "denied"));
        Log.println(Log.INFO, "DeviceListActivity", "BLUETOOTH_SCAN is " + ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) ? "granted" : "denied"));
        Log.println(Log.INFO, "DeviceListActivity", "ACCESS_FINE_LOCATION is " + ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ? "granted" : "denied"));
        Log.println(Log.INFO, "DeviceListActivity", "BLUETOOTH_CONNECT is " + ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) ? "granted" : "denied"));

    }

    private boolean ask_for_BT() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ACTIVITY_RESULT_BT_ENABLE);
            return false;
        }
        return true;
    }


    private void setConnectedIcon(){
        IMV_BT_status.setImageResource(R.drawable.bt_connected);
    }
    private void setSearchingIcon(){
        IMV_BT_status.setImageResource(R.drawable.bt_searching);
    }
    private void setDisabledIcon(){
        IMV_BT_status.setImageResource(R.drawable.bt_disable);
    }
    private void setErrorIcon(){
        IMV_BT_status.setImageResource(R.drawable.error);
    }

    private AlertDialog create_NoConnectDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Bluetooth connection alert");
        builder.setMessage("Bluetooth connection is off,\nRemote wont work.\nWould you like to quit ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            this.finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            startSearching();
        });
       return  builder.create();
    }
    private AlertDialog create_NormalExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Exit bluetooth manager");
        builder.setMessage("Are you sure to quit ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            this.finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {

        });
        return builder.create();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ACTIVITY_RESULT_BT_ENABLE) {
            Log.d("onActivityResult", "set on activity result call for BT ");
            show_available_device();
        } else if (resultCode == RSLT_CODE_ACCESS_FINE_LOCATION) {
            check_permission_log();
        }
    }

    @Override
    public void onDestroy() {

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

}
