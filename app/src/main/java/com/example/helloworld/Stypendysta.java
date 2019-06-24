package com.example.helloworld;

public class Stypendysta {

    public Stypendysta(String email, String haslo, String imie, String nazwisko, String etykieta) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.haslo = haslo;
        this.etykieta = etykieta;

    }
    public Stypendysta() {
        this.imie = " ";
        this.nazwisko = " ";
        this.email = " ";
        this.haslo = " ";
        this.etykieta = " ";
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
