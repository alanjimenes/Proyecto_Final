package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Enfermedad;

public class ConsultarEnfermedades extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;

	public ConsultarEnfermedades() {
		setTitle("Catálogo de Enfermedades");
		setBounds(100, 100, 700, 500);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.desktop);
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));

		model = new DefaultTableModel(new Object[] { "Código", "Nombre", "Vigilancia" }, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);

		JTableHeader header = table.getTableHeader();
		header.setBackground(new Color(60, 70, 123));
		header.setForeground(Color.WHITE);
		header.setOpaque(true);

		scrollPane.setViewportView(table);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(60, 70, 123));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnNueva = new JButton("Registrar Nueva");
		Estilos.estilarBoton(btnNueva, new Color(176, 206, 136), Color.WHITE);
		btnNueva.addActionListener(e -> {
			RegEnfermedades reg = new RegEnfermedades();
			reg.setModal(true);
			reg.setVisible(true);
			cargarEnfermedades();
		});
		buttonPane.add(btnNueva);

		JButton btnCerrar = new JButton("Cerrar");
		Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		cargarEnfermedades();
	}

	@SuppressWarnings("unchecked")
	private void cargarEnfermedades() {
		model.setRowCount(0);
		ArrayList<Enfermedad> lista = (ArrayList<Enfermedad>) ClienteSocket.enviar("LISTAR_ENFERMEDADES", null);

		if (lista != null) {
			for (Enfermedad enf : lista) {
				String vig = enf.isVigilancia() ? "SÍ (ALERTA)" : "No";
				model.addRow(new Object[] { enf.getCodigo_sick(), enf.getNombre(), vig });
			}
		}
	}
}