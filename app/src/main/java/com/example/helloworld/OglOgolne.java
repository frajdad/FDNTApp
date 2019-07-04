package com.example.helloworld;


import android.os.Bundle;

public class OglOgolne extends ZakladkaWyswietlajaca{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.ogl_ogolne;
        Dane.czy_chcemy_Internet = true;
        super.onCreate(savedInstanceState);
    }
}
