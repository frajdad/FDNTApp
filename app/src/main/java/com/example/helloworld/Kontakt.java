package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Kontakt extends ZakladkaWyswietlajaca {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.kontakt;
        Dane.czy_chcemy_Internet = false;
        Dane.czy_jest_przycisk = true;
        super.onCreate(savedInstanceState);

    }
}
