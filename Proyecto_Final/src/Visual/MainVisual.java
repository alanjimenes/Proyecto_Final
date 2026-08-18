package Visual;

import Servidor.Flujo;
import Utils.ConexionDB;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainVisual {
    public static void main(String[] args) {

        Thread hiloServidor = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket sfd = null;
                System.out.println("--- INICIANDO SERVIDOR CLINICA ---");

                try {
                    ConexionDB.getConexion();
                    System.out.println(">>> CONEXIÓN EXITOSA A BD <<<");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                try {
                    sfd = new ServerSocket(7000);
                    System.out.println(">>> SERVIDOR ONLINE EN PUERTO 7000 <<<");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                while (true) {
                    try {
                        Socket socket = sfd.accept();
                        new Flujo(socket).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        hiloServidor.start();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Login ventanaLogin = new Login();
                    ventanaLogin.setVisible(true);
                    System.out.println(">>> INTERFAZ GRÁFICA ABIERTA <<<");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}