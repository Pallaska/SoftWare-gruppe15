package com.example.smarthomeapp.auth;
import java.util.List;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.User;
import org.mindrot.jbcrypt.BCrypt;
import android.content.Context;

// Klasse for autentisering, lasting av brukere fra json og validering
public class Authenticate {
    private Context context;

    private DataKonvertering dataKonvertering;

    // Liste som lagrer data om brukere når de er lastet fra json
    private List<Object> users;

    public Authenticate(Context context) {
        this.context = context;

        dataKonvertering = new DataKonvertering(context);
        users = dataKonvertering.hentFraJson("brukere", User.class, "Data.json");
    }

    // Få listen over brukere
    public List<Object> getUsers() {
        return users;
    }

    // Legge til bruker
    public void addUser(User user) {
        dataKonvertering.leggTilJson(user,"brukere", User.class, "Data.json");
    }
    // Metode for å validere innlogging. Returnerer true om brukernavn og passord matcher
    public boolean validateLogin(String username, String password) {
        for (Object user : users) {
            User userObjekt = (User) user;
            if (userObjekt.getBrukernavn().equals(username) && BCrypt.checkpw(password, userObjekt.getPassord())) {
                return true;
            }
        }
        return false;
    }
    // Metode for endring av brukernavn og passord
    public boolean updateCredentials(int brukerID, String currentPassword, String newUsername, String newPassword) {
        for (Object user : users) {
            User userObjekt = (User) user;
            if (userObjekt.getBrukerID() == brukerID && BCrypt.checkpw(currentPassword, userObjekt.getPassord())) {

                userObjekt.setBrukernavn(newUsername);

                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                userObjekt.setPassord(hashedNewPassword);

                dataKonvertering.leggTilJson(userObjekt, "brukere", User.class, "Data.json");

                return true;
            }
        }
        return false;
    }
}
