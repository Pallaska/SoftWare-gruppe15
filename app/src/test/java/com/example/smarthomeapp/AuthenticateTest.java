package com.example.smarthomeapp;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.smarthomeapp.service.Authenticate;

import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthenticateTest {

    private Authenticate authenticate;

    @Before
    public void setUp() throws Exception {
        // Mock Context og AssetManager
        Context mockContext = Mockito.mock(Context.class);
        AssetManager mockAssetManager = Mockito.mock(AssetManager.class);

        // Passordet hashes
        String hashedPassword = BCrypt.hashpw("passord123", BCrypt.gensalt());

        // Eksempel på JSON-data
        String testJson = "{\n" +
                "  \"brukere\": [\n" +
                "    {\n" +
                "      \"brukerID\": 400,\n" +
                "      \"brukernavn\": \"Hans\",\n" +
                "      \"passord\": \"" + hashedPassword + "\",\n" +
                "      \"fodselsdato\": \"15102000\",\n" +
                "      \"rettigheter\": \"12345\",\n" +
                "      \"adresse\": \"basestien 22\",\n" +
                "      \"email\": \"testmail@live.no\",\n" +
                "      \"telefon\": 34724923\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Konverter JSON-strengen til InputStream
        InputStream testInputStream = new ByteArrayInputStream(testJson.getBytes(StandardCharsets.UTF_8));

        // Konfigurer mockene til å returnere test-InputStream
        Mockito.when(mockContext.getAssets()).thenReturn(mockAssetManager);
        Mockito.when(mockAssetManager.open("data.json")).thenReturn(testInputStream);

        // Initialiser Authenticate med den mockede Context
        authenticate = new Authenticate(mockContext);
    }
    // Test som sjekker respons for korrekt brukernavn og passord
    @Test
    public void testValidateLogin_Success() {
        boolean result = authenticate.validateLogin("Hans", "passord123");
        assertTrue("Innlogging bør lykkes med korrekte legitimasjoner", result);
    }

    // Test som sjekker respons for korrekt brukernavn, men feil passord
    @Test
    public void testValidateLogin_Failure_WrongPassword() {
        boolean result = authenticate.validateLogin("Hans", "feilpassord");
        assertFalse("Innlogging bør mislykkes med feil passord", result);
    }

    // Test som sjekker respons for feil brukernavn, men korrekt passord
    @Test
    public void testValidateLogin_Failure_WrongUsername() {
        boolean result = authenticate.validateLogin("FeilBruker", "passord123");
        assertFalse("Innlogging bør mislykkes med feil brukernavn", result);
    }

    // Test som sjekker etter null-verdi i brukernavn og passord
    @Test
    public void testValidateLogin_Failure_NullValues() {
        boolean result = authenticate.validateLogin(null, null);
        assertFalse("Innlogging bør mislykkes når brukernavn og passord er null", result);
    }

    // Test som sjekker etter tomme strenger i brukernavn og passord
    @Test
    public void testValidateLogin_Failure_EmptyStrings() {
        boolean result = authenticate.validateLogin("", "");
        assertFalse("Innlogging bør mislykkes når brukernavn og passord er tomme", result);
    }

    // Test som sjekker case-sensitivitet i passord. Passordet skal ikke godkjennes om bokstavene er store/små feilaktig
    @Test
    public void testValidateLogin_Failure_CaseSensitivityPassword() {
        boolean result = authenticate.validateLogin("Hans", "Passord123");
        assertFalse("Innlogging bør mislykkes når passordet har feil store/små bokstaver", result);
    }

}