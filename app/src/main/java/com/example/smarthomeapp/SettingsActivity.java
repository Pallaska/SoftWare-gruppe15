package com.example.smarthomeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private ImageView backButton;
    private ImageView tilgangskontrollImageView; // Legg til denne variabelen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innstillinger);

        // Finn backButton i layouten
        backButton = findViewById(R.id.logoutButton);

        // Sett en OnClickListener på backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Avslutt denne aktiviteten for å gå tilbake til forrige aktivitet
                finish();
            }
        });

        // **Finn tilgangskontrollImageView i layouten**
        tilgangskontrollImageView = findViewById(R.id.tilgangskontrollImageView);

        // **Sett en OnClickListener på tilgangskontrollImageView**
        tilgangskontrollImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start TilgangskontrollActivity
                Intent intent = new Intent(SettingsActivity.this, AccessControlActivity.class);
                startActivity(intent);
            }
        });
    }
}