package com.example.helloworld;


import android.os.Bundle;

public class Formacja extends ZakladkaWyswietlajaca {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.formacja;
        Dane.czy_chcemy_Internet = true;
        super.onCreate(savedInstanceState);
    }
}
