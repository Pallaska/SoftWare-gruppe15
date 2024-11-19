package com.example.smarthomeapp;
import java.util.List;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.User;
import org.mindrot.jbcrypt.BCrypt;
import android.content.Context;

// Klasse for autentisering, lasting av brukere fra json og validering
public class Authenticate {
    DataKonvertering dataKonvertering = new DataKonvertering();
    private Context context;
    // Liste som lagrer data om brukere når de er lastet fra json
    private List<Object> users;

    public Authenticate(Context context) {
        this.context = context;
        users = dataKonvertering.hentFraJson("A", User.class, "C");
    }

    // Få listen over brukere
    public List<Object> getUsers() {
        return users;
    }

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
    }
}


