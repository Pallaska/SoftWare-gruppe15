package com.example.smarthomeapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.*;

public class AuthenticateLoginTest {

    private Authenticate authenticate;
    private File tempDataFile;

    @Before
    public void setUp() throws Exception {
        // Oppretter en midlertidig fil med testdata
        tempDataFile = File.createTempFile("testData", ".json");

        // Hasher passordet for å simulere lagrede passord
        String hashedPassword = BCrypt.hashpw("passord123", BCrypt.gensalt());

        String testData = "{ \"brukere\": [ " +
                "{ \"brukerID\": 400, \"brukernavn\": \"Hans\", \"passord\": \"" + hashedPassword + "\", " +
                "\"fodselsdato\": \"15102000\", \"rettigheter\": \"12345\", \"adresse\": \"basestien 22\", " +
                "\"email\": \"testmail@live.no\", \"telefon\": 34724923 } ] }";

        try (FileWriter writer = new FileWriter(tempDataFile)) {
            writer.write(testData);
        }

        // Initialiserer Authenticate med den midlertidige filen
        authenticate = new Authenticate(tempDataFile.getAbsolutePath());
    }

    @After
    public void tearDown() {
        // Sletter den midlertidige filen etter testene
        if (tempDataFile != null && tempDataFile.exists()) {
            tempDataFile.delete();
        }
    }

    // Test som sjekker respons for korrekt brukernavn og passord
    @Test
    public void testLoginWithCorrectCredentials() {
        boolean result = authenticate.validateLogin("Hans", "passord123");
        assertTrue("Innlogging bør være gyldig med riktig brukernavn og passord", result);
    }

    // Test som sjekker respons for korrekt brukernavn, men feil passord
    @Test
    public void testLoginWithCorrectUsernameWrongPassword() {
        boolean result = authenticate.validateLogin("Hans", "feilPassord");
        assertFalse("Innlogging bør være ugyldig med riktig brukernavn men feil passord", result);
    }

    // Test som sjekker respons for feil brukernavn, men korrekt passord
    @Test
    public void testLoginWithWrongUsernameCorrectPassword() {
        boolean result = authenticate.validateLogin("FeilBrukernavn", "passord123");
        assertFalse("Innlogging bør være ugyldig med feil brukernavn men riktig passord", result);
    }

    // Test som sjekker etter null-verdi i brukernavn og passord
    @Test
    public void testLoginWithNullValues() {
        boolean result = authenticate.validateLogin(null, null);
        assertFalse("Innlogging bør være ugyldig når brukernavn og passord er null", result);

        result = authenticate.validateLogin("Hans", null);
        assertFalse("Innlogging bør være ugyldig når passord er null", result);

        result = authenticate.validateLogin(null, "passord123");
        assertFalse("Innlogging bør være ugyldig når brukernavn er null", result);
    }

    // Test som sjekker etter tomme strenger i brukernavn og passord
    @Test
    public void testLoginWithEmptyStrings() {
        boolean result = authenticate.validateLogin("", "");
        assertFalse("Innlogging bør være ugyldig når brukernavn og passord er tomme strenger", result);

        result = authenticate.validateLogin("Hans", "");
        assertFalse("Innlogging bør være ugyldig når passord er en tom streng", result);

        result = authenticate.validateLogin("", "passord123");
        assertFalse("Innlogging bør være ugyldig når brukernavn er en tom streng", result);
    }

    // Test som sjekker case-sensitivitet i passord
    @Test
    public void testPasswordCaseSensitivity() {
        // Passord med feil case
        boolean result = authenticate.validateLogin("Hans", "Passord123");
        assertFalse("Innlogging bør være ugyldig når passordet har feil case", result);

        result = authenticate.validateLogin("Hans", "PASSORD123");
        assertFalse("Innlogging bør være ugyldig når passordet har feil case", result);
    }

    // Test som sjekker case-sensitivitet i brukernavn
    @Test
    public void testUsernameCaseSensitivity() {
        // Brukernavn med feil case
        boolean result = authenticate.validateLogin("hans", "passord123");
        assertFalse("Innlogging bør være ugyldig når brukernavnet har feil case", result);

        result = authenticate.validateLogin("HANS", "passord123");
        assertFalse("Innlogging bør være ugyldig når brukernavnet har feil case", result);
    }
}

