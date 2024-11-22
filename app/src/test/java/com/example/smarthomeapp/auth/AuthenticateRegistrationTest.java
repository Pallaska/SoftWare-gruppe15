package com.example.smarthomeapp.auth;
import com.example.smarthomeapp.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import java.io.File;
import java.io.FileWriter;
import static org.junit.Assert.*;

public class AuthenticateRegistrationTest {

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
                "\"email\": \"hans@example.com\", \"telefon\": 34724923 } ] }";

        try (FileWriter writer = new FileWriter(tempDataFile)) {
            writer.write(testData);
        }

        // Initialiserer Authenticate med den midlertidige filen
        //authenticate = new Authenticate(tempDataFile.getAbsolutePath());
    }

    @After
    public void tearDown() {
        // Sletter den midlertidige filen etter testene
        if (tempDataFile != null && tempDataFile.exists()) {
            tempDataFile.delete();
        }
    }

    // ID 66 Test for korrekt registrering av bruker
    @Test
    public void testSuccessfulRegistration() {
        User newUser = new User(401, "Anna", "securePass123", "01011990", "12345",
                "anna@example.com", "Noen Gate 1", 12345678);
        //boolean result = authenticate.addUser(newUser);
        //assertTrue("Registrering bør lykkes med gyldige data", result);

        // Verifiserer at brukeren er lagt til
        //assertTrue(authenticate.getUsers().stream().anyMatch(user -> user.getBrukernavn().equals("Anna")));
    }

    // ID 66 Test for registrering med ugyldig e-post
    @Test
    public void testRegistrationWithInvalidEmail() {
        User newUser = new User(402, "Bob", "securePass123", "02021990", "12345",
                "ugyldig-epost", "Noen Gate 2", 87654321);
        //boolean result = authenticate.addUser(newUser);
        //assertFalse("Registrering bør feile med ugyldig e-post", result);
    }

    // ID 66 Test med for svakt passord (for kort)
    @Test
    public void testRegistrationWithShortPassword() {
        User newUser = new User(403, "Charlie", "123", "03031990", "12345",
                "charlie@example.com", "Noen Gate 3", 12348765);
        //boolean result = authenticate.addUser(newUser);
        //assertFalse("Registrering bør feile med for kort passord", result);
    }

    // ID 66 Test med for langt passord
    @Test
    public void testRegistrationWithLongPassword() {
        User newUser = new User(404, "Dave", "DettePassordetErAltForLangtTilÅGodtas12345", "04041990", "12345",
                "dave@example.com", "Noen Gate 4", 87651234);
        //boolean result = authenticate.addUser(newUser);
        //assertFalse("Registrering bør feile med for langt passord", result);
    }

    // ID 66 Test med for langt brukernavn
    @Test
    public void testRegistrationWithLongUsername() {
        User newUser = new User(405, "VeldigLangtBrukernavnSomOverskriderGrensen", "securePass123", "05051990", "12345",
                "langtbrukernavn@example.com", "Noen Gate 5", 11223344);
        //boolean result = authenticate.addUser(newUser);
        //assertFalse("Registrering bør feile med for langt brukernavn", result);
    }

    // ID 66 Test for registrering av eksisterende brukernavn
    @Test
    public void testRegistrationWithExistingUsername() {
        User newUser = new User(406, "Hans", "anotherPass123", "06061990", "12345",
                "annenhans@example.com", "Noen Gate 6", 44332211);
        //boolean result = authenticate.addUser(newUser);
        //assertFalse("Registrering bør feile når brukernavnet allerede eksisterer", result);
    }

    // ID 66 Test for tomt brukernavn og passord
    @Test
    public void testRegistrationWithEmptyUsernameAndPassword() {
        User newUser = new User(407, "", "", "07071990", "12345",
                "tom@example.com", "Noen Gate 7", 55667788);
        //boolean result = authenticate.addUser(newUser);
        //assertFalse("Registrering bør feile med tomt brukernavn og passord", result);
    }
}
