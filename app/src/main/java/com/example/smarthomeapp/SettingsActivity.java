package com.example.smarthomeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * SettingsActivity håndterer innstillingersiden for appen.
 */

public class SettingsActivity extends AppCompatActivity {

    // Knapp for å navigere tilbake til forrige aktivitet
    private ImageView backButton;

    // Knapp for å navigere til tilgangskontrollsiden
    private ImageView tilgangskontrollImageView;

    // Knapp for å endre tema (lys/mørk modus)
    private Button appTemaButton;

    // Knapp for å navigere til administrer enheter-siden
    private Button manageDevicesButton;

    // SharedPreferences for å lagre brukerpreferanser
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innstillinger);

        // Initialiserer SharedPreferences
        preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        // Sjekk om "dark_mode" finnes i SharedPreferences
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);

        // Hvis det ikke finnes, sett standard til lys modus
        if (!preferences.contains("dark_mode")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            preferences.edit().putBoolean("dark_mode", false).apply();
            isDarkMode = false;
        }

        // Initialiserer backButton fra layouten
        backButton = findViewById(R.id.logoutButton);

        // Legger til en OnClickListener på backButton for å gå tilbake til hjemsiden
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialiserer tilgangskontrollImageView fra layouten
        tilgangskontrollImageView = findViewById(R.id.tilgangskontrollImageView);

        // Legger til en OnClickListener på tilgangskontrollImageView for å åpne tilgangskontrollsiden
        tilgangskontrollImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AccessControlActivity.class);
                startActivity(intent);
            }
        });

        // Initialiserer appTemaButton fra layouten
        appTemaButton = findViewById(R.id.apptema);

        // Sett riktig tekst basert på lagret modus
        appTemaButton.setText(isDarkMode ? "Mørkt" : "Lyst");

        // Legg til klikk-lytter for å endre tema
        appTemaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDarkMode = preferences.getBoolean("dark_mode", false);
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    preferences.edit().putBoolean("dark_mode", false).apply();
                    appTemaButton.setText("Lyst");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    preferences.edit().putBoolean("dark_mode", true).apply();
                    appTemaButton.setText("Mørkt");
                }
            }
        });

        // Initialiserer manageDevicesButton fra layouten
        manageDevicesButton = findViewById(R.id.manageDevicesButton);

        // Legger til en OnClickListener på manageDevicesButton for å åpne administrer enheter-siden
        manageDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AdministrerEnheterActivity.class);
                startActivity(intent);
            }
        });
    }
}
