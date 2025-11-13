package logico;

import java.util.ArrayList;

public class Clinica {

	private int genCodigoPaciente = 1;
	private int genCodigoCita = 1;
	private int genCodigoConsulta = 1;
	private ArrayList<Paciente> pacientes;
	private ArrayList<Medico> medicos;
	private ArrayList<Enfermedad> enfermedades;
	private ArrayList<Vacuna> vacunas;
	private ArrayList<Cita> citas;

	private static Medico medicoIniciado;
	private static Clinica instancia;


	public Clinica() {
		this.pacientes = new ArrayList<>();
		this.medicos = new ArrayList<>();
		this.enfermedades = new ArrayList<>();
		this.vacunas = new ArrayList<>();
		this.citas = new ArrayList<>();
		medicoIniciado = null;
	}



	public static Clinica getInstance() {
		if (instancia == null) {
			instancia = new Clinica();
		}
		return instancia;
	}

	public void insertarPaciente(Paciente pac) {

		pac.setNumExpediente("PAC-" + genCodigoPaciente);
		genCodigoPaciente++;

		this.pacientes.add(pac);
	}

	public Paciente buscarPacientePorCedula(String cedula) {
		for (Paciente pac : pacientes) {
			if (pac.getCedula().equals(cedula)) {
				return pac;
			}
		}
		return null;
	}


	public int getGenCodigoPaciente() {
		return genCodigoPaciente;
	}

	public void setGenCodigoPaciente(int genCodigoPaciente) {
		this.genCodigoPaciente = genCodigoPaciente;
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


	public ArrayList<Paciente> getPacientes() {
		return pacientes;
	}



	public void setPacientes(ArrayList<Paciente> pacientes) {
		this.pacientes = pacientes;
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


	public static Medico getMedicoIniciado() {
		return medicoIniciado;
	}


	public static void setMedicoIniciado(Medico medicoIniciado) {
		Clinica.medicoIniciado = medicoIniciado;
	}




	public static Clinica getInstancia() {
		return instancia;

	}


	public static void setInstancia(Clinica instancia) {
			Clinica.instancia = instancia;
		}



		/*
	public Paciente buscarPacienteCedula(String cedula){

	}

	public Medico buscarMedicoCedula(String cedula) {

	}

	public boolean medicoDisponible(Medico medico, LocalDateTime fechaHora) {

	}



	public void cancelarCita(Cita cita) {

	}

	public void modificarCita(Cita cita) {

	}
		 */
		//LOGIN

		public static Medico getMedicoLogueado() {
			return medicoIniciado;
		}

		public static void logoutMedico() {
			medicoIniciado = null;
		}

		/*Logica del login:
		 * Para el usuario se usara la contraseña (menos jodon y mas sencillo ya que estara ingresado :)  )
		 * Para la contraseña es obvio no?
		 * 
		 * Entonces el codigo retorna True si el medico inicio sesion correctamente
		 * False si fallo
		 * 
		 * Fin de explicacion :)   
		 * OJAZO AQUI: saben que falta insertarlo en lo visual no quise joder
		 *  con eso ahora dejemos eso pa depue :)
		 */
		public boolean loginMedico(String cedula, String password) {
			for (Medico medico : medicos) {
				if (medico.getCedula().equals(cedula) && medico.getPassword().equals(password)) {
					medicoIniciado = medico; 
					return true;
				}
			}
			return false; 
		}


	}
