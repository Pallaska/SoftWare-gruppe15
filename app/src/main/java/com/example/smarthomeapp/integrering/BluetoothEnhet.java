package com.example.smarthomeapp.integrering;
import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothEnhet extends SmartEnhet {
    private BluetoothDevice enhet;
    private BluetoothSocket socket;
    private Context context;

    public BluetoothEnhet(String navn, String adresse, BluetoothDevice enhet, Context context) {
        super(navn, adresse);
        this.enhet = enhet;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void connect() {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                return;
            }
            UUID uuid = enhet.getUuids()[0].getUuid();
            socket = enhet.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            System.out.println("Aktiv tilkobling");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendCommand(String kommando) {
        try {
            if (socket != null && socket.isConnected()) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(kommando.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}