package Visual;

import java.io.*;
import java.net.*;
import logico.PaqueteDeDatos;

public class ClienteSocket {

	static String HOST = "127.0.0.1";
	static int PUERTO = 7000;

	public static Object enviar(String comando, Object dato) {
		Object respuesta = null;
		Socket sfd = null;

		try {
			sfd = new Socket(HOST, PUERTO);

			ObjectOutputStream SalidaSocket = new ObjectOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
			SalidaSocket.flush();
			ObjectInputStream EntradaSocket = new ObjectInputStream(new BufferedInputStream(sfd.getInputStream()));

			PaqueteDeDatos paquete = new PaqueteDeDatos(comando, dato);
			SalidaSocket.writeObject(paquete);
			SalidaSocket.flush();
			PaqueteDeDatos paqueteRecibido = (PaqueteDeDatos) EntradaSocket.readObject();
			respuesta = paqueteRecibido.getRespuesta();

		}
		catch (Exception e) {
			System.out.println("Error en Cliente: " + e);
		}
		finally {
			try { if(sfd!=null) sfd.close(); } catch(Exception e) {}
		}

		return respuesta;
	}
}