package com.example.smarthomeapp;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.smarthomeapp.service.Authenticate;

import org.junit.Before;
import org.junit.Test;
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

        // Eksempel på JSON-data
        String testJson = "{\n" +
                "  \"brukere\": [\n" +
                "    {\n" +
                "      \"brukerID\": 400,\n" +
                "      \"brukernavn\": \"Hans\",\n" +
                "      \"passord\": \"passord123\",\n" +
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
    @Test
    public void testValidateLogin_Success() {
        boolean result = authenticate.validateLogin("Hans", "passord123");
        assertTrue("Innlogging bør lykkes med korrekte legitimasjoner", result);
    }

    @Test
    public void testValidateLogin_Failure_WrongPassword() {
        boolean result = authenticate.validateLogin("Hans", "feilpassord");
        assertFalse("Innlogging bør mislykkes med feil passord", result);
    }

    @Test
    public void testValidateLogin_Failure_WrongUsername() {
        boolean result = authenticate.validateLogin("FeilBruker", "passord123");
        assertFalse("Innlogging bør mislykkes med feil brukernavn", result);
    }
}