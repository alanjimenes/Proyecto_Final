package Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import logico.Clinica;
import logico.Control;
import logico.User;

public class Server {

	public static void main (String args[]) {
		ServerSocket sfd = null;

		System.out.println("--- INICIANDO SERVIDOR ---");

		boolean usuariosCargados = false;
		boolean clinicaCargada = false;

		System.out.print("Cargando Usuarios...");
		usuariosCargados = cargarUsuariosIndividual();

		System.out.print("Cargando Datos Clínicos...");
		clinicaCargada = cargarClinicaIndividual();

		if (!usuariosCargados || !clinicaCargada) {
			System.out.println(" Ficheros principales dañados o faltantes.");
			System.out.println(" Intentando restaurar desde 'RespaldoTotal.dat'...");

			boolean rescateExitoso = recuperarDesdeRespaldoTotal();

			if (rescateExitoso) {
				System.out.println("¡SISTEMA RESTAURADO DESDE EL RESPALDO TOTAL!");
				guardarUsuariosIndividual();
				Clinica.getInstancia().guardarDatosClinica();
			} else {
				System.out.println(" No hay respaldo válido. Iniciando sistema desde CERO.");
				if (Control.getInstance().getMisUsuarios().isEmpty()) {
					try {
						User aux = new User("Administrador", "Admin", "Admin", "Admin");
						Control.getInstance().regUser(aux);
						generarRespaldoTotal();
					} catch (Exception e) {}
				}
			}
		}

		try {
			sfd = new ServerSocket(7000);
			System.out.println(" Servidor ONLINE en puerto 7000");
		}
		catch (IOException ioe) {
			System.out.println(" Error puerto 7000: " + ioe);
			System.exit(1);
		}

		while (true) {
			try {
				Socket nsfd = sfd.accept();
				Flujo flujo = new Flujo(nsfd);
				flujo.start();
			}
			catch(IOException ioe) {
				System.out.println("Error: "+ioe);
			}
		}
	}

	private static boolean cargarUsuariosIndividual() {
		try {
			FileInputStream fis = new FileInputStream("Usuarios.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Control temp = (Control) ois.readObject();
			Control.setControl(temp);
			ois.close();
			System.out.println(" OK");
			return true;
		} catch (Exception e) {
			System.out.println(" FALLÓ");
			return false;
		}
	}

	private static boolean cargarClinicaIndividual() {
		try {
			FileInputStream fis = new FileInputStream("clinica.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Clinica temp = (Clinica) ois.readObject();
			Clinica.setInstancia(temp);
			ois.close();
			Clinica.getInstancia().refrescarRelaciones();
			System.out.println(" OK");
			return true;
		} catch (Exception e) {
			System.out.println(" FALLÓ");
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
			fos.close();
		} catch (Exception e) {
			System.out.println("Error creando RespaldoTotal: " + e.getMessage());
		}
	}

	public static boolean recuperarDesdeRespaldoTotal() {
		try {
			FileInputStream fis = new FileInputStream("RespaldoTotal.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);

			Control ctrlRecuperado = (Control) ois.readObject();
			Control.setControl(ctrlRecuperado);

			Clinica clinicaRecuperada = (Clinica) ois.readObject();
			Clinica.setInstancia(clinicaRecuperada);
			Clinica.getInstancia().refrescarRelaciones();

			ois.close();
			fis.close();
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

		} catch (Exception e) { e.printStackTrace(); }
	}
}