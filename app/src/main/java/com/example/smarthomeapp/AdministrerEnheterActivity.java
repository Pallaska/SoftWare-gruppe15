package com.example.smarthomeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AdministrerEnheterActivity extends AppCompatActivity {

    private ImageView logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrer_enheter);

        // Finn logoutButton i layouten
        logoutButton = findViewById(R.id.logoutButton);

        // Sett en OnClickListener på logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avslutt denne aktiviteten for å gå tilbake til forrige aktivitet
                finish();
            }
        });
    }
}