package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class ZakladkaWyswietlajaca extends AppCompatActivity {

    protected String adres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Dane.ta_aktywnosc.finish();
        Dane.doWyświetlenia = R.layout.activity_main;

        Dane.zawartoscDoWyświetlenia = adres;

        this.finish();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

}