package com.example.smarthomeapp;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import com.example.smarthomeapp.RegisterActivity;


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

        // For innlogging. Stopper utførelse av videre kode dersom noe går galt
        try {
            authenticate = new Authenticate(this);
        } catch (Exception e) {
            Toast.makeText(this, "Feil ved lasting av brukerdata", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tekstfelt. ID-er skal være like som i xml-filen
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);

        // Innloggingsknapp
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Sjekker om brukernavn og passord er riktig
                if (authenticate.validateLogin(username, password)) {
                    Toast.makeText(MainActivity.this, "Innlogging vellykket!", Toast.LENGTH_SHORT).show();
                    // Navigerer til neste skjerm. HomeActivity er skjermen som man kommer til når man logger inn
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Feil brukernavn eller passord", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Knapp for å navigere til RegisterActivity
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
