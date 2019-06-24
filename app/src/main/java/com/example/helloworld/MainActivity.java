
package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    //ładuje ponownie MainActivity
    public void odswiez() {

        finish();
        startActivity(getIntent());
    }


    //to bardzo ważna funkcja
    protected void onCreate(Bundle savedInstanceState) {

        Dane.ta_aktywnosc = this;
        new AktualizacjaDanych().aktualizuj();

        super.onCreate(savedInstanceState);
        setContentView(Dane.doWyświetlenia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Moja akcja", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       //Tutaj ustawiam widzialnosc poszczegolnych elementow paska bocznego
        Menu nav_Menu = navigationView.getMenu();

        if(Dane.czy_zalogowany) {

            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(true);
        }

        else {

            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(false);

        }



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //tu robimy cos, żeby było widać odpowiednie napisy na samej górze paska bocznego
        //nie wiem czy to najlepsze miejsce na to, ale działa
        if(Dane.czy_zalogowany) {

            TextView email_text = findViewById(R.id.miejsce_na_email);
            email_text.setText(Dane.email());
            TextView name_text = findViewById(R.id.miejsce_logowanie);
            name_text.setText("Wyloguj się");


        }
        else {
            TextView email_text = findViewById(R.id.miejsce_na_email);
            email_text.setText(" ");
            TextView name_text = findViewById(R.id.miejsce_logowanie);
            name_text.setText("Zaloguj się");
        }

        if(Dane.zakładka_ze_zmiennym_tekstem) {
            TextView name_text = findViewById(R.id.text_materialy_prasowe);
            String[] napis = Dane.tekstDoWyświetlenia.split("\\\\n");
            String tekst ="";
            for(int i=0; i<napis.length; i++) {
                tekst=tekst+napis[i]+"\n";
            }
            name_text.setText(tekst);
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //tutaj ustawiamy co się dzieje jak coś klikniemy w bocznym pasku
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {



        int id = item.getItemId();

        if (id == R.id.nav_main_menu) {
            // Handle the camera action
        }
        else if (id == R.id.nav_o_fundacji) {
            Intent intent = new Intent(this, OFundacji.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_nasz_patron) {
            Intent intent = new Intent(this, NaszPatron.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_dla_darczyncy) {
            Intent intent = new Intent(this, DlaDarczyncy.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_materialy_prasowe) {
            Intent intent = new Intent(this, MaterialyPrasowe.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_kontakt) {
            Intent intent = new Intent(this, Kontakt.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_formacja) {
            Intent intent = new Intent(this, Formacja.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_ogl_ogolne) {
            Intent intent = new Intent(this, OglOgolne.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_ogl_wspolnotowe) {
            Intent intent = new Intent(this, OglWspolnotowe.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_kom_for) {
            Intent intent = new Intent(this, KomFor.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_materialy) {
            Intent intent = new Intent(this, Materialy.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_poczta) {
            Intent intent = new Intent(this, Poczta.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logInOut(View view) {

        if(!Dane.czy_zalogowany) {
            Intent intent = new Intent(this, Logowanie.class);
            startActivity(intent);
        }
        else {

            Dane.czy_zalogowany=false;
            odswiez();
        }
    }


}