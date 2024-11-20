package com.example.smarthomeapp;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.smarthomeapp.model.User;
import com.example.smarthomeapp.service.Authenticate;

import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class AuthenticateRegistrationTest {

    private Authenticate authenticate;

    @Before
    public void setUp() throws Exception {
        Context mockContext = Mockito.mock(Context.class);
        AssetManager mockAssetManager = Mockito.mock(AssetManager.class);

        String testJson = "{ \"brukere\": [] }";

        InputStream testInputStream = new ByteArrayInputStream(testJson.getBytes(StandardCharsets.UTF_8));

        Mockito.when(mockContext.getAssets()).thenReturn(mockAssetManager);
        Mockito.when(mockAssetManager.open("data.json")).thenReturn(testInputStream);

        authenticate = new Authenticate(mockContext);
    }

    // Test for registrering av bruker korrekt
    @Test
    public void testRegisterNewUser_Success() {
        User newUser = new User(
                410,
                "NyBruker",
                "nyttPassord123",
                "01012000",
                "standard",
                "nybruker@eksempel.no",
                "Nygata 1",
                12345678
        );

        boolean result = authenticate.addUser(newUser);

        assertTrue("Registrering bør lykkes med gyldige data", result);
        assertEquals("Brukerlisten skal inneholde 1 bruker", 1, authenticate.getUsers().size());
        assertEquals("Brukernavnet skal være 'NyBruker'", "NyBruker", authenticate.getUsers().get(0).getBrukernavn());

        // Verifiserer at passordet er hashet
        String storedPassword = authenticate.getUsers().get(0).getPassord();
        assertNotEquals("Passordet skal være hashet og ikke i klartekst", "nyttPassord123", storedPassword);
        assertTrue("Passordet skal validere med BCrypt", BCrypt.checkpw("nyttPassord123", storedPassword));
    }

    // Test for registrering av bruker der formatet på eposten er ugyldig
    @Test
    public void testRegisterNewUser_Failure_InvalidEmail() {
        User newUser = new User(
                411,
                "BrukerMedUgyldigEmail",
                "passord123",
                "02022000",
                "standard",
                "ugyldigemail", // Ugyldig e-post
                "Ugyldiggata 2",
                12345678
        );

        boolean result = authenticate.addUser(newUser);

        assertFalse("Registrering bør mislykkes med ugyldig e-postformat", result);
        assertEquals("Brukerlisten skal være tom", 0, authenticate.getUsers().size());
    }

    // Test der passordet er for svakt
    @Test
    public void testRegisterNewUser_Failure_WeakPassword() {
        User newUser = new User(
                412,
                "BrukerMedSvaktPassord",
                "123", // Svakt passord
                "03032000",
                "standard",
                "gyldig@eksempel.no",
                "Gyldiggata 3",
                12345678
        );

        boolean result = authenticate.addUser(newUser);

        assertFalse("Registrering bør mislykkes med for svakt passord", result);
        assertEquals("Brukerlisten skal være tom", 0, authenticate.getUsers().size());
    }

    // Test for passord som er for langt
    @Test
    public void testRegisterNewUser_Failure_PasswordTooLong() {
        String longPassword = "a".repeat(101);

        User newUser = new User(
                414,
                "GyldigBruker",
                longPassword,
                "05052000",
                "standard",
                "gyldig@eksempel.no",
                "Gyldiggata 5",
                12345678
        );

        boolean result = authenticate.addUser(newUser);

        assertFalse("Registrering bør mislykkes når passordet er for langt", result);
        assertEquals("Brukerlisten skal være tom", 0, authenticate.getUsers().size());
    }

    // Test for brukernavn som er for langt
    @Test
    public void testRegisterNewUser_Failure_UsernameTooLong() {
        String longUsername = "a".repeat(51);

        User newUser = new User(
                413,
                longUsername,
                "passord123",
                "04042000",
                "standard",
                "gyldig@eksempel.no",
                "Gyldiggata 4",
                12345678
        );

        boolean result = authenticate.addUser(newUser);

        assertFalse("Registrering bør mislykkes når brukernavnet er for langt", result);
        assertEquals("Brukerlisten skal være tom", 0, authenticate.getUsers().size());
    }

    // Test for registrering av bruker der brukernavnet eksisterer fra før
    @Test
    public void testRegisterNewUser_Failure_DuplicateUsername() {
        User existingUser = new User(
                411,
                "EksisterendeBruker",
                "passord123",
                "01012000",
                "standard",
                "eksisterende@eksempel.no",
                "Eksisterendevei 1",
                87654321
        );
        authenticate.addUser(existingUser);

        User newUser = new User(
                412,
                "EksisterendeBruker", // Samme brukernavn
                "nyttPassord123",
                "02022000",
                "standard",
                "nybruker@eksempel.no",
                "Nygata 2",
                12345678
        );

        boolean result = authenticate.addUser(newUser);

        assertFalse("Registrering bør mislykkes når brukernavnet allerede er i bruk", result);
        assertEquals("Brukerlisten skal fortsatt inneholde 1 bruker", 1, authenticate.getUsers().size());
    }

    // Test der brukernavnet er tomt
    @Test
    public void testRegisterNewUser_Failure_InvalidData() {
        User newUser = new User(
                413,
                "",
                "passord123",
                "03032000",
                "standard",
                "invalid@eksempel.no",
                "Invalidgata 3",
                12345678
        );

        boolean result = authenticate.addUser(newUser);

        assertFalse("Registrering bør mislykkes når brukernavnet er tomt", result);
        assertEquals("Brukerlisten skal være tom", 0, authenticate.getUsers().size());
    }
}