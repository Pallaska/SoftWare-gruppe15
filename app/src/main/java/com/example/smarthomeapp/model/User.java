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
    public String getBilde() { return bilde; }

    public void setBrukerID(int brukerID) {
        this.brukerID = brukerID;
    }
    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }
    public void setPassord(String passord) {
        this.passord = passord;
    }
    public void setFodselsdato(String fodselsdato) {
        this.fodselsdato = fodselsdato;
    }
    public void setRettigheter(String rettigheter) {
        this.rettigheter = rettigheter;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }

    public void setBilde(String bilde) { this.bilde = bilde; }

    @Override
    public String toString() {
        return "bruker{" +
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
