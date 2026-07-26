package Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import logico.Control;
import logico.User;
import Utils.ConexionDB;

public class Server {

	public static void main(String args[]) {
		ServerSocket sfd = null;

		System.out.println("--- INICIANDO SERVIDOR CLINICA ---");

		// 1. Probamos la conexión a SQL Server de arranque
		try {
			ConexionDB.getConexion();
			System.out.println(">>> CONEXIÓN A BASE DE DATOS SQL SERVER: ¡EXITOSA! <<<");
		} catch (Exception e) {
			System.err.println(">>> ERROR FATAL: No se pudo conectar a SQL Server. Verifica credenciales y TCP/IP. <<<");
			e.printStackTrace();
			System.exit(1);
		}

		// 2. Cargamos solo los usuarios (hasta que los migremos a BD también)
		boolean usuariosCargados = cargarUsuariosIndividual();
		if (!usuariosCargados) {
			System.out.println("No se encontraron usuarios. Iniciando sistema limpio y creando Admin por defecto.");
			if (Control.getInstance().getMisUsuarios().isEmpty()) {
				User admin = new User("Administrador", "Admin", "Admin", "000-0000000-0");
				Control.getInstance().regUser(admin);
				guardarUsuariosIndividual();
			}
		} else {
			System.out.println("Usuarios cargados correctamente en memoria.");
		}

		// 3. Levantamos el Socket
		try {
			sfd = new ServerSocket(7000);
			System.out.println(">>> SERVIDOR ONLINE EN PUERTO 7000 <<<");
		} catch (IOException ioe) {
			System.err.println("Error fatal al abrir el puerto 7000. ¿Ya está corriendo el servidor?");
			System.exit(1);
		}

		while (true) {
			try {
				Socket nsfd = sfd.accept();
				Flujo flujo = new Flujo(nsfd);
				flujo.start();
			} catch (IOException ioe) {
				System.out.println("Error aceptando conexión: " + ioe);
			}
		}
	}

	private static boolean cargarUsuariosIndividual() {
		try {
			File f = new File("Usuarios.dat");
			if (!f.exists())
				return false;

			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Control temp = (Control) ois.readObject();
			Control.setControl(temp);
			ois.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void guardarUsuariosIndividual() {
		try {
			FileOutputStream fos = new FileOutputStream("Usuarios.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(Control.getInstance());
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}