package com.example.smarthomeapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.json.DataBehandling;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.BluetoothEnhet;
import com.example.smarthomeapp.model.WiFiEnhet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * HomeActivity fungerer som hjemsiden i appen.
 * Brukeren kan logge ut, administrere enheter eller gå til innstillinger.
 */

public class HomeActivity extends AppCompatActivity {
    private static final Logger log = LoggerFactory.getLogger(HomeActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DataKonvertering dataKonvertering = new DataKonvertering(this);
        DataBehandling dataBehandling = new DataBehandling(this);

        // Listene som brukes i håndtering av "Belysning" og "Adgang"
        List<Object> hentedeWiFiEnheter = dataKonvertering.hentFraJson("WiFi enheter", WiFiEnhet.class, "Data.json");
        List<Object> hentedeBluetoothEnheter = dataKonvertering.hentFraJson("Bluetooth enheter", BluetoothEnhet.class, "Data.json");
        List<String> steder = Arrays.asList("bedroom", "bathroom", "entrance", "garden", "front door", "back door", "garage", "safe");

        // Map for å sjekke om sted passer med switch
        Map<Integer, String> switchButtonArea = new HashMap<>();
        switchButtonArea.put(R.id.bedroomSwitch, "bedroom");
        switchButtonArea.put(R.id.bathroomSwitch, "bathroom");
        switchButtonArea.put(R.id.entranceSwitch, "entrance");
        switchButtonArea.put(R.id.gardenSwitch, "garden");
        switchButtonArea.put(R.id.entranceDoorButton, "entrance door");
        switchButtonArea.put(R.id.backDoorButton, "back door");
        switchButtonArea.put(R.id.garageButton, "garage");
        switchButtonArea.put(R.id.safeButton, "safe");

        // Map for å sjekke funksjonen til stedet
        Map<String, String> areaFunction = new HashMap<>();
        areaFunction.put("bedroom", "lightning");
        areaFunction.put("bathroom", "lightning");
        areaFunction.put("entrance", "lightning");
        areaFunction.put("garden", "lightning");
        areaFunction.put("front door", "access");
        areaFunction.put("back door", "access");
        areaFunction.put("garage", "access");
        areaFunction.put("safe", "access");

        TextView headerTitle = findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.home_title);

        // Initialiserer logoutButton fra layouten
        ImageView logoutButton = findViewById(R.id.logoutButton);

        // Belysning, Smart TV, Vaskemaskin
        ImageView category1 = findViewById(R.id.category1);
        category1.setContentDescription(getString(R.string.home_category1));
        ImageView category2 = findViewById(R.id.category2);
        category2.setContentDescription(getString(R.string.home_category2));
        ImageView category3 = findViewById(R.id.category3);
        category3.setContentDescription(getString(R.string.home_category3));

        // Belysning
        TextView lightningTitle = findViewById(R.id.lightingTitle);
        lightningTitle.setText(R.string.home_category1);

        // Bedroom
        TextView bedroomTitle = findViewById(R.id.bedroomTitle);
        bedroomTitle.setText(R.string.home_place1);
        Switch bedroomSwitch = findViewById(R.id.bedroomSwitch);
        bedroomSwitch.setChecked(false);
        bedroomSwitch.setOnClickListener(v -> {
            bedroomSwitch.setChecked(false);
            switchButtonAction(steder, hentedeWiFiEnheter, switchButtonArea,
                    areaFunction, hentedeBluetoothEnheter, v);
        });

        // Bathroom
        TextView bathroomTitle = findViewById(R.id.bathroomTitle);
        bathroomTitle.setText(R.string.home_place2);
        Switch bathroomSwitch = findViewById(R.id.bathroomSwitch);
        bathroomSwitch.setChecked(false);
        bathroomSwitch.setOnClickListener(v -> {
            bathroomSwitch.setChecked(false);
            switchButtonAction(steder, hentedeWiFiEnheter, switchButtonArea,
                    areaFunction, hentedeBluetoothEnheter, v);
        });

        // Entrance
        TextView entranceTitle = findViewById(R.id.entranceTitle);
        entranceTitle.setText(R.string.home_place3);
        Switch entranceSwitch = findViewById(R.id.entranceSwitch);
        entranceSwitch.setChecked(false);
        entranceSwitch.setOnClickListener(v -> {
            entranceSwitch.setChecked(false);
            switchButtonAction(steder, hentedeWiFiEnheter, switchButtonArea,
                    areaFunction, hentedeBluetoothEnheter, v);
        });

        // Garden
        TextView gardenTitle = findViewById(R.id.gardenTitle);
        gardenTitle.setText(R.string.home_place4);
        Switch gardenSwitch = findViewById(R.id.gardenSwitch);
        gardenSwitch.setChecked(false);
        gardenSwitch.setOnClickListener(v -> {
            gardenSwitch.setChecked(false);
            switchButtonAction(steder, hentedeWiFiEnheter, switchButtonArea,
                    areaFunction, hentedeBluetoothEnheter, v);
        });

        // Adgang
        TextView accessTitle = findViewById(R.id.accessTitle);
        accessTitle.setText(R.string.home_function1);

        // Front door
        TextView entranceDoorTitle = findViewById(R.id.entranceDoorTitle);
        entranceDoorTitle.setText(R.string.home_function2);
        Button entranceDoorButton = findViewById(R.id.entranceDoorButton);
        entranceDoorButton.setText(R.string.home_closed);
        entranceDoorButton.setOnClickListener(v -> {
            switchButtonAction(steder, hentedeWiFiEnheter, switchButtonArea,
                    areaFunction, hentedeBluetoothEnheter, v);
        });

        // Back door
        TextView backDoorTitle = findViewById(R.id.backDoorTitle);
        backDoorTitle.setText(R.string.home_function3);
        Button backDoorButton = findViewById(R.id.backDoorButton);
        backDoorButton.setText(R.string.home_closed);
        backDoorButton.setOnClickListener(v -> {
            switchButtonAction(steder, hentedeWiFiEnheter, switchButtonArea,
                    areaFunction, hentedeBluetoothEnheter, v);
        });

        // Garage
        TextView garageTitle = findViewById(R.id.garageTitle);
        garageTitle.setText(R.string.home_function4);
        Button garageButton = findViewById(R.id.garageButton);
        garageButton.setText(R.string.home_closed);
        garageButton.setOnClickListener(v -> {
            switchButtonAction(steder, hentedeWiFiEnheter, switchButtonArea,
                    areaFunction, hentedeBluetoothEnheter, v);
        });

        // Safe
        TextView safeTitle = findViewById(R.id.safeTitle);
        safeTitle.setText(R.string.home_function5);
        Button safeButton = findViewById(R.id.safeButton);
        safeButton.setText(R.string.home_closed);
        safeButton.setOnClickListener(v -> {
            switchButtonAction(steder, hentedeWiFiEnheter, switchButtonArea,
                    areaFunction, hentedeBluetoothEnheter, v);
        });

        // Setter en OnClickListener på logoutButton for å logge ut brukeren
        logoutButton.setOnClickListener(v -> {
            // Navigerer til innlogginsskjermen
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Initialiserer manageDevicesButton fra layouten
        Button manageDevicesButton = findViewById(R.id.manageDevicesButton);
        manageDevicesButton.setText(R.string.home_button);

        // Setter en OnClickListener på manageDevicesButton for å navigere til administrasjonssiden
        manageDevicesButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AdministrerEnheterActivity.class);
            startActivity(intent);
        });

        // Initialiserer settingsIcon fra layouten
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Setter en OnClickListener på settingsIcon for å navigere til innstillingersiden
        settingsIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
    // Brukes av switch og buttons til å sende kommandoer til enheter
    public void switchButtonAction(List<String> steder, List<Object> hentedeWiFiEnheter, Map<Integer, String> switchButtonArea,
                             Map<String, String> areaFunction, List<Object> hentedeBluetoothEnheter, View v) {
        // Ingen enheter er tilgjengelige hvis noDevices = 2
        int noMatchingDevices = 0;
        // Ingen enheter finnes hvis det er true
        boolean noWiFiDevices = true;
        boolean noBluetoothDevices = true;
        for (String sted : steder) {
            for (Object hentetEnhet : hentedeWiFiEnheter) {
                WiFiEnhet enhet = (WiFiEnhet) hentetEnhet;
                if (Objects.equals(enhet.getEnhetSted(), sted) && Objects.equals(switchButtonArea.get(v.getId()), sted)) {
                    noWiFiDevices = false;
                    if (Objects.equals(areaFunction.get(sted), "lightning")) {
                        if (v instanceof Switch) {
                            Switch switch_b = (Switch) v;
                            if (switch_b.isChecked()) {
                                switch_b.setChecked(false);
                                // Send kommando: slå av lyset i {sted}
                                Toast.makeText(HomeActivity.this, getString(R.string.home_command), Toast.LENGTH_SHORT).show();
                            } else {
                                switch_b.setChecked(true);
                                // Send kommando: slå på lyset i {sted}
                                Toast.makeText(HomeActivity.this, getString(R.string.home_command), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (v instanceof Button) {
                            Button button = (Button) v;
                            if (button.getText().equals(getString(R.string.home_open))) {
                                button.setText(getString(R.string.home_closed));
                                // Send kommando: fjern tilgang til {sted}
                                Toast.makeText(HomeActivity.this, getString(R.string.home_command), Toast.LENGTH_SHORT).show();
                            } else {
                                button.setText(getString(R.string.home_open));
                                // Send kommando: gi tilgang til {sted}
                                Toast.makeText(HomeActivity.this, getString(R.string.home_command), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    noMatchingDevices++;
                }
            }
            for (Object hentetEnhet : hentedeBluetoothEnheter) {
                BluetoothEnhet enhet = (BluetoothEnhet) hentetEnhet;
                if (Objects.equals(enhet.getEnhetSted(), sted) && Objects.equals(switchButtonArea.get(v.getId()), sted)) {
                    noBluetoothDevices = false;
                    if (Objects.equals(areaFunction.get(sted), "lightning")) {
                        if (v instanceof Switch) {
                            Switch switch_b = (Switch) v;
                            if (switch_b.isChecked()) {
                                switch_b.setChecked(false);
                                // Send kommando: slå av lyset i {sted}
                                Toast.makeText(HomeActivity.this, getString(R.string.home_command), Toast.LENGTH_SHORT).show();
                            } else {
                                switch_b.setChecked(true);
                                // Send kommando: slå på lyset i {sted}
                                Toast.makeText(HomeActivity.this, getString(R.string.home_command), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (v instanceof Button) {
                            Button button = (Button) v;
                            if (button.getText().equals(getString(R.string.home_open))) {
                                button.setText(getString(R.string.home_closed));
                                // Send kommando: fjern tilgang til {sted}
                                Toast.makeText(HomeActivity.this, getString(R.string.home_command), Toast.LENGTH_SHORT).show();
                            } else {
                                button.setText(getString(R.string.home_open));
                                // Send kommando: gi tilgang til {sted}
                                Toast.makeText(HomeActivity.this, getString(R.string.home_command), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    noMatchingDevices++;
                }
            }
        }
        if (noWiFiDevices && noBluetoothDevices) {
            Toast.makeText(HomeActivity.this, getString(R.string.home_msg2), Toast.LENGTH_SHORT).show();
        } else if (noMatchingDevices == 2) {
            Toast.makeText(HomeActivity.this, getString(R.string.home_msg1), Toast.LENGTH_SHORT).show();
        }
    }
}
