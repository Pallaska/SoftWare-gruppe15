package com.example.smarthomeapp.json.konvertering;
import com.example.smarthomeapp.model.Handling;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoggKonvertering {
    private static Gson G = new GsonBuilder().setPrettyPrinting().create();

    // Metode som leser handlinger fra JSON og returnerer en liste med handling objekter
    public List<Handling> hentHandlinger() {
        List<Handling> handlingListe = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Logg.json"))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String linje;
            while ((linje = br.readLine()) != null) {
                jsonStringBuilder.append(linje);
            }
            String jsonString = jsonStringBuilder.toString();

            // Konverterer tekst hentet fra JSON filen til et JSON objekt
            // og handling array blir laget fra JSON objektet
            JsonObject jsonObject = G.fromJson(jsonString, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("handlinger");

            // Konverterer hvert element i handling array-en til et handling objekt
            // dvs. strukturen som er definert i Handling class
            for (int i = 0; i < jsonArray.size(); i++) {
                Handling handling = G.fromJson(jsonArray.get(i), Handling.class);
                handlingListe.add(handling);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handlingListe;
    }

    // Metode for å legge til ny handling til JSON filen
    public void leggTilHandling(Handling nyHandling) {
        try {
            List<Handling> handlingListe = hentHandlinger();
            handlingListe.add(nyHandling);

            // Konverterer listen tilbake til en JSON array
            JsonArray oppdatertJsonArray = new JsonArray();
            for (Handling handling : handlingListe) {
                JsonObject brukerJson = G.toJsonTree(handling).getAsJsonObject();
                oppdatertJsonArray.add(brukerJson);
            }

            // Skriver den oppdaterte informasjonen til JSON filen
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("handlinger", oppdatertJsonArray);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Logg.json"))) {
                G.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}