package com.example.smarthomeapp.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.R;
import com.example.smarthomeapp.MainActivity;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Legg til funksjonalitet for en knapp eller annen funksjonalitet
        ImageView logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviger tilbake til MainActivity (innloggingssiden)
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Lukker startsiden for å hindre at brukeren kan gå tilbake hit etter utlogging
            }
        });

    }
}
