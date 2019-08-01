package com.example.helloworld;

import android.os.Bundle;

public class Poczta extends ZakladkaWyswietlajaca {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.poczta;
        Dane.czy_chcemy_Internet = true;
        Dane.czy_jest_przycisk = false;
        super.onCreate(savedInstanceState);

    }
}
