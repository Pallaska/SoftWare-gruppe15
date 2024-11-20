package com.example.smarthomeapp.service;
import android.content.Context;
import com.example.smarthomeapp.json.DataKonvertering;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.example.smarthomeapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

// Klasse for autentisering, lasting av brukere fra json og validering
public class Authenticate {
    // Liste som lagrer data om brukere når de er lastet fra json
    private List<User> users = new ArrayList<>();

    // Tar i bruk datakonvertering
    private DataKonvertering datakonvertering = new DataKonvertering();

    // Laster brukerinformasjonen fra json filen
    public Authenticate(Context context) throws IOException {
        loadUsersFromJson(context);
    }

    // Laster brukere fra json og konverterer fra json-data til java-objekter
    private void loadUsersFromJson(Context context) throws IOException{
        try {
            // Åpner json
            InputStream inputStream = context.getAssets().open("data.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();

            // Definerer type for brukerliste
            Type userListType = new TypeToken<DataContainer>() {}.getType();

            // Konverterer json-data til DataContainer-objekt
            DataContainer dataContainer = gson.fromJson(reader, userListType);

            // Legger til brukere i listen
            if (dataContainer != null && dataContainer.brukere != null) {
                users.addAll(dataContainer.brukere);
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    // Metode for validering av epostadresse
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    // Metode for validering av brukernavn
    private boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        // Sjekker om brukernavnet er for langt (maks 50 tegn)
        if (username.length() > 50) {
            return false;
        }
        // Sjekker om brukernavnet allerede er i bruk
        for (User existingUser : users) {
            if (existingUser.getBrukernavn().equals(username)) {
                return false;
            }
        }
        return true;
    }

    // Metode for validering av passordstyrke
    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        // Sjekker om passordet er for kort (minst 6 tegn)
        if (password.length() < 6) {
            return false;
        }
        // Sjekker om passordet er for langt (maks 100 tegn)
        return password.length() <= 100;
    }

    // Få listen over brukere
    public List<User> getUsers() {
        return users;
    }

    // Legge til bruker
    public boolean addUser(User user) {
        // Validerer brukernavn
        if (!isValidUsername(user.getBrukernavn())) {
            return false;
        }

        // Validerer epost
        if (!isValidEmail(user.getEmail())) {
            return false;
        }

        if (!isValidPassword(user.getPassord())) {
            return false;
        }

        // Hasher passordet før lagring
        String hashedPassword = BCrypt.hashpw(user.getPassord(), BCrypt.gensalt());
        user.setPassord(hashedPassword);

        // Bruker lagt til vellykket
        users.add(user);
        return true;
    }

    // Metode for å validere innlogging. Returnerer true om brukernavn og passord matcher
    public boolean validateLogin(String username, String password) {
        for (User user : users) {
            if (user.getBrukernavn().equals(username) && BCrypt.checkpw(password, user.getPassord())) {
                return true;
            }
        }
        return false;
    }

    // Klasse for å holde json-data
    public static class DataContainer {
        List<User> brukere;
    }

    // Metode for endring av brukernavn og passord
    public boolean updateCredentials(int brukerID, String currentPassword, String newUsername, String newPassword) {
        List<User> users = datakonvertering.hentBrukere();

        for (User user : users) {
            if (user.getBrukerID() == brukerID && BCrypt.checkpw(currentPassword, user.getPassord())) {

                // Validerer nytt brukernavn
                if (!isValidUsername(newUsername)) {
                    return false;
                }

                // Validerer nytt passord
                if (!isValidPassword(newPassword)) {
                    return false;
                }

                user.setBrukernavn(newUsername);

                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassord(hashedNewPassword);

                datakonvertering.leggTilBruker(user);

                return true;
            }
        }
        return false;
    }

}


