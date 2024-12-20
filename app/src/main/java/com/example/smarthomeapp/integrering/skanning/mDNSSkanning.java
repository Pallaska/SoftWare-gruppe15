package com.example.smarthomeapp.integrering.skanning;
import androidx.appcompat.app.AppCompatActivity;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class mDNSSkanning extends AppCompatActivity {
    private JmDNS jmdns;
    private List<ServiceEvent> resultater;

    // _http._tcp.local.
    // _ewelink._tcp.local.
    // _mqtt._tcp.local.
    // _googlecast._tcp.local.
    // _miio._udp.local.
    // _printer._tcp.local.
    // _ipp._tcp.local.
    public void startEnhetsskanning(String tjenestetype, int sekunder) {
        new Thread(() -> {
            try {
                // Bruker enhetens IP adresse til å lage et JmDNS objekt
                // som søker etter mDNS broadcasts på det lokale nettverket
                InetAddress adresse = InetAddress.getLocalHost();
                jmdns = JmDNS.create(adresse);

                // ServiceListener for tjenestetype EWELINK
                jmdns.addServiceListener(tjenestetype, new ServiceListener() {
                    // En tjeneste har blitt oppdaget på nettverket
                    @Override
                    public void serviceAdded(ServiceEvent hendelse) {
                        System.out.println("Tjeneste lagt til: " + hendelse.getName());
                    }

                    // En tjeneste som var oppdaget er ikke lenger tilgjengelig
                    @Override
                    public void serviceRemoved(ServiceEvent hendelse) {
                        // Hvis en WiFi enhet ikke lenger er tilgjengelig
                        // så fjernes den fra listen over WiFi enheter
                        for (ServiceEvent serviceEvent : resultater) {
                            if (serviceEvent == hendelse) {
                                resultater.remove(serviceEvent);
                            }
                        }
                        System.out.println("Tjeneste fjernet: " + hendelse.getName());
                    }

                    // En tjeneste har blitt fullstendig kartlagt
                    @Override
                    public void serviceResolved(ServiceEvent hendelse) {
                        // Lagrer enheter som blir funnet
                        resultater.add(hendelse);
                        System.out.println("Tjeneste funnet: " + hendelse.getInfo());
                    }
                });

                // Thread objekt for tidsavbrudd av enhetsskanning
                new Thread(() -> {
                    try {
                        Thread.sleep(sekunder * 1000);
                        avsluttEnhetsskanning();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Metode for å avslutte enhetsskanning, kalles av
    // Thread objektet (tidsavbrudd)
    public void avsluttEnhetsskanning() {
        if (jmdns != null) {
            try {
                jmdns.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List hentResultater() {
        return resultater;
    }
}
