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
import com.example.smarthomeapp.integrering.Tilkobling;
import com.example.smarthomeapp.integrering.skanning.BluetoothSkanning;
import com.example.smarthomeapp.integrering.skanning.WiFiSkanning;
import com.example.smarthomeapp.integrering.skanning.mDNSSkanning;
import com.example.smarthomeapp.integrering.skanning.mDNSSkanning;

import java.util.List;

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

        // Eksempel på integrering med smartenheter

        // Søk etter WiFi nettverk, siden klassen inneholder
        // onCreate metoden startes søket automatisk
        WiFiSkanning wifiSkanning = new WiFiSkanning();
        // Lagrer ssid av det første nettverket som ble funnet
        String ssid = wifiSkanning.hentResultater().get(0).SSID;

        // Tilkobling til nettverket
        Tilkobling tilkobling = new Tilkobling(this);
        // Bruker ssid som ble funnet til å koble til WiFi nettverket
        // Det er også mulig å legge inn et passord
        tilkobling.kobleTilWiFiNettverk(ssid, "passord");

        // Søk etter enheter på nettverket
        mDNSSkanning mdnsskanning = new mDNSSkanning();
        // Starter et søk etter http enheter med tidsavbrudd på 5 sekunder
        mdnsskanning.startEnhetsskanning("http", 5);
        // Henter alle http enhetene som ble funnet
        List httpEnheter = mdnsskanning.hentResultater();

        // Søk etter Bluetooth enheter, siden klassen inneholder
        // onCreate startes søket automatisk
        BluetoothSkanning bluetoothSkanning = new BluetoothSkanning();
        // Henter alle Bluetooth enheter som ble funnet
        List bluetoothEnheter = bluetoothSkanning.hentResultater();

        // Tilkobling til et http enhet
        String httpEnhetIP = httpEnheter.get(0).getInfo().getInetAddresses()[0].getHostAddress();
        int httpEnhetPort = httpEnheter.get(0).getInfo().getPort();
        tilkobling.kobleTilWiFiEnhet(httpEnhetIP, httpEnhetPort, "passord", "http");

        // Tilkobling til en Bluetooth enhet
        tilkobling.kobleTilBluetoothEnhet(bluetoothEnheter.get(0));

        // Sending av kommando til http enhet og Bluetooth enhet
        // Når kommandoen sendes til http enheten så forblir koblingen åpen
        tilkobling.sendKommandoTilEnhet("http", "kommando", false);
        // Når kommandoen sendes til Bluetooth enheten så lukker vi tilkoblingen
        tilkobling.sendKommandoTilEnhet("bluetooth", "kommando", true);

        // Koblingen til http enheten lukkes ved bruk av metode
        tilkobling.kobleFraWiFiEnhet();
    }
}
