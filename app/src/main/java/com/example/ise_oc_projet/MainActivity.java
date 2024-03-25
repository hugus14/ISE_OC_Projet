package com.example.ise_oc_projet;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To Connect to bluetooth device, start bluetoothActivity
            Intent bluetoothActivity = new Intent(MainActivity.this,DeviceListActivity.class);
            startActivity(bluetoothActivity);

        //Use Singleton_BT_interface to send and control BT connection socket.
            //StateSocket rslt = Singleton_BT_interface.send("Forward");
        //if connection is



    }

    @Override
    protected void onDestroy() {
        Singleton_BT_interface.closeConnexion();
        super.onDestroy();
    }
}