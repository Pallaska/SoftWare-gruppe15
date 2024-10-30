package com.example.smarthomeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.service.Authenticate;
import com.example.smarthomeapp.model.User;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    // Variabel for håndtering av autentisering og registrering
    private Authenticate authenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layouten for registrering
        setContentView(R.layout.activity_register);

        // Laster inn brukere fra json
        try {
            authenticate = new Authenticate(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Tekstfelt og knapp for registrering
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.registerButton);

        // Når brukeren trykker på knappen utføres dette
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Henter brukernavn og passord
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // Sjekker om noen av feltene er tomme
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Brukernavn og passord må fylles ut", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sjekker om brukernavnet allerede eksisterer
                if (authenticate.getUsers().stream().anyMatch(user -> user.getBrukernavn().equals(username))) {
                    Toast.makeText(RegisterActivity.this, "Brukernavn er allerede tatt", Toast.LENGTH_SHORT).show();
                } else {
                    // Lager en ny bruker-ID
                    int newUserId = generateNewUserId();
                    //Oppretter og legger til brukeren
                    User newUser = new User(newUserId, username, password, "01012000", "12345", "No Address", "email@example.com", 12345678);
                    authenticate.addUser(newUser);
                    Toast.makeText(RegisterActivity.this, "Registrering vellykket!", Toast.LENGTH_SHORT).show();
                    // Naviger tilbake til innloggingssiden
                    finish();
                }
            }
            // Metode for å generere ny bruker-ID
            private int generateNewUserId() {
                // Setter bruker-ID til 1 høyere enn den høyeste som eksisterer. Starter på 401 om ingen eksisterer
                return authenticate.getUsers().stream()
                        .mapToInt(User::getBrukerID)
                        .max()
                        .orElse(400) + 1;
            }
        });
    }
}
