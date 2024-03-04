package com.example.ise_oc_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class RobotInterfaceButton extends AppCompatActivity {

    Button forward;
    Button back;
    Button left;
    Button right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_interface_button);

        forward = findViewById(R.id.forward);
        back    = findViewById(R.id.back);
        left    = findViewById(R.id.left);
        right   = findViewById(R.id.right);

        forward.setText("FORWARD");
        back.setText("BACK");
        left.setText("LEFT");
        right.setText("RIGHT");
    }
}