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

				switch (comando.toUpperCase()) {
					case "LOGIN":
						User u = (User) paquete.getObjeto();
						User userLogueado = null;
						if (Control.getInstance().getMisUsuarios() != null) {
							for (User user : Control.getInstance().getMisUsuarios()) {
								if (user.getUsuario().equalsIgnoreCase(u.getUsuario()) && user.getPassword().equals(u.getPassword())) {
									userLogueado = user;
									break;
								}
							}
						}
						paquete.setRespuesta(userLogueado);
						break;

					case "REG_USER":
						User uReg = (User) paquete.getObjeto();
						boolean existe = Control.getInstance().userExist(uReg.getUsuario());
						if (!existe) {
							Control.getInstance().regUser(uReg);
							Server.guardarUsuariosIndividual();
							paquete.setRespuesta(true);
						} else {
							paquete.setRespuesta(false);
						}
						break;

					case "REG_ENFERMEDAD":
						Enfermedad enf = (Enfermedad) paquete.getObjeto();
						boolean exitoEnf = enfermedadService.agregarEnfermedad(enf);
						paquete.setRespuesta(exitoEnf);
						break;

					case "UPDATE_ENFERMEDAD":
						paquete.setRespuesta(false);
						break;

					case "LISTAR_ENFERMEDADES":
						paquete.setRespuesta(enfermedadService.listarEnfermedades());
						break;

					case "REG_MEDICO":
						Medico m = (Medico) paquete.getObjeto();
						String codEspLimpio = m.getEspecialidad().getCodigo_espe().replaceAll("[^0-9]", "");
						int idEspecialidad = Integer.parseInt(codEspLimpio);
						boolean exitoMed = medicoService.agregarMedico(m, 0, idEspecialidad);
						paquete.setRespuesta(exitoMed);
						break;

					case "LISTAR_MEDICOS":
						paquete.setRespuesta(medicoService.listarMedicos());
						break;

					case "BUSCAR_MEDICO":
						String cedulaMed = (String) paquete.getObjeto();
						paquete.setRespuesta(medicoService.buscarMedicoCedula(cedulaMed));
						break;

					case "UPDATE_MEDICO":
						Medico mUpd = (Medico) paquete.getObjeto();
						boolean exitoUpdMed = medicoService.actualizarMedico(mUpd);
						paquete.setRespuesta(exitoUpdMed);
						break;

					case "DELETE_MEDICO":
						Medico mDel = (Medico) paquete.getObjeto();
						boolean exitoDelMed = medicoService.desactivarMedico(mDel.getCedula());
						paquete.setRespuesta(exitoDelMed);
						break;

					case "REG_CLIENTE":
						Cliente cli = (Cliente) paquete.getObjeto();
						Cliente existente = clienteService.buscarClientePorCodigo(cli.getNumExpediente());
						boolean exitoCli;
						if (existente == null) {
							exitoCli = clienteService.registrarNuevoCliente(cli);
						} else {
							exitoCli = clienteService.actualizarCliente(cli);
						}
						paquete.setRespuesta(exitoCli);
						break;

					case "LISTAR_CLIENTES":
						paquete.setRespuesta(clienteService.getClientes());
						break;

					case "BUSCAR_CLIENTE":
						String codigoCli = (String) paquete.getObjeto();
						paquete.setRespuesta(clienteService.buscarClientePorCodigo(codigoCli));
						break;

					case "BUSCAR_CLIENTE_CEDULA":
						String cedulaCli = (String) paquete.getObjeto();
						paquete.setRespuesta(clienteService.buscarClientePorCedula(cedulaCli));
						break;

					case "UPDATE_CLIENTE":
						Cliente cUpd = (Cliente) paquete.getObjeto();
						boolean exitoUpdCli = clienteService.actualizarCliente(cUpd);
						paquete.setRespuesta(exitoUpdCli);
						break;

					case "DELETE_CLIENTE":
						String cedulaEliminar = (String) paquete.getObjeto();
						boolean desactivado = clienteService.desactivarCliente(cedulaEliminar);
						paquete.setRespuesta(desactivado);
						break;

					case "REG_ESPECIALIDAD":
						Especialidad esp = (Especialidad) paquete.getObjeto();
						boolean exitoEsp = especialidadService.registrarEspecialidad(esp);
						paquete.setRespuesta(exitoEsp);
						break;

					case "LISTAR_ESPECIALIDADES":
						paquete.setRespuesta(especialidadService.listarEspecialidades());
						break;

					case "BUSCAR_ESPECIALIDAD_NOMBRE":
						String nombreEsp = (String) paquete.getObjeto();
						paquete.setRespuesta(especialidadService.buscarEspecialidadPorNombre(nombreEsp));
						break;

					case "UPDATE_ESPECIALIDAD":
						Especialidad espUpd = (Especialidad) paquete.getObjeto();
						boolean exitoUpdEsp = especialidadService.actualizarEspecialidad(espUpd);
						paquete.setRespuesta(exitoUpdEsp);
						break;

					case "REG_CITA":
						Cita c = (Cita) paquete.getObjeto();
						int idMedico = 1;
						int idCliente = 1;
						boolean exitoCita = citaService.crearCita(c, idMedico, idCliente);
						paquete.setRespuesta(exitoCita);
						break;

					case "LISTAR_CITAS":
						paquete.setRespuesta(citaService.getTodasLasCitas());
						break;

					case "BUSCAR_CITA":
						String codigoCita = (String) paquete.getObjeto();
						paquete.setRespuesta(citaService.buscarCita(Integer.parseInt(codigoCita)));
						break;

					case "EDIT_CITA":
						Cita citaMod = (Cita) paquete.getObjeto();
						int idMedicoMod = 1;
						boolean exitoEditCita = citaService.editCita(Integer.parseInt(citaMod.getCodigo_cita()), citaMod.getFechaHora(), idMedicoMod);
						paquete.setRespuesta(exitoEditCita);
						break;

					case "CANCEL_CITA":
						Cita cCancel = (Cita) paquete.getObjeto();
						boolean exitoCancelCita = citaService.cancelCita(Integer.parseInt(cCancel.getCodigo_cita()));
						paquete.setRespuesta(exitoCancelCita);
						break;

					case "REG_CONSULTA":
						Consulta cons = (Consulta) paquete.getObjeto();
						boolean exitoCons = consultaService.guardarConsulta(1, cons.getSintomas(), cons.getDiagnostico(), cons.getEnfermedadesDiag());
						paquete.setRespuesta(exitoCons);
						break;

					case "REG_VACUNA":
						Vacuna v = (Vacuna) paquete.getObjeto();
						boolean exitoVac = vacunaService.agregarVacuna(v);
						paquete.setRespuesta(exitoVac);
						break;

					case "LISTAR_VACUNAS":
						paquete.setRespuesta(vacunaService.listarVacunas());
						break;

					case "APLICAR_VACUNA":
						RegistroVacunacion reg = (RegistroVacunacion) paquete.getObjeto();
						int idClienteVac = 1;
						int idVacuna = Integer.parseInt(reg.getVacuna().getCodigo_vacun());
						boolean exitoApliVac = vacunaService.aplicarVacunaCliente(idClienteVac, idVacuna, Timestamp.valueOf(reg.getFecha().atStartOfDay()));
						paquete.setRespuesta(exitoApliVac);
						break;

					case "UPDATE_VACUNA":
						paquete.setRespuesta(false);
						break;

					default:
						paquete.setRespuesta(null);
						break;
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