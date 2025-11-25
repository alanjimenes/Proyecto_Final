package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logico.Clinica;
import logico.Consulta;

public class ReporteConsultasDoctor extends JFrame {

	private JTable tabla;
	private JComboBox<String> cbDoctors;

	public ReporteConsultasDoctor() {

		setTitle("Reporte – Consultas por Doctor");
		setSize(700, 500);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.add(new JLabel("Seleccione Doctor:"));
		cbDoctors = new JComboBox<>();
		top.add(cbDoctors);
		JButton btn = new JButton("Generar");
		top.add(btn);

		add(top, BorderLayout.NORTH);

		tabla = new JTable();
		tabla.setModel(new DefaultTableModel(new Object[] { "Código", "Fecha", "Cliente", "Diagnóstico" }, 0));
		add(new JScrollPane(tabla), BorderLayout.CENTER);

		cargarDoctores();

		btn.addActionListener(e -> generar());
	}

	private void cargarDoctores() {
		cbDoctors.removeAllItems();
		Clinica.getInstancia().getMedicos().forEach(m -> cbDoctors.addItem(m.getCedula()));
	}

	private void generar() {
		String cedula = (String) cbDoctors.getSelectedItem();
		ArrayList<Consulta> lista = Clinica.getInstancia().getConsultasPorDoctor(cedula);

		DefaultTableModel model = (DefaultTableModel) tabla.getModel();
		model.setRowCount(0);

		for (Consulta c : lista) {
			model.addRow(new Object[] { c.getCodigo_cons(), c.getFechaConsulta(), c.getCliente().getNombre(),
					c.getDiagnostico() });
		}
	}
}
