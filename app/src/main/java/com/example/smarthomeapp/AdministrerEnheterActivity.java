package com.example.smarthomeapp;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.smarthomeapp.integrering.skanning.BluetoothSkanning;
import com.example.smarthomeapp.integrering.skanning.mDNSSkanning;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.BluetoothEnhet;
import com.example.smarthomeapp.model.WiFiEnhet;
import javax.jmdns.ServiceEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * AdministrerEnheterActivity håndterer siden for administrasjon av enheter.
 */

public class AdministrerEnheterActivity extends AppCompatActivity {

    private ImageView logoutButton; // Bildeikon som fungerer som en logg ut knapp
    private Context context;
    ImageView imageView;
    BluetoothSkanning bluetoothSkanning;
    mDNSSkanning mdnsSkanning;
    DataKonvertering dataKonvertering;
    private LinearLayout linearLayout;

    public AdministrerEnheterActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrer_enheter);

        context = this;;

        imageView = findViewById(R.id.ic_soketterenheter);
        linearLayout = findViewById(R.id.linearLayoutDevices);
        dataKonvertering = new DataKonvertering(context);

        // TEST for visning av lagrede enheter
        //BluetoothEnhet btEnhet = new BluetoothEnhet(1, "Bluetooth Enhet 1");
        //dataKonvertering.leggTilJson(btEnhet,"Bluetooth enheter", BluetoothEnhet.class, "Data.json");

        // Skriver ut de lagrede WiFi enhetene etter at linearLayout er satt opp
        // og enheter er hentet fra Data.
        List<Object> hentBtEnheter = dataKonvertering.hentFraJson("Bluetooth enheter", BluetoothEnhet.class, "Data.json");
        List<Object> hentWifiEnheter = dataKonvertering.hentFraJson("WiFi enheter", WiFiEnhet.class, "Data.json");
        hentLagredeEnheter(hentBtEnheter);
        hentLagredeEnheter(hentWifiEnheter);

        // Initialiserer logoutButton fra layouten
        logoutButton = findViewById(R.id.logoutButton); // Finner logoutButton i layouten

        // Legger til en lytter for logOut knappen
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avslutter denne aktiviteten for å gå tilbake til hjemsiden
                finish();
            }
        });

        imageView.setOnClickListener(v -> {
            bluetoothSkanning = new BluetoothSkanning();
            List<BluetoothDevice> btEnheter = bluetoothSkanning.hentResultater();

            // Kan skanne etter flere protokoller enn http
            mdnsSkanning = new mDNSSkanning();
            mdnsSkanning.startEnhetsskanning("http", 5);
            List<ServiceEvent> wifiEnheter = mdnsSkanning.hentResultater();

            List<Object> WiFi_Bluetooth = new ArrayList<>();
            boolean funnetEnheter = false;

            if (btEnheter == null || btEnheter.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.scan_msg1), Toast.LENGTH_SHORT).show();
            } else {
                WiFi_Bluetooth.addAll(btEnheter);
                funnetEnheter = true;
            }
            if (wifiEnheter == null || wifiEnheter.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.scan_msg2), Toast.LENGTH_SHORT).show();
            } else {
                WiFi_Bluetooth.addAll(wifiEnheter);
                funnetEnheter = true;
            }

            if (funnetEnheter) {
                ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, WiFi_Bluetooth) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        }
                        Object enhet = getItem(position);
                        TextView textView = convertView.findViewById(android.R.id.text1);
                        textView.setTextSize(16);

                        if (enhet instanceof BluetoothDevice) {
                            BluetoothDevice btEnhet = (BluetoothDevice) enhet;
                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // Ingenting
                            }
                            String btNavn = btEnhet.getName() != null ? btEnhet.getName() : "Ukjent Bluetooth Enhet";
                            String btMAC = btEnhet.getAddress();
                            textView.setText(btNavn + "\n" + btMAC);
                        }
                        else if (enhet instanceof ServiceEvent) {
                            ServiceEvent wifiEnhet = (ServiceEvent) enhet;
                            String wifiEnhetNavn = wifiEnhet.getName() != null ? wifiEnhet.getName() : "Ukjent WiFi Enhet";
                            String wifiEnhetIP = wifiEnhet.getInfo().getInetAddress().getHostAddress();
                            textView.setText(wifiEnhetNavn + "\n" + wifiEnhetIP);
                        }
                        return convertView;
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Velg en enhet")
                        .setAdapter(adapter, (dialog, which) -> {
                            Object valgtEnhet = WiFi_Bluetooth.get(which);
                            if (valgtEnhet instanceof BluetoothDevice) {
                                BluetoothDevice valgtBtEnhet = (BluetoothDevice) valgtEnhet;
                                String enhetNavn = valgtBtEnhet.getName() != null ? valgtBtEnhet.getName() : "Ukjent Bluetooth Enhet";
                                Toast.makeText(getApplicationContext(), "Lagt til " + enhetNavn, Toast.LENGTH_SHORT).show();
                                dataKonvertering.leggTilJson(valgtBtEnhet, "Bluetooth enheter", BluetoothEnhet.class, "Data.json");
                            } else if (valgtEnhet instanceof ServiceEvent) {
                                ServiceEvent valgtWiFiEnhet = (ServiceEvent) valgtEnhet;
                                String enhetNavn = valgtWiFiEnhet.getName() != null ? valgtWiFiEnhet.getName() : "Ukjent WiFi Enhet";
                                Toast.makeText(getApplicationContext(), "Lagt til " + enhetNavn, Toast.LENGTH_SHORT).show();
                                dataKonvertering.leggTilJson(valgtWiFiEnhet, "WiFi enheter", WiFiEnhet.class, "Data.json");
                            }
                        }).setNegativeButton("Avbryt", null);

                builder.create().show();
            }
        });
    }

    private void hentLagredeEnheter(List<Object> enheter) {
        for (Object enhet : enheter) {
            String enhetNavn;
            if (enhet instanceof BluetoothEnhet) {
                BluetoothEnhet btDevice = (BluetoothEnhet) enhet;
                enhetNavn = btDevice.getEnhetNavn();
            } else if (enhet instanceof WiFiEnhet) {
                WiFiEnhet wifiDevice = (WiFiEnhet) enhet;
                enhetNavn = wifiDevice.getEnhetNavn();
            } else {
                enhetNavn = null;
            }
            if (enhetNavn == null) continue;

            LinearLayout enhetLayout = new LinearLayout(this);
            enhetLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams enhetLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            enhetLayout.setLayoutParams(enhetLayoutParams);
            enhetLayout.setPadding(0, 16, 0, 16);

            TextView textView = new TextView(this);
            textView.setText(enhetNavn);
            textView.setTextSize(16);
            textView.setPadding(0, 0, 16, 0);
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            textView.setLayoutParams(textViewParams);

            Button button = new Button(this);
            button.setText("Endre");
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B3E7FD")));
            button.setTextColor(Color.parseColor("#000000"));
            button.setOnClickListener(view -> {
                Toast.makeText(this, "Endre ikke implementert", Toast.LENGTH_SHORT).show();
            });

            enhetLayout.addView(textView);
            enhetLayout.addView(button);

            linearLayout.addView(enhetLayout);
        }
    }
}