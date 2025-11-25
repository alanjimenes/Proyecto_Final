package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

import logico.Clinica;
import logico.Consulta;

public class ReporteConsultasFecha extends JFrame {

	private JTable tabla;
	private JFormattedTextField txtDesde;
	private JFormattedTextField txtHasta;

	public ReporteConsultasFecha() {

		setTitle("Reporte – Consultas por Rango de Fechas");
		setSize(700, 500);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel panelTop = new JPanel();
		panelTop.add(new JLabel("Desde (AAAA-MM-DD):"));
		txtDesde = new JFormattedTextField();
		txtDesde.setColumns(10);
		panelTop.add(txtDesde);

		panelTop.add(new JLabel("Hasta:"));
		txtHasta = new JFormattedTextField();
		txtHasta.setColumns(10);
		panelTop.add(txtHasta);

		JButton btnBuscar = new JButton("Generar");
		panelTop.add(btnBuscar);

		add(panelTop, BorderLayout.NORTH);

		tabla = new JTable();
		tabla.setModel(
				new DefaultTableModel(new Object[] { "Código", "Fecha", "Doctor", "Cliente", "Diagnóstico" }, 0));
		add(new JScrollPane(tabla), BorderLayout.CENTER);

		btnBuscar.addActionListener(e -> cargarDatos());
	}

	private void cargarDatos() {
		try {
			LocalDate d1 = LocalDate.parse(txtDesde.getText());
			LocalDate d2 = LocalDate.parse(txtHasta.getText());

			ArrayList<Consulta> lista = Clinica.getInstancia().getConsultasPorRango(d1, d2);

			DefaultTableModel model = (DefaultTableModel) tabla.getModel();
			model.setRowCount(0);

			for (Consulta c : lista) {
				model.addRow(new Object[] { c.getCodigo_cons(), c.getFechaConsulta(), c.getMedico().getNombre(),
						c.getCliente().getNombre(), c.getDiagnostico() });
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Fechas inválidas");
		}
	}
}
