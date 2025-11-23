package Servidor;

import java.net.*;
import java.io.*;
import logico.*;

public class Flujo extends Thread {
	Socket nsfd;
	ObjectInputStream FlujoLectura;
	ObjectOutputStream FlujoEscritura;

	public Flujo (Socket sfd) {
		nsfd = sfd;
		try {
			FlujoEscritura = new ObjectOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
			FlujoEscritura.flush();
			FlujoLectura = new ObjectInputStream(new BufferedInputStream(sfd.getInputStream()));
		}
		catch(IOException ioe) {
			System.out.println("IOException(Flujo): "+ioe);
		}
	}

	public void run() {
		try {
			PaqueteDeDatos paquete = (PaqueteDeDatos) FlujoLectura.readObject();
			String comando = paquete.getComando();
			//LOGIN
			if (comando.equalsIgnoreCase("LOGIN")) {
				User u = (User) paquete.getObjeto();
				boolean log = Control.getInstance().confirmLogin(u.getUsuario(), u.getPassword());
				if(log) {
					paquete.setRespuesta(Control.getLoginUser());
				} else {
					paquete.setRespuesta(null);
				}
			}
			//GESTIÓN DE USUARIOS
			else if (comando.equalsIgnoreCase("REG_USER")) {
				User u = (User) paquete.getObjeto();
				Control.getInstance().regUser(u);
				Clinica.guardarUsuarios();
				paquete.setRespuesta(true);
			}
			//GESTIÓN DE MEDICOS
			else if (comando.equalsIgnoreCase("REG_MEDICO")) {
				Medico m = (Medico) paquete.getObjeto();
				boolean exito = Clinica.getInstancia().agregarMedico(m);
				if(exito) Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(exito);
			}
			else if (comando.equalsIgnoreCase("LISTAR_MEDICOS")) {
				paquete.setRespuesta(Clinica.getInstancia().getMedicos());
			}
			else if (comando.equalsIgnoreCase("BUSCAR_MEDICO")) {
				String cedula = (String) paquete.getObjeto();
				paquete.setRespuesta(Clinica.getInstancia().buscarMedicoCedula(cedula));
			}
			//GESTIÓN DE CLIENTES (PACIENTES)
			else if (comando.equalsIgnoreCase("REG_CLIENTE")) {
				Cliente cli = (Cliente) paquete.getObjeto();
				Clinica.getInstancia().actualizarCliente(cli);
				if(Clinica.getInstancia().buscarClientePorCodigo(cli.getNumExpediente()) == null) {
					Clinica.getInstancia().insertarCliente(cli);
				}
				Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(true);
			}
			else if (comando.equalsIgnoreCase("LISTAR_CLIENTES")) {
				paquete.setRespuesta(Clinica.getInstancia().getClientes());
			}
			else if (comando.equalsIgnoreCase("BUSCAR_CLIENTE")) {
				String codigo = (String) paquete.getObjeto();
				paquete.setRespuesta(Clinica.getInstancia().buscarClientePorCodigo(codigo));
			}

			//GESTIÓN DE ESPECIALIDADES
			else if (comando.equalsIgnoreCase("REG_ESPECIALIDAD")) {
				Especialidad esp = (Especialidad) paquete.getObjeto();
				Clinica.getInstancia().agregarEspecialidad(esp);
				Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(true);
			}
			else if (comando.equalsIgnoreCase("LISTAR_ESPECIALIDADES")) {
				paquete.setRespuesta(Clinica.getInstancia().getEspecialidades());
			}

			//GESTIÓN DE CITAS 
			else if (comando.equalsIgnoreCase("REG_CITA")) {
				Cita c = (Cita) paquete.getObjeto();
				boolean exito = Clinica.getInstancia().crearCita(
						c.getFechaHora(), 
						c.getMedico().getCedula(), 
						c.getCliente().getNumExpediente()
						);
				if(exito) Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(exito);
			}
			else if (comando.equalsIgnoreCase("LISTAR_CITAS")) {
				Clinica.getInstancia().refrescarRelaciones(); 
				paquete.setRespuesta(Clinica.getInstancia().getCitas());
			}
			else if (comando.equalsIgnoreCase("CANCELAR_CITA")) {
				Cita c = (Cita) paquete.getObjeto();
				Cita citaReal = Clinica.getInstancia().buscarCita(c.getCodigo_cita());
				boolean exito = false;
				if (citaReal != null) {
					exito = Clinica.getInstancia().cancelCita(citaReal);
					if(exito) Clinica.getInstancia().guardarDatosClinica();
				}
				paquete.setRespuesta(exito);
			}
			else if (comando.equalsIgnoreCase("REG_CONSULTA")) {
				paquete.setRespuesta(true); 
			}

			FlujoEscritura.writeObject(paquete);
			FlujoEscritura.flush();

		}
		catch(IOException | ClassNotFoundException e) {
			System.out.println("Error en flujo: " + e);
		}
		finally {
			try {
				nsfd.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
}