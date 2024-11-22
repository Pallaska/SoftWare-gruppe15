package com.example.smarthomeapp;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smarthomeapp.auth.Authenticate;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.Instillinger;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Authenticate authenticate;
    private DataKonvertering dataKonvertering;
    public static String language;
    public static String loggedInUser;

    // Opprettelse av JSON, bruk av språk-instillinger
    // Det er viktig å laste det inn før onCreate og ved bruk
    // av attachBaseContext, som modifiserer konfigurasjonen
    // til en Activity før den er initialisert
    @Override
    protected void attachBaseContext(Context newBase) {
        // Bruker denne konteksten siden "this" ikke
        // er initialisert på dette tidspunktet
        dataKonvertering = new DataKonvertering(newBase);

        // Når appen startes så lages det Data/Logg med enkel struktur
        // om den ikke finnes allerede
        dataKonvertering.enkelJsonStruktur("Data.json");
        dataKonvertering.enkelJsonStruktur("Logg.json");

        // Henter det lagrede språket
        Instillinger instillinger = null;
        List<Object> instillingerListe = dataKonvertering.hentFraJson("instillinger", Instillinger.class, "Data.json");
        if (instillingerListe != null && !instillingerListe.isEmpty()) {
            Object instillingerObjekt = instillingerListe.get(0);
            instillinger = (Instillinger) instillingerObjekt;
        }
        // Configuration objektet initialiseres utenfor if-statement i
        // tilfelle instillinger eller instillinger.getLanguage() er null
        Configuration config = newBase.getResources().getConfiguration();
        if (instillinger != null && instillinger.getLanguage() != null) {
            // Bruker det lagrede språket
            Locale locale = new Locale(instillinger.getLanguage());
            Locale.setDefault(locale);
            config.setLocale(locale);

            // Lagt til i variabel for å hente språk i andre activity
            // uten å måtte hente fra JSON
            language = instillinger.getLanguage();
        } else {
            language = "no";
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            config.setLocale(locale);
        }

        super.attachBaseContext(newBase.createConfigurationContext(config));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // For innlogging
        authenticate = new Authenticate(getApplicationContext());

        // Tekstfelt og innloggingsknapp. ID-er skal være like som i xml-filen
        TextView statusMessage = findViewById(R.id.statusMessage);
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerLink = findViewById(R.id.registerLink);

        statusMessage.setText(R.string.login_msg1);

        usernameField.setHint(R.string.login_cred1);
        passwordField.setHint(R.string.login_cred2);

        loginButton.setText(R.string.login_button);
        registerLink.setText(R.string.register_msg1);

        loginButton.setOnClickListener(v -> {
            loggedInUser = usernameField.getText().toString();
            String username = usernameField.getText().toString();  // Henter brukernavn
            String password = passwordField.getText().toString();  // Henter passord

            // Sjekker om brukernavn og passord er riktig
            if (authenticate.validateLogin(username, password)) {
                Toast.makeText(MainActivity.this, getString(R.string.login_msg3), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.login_msg2), Toast.LENGTH_SHORT).show();
            }
        });

        // Registrering-skjerm
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
