package com.example.smarthomeapp;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccessControlActivity håndterer funksjonalitet for tilgangskontrollsiden i appen.
 * Brukeren kan vise en tilgangskode og logge ut via denne aktiviteten.
 */

public class AccessControlActivity extends AppCompatActivity {
    private TextView hiddenCodeField; // TextView som viser tilgangskoden (skjult som standard)
    private Button showAccessCodeButton; // Knapp for å vise tilgangskoden
    private ImageView logoutButton; // Bildeikon som fungerer som en logg ut knapp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilgangskontroll);

        // Initialiserer UI-elementene definert i XML-layouten
        hiddenCodeField = findViewById(R.id.hiddenCodeField);
        showAccessCodeButton = findViewById(R.id.showAccessCodeButton);
        logoutButton = findViewById(R.id.logoutButton); // Finner logoutButton

        // ImageView ID til BrukerID
        // Det er maks. 9 brukere på appen
        Map<Integer, Integer> imageViewBruker = new HashMap<>();
        imageViewBruker.put(R.id.person1, 1);
        imageViewBruker.put(R.id.person2, 2);
        imageViewBruker.put(R.id.person3, 3);
        imageViewBruker.put(R.id.person4, 4);
        imageViewBruker.put(R.id.person5, 5);
        imageViewBruker.put(R.id.person6, 6);
        imageViewBruker.put(R.id.person7, 7);
        imageViewBruker.put(R.id.person8, 8);
        imageViewBruker.put(R.id.person9, 9);

        // Laster inn profilbildene
        ImageView person1 = findViewById(R.id.person1);
        hentBilde(person1, imageViewBruker);
        ImageView person2 = findViewById(R.id.person2);
        hentBilde(person2, imageViewBruker);
        ImageView person3 = findViewById(R.id.person3);
        hentBilde(person3, imageViewBruker);
        ImageView person4 = findViewById(R.id.person4);
        hentBilde(person4, imageViewBruker);
        ImageView person5 = findViewById(R.id.person5);
        hentBilde(person5, imageViewBruker);
        ImageView person6 = findViewById(R.id.person6);
        hentBilde(person6, imageViewBruker);
        ImageView person7 = findViewById(R.id.person7);
        hentBilde(person7, imageViewBruker);
        ImageView person8 = findViewById(R.id.person8);
        hentBilde(person8, imageViewBruker);
        ImageView person9 = findViewById(R.id.person9);
        hentBilde(person9, imageViewBruker);

        showAccessCodeButton.setOnClickListener(v -> {
            // Viser en forhåndsdefinert tilgangskode i TextView
            hiddenCodeField.setText("1234-5678"); // Tallet er et eksempel på en tilgangskode
        });
        logoutButton.setOnClickListener(v -> {
            // Avslutter denne aktiviteten for å gå tilbake til hjemsiden
            finish();
        });
    }
    public void hentBilde(ImageView imageView, Map<Integer, Integer> imageBrukerView) {
        DataKonvertering dataKonvertering = new DataKonvertering(this);
        List<Object> brukerListe = dataKonvertering.hentFraJson("brukere", User.class, "Data.json");
        if (!brukerListe.isEmpty()) {
            for (Object brukerObjekt : brukerListe) {
                User bruker = (User) brukerObjekt;
                if (bruker.getBrukerID() == imageBrukerView.get(imageView.getId())) {
                    File file = new File(getFilesDir(), bruker.getBilde());

                    if (file.exists()) {
                        Uri bildeAdresse = Uri.fromFile(file);
                        imageView.setImageURI(bildeAdresse);
                    }
                }
            }
        }
    }
}