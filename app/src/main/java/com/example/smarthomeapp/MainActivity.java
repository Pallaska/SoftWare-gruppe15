package com.example.smarthomeapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smarthomeapp.JSON.DataKonvertering;
import com.example.smarthomeapp.JSON.LoggKonvertering;
import com.example.smarthomeapp.model.Enhet;
import com.example.smarthomeapp.model.Handling;
import com.example.smarthomeapp.model.User;
import com.example.smarthomeapp.integrering.skanning.BluetoothSkanning;
import com.example.smarthomeapp.integrering.skanning.WiFiSkanning;
import com.example.smarthomeapp.integrering.skanning.mDNSSkanning;

public class MainActivity extends AppCompatActivity {

    private ChatClientAPI chatClientAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

            // Oppretter en handling med brukerID og skriver til logg,
            // så lagres det til JSON-filen ved bruk av LoggKonvertering
            // BrukerID skal egentlig være dynamisk men det blir etter at login er ferdig
            // Tiden blir notert automatisk i LoggKonvertering konstruktøren
            Handling handling_A = new Handling(1, "MainActivity");
            new LoggKonvertering().leggTilHandling(handling_A);

            // Eksempel på bruk av JSON database
            // Lag objekter av User, Enhet og Konvertering
            User eksempelBruker = new User(1, "A", "B", "C", "D", "E", "F", 2);
            Enhet eksempelEnhet = new Enhet(1, "A");
            DataKonvertering K = new DataKonvertering();
            // Returnerer en liste med brukere og enheter fra JSON-filen
            K.hentBrukere();
            K.hentEnheter();
            // Legger til et bruker og et enhet objekt til JSON-filen
            K.leggTilBruker(eksempelBruker);
            K.leggTilEnhet(eksempelEnhet);

            // Eksempel på bruk av chat
            chatClientAPI = new ChatClientAPI();
            String melding = "Hei";
            chatClientAPI.sendMelding(melding);

            // mDNS objekt som søker etter enheter med tjenestetype EWELINK
            // og med et tidsavbrudd på 5 sekunder
            mDNSSkanning mdnsSkanning = new mDNSSkanning();
            mdnsSkanning.startEnhetsskanning("_ewelink._tcp.local.", 5);

            // Referanse til UI element i activity_main
            Button buttonWifi = findViewById(R.id.button_wifi);
            // Setter opp kode for håndtering av det å trykke på knappen
            // og da brukes en Intent objekt til å starte WiFi skanning
            buttonWifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, WiFiSkanning.class);
                    startActivity(intent);
                }
            });
            // Referanse til UI element i activity_main
            Button buttonBluetooth = findViewById(R.id.button_bluetooth);
            // Setter opp kode for håndtering av det å trykke på knappen
            // og da brukes en Intent objekt til å starte Bluetooth skanning
            buttonBluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BluetoothSkanning.class);
                    startActivity(intent);
                }
            });
        });
    }
}
