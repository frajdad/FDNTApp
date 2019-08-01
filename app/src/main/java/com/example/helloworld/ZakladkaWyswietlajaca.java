package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;


public class ZakladkaWyswietlajaca extends AppCompatActivity {

    protected String adres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Dane.ta_aktywnosc.finish();
        Dane.doWyświetlenia = R.layout.activity_main;

        Dane.zawartoscDoWyświetlenia = adres;

        this.finish();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

    protected static void dodatkoweDziałania(WebView webView) {

        if(Dane.getZawartoscDoWyświetlenia().equals(Dane.poczta)) {

            //webView.loadUrl("javascript:document.getElementsByName('username').value = 'lukasz.kaminski@dzielo.pl'");
            //webView.loadUrl("javascript:document.getElementsByName('Pass').value = 'a02b100x3'");
            //webView.loadUrl("javascript:document.forms['login'].submit()");
        }

    }

}