package com.example.smarthomeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Authenticate authenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authenticate = new Authenticate();  // Bruker samme authenticate-objekt, kan senere utvides

        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Brukernavn og passord må fylles ut", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sjekker om brukernavnet allerede eksisterer
                if (authenticate.getUsers().containsKey(username)) {
                    Toast.makeText(RegisterActivity.this, "Brukernavn er allerede tatt", Toast.LENGTH_SHORT).show();
                } else {
                    // Legger til ny bruker i HashMap (midlertidig)
                    authenticate.addUser(new User(username, password));
                    Toast.makeText(RegisterActivity.this, "Registrering vellykket!", Toast.LENGTH_SHORT).show();
                    // Naviger tilbake til innloggingssiden
                    finish();
                }
            }
        });
    }
}
