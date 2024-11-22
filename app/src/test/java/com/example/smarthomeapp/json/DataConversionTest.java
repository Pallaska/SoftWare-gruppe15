package com.example.smarthomeapp.json;

import com.example.smarthomeapp.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class DataConversionTest {

    private DataKonvertering dataKonvertering;
    private File tempDataFile;

    @Before
    public void setUp() throws Exception {
        dataKonvertering = new DataKonvertering();
        tempDataFile = File.createTempFile("testData", ".json");
    }

    @After
    public void tearDown() {
        if (tempDataFile != null && tempDataFile.exists()) {
            tempDataFile.delete();
        }
    }

    // ID 101 Test for om brukere kan leses fra JSON-filen
    @Test
    public void testReadUsersFromJsonFile() throws IOException {
        String jsonContent = "{ \"brukere\": [ " +
                "{ \"brukerID\": 1, \"brukernavn\": \"TestUser\", \"passord\": \"pass123\", " +
                "\"fodselsdato\": \"01012000\", \"rettigheter\": \"admin\", \"email\": \"test@example.com\", " +
                "\"adresse\": \"Testveien 1\", \"telefon\": 12345678 } ] }";

        try (FileWriter writer = new FileWriter(tempDataFile)) {
            writer.write(jsonContent);
        }

        List<User> users = dataKonvertering.hentFraJson("brukere", User.class, tempDataFile.getAbsolutePath());
        assertNotNull("Brukerlisten skal ikke være null", users);
        assertEquals("Det skal være én bruker i listen", 1, users.size());

        User user = users.get(0);
        assertEquals("Brukernavnet skal være 'TestUser'", "TestUser", user.getBrukernavn());
    }

    // ID 101 Test for håndtering av feil format i JSON-filen
    @Test
    public void testHandleIncorrectJsonFormat() throws IOException {
        String invalidJsonContent = "{ \"brukere\": [ { \"brukerID\": 1, "; // Ufullstendig JSON

        try (FileWriter writer = new FileWriter(tempDataFile)) {
            writer.write(invalidJsonContent);
        }

        List<User> users = dataKonvertering.hentFraJson("brukere", User.class, tempDataFile.getAbsolutePath());
        assertTrue("Brukerlisten skal være tom ved feil format", users.isEmpty());
    }

    // ID 101 Test for at brukere kan legges til i JSON-filen
    @Test
    public void testAddUserToJsonFile() throws IOException {
        String jsonContent = "{ \"brukere\": [] }";

        try (FileWriter writer = new FileWriter(tempDataFile)) {
            writer.write(jsonContent);
        }

        User newUser = new User(2, "NewUser", "newPass123", "02022000", "user",
                "new@example.com", "Nyveien 2", 87654321);

        dataKonvertering.leggTilJson(newUser, "brukere", User.class, tempDataFile.getAbsolutePath());

        List<User> users = dataKonvertering.hentFraJson("brukere", User.class, tempDataFile.getAbsolutePath());
        assertEquals("Det skal være én bruker i listen etter å ha lagt til en bruker", 1, users.size());

        User user = users.get(0);
        assertEquals("Brukernavnet til den nye brukeren skal være 'NewUser'", "NewUser", user.getBrukernavn());
    }
}
