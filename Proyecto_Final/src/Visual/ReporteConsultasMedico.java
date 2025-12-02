package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logico.Clinica;
import logico.Consulta;
import logico.Medico;

public class ReporteConsultasMedico extends JDialog {

	private JComboBox<String> cbMedicos;
	private JTable table;
	private DefaultTableModel modelo;

	public ReporteConsultasMedico() {
		setTitle("Consultas por Médico");
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

		modelo = new DefaultTableModel(new String[] { "Código", "Fecha", "Cliente", "Diagnóstico" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void cargar() {
		modelo.setRowCount(0);
		if (cbMedicos.getSelectedIndex() == -1)
			return;
		String ced = cbMedicos.getSelectedItem().toString().split(" - ")[0];
		ArrayList<Consulta> lista = Clinica.getInstancia().getConsultasPorDoctor(ced);
		for (Consulta c : lista) {
			modelo.addRow(new Object[] { c.getCodigo_cons(), c.getFechaConsulta(),
					(c.getCliente() != null ? c.getCliente().getNombre() + " " + c.getCliente().getApellido() : ""),
					c.getDiagnostico() });
		}
	}
}
