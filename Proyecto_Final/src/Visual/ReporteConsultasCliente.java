package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logico.Cliente;
import logico.Clinica;
import logico.Consulta;

public class ReporteConsultasCliente extends JDialog {

	private JComboBox<String> cbClientes;
	private JTable table;
	private DefaultTableModel modelo;

	public ReporteConsultasCliente() {
		setTitle("Consultas por Cliente");
		setSize(700, 450);
		setModal(true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.add(new JLabel("Cliente:"));

		cbClientes = new JComboBox<>();

		for (Cliente c : Clinica.getInstancia().getClientes()) {
			cbClientes.addItem(c.getCedula() + " - " + c.getNombre());
		}

		top.add(cbClientes);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> cargar());
		top.add(btnBuscar);

		add(top, BorderLayout.NORTH);

		modelo = new DefaultTableModel(new String[] { "Código", "Fecha", "Médico", "Motivo" }, 0);

		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void cargar() {
		modelo.setRowCount(0);

		if (cbClientes.getSelectedIndex() == -1)
			return;

		String cedula = cbClientes.getSelectedItem().toString().split(" - ")[0];

		ArrayList<Consulta> lista = Clinica.getInstancia().getConsultasPorCliente(cedula);

		for (Consulta c : lista) {
			modelo.addRow(new Object[] { c.getCodigo_cons(), c.getFechaConsulta(), c.getMedico().getNombre(),
					c.getMotivo() });
		}
	}
}
