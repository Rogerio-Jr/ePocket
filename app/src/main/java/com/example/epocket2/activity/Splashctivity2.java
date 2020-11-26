package com.example.epocket2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.epocket2.R;

public class Splashctivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashctivity2);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            abrirTelaPrincipal();
            }
        },3000);

    }
    public void abrirTelaPrincipal(){
        Intent i = new Intent(Splashctivity2.this, PrincipalActivity.class);
        startActivity(i);
        finish();
    }
}