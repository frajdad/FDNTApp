package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Logowanie extends AppCompatActivity {



    private Stypendysta baza[] = new Stypendysta[100];

    private void inicjuj() {

        for(int i = 0; i<100; i++) {
            baza[i] = new Stypendysta();
        }

        baza[0] = new Stypendysta("a", "b","Łukasz", "Kamiński","warszawska");
        //baza[1] = new Stypendysta("Paweł", "Pieńczuk", "stypendysta@dzielo.pl", "dzielo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);
    }

    // uruchamia się gdy zostanie przyciśnięty przysisk
    public void zaloguj(View view) {

        inicjuj();

        EditText editTextEmail = (EditText) findViewById(R.id.email);
        String email = editTextEmail.getText().toString();
        EditText editTextPassword = (EditText) findViewById(R.id.password);
        String password = editTextPassword.getText().toString();

        //ten fragment to test - do usunięcia
        /*Intent intent = new Intent(this, TestLogowania.class);
        intent.putExtra(EXTRA_MESSAGE, password);
        startActivity(intent);*/

        znajdzStypendyste(email, password);
    }

    public void znajdzStypendyste(String email, String password) {

        if(!email.equals(" ")) {
            for (int i = 0; i < 100; i++) {

                if(email.equals(baza[i].email()) && baza[i].weryfikuj(password)) {

                    Dane.czy_zalogowany = true;
                    Dane.zalogowany = baza[i];
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Dane.ta_aktywnosc.finish();
                    finish();
                    return;
                }
            }
            TextView zle_dane = findViewById(R.id.zle_dane);
            zle_dane.setVisibility(View.VISIBLE);
        }
    }
}