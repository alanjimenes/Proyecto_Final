package Servidor;

import java.net.*;
import java.io.*;
import logico.*;

public class Flujo extends Thread {
	Socket nsfd;
	ObjectInputStream FlujoLectura;
	ObjectOutputStream FlujoEscritura;

	public Flujo(Socket sfd) {
		nsfd = sfd;
		try {
			FlujoEscritura = new ObjectOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
			FlujoEscritura.flush();
			FlujoLectura = new ObjectInputStream(new BufferedInputStream(sfd.getInputStream()));
		} catch (IOException ioe) {
			System.out.println("IOException(Flujo): " + ioe);
		}
	}

	public void run() {
		try {
			PaqueteDeDatos paquete = (PaqueteDeDatos) FlujoLectura.readObject();
			String comando = paquete.getComando();

			// LOGIN
			if (comando.equalsIgnoreCase("LOGIN")) {
				User u = (User) paquete.getObjeto();
				boolean log = Control.getInstance().confirmLogin(u.getUsuario(), u.getPassword());
				if (log) {
					paquete.setRespuesta(Control.getLoginUser());
				} else {
					paquete.setRespuesta(null);
				}
			}

			// GESTIÓN DE USUARIOS
			else if (comando.equalsIgnoreCase("REG_USER")) {
				User u = (User) paquete.getObjeto();
				Control.getInstance().regUser(u);
				Clinica.guardarUsuarios();
				Server.guardarUsuariosIndividual();
				paquete.setRespuesta(true);
			}

			// GESTION DE ENFERMEDADES
			else if (comando.equalsIgnoreCase("REG_ENFERMEDAD")) {
				Enfermedad enf = (Enfermedad) paquete.getObjeto();
				boolean exito = Clinica.getInstancia().agregarEnfermedad(enf);
				if (exito)
					Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(exito);
			} else if (comando.equalsIgnoreCase("UPDATE_ENFERMEDAD")) {
				Enfermedad enfNueva = (Enfermedad) paquete.getObjeto();
				for (Enfermedad e : Clinica.getInstancia().getEnfermedades()) {
					if (e.getCodigo_sick().equals(enfNueva.getCodigo_sick())) {
						e.setNombre(enfNueva.getNombre());
						e.setDescripcion(enfNueva.getDescripcion());
						e.setVigilancia(enfNueva.isVigilancia());
						break;
					}
				}
				Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(true);
			} else if (comando.equalsIgnoreCase("LISTAR_ENFERMEDADES")) {
				paquete.setRespuesta(Clinica.getInstancia().getEnfermedades());
			}

			// GESTIÓN DE MEDICOS
			else if (comando.equalsIgnoreCase("REG_MEDICO")) {
				Medico m = (Medico) paquete.getObjeto();
				boolean exito = Clinica.getInstancia().agregarMedico(m);
				if (exito)
					Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(exito);
			} else if (comando.equalsIgnoreCase("LISTAR_MEDICOS")) {
				paquete.setRespuesta(Clinica.getInstancia().getMedicos());
			} else if (comando.equalsIgnoreCase("BUSCAR_MEDICO")) {
				String cedula = (String) paquete.getObjeto();
				paquete.setRespuesta(Clinica.getInstancia().buscarMedicoCedula(cedula));
			} else if (comando.equalsIgnoreCase("UPDATE_MEDICO")) {
				Medico m = (Medico) paquete.getObjeto();
				Clinica.getInstancia().actualizarMedico(m);
				Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(true);
			} else if (comando.equalsIgnoreCase("DELETE_MEDICO")) {
				Medico m = (Medico) paquete.getObjeto();
				boolean exito = Clinica.getInstancia().desactivarMedico(m.getCedula());
				paquete.setRespuesta(exito);
			}

			// GESTIÓN DE CLIENTES (PACIENTES)
			else if (comando.equalsIgnoreCase("REG_CLIENTE")) {
				Cliente cli = (Cliente) paquete.getObjeto();
				Clinica.getInstancia().actualizarCliente(cli);
				if (Clinica.getInstancia().buscarClientePorCodigo(cli.getNumExpediente()) == null) {
					Clinica.getInstancia().insertarCliente(cli);
				}
				Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(true);
			} else if (comando.equalsIgnoreCase("LISTAR_CLIENTES")) {
				paquete.setRespuesta(Clinica.getInstancia().getClientes());
			} else if (comando.equalsIgnoreCase("BUSCAR_CLIENTE")) {
				String codigo = (String) paquete.getObjeto();
				paquete.setRespuesta(Clinica.getInstancia().buscarClientePorCodigo(codigo));
			} else if (comando.equalsIgnoreCase("BUSCAR_CLIENTE_CEDULA")) {
				String cedula = (String) paquete.getObjeto();
				Cliente encontrado = null;
				for (Cliente c : Clinica.getInstancia().getClientes()) {
					if (c.getCedula().equals(cedula)) {
						encontrado = c;
						break;
					}
				}
				paquete.setRespuesta(encontrado);
			} else if (comando.equalsIgnoreCase("UPDATE_CLIENTE")) {
				Cliente c = (Cliente) paquete.getObjeto();
				Clinica.getInstancia().actualizarCliente(c);
				Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(true);
			}

			// GESTIÓN DE ESPECIALIDADES
			else if (comando.equalsIgnoreCase("REG_ESPECIALIDAD")) {
				Especialidad esp = (Especialidad) paquete.getObjeto();
				Clinica.getInstancia().agregarEspecialidad(esp);
				Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(true);
			} else if (comando.equalsIgnoreCase("LISTAR_ESPECIALIDADES")) {
				paquete.setRespuesta(Clinica.getInstancia().getEspecialidades());
			} else if (comando.equalsIgnoreCase("BUSCAR_ESPECIALIDAD_NOMBRE")) {
				String nombre = (String) paquete.getObjeto();
				paquete.setRespuesta(Clinica.getInstancia().buscarEspecialidadPorNombre(nombre));
			}

			// GESTIÓN DE CITAS
			else if (comando.equalsIgnoreCase("REG_CITA")) {
				Cita c = (Cita) paquete.getObjeto();
				boolean exito = Clinica.getInstancia().crearCita(c.getFechaHora(), c.getMedico().getCedula(),
						c.getCliente().getNumExpediente(), c.getMotivo());
				if (exito)
					Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(exito);
			} else if (comando.equalsIgnoreCase("LISTAR_CITAS")) {
				Clinica.getInstancia().refrescarRelaciones();
				paquete.setRespuesta(Clinica.getInstancia().getCitas());
			} else if (comando.equalsIgnoreCase("BUSCAR_CITA")) {
				String codigo = (String) paquete.getObjeto();
				paquete.setRespuesta(Clinica.getInstancia().buscarCita(codigo));
			} else if (comando.equalsIgnoreCase("EDIT_CITA")) {
				Cita citaModificada = (Cita) paquete.getObjeto();
				Cita original = Clinica.getInstancia().buscarCita(citaModificada.getCodigo_cita());
				boolean exito = Clinica.getInstancia().editCita(original, citaModificada.getFechaHora(),
						citaModificada.getMedico());
				paquete.setRespuesta(exito);
			} else if (comando.equalsIgnoreCase("CANCEL_CITA")) {
				Cita c = (Cita) paquete.getObjeto();
				Cita real = Clinica.getInstancia().buscarCita(c.getCodigo_cita());
				boolean exito = false;
				if (real != null) {
					exito = Clinica.getInstancia().cancelCita(real);
					Clinica.getInstancia().guardarDatosClinica();
				}
				paquete.setRespuesta(exito);
			}

			// GESTION DE CONSULTAS
			else if (comando.equalsIgnoreCase("REG_CONSULTA")) {
				Consulta c = (Consulta) paquete.getObjeto();
				boolean exito = false;
				if (c != null && c.getCliente() != null) {
					String numExp = c.getCliente().getNumExpediente();
					Cliente clienteReal = Clinica.getInstancia().buscarClientePorCodigo(numExp);
					if (clienteReal != null) {
						if (clienteReal.getHistorial() == null)
							clienteReal.setHistorial(new Historial("HIST-" + clienteReal.getNumExpediente()));
						clienteReal.getHistorial().getConsultas().add(c);
						Clinica.getInstancia().guardarDatosClinica();
						exito = true;
					}
				}
				paquete.setRespuesta(exito);
			}

			// GESTION DE VACUNAS
			else if (comando.equalsIgnoreCase("REG_VACUNA")) {
				Vacuna v = (Vacuna) paquete.getObjeto();
				boolean exito = Clinica.getInstancia().agregarVacuna(v);
				if (exito)
					Clinica.getInstancia().guardarDatosClinica();
				paquete.setRespuesta(exito);
			} else if (comando.equalsIgnoreCase("APLICAR_VACUNA")) {
				RegistroVacunacion reg = (RegistroVacunacion) paquete.getObjeto();
				Cliente clienteReal = Clinica.getInstancia()
						.buscarClientePorCodigo(reg.getCliente().getNumExpediente());
				if (clienteReal != null) {
					clienteReal.getRegVacunas().add(reg);
					Clinica.getInstancia().guardarDatosClinica();
					paquete.setRespuesta(true);
				} else {
					paquete.setRespuesta(false);
				}
			} else if (comando.equalsIgnoreCase("LISTAR_VACUNAS")) {
				paquete.setRespuesta(Clinica.getInstancia().getVacunas());
			}
			FlujoEscritura.writeObject(paquete);
			FlujoEscritura.flush();

		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error en flujo: " + e);
		} finally {
			try {
				nsfd.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}