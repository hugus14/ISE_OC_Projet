package com.example.ise_oc_projet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.*;


import java.io.IOException;

import okhttp3.*;

public class MainActivity extends AppCompatActivity  implements CallBackMain {


    //*** VARIABLES ***//
    // Timestamp
    long timestamp;
    String timestampString;

    // Action
    Button back;
    Button forward;
    Button left;
    Button right;

    Button bluetooth;

    // Result
    TextView tb;

    int brightnessLevel;

    ApiWeb apiWeb;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //To Connect to bluetooth device, start bluetoothActivity


        //Use Singleton_BT_interface to send and control BT connection socket.
        // = Singleton_BT_interface.send("Forward");
        //if connection is

        SeekBar brightnessSeekBar;
        CheckBox autoBrightnessCheckBox;

        // To get the timestamp in mS
        timestamp = System.currentTimeMillis() / 1000;
        timestampString = String.valueOf(timestamp);

        // Init Buttons and result
        tb = findViewById(R.id.result);
        back = findViewById(R.id.back);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        forward = findViewById(R.id.forward);
        bluetooth = findViewById(R.id.btn_bluetooth);

        checkAndRequestPermissions();


        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);
        autoBrightnessCheckBox = findViewById(R.id.autoBrightnessCheckBox);


        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bluetoothActivity = new Intent(MainActivity.this,DeviceListActivity.class);
                startActivity(bluetoothActivity);
            }
        });

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!autoBrightnessCheckBox.isChecked()) {
                    changeScreenBrightness(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        autoBrightnessCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setAutoBrightness(true);
            } else {
                setAutoBrightness(false);
                changeScreenBrightness(brightnessSeekBar.getProgress());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightnessLevel = getScreenBrightness();
                apiWeb = new ApiWeb("back", brightnessLevel);
                apiWeb.requetAPI(MainActivity.this);

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightnessLevel = getScreenBrightness();
                apiWeb = new ApiWeb("forward", brightnessLevel);
                apiWeb.requetAPI(MainActivity.this);

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightnessLevel = getScreenBrightness();
                brightnessLevel = getScreenBrightness();
                apiWeb = new ApiWeb("left", brightnessLevel);
                apiWeb.requetAPI(MainActivity.this);

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightnessLevel = getScreenBrightness();
                brightnessLevel = getScreenBrightness();
                apiWeb = new ApiWeb("right", brightnessLevel);
                apiWeb.requetAPI(MainActivity.this);

            }
        });


    }

    private void changeScreenBrightness(int brightnessValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getApplicationContext())) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightnessValue);
            } else {
                // Montrez à l'utilisateur comment activer cette permission
                Toast.makeText(this, "Please enable write settings for this app", Toast.LENGTH_LONG).show();
                checkAndRequestPermissions();
            }
        } else {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightnessValue);
        }
    }

    private void setAutoBrightness(boolean value) {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);
            }
        }
    }

    private int getScreenBrightness() {
        int brightness = 0;
        ContentResolver contentResolver = getContentResolver();
        try {
            // Récupérer le niveau de luminosité actuel depuis les paramètres système
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightness;
    }

    @Override
    protected void onDestroy() {
        Singleton_BT_interface.closeConnexion();
        super.onDestroy();


    }


    @Override
    public void notifyApiWebEnded(String webContent) {
        runOnUiThread(()->(tb).setText(webContent));

    }
}