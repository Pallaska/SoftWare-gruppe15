package com.example.smarthomeapp.model;
import com.google.gson.annotations.SerializedName;

public class BluetoothEnhet {
    // @SerializedName er der i tilfelle det er forskjellig
    // format mellom Java variablene og det som er i JSON
    @SerializedName("enhetID")
    private int enhetID;
    @SerializedName("enhetNavn")
    private String enhetNavn;

    public BluetoothEnhet(int enhetID, String enhetNavn) {
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
        return "BluetoothEnhet{" + "enhetID=" + enhetID + ", enhetNavn=" + enhetNavn + '}';
    }
}
