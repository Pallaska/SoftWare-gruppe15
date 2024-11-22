package com.example.smarthomeapp.ui.instillinger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.ui.kontroll.AccessControlActivity;
import com.example.smarthomeapp.R;

/**
 * SettingsActivity håndterer innstillingersiden for appen.
 */

public class SettingsActivity extends AppCompatActivity {

    // Knapp for å navigere tilbake til forrige aktivitet
    private ImageView backButton;

    // Knapp for å navigere til tilgangskontrollsiden
    private ImageView tilgangskontrollImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innstillinger);

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
    }
}
