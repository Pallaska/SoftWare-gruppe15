package com.example.smarthomeapp.json;
import com.example.smarthomeapp.model.User;
import com.google.gson.*;
import org.mindrot.jbcrypt.BCrypt;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataKonvertering {
    public static Gson G = new GsonBuilder().setPrettyPrinting().create();

    // Metode som leser objekter fra JSON og returnerer en liste med hentede objekter
    public <T> List<T> hentFraJson(String kategori, Class<T> klasseNavn, String filnavn) {
        List<T> hentetListe = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filnavn))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String linje;
            while ((linje = br.readLine()) != null) {
                jsonStringBuilder.append(linje);
            }
            String jsonString = jsonStringBuilder.toString();

            // Konverterer tekst hentet fra JSON filen til et JSON objekt
            // og objekt array blir laget fra JSON objektet
            JsonObject jsonObject = G.fromJson(jsonString, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray(kategori);

            // Konverterer hvert element i bruker array-en til et objekt T
            // dvs. strukturen som er definert i objektens klasse
            for (int i = 0; i < jsonArray.size(); i++) {
                T hentet = G.fromJson(jsonArray.get(i), klasseNavn);
                hentetListe.add(hentet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            // Håndterer JSON-parsing unntak
            e.printStackTrace();
            return hentetListe;
        }
        return hentetListe;
    }

    // Metode for å legge til noe nytt til et kategori i JSON filen
    public <T> void leggTilJson(T ny, String kategori, Class<T> klasseNavn, String filnavn) {
        List<T> hentetListe = hentFraJson(kategori, klasseNavn, filnavn);

        if (Objects.equals(kategori, "bruker")) {
            // Hasher passordet
            User nyBruker = (User)ny;
            String hashedPassword = BCrypt.hashpw(nyBruker.getPassord(), BCrypt.gensalt());
            nyBruker.setPassord(hashedPassword); // linket med setPassord-metoden er lagt til i User.java
        }
        // Legger til nytt objekt til listen
        hentetListe.add(ny);

        // Konverterer objekt til Json
        objektTilJson(hentetListe, kategori, filnavn);
    }

    public <T> void objektTilJson(List<T> hentetListe, String kategori, String filnavn) {
        try {
            // Konverterer listen tilbake til en JSON array
            JsonArray oppdatertJsonArray = new JsonArray();
            for (Object hentet : hentetListe) {
                JsonObject brukerJson = G.toJsonTree(hentet).getAsJsonObject();
                oppdatertJsonArray.add(brukerJson);
            }

            // Skriver den oppdaterte informasjonen til JSON filen
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(kategori, oppdatertJsonArray);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filnavn))) {
                G.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
