package com.example.smarthomeapp.json;
import com.example.smarthomeapp.model.WiFiEnhet;
import com.example.smarthomeapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class DataKonvertering {
    private static Gson G = new GsonBuilder().setPrettyPrinting().create();

    // Metode som leser brukere fra JSON og returnerer en liste med bruker objekter
    public List<User> hentBrukere() {
        List<User> brukereList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Data.json"))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String linje;
            while ((linje = br.readLine()) != null) {
                jsonStringBuilder.append(linje);
            }
            String jsonString = jsonStringBuilder.toString();

            // Konverterer tekst hentet fra JSON filen til et JSON objekt
            // og bruker array blir laget fra JSON objektet
            JsonObject jsonObject = G.fromJson(jsonString, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("brukere");

            // Konverterer hvert element i bruker array-en til et bruker objekt
            // dvs. strukturen som er definert i User class
            for (int i = 0; i < jsonArray.size(); i++) {
                User bruker = G.fromJson(jsonArray.get(i), User.class);
                brukereList.add(bruker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return brukereList;
    }

    // Metode for å legge til ny bruker til JSON filen
    public void leggTilBruker(User nyBruker) {
        try {
            List<User> brukereList = hentBrukere();

            // Hash passordet
            String hashedPassword = BCrypt.hashpw(nyBruker.getPassord(), BCrypt.gensalt());
            nyBruker.setPassord(hashedPassword); // linket med setPassord-metoden er lagt til i User.java

            brukereList.add(nyBruker);

            // Konverterer listen tilbake til en JSON array
            JsonArray oppdatertJsonArray = new JsonArray();
            for (User bruker : brukereList) {
                JsonObject brukerJson = G.toJsonTree(bruker).getAsJsonObject();
                oppdatertJsonArray.add(brukerJson);
            }

            // Skriver den oppdaterte informasjonen til JSON filen
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("brukere", oppdatertJsonArray);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data.json"))) {
                G.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metode som leser enheter fra JSON og returnerer en liste med enhet objekter
    public List<WiFiEnhet> hentEnheter() {
        List<WiFiEnhet> wiFiEnhetListe = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Data.json"))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String linje;
            while ((linje = br.readLine()) != null) {
                jsonStringBuilder.append(linje);
            }
            String jsonString = jsonStringBuilder.toString();

            // Konverterer tekst hentet fra JSON filen til et JSON objekt
            // og bruker array blir laget fra JSON objektet
            JsonObject jsonObject = G.fromJson(jsonString, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("enheter");

            // Konverterer hvert element i bruker array-en til et enhet objekt
            // dvs. strukturen som er definert i Enhet class
            for (int i = 0; i < jsonArray.size(); i++) {
                WiFiEnhet wiFiEnhet = G.fromJson(jsonArray.get(i), WiFiEnhet.class);
                wiFiEnhetListe.add(wiFiEnhet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wiFiEnhetListe;
    }

    // Metode for å legge til ny WiFi enhet til JSON filen
    public void leggTilWiFiEnhet(WiFiEnhet nyWiFiEnhet) {
        try {
            List<WiFiEnhet> wiFiEnhetListe = hentEnheter();
            wiFiEnhetListe.add(nyWiFiEnhet);

            // Konverterer listen tilbake til en JSON array
            JsonArray oppdatertJsonArray = new JsonArray();
            for (WiFiEnhet wiFiEnhet : wiFiEnhetListe) {
                JsonObject enhetJson = G.toJsonTree(wiFiEnhet).getAsJsonObject();
                oppdatertJsonArray.add(enhetJson);
            }

            // Skriver den oppdaterte informasjonen til JSON filen
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("WiFi enheter", oppdatertJsonArray);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data.json"))) {
                G.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
