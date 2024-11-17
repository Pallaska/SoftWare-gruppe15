package com.example.smarthomeapp.model;
import com.google.gson.annotations.SerializedName;

public class WiFiEnhet {
    // @SerializedName er der i tilfelle det er forskjellig
    // format mellom Java variablene og det som er i JSON
    @SerializedName("enhetID")
    private int enhetID;
    @SerializedName("enhetNavn")
    private String enhetNavn;
    @SerializedName("enhetProtokoll")
    private String enhetProtokoll;
    @SerializedName("enhetIp")
    private String enhetIp;
    @SerializedName("enhetPort")
    private String enhetPort;
    @SerializedName("enhetFunksjon")
    private String enhetFunksjon;
    @SerializedName("enhetSted")
    private String enhetSted;

    public WiFiEnhet(int enhetID, String enhetNavn, String enhetProtokoll,
                     String enhetIp, String enhetPort, String enhetFunksjon, String enhetSted) {
        this.enhetID = enhetID;
        this.enhetNavn = enhetNavn;
        this.enhetProtokoll = enhetProtokoll;
        this.enhetIp = enhetIp;
        this.enhetPort = enhetPort;
        this.enhetFunksjon = enhetFunksjon;
        this.enhetSted = enhetSted;
    }

    public int getEnhetID() {
        return enhetID;
    }
    public String getEnhetNavn() {
        return enhetNavn;
    }
    public String getEnhetProtokoll() { return enhetProtokoll; }
    public String getEnhetIp() { return enhetIp; }
    public String getEnhetPort() { return enhetPort; }
    public String getEnhetFunksjon() { return enhetFunksjon; }
    public String getEnhetSted() { return enhetSted; }

    public void setEnhetID(int enhetID) {
        this.enhetID = enhetID;
    }
    public void setEnhetNavn(String enhetNavn) {
        this.enhetNavn = enhetNavn;
    }
    public void setEnhetProtokoll(String enhetProtokoll) {
        this.enhetProtokoll = enhetProtokoll;
    }
    public void setEnhetIp(String enhetIp) {
        this.enhetIp = enhetIp;
    }
    public void setEnhetPort(String enhetPort) {
        this.enhetPort = enhetPort;
    }
    public void setEnhetFunksjon(String enhetFunksjon) {
        this.enhetFunksjon = enhetFunksjon;
    }
    public void setEnhetSted(String enhetSted) {
        this.enhetSted = enhetSted;
    }

    @Override
    public String toString() {
        return "WiFiEnhet{" + "enhetID=" + enhetID + ", enhetNavn=" + enhetNavn + ", enhetProtokoll=" + enhetProtokoll +
                ", enhetIp=" + enhetIp + ", enhetPort=" + enhetPort + ", enhetFunksjon=" + enhetFunksjon + ", enhetSted=" + enhetSted + '}';
    }
}
