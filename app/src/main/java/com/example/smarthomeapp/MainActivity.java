package com.example.smarthomeapp;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smarthomeapp.model.User;

public class MainActivity extends AppCompatActivity {

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

        // Eksempel på bruk av JSON database

        // Lag objekter av User og Konvertering
        User eksempelBruker = new User(1, "A", "B", "C", "D", "E", "F", 2);
        Konvertering K = new Konvertering();

        // Returnerer en liste med brukere fra JSON-filen
        K.hentBrukere();
        // Legger til et bruker objekt til JSON-filen
        K.leggTilBruker(eksempelBruker);
    }
}
