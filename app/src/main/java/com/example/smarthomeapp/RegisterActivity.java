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

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameField, passwordField, birthdateField;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Få referanser til feltene
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        birthdateField = findViewById(R.id.birthdate);
        registerButton = findViewById(R.id.registerButton);

        // Håndterer klikk på fødselsdato-feltet (samme som før)
        birthdateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Hent dagens dato som startverdi for DatePicker
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Åpne DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Formater datoen som dd/mm/åååå
                            String date = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1);
                            birthdateField.setText(date); // Sett valgt dato i EditText
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Håndterer klikk på "Har du allerede en bruker?" (samme som før)
        TextView alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Går tilbake til MainActivity (innloggingssiden)
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Lukker registreringssiden for å hindre at brukeren kan gå tilbake hit med "tilbake"-knappen
            }
        });

        // Legg til klikklytter for "Registrer"-knappen
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Hent inn data fra feltene
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString();
                String birthdate = birthdateField.getText().toString();

                // Valider input (sjekk om feltene er fylt ut)
                if (username.isEmpty() || password.isEmpty() || birthdate.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vennligst fyll ut alle feltene", Toast.LENGTH_SHORT).show();
                } else {
                    // Lagre brukernavn og passord ved hjelp av SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "Registrering vellykket!", Toast.LENGTH_SHORT).show();

                    // Gå til MainActivity for innlogging
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}