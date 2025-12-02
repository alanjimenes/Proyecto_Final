package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logico.Cita;
import logico.Cliente;
import logico.Clinica;

public class ReporteCitasCliente extends JDialog {

	private JComboBox<String> cbClientes;
	private JTable table;
	private DefaultTableModel modelo;

	public ReporteCitasCliente() {
		setTitle("Citas por Cliente");
		setSize(750, 450);
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

		modelo = new DefaultTableModel(new String[] { "Código", "Fecha/Hora", "Médico", "Estado" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void cargar() {
		modelo.setRowCount(0);
		if (cbClientes.getSelectedIndex() == -1)
			return;
		String expediente = cbClientes.getSelectedItem().toString().split(" - ")[0];
		ArrayList<Cita> lista = Clinica.getInstancia().getCitasDeCliente(expediente);
		for (Cita c : lista) {
			modelo.addRow(new Object[] { c.getCodigo_cita(), c.getFechaHora(),
					(c.getMedico() != null ? c.getMedico().getNombre() + " " + c.getMedico().getApellido() : ""),
					c.getEstado() });
		}
	}
}
