package com.example.smarthomeapp.model;
import com.google.gson.annotations.SerializedName;

public class Enhet {
    // @SerializedName er der i tilfelle det er forskjellig
    // format mellom Java variablene og det som er i JSON
    @SerializedName("brukerID")
    private int enhetID;
    @SerializedName("brukernavn")
    private String enhetNavn;

    public Enhet(int enhetID, String enhet) {
        this.enhetID = enhetID;
        this.enhetNavn = enhetNavn;
    }

    public int getEnhetID() {
        return enhetID;
    }
    public String getEnhetNavn() {
        return enhetNavn;
    }

    @Override
    public String toString() {
        return "Enhet{" + "enhetID=" + enhetID + ", enhetNavn=" + enhetNavn + '}';
    }
}
