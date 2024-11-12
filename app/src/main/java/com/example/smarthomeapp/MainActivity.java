package com.example.smarthomeapp;

import com.example.smarthomeapp.service.Authenticate;
import com.example.smarthomeapp.service.HomeActivity;
import com.example.smarthomeapp.service.RegisterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Authenticate authenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // For innlogging
        authenticate = new Authenticate();

        // Tekstfelt og innloggingsknapp
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // Sjekker om brukernavn og passord er riktig
                if (authenticate.validateLogin(username, password)) {
                    Toast.makeText(MainActivity.this, "Innlogging vellykket!", Toast.LENGTH_SHORT).show();
                    // Her kan du legge kode for å navigere til neste skjerm
                } else {
                    Toast.makeText(MainActivity.this, "Feil brukernavn eller passord", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // For å lukke innloggingssiden


        // Legger til klikklytter for "Registrer ny bruker" som navigerer til RegisterActivity
        TextView registerLink = findViewById(R.id.registerLink);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
