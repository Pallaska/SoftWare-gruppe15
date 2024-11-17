package com.example.smarthomeapp.service;
import com.example.smarthomeapp.json.DataKonvertering;
import java.io.IOException;
import java.util.List;
import com.example.smarthomeapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

// Klasse for autentisering, lasting av brukere fra json og validering
public class Authenticate {
    DataKonvertering dataKonvertering = new DataKonvertering();

    // Liste som lagrer data om brukere når de er lastet fra json
    private List<User> users;

    // Laster brukerinformasjonen fra json filen
    public Authenticate() throws IOException {
        users = dataKonvertering.hentFraJson();
    }

    // Få listen over brukere
    public List<User> getUsers() {
        return users;
    }

    // Legge til bruker
    public void addUser(User user) {
        dataKonvertering.leggTilJson();
    }

    // Metode for å validere innlogging. Returnerer true om brukernavn og passord matcher
    public boolean validateLogin(String username, String password) {
        for (User user : users) {
            if (user.getBrukernavn().equals(username) && user.getPassord().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Metode for endring av brukernavn og passord
    public boolean updateCredentials(int brukerID, String currentPassword, String newUsername, String newPassword) {
        List<User> users = datakonvertering.hentFraJson();

        for (User user : users) {
            if (user.getBrukerID() == brukerID && BCrypt.checkpw(currentPassword, user.getPassord())) {

                user.setBrukernavn(newUsername);

                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassord(hashedNewPassword);

                datakonvertering.leggTilJson(user);

                return true;
            }
        }
        return false;
    }
}


