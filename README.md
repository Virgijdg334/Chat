# Java Socket Chat 💬

Un sistema de chat minimalista Cliente-Servidor implementado en Java utilizando Sockets y Threads. Permite que múltiples usuarios se conecten y hablen entre sí en tiempo real a través de la consola.

## 📋 Características
* **Multicliente:** El servidor soporta múltiples conexiones simultáneas.
* **Tiempo Real:** Uso de hilos (`Threads`) para enviar y recibir mensajes al mismo tiempo.
* **Broadcast:** Cuando un cliente escribe, el servidor reenvía el mensaje a todos los conectados.

## 🚀 Instrucciones de Ejecución

Sigue este orden estricto para probar el programa:

1.  **Iniciar el Servidor:**
    * Ejecuta la clase `Servidor`.
    * Verás el mensaje: *"Servidor encendido. Esperando clientes..."*.
    * *Nota:* No cierres esta ventana.

2.  **Iniciar el Cliente:**
    * Ejecuta la clase `Cliente`.
    * Verás el mensaje: *"Conectado. Escribe tus mensajes:"*.

3.  **Simular una conversación:**
    * Para probar el chat real, ejecuta la clase `Cliente` **varias veces** (o en diferentes terminales).
    * Lo que escribas en una ventana de Cliente aparecerá automáticamente en las demás.

## ⚙️ Configuración Técnica

| Parámetro | Valor | Descripción |
| :--- | :--- | :--- |
| **Host** | `localhost` | Dirección local de tu máquina. |
| **Puerto** | `5000` | Puerto TCP de escucha del servidor. |

---
**Requisito:** Java JDK instalado y conexión a red local (para localhost).
