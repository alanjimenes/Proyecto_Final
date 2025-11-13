package logico;

import java.util.ArrayList;

public class Clinica {

	private ArrayList<Paciente> pacientes;
	private ArrayList<Medico> medicos;
	private ArrayList<Enfermedad> enfermedades;
	private ArrayList<Vacuna> vacunas;
	private ArrayList<Cita> citas;

	private int genCodigoPaciente = 1;
	private int genCodigoCita = 1;
	private int genCodigoConsulta = 1;


	public Clinica() {
		this.pacientes = new ArrayList<>();
		this.medicos = new ArrayList<>();
		this.enfermedades = new ArrayList<>();
		this.vacunas = new ArrayList<>();
		this.citas = new ArrayList<>();
	}

	//FALTAN LOS GETTERS AND SETTERS

	public static Clinica getInstance() {
		if (instance == null) {
			instance = new Clinica();
		}
		return instance;
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

}
