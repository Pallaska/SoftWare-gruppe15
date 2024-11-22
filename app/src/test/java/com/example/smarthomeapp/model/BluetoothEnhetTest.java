package com.example.smarthomeapp.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class BluetoothEnhetTest {

    // ID 44 Tester at det opprettes en bluetooth enhet
    @Test
    public void testBluetoothEnhetOpprettelse() {
        BluetoothEnhet enhet = new BluetoothEnhet(1, "SmartSpeaker");

        assertEquals(1, enhet.getEnhetID());
        assertEquals("SmartSpeaker", enhet.getEnhetNavn());
    }

    // ID 44 Tester at navnet på enheten kan endres
    @Test
    public void testSetEnhetNavn() {
        BluetoothEnhet enhet = new BluetoothEnhet(2, "SmartLight");

        enhet.setEnhetNavn("UpdatedLight");
        assertEquals("UpdatedLight", enhet.getEnhetNavn());
    }

    // ID 44 Tester at det kan hentes en streng for enheten
    @Test
    public void testToString() {
        BluetoothEnhet enhet = new BluetoothEnhet(3, "SmartPlug");

        String forventet = "BluetoothEnhet{enhetID=3, enhetNavn=SmartPlug}";
        assertEquals(forventet, enhet.toString());
    }
}
