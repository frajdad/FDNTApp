
package com.example.helloworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
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
    private NavigationView navigationView;
    private SharedPreferences preferences;

    private Boolean clear; //Zmienna pilnująca żeby się nie cofnąć za daleko

    //to bardzo ważna funkcja
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Dane.ta_aktywnosc = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Wyświetlanie odpowiedniego layoutu
        setContentView(R.layout.activity_main);


        //Wyświetlanie treści napisanej w html i różne ustawienia Webview
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url) {

                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE); //chowamy kręcące się kółko
                if(clear)
                    myWebView.clearHistory(); //czyścimy historię żeby nie móc się cofać zbyt daleko
                clear = false;
            }

            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                myWebView.loadUrl("file:///android_asset/offline.html");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){

                //You can also use 'url.startsWith()'
                if (url.contains("mailto:")){
                    sendEmail(url);
                    return true;
                }
                else if (url.contains("tel:")) {
                    callTo(url);
                    return true;
                }
                else{
                    // Handle what t do if the link doesn't contain or start with mailto:
                    view.loadUrl(url); // you want to use this otherwise the links in the webview won't work
                }
                return true;
            }
        });

        zmieńZakładkę(Dane.główna(), "FDNT");



        //Menu w prawym górnym rogu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Pasek poczny z opcjami
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       //Tutaj ustawiam widzialnosc poszczegolnych elementow paska bocznego
        Menu nav_Menu = navigationView.getMenu();
        if(Dane.czy_zalogowany()) {

            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(true);
        }
        else {

            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(false);
        }

       //Pobieramy treści związane z tym, jakie zakładki wyświetlić.
        Dane.wczytajUprawnieniaZalogowanego();
        dostosujZakładki();

        //wysyłąnie danych do analizy w Firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        wyświetlPowiadomienia();
    }

    private void wyświetlPowiadomienia() {

        try {

            String tekst = (String) getIntent().getExtras().get("value");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(Html.fromHtml("<i>"+tekst+"</i>"));

                AlertDialog dialog = builder.create();
                dialog.show();


        }
        catch (NullPointerException e) {

        }
    }


    //Co się dzieje jak klikamy "wstecz"
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (myWebView.canGoBack()) {
            myWebView.goBack();
        }
        else {
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
            email_text.setText(Dane.emailZalogowanego());
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


    public void dostosujZakładki() {

        Boolean[] dostęp = new Boolean[50];
        Long kod = Dane.getUprawnienia();
        Menu nav_Menu = navigationView.getMenu();

        for(int i = 0; i<50; i++)
        {
            dostęp[i] = kod % 2 == 1;
            kod /= 2;
        }


        if (dostęp[1]) //1
            nav_Menu.findItem(R.id.nav_formacja).setVisible(true);

        if (dostęp[2]) //2
            nav_Menu.findItem(R.id.nav_ogl_ogolne).setVisible(true);

        if (dostęp[3]) //3
            nav_Menu.findItem(R.id.nav_ogl_wspolnotowe).setVisible(true);

        if (dostęp[10]) //10
            nav_Menu.findItem(R.id.nav_wspol_warszawska).setVisible(true);

        if (dostęp[11]) //11
            nav_Menu.findItem(R.id.nav_warszawscy_pierwszoroczni).setVisible(true);

        if (dostęp[40]) //40
            nav_Menu.findItem(R.id.nav_kom_for).setVisible(true);

        if (dostęp[41]) //41
            nav_Menu.findItem(R.id.nav_materialy).setVisible(true);

        if(dostęp[42]) //42
            nav_Menu.findItem(R.id.nav_poczta).setVisible(true);
    }


    private void zmieńZakładkę(String adres, String nagłówek) {

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        clear = true;

        myWebView.loadUrl(adres);


        setTitle(nagłówek);
    }

    //tutaj ustawiamy co się dzieje jak coś klikniemy w bocznym pasku
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_main_menu) {
            zmieńZakładkę(Dane.główna(), "FDNT");
        }
        else if (id == R.id.nav_o_fundacji) {
            zmieńZakładkę(Dane.o_fundacji(), "O Fundacji");
        }
        else if (id == R.id.nav_nasz_patron) {
            zmieńZakładkę(Dane.nasz_patron(), "Nasz Patron");
        }
        else if (id == R.id.nav_dla_darczyncy) {
            zmieńZakładkę(Dane.dla_daroczyncy(), "Dla Darczyńcy");
        }
        else if (id == R.id.nav_materialy_prasowe) {
            zmieńZakładkę(Dane.materialy_prasowe(), "Materiały Prasowe");
        }
        else if (id == R.id.nav_kontakt) {
            zmieńZakładkę(Dane.kontakt(), "Kontakt");
        }
        else if (id == R.id.nav_formacja) {
            zmieńZakładkę(Dane.formacja(), "Formacja");
        }
        else if (id == R.id.nav_ogl_ogolne) {
            zmieńZakładkę(Dane.ogl_ogolne(), "Ogłoszenia ogólne");
        }
        else if (id == R.id.nav_ogl_wspolnotowe) {
            zmieńZakładkę(Dane.oglWspólnotowe(), "Ogłoszenia Wspólnotowe");
        }
        else if (id == R.id.nav_wspol_warszawska) {
            zmieńZakładkę(Dane.wsp_warszawska(), "Wspólnota Warszawska");
        }
        else if (id == R.id.nav_warszawscy_pierwszoroczni) {
            zmieńZakładkę(Dane.warszawscy_pierwszoroczni(), "Warszawscy Pierwszoroczni");
        }
        else if (id == R.id.nav_kom_for) {
            zmieńZakładkę(Dane.komunikator(), "Komunikator");
        }
        else if (id == R.id.nav_materialy) {
            zmieńZakładkę(Dane.materialy(), "Materiały");
        }
        else if (id == R.id.nav_poczta) {
            zmieńZakładkę(Dane.poczta(), "Poczta");
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

        Intent intent = new Intent(this, UstawieniaAX.class);
        startActivity(intent);
    }


    public void callTo(String command) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(command));
        startActivity(callIntent);
    }

    public void sendEmail(String command) {

        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
        emailIntent.setData(Uri.parse(command));
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

}