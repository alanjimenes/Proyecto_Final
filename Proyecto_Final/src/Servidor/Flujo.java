package Servidor;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
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
			System.out.println("Error creando flujos: " + ioe);
		}
	}

	public void run() {
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
					boolean exito = Clinica.getInstancia().agregarEnfermedad(enf);
					if (exito)
						Clinica.getInstancia().guardarDatosClinica();
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("UPDATE_ENFERMEDAD")) {
					Enfermedad enfNueva = (Enfermedad) paquete.getObjeto();
					ArrayList<Enfermedad> list = Clinica.getInstancia().getEnfermedades();
					boolean found = false;
					for (Enfermedad e : list) {
						if (e.getCodigo_sick().equalsIgnoreCase(enfNueva.getCodigo_sick())) {
							e.setNombre(enfNueva.getNombre());
							e.setDescripcion(enfNueva.getDescripcion());
							e.setVigilancia(enfNueva.isVigilancia());
							found = true;
							break;
						}
					}
					if (found)
						Clinica.getInstancia().guardarDatosClinica();
					paquete.setRespuesta(found);
				} else if (comando.equalsIgnoreCase("LISTAR_ENFERMEDADES")) {
					paquete.setRespuesta(Clinica.getInstancia().getEnfermedades());
				}

				// --- SECCION: MEDICOS ---
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
					paquete.setRespuesta(true);
				} else if (comando.equalsIgnoreCase("DELETE_MEDICO")) {
					Medico m = (Medico) paquete.getObjeto();
					boolean exito = Clinica.getInstancia().desactivarMedico(m.getCedula());
					paquete.setRespuesta(exito);
				}

				// --- SECCION: CLIENTES ---
				else if (comando.equalsIgnoreCase("REG_CLIENTE")) {
					Cliente cli = (Cliente) paquete.getObjeto();
					Cliente existente = Clinica.getInstancia().buscarClientePorCodigo(cli.getNumExpediente());
					if (existente == null) {
						Clinica.getInstancia().insertarCliente(cli);
					} else {
						Clinica.getInstancia().actualizarCliente(cli);
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

				// --- SECCION: ESPECIALIDADES ---
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

				// --- SECCION: CITAS ---
				else if (comando.equalsIgnoreCase("REG_CITA")) {
					Cita c = (Cita) paquete.getObjeto();
					boolean exito = Clinica.getInstancia().crearCita(c.getFechaHora(), c.getMedico().getCedula(),
							c.getCliente().getNumExpediente(), c.getMotivo());
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("LISTAR_CITAS")) {
					Clinica.getInstancia().refrescarRelaciones();
					paquete.setRespuesta(Clinica.getInstancia().getCitas());
				} else if (comando.equalsIgnoreCase("BUSCAR_CITA")) {
					String codigo = (String) paquete.getObjeto();
					paquete.setRespuesta(Clinica.getInstancia().buscarCita(codigo));
				} else if (comando.equalsIgnoreCase("EDIT_CITA")) {
					Cita citaMod = (Cita) paquete.getObjeto();
					Cita original = Clinica.getInstancia().buscarCita(citaMod.getCodigo_cita());
					boolean exito = Clinica.getInstancia().editCita(original, citaMod.getFechaHora(),
							citaMod.getMedico());
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("CANCEL_CITA")) {
					Cita c = (Cita) paquete.getObjeto();
					Cita real = Clinica.getInstancia().buscarCita(c.getCodigo_cita());
					boolean exito = false;
					if (real != null) {
						exito = Clinica.getInstancia().cancelCita(real);
					}
					paquete.setRespuesta(exito);
				}

				// --- SECCION: CONSULTAS E HISTORIAL ---
				else if (comando.equalsIgnoreCase("REG_CONSULTA")) {
					Consulta c = (Consulta) paquete.getObjeto();
					boolean exito = false;
					// BLINDAJE ANTI-EXPLOSIONES AQUI
					if (c != null && c.getCliente() != null && c.getCliente().getNumExpediente() != null) {
						Cliente clienteReal = Clinica.getInstancia()
								.buscarClientePorCodigo(c.getCliente().getNumExpediente());
						if (clienteReal != null) {
							if (clienteReal.getHistorial() == null) {
								clienteReal.setHistorial(new Historial("HIST-" + clienteReal.getNumExpediente()));
							}
							clienteReal.getHistorial().agregarConsulta(c);

							if (c.getMedico() != null) {
								Medico medicoReal = Clinica.getInstancia()
										.buscarMedicoCedula(c.getMedico().getCedula());
								if (medicoReal != null) {
									medicoReal.agregarConsultaRealizada(c);
								}
							}
							Clinica.getInstancia().guardarDatosClinica();
							exito = true;
						}
					}
					paquete.setRespuesta(exito);
				}

				// --- SECCION: VACUNAS ---
				else if (comando.equalsIgnoreCase("REG_VACUNA")) {
					Vacuna v = (Vacuna) paquete.getObjeto();
					boolean exito = Clinica.getInstancia().agregarVacuna(v);
					if (exito)
						Clinica.getInstancia().guardarDatosClinica();
					paquete.setRespuesta(exito);
				} else if (comando.equalsIgnoreCase("LISTAR_VACUNAS")) {
					paquete.setRespuesta(Clinica.getInstancia().getVacunas());
				} else if (comando.equalsIgnoreCase("APLICAR_VACUNA")) {
					RegistroVacunacion reg = (RegistroVacunacion) paquete.getObjeto();
					boolean exito = false;

					// BLINDAJE ANTI-EXPLOSIONES AQUI TAMBIEN
					if (reg != null && reg.getCliente() != null && reg.getCliente().getNumExpediente() != null) {
						Cliente clienteReal = Clinica.getInstancia()
								.buscarClientePorCodigo(reg.getCliente().getNumExpediente());
						if (clienteReal != null) {
							if (clienteReal.getRegVacunas() == null) {
								clienteReal.setRegVacunas(new ArrayList<>());
							}
							clienteReal.getRegVacunas().add(reg);
							Clinica.getInstancia().guardarDatosClinica();
							exito = true;
						}
					}
					paquete.setRespuesta(exito);
				}

				FlujoEscritura.writeObject(paquete);
				FlujoEscritura.flush();
			}
		} catch (SocketException se) {
			// Cliente cerrado
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error en flujo de datos: " + e);
		} finally {
			try {
				nsfd.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}