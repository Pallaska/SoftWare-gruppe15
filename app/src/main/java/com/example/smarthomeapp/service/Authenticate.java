package com.example.smarthomeapp.service;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.example.smarthomeapp.model.User;

// Klasse for autentisering, lasting av brukere fra json og validering
public class Authenticate {
    // Liste som lagrer data om brukere når de er lastet fra json
    private List<User> users = new ArrayList<>();

    // Laster brukerinformasjonen fra json filen
    public Authenticate(Context context) throws IOException {
        loadUsersFromJson(context);
    }

    // Laster brukere fra json og konverterer fra json-data til java-objekter
    private void loadUsersFromJson(Context context) throws IOException{
        try {
            // Åpner json
            InputStream inputStream = context.getAssets().open("data.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();

            // Definerer type for brukerliste
            Type userListType = new TypeToken<DataContainer>() {}.getType();

            // Konverterer json-data til DataContainer-objekt
            DataContainer dataContainer = gson.fromJson(reader, userListType);

            // Legger til brukere i listen
            if (dataContainer != null && dataContainer.brukere != null) {
                users.addAll(dataContainer.brukere);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Få listen over brukere
    public List<User> getUsers() {
        return users;
    }

    // Legge til bruker
    public void addUser(User user) {
        users.add(user);
    }

    // Metode for å validere innlogging. Returnerer true om brukernavn og passord matcher
    public boolean validateLogin(String username, String password) {
        for (User user : users) {
            if (user.getBrukernavn().equals(username) && user.getPassord().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Klasse for å holde json-data
    public static class DataContainer {
        List<User> brukere;
    }

    // Metode for endring av brukernavn og passord
    public boolean updateCredentials(int brukerID, String newUsername, String newPassword) {
        for (User user : users) {
            if (user.getBrukerID() == brukerID) {
                user.setBrukernavn(newUsername);
                user.setPassord(newPassword);
                return true;
            }
        }
        return false;
    }
}


