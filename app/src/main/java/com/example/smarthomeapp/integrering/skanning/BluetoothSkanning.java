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
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.example.smarthomeapp.R;
public class BluetoothSkanning extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

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
                Toast.makeText(this, "Du mangler tillatelse", Toast.LENGTH_SHORT).show();
                return;
            }
            // Søk etter Bluetooth enheter
            bluetoothAdapter.startDiscovery();
        } else {
            Toast.makeText(this, "Bluetooth er ikke aktivert/tilgjengelig", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Du mangler tillatelse", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Presenterer Bluetooth enheter som ble funnet
                    Toast.makeText(context, "Bluetooth enheter som ble funnet: " + enhet.getName(), Toast.LENGTH_SHORT).show();
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
}