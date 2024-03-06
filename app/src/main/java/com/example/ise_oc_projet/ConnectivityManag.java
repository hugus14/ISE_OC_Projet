package com.example.ise_oc_projet;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConnectivityManag extends AppCompatActivity {

    //*** VARIABLES ***//
    Button btnConnection;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnection = findViewById(R.id.button);
        tv = findViewById(R.id.textView);

        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null) tv.setText(networkInfo.getTypeName());
                else tv.setText("Pas de réseau");

            }
        });

    }
}
