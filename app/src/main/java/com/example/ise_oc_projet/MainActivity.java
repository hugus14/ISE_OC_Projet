package com.example.ise_oc_projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    // Result
    TextView tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To get the timestamp in mS
        timestamp = System.currentTimeMillis() / 1000;
        timestampString = String.valueOf(timestamp);

        // Init Buttons and result
        tb = findViewById(R.id.result);
        back = findViewById(R.id.back);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        forward = findViewById(R.id.forward);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=10&timestamp=" + timestamp + "&action=back")
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

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=10&timestamp=" + timestamp + "&action=forward")
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
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=10&timestamp=" + timestamp + "&action=left")
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
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=10&timestamp=" + timestamp + "&action=right")
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
}