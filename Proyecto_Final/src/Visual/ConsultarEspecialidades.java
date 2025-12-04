package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Especialidad;

public class ConsultarEspecialidades extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private Object[] row;

	public ConsultarEspecialidades() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ConsultarEspecialidades.class.getResource("/img/seguro-de-salud.png")));
		setTitle("Gestión de Especialidades");
		setBounds(100, 100, 700, 500);
		setResizable(false);
		setLocationRelativeTo(null);
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
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		String[] headers = { "Código", "Nombre" };
		model.setColumnIdentifiers(headers);
		table.setModel(model);

		JTableHeader header = table.getTableHeader();
		header.setBackground(new Color(60, 70, 123));
		header.setForeground(Color.WHITE);
		header.setOpaque(true);
		header.setReorderingAllowed(false);

		scrollPane.setViewportView(table);

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(60, 70, 123));
		contentPanel.add(panelNorte, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("Listado de Especialidades Médicas");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
		panelNorte.add(lblTitulo);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(60, 70, 123));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		{
			JButton btnNuevo = new JButton("Nueva");
			Estilos.estilarBoton(btnNuevo, new Color(176, 206, 136), Color.WHITE);
			btnNuevo.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnNuevo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegEspecialidad reg = new RegEspecialidad();
					reg.setModal(true);
					reg.setVisible(true);
					cargarEspecialidades();
				}
			});
			buttonPane.add(btnNuevo);
		}

		{
			JButton btnCerrar = new JButton("Cerrar");
			Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
			btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			buttonPane.add(btnCerrar);
		}
		cargarEspecialidades();
	}

	@SuppressWarnings("unchecked")
	private void cargarEspecialidades() {
		model.setRowCount(0);
		row = new Object[2];

		ArrayList<Especialidad> lista = (ArrayList<Especialidad>) ClienteSocket.enviar("LISTAR_ESPECIALIDADES", null);

		if (lista != null) {
			for (Especialidad esp : lista) {
				row[0] = esp.getCodigo_espe();
				row[1] = esp.getNombre();
				model.addRow(row);
			}
		}
	}
}