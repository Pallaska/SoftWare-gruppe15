package com.example.smarthomeapp;

import java.util.HashMap;

public class Authenticate {
    // Kun midlertidig for å vise funksjonalitet, skal byttes ut med databasen
    private HashMap<String, String> users = new HashMap<>();

    // Kun midlertidig for å vise funksjonalitet, skal hentes fra databasen senere
    public Authenticate() {
        users.put("bruker1", "passord1");
        users.put("bruker2", "passord2");
    }

    // Metode for å validere innlogging
    public boolean validateLogin(String username, String password) {
        if (users.containsKey(username)) {
            return users.get(username).equals(password);
        }
        return false;
    }
}
