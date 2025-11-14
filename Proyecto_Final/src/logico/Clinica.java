package logico;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Clinica {

	private int genCodigoPaciente = 1;
	private int genCodigoCita = 1;
	private int genCodigoConsulta = 1;
	private int genCodigoMedico = 1;
	private int genCodigoUser = 1;
	private ArrayList<Paciente> pacientes;
	private ArrayList<Medico> medicos;
	private ArrayList<Enfermedad> enfermedades;
	private ArrayList<Vacuna> vacunas;
	private ArrayList<Cita> citas;
	private Agenda agenda;
	private ArrayList<User>users;
	private static Clinica instancia;

	public Clinica() {
		this.pacientes = new ArrayList<>();
		this.medicos = new ArrayList<>();
		this.enfermedades = new ArrayList<>();
		this.vacunas = new ArrayList<>();
		this.citas = new ArrayList<>();
		this.agenda = new Agenda();
		this.users = new ArrayList<>();
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
	public void insertarPaciente(Paciente pac) {

		pac.setNumExpediente("PAC-" + genCodigoPaciente);
		genCodigoPaciente++;

		this.pacientes.add(pac);
	}
	public Paciente buscarPacientePorCodigo(String codigoExpediente) {
	    for (Paciente pac : this.pacientes) {
	        if (pac.getNumExpediente().equals(codigoExpediente)) {
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


}
