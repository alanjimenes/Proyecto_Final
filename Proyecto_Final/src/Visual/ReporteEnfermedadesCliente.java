package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logico.Clinica;

public class ReporteEnfermedadesCliente extends JFrame {

	private JTable tabla;
	private JComboBox<String> cbClientes;

	public ReporteEnfermedadesCliente() {

		setTitle("Reporte – Enfermedades por Cliente");
		setSize(600, 450);
		setLocationRelativeTo(null);

		JPanel top = new JPanel();
		top.add(new JLabel("Cliente:"));
		cbClientes = new JComboBox<>();
		top.add(cbClientes);
		JButton btn = new JButton("Generar");
		top.add(btn);

		tabla = new JTable();
		tabla.setModel(new DefaultTableModel(new Object[] { "Enfermedad" }, 0));

		add(top, "North");
		add(new JScrollPane(tabla), "Center");

		cargarClientes();

		btn.addActionListener(e -> generar());
	}

	private void cargarClientes() {
		Clinica.getInstancia().getClientes().forEach(c -> cbClientes.addItem(c.getNumExpediente()));
	}

	private void generar() {
		DefaultTableModel model = (DefaultTableModel) tabla.getModel();
		model.setRowCount(0);

		String exp = (String) cbClientes.getSelectedItem();

		for (String enf : Clinica.getInstancia().getEnfermedadesDeCliente(exp)) {
			model.addRow(new Object[] { enf });
		}
	}
}
