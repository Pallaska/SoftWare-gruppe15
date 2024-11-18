package com.example.smarthomeapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Authenticate {

    private Context context;

    public Authenticate(Context context) {
        this.context = context;
    }

    public boolean validateLogin(String username, String password) {
        SharedPreferences preferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        String registeredUsername = preferences.getString("username", "");
        String registeredPassword = preferences.getString("password", "");

        return username.equals(registeredUsername) && password.equals(registeredPassword);
    }
}