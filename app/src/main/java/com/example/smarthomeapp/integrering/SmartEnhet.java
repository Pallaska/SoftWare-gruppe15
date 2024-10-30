package com.example.smarthomeapp.integrering;

// En generell klasse for alle enheter uansett type, der navn bare er navn
// og adresse kan være MAC adresse for enheter som bruker Bluetooth, eller
// IP adresse for enheter som bruker WiFi
public abstract class SmartEnhet {
    protected String navn;
    protected String adresse;

    public SmartEnhet(String navn, String adresse) {
        this.navn = navn;
        this.adresse = adresse;
    }

    // Abstrakte metoder som skal implementeres av underklassene
    // BluetoothEnhet og WiFiEnhet
    public abstract void connect();
    public abstract void disconnect();
    public abstract void sendCommand(String kommando);

    public String hentNavn() { return navn; }
    public String hentAdresse() { return adresse; }
}