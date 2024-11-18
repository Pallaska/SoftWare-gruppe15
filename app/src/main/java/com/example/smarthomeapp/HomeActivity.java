package com.example.smarthomeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button; // Hvis du bruker knapper
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Eksisterende kode for logoutButton
        ImageView logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Tilbakestill innloggingsstatus
                SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                // Naviger til MainActivity
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Eksisterende kode for manageDevicesButton (hvis du har det)
        Button manageDevicesButton = findViewById(R.id.manageDevicesButton);

        manageDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start AdministrerEnheterActivity
                Intent intent = new Intent(HomeActivity.this, AdministrerEnheterActivity.class);
                startActivity(intent);
            }
        });

        // **Ny kode for settingsIcon**

        // Finn settingsIcon i layouten
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Sett en OnClickListener på settingsIcon
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start InnstillingerActivity
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}