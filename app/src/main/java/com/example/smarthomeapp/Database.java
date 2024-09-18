package com.example.smarthomeapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context)
    {
        super(context, "smartAppDatabase.db", null, 1);
    }
    // Brukertabellen
    // Tabellen består av Id, Brukernavn, Passord, Fødselsdato og Rettigheter
    // Legg merke til at Fødselsdato også er av datatype TEXT og skrives som
    // ÅÅÅÅ-MM-DD siden datatype DATE eller DATETIME mangler i SQLite 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + "BRUKERE" + " ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "BRUKERNAVN TEXT, "
                + "PASSORD TEXT, "
                + "FODSELSDATO TEXT, "
                + "RETTIGHETER TEXT)");
    }
    // Håndtering av database oppgradering
    // Parametrene eldreVersjon og nyereVersjon er tilstede fordi det er et krav
    // for denne metoden, men vi har ikke bruk for dem på dette tidspunktet
    @Override
    public void onUpgrade(SQLiteDatabase db, int eldreVersjon, int nyereVersjon) {
        db.execSQL("DROP TABLE IF EXISTS " + "BRUKERE");
        onCreate(db);
    }
    // Metode for å legge til en bruker til brukertabellen
    public long leggTilBruker(String brukernavn, String passord, String fodselsdato, int rettigheter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("BRUKERNAVN", brukernavn);
        values.put("PASSORD", passord);
        values.put("FODSELSDATO", fodselsdato);
        values.put("RETTIGHETER", rettigheter);

        return db.insert("BRUKERE", null, values);
    }
    // Metode for å hente all brukerdata fra databasen, dvs. innholdet
    // i alle kolonner som finnes i tabellen, og så kan informasjonen
    // som trengs hentes ved bruk av et Cursor objekt (se eksempel i MainActivity)
    public Cursor hentBrukerdata() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + "BRUKERE", null);
    }
}
