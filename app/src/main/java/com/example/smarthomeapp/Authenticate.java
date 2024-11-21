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

        // Validerer det nye brukernavnet
        if (!isValidUsername(newUser.getBrukernavn())) {
            return false;
        }

        // Validerer det nye passordet
        if (!isValidPassword(newUser.getPassord())) {
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
            if (userObjekt.getBrukerID() == brukerID) {
                // Sjekker om nåværende passord er korrekt
                if (!BCrypt.checkpw(currentPassword, userObjekt.getPassord())) {
                    return false;
                }

                // Sjekker om det nye brukernavnet allerede er tatt av en annen bruker
                for (User user : users) {
                    if (user.getBrukernavn().equals(newUsername) && user.getBrukerID() != brukerID) {
                        return false;
                    }
                }

                // Validerer det nye brukernavnet
                if (!isValidUsername(newUsername)) {
                    return false;
                }

                // Validerer det nye passordet
                if (!isValidPassword(newPassword)) {
                    return false;
                }

                // Oppdaterer brukernavn og passord
                userObjekt.setBrukernavn(newUsername);
                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                userObjekt.setPassord(hashedNewPassword);

                // Lagre oppdatert liste til JSON-filen
                dataKonvertering.objektTilJson(users, "brukere", dataFilePath);

                return true;
            }
        }
        return false;
    }

    // Metode som validerer email
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }

    // Metode for å validere brukernavn
    private boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        // Sjekker at brukernavnet ikke er for langt (maks 15 tegn)
        return username.length() <= 15;
    }

    // Metode for å validere passord
    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        // Sjekker at passordet er mellom 6 og 20 tegn
        return password.length() >= 6 && password.length() <= 20;
    }

}


