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
    @SerializedName("bilde")
    private String bilde;

    public User(int brukerID, String brukernavn, String passord, String fodselsdato, String rettigheter, String email, String adresse, int telefon, String bilde) {
        this.brukerID = brukerID;
        this.brukernavn = brukernavn;
        this.passord = passord;
        this.fodselsdato = fodselsdato;
        this.rettigheter = rettigheter;
        this.email = email;
        this.adresse = adresse;
        this.telefon = telefon;
        this.bilde = bilde;
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
    public String getBilde() { return bilde; }

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
                ", telefon=" + telefon + '\'' +
                ", bilde=" + bilde +
                '}';
    }
}
