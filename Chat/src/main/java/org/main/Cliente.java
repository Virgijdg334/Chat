package org.main;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try {
            // 1. Conectarse al servidor en localhost:5000
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Conectado. Escribe tus mensajes:");

            // 2. LANZAR UN HILO para escuchar los mensajes que llegan del servidor
            // (Si no hacemos esto en un hilo aparte, no podríamos escribir y leer a la vez)
            Thread hiloEscucha = new Thread(new LeerMensajes(socket));
            hiloEscucha.start();

            // 3. El hilo principal se queda leyendo del teclado y enviando
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner teclado = new Scanner(System.in);

            while (teclado.hasNextLine()) {
                String mensaje = teclado.nextLine();
                out.println(mensaje); // Enviar al servidor
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Clase HILO que solo se dedica a imprimir lo que llega del servidor
class LeerMensajes implements Runnable {
    private Socket socket;

    public LeerMensajes(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mensajeDelServidor;

            // Bucle infinito leyendo lo que manda el servidor
            while ((mensajeDelServidor = in.readLine()) != null) {
                System.out.println("Chat: " + mensajeDelServidor);
            }
        } catch (IOException e) {
            System.out.println("Se perdió la conexión.");
        }
    }
}