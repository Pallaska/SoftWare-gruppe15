package com.example.smarthomeapp;
import java.util.List;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

// Klasse for autentisering, lasting av brukere fra json og validering
public class Authenticate {

    DataKonvertering dataKonvertering = new DataKonvertering();
    // Liste som lagrer data om brukere når de er lastet fra json
    private List<User> users;
    private String dataFilePath;

    public Authenticate(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        users = dataKonvertering.hentFraJson("brukere", User.class, dataFilePath);
    }

    // Få listen over brukere
    public List<User> getUsers() {
        return users;
    }

    // Legge til bruker
    public boolean addUser(User newUser) {
        // Validerer brukernavn, passord og e-post
        if (newUser.getBrukernavn() == null || newUser.getPassord() == null || newUser.getEmail() == null ||
                newUser.getBrukernavn().isEmpty() || newUser.getPassord().isEmpty() || newUser.getEmail().isEmpty()) {
            return false;
        }

        // Sjekker om brukernavnet allerede eksisterer
        for (User user : users) {
            if (user.getBrukernavn().equals(newUser.getBrukernavn())) {
                return false;
            }
        }

        // Validerer e-postformat
        if (!isValidEmail(newUser.getEmail())) {
            return false;
        }

        // Validerer passordlengde (mellom 6 og 20 tegn)
        if (newUser.getPassord().length() < 6 || newUser.getPassord().length() > 20) {
            return false;
        }

        // Validerer brukernavnlengde (maks 15 tegn)
        if (newUser.getBrukernavn().length() > 15) {
            return false;
        }

        // Hasher passordet før lagring
        String hashedPassword = BCrypt.hashpw(newUser.getPassord(), BCrypt.gensalt());
        newUser.setPassord(hashedPassword);

        // Legger til brukeren i listen
        users.add(newUser);

        // Lagrer oppdatert liste til JSON-filen
        dataKonvertering.objektTilJson(users, "brukere", dataFilePath);

        return true;
    }

    // Metode for å validere innlogging. Returnerer true om brukernavn og passord matcher
    public boolean validateLogin(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
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

    // Metode som validerer email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }
}


