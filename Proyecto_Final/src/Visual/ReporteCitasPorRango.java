package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import logico.Cita;
import logico.Clinica;

public class ReporteCitasPorRango extends JDialog {

	private JTextField txtDesde, txtHasta;
	private JTable table;
	private DefaultTableModel modelo;
	private DateTimeFormatter[] accepted = new DateTimeFormatter[] { DateTimeFormatter.ISO_LOCAL_DATE_TIME,
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") };

	public ReporteCitasPorRango() {
		setTitle("Citas por Rango");
		setSize(800, 500);
		setModal(true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.add(new JLabel("Desde (yyyy-MM-dd HH:mm):"));
		txtDesde = new JTextField(16);
		top.add(txtDesde);
		top.add(new JLabel("Hasta:"));
		txtHasta = new JTextField(16);
		top.add(txtHasta);
		JButton btn = new JButton("Buscar");
		btn.addActionListener(e -> cargar());
		top.add(btn);
		add(top, BorderLayout.NORTH);

		modelo = new DefaultTableModel(new String[] { "Código", "Fecha/Hora", "Cliente", "Médico", "Estado" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private LocalDateTime parse(String s) {
		for (DateTimeFormatter f : accepted) {
			try {
				return LocalDateTime.parse(s, f);
			} catch (Exception ex) {
			}
		}
		return null;
	}

	private void cargar() {
		modelo.setRowCount(0);
		LocalDateTime desde = parse(txtDesde.getText().trim());
		LocalDateTime hasta = parse(txtHasta.getText().trim());
		if (desde == null || hasta == null) {
			JOptionPane.showMessageDialog(this, "Formato inválido. Ej: 2025-12-01 14:30");
			return;
		}
		ArrayList<Cita> lista = Clinica.getInstancia().getCitasPorRango(desde, hasta);
		for (Cita c : lista) {
			modelo.addRow(new Object[] { c.getCodigo_cita(), c.getFechaHora(),
					(c.getCliente() != null ? c.getCliente().getNombre() + " " + c.getCliente().getApellido() : ""),
					(c.getMedico() != null ? c.getMedico().getNombre() + " " + c.getMedico().getApellido() : ""),
					c.getEstado() });
		}
	}
}
