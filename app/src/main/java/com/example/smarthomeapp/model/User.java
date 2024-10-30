package com.example.smarthomeapp.model;

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

    public int getBrukerID() {
        return brukerID;
    }

    public void setBrukerID(int brukerID) {
        this.brukerID = brukerID;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public String getFodselsdato() {
        return fodselsdato;
    }

    public void setFodselsdato(String fodselsdato) {
        this.fodselsdato = fodselsdato;
    }

    public String getRettigheter() {
        return rettigheter;
    }

    public void setRettigheter(String rettigheter) {
        this.rettigheter = rettigheter;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefon() {
        return telefon;
    }

    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }
}
