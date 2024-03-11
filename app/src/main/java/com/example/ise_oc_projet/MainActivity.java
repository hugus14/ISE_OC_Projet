package com.example.ise_oc_projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    Request request;
    // Obtenez le timestamp actuel en millisecondes
    long timestamp;
    String timestampString;
    Button back;
    Button forward;
    Button left;
    Button right;

    TextView tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenez le timestamp actuel en millisecondes
        timestamp = System.currentTimeMillis() / 1000;
        timestampString = String.valueOf(timestamp);

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
                            Log.e("test", "Erreur : " + response);
                            throw new IOException("Erreur : " + response);
                        } else {
                            Log.d("Test", webContent);
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
                            Log.e("test", "Erreur : " + response);
                            throw new IOException("Erreur : " + response);
                        } else {
                            Log.d("Test", webContent);
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
                            Log.e("test", "Erreur : " + response);
                            throw new IOException("Erreur : " + response);
                        } else {
                            Log.d("Test", webContent);
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
                            Log.e("test", "Erreur : " + response);
                            throw new IOException("Erreur : " + response);
                        } else {
                            Log.d("Test", webContent);
                            MainActivity.this.runOnUiThread(()->(tb).setText(webContent));
                        }
                    }
                });

            }
        });



    }
}