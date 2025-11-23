package Servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import logico.Clinica;
import logico.Control;
import logico.User;

public class Server {

	public static void main (String args[]) {
		ServerSocket sfd = null;

		System.out.println("Cargando datos del sistema...");
		cargarUsuariosEnServidor(); 
		Clinica.cargarDatosClinica(); 

		try {
			sfd = new ServerSocket(7000);
			System.out.println("Servidor de Clínica Iniciado en puerto 7000");
		}
		catch (IOException ioe) {
			System.out.println("Comunicación rechazada."+ioe);
			System.exit(1);
		}

		while (true) {
			try {
				Socket nsfd = sfd.accept();
				System.out.println("Conexion aceptada de: "+nsfd.getInetAddress());

				Flujo flujo = new Flujo(nsfd);
				flujo.start();
			}
			catch(IOException ioe) {
				System.out.println("Error: "+ioe);
			}
		}
	}

	private static void cargarUsuariosEnServidor() {
		FileInputStream usuarios;
		FileOutputStream usuarios2;
		ObjectInputStream usuariosRead;
		ObjectOutputStream usuariosWrite;
		try {
			usuarios = new FileInputStream("Usuarios.dat");
			usuariosRead = new ObjectInputStream(usuarios);
			Control temp = (Control) usuariosRead.readObject();
			Control.setControl(temp);
			usuarios.close();
			usuariosRead.close();

		} catch (FileNotFoundException e) {
			try {
				usuarios2 = new FileOutputStream("Usuarios.dat");
				usuariosWrite = new ObjectOutputStream(usuarios2);
				User aux = new User("Administrador", "Admin", "Admin", "Admin");
				Control.getInstance().regUser(aux);
				usuariosWrite.writeObject(Control.getInstance());
				usuarios2.close();
				usuariosWrite.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}