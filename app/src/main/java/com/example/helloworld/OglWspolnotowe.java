package com.example.helloworld;

import android.os.Bundle;


public class OglWspolnotowe extends ZakladkaWyswietlajaca{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.oglWspólnotowe();
        Dane.czy_chcemy_Internet = true;
        super.onCreate(savedInstanceState);
    }

}
