package com.example.smarthomeapp.JSON;
import com.example.smarthomeapp.model.Enhet;
import com.example.smarthomeapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Konvertering {
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
    public List<Enhet> hentEnheter() {
        List<Enhet> enhetListe = new ArrayList<>();
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
                Enhet enhet = G.fromJson(jsonArray.get(i), Enhet.class);
                enhetListe.add(enhet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return enhetListe;
    }

    // Metode for å legge til ny enhet til JSON filen
    public void leggTilEnhet(Enhet nyEnhet) {
        try {
            List<Enhet> enhetListe = hentEnheter();
            enhetListe.add(nyEnhet);

            // Konverterer listen tilbake til en JSON array
            JsonArray oppdatertJsonArray = new JsonArray();
            for (Enhet enhet : enhetListe) {
                JsonObject enhetJson = G.toJsonTree(enhet).getAsJsonObject();
                oppdatertJsonArray.add(enhetJson);
            }

            // Skriver den oppdaterte informasjonen til JSON filen
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("enheter", oppdatertJsonArray);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data.json"))) {
                G.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
