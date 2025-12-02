package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import logico.Cliente;
import logico.Clinica;
import logico.Medico;
import logico.RegistroVacunacion;

public class ReporteVacunasPorMedico extends JDialog {

	private JComboBox<String> cbMedicos;
	private JTable table;
	private DefaultTableModel modelo;

	public ReporteVacunasPorMedico() {
		setTitle("Vacunas por Médico");
		setSize(800, 450);
		setModal(true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.add(new JLabel("Médico:"));
		cbMedicos = new JComboBox<>();
		for (Medico m : Clinica.getInstancia().getMedicos()) {
			cbMedicos.addItem(m.getCedula() + " - " + m.getNombre() + " " + m.getApellido());
		}
		top.add(cbMedicos);
		JButton btn = new JButton("Buscar");
		btn.addActionListener(e -> cargar());
		top.add(btn);
		add(top, BorderLayout.NORTH);

		modelo = new DefaultTableModel(new String[] { "Fecha", "Vacuna", "Cliente" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void cargar() {
		modelo.setRowCount(0);
		if (cbMedicos.getSelectedIndex() == -1)
			return;
		String ced = cbMedicos.getSelectedItem().toString().split(" - ")[0];
		// recorrer todos los clientes y sus registros
		for (Cliente cli : Clinica.getInstancia().getClientes()) {
			if (cli.getRegVacunas() == null)
				continue;
			for (RegistroVacunacion r : cli.getRegVacunas()) {
				if (r.getMedico() != null && ced.equalsIgnoreCase(r.getMedico().getCedula())) {
					modelo.addRow(new Object[] { r.getFecha(), (r.getVacuna() != null ? r.getVacuna().getNombre() : ""),
							(r.getCliente() != null ? r.getCliente().getNombre() + " " + r.getCliente().getApellido()
									: "") });
				}
			}
		}
	}
}
