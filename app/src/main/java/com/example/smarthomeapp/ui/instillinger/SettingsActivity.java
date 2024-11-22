package com.example.smarthomeapp;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.smarthomeapp.json.DataBehandling;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.Instillinger;
import com.example.smarthomeapp.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * SettingsActivity håndterer innstillingersiden for appen.
 */

public class SettingsActivity extends AppCompatActivity {
    private Context context;

    // Knapp for å navigere tilbake til forrige aktivitet
    private ImageView backButton;
    private ImageView endreProfilbilde;
    private ImageView endrePassord;

    // Knapp for å navigere til tilgangskontrollsiden
    private ImageView tilgangskontrollImageView;

    // Knapp for å endre språket
    private Button changeLanguageButton;

    private Button manageDevicesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innstillinger);

        context = this;

        // Initialiserer backButton fra layouten
        backButton = findViewById(R.id.logoutButton);
        // Legger til en OnClickListener på backButton for å gå tilbake til hjemsiden
        backButton.setOnClickListener(v -> finish());

        // For å åpne bilder på mobilen
        endreProfilbilde = findViewById(R.id.endreProfilbilde);
        endreProfilbilde.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        endrePassord = findViewById(R.id.endrePassord);
        endrePassord.setOnClickListener(v -> {
            EditText passordFelt = new EditText(this);
            passordFelt.setHint("Din passord");
            passordFelt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            EditText nyPassordFelt = new EditText(this);
            nyPassordFelt.setHint("Ny passord");
            nyPassordFelt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(50, 0, 50, 0);

            linearLayout.addView(passordFelt);
            linearLayout.addView(nyPassordFelt);

            new AlertDialog.Builder(this)
                    .setTitle("Endre passord")
                    .setView(linearLayout)
                    .setPositiveButton("Lagre", (dialog, which) -> {
                        String passord = passordFelt.getText().toString();
                        String nyPassord = nyPassordFelt.getText().toString();

                        if (!passord.isEmpty() && !nyPassord.isEmpty()) {
                            DataBehandling dataBehandling = new DataBehandling(this);
                            dataBehandling.endreJson("brukere", User.class, passord, nyPassord, "Data.json");
                            Toast.makeText(this, getString(R.string.settings_msg2), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getString(R.string.settings_msg3), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(getString(R.string.settings_msg4), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        // Initialiserer tilgangskontrollImageView fra layouten
        tilgangskontrollImageView = findViewById(R.id.tilgangskontrollImageView);
        // Legger til en OnClickListener på tilgangskontrollImageView for å åpne tilgangskontrollsiden
        tilgangskontrollImageView.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AccessControlActivity.class);
            startActivity(intent);
        });

        manageDevicesButton = findViewById(R.id.manageDevicesButton);
        manageDevicesButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AdministrerEnheterActivity.class);
            startActivity(intent);
        });

        // Initialiserer button fra res/layout
        changeLanguageButton = findViewById(R.id.changeLanguageButton);
        changeLanguageButtonText();
        // Legger til en OnClickListener for å endre språket
        changeLanguageButton.setOnClickListener(v -> {
            DataBehandling dataBehandling = new DataBehandling(context);
            DataKonvertering dataKonvertering = new DataKonvertering(context);

            // Hvilket språk som skal legges til i JSON
            // Hvis "NO" er i bruk og språket byttes, så blir det "EN"
            // Og hvis "EN" er i bruk og språket byttes, så blir det "NO"
            String languageCode = "Error";
            if (Objects.equals(MainActivity.language, "no")) {
                languageCode = "en";
            } else if (Objects.equals(MainActivity.language, "en")) {
                languageCode = "no";
            }
            List<Object> instillingerListe = dataKonvertering.hentFraJson("instillinger", Instillinger.class, "Data.json");
            // Hvis listen ikke er tom endres det eksisterende språket med det som ble valgt
            if (!instillingerListe.isEmpty()) {
                dataBehandling.endreJson("instillinger", Instillinger.class, MainActivity.language, languageCode, "Data.json");
            } else {
                // Hvis listen er tom, legges det til det språket som ble valgt
                Instillinger instillinger = new Instillinger("undefined", languageCode, "undefined");
                dataKonvertering.leggTilJson(instillinger, "instillinger", Instillinger.class, "Data.json");
            }
            // Restarter MainActivity for å oppdatere språket
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        });
    }
    public void changeLanguageButtonText() {
        // Setter teksten til knappen til det språket som
        // ikke er valgt for å vise hva språket skal byttes til
        // ved bruk av språk knappen
        if (Objects.equals(MainActivity.language, "no")) {
            changeLanguageButton.setText("ENG");
        } else if (Objects.equals(MainActivity.language, "en")) {
            changeLanguageButton.setText("NOR");
        }
    }

    // Når brukeren velger et bilde så kalles lagreBilde metoden
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent bildeData) {
        super.onActivityResult(reqCode, resCode, bildeData);

        if (reqCode == 100 && resCode == RESULT_OK && bildeData != null) {
            Uri valgtBilde = bildeData.getData();
            lagreBilde(valgtBilde);
        }
    }

    private void lagreBilde(Uri bildeAdresse) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(bildeAdresse);
            String fileName = "bilde_" + System.currentTimeMillis() + ".png";
            FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close();
            inputStream.close();

            DataKonvertering dataKonvertering = new DataKonvertering(this);
            DataBehandling dataBehandling = new DataBehandling(this);

            List<Object> brukerListe = dataKonvertering.hentFraJson("brukere", User.class, "Data.json");
            if (!brukerListe.isEmpty()) {
                for (Object brukerObjekt : brukerListe) {
                    User bruker = (User) brukerObjekt;
                    if (Objects.equals(bruker.getBrukernavn(), MainActivity.loggedInUser)) {
                        bruker.setBilde(fileName);
                        dataBehandling.slettFraJson(bruker, "brukere", User.class, "Data.json");
                        dataKonvertering.leggTilJson(bruker, "brukere", User.class, "Data.json");
                    }
                }
            }
            Toast.makeText(this, getString(R.string.settings_msg1), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
