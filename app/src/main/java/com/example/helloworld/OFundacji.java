package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class OFundacji extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Dane.ta_aktywnosc.finish();
        Dane.doWyświetlenia = R.layout.activity_o_fundacji;
        Dane.zakładka_ze_zmiennym_tekstem = false;

        this.finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

