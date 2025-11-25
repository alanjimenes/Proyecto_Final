package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logico.Clinica;

public class ReporteClientesPorEnfermedad extends JFrame {

	private JTable tabla;
	private JComboBox<String> cbEnf;

	public ReporteClientesPorEnfermedad() {
		setTitle("Clientes por Enfermedad");
		setSize(600, 450);
		setLocationRelativeTo(null);

		cbEnf = new JComboBox<>();
		tabla = new JTable();
		tabla.setModel(new DefaultTableModel(new Object[] { "Cliente", "Cédula" }, 0));

		JPanel top = new JPanel();
		top.add(new JLabel("Enfermedad:"));
		top.add(cbEnf);
		JButton btn = new JButton("Generar");
		top.add(btn);

		add(top, "North");
		add(new JScrollPane(tabla), "Center");

		cargarEnfermedades();
		btn.addActionListener(e -> generar());
	}

	private void cargarEnfermedades() {
		Clinica.getInstancia().getEnfermedades().forEach(e -> cbEnf.addItem(e.getNombre()));
	}

	private void generar() {
		DefaultTableModel model = (DefaultTableModel) tabla.getModel();
		model.setRowCount(0);

		String nombre = (String) cbEnf.getSelectedItem();

		Clinica.getInstancia().getClientesPorEnfermedad(nombre)
				.forEach(cli -> model.addRow(new Object[] { cli.getNombre(), cli.getCedula() }));
	}
}
