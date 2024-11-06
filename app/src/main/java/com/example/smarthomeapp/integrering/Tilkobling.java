package com.example.smarthomeapp.integrering;
import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class Tilkobling {
    // Objektvariabler for WiFi nettverk, Bluetooth enheter,
    // og de forskjellige protokollene for WiFi enheter
    // Mangler fortsatt MQTT, MilO (UDP), Google Cast og eWeLink
    private WifiManager wifiManager; // WiFi nettverk
    private URL enhetUrl; // HTTP
    private Socket socket; // TCP, Printer, IPP
    private BluetoothSocket bluetoothSocket; // Bluetooth enhet

    Context context;
    public Tilkobling(Context context) {
        this.context = context;
    }
    public boolean kobleTilWiFiNettverk(String ssid, String passord) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled() == false) {
            wifiManager.setWifiEnabled(true);
        }
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + ssid + "\"";
        wifiConfiguration.preSharedKey = "\"" + passord + "\"";

        int nettverksId = wifiManager.addNetwork(wifiConfiguration);
        if (nettverksId == -1) {
            System.out.println("Feil: nettverkskonfigurasjon ble ikke lagt til");
        }
        wifiManager.disconnect();
        boolean aktiv = wifiManager.enableNetwork(nettverksId, true);
        wifiManager.reconnect();

        return aktiv;
    }
    public void kobleTilWiFiEnhet(String ssid, String passord, String ip, int port, String protokoll) throws IOException {
        boolean tilkoblet = kobleTilWiFiNettverk(ssid, passord);

        if (tilkoblet == false) {
            System.out.println("Feil: ikke tilkoblet til nettverket");
            return;
        }
        // HTTP-protokoll
        if (Objects.equals(protokoll, "HTTP")) {
            // Tilkobling
            enhetUrl = new URL(protokoll.toLowerCase() + "://" + ip + ":" + port);
            HttpURLConnection httpURLConnection = (HttpURLConnection) enhetUrl.openConnection();

            // GET-forespørsel
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + "token");

            // Svar på GET-forespørsel
            int responseCode = httpURLConnection.getResponseCode();
            // Sjekker om tilkoblingen til HTTP enheten er vellykket
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Koblet til HTTP enhet: " + enhetUrl);
            } else {
                System.out.println("Feil med å tilkoble til enhet: " + responseCode);
            }
            httpURLConnection.disconnect();
        }
        // TCP-protokoll (Printer, IPP)
        if (Objects.equals(protokoll, "TCP")) {
            try {
                socket = new Socket(ip, port);
                System.out.println("Koblet til enhet: " + ip + ":" + port);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Må lukke socket i en "finally" blokk siden jeg ikke har
                // deklarert/initialisert socket i parentesen til try-blokken og da
                // blir ikke socket lukket automatisk når try-blokken er ferdig
                if (socket != null && !socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public boolean kobleTilBluetoothEnhet(BluetoothDevice enhet) {
        // Konverterer en string-representasjon av UUID til et UUID objekt
        // I dette tilfellet blir det Serial Port Profile,som brukes av en
        // rekke IoT enheter
        // Det er mulig jeg legger til en annen UUID senere hvis jeg finner
        // ut av Serial Port Profile ikke er tilstrekkelig
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        try {
            // Sjekker om BLUETOOTH_CONNECT tillatelsen har blitt gitt
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Feil: mangler BLUETOOTH_CONNECT tillatelsen");
                return false;
            }
            // Tilkobling til Bluetooth enheten
            bluetoothSocket = enhet.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            System.out.println("Koblet til Bluetooth enhet: " + enhet.getName());

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Skriver koden for å slå av tilkoblingen i "finally" i stedet for en
            // if-statement sånn at det blir utført selv om det kommer opp en Exception
            if (bluetoothSocket != null && bluetoothSocket.isConnected() == false) {
                try {
                    bluetoothSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    // Metode for å sende kommandoer til enheter
    // Brukes etter at tilkoblingen er etablert
    public void sendKommandoTilEnhet(String protokoll, String kommando) throws IOException {
        if (Objects.equals(protokoll, "http")) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(kommando.getBytes());
        }
        if (Objects.equals(protokoll, "bluetooth")) {
            OutputStream outputStream = null;
            InputStream inputStream = null;

            // Streams for kommunikasjon med enheten
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            // Eksempel på bruk av output/input

            // outputStream.write(kommando.getBytes());
            // byte[] buffer = new byte[1024];
            // int bytesRead = inputStream.read(buffer);
            // String mottattData = new String(buffer, 0, bytesRead);
            // System.out.println("Mottatt: " + mottattData);
        }
    }
    // Metode for å koble fra nettverk/enhet
    public void kobleFraEnhet(String tilkoblingsType) {
        // Statement for å koble fra WiFi nettverket
        // Det er ingen grunn til å koble fra enheten om vi har koblet fra nettverket
        if (tilkoblingsType == "WIFI") {
            if (wifiManager != null) {
                wifiManager.disconnect();
                System.out.println("Koblet fra WiFi nettverket");
            }
        }
        // Statement for å koble fra en Bluetooth enhet
        if (tilkoblingsType == "BLUETOOTH") {
            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close();
                    System.out.println("Koblet fra Bluetooth enheten");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Feil: tilkoblingen til Bluetooth enheten ble ikke lukket");
                }
            }
        }
    }
}
