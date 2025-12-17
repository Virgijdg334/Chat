package org.main;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    // Variable compartida para saber si hemos salido queriendo
    public static volatile boolean salidaIntencionada = false;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.print("Escribe tu nombre: ");
            String nombre = scanner.nextLine();
            out.println(nombre); // Enviar nombre al servidor

            // Iniciar hilo de escucha
            Thread hiloEscucha = new Thread(new EscucharServidor(socket));
            hiloEscucha.start();

            // Bucle principal (Escribir)
            while (scanner.hasNextLine()) {
                String mensaje = scanner.nextLine();

                if (mensaje.equalsIgnoreCase("Salir")) {
                    // AVISAMOS QUE NOS VAMOS A PROPÓSITO
                    salidaIntencionada = true;
                    System.out.println("Has salido del chat exitosamente.");

                    // Cerramos socket y matamos programa
                    socket.close();
                    System.exit(0);
                    break;
                }

                out.println(mensaje);
            }

        } catch (ConnectException e) {
            System.out.println("Error: El servidor no está disponible.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Hilo que escucha al servidor
class EscucharServidor implements Runnable {
    private Socket socket;

    public EscucharServidor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mensaje;

            while ((mensaje = in.readLine()) != null) {
                System.out.println(mensaje);
            }

            // Si el bucle termina sin error, el servidor cortó la conexión
            throw new IOException("Servidor cerrado");

        } catch (IOException e) {
            // AQUÍ ESTÁ EL TRUCO:
            // Si la desconexión fue a propósito (porque escribí Salir), no mostramos error.
            if (!Cliente.salidaIntencionada) {
                System.out.println("\n!!! Se ha perdido la conexión con el servidor !!!");
                System.exit(0);
            }
        }
    }
}