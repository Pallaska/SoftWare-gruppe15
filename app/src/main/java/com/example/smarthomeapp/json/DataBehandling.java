package com.example.smarthomeapp.json;

import com.example.smarthomeapp.model.BluetoothEnhet;
import com.example.smarthomeapp.model.WiFiEnhet;
import com.example.smarthomeapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBehandling {
    DataKonvertering dataKonvertering = new DataKonvertering();
    Gson G = new GsonBuilder().setPrettyPrinting().create();

    // Metode som returnerer en liste med enheter basert på område eller funksjon
    public List<WiFiEnhet> hentWiFiEnhetGruppe(String gruppe, String verdi) {
        List<WiFiEnhet> hentetListe = dataKonvertering.hentFraJson("WiFi enheter", WiFiEnhet.class, "Data.json");
        List<WiFiEnhet> resultatListe = new ArrayList<>();

        if (Objects.equals(gruppe, "sted")) {
            for (WiFiEnhet enhetObjekt : hentetListe) {
                if (Objects.equals(enhetObjekt.getEnhetSted(), verdi)) {
                    resultatListe.add(enhetObjekt);
                }
            }
        } else if (Objects.equals(gruppe, "funksjon")) {
            for (WiFiEnhet enhetObjekt : hentetListe) {
                if (Objects.equals(enhetObjekt.getEnhetFunksjon(), verdi)) {
                    resultatListe.add(enhetObjekt);
                }
            }
        } else {
            System.out.println("Feil: mulige parametere er 'sted' eller 'funksjon'");
        }
        return resultatListe;
    }

    // Metode for å endre enhet/bruker verdier i JSON
    public <T> void endreJson(String kategori, Class<T> klasseNavn, String endreHva, String endreTil, String filnavn) {
        List<T> hentetListe = dataKonvertering.hentFraJson(kategori, klasseNavn, filnavn);

        // Endrer informasjon i JSON
        if (Objects.equals(kategori, "brukere")) {
            for (T objekt : hentetListe) {
                User endreBruker = (User) objekt;
                boolean funnet = false;
                if (Objects.equals(endreBruker.getAdresse(), endreHva)) {
                    endreBruker.setAdresse(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreBruker.getBrukernavn(), endreHva)) {
                    endreBruker.setBrukernavn(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreBruker.getEmail(), endreHva)) {
                    endreBruker.setEmail(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreBruker.getFodselsdato(), endreHva)) {
                    endreBruker.setFodselsdato(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreBruker.getPassord(), endreHva)) {
                    endreBruker.setPassord(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreBruker.getRettigheter(), endreHva)) {
                    endreBruker.setRettigheter(endreTil);
                    funnet = true;
                } else if (String.valueOf(endreBruker.getTelefon()).equals(endreHva)) {
                    endreBruker.setTelefon(Integer.parseInt(endreTil));
                    funnet = true;
                }
                if (!funnet) {
                    System.out.println("Feil: " + endreHva + " finnes ikke i bruker kategorien");
                }
            }
        } else if (Objects.equals(kategori, "WiFi enheter")) {
            for (T objekt : hentetListe) {
                WiFiEnhet endreWiFiEnhet = (WiFiEnhet) objekt;
                boolean funnet = false;
                if (Objects.equals(endreWiFiEnhet.getEnhetNavn(), endreHva)) {
                    endreWiFiEnhet.setEnhetNavn(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreWiFiEnhet.getEnhetProtokoll(), endreHva)) {
                    endreWiFiEnhet.setEnhetProtokoll(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreWiFiEnhet.getEnhetIp(), endreHva)) {
                    endreWiFiEnhet.setEnhetIp(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreWiFiEnhet.getEnhetPort(), endreHva)) {
                    endreWiFiEnhet.setEnhetPort(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreWiFiEnhet.getEnhetFunksjon(), endreHva)) {
                    endreWiFiEnhet.setEnhetFunksjon(endreTil);
                    funnet = true;
                } else if (Objects.equals(endreWiFiEnhet.getEnhetSted(), endreHva)) {
                    endreWiFiEnhet.setEnhetSted(endreTil);
                    funnet = true;
                }
                if (!funnet) {
                    System.out.println("Feil: " + endreHva + " finnes ikke i WiFi enheter kategorien");
                }
            }
        } else if (Objects.equals(kategori, "Bluetooth enheter")) {
            for (T objekt : hentetListe) {
                BluetoothEnhet endreBluetoothEnhet = (BluetoothEnhet) objekt;
                boolean funnet = false;
                if (Objects.equals(endreBluetoothEnhet.getEnhetNavn(), endreHva)) {
                    endreBluetoothEnhet.setEnhetNavn(endreTil);
                    funnet = true;
                }
                if (!funnet) {
                    System.out.println("Feil: " + endreHva + " finnes ikke i Bluetooth enheter kategorien");
                }
            }
        }
        // Konverterer objekt til json
        dataKonvertering.objektTilJson(hentetListe, kategori, filnavn);
    }

    // Metode for å slette fra et kategori i JSON
    public <T> void slettFraJson(T objektForSletting, String kategori, Class<T> klasseNavn, String filnavn) {
        List<T> hentetListe = dataKonvertering.hentFraJson(kategori, klasseNavn, filnavn);

        // Sletter et objekt fra listen
        hentetListe.remove(objektForSletting);

        // Konverterer objekt til json
        dataKonvertering.objektTilJson(hentetListe, kategori, filnavn);
    }
}
