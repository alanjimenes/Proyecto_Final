package logico;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Clinica implements Serializable {

	private static final long serialVersionUID = 1L;
	private int genCodigoCliente = 1;
	private int genCodigoCita = 1;
	private int genCodigoConsulta = 1;
	private int genCodigoMedico = 1;
	private int genCodigoUser = 1;
	private ArrayList<Cliente> clientes;
	private ArrayList<Medico> medicos;
	private ArrayList<Enfermedad> enfermedades;
	private ArrayList<Vacuna> vacunas;
	private ArrayList<Cita> citas;
	private Agenda agenda;
	private static ArrayList<User> users;
	private static Clinica instancia = null;
	private ArrayList<Especialidad> especialidades;

	public Clinica() {
		this.clientes = new ArrayList<>();
		this.medicos = new ArrayList<>();
		this.enfermedades = new ArrayList<>();
		this.vacunas = new ArrayList<>();
		this.citas = new ArrayList<>();
		this.agenda = new Agenda();
		this.users = new ArrayList<>();
		this.especialidades = new ArrayList<>();
	}

	public static Clinica getInstancia() {
		if (instancia == null) {
			instancia = new Clinica();
		}
		return instancia;
	}

	public static void setInstancia(Clinica instancia) {
		Clinica.instancia = instancia;
	}

	public int getGenCodigoCliente() {
		return genCodigoCliente;
	}

	public void setGenCodigoPaciente(int genCodigoCliente) {
		this.genCodigoCliente = genCodigoCliente;
	}

	public int getGenCodigoCita() {
		return genCodigoCita;
	}

	public void setGenCodigoCita(int genCodigoCita) {
		this.genCodigoCita = genCodigoCita;
	}

	public int getGenCodigoConsulta() {
		return genCodigoConsulta;
	}

	public void setGenCodigoConsulta(int genCodigoConsulta) {
		this.genCodigoConsulta = genCodigoConsulta;
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(ArrayList<Cliente> Clientes) {
		this.clientes = Clientes;
	}

	public ArrayList<Medico> getMedicos() {
		return medicos;
	}

	public void setMedicos(ArrayList<Medico> medicos) {
		this.medicos = medicos;
	}

	public ArrayList<Enfermedad> getEnfermedades() {
		return enfermedades;
	}

	public void setEnfermedades(ArrayList<Enfermedad> enfermedades) {
		this.enfermedades = enfermedades;
	}

	public ArrayList<Vacuna> getVacunas() {
		return vacunas;
	}

	public void setVacunas(ArrayList<Vacuna> vacunas) {
		this.vacunas = vacunas;
	}

	public ArrayList<Cita> getCitas() {
		return citas;
	}

	public void setCitas(ArrayList<Cita> citas) {
		this.citas = citas;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public int getGenCodigoMedico() {
		return genCodigoMedico;
	}

	public void setGenCodigoMedico(int genCodigoMedico) {
		this.genCodigoMedico = genCodigoMedico;
	}

	public int getGenCodigoUser() {
		return genCodigoUser;
	}

	public void setGenCodigoUser(int genCodigoUser) {
		this.genCodigoUser = genCodigoUser;
	}

	public ArrayList<Especialidad> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(ArrayList<Especialidad> especialidades) {
		this.especialidades = especialidades;
	}

	public boolean registrarNuevoCliente(Cliente cliente) {
		if (cliente == null)
			return false;

		insertarCliente(cliente);
		return true;
	}

	public void insertarCliente(Cliente cli) {

		cli.setNumExpediente("CLI-" + genCodigoCliente);
		genCodigoCliente++;

		this.clientes.add(cli);
	}

	public Cliente buscarClientePorCodigo(String codigoExpediente) {
		for (Cliente cli : this.clientes) {
			if (cli.getNumExpediente().equals(codigoExpediente)) {
				return cli;
			}
		}
		return null;
	}

	public int buscarIndiceClientePorCedula(String cedula) {
		boolean encontrado = false;
		int indice = 0;
		while (encontrado == false && indice < this.clientes.size()) {
			if (this.clientes.get(indice).getCedula().equals(cedula)) {
				encontrado = true;
			} else {
				indice++;
			}
		}
		if (encontrado) {
			return indice;
		} else {
			return -1;
		}
	}

	public void actualizarCliente(Cliente seleccionado) {
		int indice = buscarIndiceClientePorCedula(seleccionado.getCedula());
		if (indice != -1) {
			clientes.set(indice, seleccionado);
		}
	}

	public void desactivarCliente(Cliente desactivar) {
		if (desactivar != null) {
			desactivar.setActivo(false);
		}
	}

	// Citas

	public Cita buscarCita(String codigoCita) {
		for (Cita cita : this.citas) {
			if (cita.getCodigo_cita().equals(codigoCita)) {
				return cita;
			}
		}
		return null;
	}

	public boolean cancelCita(Cita cita) {
		if (cita.getFechaHora().toLocalDate().isBefore(LocalDate.now())) {
			return false;
		}

		this.citas.remove(cita);

		if (cita.getMedico() != null) {
			cita.getMedico().getCitasAsignadas().remove(cita);
		}

		return true;
	}

	public boolean editCita(Cita citaOriginal, LocalDateTime nuevaFechaHora, Medico nuevoMedico) {
		if (citaOriginal.getFechaHora().isBefore(LocalDateTime.now())) {
			return false;
		}

		if (citaOriginal == null || nuevoMedico == null || nuevaFechaHora == null)
			return false;

		for (Cita c : citas) {
			if (c == citaOriginal)
				continue;
			if (c.getMedico() != null && c.getMedico().equals(nuevoMedico) && c.getFechaHora().equals(nuevaFechaHora)) {
				return false;
			}
		}

		int contador = 0;
		for (Cita c : nuevoMedico.getCitasAsignadas()) {
			if (c == citaOriginal)
				continue;
			if (c.getFechaHora().toLocalDate().equals(nuevaFechaHora.toLocalDate())) {
				contador++;
			}
		}
		if (contador >= nuevoMedico.getMaxCitasPorDia()) {
			return false;
		}

		Medico medicoAnterior = citaOriginal.getMedico();
		if (medicoAnterior != null && medicoAnterior != nuevoMedico) {
			medicoAnterior.getCitasAsignadas().remove(citaOriginal);
		}

		if (!nuevoMedico.getCitasAsignadas().contains(citaOriginal)) {
			nuevoMedico.getCitasAsignadas().add(citaOriginal);
		}

		citaOriginal.setFechaHora(nuevaFechaHora);
		citaOriginal.setMedico(nuevoMedico);

		guardarDatosClinica();

		return true;
	}

	private int contarCitasPorDia(Medico medico, LocalDate fecha) {
		int contador = 0;

		for (Cita c : medico.getCitasAsignadas()) {
			if (c.getFechaHora().toLocalDate().equals(fecha)) {
				contador++;
			}
		}
		return contador;
	}

	public boolean crearCita(LocalDateTime fechaHora, String cedulaMedico, String codigoCliente) {

		Medico medico = buscarMedicoCedula(cedulaMedico);
		if (medico == null)
			return false;

		Cliente cliente = buscarClientePorCodigo(codigoCliente);
		if (cliente == null)
			return false;

		for (Cita c : medico.getCitasAsignadas()) {
			if (c.getFechaHora().equals(fechaHora)) {
				return false;
			}
		}

		int citasDia = contarCitasPorDia(medico, fechaHora.toLocalDate());
		if (citasDia >= medico.getMaxCitasPorDia()) {
			return false;
		}

		Cita nuevaCita = new Cita(fechaHora, cliente, medico, "Pendiente");

		String codigo = "CIT-" + System.currentTimeMillis();
		nuevaCita.setCodigo_cita(codigo);

		this.citas.add(nuevaCita);

		medico.agregarCitaAsignada(nuevaCita);

		guardarDatosClinica();

		return true;
	}

	public boolean agendarCita(LocalDateTime fechaHora, String cedulaMedico, String codigoCliente) {
		return crearCita(fechaHora, cedulaMedico, codigoCliente);
	}

	// Consultas

	public boolean iniciarConsulta(Cita cita, String sintomasIniciales, String diagnosticoInicial) {

		if (cita == null)
			return false;

		Cliente cliente = cita.getCliente();
		Medico medico = cita.getMedico();

		if (cliente == null || medico == null)
			return false;

		String codigo = "CONS-" + System.currentTimeMillis();

		Consulta nuevaConsulta = new Consulta(codigo, cita.getFechaHora().toLocalDate(), sintomasIniciales,
				diagnosticoInicial, medico, cliente);

		cliente.getHistorial().agregarConsulta(nuevaConsulta);
		medico.agregarConsultaRealizada(nuevaConsulta);

		cita.setEstado("Completada");

		return true;
	}

	public boolean guardarConsulta(Consulta consulta, String sintomas, String diagnostico,
			ArrayList<Enfermedad> enfermedades) {

		if (consulta == null)
			return false;

		Cliente cliente = consulta.getCliente();
		if (cliente == null)
			return false;

		if (sintomas != null) {
			consulta.setSintomas(sintomas.trim());
		}

		if (diagnostico != null) {
			consulta.setDiagnostico(diagnostico.trim());
		}

		if (enfermedades != null) {
			consulta.setEnfermedadesDiag(enfermedades);
			if (!enfermedades.isEmpty()) {
				cliente.setEnfermo(true);
			}
		} else {
			consulta.setEnfermedadesDiag(new ArrayList<>());
		}

		cliente.getHistorial().agregarConsulta(consulta);

		return true;
	}

	// Entidades

	// Medico

	public boolean agregarMedico(Medico medico) {
		if (medico == null)
			return false;

		for (Medico m : medicos) {
			if (m.getCedula().equals(medico.getCedula())) {
				return false;
			}
		}

		medicos.add(medico);
		return true;
	}

	public Medico buscarMedicoCedula(String cedula) {
		for (Medico med : this.medicos) {
			if (med.getCedula().equals(cedula)) {
				return med;
			}
		}
		return null;
	}

	public boolean medicoDisponible(Medico medico, LocalDateTime fechaHora) {
		return this.agenda.medicoDisponible(medico, fechaHora);
	}

	public boolean verificarDisponibilidad(Medico medico, LocalDateTime fechaHora) {
		return medicoDisponible(medico, fechaHora);
	}

	// Enfermedad

	public boolean agregarEnfermedad(Enfermedad enfermedad) {
		if (enfermedad == null)
			return false;

		for (Enfermedad e : enfermedades) {
			if (e.getCodigo_sick().equalsIgnoreCase(enfermedad.getCodigo_sick())) {
				return false;
			}
		}

		enfermedades.add(enfermedad);
		return true;
	}

	public void generarReporteEnfermedades() {

		ArrayList<Enfermedad> enfermedades = new ArrayList<>();
		ArrayList<Integer> cantidades = new ArrayList<>();

		for (Cliente c : clientes) {
			Historial h = c.getHistorial();
			if (h == null)
				continue;

			for (Consulta cons : h.getConsultas()) {
				if (cons == null || cons.getEnfermedadesDiag() == null)
					continue;

				for (Enfermedad e : cons.getEnfermedadesDiag()) {

					int index = enfermedades.indexOf(e);

					if (index == -1) {
						enfermedades.add(e);
						cantidades.add(1);
					} else {

						cantidades.set(index, cantidades.get(index) + 1);
					}
				}
			}
		}

		// Para probarlo en la consola
		System.out.println("=== REPORTE DE ENFERMEDADES ===");
		for (int i = 0; i < enfermedades.size(); i++) {
			System.out.println(enfermedades.get(i).getNombre() + ": " + cantidades.get(i));
		}
	}

	// Vacuna
	public boolean agregarVacuna(Vacuna vacuna) {
		if (vacuna == null)
			return false;

		for (Vacuna v : vacunas) {
			if (v.getCodigo_vacun().equalsIgnoreCase(vacuna.getCodigo_vacun())) {
				return false;
			}
		}

		vacunas.add(vacuna);
		return true;
	}

	public boolean aplicarVacunaCliente(Cliente cliente, Vacuna vacuna, Medico medico) {

		if (cliente == null || vacuna == null || medico == null) {
			return false;
		}
		RegistroVacunacion reg = new RegistroVacunacion(cliente, vacuna, LocalDate.now(), true);

		reg.setCodigo_reg("REG-VAC-" + vacuna.getCodigo_vacun() + "-" + cliente.getCedula());
		cliente.getRegVacunas().add(reg);

		Consulta consulta = new Consulta("CONS-VAC-" + vacuna.getCodigo_vacun(), LocalDate.now(),
				"Aplicación de vacuna: " + vacuna.getNombre(), "Vacuna aplicada correctamente al cliente", medico,
				cliente);
		consulta.setMedico(medico);
		cliente.getHistorial().agregarConsulta(consulta);

		return true;
	}

	// Manejo de datos

	public static void guardarUsuarios() {
		try {
			FileOutputStream fos = new FileOutputStream("Usuarios.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(users);

			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked") // Para quitar el warning de user = ....
	public void cargarUsuarios() {
		try {
			FileInputStream fis = new FileInputStream("Usuarios.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);

			users = (ArrayList<User>) ois.readObject();

			ois.close();
			fis.close();
		} catch (Exception e) {
			users = new ArrayList<>();
		}
	}

	// Especialidades

	public void agregarEspecialidad(Especialidad esp) {
		this.especialidades.add(esp);
	}

	public Especialidad buscarEspecialidadPorNombre(String nombre) {
		for (Especialidad esp : especialidades) {
			if (esp.getNombre().equalsIgnoreCase(nombre)) {
				return esp;
			}
		}
		return null;
	}

	// Metodos de cargas
	public void refrescarRelaciones() {
		for (Cita c : this.citas) {
			if (c.getCliente() != null) {
				Cliente clienteReal = buscarClientePorCodigo(c.getCliente().getNumExpediente());
				if (clienteReal != null) {
					c.setCliente(clienteReal);
				}
			}
			if (c.getMedico() != null) {
				Medico medicoReal = buscarMedicoCedula(c.getMedico().getCedula());
				if (medicoReal != null) {
					c.setMedico(medicoReal);
					if (!medicoReal.getCitasAsignadas().contains(c)) {
						medicoReal.agregarCitaAsignada(c);
					}
				}
			}
		}

		for (Cliente cli : this.clientes) {
			if (cli.getHistorial() != null) {
				for (Consulta cons : cli.getHistorial().getConsultas()) {
					if (cons.getMedico() != null) {
						Medico m = buscarMedicoCedula(cons.getMedico().getCedula());
						if (m != null)
							cons.setMedico(m);
					}
					cons.setPaciente(cli);
				}
			}
		}
	}

	/*
	 * OJO AQUI: LEER BIEN ANTES DE DAR ENTER
	 * 
	 * O J O
	 * 
	 * DEJAR ESTOS METODOS AL FINAL :)
	 * 
	 */

	public void guardarDatosClinica() {
		try {
			FileOutputStream fos = new FileOutputStream("clinica.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(instancia);

			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void cargarDatosClinica() {
		try {
			FileInputStream fis = new FileInputStream("clinica.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);

			instancia = (Clinica) ois.readObject();

			ois.close();
			fis.close();

			instancia.refrescarRelaciones();

		} catch (Exception e) {
			System.out.println("No se encontro archivo de datos clinicos. Iniciando vacio.");
		}

	}

	// Métodos auxiliares para los reportes

	// Consultas

	public ArrayList<Consulta> getTodasLasConsultas() {
		ArrayList<Consulta> todas = new ArrayList<>();
		for (Cliente cli : this.clientes) {
			if (cli == null)
				continue;
			Historial h = cli.getHistorial();
			if (h == null || h.getConsultas() == null)
				continue;
			todas.addAll(h.getConsultas());
		}
		return todas;
	}

	public ArrayList<Consulta> getConsultasPorRango(LocalDate desde, LocalDate hasta) {
		ArrayList<Consulta> result = new ArrayList<>();

		if (desde == null || hasta == null)
			return result;

		for (Cliente cli : this.clientes) {
			if (cli == null)
				continue;

			Historial h = cli.getHistorial();
			if (h == null || h.getConsultas() == null)
				continue;

			for (Consulta con : h.getConsultas()) {
				if (con == null || con.getFechaConsulta() == null)
					continue;

				LocalDate f = con.getFechaConsulta();

				if ((f.isAfter(desde) || f.isEqual(desde)) && (f.isBefore(hasta) || f.isEqual(hasta))) {
					result.add(con);
				}
			}
		}
		return result;
	}

	public ArrayList<Cita> getCitasDeCliente(String codigoExpediente) {
		ArrayList<Cita> result = new ArrayList<>();
		if (codigoExpediente == null)
			return result;

		for (Cita c : this.citas) {
			if (c == null)
				continue;
			Cliente cli = c.getCliente();
			if (cli == null)
				continue;

			if (cli.getNumExpediente() != null && cli.getNumExpediente().equalsIgnoreCase(codigoExpediente)) {
				result.add(c);
			}
		}
		return result;
	}

	public ArrayList<Consulta> getConsultasPorDoctor(String cedulaDoctor) {
		ArrayList<Consulta> result = new ArrayList<>();
		if (cedulaDoctor == null)
			return result;

		for (Cliente cli : this.clientes) {
			if (cli == null)
				continue;

			Historial h = cli.getHistorial();
			if (h == null || h.getConsultas() == null)
				continue;

			for (Consulta con : h.getConsultas()) {
				if (con == null)
					continue;

				Medico m = con.getMedico();

				if (m != null && m.getCedula() != null && m.getCedula().equalsIgnoreCase(cedulaDoctor)) {

					result.add(con);
				}
			}
		}
		return result;
	}

	// Enfermedades

	public HashMap<String, Integer> getFrecuenciaEnfermedades() {
		HashMap<String, Integer> mapa = new HashMap<>();

		if (this.clientes == null) {
			return mapa;
		}

		for (Cliente cli : this.clientes) {
			if (cli == null) {
			} else {
				Historial h = cli.getHistorial();
				if (h == null) {
				} else {
					ArrayList<Consulta> consultas = h.getConsultas();
					if (consultas == null) {
					} else {
						for (Consulta con : consultas) {
							if (con == null) {
							} else {
								ArrayList<Enfermedad> listaEnf = con.getEnfermedadesDiag();
								if (listaEnf == null) {
								} else {
									for (Enfermedad enfObj : listaEnf) {
										if (enfObj == null) {
										} else {
											String nombre = enfObj.getNombre();
											if (nombre == null) {
											} else {
												nombre = nombre.trim().toLowerCase();
												int actual = 0;
												if (mapa.containsKey(nombre)) {
													actual = mapa.get(nombre);
												}
												mapa.put(nombre, actual + 1);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return mapa;
	}

	public ArrayList<String> getEnfermedadesDeCliente(String codigoCliente) {
		ArrayList<String> lista = new ArrayList<>();

		Cliente cli = buscarClientePorCodigo(codigoCliente);
		if (cli == null)
			return lista;

		Historial h = cli.getHistorial();
		if (h == null || h.getConsultas() == null)
			return lista;

		for (Consulta con : h.getConsultas()) {
			if (con != null && con.getEnfermedadesDiag() != null) {
				for (Enfermedad enf : con.getEnfermedadesDiag()) {
					if (enf != null && enf.getNombre() != null) {
						lista.add(enf.getNombre().trim());
					}
				}
			}
		}

		return lista;
	}

	public ArrayList<Cliente> getClientesPorEnfermedad(String enfermedad) {
		ArrayList<Cliente> lista = new ArrayList<>();

		if (clientes == null || enfermedad == null)
			return lista;

		for (Cliente cli : clientes) {
			if (cli != null && cli.getHistorial() != null) {
				Historial h = cli.getHistorial();

				if (h.getConsultas() != null) {
					boolean encontrado = false;

					for (Consulta con : h.getConsultas()) {
						if (con != null && con.getEnfermedadesDiag() != null) {
							for (Enfermedad enf : con.getEnfermedadesDiag()) {
								if (enf != null && enfermedad.equalsIgnoreCase(enf.getNombre())) {
									encontrado = true;
									break;
								}
							}
						}
						if (encontrado)
							break;
					}

					if (encontrado)
						lista.add(cli);
				}
			}
		}

		return lista;
	}

	public ArrayList<String> getTop5Enfermedades() {
		HashMap<String, Integer> mapa = getFrecuenciaEnfermedades();
		ArrayList<Map.Entry<String, Integer>> lista = new ArrayList<>(mapa.entrySet());

		lista.sort((a, b) -> b.getValue() - a.getValue());

		ArrayList<String> top = new ArrayList<>();

		int max = Math.min(5, lista.size());
		for (int i = 0; i < max; i++) {
			top.add(lista.get(i).getKey());
		}

		return top;
	}

	// Vacunas

	public HashMap<String, Integer> getFrecuenciaVacunas() {
		HashMap<String, Integer> mapa = new HashMap<>();

		if (clientes == null)
			return mapa;

		for (Cliente cli : clientes) {
			if (cli != null && cli.getRegVacunas() != null) {

				for (RegistroVacunacion reg : cli.getRegVacunas()) {
					if (reg != null && reg.getVacuna() != null && reg.getVacuna().getNombre() != null) {

						String nombre = reg.getVacuna().getNombre().trim();
						int actual = mapa.containsKey(nombre) ? mapa.get(nombre) : 0;

						mapa.put(nombre, actual + 1);
					}
				}
			}
		}

		return mapa;
	}

	public ArrayList<Cliente> getClientesPorVacuna(String nombreVacuna) {
		ArrayList<Cliente> lista = new ArrayList<>();

		if (clientes == null || nombreVacuna == null)
			return lista;

		for (Cliente cli : clientes) {
			if (cli != null && cli.getRegVacunas() != null) {

				for (RegistroVacunacion reg : cli.getRegVacunas()) {
					if (reg != null && reg.getVacuna() != null
							&& nombreVacuna.equalsIgnoreCase(reg.getVacuna().getNombre())) {

						lista.add(cli);
						break;
					}
				}
			}
		}

		return lista;
	}

	public HashMap<Cliente, RegistroVacunacion> getUltimaVacunaPorCliente() {
		HashMap<Cliente, RegistroVacunacion> mapa = new HashMap<>();

		if (clientes == null)
			return mapa;

		for (Cliente cli : clientes) {
			RegistroVacunacion ultima = null;

			if (cli != null && cli.getRegVacunas() != null) {

				for (RegistroVacunacion reg : cli.getRegVacunas()) {
					if (reg != null && reg.getFecha() != null) {

						if (ultima == null || reg.getFecha().isAfter(ultima.getFecha())) {
							ultima = reg;
						}
					}
				}
			}

			mapa.put(cli, ultima);
		}

		return mapa;
	}

	public HashMap<Cliente, Integer> getCantidadVacunasPorCliente() {
		HashMap<Cliente, Integer> mapa = new HashMap<>();

		if (clientes == null)
			return mapa;

		for (Cliente cli : clientes) {
			int cant = 0;

			if (cli != null && cli.getRegVacunas() != null) {
				cant = cli.getRegVacunas().size();
			}

			mapa.put(cli, cant);
		}

		return mapa;
	}

}
