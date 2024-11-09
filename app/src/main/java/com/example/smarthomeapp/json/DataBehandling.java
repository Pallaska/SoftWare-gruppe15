package com.example.smarthomeapp.json;
import com.example.smarthomeapp.json.konvertering.DataKonvertering;
import com.example.smarthomeapp.model.Enhet;
import com.example.smarthomeapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBehandling {
    DataKonvertering dataKonvertering = new DataKonvertering();
    Gson G = new GsonBuilder().setPrettyPrinting().create();

    // Metode som returnerer en liste med enheter basert på område eller funksjon
    public List<Enhet> hentEnhetGruppe(Enhet enhet, String gruppe) {
        try {
            List<Object> hentetListe = dataKonvertering.hentFraJson("enheter", Enhet.class);
            List<Enhet> stedListe = new ArrayList<>();
            List<Enhet> funksjonListe = new ArrayList<>();

            if (Objects.equals(gruppe, "sted")) {
                for (Object objekt : hentetListe) {
                    Enhet enhetObjekt = (Enhet) objekt;
                    // Hvis området stemmer så legges enheten til listen
                    // getSted() finnes i annen branch
                    if (Objects.equals(enhetObjekt.getSted(), gruppe)) {
                        stedListe.add(enhetObjekt);
                    }
                }
                return stedListe;
            } else if (Objects.equals(gruppe, "funksjon")) {
                for (Object objekt : hentetListe) {
                    Enhet enhetObjekt = (Enhet) objekt;
                    // Hvis funksjon stemmer så legges enheten til listen
                    // getFunksjon() finnes i annen branch
                    if (Objects.equals(enhetObjekt.getFunksjon(), gruppe)) {
                        funksjonListe.add(enhetObjekt);
                    }
                }
                return funksjonListe;
            } else {
                System.out.println("Feil: mulige parametere er 'sted' eller 'funksjon'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return java.util.Collections.emptyList();
    }
    
    // Metode for å endre enhet/bruker verdier i JSON
    public <T> void endreJson(Object endreObjekt, String kategori, Class<T> klasseNavn, String endreHva, String endreTil) {
        try {
            List<Object> hentetListe = dataKonvertering.hentFraJson(kategori, klasseNavn);

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
            } else if (Objects.equals(kategori, "enheter")) {
                for (Object enhet : hentetListe) {

                    // Metodene getEnhetFunksjon og getEnhetSted er i en annen branch
                    // Metodene setEnhetFunksjon og setEnhetSted må lages i en annen branch
                    Enhet endreEnhet = (Enhet) enhet;
                    if (Objects.equals(endreEnhet.getEnhetNavn(), endreHva)) {
                        endreEnhet.setEnhetNavn(endreTil);
                    } else if (Objects.equals(endreEnhet.getEnhetFunksjon(), endreHva)) {
                        endreEnhet.setEnhetFunksjon(endreTil);
                    } else if (Objects.equals(endreEnhet.getEnhetSted(), endreHva)) {
                        endreEnhet.setEnhetSted(endreTil);
                    } else {
                        System.out.println("Feil: " + endreHva + " finnes ikke i enhet kategorien");
                    }
                }
            }

            // Konverterer listen tilbake til en JSON array
            JsonArray oppdatertJsonArray = new JsonArray();
            for (Object hentet : hentetListe) {
                JsonObject brukerJson = G.toJsonTree(hentet).getAsJsonObject();
                oppdatertJsonArray.add(brukerJson);
            }

            // Skriver den oppdaterte informasjonen til JSON filen
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(kategori, oppdatertJsonArray);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data.json"))) {
                G.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Metode for å slette fra et kategori i JSON
    public <T> void slettFraJson(Object objektForSletting, String kategori, Class<T> klasseNavn) {
        try {
            List<Object> hentetListe = dataKonvertering.hentFraJson(kategori, klasseNavn);

            // Sletter et objekt fra listen
            hentetListe.remove(objektForSletting);

            // Konverterer listen tilbake til en JSON array
            JsonArray oppdatertJsonArray = new JsonArray();
            for (Object hentet : hentetListe) {
                JsonObject brukerJson = G.toJsonTree(hentet).getAsJsonObject();
                oppdatertJsonArray.add(brukerJson);
            }

            // Skriver den oppdaterte informasjonen til JSON filen
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(kategori, oppdatertJsonArray);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data.json"))) {
                G.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
