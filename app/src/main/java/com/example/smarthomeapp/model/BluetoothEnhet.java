package com.example.smarthomeapp.model;
import com.google.gson.annotations.SerializedName;

public class BluetoothEnhet {
    // @SerializedName er der i tilfelle det er forskjellig
    // format mellom Java variablene og det som er i JSON
    @SerializedName("enhetID")
    private int enhetID;
    @SerializedName("enhetNavn")
    private String enhetNavn;
    @SerializedName("enhetFunksjon")
    private String enhetFunksjon;
    @SerializedName("enhetSted")
    private String enhetSted;

    public BluetoothEnhet(int enhetID, String enhetNavn, String enhetFunksjon, String enhetSted) {
        this.enhetID = enhetID;
        this.enhetNavn = enhetNavn;
        this.enhetFunksjon = enhetFunksjon;
        this.enhetSted = enhetSted;
    }

    public int getEnhetID() {
        return enhetID;
    }
    public String getEnhetNavn() {
        return enhetNavn;
    }
    public String getEnhetFunksjon() { return enhetFunksjon; }
    public String getEnhetSted() { return enhetSted; }
    public void setEnhetNavn(String nyEnhetNavn) {
        this.enhetNavn = nyEnhetNavn;
    }
    public void setEnhetFunksjon(String nyEnhetFunksjon) { this.enhetFunksjon = nyEnhetFunksjon; }
    public void setEnhetSted(String nyEnhetSted) { this.enhetSted = nyEnhetSted; }

    @Override
    public String toString() {
        return "Bluetooth enheter{" + "enhetID=" + enhetID + ", enhetNavn=" + enhetNavn +
                ", enhetFunksjon=" + enhetFunksjon + ", enhetSted=" + enhetSted + '}';
    }
}
