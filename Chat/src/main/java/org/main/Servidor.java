package org.main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Servidor {
    // Lista para guardar a los clientes y poder enviarles mensajes a todos
    static ArrayList<PrintWriter> clientes = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // 1. Abrir el puerto 5000
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Servidor encendido. Esperando clientes...");

            while (true) {
                // 2. Esperar conexión (el programa se pausa aquí hasta que alguien entre)
                Socket socketCliente = serverSocket.accept();

                // 3. Guardar el canal de escritura de este cliente
                PrintWriter out = new PrintWriter(socketCliente.getOutputStream(), true);
                clientes.add(out);

                // 4. LANZAR UN HILO para atender a este cliente individualmente
                Thread hilo = new Thread(new ClientHandler(socketCliente));
                hilo.start();

                System.out.println("Nuevo cliente conectado desde: " + socketCliente.getInetAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Clase HILO encargada de escuchar a UN solo cliente
class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Preparar para leer lo que envía este cliente
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String mensaje;
            // Bucle: Mientras el cliente envíe mensajes...
            while ((mensaje = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);

                // Reenviar el mensaje a TODOS los clientes conectados
                for (PrintWriter escritor : Servidor.clientes) {
                    escritor.println(mensaje);
                }
            }
        } catch (IOException e) {
            System.out.println("Un cliente se desconectó.");
        }
    }
}