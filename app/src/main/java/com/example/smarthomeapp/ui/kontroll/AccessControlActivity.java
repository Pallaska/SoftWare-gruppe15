package com.example.smarthomeapp.ui.kontroll;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.R;

/**
 * AccessControlActivity håndterer funksjonalitet for tilgangskontrollsiden i appen.
 * Brukeren kan vise en tilgangskode og logge ut via denne aktiviteten.
 */

public class AccessControlActivity extends AppCompatActivity {

    private TextView hiddenCodeField; // TextView som viser tilgangskoden (skjult som standard)
    private Button showAccessCodeButton; // Knapp for å vise tilgangskoden
    private ImageView logoutButton; // Bildeikon som fungerer som en logg ut knapp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilgangskontroll);

        // Initialiserer UI-elementene definert i XML-layouten
        hiddenCodeField = findViewById(R.id.hiddenCodeField);
        showAccessCodeButton = findViewById(R.id.showAccessCodeButton);
        logoutButton = findViewById(R.id.logoutButton); // Finner logoutButton


        showAccessCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Viser en forhåndsdefinert tilgangskode i TextView
                hiddenCodeField.setText("1234-5678"); // Tallet er et eksempel på en tilgangskode
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avslutter denne aktiviteten for å gå tilbake til hjemsiden
                finish();
            }
        });
    }
}