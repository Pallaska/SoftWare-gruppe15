package com.example.smarthomeapp.json;
import com.example.smarthomeapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.mindrot.jbcrypt.BCrypt;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataKonvertering {
    public static Gson G = new GsonBuilder().setPrettyPrinting().create();

    // Metode som leser brukere fra JSON og returnerer en liste med bruker objekter
    public List<User> hentBrukere() {
        List<User> brukereList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(getFileReader("Data.json"))) {
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
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return hentetListe;
    }

    // Metode for å legge til noe nytt til et kategori i JSON filen
    public <T> void leggTilJson(Object ny, String kategori, Class<T> klasseNavn, String filnavn) {
        List<Object> hentetListe = hentFraJson(kategori, klasseNavn, filnavn);

            // Sjekker for duplikat brukerID eller brukernavn
            for (User bruker : brukereList) {
                if (bruker.getBrukerID() == nyBruker.getBrukerID() || bruker.getBrukernavn().equals(nyBruker.getBrukernavn())) {
                    return;
                }
            }

            // Hash passordet
            User nyBruker = (User)ny;
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
            String jsonData = G.toJson(jsonObject);

            writeToFile("Data.json", jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Legger til nytt objekt til listen
        hentetListe.add(ny);

        // Konverterer objekt til Json
        objektTilJson(hentetListe, kategori, filnavn);
    }

    public void objektTilJson(List<Object> hentetListe, String kategori, String filnavn) {
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

    public Reader getFileReader(String filnavn) throws FileNotFoundException {
        return new FileReader(filnavn);
    }

    public void writeToFile(String filnavn, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filnavn))) {
            writer.write(data);
        }
    }
}
