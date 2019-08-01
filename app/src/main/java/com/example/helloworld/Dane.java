package com.example.helloworld;

//chcę tu trzymać jakieś globalne dane


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dane {

    public String nazwaWersjiAplikacji() {

        return BuildConfig.VERSION_NAME;
    }
    public int numerWersjiAplikacji() {

        return BuildConfig.VERSION_CODE;
    }

    protected static Boolean czy_zalogowany() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            return false;
        else
            return true;
    }
    protected static Boolean czy_jest_przycisk = false;



    protected static String zawartoscDoWyświetlenia = Dane.główna;
    protected static Boolean czy_chcemy_Internet = true;
    public static String getZawartoscDoWyświetlenia() {

        ConnectivityManager connectivityManager = (ConnectivityManager) ta_aktywnosc.getSystemService(Context.CONNECTIVITY_SERVICE);
        if((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) ||
                !Dane.czy_chcemy_Internet) {

            //jesli to jest zakladla z internetem i jest Internet
            return zawartoscDoWyświetlenia;
        }
        else
            return "file:///android_asset/offline.html";
    }



    protected static MainActivity ta_aktywnosc;

    protected static int doWyświetlenia = R.layout.activity_main;


    protected static String email() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return user.getEmail();
        else
            return "";
    }
    protected static String nazwa() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return user.getDisplayName();
        else
            return "";
    }
    public static String etykieta() {return "Do zmiany";}

    //Rzeczy z ustawieniami
    protected static Boolean ciemny_motyw = false;


    //adresy do zakładek
    protected static final String główna = "https://dzielo.pl/";
    protected static final String o_fundacji = "file:///android_asset/o_fundacji.html";
    protected static final String nasz_patron = "file:///android_asset/nasz_patron.html";
    protected static final String dla_daroczyncy = "file:///android_asset/dla_daroczyncy.html";
    protected static final String materialy_prasowe = "https://dzielo.pl/dla-mediow/do-pobrania/";
    protected static final String kontakt = "file:///android_asset/kontakt.html";

    protected static final String formacja = "https://pl.wikipedia.org/wiki/II_wojna_karlistowska";
    protected static final String ogl_ogolne = "http://students.mimuw.edu.pl/~lk406698/FDNT/ogl_ogolne/";
    protected static String oglWspólnotowe() {

        if(Dane.etykieta().equals("warszawska"))
            return "http://students.mimuw.edu.pl/~lk406698/FDNT/ogl_wspolnotowe/";

        return "";
    }
    protected static String materiały = "http://students.mimuw.edu.pl/~lk406698/FDNT/materialy/";
    protected static String poczta = "https://login.poczta.home.pl/";

}




