package com.example.smarthomeapp.integrering;
import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import androidx.core.app.ActivityCompat;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class Tilkobling {
    // Objektvariabler for WiFi nettverk, Bluetooth enheter, og de forskjellige protokollene
    // for WiFi enheter, mangler fortsatt MQTT, MilO (UDP), Google Cast og eWeLink
    private WifiManager wifiManager; // WiFi nettverk
    private URL enhetUrl; // HTTP
    private HttpURLConnection httpURLConnection; // HTTP
    private Socket socket; // TCP, Printer, IPP
    private BluetoothSocket bluetoothSocket; // Bluetooth enhet

    Context context;
    public Tilkobling(Context context) {
        this.context = context;
    }

    public boolean kobleTilWiFiNettverk(String ssid, String passord) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
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
    public void kobleTilWiFiEnhet(String ip, int port, String passord, String protokoll) {
        // HTTP-protokoll
        if (Objects.equals(protokoll, "HTTP")) {
            // Tilkobling
            try {
                enhetUrl = new URL(protokoll.toLowerCase() + "://" + ip + ":" + port);
                httpURLConnection = (HttpURLConnection) enhetUrl.openConnection();

                // GET-forespørsel
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization", "Bearer " + "token");

                // Svar på GET-forespørsel
                int responseCode = httpURLConnection.getResponseCode();
                // Sjekker om tilkoblingen til HTTP enheten er vellykket
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Koblet til HTTP enhet " + enhetUrl);
                } else {
                    System.out.println("Feil med å koble til enhet " + enhetUrl + ": " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TCP-protokoll (Printer, IPP)
        if (Objects.equals(protokoll, "TCP")) {
            try {
                socket = new Socket(ip, port);
                System.out.println("Koblet til enhet: " + ip + ":" + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void kobleTilBluetoothEnhet(BluetoothDevice enhet) {
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
                return;
            }

            // Tilkobling til Bluetooth enheten
            bluetoothSocket = enhet.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            System.out.println("Koblet til Bluetooth enhet: " + enhet.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Metode for å sende kommandoer til enheter
    // Brukes etter at tilkoblingen er etablert
    public void sendKommandoTilEnhet(String protokoll, String kommando, boolean kobleFraEnhet) {
        try {
            if (Objects.equals(protokoll, "http")) {
                String enhetURL = httpURLConnection.getURL().toString();

                // Setter opp request metode og output
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                // Lager et OutputStream objekt og sender meldingen
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(kommando.getBytes());

                // Rydder opp etter at meldingen er sendt
                outputStream.flush();
                outputStream.close();
                System.out.println("Kommando til enheten: " + kommando);

                // Sjekker om responsen var vellykket og leser responsen
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String input;
                    StringBuilder innhold = new StringBuilder();
                    while ((input = bufferedReader.readLine()) != null) {
                        innhold.append(input);
                        System.out.println("Svar fra enheten: " + innhold);
                    }
                } else {
                    System.out.println("Feil: ingen svar fra enheten");
                }

                if (kobleFraEnhet) {
                    httpURLConnection.disconnect();
                    System.out.println("Koblet fra HTTP enheten " + enhetURL);
                }
            }
            if (Objects.equals(protokoll, "bluetooth")) {
                // Sjekker om BLUETOOTH_CONNECT tillatelsen er gitt
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Feil: mangler BLUETOOTH_CONNECT tillatelsen");
                    return;
                }
                String enhetNavn = bluetoothSocket.getRemoteDevice().getName();

                // Lager OutputStream og InputStream objekter for å sende
                // og ta imot meldinger fra enheten
                OutputStream outputStream = bluetoothSocket.getOutputStream();
                InputStream inputStream = bluetoothSocket.getInputStream();

                // Sender meldingen til enheten
                outputStream.write(kommando.getBytes());
                System.out.println("Kommando til enheten: " + kommando);

                // Rydder opp etter at meldingen er sendt
                outputStream.flush();
                outputStream.close();

                // Leser responsen fra enheten
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                String mottattData = new String(buffer, 0, bytesRead);
                System.out.println("Svar fra enheten: " + mottattData);
                if (kobleFraEnhet) {
                    bluetoothSocket.close();
                    System.out.println("Koblet fra Bluetooth enheten " + enhetNavn);
                }
            }
            if (Objects.equals(protokoll, "tcp")) {
                String enhetNavn = socket.getInetAddress().getHostName();

                // Setter opp OutputStream og InputStream
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();

                // Setter opp BufferedWriter og BufferedReader
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // Sender melding til enheten
                bufferedWriter.write(kommando);

                // Rydder opp etter at meldingen er sendt
                bufferedWriter.flush();
                bufferedWriter.close();
                System.out.println("Kommando til enheten: " + kommando);

                // Leser svar fra enheten
                String input = bufferedReader.readLine();

                if (input != null) {
                    System.out.println("Svar fra enheten: " + input);
                } else {
                    System.out.println("Ingen svar fra enheten " + enhetNavn);
                }
                if (kobleFraEnhet) {
                    if (socket != null) {
                        socket.close();
                        System.out.println("Koblet fra TCP enheten " + enhetNavn);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void kobleFraNettverk() {
        // Statement for å koble fra WiFi nettverket
        if (wifiManager != null) {
            wifiManager.disconnect();
            System.out.println("Koblet fra WiFi nettverket " + wifiManager.getConnectionInfo().getSSID());
        }
    }
    public void kobleFraWiFiEnhet() {
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
            System.out.println("Koblet fra WiFi enheten " + httpURLConnection.getURL());
        }
    }
    public void kobleFraBluetoothEnhet() {
        // Sjekker om BLUETOOTH_CONNECT tillatelsen er gitt
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Feil: mangler BLUETOOTH_CONNECT tillatelsen");
            return;
        }
        String enhetNavn = bluetoothSocket.getRemoteDevice().getName();

        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                System.out.println("Koblet fra Bluetooth enheten " + enhetNavn);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Feil: koblingen til Bluetooth enheten " + enhetNavn + " ble ikke lukket");
            }
        }
    }
}
