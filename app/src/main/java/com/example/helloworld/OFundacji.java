package com.example.helloworld;


import android.os.Bundle;

public class OFundacji extends ZakladkaPobierajacaTekst {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tekst = Dane.o_fundacji;
        super.onCreate(savedInstanceState);
    }

}

