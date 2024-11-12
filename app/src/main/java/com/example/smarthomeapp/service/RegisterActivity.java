package com.example.smarthomeapp.service;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smarthomeapp.R;
import com.example.smarthomeapp.MainActivity;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Få referanse til fødselsdato-feltet
        EditText birthdateField = findViewById(R.id.birthdate);

        // Åpne DatePickerDialog når fødselsdato-feltet klikkes
        birthdateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hent dagens dato som startverdi for DatePicker
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Åpne DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Formater datoen som dd/mm/åååå
                            String date = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1);
                            birthdateField.setText(date); // Sett valgt dato i EditText
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Gå til innloggingssiden hvis "Har du allerede en bruker?" trykkes
        TextView alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Går tilbake til MainActivity (innloggingssiden)
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Lukker registreringssiden for å hindre at brukeren kan gå tilbake hit med "tilbake"-knappen
            }
        });
    }
}
