package com.example.helloworld;

//chcę tu trzymać jakieś globalne dane, głównie związane z aktualnie zalogowaną osobą

import android.content.Intent;

public class Dane {

    protected static Stypendysta zalogowany = new Stypendysta();

    protected static Boolean czy_zalogowany = false;


    public static String imie() {return zalogowany.imie();}
    public static String nazwisko() {return zalogowany.nazwisko();}
    public static String email() {return  zalogowany.email();}
    public static String etykieta() {return zalogowany.etykieta();}
}




