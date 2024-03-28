package com.example.ise_oc_projet;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mmBTAdaptater;
    private final Activity myActivity;

    private static final String TAG = "ConnectThread";

    public ConnectThread(Activity activity, BluetoothAdapter bluetoothAdapter, BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;
        mmBTAdaptater = bluetoothAdapter;
        myActivity = activity;

        Log.d(TAG, "start creating connexion socket for BT with device ");


        try {

            UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            //Arrays.stream(device.getUuids()).forEach(parcelUuid -> Log.d(TAG, String.valueOf(parcelUuid)));
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }

        mmSocket = tmp;
    }

    public void run(){
        // Cancel discovery because it otherwise slows down the connection.
        mmBTAdaptater.cancelDiscovery();

        try {
            mmSocket.connect();
        } catch (IOException connectException) {
            Log.e(TAG, "Could not connect the client socket", connectException);
            myActivity.runOnUiThread(() -> Toast.makeText(myActivity, "Error to connect Bluetooth device", Toast.LENGTH_SHORT).show());
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }
        myActivity.runOnUiThread(() -> Toast.makeText(myActivity, "Device Connected", Toast.LENGTH_SHORT).show());
        Singleton_BT_interface.set(mmSocket);
        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
    }


    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}