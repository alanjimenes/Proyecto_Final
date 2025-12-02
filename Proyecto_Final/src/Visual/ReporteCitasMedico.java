package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logico.Cita;
import logico.Clinica;
import logico.Medico;

public class ReporteCitasMedico extends JDialog {

	private JComboBox<String> cbMedicos;
	private JTable table;
	private DefaultTableModel modelo;

	public ReporteCitasMedico() {
		setTitle("Citas por Médico");
		setSize(750, 450);
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

		modelo = new DefaultTableModel(new String[] { "Código", "Fecha/Hora", "Cliente", "Estado" }, 0);
		table = new JTable(modelo);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private void cargar() {
		modelo.setRowCount(0);
		if (cbMedicos.getSelectedIndex() == -1)
			return;
		String ced = cbMedicos.getSelectedItem().toString().split(" - ")[0];
		ArrayList<Cita> lista = Clinica.getInstancia().getCitasPorMedico(ced);
		for (Cita c : lista) {
			modelo.addRow(new Object[] { c.getCodigo_cita(), c.getFechaHora(),
					(c.getCliente() != null ? c.getCliente().getNombre() + " " + c.getCliente().getApellido() : ""),
					c.getEstado() });
		}
	}
}
