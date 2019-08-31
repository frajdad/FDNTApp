package com.example.helloworld;

//chcę tu trzymać jakieś globalne dane


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.mail.Session;

public class Dane {



    protected static Boolean czy_zalogowany() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            return false;
        else
            return true;
    }
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

    //Aktualnie zalogowany użytkownik
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
    //W tej wersji przechowujemy etykietę użytkownika i jego nazwie. Jest to nieco dziwne,
    //ale na razie nie potrzebujmy nazwy, a jest to oszczędąść pracy programisty,
    //bo dzięki temu nie bawimy się w dodatkową bazę danych
    protected static Long etykieta() {

        String etykieta = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return Long.parseLong(etykieta);
    }

    //Rzeczy z ustawieniami
    protected static Boolean ciemny_motyw = false;


    //adresy do zakładek
    private static final String główna = "https://dzielo.pl/";
    private static final String o_fundacji = "file:///android_asset/o_fundacji.html";
    private static final String nasz_patron = "file:///android_asset/nasz_patron.html";
    private static final String dla_daroczyncy = "file:///android_asset/dla_daroczyncy.html";
    private static final String materialy_prasowe = "https://dzielo.pl/dla-mediow/do-pobrania/";
    private static final String kontakt = "file:///android_asset/kontakt.html";
    private static final String formacja = "https://pl.wikipedia.org/wiki/II_wojna_karlistowska";
    private static final String ogl_ogolne = "http://students.mimuw.edu.pl/~lk406698/FDNT/ogl_ogolne/";
    private static final String komunikator = "";
    private static final String materiały = "http://students.mimuw.edu.pl/~lk406698/FDNT/materialy/";
    private static final String poczta = "https://login.poczta.home.pl/";

    protected static String główna() {
        return główna;
    }
    protected static String o_fundacji() {
        return o_fundacji;
    }
    protected static String nasz_patron() {
        return nasz_patron;
    }
    protected static String dla_daroczyncy() {
        return dla_daroczyncy;
    }
    protected static String materialy_prasowe() {
        return materialy_prasowe;
    }
    protected static String kontakt() {
        return kontakt;
    }
    protected static String formacja() {
        return formacja;
    }
    protected static String ogl_ogolne() {
        return ogl_ogolne;
    }
    protected static String oglWspólnotowe() {
        return "https://sites.google.com/view/fdnt-strona-testowa";
    }
    protected static String komunikator() {
        return komunikator;
    }
    protected static String materialy() {
        return materiały;
    }
    protected static String poczta() {
        return poczta;
    }



    //Wersja aplikacji
    public String nazwaWersjiAplikacji() {

        return BuildConfig.VERSION_NAME;
    }
    public int numerWersjiAplikacji() {

        return BuildConfig.VERSION_CODE;
    }

    //Sesja email
    protected static Session session;
    protected static String emailAddress;
}




