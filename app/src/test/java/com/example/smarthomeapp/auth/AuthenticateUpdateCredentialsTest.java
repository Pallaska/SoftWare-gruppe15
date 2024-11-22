package com.example.smarthomeapp.auth;

import com.example.smarthomeapp.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.*;

public class AuthenticateUpdateCredentialsTest {

    private Authenticate authenticate;
    private File tempDataFile;

    @Before
    public void setUp() throws Exception {
        // Oppretter en midlertidig fil med initial testdata
        tempDataFile = File.createTempFile("testData", ".json");

        // Hasher passord for initial bruker
        String hashedPassword = BCrypt.hashpw("passord123", BCrypt.gensalt());

        String testData = "{ \"brukere\": [ " +
                "{ \"brukerID\": 400, \"brukernavn\": \"Hans\", \"passord\": \"" + hashedPassword + "\", " +
                "\"fodselsdato\": \"15102000\", \"rettigheter\": \"12345\", \"adresse\": \"basestien 22\", " +
                "\"email\": \"hans@example.com\", \"telefon\": 34724923 }, " +
                "{ \"brukerID\": 401, \"brukernavn\": \"Anna\", \"passord\": \"" + BCrypt.hashpw("annaPass", BCrypt.gensalt()) + "\", " +
                "\"fodselsdato\": \"01011990\", \"rettigheter\": \"12345\", \"adresse\": \"noen gate 1\", " +
                "\"email\": \"anna@example.com\", \"telefon\": 12345678 } ] }";

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

    // ID 77 Test av vellykket oppdatering av brukernavn og passord
    @Test
    public void testSuccessfulUpdate() {
        boolean result = authenticate.updateCredentials(400, "passord123", "HansNytt", "nyttPassord123");
        assertTrue("Oppdatering bør lykkes med korrekte data", result);

        // Verifiserer at brukernavnet er oppdatert
        assertTrue(authenticate.getUsers().stream()
                .anyMatch(user -> user.getBrukerID() == 400 && user.getBrukernavn().equals("HansNytt")));

        // Verifiserer at det nye passordet er oppdatert
        User updatedUser = authenticate.getUsers().stream()
                .filter(user -> user.getBrukerID() == 400)
                .findFirst()
                .orElse(null);

        assertNotNull("Oppdatert bruker skal finnes", updatedUser);
        assertTrue("Passordet skal være oppdatert", BCrypt.checkpw("nyttPassord123", updatedUser.getPassord()));
    }

    // ID 77 Test for feil nåværende passord
    @Test
    public void testUpdateWithWrongCurrentPassword() {
        boolean result = authenticate.updateCredentials(400, "feilPassord", "HansNytt", "nyttPassord123");
        assertFalse("Oppdatering bør feile med feil nåværende passord", result);
    }

    // ID 77 Test for brukernavn som allerede eksisterer
    @Test
    public void testUpdateWithExistingUsername() {
        // Forsøker å endre Hans sitt brukernavn til Anna, som allerede eksisterer
        boolean result = authenticate.updateCredentials(400, "passord123", "Anna", "nyttPassord123");
        assertFalse("Oppdatering bør feile når det nye brukernavnet allerede er tatt", result);
    }

    // ID 77 Test for tomt brukernavn og passord
    @Test
    public void testUpdateWithEmptyUsernameAndPassword() {
        boolean result = authenticate.updateCredentials(400, "passord123", "", "");
        assertFalse("Oppdatering bør feile med tomt brukernavn og passord", result);
    }

    // ID 77 Test for null oppgitt
    @Test
    public void testUpdateWithNullValues() {
        boolean result = authenticate.updateCredentials(400, "passord123", null, null);
        assertFalse("Oppdatering bør feile når nye verdier er null", result);
    }
}
