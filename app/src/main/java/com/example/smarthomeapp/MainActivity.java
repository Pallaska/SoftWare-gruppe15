package com.example.smarthomeapp;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Database database;

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

        // Database objekt
        database = new Database(this);
        SQLiteDatabase db = database.getWritableDatabase();

        // Legger til en bruker til databasen
        database.leggTilBruker("Brukernavn1", "Halden_123", "15-10-2002", 3451);

        // Henter brukerdata fra databasen
        Cursor cursor = database.hentBrukerdata();
        if (cursor.moveToFirst()) {
            do {
                String brukernavn = cursor.getString(cursor.getColumnIndexOrThrow("BRUKERNAVN"));
                String passord = cursor.getString(cursor.getColumnIndexOrThrow("PASSORD"));
                String fodselsdato = cursor.getString(cursor.getColumnIndexOrThrow("FODSELSDATO"));
                int rettigheter = cursor.getInt(cursor.getColumnIndexOrThrow("RETTIGHETER"));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}