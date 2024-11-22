package com.example.smarthomeapp;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.User;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * RegisterActivity håndterer registreringssiden.
 * Brukeren angir brukernavn, passord og fødselsdato for å opprette en konto.
 */

public class RegisterActivity extends AppCompatActivity {

    // Input felter for brukernavn, passord og fødselsdato
    private TextView statusMessage;
    private EditText usernameField, passwordField, birthdateField, rettigheterField, adresseField, emailField, telefonField;

    // Knapp for å registrere en ny bruker
    private Button registerButton;
    private Context context;

    public RegisterActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        // Få referanser til feltene og knappen fra layouten
        statusMessage = findViewById(R.id.statusMessage);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        birthdateField = findViewById(R.id.birthdate);
        rettigheterField = findViewById(R.id.rettigheter);
        adresseField = findViewById(R.id.adresse);
        emailField = findViewById(R.id.email);
        telefonField = findViewById(R.id.telefon);
        registerButton = findViewById(R.id.registerButton);

        // Setter opp klikklytter for fødselsdatofeltet for å åpne en DatePickerDialog
        birthdateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Henter dagens dato for DatePicker
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Oppretter og viser DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Formaterer valgt dato som dd/mm/yyyy
                            String date = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1);
                            birthdateField.setText(date);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Setter opp klikklytter for teksten "Har du allerede en bruker?" for å navigere tilbake til innloggingssiden
        TextView alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        alreadyHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Setter opp klikklytter for "Registrer"-knappen
        registerButton.setOnClickListener(v -> {
            DataKonvertering dataKonvertering = new DataKonvertering(this);
            List<Object> brukere = dataKonvertering.hentFraJson("brukere", User.class, "Data.json");
            if (brukere.size() < 9) {
                // Henter brukerens input fra feltene
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString();
                String birthdate = birthdateField.getText().toString();
                String rettigheter = rettigheterField.getText().toString();
                String adresse = adresseField.getText().toString();
                String email = emailField.getText().toString();
                int telefon = Integer.parseInt(telefonField.getText().toString());

                // Sjekker input for å sikre at alle feltene er fylt ut
                if (username.isEmpty() || password.isEmpty() || birthdate.isEmpty()
                        || adresse.isEmpty() || email.isEmpty() || telefon == 0) {
                    // Viser en feilmelding dersom noen felter mangler
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_msg6), Toast.LENGTH_SHORT).show();
                } else {
                    // Sjekker hvilken ID som er sist, for å sette neste ID
                    // Sjekker også om brukernavn allerede finnes
                    List<Object> brukerListe = dataKonvertering.hentFraJson("bruker", User.class, "Data.json");
                    int nyBrukerID = 0;
                    boolean brukerFinnes = false;
                    for (Object bruker : brukerListe) {
                        User user = (User) bruker;
                        if (user.getBrukerID() > nyBrukerID) {
                            nyBrukerID = user.getBrukerID();
                        }
                        if (Objects.equals(user.getBrukernavn(), username)) {
                            brukerFinnes = true;
                        }
                    }
                    // Lager en ny brukerobjekt
                    User user = new User(nyBrukerID, username, password, birthdate, rettigheter, email, adresse, telefon);
                    // Legger til nytt brukerobjekt til json
                    if (!brukerFinnes) {
                        dataKonvertering.leggTilJson(user, "brukere", User.class, "Data.json");
                        // Viser en melding om at registreringen var vellykket
                        Toast.makeText(RegisterActivity.this, getString(R.string.register_msg3), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.register_msg4), Toast.LENGTH_SHORT).show();
                    }

                    // Navigerer til MainActivity (innloggingsskjermen)
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(RegisterActivity.this, getString(R.string.register_msg4), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
