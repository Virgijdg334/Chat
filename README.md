# Chat Multihilo con Sockets en Java (Cliente-Servidor)

Este proyecto implementa un sistema de chat concurrente en tiempo real utilizando la API de **Sockets** de Java y gestión de **Hilos (Threads)**. El sistema resuelve la limitación de los servidores secuenciales, permitiendo que múltiples clientes se comuniquen simultáneamente sin bloqueos.

---

## 📂 Estructura del Proyecto

El código se ha modularizado en tres componentes para cumplir con los requisitos de arquitectura:

1.  **`Servidor.java`**:
    * Actúa como el despachador principal.
    * Su única función es aceptar conexiones (`server.accept()`) y lanzar inmediatamente un hilo dedicado para no bloquear el puerto.
    * Mantiene la lista global de usuarios conectados.

2.  **`GestorCliente.java` (Hilo / Runnable)**:
    * Contiene toda la lógica de comunicación con un cliente específico.
    * Gestiona los flujos de entrada/salida (`BufferedReader`, `PrintWriter`).
    * Detecta desconexiones forzadas y notifica al resto de usuarios.

3.  **`Cliente.java`**:
    * Interfaz de consola para el usuario.
    * Implementa una arquitectura de **doble hilo**:
        * *Main Thread:* Lectura de teclado y envío de mensajes.
        * *Hilo Secundario (`EscucharServidor`):* Recepción asíncrona de mensajes entrantes.

---

## ✅ Análisis de Fases y Soluciones

Este desarrollo sigue un enfoque iterativo basado en los siguientes hitos técnicos:

### 🔹 Fase 1: Análisis del Bloqueo (Why Threads?)
* **Experimento:** Se comprobó que en un servidor de un solo hilo, si se introduce un retardo (ej. `Thread.sleep(15000)`) tras aceptar al Cliente 1, el Cliente 2 queda en espera indefinida ("colgado") porque el hilo principal está ocupado.
* **Solución:** Se implementó `GestorCliente`. El `main` del servidor ahora delega el trabajo a un nuevo hilo (`new Thread(gestor).start()`) y vuelve instantáneamente a `accept()`, permitiendo conexiones casi simultáneas.

### 🔹 Fase 2: Conversación Fluida (Protocolo)
* **Bucle Infinito:** Se utilizan bucles `while(true)` para mantener el canal abierto indefinidamente.
* **Protocolo de Fin:** Se implementó la palabra clave **"Salir"** (equivalente funcional al "FIN" requerido).
    * Cuando el cliente escribe "Salir", se envía la señal de desconexión, se cierran los recursos localmente y se termina el proceso con `System.exit(0)`.

### 🔹 Fase 3: El Servidor Multihilo (Concurrencia)
* **Gestión de Sockets:** El socket generado por `server.accept()` se pasa por constructor a la clase `GestorCliente`.
* **Independencia:** Cada instancia de `GestorCliente` tiene sus propios Streams, evitando que los mensajes de un usuario se mezclen con los de otro.
* **Broadcast:** Se utiliza una lista sincronizada (`synchronized`) en el Servidor para reenviar los mensajes recibidos a todos los clientes conectados.

### 🔹 Fase 4: Mejoras Profesionales (Bonus)
1.  **Identificación de Usuarios:**
    * Al conectarse, el servidor solicita el nombre del cliente.
    * Se notifica globalmente: *"--- [Nombre] se ha unido al chat ---"*.
    * Se identifican los mensajes: `Nombre: Mensaje`.
2.  **Robustez (Desconexiones Abruptas):**
    * Se implementó control de excepciones (`IOException`).
    * Si un usuario cierra la ventana (mata el proceso) sin avisar, el servidor captura el error, lo elimina de la lista de difusión y avisa al resto: *"--- [Nombre] se ha desconectado forzosamente ---"*.

---

## 🚀 Instrucciones de Ejecución

Para probar el sistema con múltiples clientes en **IntelliJ IDEA**:

1.  **Iniciar el Servidor:**
    * Ejecuta la clase `Servidor`.
    * Consola: *"Servidor iniciado. Esperando gente..."*

2.  **Configurar Multi-Instancia (Importante):**
    * Ve a **Edit Configurations...** (arriba a la derecha).
    * Selecciona la configuración de **`Cliente`**.
    * Activa la casilla **"Allow multiple instances"** (o "Allow parallel run").
    * Guarda los cambios.

3.  **Iniciar Clientes:**
    * Dale a *Run* en `Cliente`. Ingresa el nombre "Usuario A".
    * Dale a *Run* en `Cliente` nuevamente (se abrirá otra pestaña). Ingresa el nombre "Usuario B".
    * ¡Chatea entre las pestañas!

---

## 🧪 Pruebas de Funcionamiento

* **Entrada:** El servidor registra el nombre y avisa a todos.
* **Chat:** Los mensajes escritos en una consola aparecen en todas las demás.
* **Salida (Protocolo):** Escribir "Salir" cierra el cliente correctamente.
* **Caída del Servidor:** Si el servidor se detiene, los clientes muestran *"!!! Se ha perdido la conexión !!!"* y se cierran.
* **Caída del Cliente:** Si un cliente se cierra forzosamente, el servidor no colapsa y notifica la desconexión.

---
**Desarrollado con:** Java SE, Sockets TCP, Java Threads.
