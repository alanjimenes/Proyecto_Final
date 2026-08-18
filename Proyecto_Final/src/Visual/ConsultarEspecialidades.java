package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Especialidad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ConsultarEspecialidades extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private Especialidad seleccionado = null;
	private JButton btnUpdate;
	private JButton btnDelete;
	private ArrayList<Especialidad> listaGlobalEspecialidades;

	public ConsultarEspecialidades() {
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

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					String codigo = table.getValueAt(index, 0).toString();
					seleccionado = buscarLocal(codigo);
					if (seleccionado != null) {
						btnUpdate.setEnabled(true);
						btnDelete.setEnabled(true);
					}
				}
			}
		});
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
			btnNuevo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegEspecialidad reg = new RegEspecialidad();
					reg.setModal(true);
					reg.setVisible(true);
					cargarEspecialidades();
					resetBotones();
				}
			});
			buttonPane.add(btnNuevo);

			btnUpdate = new JButton("Modificar");
			Estilos.estilarBoton(btnUpdate, new Color(41, 128, 185), Color.WHITE);
			btnUpdate.setEnabled(false);
			btnUpdate.addActionListener(e -> {
				if(seleccionado != null) {
					RegEspecialidad reg = new RegEspecialidad(seleccionado);
					reg.setModal(true);
					reg.setVisible(true);
					cargarEspecialidades();
					resetBotones();
				}
			});
			buttonPane.add(btnUpdate);

			btnDelete = new JButton("Eliminar");
			Estilos.estilarBoton(btnDelete, new Color(231, 76, 60), Color.WHITE);
			btnDelete.setEnabled(false);
			btnDelete.addActionListener(e -> {
				if(seleccionado != null) {
					int opt = JOptionPane.showConfirmDialog(null, "¿Eliminar especialidad?", "Confirmar", JOptionPane.YES_NO_OPTION);
					if(opt == JOptionPane.YES_OPTION) {
						ClienteSocket.enviar("DELETE_ESPECIALIDAD", seleccionado.getCodigoEspecialidad() + "");
						cargarEspecialidades();
						resetBotones();
					}
				}
			});
			buttonPane.add(btnDelete);
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
		listaGlobalEspecialidades = (ArrayList<Especialidad>) ClienteSocket.enviar("LISTAR_ESPECIALIDADES", null);

		if (listaGlobalEspecialidades != null) {
			for (Especialidad esp : listaGlobalEspecialidades) {
				model.addRow(new Object[] { esp.getCodigoEspecialidad(), esp.getNombre() });
			}
		}
	}

	private Especialidad buscarLocal(String codigo) {
		try {
			int codigoInt = Integer.parseInt(codigo);
			if (listaGlobalEspecialidades != null) {
				for (Especialidad esp : listaGlobalEspecialidades) {
					if (esp.getCodigoEspecialidad() == codigoInt) return esp;
				}
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
	}
}