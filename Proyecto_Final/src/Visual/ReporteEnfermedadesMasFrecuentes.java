package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import logico.Clinica;

public class ReporteEnfermedadesMasFrecuentes extends JFrame {

	private JTable tabla;

	public ReporteEnfermedadesMasFrecuentes() {
		setTitle("Reporte – Frecuencia de Enfermedades");
		setSize(500, 400);
		setLocationRelativeTo(null);

		tabla = new JTable();
		tabla.setModel(new DefaultTableModel(new Object[] { "Enfermedad", "Casos" }, 0));
		add(new JScrollPane(tabla));

		cargar();
	}

	private void cargar() {
		Map<String, Integer> map = Clinica.getInstancia().getFrecuenciaEnfermedades();
		DefaultTableModel model = (DefaultTableModel) tabla.getModel();
		model.setRowCount(0);

		for (String enf : map.keySet()) {
			model.addRow(new Object[] { enf, map.get(enf) });
		}
	}
}
