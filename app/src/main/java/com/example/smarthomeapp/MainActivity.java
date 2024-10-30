package com.example.smarthomeapp;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smarthomeapp.JSON.DataKonvertering;
import com.example.smarthomeapp.model.User;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class MainActivity extends AppCompatActivity {

    private DataKonvertering K;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Setter padding for system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialiserer Konvertering-klassen for å håndtere JSON-database
        K = new DataKonvertering();

        // Eksempel på å legge til en ny bruker med hashet passord
        User eksempelBruker = new User(1, "brukernavn", "passord", "01-01-2000", "rettighet", "email@example.com", "adresse", 12345678);
        K.leggTilBruker(eksempelBruker); // Hasher passordet før lagring

        // Eksempel på validering av innlogging
        boolean loginSuccess = validateLogin("brukernavn", "passord"); // Sjekker brukernavn og passord

        if (loginSuccess) {
            System.out.println("Innlogging vellykket!");
        } else {
            System.out.println("Innlogging feilet!");
        }
    }

    // Metode for å validere innlogging ved å sjekke oppgitt passord mot hashet passord i JSON
    public boolean validateLogin(String username, String password) {
        // Henter alle brukere fra JSON-filen
        List<User> brukere = K.hentBrukere();

        // Sjekker om brukernavn finnes i listen og sammenligner passord
        for (User bruker : brukere) {
            if (bruker.getBrukernavn().equals(username)) {
                // Sjekker om passordet stemmer overens med det hashede passordet
                return BCrypt.checkpw(password, bruker.getPassord());
            }
        }
        return false; // Hvis brukernavn ikke finnes eller passordet ikke matcher
    }
}
