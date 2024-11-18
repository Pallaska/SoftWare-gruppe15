package com.example.smarthomeapp;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.smarthomeapp.model.User;
import com.example.smarthomeapp.service.Authenticate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class AuthenticateDataTest {

    private Authenticate authenticate;

    @Before
    public void setUp() throws Exception {
        // Mock Context og AssetManager
        Context mockContext = Mockito.mock(Context.class);
        AssetManager mockAssetManager = Mockito.mock(AssetManager.class);

        // Leser fra data.json
        String filePath = new File("src/main/java/com/example/smarthomeapp/json/Data.json").getAbsolutePath();
        File dataJsonFile = new File(filePath);

        // Sjekker at filen eksisterer (endte med å legge til denne ettersom jeg slet med å nå filen tidligere)
        assertTrue("data.json ikke funnet på stien: " + dataJsonFile.getAbsolutePath(), dataJsonFile.exists());

        InputStream inputStream = Files.newInputStream(dataJsonFile.toPath());

        // Konfigurerer mockene til å returnere inputStream fra data.json
        Mockito.when(mockContext.getAssets()).thenReturn(mockAssetManager);
        Mockito.when(mockAssetManager.open("data.json")).thenReturn(inputStream);

        // Initialiserer Authenticate med den mockede Context
        authenticate = new Authenticate(mockContext);
    }

    // Test for antall brukere som lastes inn
    @Test
    public void testLoadUsersFromJson() {
        int expectedUserCount = 10; // Denne må oppdateres til antall brukere når dette endres
        List<User> users = authenticate.getUsers();
        assertEquals("Antall brukere som er lastet inn skal være " + expectedUserCount, expectedUserCount, users.size());
    }

    // Test som sjekker om en bruker eksisterer. Sjekker om brukerID samsvarer med navn og mail
    @Test
    public void testUserDataIntegrity() {
        List<User> users = authenticate.getUsers();

        User hans = null;
        for (User user : users) {
            if (user.getBrukerID() == 400) {
                hans = user;
                break;
            }
        }

        assertNotNull("Bruker med ID 400 skal finnes", hans);
        assertEquals("Brukernavnet skal være 'Hans'", "Hans", hans.getBrukernavn());
        assertEquals("E-posten skal være 'testmail@live.no'", "testmail@live.no", hans.getEmail());
        // Legg til flere asserter etter behov
    }
}

