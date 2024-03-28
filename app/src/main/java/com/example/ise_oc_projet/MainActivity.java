package com.example.ise_oc_projet;

import static com.example.ise_oc_projet.StateSocket.*;

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

public class MainActivity extends AppCompatActivity {


    //*** VARIABLES ***//
    // WEB
    OkHttpClient client;
    Request request;

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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //To Connect to bluetooth device, start bluetoothActivity
        Intent bluetoothActivity = new Intent(MainActivity.this,DeviceListActivity.class);
        startActivity(bluetoothActivity);


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


        // LUMINOSITY
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

        // BLUETOOTH

        //Use Singleton_BT_interface to send and control BT connection socket.

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateSocket rslt = Singleton_BT_interface.send("Forward");

                if (rslt == NULL || rslt == DISCONNECTED  || rslt == SENDING_ERROR){
                    tb.setText("Erreur enzo ton bluethooth marche pas !");
                }
            }
        });



        // MA PARTIE
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightnessLevel = getScreenBrightness();
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=" + brightnessLevel + "&timestamp=" + timestamp + "&action=back")
                        .build();
                tb.setText("back");

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final String webContent = response.body().string();
                        if(!response.isSuccessful()){
                            Log.e("Projet gp 56", "Erreur : " + response);
                            throw new IOException("Erreur : " + response);
                        } else {
                            Log.d("Projet gp 56", webContent);
                            MainActivity.this.runOnUiThread(()->(tb).setText(webContent));
                        }
                    }
                });

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightnessLevel = getScreenBrightness();
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=" + brightnessLevel + "&timestamp=" + timestamp + "&action=forward")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final String webContent = response.body().string();
                        if(!response.isSuccessful()){
                            Log.e("Projet gp 56", "Erreur : " + response);
                            throw new IOException("Erreur : " + response);
                        } else {
                            Log.d("Projet gp 56", webContent);
                            MainActivity.this.runOnUiThread(()->(tb).setText(webContent));
                        }
                    }
                });

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightnessLevel = getScreenBrightness();
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=" + brightnessLevel + "&timestamp=" + timestamp + "&action=left")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final String webContent = response.body().string();
                        if(!response.isSuccessful()){
                            Log.e("Projet gp 56", "Erreur : " + response);
                            throw new IOException("Erreur : " + response);
                        } else {
                            Log.d("Projet gp 56", webContent);
                            MainActivity.this.runOnUiThread(()->(tb).setText(webContent));
                        }
                    }
                });

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brightnessLevel = getScreenBrightness();
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=" + brightnessLevel + "&timestamp=" + timestamp + "&action=right")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final String webContent = response.body().string();
                        if(!response.isSuccessful()){
                            Log.e("Projet gp 56", "Erreur : " + response);
                            throw new IOException("Erreur : " + response);
                        } else {
                            Log.d("Projet gp 56", webContent);
                            MainActivity.this.runOnUiThread(()->(tb).setText(webContent));
                        }
                    }
                });

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

}