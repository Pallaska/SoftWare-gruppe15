package com.example.smarthomeapp.integrering;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class WiFiEnhet extends SmartEnhet {
    private String ipAdresse;
    private int port;

    public WiFiEnhet(String navn, String ipAdresse, int port) {
        super(navn, ipAdresse);
        this.ipAdresse = ipAdresse;
        this.port = port;
    }

    // Her tok jeg ikke i bruk connect/disonnect metodene fordi det ikke trengs
    // for TCP og WiFi slik som det er tilfelle for Bluetooth, men metodene
    // må legges til siden WiFiEnhet er en underklasse av SmartEnhet
    @Override
    public void connect() {}

    @Override
    public void disconnect() {}

    @Override
    public void sendCommand(String kommando) {
        try {
            // Kommandoene sendes ved bruk av TCP og rene bytes, skal
            // senere legge til headers og annet funksjonalitet som trengs
            // for enheter som bruker andre protokoller slik som HTTP, MQTT
            Socket socket = new Socket(ipAdresse, port);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(kommando.getBytes());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}