package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Enfermedad;

public class ConsultarEnfermedades extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private Enfermedad seleccionado = null;
	private JButton btnUpdate;
	private JButton btnDelete;
	private ArrayList<Enfermedad> listaEnfermedadesGlobal;
	private JTextField txtFiltro;

	public ConsultarEnfermedades() {
		setTitle("Catálogo de Enfermedades");
		setBounds(100, 100, 750, 500);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(60, 70, 123));
		panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
		contentPanel.add(panelNorte, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("Gestión de Enfermedades");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
		panelNorte.add(lblTitulo);

		JLabel lblFiltro = new JLabel("Filtrar por Nombre:");
		lblFiltro.setForeground(Color.WHITE);
		lblFiltro.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		panelNorte.add(lblFiltro);

		txtFiltro = new JTextField();
		txtFiltro.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtFiltro.setColumns(15);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtrarLocal(txtFiltro.getText()); //
			}
		});
		panelNorte.add(txtFiltro);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBorder(null);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setRowHeight(30);
		table.setShowVerticalLines(false);
		table.setGridColor(new Color(230, 230, 230));
		table.setSelectionBackground(new Color(232, 246, 255));
		table.setSelectionForeground(Color.BLACK);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					String codigo = table.getValueAt(index, 0).toString();
					seleccionado = buscarEnfermedadLocal(codigo);
					btnUpdate.setEnabled(true);
					btnDelete.setEnabled(true);
				}
			}
		});

		model = new DefaultTableModel(new Object[] { "Código", "Nombre", "Vigilancia" }, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);

		JTableHeader header = table.getTableHeader();
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setBackground(new Color(60, 70, 123));
				setForeground(Color.WHITE);
				setFont(new Font("Bahnschrift", Font.BOLD, 14));
				setHorizontalAlignment(JLabel.CENTER);
				setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
				return this;
			}
		});

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		scrollPane.setViewportView(table);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnNueva = new JButton("Nueva");
		Estilos.estilarBoton(btnNueva, new Color(0, 150, 136), Color.WHITE);
		btnNueva.addActionListener(e -> {
			RegEnfermedades reg = new RegEnfermedades();
			reg.setModal(true);
			reg.setVisible(true);
			cargarEnfermedadesServer();
			resetBotones();
		});
		buttonPane.add(btnNueva);

		btnUpdate = new JButton("Modificar");
		Estilos.estilarBoton(btnUpdate, new Color(41, 128, 185), Color.WHITE);
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(e -> {
			if (seleccionado != null) {
				RegEnfermedades reg = new RegEnfermedades(seleccionado);
				reg.setModal(true);
				reg.setVisible(true);
				cargarEnfermedadesServer();
				resetBotones();
			}
		});
		buttonPane.add(btnUpdate);

		btnDelete = new JButton("Desactivar");
		Estilos.estilarBoton(btnDelete, new Color(231, 76, 60), Color.WHITE);
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(e -> {
			if (seleccionado != null) {
				int opt = JOptionPane.showConfirmDialog(null,
						"¿Seguro desea desactivar la enfermedad " + seleccionado.getNombre() + "?",
						"Confirmar", JOptionPane.YES_NO_OPTION);
				if (opt == JOptionPane.YES_OPTION) {
					seleccionado.setActivo(false);
					boolean exito = (boolean) ClienteSocket.enviar("UPDATE_ENFERMEDAD", seleccionado);

					if (exito) {
						JOptionPane.showMessageDialog(null, "Enfermedad desactivada.");
						cargarEnfermedadesServer();
						resetBotones();
					}
				}
			}
		});
		buttonPane.add(btnDelete);

		JButton btnCerrar = new JButton("Cerrar");
		Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		cargarEnfermedadesServer();
	}

	@SuppressWarnings("unchecked")
	public void cargarEnfermedadesServer() {
		listaEnfermedadesGlobal = (ArrayList<Enfermedad>) ClienteSocket.enviar("LISTAR_ENFERMEDADES", null);
		if (listaEnfermedadesGlobal == null)
			listaEnfermedadesGlobal = new ArrayList<>();

		filtrarLocal(txtFiltro.getText());
	}

	private void filtrarLocal(String texto) {
		model.setRowCount(0);
		String filtro = texto.toLowerCase();

		for (Enfermedad enf : listaEnfermedadesGlobal) {
			if (filtro.isEmpty() || enf.getNombre().toLowerCase().contains(filtro)) {
				String vig = enf.isVigilancia() ? "Sí (ALERTA)" : "No";
				model.addRow(new Object[] { enf.getCodigoEnfermedad(), enf.getNombre(), vig });
			}
		}
	}

	private Enfermedad buscarEnfermedadLocal(String codigo) {
		try {
			int codigoInt = Integer.parseInt(codigo);
			for (Enfermedad e : listaEnfermedadesGlobal) {
				if (e.getCodigoEnfermedad() == codigoInt) return e;
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void resetBotones() {
		seleccionado = null;
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		table.clearSelection();
		txtFiltro.setText("");
	}
}