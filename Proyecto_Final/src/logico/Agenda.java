package logico;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Agenda {

	public boolean medicoDisponible(Medico medico, LocalDateTime fechaHora) {
		if (medico.getCitasAsignadas() == null) {
			System.err.println("Error Crítico: La lista 'citasAsignadas' del médico es nula.");
			return false; 
		}

		LocalDate diaDeLaCita = fechaHora.toLocalDate();
		int contadorCitasDia = 0;

		for (Cita citaExistente : medico.getCitasAsignadas()) {
			if (citaExistente.getFechaHora().isEqual(fechaHora)) {
				return false; 
			}
			if (citaExistente.getFechaHora().toLocalDate().isEqual(diaDeLaCita)) {
				contadorCitasDia++;
			}
		}
		if (contadorCitasDia >= medico.getMaxCitasPorDia()) {
			return false; 
		}

		return true;
	}
}
