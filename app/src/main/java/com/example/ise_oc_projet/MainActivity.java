package com.example.ise_oc_projet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.net.Uri;

import android.os.*;
import android.provider.Settings;
import android.view.View;
import android.widget.*;

/**
 * this class is the main part of our project,
 * it centralizes the bluetooth and the web api
 * with the brightness of the screen
 *
 * @author LEVEEL LE MOUELLIC LEVILLAIN
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity  implements CallBackMain {


    //*** VARIABLES ***//

    // Action
    Button back;
    Button forward;
    Button left;
    Button right;
    Button bluetooth;

    // Result for interface
    TextView tb;

    // Luminosity
    int brightnessLevel;
    SeekBar brightnessSeekBar;
    CheckBox autoBrightnessCheckBox;

    // API
    ApiWeb apiWeb;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Buttons and result for the result
        tb = findViewById(R.id.result);
        back = findViewById(R.id.back);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        forward = findViewById(R.id.forward);
        bluetooth = findViewById(R.id.btn_bluetooth);

        // Init buttons for luminosity
        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);
        autoBrightnessCheckBox = findViewById(R.id.autoBrightnessCheckBox);

        checkAndRequestPermissions();

        // When the user click on bluetooth button
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bluetoothActivity = new Intent(MainActivity.this,DeviceListActivity.class);
                startActivity(bluetoothActivity);
            }
        });


        // When the user click on the button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the luminosity
                brightnessLevel = getScreenBrightness();
                // Call API class
                apiWeb = new ApiWeb("back", brightnessLevel);
                apiWeb.requetAPI(MainActivity.this);
                // Send with bluetooth
                Singleton_BT_interface.send("back");

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the luminosity
                brightnessLevel = getScreenBrightness();
                // Call API class
                apiWeb = new ApiWeb("forward", brightnessLevel);
                apiWeb.requetAPI(MainActivity.this);
                // Send with bluetooth
                Singleton_BT_interface.send("forward");

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the luminosity
                brightnessLevel = getScreenBrightness();
                // Call API class
                apiWeb = new ApiWeb("left", brightnessLevel);
                apiWeb.requetAPI(MainActivity.this);
                // Send with bluetooth
                Singleton_BT_interface.send("left");

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the luminosity
                brightnessLevel = getScreenBrightness();
                // Call API class
                apiWeb = new ApiWeb("right", brightnessLevel);
                apiWeb.requetAPI(MainActivity.this);
                // Send with bluetooth
                Singleton_BT_interface.send("right");

            }
        });

        // Get the luminosity
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

    }

    /**
     * method for changing the luminosity of the screen
     * @param brightnessValue
     */
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

    /**
     * method for getting the luminosity of the user
     * @param value
     */
    private void setAutoBrightness(boolean value) {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * method for add the permission
     */
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);
            }
        }
    }

    /**
     * method for getting the luminosity of the screen
     * @return the luminosity
     */
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

    /**
     * method for destroy the bluetooth connection
     */
    @Override
    protected void onDestroy() {
        Singleton_BT_interface.closeConnexion();
        super.onDestroy();


    }

    /**
     * method for refresh the screen
     * @param webContent
     */
    @Override
    public void notifyApiWebEnded(String webContent) {
        runOnUiThread(()->(tb).setText(webContent));

    }
}