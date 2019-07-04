package com.example.helloworld;

import android.os.Bundle;


public class Materialy extends ZakladkaWyswietlajaca {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.materiały;
        Dane.czy_chcemy_Internet = true;
        super.onCreate(savedInstanceState);
    }
}
