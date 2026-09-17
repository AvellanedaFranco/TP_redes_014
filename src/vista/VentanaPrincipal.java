import modelo.Dispositivo;
import servicio.EscanerServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {
    private JTextField txtIpInicio, txtIpFin, txtTimeout;
    private JButton btnEscanear, btnLimpiar, btnGuardar;
    private JProgressBar progressBar;
    private JTable tablaResultados;
    private DefaultTableModel tableModel;
    private EscanerServicio servicio;

    public VentanaPrincipal() {
        servicio = new EscanerServicio();
        setTitle("Escáner de Red - TP Redes");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Superior: Formulario
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 5, 5));
        panelForm.add(new JLabel("IP Inicio:"));
        txtIpInicio = new JTextField("192.168.1.1");
        panelForm.add(txtIpInicio);

        panelForm.add(new JLabel("IP Fin:"));
        txtIpFin = new JTextField("192.168.1.10");
        panelForm.add(txtIpFin);

        panelForm.add(new JLabel("Timeout (ms):"));
        txtTimeout = new JTextField("1000");
        panelForm.add(txtTimeout);

        btnEscanear = new JButton("Iniciar Escaneo");
        btnLimpiar = new JButton("Limpiar");
        panelForm.add(btnEscanear);
        panelForm.add(btnLimpiar);

        add(panelForm, BorderLayout.NORTH);

        // Panel Central: Tabla
        String[] columnas = {"Dirección IP", "Nombre del Equipo", "Estado", "Tiempo (ms)"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaResultados = new JTable(tableModel);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);

        // Panel Inferior: Progreso y Exportar
        JPanel panelInferior = new JPanel(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        btnGuardar = new JButton("Guardar TXT");

        panelInferior.add(progressBar, BorderLayout.CENTER);
        panelInferior.add(btnGuardar, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        btnEscanear.addActionListener(e -> iniciarEscaneo());
        btnLimpiar.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardarResultados());
    }

    private void iniciarEscaneo() {
        String ipInicio = txtIpInicio.getText();
        String ipFin = txtIpFin.getText();

        if (!servicio.validarIP(ipInicio) || !servicio.validarIP(ipFin)) {
            JOptionPane.showMessageDialog(this, "Formato de IP inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        
        // Ejecutar en segundo plano para no congelar la ventana
        new Thread(() -> {
            int timeout = Integer.parseInt(txtTimeout.getText());
            // Lógica simple para iterar el último octeto (asumiendo misma subred)
            int inicioOcteto = Integer.parseInt(ipInicio.substring(ipInicio.lastIndexOf('.') + 1));
            int finOcteto = Integer.parseInt(ipFin.substring(ipFin.lastIndexOf('.') + 1));
            String baseIp = ipInicio.substring(0, ipInicio.lastIndexOf('.') + 1);

            int total = (finOcteto - inicioOcteto) + 1;
            int respondieron = 0;

            for (int i = inicioOcteto; i <= finOcteto; i++) {
                String ip = baseIp + i;
                Dispositivo d = servicio.escanearIP(ip, timeout);

                if (d.isConectado()) respondieron++;

                tableModel.addRow(new Object[]{
                        d.getIp(),
                        d.getNombre(),
                        d.isConectado() ? "Conectado" : "No conectado",
                        d.getTiempoRespuesta() >= 0 ? d.getTiempoRespuesta() + " ms" : "N/A"
                });

                int progreso = (int) (((double) (i - inicioOcteto + 1) / total) * 100);
                progressBar.setValue(progreso);
            }
            JOptionPane.showMessageDialog(this, "Escaneo finalizado. Equipos activos: " + respondieron);
        }).start();
    }

    private void limpiar() {
        tableModel.setRowCount(0);
        progressBar.setValue(0);
    }

    private void guardarResultados() {
        try (FileWriter writer = new FileWriter("resultados_escaneo.txt")) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write(tableModel.getValueAt(i, 0) + " | " +
                             tableModel.getValueAt(i, 1) + " | " +
                             tableModel.getValueAt(i, 2) + " | " +
                             tableModel.getValueAt(i, 3) + "\n");
            }
            JOptionPane.showMessageDialog(this, "Guardado exitosamente en resultados_escaneo.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}