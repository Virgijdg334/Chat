package org.main;

import java.io.*;
import java.net.*;

public class GestorCliente implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String nombre = "Usuario desconocido"; // Valor por defecto

    public GestorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Añadir a la lista
            synchronized (Servidor.clientes) {
                Servidor.clientes.add(out);
            }

            // 1. Leer nombre y avisar CONEXIÓN
            nombre = in.readLine();
            System.out.println("SERVIDOR: " + nombre + " se ha conectado."); // Log en Servidor
            Servidor.broadcast("--- " + nombre + " se ha unido al chat ---"); // Aviso a Clientes

            String mensaje;
            // 2. Bucle de mensajes
            while ((mensaje = in.readLine()) != null) {
                // Log en Servidor de que alguien habló
                System.out.println("Mensaje de " + nombre + ": " + mensaje);
                // Reenviar a todos
                Servidor.broadcast(nombre + ": " + mensaje);
            }

        } catch (IOException e) {
            // Esto salta si cierran la ventana de golpe (X roja)
            System.out.println("SERVIDOR: " + nombre + " ha sufrido una desconexión forzada.");
        } finally {
            // 3. DESCONEXIÓN (Se ejecuta SIEMPRE al final)

            // Borramos al cliente de la lista
            if (out != null) {
                synchronized (Servidor.clientes) {
                    Servidor.clientes.remove(out);
                }
            }

            // Log en Servidor de que se ha ido (esto es lo que te faltaba)
            System.out.println("SERVIDOR: " + nombre + " se ha desconectado.");

            // Avisamos al resto de clientes
            Servidor.broadcast("--- " + nombre + " ha abandonado el chat ---");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}