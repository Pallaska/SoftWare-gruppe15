package com.example.smarthomeapp.model;
import com.google.gson.annotations.SerializedName;

public class Enhet {
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

    public Enhet(int enhetID, String enhetNavn, String enhetFunksjon, String enhetSted) {
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

    @Override
    public String toString() {
        return "Enhet{" + "enhetID=" + enhetID + ", enhetNavn=" + enhetNavn + ", enhetFunksjon=" + enhetFunksjon + ", enhetSted=" + enhetSted + '}';
    }
}
