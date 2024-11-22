package com.example.smarthomeapp.auth;
import android.content.Context;
import com.example.smarthomeapp.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.Assert.*;

public class AuthenticateDataTest {

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
        //authenticate = new Authenticate(this);
    }

    @After
    public void tearDown() {
        // Sletter den midlertidige filen etter testene
        if (tempDataFile != null && tempDataFile.exists()) {
            tempDataFile.delete();
        }
    }

    // ID 95 Tester om antall brukere som lastes inn stemmer
    @Test
    public void testLoadUsersFromJson() {
        int expectedUserCount = 1; // Satt til 1, ettersom test-databasen kun har det
        List<Object> users = authenticate.getUsers();
        assertEquals("Forventet antall brukere stemmer ikke", expectedUserCount, users.size());
    }


    // ID 95 Tester om en bruker eksisterer, og om brukerdataen er korrekt
    @Test
    public void testUserDataIntegrity() {
        List<Object> users = authenticate.getUsers();

        User hans = null;
        for (Object user : users) {
            User u = (User) user;
            if (u.getBrukerID() == 400) {
                hans = u;
                break;
            }
        }

        assertNotNull("Bruker med ID 400 skal finnes", hans);
        assertEquals("Brukernavnet skal være 'Hans'", "Hans", hans.getBrukernavn());
        assertTrue("Passordet skal være 'passord123'", BCrypt.checkpw("passord123", hans.getPassord()));
        assertEquals("Fødselsdato skal være '15102000'", "15102000", hans.getFodselsdato());
        assertEquals("Rettigheter skal være '12345'", "12345", hans.getRettigheter());
        assertEquals("Adresse skal være 'basestien 22'", "basestien 22", hans.getAdresse());
        assertEquals("E-posten skal være 'testmail@live.no'", "testmail@live.no", hans.getEmail());
        assertEquals("Telefonnummer skal være 34724923", 34724923, hans.getTelefon());
    }



    // ID 95 Tester håndtering når filen ikke finnes
    @Test
    public void testLoadUsersFromJson_FileNotFound() {
        // Initialiserer Authenticate med en ikke-eksisterende fil
        //authenticate = new Authenticate("nonexistent.json");

        //List<User> users = authenticate.getUsers();
        //assertTrue("Brukerlisten skal være tom når filen ikke finnes", users.isEmpty());
    }

    // ID 95 Tester håndtering av korrupt JSON fil
    @Test
    public void testLoadUsersFromJson_CorruptedJson() throws Exception {
        // Oppretter en midlertidig fil med korrupt JSON
        File corruptedFile = File.createTempFile("corrupted", ".json");
        try (FileWriter writer = new FileWriter(corruptedFile)) {
            writer.write("{ \"brukere\": [ { \"brukerID\": 400, ");
        }

        //uthenticate = new Authenticate(corruptedFile.getAbsolutePath());

        //List<User> users = authenticate.getUsers();
        //assertTrue("Brukerlisten skal være tom når JSON er korrupt", users.isEmpty());

        // Sletter den midlertidige filen
        corruptedFile.delete();
    }
}
