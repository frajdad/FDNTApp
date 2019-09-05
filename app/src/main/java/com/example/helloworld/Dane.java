package com.example.helloworld;

//chcę tu trzymać jakieś globalne dane


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    protected static String emailZalogowanego() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return user.getEmail();
        else
            return "";
    }
    protected static String nazwaZalogowanego() {

         return emailZalogowanego()
                 .replace(".", "")
                 .replace("@dzielopl", "");
    }

    protected static String idZalogowanego() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return user.getUid();
        else
            return "";
    }

    private static Long uprawnienia = null;

    protected static Long getUprawnienia() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return Long.valueOf(0);

        Long value;

        try {

            value = Long.parseLong(user.getDisplayName());
            return value;
        }
        catch (NumberFormatException e)
        {
            return Long.valueOf(0);
        }
    }

    protected static void wczytajUprawnieniaZalogowanego() {

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            return;

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference upr = mRef.child("users").child(nazwaZalogowanego());


        upr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(dataSnapshot.getValue(String.class))
                        .build();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateProfile(profileUpdates);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }


    //Rzeczy z ustawieniami
    protected static Boolean ciemny_motyw = false;


    //adresy do zakładek
    private static final String główna = "https://dzielo.pl/";
    private static final String o_fundacji = "file:///android_asset/o_fundacji.html";
    private static final String nasz_patron = "file:///android_asset/nasz_patron.html";
    private static final String dla_daroczyncy = "file:///android_asset/dla_daroczyncy.html";
    private static final String materialy_prasowe = "file:///android_asset/materialy_prasowe.html";
    private static final String kontakt = "file:///android_asset/kontakt.html";
    private static final String formacja = "https://pl.wikipedia.org/wiki/II_wojna_karlistowska";
    private static final String ogl_ogolne = "http://students.mimuw.edu.pl/~lk406698/FDNT/ogl_ogolne/";
    private static final String wsp_warszawska = "https://sites.google.com/view/ogloszenia-wspolnotowe";
    private static final String warszawscy_pierwszoroczni = "https://sites.google.com/view/fdnt-strona-testowa";
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
        return "";
    }
    protected static String wsp_warszawska() {
        return wsp_warszawska;
    }
    protected static String warszawscy_pierwszoroczni() {
        return warszawscy_pierwszoroczni;
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
}




