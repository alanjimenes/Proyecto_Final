package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import logico.Clinica;
import logico.Cliente;

public class ReportesEnfermedades extends JDialog {

	private JTable tableFrecuencia;
	private JTable tableClientes;

	private DefaultTableModel modeloFrecuencia;
	private DefaultTableModel modeloClientes;

	public ReportesEnfermedades() {

		setTitle("Reporte de Enfermedades");
		setSize(700, 500);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.setBorder(BorderFactory.createTitledBorder("Frecuencia de Enfermedades"));
		getContentPane().add(panelTop, BorderLayout.NORTH);

		modeloFrecuencia = new DefaultTableModel(
				new String[] { "Enfermedad", "Cantidad" }, 0);

		tableFrecuencia = new JTable(modeloFrecuencia);
		panelTop.add(new JScrollPane(tableFrecuencia), BorderLayout.CENTER);

		JPanel panelBottom = new JPanel(new BorderLayout());
		panelBottom.setBorder(BorderFactory.createTitledBorder("Clientes con esta Enfermedad"));
		getContentPane().add(panelBottom, BorderLayout.CENTER);

		modeloClientes = new DefaultTableModel(
				new String[] { "Cédula", "Nombre", "Apellido" }, 0);

		tableClientes = new JTable(modeloClientes);
		panelBottom.add(new JScrollPane(tableClientes), BorderLayout.CENTER);

		JPanel panelBtns = new JPanel(new FlowLayout());
		getContentPane().add(panelBtns, BorderLayout.SOUTH);

		JButton btnVerClientes = new JButton("Ver Clientes");
		JButton btnCerrar = new JButton("Cerrar");

		panelBtns.add(btnVerClientes);
		panelBtns.add(btnCerrar);

		btnCerrar.addActionListener(e -> dispose());
		btnVerClientes.addActionListener(e -> cargarClientesPorEnfermedad());
		cargarFrecuencia();
	}

	private void cargarFrecuencia() {
		modeloFrecuencia.setRowCount(0);

		HashMap<String, Integer> mapa = Clinica.getInstancia().getFrecuenciaEnfermedades();

		for (String enf : mapa.keySet()) {
			modeloFrecuencia.addRow(
					new Object[] { enf, mapa.get(enf) }
			);
		}
	}

	private void cargarClientesPorEnfermedad() {
		int fila = tableFrecuencia.getSelectedRow();

		if (fila == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una enfermedad.");
			return;
		}

		String enfermedad = (String) modeloFrecuencia.getValueAt(fila, 0);
		modeloClientes.setRowCount(0);

		ArrayList<Cliente> lista = Clinica.getInstancia().getClientesPorEnfermedad(enfermedad);

		for (Cliente c : lista) {
			modeloClientes.addRow(new Object[] {
					c.getCedula(),
					c.getNombre(),
					c.getApellido()
			});
		}
	}
}
