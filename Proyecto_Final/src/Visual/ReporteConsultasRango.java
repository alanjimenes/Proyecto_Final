package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

import logico.Clinica;
import logico.Consulta;

public class ReporteConsultasRango extends JDialog {

	private JTextField txtDesde, txtHasta;
	private JTable table;
	private DefaultTableModel modelo;

	public ReporteConsultasRango() {
		setTitle("Consultas por Rango");
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

		modelo = new DefaultTableModel(new String[] { "Código", "Fecha", "Cliente", "Médico", "Diagnóstico" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void cargar() {
		modelo.setRowCount(0);
		try {
			LocalDate desde = LocalDate.parse(txtDesde.getText());
			LocalDate hasta = LocalDate.parse(txtHasta.getText());
			ArrayList<Consulta> lista = Clinica.getInstancia().getConsultasPorRango(desde, hasta);
			for (Consulta c : lista) {
				modelo.addRow(new Object[] { c.getCodigo_cons(), c.getFechaConsulta(),
						(c.getCliente() != null ? c.getCliente().getNombre() + " " + c.getCliente().getApellido() : ""),
						(c.getMedico() != null ? c.getMedico().getNombre() + " " + c.getMedico().getApellido() : ""),
						c.getDiagnostico() });
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD");
		}
	}
}
