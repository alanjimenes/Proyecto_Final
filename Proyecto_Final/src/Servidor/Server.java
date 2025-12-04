package Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import logico.Clinica;
import logico.Control;
import logico.User;

public class Server {

	public static void main(String args[]) {
		ServerSocket sfd = null;

		System.out.println("--- INICIANDO SERVIDOR CLINICA ---");

		boolean usuariosCargados = cargarUsuariosIndividual();
		boolean clinicaCargada = cargarClinicaIndividual();

		if (!usuariosCargados || !clinicaCargada) {
			System.err.println("Advertencia: Archivos de datos principales no encontrados o corruptos.");
			System.out.println("Intentando recuperar desde RespaldoTotal.dat...");

			if (recuperarDesdeRespaldoTotal()) {
				System.out.println("¡SISTEMA RECUPERADO EXITOSAMENTE!");
				guardarUsuariosIndividual();
				Clinica.getInstancia().guardarDatosClinica();
			} else {
				System.out.println("No se encontró respaldo. Iniciando sistema limpio.");
				if (Control.getInstance().getMisUsuarios().isEmpty()) {
					try {
						User admin = new User("Administrador", "Admin", "Admin", "000-0000000-0");
						Control.getInstance().regUser(admin);
						guardarUsuariosIndividual();
					} catch (Exception e) {
					}
				}
			}
		} else {
			System.out.println("Carga de datos completada correctamente.");
		}

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

	private static boolean cargarClinicaIndividual() {
		try {
			File f = new File("clinica.dat");
			if (!f.exists())
				return false;

			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Clinica temp = (Clinica) ois.readObject();
			Clinica.setInstancia(temp);

			Clinica.getInstancia().refrescarRelaciones();

			ois.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void generarRespaldoTotal() {
		try {
			FileOutputStream fos = new FileOutputStream("RespaldoTotal.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(Control.getInstance());
			oos.writeObject(Clinica.getInstancia());
			oos.close();
		} catch (Exception e) {
			System.out.println("Error generando respaldo: " + e.getMessage());
		}
	}

	public static boolean recuperarDesdeRespaldoTotal() {
		try {
			File f = new File("RespaldoTotal.dat");
			if (!f.exists())
				return false;

			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);

			Control ctrlRecuperado = (Control) ois.readObject();
			Control.setControl(ctrlRecuperado);

			Clinica clinicaRecuperada = (Clinica) ois.readObject();
			Clinica.setInstancia(clinicaRecuperada);
			Clinica.getInstancia().refrescarRelaciones();

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
			generarRespaldoTotal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}