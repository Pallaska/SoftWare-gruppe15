package com.example.smarthomeapp;
import java.util.List;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.User;
import org.mindrot.jbcrypt.BCrypt;
import android.content.Context;

// Klasse for autentisering, lasting av brukere fra json og validering
public class Authenticate {

    DataKonvertering dataKonvertering = new DataKonvertering();
    // Liste som lagrer data om brukere når de er lastet fra json
    private List<User> users;

    public Authenticate(String dataFilePath) {
        users = dataKonvertering.hentFraJson("brukere", User.class, dataFilePath);
    }

    // Få listen over brukere
    public List<User> getUsers() {
        return users;
    }

    // Legge til bruker
    public void addUser(User user) {
        // Hash passordet før lagring
        String hashedPassword = BCrypt.hashpw(user.getPassord(), BCrypt.gensalt());
        user.setPassord(hashedPassword);

        // Legg til brukeren i listen
        users.add(user);

        // Lagre oppdatert liste til JSON-filen
        dataKonvertering.objektTilJson(users, "brukere", "Data.json");
    }
    // Metode for å validere innlogging. Returnerer true om brukernavn og passord matcher
    public boolean validateLogin(String username, String password) {
        for (User userObjekt : users) {
            if (userObjekt.getBrukernavn().equals(username) && BCrypt.checkpw(password, userObjekt.getPassord())) {
                return true;
            }
        }
        return false;
    }
    // Metode for endring av brukernavn og passord
    public boolean updateCredentials(int brukerID, String currentPassword, String newUsername, String newPassword) {
        for (User userObjekt : users) {
            if (userObjekt.getBrukerID() == brukerID && BCrypt.checkpw(currentPassword, userObjekt.getPassord())) {

                userObjekt.setBrukernavn(newUsername);

                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                userObjekt.setPassord(hashedNewPassword);

                // Lagre oppdatert liste til JSON-filen
                dataKonvertering.objektTilJson(users, "brukere", "Data.json");

                return true;
            }
        }
        return false;
    }
}


