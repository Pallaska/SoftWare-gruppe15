package com.example.smarthomeapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

/**
 * RegisterActivity håndterer registreringssiden.
 * Brukeren angir brukernavn, passord og fødselsdato for å opprette en konto.
 */

public class RegisterActivity extends AppCompatActivity {

    // Input felter for brukernavn, passord og fødselsdato
    private EditText usernameField, passwordField, birthdateField;

    // Knapp for å registrere en ny bruker
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Få referanser til feltene og knappen fra layouten
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        birthdateField = findViewById(R.id.birthdate);
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
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Setter opp klikklytter for "Registrer"-knappen
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Henter brukerens input fra feltene
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString();
                String birthdate = birthdateField.getText().toString();

                // Sjekker input for å sikre at alle feltene er fylt ut
                if (username.isEmpty() || password.isEmpty() || birthdate.isEmpty()) {
                    // Viser en feilmelding dersom noen felter mangler
                    Toast.makeText(RegisterActivity.this, "Vennligst fyll ut alle feltene", Toast.LENGTH_SHORT).show();
                } else {
                    // Lagrer brukerinformasjon ved hjelp av SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.apply();

                    // Viser en melding om at registreringen var vellykket
                    Toast.makeText(RegisterActivity.this, "Registrering vellykket!", Toast.LENGTH_SHORT).show();

                    // Navigerer til MainActivity (innloggingsskjermen)
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
