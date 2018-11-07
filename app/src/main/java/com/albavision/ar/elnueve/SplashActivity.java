package com.albavision.ar.elnueve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        //Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
