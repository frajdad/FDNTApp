package com.example.helloworld;

import android.os.Bundle;


public class DlaDarczyncy extends ZakladkaWyswietlajaca {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.dla_daroczyncy;
        Dane.czy_chcemy_Internet = false;
        super.onCreate(savedInstanceState);
    }
}
