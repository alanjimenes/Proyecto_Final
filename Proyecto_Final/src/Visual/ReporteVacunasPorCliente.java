package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logico.Clinica;
import logico.Cliente;
import logico.RegistroVacunacion;

public class ReporteVacunasPorCliente extends JDialog {

	private JComboBox<String> cbClientes;
	private JTable table;
	private DefaultTableModel modelo;

	public ReporteVacunasPorCliente() {
		setTitle("Vacunas por Cliente");
		setSize(800, 450);
		setModal(true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.add(new JLabel("Cliente:"));
		cbClientes = new JComboBox<>();
		for (Cliente c : Clinica.getInstancia().getClientes()) {
			cbClientes.addItem(c.getNumExpediente() + " - " + c.getNombre() + " " + c.getApellido());
		}
		top.add(cbClientes);
		JButton btn = new JButton("Buscar");
		btn.addActionListener(e -> cargar());
		top.add(btn);
		add(top, BorderLayout.NORTH);

		modelo = new DefaultTableModel(new String[] { "Fecha", "Vacuna", "Médico" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void cargar() {
		modelo.setRowCount(0);
		if (cbClientes.getSelectedIndex() == -1)
			return;
		String exp = cbClientes.getSelectedItem().toString().split(" - ")[0];
		for (Cliente c : Clinica.getInstancia().getClientes()) {
			if (c.getNumExpediente() != null && c.getNumExpediente().equalsIgnoreCase(exp)) {
				if (c.getRegVacunas() != null) {
					for (RegistroVacunacion r : c.getRegVacunas()) {
						modelo.addRow(
								new Object[] { r.getFecha(), (r.getVacuna() != null ? r.getVacuna().getNombre() : ""),
										(r.getMedico()!= null
												? r.getMedico().getNombre() + " " + r.getMedico().getApellido()
												: "") });
					}
				}
				break;
			}
		}
	}
}
