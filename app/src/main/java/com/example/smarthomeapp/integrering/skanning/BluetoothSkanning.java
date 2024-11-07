package com.example.smarthomeapp.integrering.skanning;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.smarthomeapp.R;
import java.util.List;

public class BluetoothSkanning extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> resultater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_skann);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Registrer mottakeren for Bluetooth skanninger
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, intentFilter);

        // Sjekk om ACCESS_FINE_LOCATION er OK, og hvis det trengs tillatelse spør om det
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        } else {
            startBluetoothSkanning();
        }
    }
    private void startBluetoothSkanning() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {

            // Håndterer tilfellet at BLUETOOTH_SCAN tillatelsen ikke ble gitt
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Du mangler BLUETOOTH_SCAN tillatelsen");
                return;
            }
            // Søk etter Bluetooth enheter
            bluetoothAdapter.startDiscovery();
        } else {
            System.out.println("Bluetooth er ikke aktivert/tilgjengelig");
        }
    }
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice enhet = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Sjekker om enheten er en Bluetooth enhet, siden flere ting kan hende
                // som fører til at Bluetooth enheten fra en intent ikke blir lagt til ordentlig
                if (enhet != null) {
                    // Håndterer tilfellet at BLUETOOTH_CONNECT tillatelsen ikke ble gitt
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Du mangler BLUETOOTH_CONNECT tillatelsen");
                        return;
                    }
                    // Lagrer Bluetooth enheter som ble funnet
                    resultater.add(enhet);
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int kode, @NonNull String[] tillatelser, @NonNull int[] resultater) {
        super.onRequestPermissionsResult(kode, tillatelser, resultater);
        // Sjekker om det er minst ett element i listen av tillatelser, men også om det elementet
        // er det samme som PackageManger.PERMISSION_GRANTED
        // I dette tilfellet er det bare index 0 som er viktig siden vi har spurt om en tillatelse
        // og det er ACCESS_FINE_LOCATION
        if (kode == 2 && resultater.length > 0 && resultater[0] == PackageManager.PERMISSION_GRANTED) {
            startBluetoothSkanning();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
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