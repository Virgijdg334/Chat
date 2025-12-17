package org.main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Servidor {
    // Lista pública para que el Gestor pueda acceder
    public static ArrayList<PrintWriter> clientes = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Servidor iniciado. Esperando gente...");

            while (true) {
                Socket socket = server.accept();

                // Lanzamos el gestor en un hilo nuevo
                GestorCliente gestor = new GestorCliente(socket);
                Thread hilo = new Thread(gestor);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para enviar mensaje a TODOS
    public static void broadcast(String mensaje) {
        synchronized (clientes) {
            for (PrintWriter escritor : clientes) {
                escritor.println(mensaje);
            }
        }
    }
}