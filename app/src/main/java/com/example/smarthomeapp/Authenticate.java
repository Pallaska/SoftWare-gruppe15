package com.example.smarthomeapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Authenticate klassen håndterer validering av brukerinformasjon
 * ved bruk av data lagret i SharedPreferences.
 */

public class Authenticate {

    private Context context;

    public Authenticate(Context context) {
        this.context = context;
    }

    /**
     * Sjekker dataen i SharedPreferences og ser om det matcher.
     *
     * @param username Brukernavn som brukeren oppgir.
     * @param password Passord som brukeren oppgir.
     * @return true hvis brukernavn og passord samsvarer med lagrede data eller så blir det false.
     */

    public boolean validateLogin(String username, String password) {
        SharedPreferences preferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        String registeredUsername = preferences.getString("username", "");
        String registeredPassword = preferences.getString("password", "");

        // Sammenligner brukerinformasjonen med lagrede verdier
        return username.equals(registeredUsername) && password.equals(registeredPassword);
    }
}