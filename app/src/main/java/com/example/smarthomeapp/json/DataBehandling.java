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
    public List<WiFiEnhet> hentWiFiEnhetGruppe(WiFiEnhet enhet, String gruppe) {
        List<Object> hentetListe = dataKonvertering.hentFraJson("WiFi enheter", WiFiEnhet.class, "Data.json");
        List<WiFiEnhet> stedListe = new ArrayList<>();
        List<WiFiEnhet> funksjonListe = new ArrayList<>();

        if (Objects.equals(gruppe, "sted")) {
            for (Object objekt : hentetListe) {
                WiFiEnhet enhetObjekt = (WiFiEnhet) objekt;
                // Hvis området stemmer så legges enheten til listen
                // getEnhetSted() finnes i annen branch
                if (Objects.equals(enhetObjekt.getEnhetSted(), gruppe)) {
                    stedListe.add(enhetObjekt);
                }
            }
            return stedListe;
        } else if (Objects.equals(gruppe, "funksjon")) {
            for (Object objekt : hentetListe) {
                WiFiEnhet enhetObjekt = (WiFiEnhet) objekt;
                // Hvis funksjon stemmer så legges enheten til listen
                // getEnhetFunksjon() finnes i annen branch
                if (Objects.equals(enhetObjekt.getEnhetFunksjon(), gruppe)) {
                    funksjonListe.add(enhetObjekt);
                }
            }
            return funksjonListe;
        } else {
            System.out.println("Feil: mulige parametere er 'sted' eller 'funksjon'");
        }
        return java.util.Collections.emptyList();
    }
    
    // Metode for å endre enhet/bruker verdier i JSON
    public <T> void endreJson(Object endreObjekt, String kategori, Class<T> klasseNavn, String endreHva, String endreTil, String filnavn) {
        List<Object> hentetListe = dataKonvertering.hentFraJson(kategori, klasseNavn, filnavn);

        // Endrer informasjon i JSON
        if (Objects.equals(kategori, "brukere")) {
            for (Object bruker : hentetListe) {

                // Finner ut om 'endreHva' finnes i noen av feltene
                // Hvis det finnes, endre verdien til 'endreTil'
                User endreBruker = (User) bruker;
                if (Objects.equals(endreBruker.getAdresse(), endreHva)) {
                    endreBruker.setAdresse(endreTil);
                } else if (Objects.equals(endreBruker.getBrukernavn(), endreHva)) {
                    endreBruker.setBrukernavn(endreTil);
                } else if (Objects.equals(endreBruker.getEmail(), endreHva)) {
                    endreBruker.setEmail(endreTil);
                } else if (Objects.equals(endreBruker.getFodselsdato(), endreHva)) {
                    endreBruker.setFodselsdato(endreTil);
                } else if (Objects.equals(endreBruker.getPassord(), endreHva)) {
                    endreBruker.setPassord(endreTil);
                } else if (Objects.equals(endreBruker.getRettigheter(), endreHva)) {
                    endreBruker.setRettigheter(endreTil);
                } else if (String.valueOf(endreBruker.getTelefon()).equals(endreHva)) {
                    endreBruker.setTelefon(Integer.parseInt(endreTil));
                } else {
                    System.out.println("Feil: " + endreHva + " finnes ikke i bruker kategorien");
                }
            }
        } else if (Objects.equals(kategori, "WiFi enheter")) {
            for (Object enhet : hentetListe) {

                // Metodene getWiFiEnhetFunksjon og getWiFiEnhetSted er i en annen branch
                // Metodene setWiFiEnhetFunksjon og setWiFiEnhetSted må lages i en annen branch
                WiFiEnhet endreWiFiEnhet = (WiFiEnhet) enhet;
                if (Objects.equals(endreWiFiEnhet.getEnhetNavn(), endreHva)) {
                    endreWiFiEnhet.setEnhetNavn(endreTil);
                } else if (Objects.equals(endreWiFiEnhet.getEnhetSted(), endreHva)) {
                    endreWiFiEnhet.setEnhetProtokoll(endreTil);
                } else if (Objects.equals(endreWiFiEnhet.getEnhetSted(), endreHva)) {
                    endreWiFiEnhet.setEnhetIp(endreTil);
                } else if (Objects.equals(endreWiFiEnhet.getEnhetSted(), endreHva)) {
                    endreWiFiEnhet.setEnhetPort(endreTil);
                } else if (Objects.equals(endreWiFiEnhet.getEnhetFunksjon(), endreHva)) {
                    endreWiFiEnhet.setEnhetFunksjon(endreTil);
                } else if (Objects.equals(endreWiFiEnhet.getEnhetSted(), endreHva)) {
                    endreWiFiEnhet.setEnhetSted(endreTil);
                } else {
                    System.out.println("Feil: " + endreHva + " finnes ikke i WiFi enheter kategorien");
                }
            }
        } else if (Objects.equals(kategori, "Bluetooth enheter")) {
            for (Object enhet : hentetListe) {

                BluetoothEnhet endreBluetoothEnhet = (BluetoothEnhet) enhet;
                if (Objects.equals(endreBluetoothEnhet.getEnhetNavn(), endreHva)) {
                    endreBluetoothEnhet.setEnhetNavn(endreTil);
                } else {
                    System.out.println("Feil: " + endreHva + " finnes ikke i WiFi enheter kategorien");
                }
            }
        }
        // Konverterer objekt til json
        dataKonvertering.objektTilJson(hentetListe, kategori, "Data.json");
    }
    
    // Metode for å slette fra et kategori i JSON
    public <T> void slettFraJson(Object objektForSletting, String kategori, Class<T> klasseNavn, String filnavn) {
        List<Object> hentetListe = dataKonvertering.hentFraJson(kategori, klasseNavn, filnavn);

        // Sletter et objekt fra listen
        hentetListe.remove(objektForSletting);

        // Konverterer objekt til json
        dataKonvertering.objektTilJson(hentetListe, kategori, filnavn);
    }
}
