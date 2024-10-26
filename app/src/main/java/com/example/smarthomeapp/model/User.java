package com.example.smarthomeapp;

// Klasse for å lage bruker
public class User {
    private int brukerID;
    private String brukernavn;
    private String passord;
    private String fodselsdato;
    private String rettigheter;
    private String adresse;
    private String email;
    private int telefon;

    public User(int brukerID, String brukernavn, String passord, String fodselsdato, String rettigheter, String adresse, String email, int telefon) {
        this.brukerID = brukerID;
        this.brukernavn = brukernavn;
        this.passord = passord;
        this.fodselsdato = fodselsdato;
        this.rettigheter = rettigheter;
        this.adresse = adresse;
        this.email = email;
        this.telefon = telefon;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public String getPassord() {
        return passord;
    }
}
