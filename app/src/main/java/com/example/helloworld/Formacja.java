package com.example.helloworld;


import android.os.Bundle;

public class Formacja extends ZakladkaPobierajacaTekst {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tekst = Dane.formacja;
        super.onCreate(savedInstanceState);
    }
}
