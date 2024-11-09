package com.example.smarthomeapp.json.konvertering;
import com.example.smarthomeapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.mindrot.jbcrypt.BCrypt;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataKonvertering {
    public static Gson G = new GsonBuilder().setPrettyPrinting().create();

    // Metode som leser objekter fra JSON og returnerer en liste med hentede objekter
    public <T> List<Object> hentFraJson(String kategori, Class<T> klasseNavn) {
        List<Object> hentetListe = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Data.json"))) {
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

            // Konverterer hvert element i bruker array-en til et objekt
            // dvs. strukturen som er definert i objektens klasse
            for (int i = 0; i < jsonArray.size(); i++) {
                Object hentet = G.fromJson(jsonArray.get(i), klasseNavn);
                hentetListe.add(hentet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hentetListe;
    }

    // Metode for å legge til noe nytt til et kategori i JSON filen
    public <T> void leggTilJson(Object ny, String kategori, Class<T> klasseNavn) {
        try {
            List<Object> hentetListe = hentFraJson(kategori, klasseNavn);

            if (Objects.equals(kategori, "bruker")) {
                // Hash passordet
                User nyBruker = (User)ny;
                String hashedPassword = BCrypt.hashpw(nyBruker.getPassord(), BCrypt.gensalt());
                nyBruker.setPassord(hashedPassword); // linket med setPassord-metoden er lagt til i User.java
            }
            // Legger til nytt objekt til listen
            hentetListe.add(ny);

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
