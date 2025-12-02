package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

import logico.Cliente;
import logico.Clinica;
import logico.RegistroVacunacion;

public class ReporteVacunasPorRango extends JDialog {

	private JTextField txtDesde, txtHasta;
	private JTable table;
	private DefaultTableModel modelo;

	public ReporteVacunasPorRango() {
		setTitle("Vacunas por Rango");
		setSize(800, 500);
		setModal(true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.add(new JLabel("Desde (YYYY-MM-DD):"));
		txtDesde = new JTextField(10);
		top.add(txtDesde);
		top.add(new JLabel("Hasta:"));
		txtHasta = new JTextField(10);
		top.add(txtHasta);
		JButton btn = new JButton("Buscar");
		btn.addActionListener(e -> cargar());
		top.add(btn);
		add(top, BorderLayout.NORTH);

		modelo = new DefaultTableModel(new String[] { "Fecha", "Vacuna", "Cliente", "Médico" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void cargar() {
		modelo.setRowCount(0);
		try {
			LocalDate desde = LocalDate.parse(txtDesde.getText());
			LocalDate hasta = LocalDate.parse(txtHasta.getText());
			for (Cliente cli : Clinica.getInstancia().getClientes()) {
				if (cli.getRegVacunas() == null)
					continue;
				for (RegistroVacunacion r : cli.getRegVacunas()) {
					if (r.getFecha() == null)
						continue;
					if ((r.getFecha().isAfter(desde) || r.getFecha().isEqual(desde))
							&& (r.getFecha().isBefore(hasta) || r.getFecha().isEqual(hasta))) {
						modelo.addRow(
								new Object[] { r.getFecha(), (r.getVacuna() != null ? r.getVacuna().getNombre() : ""),
										(r.getCliente() != null
												? r.getCliente().getNombre() + " " + r.getCliente().getApellido()
												: ""),
										(r.getMedico() != null
												? r.getMedico().getNombre() + " " + r.getMedico().getApellido()
												: "") });
					}
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD");
		}
	}
}
