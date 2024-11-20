package com.example.smarthomeapp;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.smarthomeapp.model.User;
import com.example.smarthomeapp.service.Authenticate;
import com.example.smarthomeapp.json.DataKonvertering;

import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthenticateUpdateCredentialsTest {

    private Authenticate authenticate;
    private DataKonvertering mockDataKonvertering;

    @Before
    public void setUp() throws Exception {
        // Mocker Context og AssetManager
        Context mockContext = Mockito.mock(Context.class);
        AssetManager mockAssetManager = Mockito.mock(AssetManager.class);

        // Eksempel på JSON-data med en bruker
        String testJson = "{\n" +
                "  \"brukere\": [\n" +
                "    {\n" +
                "      \"brukerID\": 400,\n" +
                "      \"brukernavn\": \"Hans\",\n" +
                "      \"passord\": \"" + BCrypt.hashpw("passord123", BCrypt.gensalt()) + "\",\n" +
                "      \"fodselsdato\": \"15102000\",\n" +
                "      \"rettigheter\": \"12345\",\n" +
                "      \"adresse\": \"basestien 22\",\n" +
                "      \"email\": \"testmail@live.no\",\n" +
                "      \"telefon\": 34724923\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Konverterer JSON-strengen til InputStream
        InputStream testInputStream = new ByteArrayInputStream(testJson.getBytes(StandardCharsets.UTF_8));

        // Konfigurerer mockene til å returnere test-InputStream
        Mockito.when(mockContext.getAssets()).thenReturn(mockAssetManager);
        Mockito.when(mockAssetManager.open("data.json")).thenReturn(testInputStream);

        // Mocker DataKonvertering
        mockDataKonvertering = Mockito.mock(DataKonvertering.class);

        // Initialiserer Authenticate med den mockede Context og DataKonvertering
        authenticate = new Authenticate(mockContext);
        authenticate.setDataKonvertering(mockDataKonvertering);
    }

    // Tester vellykket oppdatering av brukernavn og passord
    @Test
    public void testUpdateCredentials_Success() {
        // Mocker hentBrukere()
        when(mockDataKonvertering.hentBrukere()).thenReturn(authenticate.getUsers());

        boolean result = authenticate.updateCredentials(400, "passord123", "HansNytt", "nyttPassord123");

        assertTrue("Oppdatering bør lykkes med korrekt nåværende passord", result);

        // Verifiserer at passordet er hashet
        User updatedUser = authenticate.getUsers().get(0);
        assertEquals("Brukernavnet skal være oppdatert til 'HansNytt'", "HansNytt", updatedUser.getBrukernavn());
        assertNotEquals("Passordet skal være hashet og ikke i klartekst", "nyttPassord123", updatedUser.getPassord());
        assertTrue("Det nye passordet skal validere med BCrypt", BCrypt.checkpw("nyttPassord123", updatedUser.getPassord()));

        // Verifiserer at leggTilBruker() ble kalt
        verify(mockDataKonvertering, times(1)).leggTilBruker(updatedUser);
    }

    // Tester respons for feil passord oppgitt
    @Test
    public void testUpdateCredentials_Failure_WrongCurrentPassword() {
        // Mocker hentBrukere()
        when(mockDataKonvertering.hentBrukere()).thenReturn(authenticate.getUsers());

        boolean result = authenticate.updateCredentials(400, "feilPassord", "HansNytt", "nyttPassord123");

        assertFalse("Oppdatering bør mislykkes med feil nåværende passord", result);

        // Verifiserer at leggTilBruker() ikke ble kalt
        verify(mockDataKonvertering, never()).leggTilBruker(any(User.class));
    }

    // Tester respons for brukernavn som allerede eksisterer
    @Test
    public void testUpdateCredentials_Failure_UsernameAlreadyExists() {
        // Legg til en eksisterende bruker med brukernavnet 'Mari'
        authenticate.getUsers().add(new User(
                401,
                "Mari",
                BCrypt.hashpw("passord456", BCrypt.gensalt()),
                "16102000",
                "12345",
                "mari@live.no",
                "rosestien 21",
                12345678
        ));

        // Mocker hentBrukere()
        when(mockDataKonvertering.hentBrukere()).thenReturn(authenticate.getUsers());

        boolean result = authenticate.updateCredentials(400, "passord123", "Mari", "nyttPassord123");

        assertFalse("Oppdatering bør mislykkes når brukernavnet allerede eksisterer", result);

        // Verifiserer at leggTilBruker() ikke ble kalt
        verify(mockDataKonvertering, never()).leggTilBruker(any(User.class));
    }

    // Tester respons for tomt brukernavn og passord oppgitt
    @Test
    public void testUpdateCredentials_Failure_InvalidNewCredentials() {
        // Mocker hentBrukere()
        when(mockDataKonvertering.hentBrukere()).thenReturn(authenticate.getUsers());

        boolean result = authenticate.updateCredentials(400, "passord123", "", "");

        assertFalse("Oppdatering bør mislykkes med ugyldige nye legitimasjoner", result);

        // Verifiserer at leggTilBruker() ikke ble kalt
        verify(mockDataKonvertering, never()).leggTilBruker(any(User.class));
    }

    // Tester respons for null oppgitt som brukernavn og passord
    @Test
    public void testUpdateCredentials_Failure_NullNewCredentials() {
        // Mocker hentBrukere()
        when(mockDataKonvertering.hentBrukere()).thenReturn(authenticate.getUsers());

        boolean result = authenticate.updateCredentials(400, "passord123", null, null);

        assertFalse("Oppdatering bør mislykkes når nye legitimasjoner er null", result);

        // Verifiserer at leggTilBruker() ikke ble kalt
        verify(mockDataKonvertering, never()).leggTilBruker(any(User.class));
    }
}
