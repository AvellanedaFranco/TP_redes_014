# Escáner de Red

## Información
* Materia: Redes
* Curso: 5to 1ra - E.T. N° 36
* Tecnología: Java (Swing)

## Descripción
Aplicación gráfica para escanear rangos de direcciones IP en una red local (LAN). Permite verificar la conectividad de los equipos mediante ping (ICMP), medir la latencia, obtener el nombre del dispositivo mediante DNS y exportar los resultados a un archivo .txt.

## Estructura del Proyecto
* src/Main.java: Punto de entrada de la aplicación.
* src/control/EscanerServicio.java: Lógica de red (ping, DNS y validaciones).
* src/modelo/Dispositivo.java: Estructura de datos del equipo.
* src/vista/VentanaPrincipal.java: Interfaz gráfica con Swing.

## Cómo Ejecutar
1. Abrir la carpeta raíz del proyecto en un IDE (VS Code, NetBeans, IntelliJ).
2. Asegurarse de tener instalado Java JRE/JDK 8 o superior.
3. Ejecutar el archivo src/Main.java.