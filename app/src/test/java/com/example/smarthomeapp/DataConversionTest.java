package com.example.smarthomeapp;

import com.example.smarthomeapp.json.DataKonvertering;
import com.example.smarthomeapp.model.User;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataConversionTest {

    private DataKonvertering dataKonvertering;

    @Before
    public void setUp() {
        dataKonvertering = new DataKonvertering();
        Gson gson = new Gson();
    }

    // Tester om brukere kan leses fra JSON-fil
    @Test
    public void testHentBrukere_ValidData() throws Exception {
        String validJson = "{ \"brukere\": [ { \"brukerID\": 1, \"brukernavn\": \"Hans\" } ] }";
        Reader reader = new StringReader(validJson);

        DataKonvertering spyDataKonvertering = Mockito.spy(dataKonvertering);
        doReturn(reader).when(spyDataKonvertering).getFileReader(anyString());

        List<User> brukere = spyDataKonvertering.hentBrukere();
        assertEquals("Skal være 1 bruker i listen", 1, brukere.size());
        assertEquals("Brukernavnet skal være 'Hans'", "Hans", brukere.get(0).getBrukernavn());
    }


    // Tester håndtering av feil format i JSON-filen
    @Test
    public void testHentBrukere_MalformedJson() throws Exception {
        String invalidJson = "{ \"brukere\": [ { \"brukerID\": 1, ";
        Reader reader = new StringReader(invalidJson);

        DataKonvertering spyDataKonvertering = Mockito.spy(dataKonvertering);
        doReturn(reader).when(spyDataKonvertering).getFileReader(anyString());

        List<User> brukere = spyDataKonvertering.hentBrukere();
        assertTrue("Brukerlisten skal være tom ved feilformet JSON", brukere.isEmpty());
    }

    // Tester at en tom liste blir returnert, om JSON-filen skulle være tom for data
    @Test
    public void testHentBrukere_EmptyJsonArray() throws Exception {
        String emptyJson = "{ \"brukere\": [] }";
        Reader reader = new StringReader(emptyJson);

        DataKonvertering spyDataKonvertering = Mockito.spy(dataKonvertering);
        doReturn(reader).when(spyDataKonvertering).getFileReader(anyString());

        List<User> brukere = spyDataKonvertering.hentBrukere();
        assertTrue("Brukerlisten skal være tom når JSON-arrayen er tom", brukere.isEmpty());
    }

    // Tester at brukere kan legges til i JSON-filen
    @Test
    public void testLeggTilBruker_Success() throws Exception {
        User nyBruker = new User(2, "Mari", "passord123", "16102000", "12345", "mari@live.no", "rosestien 21", 12345678);

        DataKonvertering spyDataKonvertering = Mockito.spy(dataKonvertering);
        doReturn(new ArrayList<User>()).when(spyDataKonvertering).hentBrukere();

        doNothing().when(spyDataKonvertering).writeToFile(anyString(), anyString());

        spyDataKonvertering.leggTilBruker(nyBruker);

        verify(spyDataKonvertering, times(1)).writeToFile(anyString(), anyString());
    }

    // Tester håndtering av forsøk på å legge til bruker med eksisterende ID eller brukernavn
    @Test
    public void testLeggTilBruker_DuplicateUser() throws Exception {
        // Forutsetter at det finnes en bruker med brukerID 400
        String existingJson = "{ \"brukere\": [ { \"brukerID\": 400, \"brukernavn\": \"Hans\" } ] }";
        Reader reader = new StringReader(existingJson);

        DataKonvertering spyDataKonvertering = Mockito.spy(dataKonvertering);
        doReturn(reader).when(spyDataKonvertering).getFileReader(anyString());

        User duplicateUser = new User(400, "Hans", "passord123", "15102000", "12345", "hans@live.no", "basestien 22", 34724923);

        doNothing().when(spyDataKonvertering).writeToFile(anyString(), anyString());

        spyDataKonvertering.leggTilBruker(duplicateUser);

        verify(spyDataKonvertering, never()).writeToFile(anyString(), anyString());
    }

    // Tester håndtering av exception dersom det ikke kan skrives til fil
    @Test
    public void testLeggTilBruker_FileWriteException() throws Exception {
        User nyBruker = new User(3, "Einar", "passord789", "17102000", "12345", "einar@live.no", "poppystien 20", 34567890);

        DataKonvertering spyDataKonvertering = Mockito.spy(dataKonvertering);
        doThrow(new IOException("Kunne ikke skrive til fil")).when(spyDataKonvertering).writeToFile(anyString(), anyString());

        spyDataKonvertering.leggTilBruker(nyBruker);
    }
}