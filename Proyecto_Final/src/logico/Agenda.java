package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Agenda implements Serializable {

	private static final long serialVersionUID = 1L;

	public boolean medicoDisponible(Medico medico, LocalDateTime fechaHora) {
		if (medico.getCitasAsignadas() == null) {
			return true;
		}

		LocalDate diaDeLaCita = fechaHora.toLocalDate();
		int contadorCitasDia = 0;

		LocalDateTime finNuevaCita = fechaHora.plusMinutes(30);

		for (Cita citaExistente : medico.getCitasAsignadas()) {
			if (citaExistente.getEstado().equalsIgnoreCase("Cancelada")) {
				continue;
			}

			LocalDateTime inicioExistente = citaExistente.getFechaHora();
			LocalDateTime finExistente = inicioExistente.plusMinutes(30);

			if (fechaHora.isBefore(finExistente) && inicioExistente.isBefore(finNuevaCita)) {
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