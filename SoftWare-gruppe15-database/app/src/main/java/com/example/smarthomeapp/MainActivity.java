package com.example.smarthomeapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private Database database;

    // Lists to store data from the cursor
    private ArrayList<String> brukernavnListe = new ArrayList<>();
    private ArrayList<String> passordListe = new ArrayList<>();
    private ArrayList<String> fodselsdatoListe = new ArrayList<>();
    private ArrayList<String> rettigheterListe = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // Initialize the database
        database = new Database(this);

        // Fetch data from the database
        fetchDataFromDatabase();

        // Set login button listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check login credentials
                if (validateLogin(username, password)) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Proceed to next activity or home screen
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Fetch data from the database and store it in the lists
    private void fetchDataFromDatabase() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM BRUKERE", null);

        if (cursor.moveToFirst()) {
            do {
                String brukernavn = cursor.getString(cursor.getColumnIndexOrThrow("BRUKERNAVN"));
                String passord = cursor.getString(cursor.getColumnIndexOrThrow("PASSORD"));
                String fodselsdato = cursor.getString(cursor.getColumnIndexOrThrow("FODSELSDATO"));
                String rettigheter = cursor.getString(cursor.getColumnIndexOrThrow("RETTIGHETER"));

                // Add data to corresponding lists
                brukernavnListe.add(brukernavn);
                passordListe.add(passord);
                fodselsdatoListe.add(fodselsdato);
                rettigheterListe.add(rettigheter);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    // Validate login by checking if username exists and if password matches
    private boolean validateLogin(String username, String password) {
        // Check if the username exists in the list
        if (brukernavnListe.contains(username)) {
            // Get the index of the username and check if the password at the same index matches
            int index = brukernavnListe.indexOf(username);
            return passordListe.get(index).equals(password);
        }
        return false;
    }
}
