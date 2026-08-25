package Utils;

import java.io.*;
import java.net.*;
import logico.PaqueteDeDatos;

public class ClienteSocket {

	static String HOST = "127.0.0.1";
	static int PUERTO = 7000;


	/**
	 * PROCESO: Transmite una orden al servidor por socket de forma sincrónica y retorna la respuesta procesada.
	 * * ENTRADAS:
	 * - comando: Acción que ejecutará el servidor.
	 * - dato: Objeto adjunto con la información de entrada para el comando.
	 * * SALIDA: Objeto resultante devuelto por la capa de servicios del servidor.
	 * * FLUJO DE LLAMADAS:
	 * 1. Instancia el Socket en el puerto asignado.
	 * 2. Prepara ObjectOutputStream y ObjectInputStream.
	 * 3. Crea e instancia PaqueteDeDatos(comando, dato).
	 * 4. Escribe el objeto en el flujo mediante SalidaSocket.writeObject(paquete).
	 * 5. Captura el PaqueteDeDatos enviado como respuesta y retorna paqueteRecibido.getRespuesta().
	 * 6. Cierra la conexión socket de forma segura.
	 */

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

		} catch (ConnectException e) {
			respuesta = null;
		} catch (Exception e) {
			e.printStackTrace();
			respuesta = null;
		} finally {
			try {
				if (sfd != null)
					sfd.close();
			} catch (Exception e) {
			}
		}

		return respuesta;
	}
}