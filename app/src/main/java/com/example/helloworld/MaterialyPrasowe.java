package com.example.helloworld;


import android.os.Bundle;

public class MaterialyPrasowe extends ZakladkaPobierajacaTekst{



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tekst = Dane.materiałyPrasowe;
        super.onCreate(savedInstanceState);

    }
}

