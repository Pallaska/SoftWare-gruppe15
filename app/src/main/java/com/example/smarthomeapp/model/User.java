package com.example.smarthomeapp.model;
import com.google.gson.annotations.SerializedName;

public class User {
    // @SerializedName er der i tilfelle det er forskjellig
    // format mellom Java variablene og det som er i JSON
    @SerializedName("brukerID")
    private int brukerID;
    @SerializedName("brukernavn")
    private String brukernavn;
    @SerializedName("passord")
    private String passord;
    @SerializedName("fodselsdato")
    private String fodselsdato;
    @SerializedName("rettigheter")
    private String rettigheter;
    @SerializedName("email")
    private String email;
    @SerializedName("adresse")
    private String adresse;
    @SerializedName("telefon")
    private int telefon;

    public User(int brukerID, String brukernavn, String passord, String fodselsdato, String rettigheter, String email, String adresse, int telefon) {
        this.brukerID = brukerID;
        this.brukernavn = brukernavn;
        this.passord = passord;
        this.fodselsdato = fodselsdato;
        this.rettigheter = rettigheter;
        this.email = email;
        this.adresse = adresse;
        this.telefon = telefon;
    }

    public int getBrukerID() {
        return brukerID;
    }
    public String getBrukernavn() {
        return brukernavn;
    }
    public String getPassord() {
        return passord;
    }
    public String getFodselsdato() {
        return fodselsdato;
    }
    public String getRettigheter() {
        return rettigheter;
    }
    public String getEmail() {
        return email;
    }
    public String getAdresse() {
        return adresse;
    }
    public int getTelefon() {
        return telefon;
    }

    // Metodene som brukes av DataBehandling.endreJson()
    public void setBrukernavn(String nyBrukernavn) {
        this.brukernavn = nyBrukernavn;
    }
    public void setFodselsdato(String nyFodselsdato) {
        this.fodselsdato = nyFodselsdato;
    }
    public void setRettigheter(String nyRettigheter) {
        this.rettigheter = nyRettigheter;
    }
    public void setEmail(String nyEmail) {
        this.email = nyEmail;
    }
    public void setAdresse(String nyAdresse) {
        this.adresse = nyAdresse;
    }
    public void setTelefon(int nyTelefon) {
        this.telefon = nyTelefon;
    }

    // for å hashet passordet
    public void setPassord(String passord) {
        this.passord = passord;
    }

    @Override
    public String toString() {
        return "Bruker{" +
                "brukerID=" + brukerID +
                ", brukernavn='" + brukernavn + '\'' +
                ", passord='" + passord + '\'' +
                ", fodselsdato='" + fodselsdato + '\'' +
                ", rettigheter='" + rettigheter + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + email + '\'' +
                ", telefon=" + telefon +
                '}';
    }
}
