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

import logico.Vacuna;

public class ConsultarVacunas extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private Object[] row;

	public ConsultarVacunas() {
		//setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarVacunas.class.getResource("/img/seguro-de-salud.png")));
		setTitle("Gestión de Vacunas");
		setBounds(100, 100, 800, 500);
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

		String[] headers = {"Código", "Nombre", "Descripción"};
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

		JLabel lblTitulo = new JLabel("Listado de Vacunas Disponibles");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
		panelNorte.add(lblTitulo);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(248, 244, 234));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		{
			JButton btnNuevo = new JButton("Nueva Vacuna");
			Estilos.estilarBoton(btnNuevo, new Color(176, 206, 136), Color.WHITE);
			btnNuevo.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnNuevo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegVacuna reg = new RegVacuna();
					reg.setModal(true);
					reg.setVisible(true);
					cargarVacunas();
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