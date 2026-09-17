package servicio;

import modelo.Dispositivo;
import java.net.InetAddress;

public class EscanerServicio {

    // Validar formato de IP
    public boolean validarIP(String ip) {
        String patron = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(patron);
    }

    // Hacer Ping y obtener hostname
    public Dispositivo escanearIP(String ip, int timeoutMs) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            long inicio = System.currentTimeMillis();
            boolean alcanzable = address.isReachable(timeoutMs);
            long fin = System.currentTimeMillis();

            if (alcanzable) {
                String nombre = address.getCanonicalHostName(); // Equivale a nslookup
                return new Dispositivo(ip, nombre, true, (fin - inicio));
            } else {
                return new Dispositivo(ip, "Desconocido", false, -1);
            }
        } catch (Exception e) {
            return new Dispositivo(ip, "Error", false, -1);
        }
    }
}