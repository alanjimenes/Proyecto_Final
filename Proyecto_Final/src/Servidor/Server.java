package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Utils.ConexionDB;

public class Server {

    /**
     * PROCESO: Punto de entrada (main) que arranca el servicio del servidor de la clínica.
     * * ENTRADAS:
     * - args: Arreglo de cadenas con parámetros del sistema.
     * * SALIDA: Ninguna.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion().
     * 2. Inicializa ServerSocket(7000).
     * 3. Ejecuta el bucle continuo sfd.accept() -> new Flujo() -> flujo.start().
     */

    public static void main(String[] args) {
        ServerSocket sfd = null;

        System.out.println("--- INICIANDO SERVIDOR CLINICA ---");

        try {
            ConexionDB.getConexion();
            System.out.println(">>> CONEXIÓN A SQL SERVER EXITOSA <<<");
        } catch (Exception e) {
            System.err.println("Error conectando a la base de datos.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            sfd = new ServerSocket(7000);
            System.out.println(">>> SERVIDOR ONLINE EN PUERTO 7000 <<<");
        } catch (IOException e) {
            System.err.println("No fue posible abrir el puerto 7000.");
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
                Socket socket = sfd.accept();
                Flujo flujo = new Flujo(socket);
                flujo.start();
            } catch (IOException e) {
                System.out.println("Error aceptando conexión.");
                e.printStackTrace();
            }
        }
    }
}