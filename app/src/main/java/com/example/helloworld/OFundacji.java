package com.example.helloworld;


import android.os.Bundle;

public class OFundacji extends ZakladkaWyswietlajaca{


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.o_fundacji;
        Dane.czy_chcemy_Internet = false;
        super.onCreate(savedInstanceState);
    }

}

