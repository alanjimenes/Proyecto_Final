package logico;

import java.time.LocalDateTime;
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
	private static Clinica instancia;
	private Agenda agenda;
	private ArrayList<User>users;


	public Clinica() {
		this.pacientes = new ArrayList<>();
		this.medicos = new ArrayList<>();
		this.enfermedades = new ArrayList<>();
		this.vacunas = new ArrayList<>();
		this.citas = new ArrayList<>();
		this.agenda = new Agenda();
	}



	public static Clinica getInstancia() {
		return instancia;

	}

	public static void setInstancia(Clinica instancia) {
		Clinica.instancia = instancia;
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



	public ArrayList<User> getUsers() {
		return users;
	}



	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

}
