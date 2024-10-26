package com.example.smarthomeapp.integrering;
import java.util.ArrayList;
import java.util.List;

public class Enhetsstyring {
    private List<SmartEnhet> enhetListe;

    public Enhetsstyring() {
        enhetListe = new ArrayList<>();
    }

    public void leggTilEnheter(SmartEnhet enhet) {
        enhetListe.add(enhet);
    }

    public List<SmartEnhet> hentEnheter() {
        return enhetListe;
    }

    public void kobleTilEnhet(int enhetNr) {
        SmartEnhet enhet = enhetListe.get(enhetNr);
        enhet.connect();
    }

    public void sendKommandoTilEnhet(int enhetNr, String kommando) {
        SmartEnhet enhet = enhetListe.get(enhetNr);
        enhet.sendCommand(kommando);
    }
}