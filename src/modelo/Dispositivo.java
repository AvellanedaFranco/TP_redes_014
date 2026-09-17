package modelo;

public class Dispositivo {
    private String ip;
    private String nombre;
    private boolean conectado;
    private long tiempoRespuesta;

    public Dispositivo(String ip, String nombre, boolean conectado, long tiempoRespuesta) {
        this.ip = ip;
        this.nombre = nombre;
        this.conectado = conectado;
        this.tiempoRespuesta = tiempoRespuesta;
    }

    // Getters y Setters
    public String getIp() { return ip; }
    public String getNombre() { return nombre; }
    public boolean isConectado() { return conectado; }
    public long getTiempoRespuesta() { return tiempoRespuesta; }
}