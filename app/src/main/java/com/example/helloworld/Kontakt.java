package com.example.helloworld;

import android.os.Bundle;


public class Kontakt extends ZakladkaPobierajacaTekst {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tekst = Dane.kontakt;
        super.onCreate(savedInstanceState);

    }
}
