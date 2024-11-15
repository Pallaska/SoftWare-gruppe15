package com.example.smarthomeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AccessControlActivity extends AppCompatActivity {

    private TextView hiddenCodeField;
    private Button showAccessCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilgangskontroll); //

        // Initialiser komponenter
        hiddenCodeField = findViewById(R.id.hiddenCodeField);
        showAccessCodeButton = findViewById(R.id.showAccessCodeButton);

        // Legg til funksjonalitet for "Vis tilgangskode"-knappen
        showAccessCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Her kan du legge til logikken for å vise koden
                hiddenCodeField.setText("1234-5678"); // Eksempel på en tilgangskode
            }
        });
    }
}
