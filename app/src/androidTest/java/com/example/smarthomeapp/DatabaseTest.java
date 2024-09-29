package com.example.smarthomeapp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import android.content.Context;
import android.database.Cursor;

import androidx.test.core.app.ApplicationProvider;

public class DatabaseTest {
    private Database db;

    // Initialiserer database før hver test
    @Before
    public void initialisering() {
        Context context = ApplicationProvider.getApplicationContext();
        db = new Database(context);
    }

    // Deaktiverer database etter hver test
    @After
    public void deaktivering() {
        db.close();
    }

    // Test for å se om bruker blir lagt til
    // Når en bruker legges til så returneres id'en, og siden den er satt til auto increment
    // så skal den bli +1 for hver bruker som legges til
    // I dette tilfellet sjekkes det bare om id > -1
    @Test
    public void testLeggTilBruker() {
        long id = db.leggTilBruker("A", "B", "09-09-2005", "2500");

        assertTrue(id > -1);
    }

    // Test for å se om det leses fra tabellen
    // Her sjekkes det om moveToFirst har flyttet cursor til den første rekken
    // og i så fall så finnes det en rekke og det er data i tabellen
    @Test
    public void testHentBrukerdata() {
        db.leggTilBruker("A", "B", "09-09-2005", "2500");

        Cursor cursor = db.hentBrukerdata();
        assertTrue(cursor.moveToFirst());
        cursor.close();
    }
}