package com.example.helloworld;

import android.os.Bundle;

public class StronaGlowna extends ZakladkaWyswietlajaca {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.główna;
        Dane.czy_chcemy_Internet = true;
        super.onCreate(savedInstanceState);
    }
}
