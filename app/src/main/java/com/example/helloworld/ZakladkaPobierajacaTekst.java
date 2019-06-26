package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class ZakladkaPobierajacaTekst extends AppCompatActivity {

    protected String tekst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Dane.ta_aktywnosc.finish();
        Dane.doWyświetlenia = R.layout.activity_zakladka_z_tekstem;
        Dane.zakładka_ze_zmiennym_tekstem = true;

        new AktualizacjaDanych().aktualizuj();
        Dane.tekstDoWyświetlenia = tekst;

        this.finish();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }


}