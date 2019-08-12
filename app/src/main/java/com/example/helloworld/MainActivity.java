
package com.example.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    //ładuje ponownie MainActivity
    public void odswiez() {

        finish();
        startActivity(getIntent());
    }

    private FirebaseAnalytics mFirebaseAnalytics;
    private WebView myWebView;

    //to bardzo ważna funkcja
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Dane.ta_aktywnosc = this;

        //Wyświetlanie odpowiedniego layoutu
        setContentView(R.layout.activity_main);

        //Wyświetlanie treści napisanej w html i różne ustawienia Webview
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(Dane.główna());


        //Menu w prawym górnym rogu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Przycisk w lewym dolnym rogu
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gdy naciśnie się przycisk ten po prawej na dole to dzwoni
                //ale widać go tylko w zakładce kontakt
                call(view);
            }
        });

        //Pasek poczny z opcjami
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       //Tutaj ustawiam widzialnosc poszczegolnych elementow paska bocznego
        Menu nav_Menu = navigationView.getMenu();
        if(Dane.czy_zalogowany()) {

            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(true);
        }
        else {

            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(false);
        }


        //wysyłąnie danych do analizy w Firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "niewiem");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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
        if(Dane.czy_zalogowany()) {

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

    private void zmieńZakładkę(String adres, String nagłówek, Boolean przycisk, Boolean internet) {

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        myWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                //Dodadtowe działania, do zrobienia później
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            }
        });
        myWebView.loadUrl(adres);

        if(przycisk)
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);


        setTitle(nagłówek);
    }

    //tutaj ustawiamy co się dzieje jak coś klikniemy w bocznym pasku
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_main_menu) {
            zmieńZakładkę(Dane.główna(), "FDNT", false, true);
        }
        else if (id == R.id.nav_o_fundacji) {
            zmieńZakładkę(Dane.o_fundacji(), "O Fundacji", false, false);
        }
        else if (id == R.id.nav_nasz_patron) {
            zmieńZakładkę(Dane.nasz_patron(), "Nasz Patron", false, false);
        }
        else if (id == R.id.nav_dla_darczyncy) {
            zmieńZakładkę(Dane.dla_daroczyncy(), "Dla Daroczyńcy", false, false);
        }
        else if (id == R.id.nav_materialy_prasowe) {
            zmieńZakładkę(Dane.materialy_prasowe(), "Materiały Prasowe", false, false);
        }
        else if (id == R.id.nav_kontakt) {
            zmieńZakładkę(Dane.kontakt(), "Kontakt", true, false);
        }
        else if (id == R.id.nav_formacja) {
            zmieńZakładkę(Dane.formacja(), "Formacja", false, true);
        }
        else if (id == R.id.nav_ogl_ogolne) {
            zmieńZakładkę(Dane.ogl_ogolne(), "Ogłoszenia ogólne", false, true);
        }
        else if (id == R.id.nav_ogl_wspolnotowe) {
            zmieńZakładkę(Dane.oglWspólnotowe(), "Ogłoszenia Wspólnotowe", false, true);
        }
        else if (id == R.id.nav_kom_for) {
            zmieńZakładkę(Dane.komunikator(), "Komunikator", false, true);
        }
        else if (id == R.id.nav_materialy) {
            zmieńZakładkę(Dane.materialy(), "Materiały", false, true);
        }
        else if (id == R.id.nav_poczta) {
            zmieńZakładkę(Dane.poczta(), "Poczta", false, true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logInOut(View view) {

        if(!Dane.czy_zalogowany()) {
            Intent intent = new Intent(this, Logowanie.class);
            startActivity(intent);
        }
        else {
            FirebaseAuth.getInstance().signOut();
            odswiez();
        }
    }

    public void startUstawienia(MenuItem item) {

        Intent intent = new Intent(this, Ustawienia.class);
        startActivity(intent);
    }

    public void call(View view) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:+48225304828"));
        startActivity(callIntent);
    }

    public void email(View view) {

        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
        emailIntent.setData(Uri.parse("mailto:dzielo@episkopat.pl"));
        //emailIntent.setType("text/plain");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }


}