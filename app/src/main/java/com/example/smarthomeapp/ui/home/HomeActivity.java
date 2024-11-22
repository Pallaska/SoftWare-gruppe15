package com.example.smarthomeapp.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.R;
import com.example.smarthomeapp.ui.admin.AdministrerEnheterActivity;
import com.example.smarthomeapp.ui.instillinger.SettingsActivity;
import com.example.smarthomeapp.ui.start.MainActivity;

/**
 * HomeActivity fungerer som hjemsiden i appen.
 * Brukeren kan logge ut, administrere enheter eller gå til innstillinger.
 */

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialiserer logoutButton fra layouten
        ImageView logoutButton = findViewById(R.id.logoutButton);

        // Setter en OnClickListener på logoutButton for å logge ut brukeren
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tilbakestiller innloggingsstatus i SharedPreferences
                SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                // Navigerer til innlogginsskjermen
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialiserer manageDevicesButton fra layouten
        Button manageDevicesButton = findViewById(R.id.manageDevicesButton);

        // Setter en OnClickListener på manageDevicesButton for å navigere til administrasjonssiden
        manageDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AdministrerEnheterActivity.class);
                startActivity(intent);
            }
        });

        // Initialiserer settingsIcon fra layouten
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Setter en OnClickListener på settingsIcon for å navigere til innstillingersiden
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
