package com.example.helloworld;

import android.os.Bundle;


public class Materialy extends ZakladkaPobierajacaTekst {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tekst = Dane.materiały;
        super.onCreate(savedInstanceState);
    }
}
