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
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + "BRUKERE" + " ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "BRUKERNAVN TEXT, "
                + "PASSORD TEXT, "
                + "FODSELSDATO TEXT, "
                + "RETTIGHETER INTEGER)");
    }
    // Håndtering av database oppgradering
    @Override
    public void onUpgrade(SQLiteDatabase db, int eldreVersjon, int nyereVersjon) {
        db.execSQL("DROP TABLE IF EXISTS " + "BRUKERE");
        onCreate(db);
    }
    // Metode for å legge til en bruker til databasen
    public long leggTilBruker(String brukernavn, String passord, String fodselsdato, int rettigheter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("BRUKERNAVN", brukernavn);
        values.put("PASSORD", passord);
        values.put("FODSELSDATO", fodselsdato);
        values.put("RETTIGHETER", rettigheter);

        return db.insert("BRUKERE", null, values);
    }
    // Metode for å hente brukerdata fra databasen
    public Cursor hentBrukerdata() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + "BRUKERE", null);
    }
}
