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

        // For innlogging
        authenticate = new Authenticate();

        // Tekstfelt. ID-er skal være like som i xml-filen
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);

        // Innloggingsknapp
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();  // Henter brukernavn
                String password = passwordField.getText().toString();  // Henter passord

                // Sjekker om brukernavn og passord er riktig
                if (authenticate.validateLogin(username, password)) {
                    Toast.makeText(MainActivity.this, "Innlogging vellykket!", Toast.LENGTH_SHORT).show();
                    // Her skal det legges kode for å navigere til neste skjerm
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
