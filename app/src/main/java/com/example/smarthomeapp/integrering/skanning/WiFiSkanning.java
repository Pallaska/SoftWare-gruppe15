package com.example.smarthomeapp.integrering.skanning;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.smarthomeapp.R;
import java.util.List;

public class WiFiSkanning extends AppCompatActivity {

    private WifiManager wifiManager;
    private List<ScanResult> resultater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_skann);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Registrer mottakeren for WiFi skanninger
        registerReceiver(wifiBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Sjekk om ACCESS_FINE_LOCATION er OK, og hvis det trengs tillatelse spør om det
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            startWiFiSkann();
        }
    }

    // Siden BroadcastReceiver er registrert i onCreate og logikken for håndtering
    // av resultater er i BroadcastReceiver, så er det ingen else-statement for å håndtere
    // wifiManager.startScan() som returnerer true
    private void startWiFiSkann() {
        if (!wifiManager.startScan()) {
            System.out.println("WiFi skann feilet");
        }
    }

    private final BroadcastReceiver wifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Håndterer tilfellet at tillatelse ikke ble gitt
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Feil: mangler ACCESS_FINE_LOCATION tillatelsen");
                return;
            }
            // Lagrer WiFi nettverk som ble funnet i en liste
            resultater = wifiManager.getScanResults();
        }
    };

    @Override
    public void onRequestPermissionsResult(int kode, @NonNull String[] tillatelser, @NonNull int[] resultater) {
        super.onRequestPermissionsResult(kode, tillatelser, resultater);
        // Sjekker om det er minst ett element i listen av tillatelser, men også om det elementet
        // er det samme som PackageManger.PERMISSION_GRANTED
        // I dette tilfellet er det bare index 0 som er viktig siden vi har spurt om en tillatelse
        // og det er ACCESS_FINE_LOCATION
        if (kode == 1 && resultater.length > 0 && resultater[0] == PackageManager.PERMISSION_GRANTED) {
            startWiFiSkann();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiBroadcastReceiver);
    }

    // Når listen med resultater hentes, så er det en ventetid på 5 sekunder
    // for å gi nok tid til skann metoden til å bli ferdig med søket
    // Dette er ikke den beste løsningen, skal forbedre det på et senere tidspunkt
    public List hentResultater() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resultater;
    }
}