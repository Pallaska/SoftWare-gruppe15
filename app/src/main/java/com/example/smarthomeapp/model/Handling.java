package com.example.smarthomeapp.model;
import java.time.LocalDateTime;

public class Handling {
    int brukerID;
    String logg;
    String tid;

    public Handling(int brukerID, String logg) {
        this.brukerID = brukerID;
        this.logg = logg;
        this.tid = LocalDateTime.now().toString();
    }

    public int hentBrukerID() {
        return brukerID;
    }
    public String hentLogg() {
        return logg;
    }
    public String hentTid() {
        return tid;
    }

    @Override
    public String toString() {
        return "handlinger{" + "brukerID=" + brukerID + ", logg='" + logg + '\'' + ", tid=" + tid + '}';
    }
}


