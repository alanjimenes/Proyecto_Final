package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logico.Clinica;
import logico.Consulta;

public class ReporteTodasConsultas extends JDialog {

	private JTable table;
	private DefaultTableModel modelo;

	public ReporteTodasConsultas() {
		setTitle("Todas las Consultas");
		setSize(800, 450);
		setModal(true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		modelo = new DefaultTableModel(new String[] { "Código", "Fecha", "Cliente", "Médico", "Diagnóstico" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);

		cargar();
	}

	private void cargar() {
		modelo.setRowCount(0);
		ArrayList<Consulta> lista = Clinica.getInstancia().getTodasLasConsultas();
		for (Consulta c : lista) {
			modelo.addRow(new Object[] { c.getCodigo_cons(), c.getFechaConsulta(),
					(c.getCliente() != null ? c.getCliente().getNombre() + " " + c.getCliente().getApellido() : ""),
					(c.getMedico() != null ? c.getMedico().getNombre() + " " + c.getMedico().getApellido() : ""),
					c.getDiagnostico() });
		}
	}
}
