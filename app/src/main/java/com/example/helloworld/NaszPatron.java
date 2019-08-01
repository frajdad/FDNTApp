package com.example.helloworld;

import android.os.Bundle;


public class NaszPatron extends ZakladkaWyswietlajaca{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.nasz_patron;
        Dane.czy_chcemy_Internet = false;
        super.onCreate(savedInstanceState);

    }


}
