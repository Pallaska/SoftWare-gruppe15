package com.example.smarthomeapp;
import java.io.IOException;
import java.util.List;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Authenticate klassen håndterer validering av brukerinformasjon
 * ved bruk av data lagret i SharedPreferences.
 */

// Klasse for autentisering, lasting av brukere fra json og validering
public class Authenticate {
    DataKonvertering dataKonvertering = new DataKonvertering();

    private Context context;
    // Liste som lagrer data om brukere når de er lastet fra json
    private List<Object> users;

    public Authenticate(Context context) {
        this.context = context;
    // Laster brukerinformasjonen fra json filen
    public Authenticate() throws IOException {
        users = dataKonvertering.hentFraJson("A", User.class, "C");
    }

    // Få listen over brukere
    public List<Object> getUsers() {
        return users;
    }
    /**
     * Sjekker dataen i SharedPreferences og ser om det matcher.
     *
     * @param username Brukernavn som brukeren oppgir.
     * @param password Passord som brukeren oppgir.
     * @return true hvis brukernavn og passord samsvarer med lagrede data eller så blir det false.
     */

    // Legge til bruker
    public void addUser(User user) {
        dataKonvertering.leggTilJson(user,"A", User.class, "C");
    }

    // Metode for å validere innlogging. Returnerer true om brukernavn og passord matcher
    public boolean validateLogin(String username, String password) {
        for (Object user : users) {
            User userObjekt = (User) user;
            if (userObjekt.getBrukernavn().equals(username) && userObjekt.getPassord().equals(password)) {
                return true;
            }
        }
        return false;
    }
        SharedPreferences preferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        String registeredUsername = preferences.getString("username", "");
        String registeredPassword = preferences.getString("password", "");

    // Metode for endring av brukernavn og passord
    public boolean updateCredentials(int brukerID, String currentPassword, String newUsername, String newPassword) {
        List<Object> users = dataKonvertering.hentFraJson("A", User.class, "B");

        for (Object user : users) {
            User userObjekt = (User) user;
            if (userObjekt.getBrukerID() == brukerID && BCrypt.checkpw(currentPassword, userObjekt.getPassord())) {

                userObjekt.setBrukernavn(newUsername);

                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                userObjekt.setPassord(hashedNewPassword);

                dataKonvertering.leggTilJson(userObjekt, "A", User.class, "B");

                return true;
            }
        }
        return false;
        // Sammenligner brukerinformasjonen med lagrede verdier
        return username.equals(registeredUsername) && password.equals(registeredPassword);
    }
}


