package com.example.smarthomeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * AdministrerEnheterActivity håndterer siden for administrasjon av enheter.
 */

public class AdministrerEnheterActivity extends AppCompatActivity {

    private ImageView logoutButton; // Bildeikon som fungerer som en logg ut knapp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrer_enheter);

        // Initialiserer logoutButton fra layouten
        logoutButton = findViewById(R.id.logoutButton); // Finner logoutButton i layouten

        // Legger til en lytter for logOut knappen
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avslutter denne aktiviteten for å gå tilbake til hjemsiden
                finish();
            }
        });
    }
}