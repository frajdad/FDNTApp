package com.example.helloworld;

public class Stypendysta {

    public Stypendysta(String imie, String nazwisko, String email, String haslo) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.haslo = haslo;
    }
    public Stypendysta() {
        this.imie = " ";
        this.nazwisko = " ";
        this.email = " ";
        this.haslo = " ";
    }

    private String imie;
    private String nazwisko;
    private String email;
    private String etykieta;
    private String haslo;

    public String imie() {
        return this.imie;
    }
    public String nazwisko() {
        return this.nazwisko;
    }
    public String email() {
        return this.email;
    }
    public String etykieta() { return this.etykieta; }

    public Boolean weryfikuj(String haslo)
    {
        if(haslo.equals(this.haslo))
            return true;
        else
            return false;
    }

}
