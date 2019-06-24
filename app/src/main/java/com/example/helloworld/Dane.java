package com.example.helloworld;

//chcę tu trzymać jakieś globalne dane, głównie związane z aktualnie zalogowaną osobą

import android.content.Intent;

public class Dane {

    private final static String wczytywanie = "Brak Danych";

    protected static Stypendysta zalogowany = new Stypendysta();

    protected static Boolean czy_zalogowany = false;

    protected static Boolean zakładka_ze_zmiennym_tekstem = false;
    protected static String tekstDoWyświetlenia = wczytywanie;
    protected static String materiałyPrasowe = wczytywanie;
    protected static String formacja = wczytywanie;
    protected static String oglOgólne = wczytywanie;
    protected static String oglWspólnotowe = wczytywanie;
    protected static String materiały = wczytywanie;



    protected static MainActivity ta_aktywnosc;

    protected static int doWyświetlenia = R.layout.activity_main;


    public static String imie() {return zalogowany.imie();}
    public static String nazwisko() {return zalogowany.nazwisko();}
    public static String email() {return  zalogowany.email();}
    public static String etykieta() {return zalogowany.etykieta();}


    protected static final String kontakt ="Telefon: +48 22 530 48 28\n\nEmail: dzielo@episkopat.pl";

}




