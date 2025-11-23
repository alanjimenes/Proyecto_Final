package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import logico.Vacuna;

public class ConsultarVacunas extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private Object[] row;

	public ConsultarVacunas() {
		setTitle("Gestión de Vacunas");
		setBounds(100, 100, 600, 400);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		model = new DefaultTableModel();
		String[] headers = {"Código", "Nombre", "Descripción"};
		model.setColumnIdentifiers(headers);
		table.setModel(model);
		scrollPane.setViewportView(table);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnNuevo = new JButton("Nueva Vacuna");
		btnNuevo.setForeground(new Color(0, 128, 0));
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegVacuna reg = new RegVacuna();
				reg.setModal(true);
				reg.setVisible(true);
				cargarVacunas();
			}
		});
		buttonPane.add(btnNuevo);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCerrar);
		cargarVacunas();
	}

	@SuppressWarnings("unchecked")
	private void cargarVacunas() {
		model.setRowCount(0);
		row = new Object[3];
		ArrayList<Vacuna> lista = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);

		if(lista != null) {
			for (Vacuna v : lista) {
				row[0] = v.getCodigo_vacun();
				row[1] = v.getNombre();
				row[2] = v.getDescripcion();
				model.addRow(row);
			}
		}
	}
}