package com.example.smarthomeapp;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smarthomeapp.JSON.Konvertering;
import com.example.smarthomeapp.model.Enhet;
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

        // Lag objekter av User, Enhet og Konvertering
        User eksempelBruker = new User(1, "A", "B", "C", "D", "E", "F", 2);
        Enhet eksempelEnhet = new Enhet(1, "A");
        Konvertering K = new Konvertering();

        // Returnerer en liste med brukere og enheter fra JSON-filen
        K.hentBrukere();
        K.hentEnheter();
        // Legger til et bruker og et enhet objekt til JSON-filen
        K.leggTilBruker(eksempelBruker);
        K.leggTilEnhet(eksempelEnhet);
    }
}
