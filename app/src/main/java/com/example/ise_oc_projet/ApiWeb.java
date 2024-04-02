package com.example.ise_oc_projet;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiWeb {

    //*** VARIABLES ***//
    // management for web api
    OkHttpClient client;
    Request request;

    // management for timestamp
    long timestamp;
    String timestampString;

    String action;

    int luminosity;


    public ApiWeb(String action, int luminosity){

        // To get the timestamp in mS
        timestamp = System.currentTimeMillis() / 1000;
        timestampString = String.valueOf(timestamp);
        this.action = action;
        this.luminosity = luminosity;

    }

    public void requetAPI(CallBackMain toCall){

        client = new OkHttpClient();
        request = new Request.Builder()
                .url("http://cabani.free.fr/ise/adddata.php?idproject=56&lux=" + luminosity + "&timestamp=" + timestamp + "&action=" + this.action)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String webContent = response.body().string();
                if (!response.isSuccessful()) {
                    Log.e("Projet gp 56", "Erreur : " + response);
                    throw new IOException("Erreur : " + response);

                } else {
                    Log.d("Projet gp 56", webContent);
                    toCall.notifyApiWebEnded(webContent);
                }
            }
        });
    }

}
