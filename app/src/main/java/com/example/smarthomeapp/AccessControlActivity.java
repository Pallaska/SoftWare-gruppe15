package com.example.smarthomeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView; // Legg til denne importen
import androidx.appcompat.app.AppCompatActivity;

public class AccessControlActivity extends AppCompatActivity {

    private TextView hiddenCodeField;
    private Button showAccessCodeButton;
    private ImageView logoutButton; // Legg til denne variabelen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilgangskontroll);

        // Initialiser komponenter
        hiddenCodeField = findViewById(R.id.hiddenCodeField);
        showAccessCodeButton = findViewById(R.id.showAccessCodeButton);
        logoutButton = findViewById(R.id.logoutButton); // Finn logoutButton

        // Legg til funksjonalitet for "Vis tilgangskode"-knappen
        showAccessCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Her kan du legge til logikken for å vise koden
                hiddenCodeField.setText("1234-5678"); // Eksempel på en tilgangskode
            }
        });

        // Legg til OnClickListener for logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Avslutt denne aktiviteten for å gå tilbake til forrige aktivitet
                finish();
            }
        });
    }
}