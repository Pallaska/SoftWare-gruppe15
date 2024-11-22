package com.example.smarthomeapp.json;
import android.content.Context;
import com.example.smarthomeapp.model.User;
import com.google.gson.*;
import org.mindrot.jbcrypt.BCrypt;
import java.io.*;
import java.util.*;

public class DataKonvertering {
    public static Gson G = new GsonBuilder().setPrettyPrinting().create();
    private Context context;
    public DataKonvertering(Context context) {
        this.context = context;
    }

    public void enkelJsonStruktur(String filnavn) {
        File file = new File(context.getFilesDir(), filnavn);

        if (!file.exists()) {
            try {
                FileOutputStream fos = context.openFileOutput(filnavn, Context.MODE_PRIVATE);

                String jsonStruktur = "";

                if (Objects.equals(filnavn, "Data.json")) {
                    jsonStruktur = "{\n" +
                            "  \"brukere\": [],\n" +
                            "  \"WiFi enheter\": [],\n" +
                            "  \"Bluetooth enheter\": [],\n" +
                            "  \"instillinger\": []\n" +
                            "}";
                }
                if (Objects.equals(filnavn, "Logg.json")) {
                    jsonStruktur = "{\n" +
                            "  \"handlinger\": []\n" +
                            "}";
                }
                fos.write(jsonStruktur.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Metode som leser objekter fra JSON og returnerer en liste med hentede objekter
    public <T> List<Object> hentFraJson(String kategori, Class<T> klasseNavn, String filnavn) {
        List<Object> hentetListe = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(context.getFilesDir(), filnavn)))) {
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
            if (jsonArray != null && !jsonArray.isEmpty()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object hentet = G.fromJson(jsonArray.get(i), klasseNavn);
                    hentetListe.add(hentet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hentetListe;
    }

    // Metode for å legge til noe nytt til et kategori i JSON filen
    public <T> void leggTilJson(Object ny, String kategori, Class<T> klasseNavn, String filnavn) {
        List<Object> hentetListe = hentFraJson(kategori, klasseNavn, filnavn);

        if (Objects.equals(kategori, "brukere")) {
            // Hash passordet
            User nyBruker = (User)ny;
            String hashedPassword = BCrypt.hashpw(nyBruker.getPassord(), BCrypt.gensalt());
            nyBruker.setPassord(hashedPassword); // linket med setPassord-metoden er lagt til i User.java
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

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(context.getFilesDir(), filnavn)))) {
                G.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
