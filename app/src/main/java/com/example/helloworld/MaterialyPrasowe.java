package com.example.helloworld;


import android.os.Bundle;

public class MaterialyPrasowe extends ZakladkaWyswietlajaca{



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adres = Dane.materialy_prasowe;
        Dane.czy_chcemy_Internet = false;
        super.onCreate(savedInstanceState);

    }
}

