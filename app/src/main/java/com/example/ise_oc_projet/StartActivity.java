package com.example.ise_oc_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button mybtn = findViewById(R.id.BT_start_app);
        mybtn.setOnClickListener(view -> {
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        });
    }
}