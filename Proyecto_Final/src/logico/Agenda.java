package logico;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Agenda {

	public boolean medicoDisponible(Medico medico, LocalDateTime fechaHora, ArrayList<Cita> citasDelDia) {
		if (citasDelDia == null || citasDelDia.isEmpty()) {
			return true;
		}

		int contadorCitasActivas = 0;
		for (Cita cita : citasDelDia) {
			if (!cita.getEstado().equalsIgnoreCase("Cancelada")) {
				contadorCitasActivas++;
			}
		}

		if (contadorCitasActivas >= medico.getMaxCitasPorDia()) {
			return false;
		}

		LocalDateTime finNuevaCita = fechaHora.plusMinutes(30);

		for (Cita citaExistente : citasDelDia) {
			if (citaExistente.getEstado().equalsIgnoreCase("Cancelada")) {
				continue;
			}

			LocalDateTime inicioExistente = citaExistente.getFechaCita();
			LocalDateTime finExistente = inicioExistente.plusMinutes(30);

			if (fechaHora.isBefore(finExistente) && inicioExistente.isBefore(finNuevaCita)) {
				return false;
			}
		}

		return true;
	}
}