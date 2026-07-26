package Servidor;

import logico.*;
import Servicios.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;

public class Flujo extends Thread {
	Socket nsfd;
	ObjectInputStream FlujoLectura = null;
	ObjectOutputStream FlujoEscritura = null;

	public Flujo(Socket sfd) {
		nsfd = sfd;
		try {
			FlujoEscritura = new ObjectOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
			FlujoEscritura.flush();

			FlujoLectura = new ObjectInputStream(new BufferedInputStream(sfd.getInputStream()));
		}
		catch(IOException ioe) {
			System.out.println("Error creando flujos: "+ioe);
		}
	}

	public void run() {
		if (FlujoLectura == null || FlujoEscritura == null) {
			try { if (nsfd != null) nsfd.close(); } catch (IOException e) { e.printStackTrace(); }
			return;
		}

		// Inicialización de los servicios de Base de Datos
		Servicios.ClienteService clienteService = new Servicios.ClienteService();
		Servicios.MedicoService medicoService = new Servicios.MedicoService();
		Servicios.EnfermedadService enfermedadService = new Servicios.EnfermedadService();
		Servicios.EspecialidadService especialidadService = new Servicios.EspecialidadService();
		Servicios.CitaService citaService = new Servicios.CitaService();
		Servicios.ConsultaService consultaService = new Servicios.ConsultaService();
		Servicios.VacunaService vacunaService = new Servicios.VacunaService();

		try {
			while (true) {
				PaqueteDeDatos paquete = null;
				try {
					paquete = (PaqueteDeDatos) FlujoLectura.readObject();
				} catch (EOFException e) {
					break;
				}

				if (paquete == null)
					break;

				String comando = paquete.getComando();

				// --- SECCION: LOGIN Y USUARIOS ---
				if (comando.equalsIgnoreCase("LOGIN")) {
					User u = (User) paquete.getObjeto();
					User userLogueado = null;
					if (Control.getInstance().getMisUsuarios() != null) {
						for (User user : Control.getInstance().getMisUsuarios()) {
							if (user.getUsuario().equalsIgnoreCase(u.getUsuario())
									&& user.getPassword().equals(u.getPassword())) {
								userLogueado = user;
								break;
							}
						}
					}
					paquete.setRespuesta(userLogueado);
				} else if (comando.equalsIgnoreCase("REG_USER")) {
					User u = (User) paquete.getObjeto();
					boolean existe = Control.getInstance().userExist(u.getUsuario());
					if (!existe) {
						Control.getInstance().regUser(u);
						Server.guardarUsuariosIndividual();
						paquete.setRespuesta(true);
					} else {
						paquete.setRespuesta(false);
					}
				}

				// --- SECCION: ENFERMEDADES ---
				else if (comando.equalsIgnoreCase("REG_ENFERMEDAD")) {
					Enfermedad enf = (Enfermedad) paquete.getObjeto();
					boolean exito = enfermedadService.agregarEnfermedad(enf);
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("UPDATE_ENFERMEDAD")) {
					// Lógica de actualización si la implementas en EnfermedadService
					paquete.setRespuesta(false);
				} else if (comando.equalsIgnoreCase("LISTAR_ENFERMEDADES")) {
					paquete.setRespuesta(enfermedadService.listarEnfermedades());
				}

				// --- SECCION: MEDICOS ---
				else if (comando.equalsIgnoreCase("REG_MEDICO")) {
					Medico m = (Medico) paquete.getObjeto();
					String codEspLimpio = m.getEspecialidad().getCodigo_espe().replaceAll("[^0-9]", "");
					int idEspecialidad = Integer.parseInt(codEspLimpio);
					boolean exito = medicoService.agregarMedico(m, 0, idEspecialidad);
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("LISTAR_MEDICOS")) {
					paquete.setRespuesta(medicoService.listarMedicos());
				} else if (comando.equalsIgnoreCase("BUSCAR_MEDICO")) {
					String cedula = (String) paquete.getObjeto();
					paquete.setRespuesta(medicoService.buscarMedicoCedula(cedula));
				} else if (comando.equalsIgnoreCase("UPDATE_MEDICO")) {
					Medico m = (Medico) paquete.getObjeto();
					boolean exito = medicoService.actualizarMedico(m);
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("DELETE_MEDICO")) {
					Medico m = (Medico) paquete.getObjeto();
					boolean exito = medicoService.desactivarMedico(m.getCedula());
					paquete.setRespuesta(exito);
				}

				// --- SECCION: CLIENTES ---
				else if (comando.equalsIgnoreCase("REG_CLIENTE")) {
					Cliente cli = (Cliente) paquete.getObjeto();
					Cliente existente = clienteService.buscarClientePorCodigo(cli.getNumExpediente());
					boolean exito;
					if (existente == null) {
						exito = clienteService.registrarNuevoCliente(cli);
					} else {
						exito = clienteService.actualizarCliente(cli);
					}
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("LISTAR_CLIENTES")) {
					paquete.setRespuesta(clienteService.getClientes());
				} else if (comando.equalsIgnoreCase("BUSCAR_CLIENTE")) {
					String codigo = (String) paquete.getObjeto();
					paquete.setRespuesta(clienteService.buscarClientePorCodigo(codigo));
				} else if (comando.equalsIgnoreCase("BUSCAR_CLIENTE_CEDULA")) {
					String cedula = (String) paquete.getObjeto();
					paquete.setRespuesta(clienteService.buscarClientePorCedula(cedula));
				} else if (comando.equalsIgnoreCase("UPDATE_CLIENTE")) {
					Cliente c = (Cliente) paquete.getObjeto();
					boolean exito = clienteService.actualizarCliente(c);
					paquete.setRespuesta(exito);
				}

				// --- SECCION: ESPECIALIDADES ---
				else if (comando.equalsIgnoreCase("REG_ESPECIALIDAD")) {
					Especialidad esp = (Especialidad) paquete.getObjeto();
					boolean exito = especialidadService.registrarEspecialidad(esp);
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("LISTAR_ESPECIALIDADES")) {
					paquete.setRespuesta(especialidadService.listarEspecialidades());
				} else if (comando.equalsIgnoreCase("BUSCAR_ESPECIALIDAD_NOMBRE")) {
					String nombre = (String) paquete.getObjeto();
					paquete.setRespuesta(especialidadService.buscarEspecialidadPorNombre(nombre));
				} else if (comando.equalsIgnoreCase("UPDATE_ESPECIALIDAD")) {
					Especialidad esp = (Especialidad) paquete.getObjeto();
					boolean exito = especialidadService.actualizarEspecialidad(esp);
					paquete.setRespuesta(exito);
				}

				// --- SECCION: CITAS ---
				else if (comando.equalsIgnoreCase("REG_CITA")) {
					Cita c = (Cita) paquete.getObjeto();
					// Se debe ajustar para buscar los IDs reales en la base de datos si es necesario
					int idMedico = 1; // Reemplazar por búsqueda de ID
					int idCliente = 1; // Reemplazar por búsqueda de ID
					boolean exito = citaService.crearCita(c, idMedico, idCliente);
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("LISTAR_CITAS")) {
					paquete.setRespuesta(citaService.getTodasLasCitas());
				} else if (comando.equalsIgnoreCase("BUSCAR_CITA")) {
					String codigo = (String) paquete.getObjeto();
					paquete.setRespuesta(citaService.buscarCita(Integer.parseInt(codigo)));
				} else if (comando.equalsIgnoreCase("EDIT_CITA")) {
					Cita citaMod = (Cita) paquete.getObjeto();
					int idMedico = 1; // Ajustar ID
					boolean exito = citaService.editCita(Integer.parseInt(citaMod.getCodigo_cita()), citaMod.getFechaHora(), idMedico);
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("CANCEL_CITA")) {
					Cita c = (Cita) paquete.getObjeto();
					boolean exito = citaService.cancelCita(Integer.parseInt(c.getCodigo_cita()));
					paquete.setRespuesta(exito);
				}

				// --- SECCION: CONSULTAS E HISTORIAL ---
				else if (comando.equalsIgnoreCase("REG_CONSULTA")) {
					Consulta c = (Consulta) paquete.getObjeto();
					int idMedico = 1; // Ajustar ID
					int idCliente = 1; // Ajustar ID
					boolean exito = consultaService.guardarConsulta(1, c.getSintomas(), c.getDiagnostico(), c.getEnfermedadesDiag());
					paquete.setRespuesta(exito);
				}

				// --- SECCION: VACUNAS ---
				else if (comando.equalsIgnoreCase("REG_VACUNA")) {
					Vacuna v = (Vacuna) paquete.getObjeto();
					boolean exito = vacunaService.agregarVacuna(v);
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("LISTAR_VACUNAS")) {
					paquete.setRespuesta(vacunaService.listarVacunas());
				} else if (comando.equalsIgnoreCase("APLICAR_VACUNA")) {
					RegistroVacunacion reg = (RegistroVacunacion) paquete.getObjeto();
					int idCliente = 1; // Ajustar ID
					int idVacuna = Integer.parseInt(reg.getVacuna().getCodigo_vacun());
					boolean exito = vacunaService.aplicarVacunaCliente(idCliente, idVacuna, Timestamp.valueOf(reg.getFecha().atStartOfDay()));
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("UPDATE_VACUNA")) {
					// Lógica de actualización si la implementas en VacunaService
					paquete.setRespuesta(false);
				}

				FlujoEscritura.writeObject(paquete);
				FlujoEscritura.flush();
			}
		}
		catch(SocketException se) {
			System.out.println("Cliente desconectado (Socket cerrado).");
		}
		catch(IOException | ClassNotFoundException e) {
			System.out.println("Error en flujo de datos: " + e);
		}
		finally {
			try {
				if(nsfd != null) nsfd.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
}