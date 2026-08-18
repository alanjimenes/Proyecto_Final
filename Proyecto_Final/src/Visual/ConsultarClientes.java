package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ConsultarClientes extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static JTable table;
	public static DefaultTableModel modelo;
	public static Object[] row;
	private Cliente seleccionado = null;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JTextField txtFiltro;
	private ArrayList<Cliente> listaGlobalClientes;

	public ConsultarClientes() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(ConsultarClientes.class.getResource("/img/seguro-de-salud.png")));
		setTitle("Gestión de Clientes");
		setBounds(100, 100, 1000, 600);
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
		{
			JScrollPane scrollPane = new JScrollPane();
			panel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				table.setFont(new Font("Tahoma", Font.PLAIN, 15));

				modelo = new DefaultTableModel() {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				String[] headers = { "Expediente", "C�dula", "Nombre", "Apellido", "Teléfono", "Estado Salud" };
				modelo.setColumnIdentifiers(headers);
				table.setModel(modelo);

				JTableHeader header = table.getTableHeader();
				header.setBackground(new Color(60, 70, 123));
				header.setForeground(Color.WHITE);
				header.setOpaque(true);
				header.setReorderingAllowed(false);
				scrollPane.setViewportView(table);

				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						int index = table.getSelectedRow();
						if (index >= 0) {
							Object objCedula = table.getValueAt(index, 1);

							if (objCedula != null) {
								String cedula = objCedula.toString();
								seleccionado = buscarEnCachePorCedula(cedula);

								if (seleccionado != null) {
									btnDelete.setEnabled(true);
									btnUpdate.setEnabled(true);

									if (e.getClickCount() == 2) {
										dispose();
									}
								}
							}
						}
					}
				});
			}
		}

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(60, 70, 123));
		contentPanel.add(panelNorte, BorderLayout.NORTH);
		{
			JLabel lblFiltro = new JLabel("Filtrar por Nombre:");
			lblFiltro.setForeground(Color.WHITE);
			lblFiltro.setFont(new Font("Bahnschrift", Font.BOLD, 18));
			panelNorte.add(lblFiltro);
		}
		{
			txtFiltro = new JTextField();
			txtFiltro.setFont(new Font("Tahoma", Font.PLAIN, 16));
			txtFiltro.setColumns(20);
			txtFiltro.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					filtrarLocal(txtFiltro.getText());
				}
			});
			panelNorte.add(txtFiltro);
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(60, 70, 123));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			btnUpdate = new JButton("Modificar");
			Estilos.estilarBoton(btnUpdate, new Color(41, 128, 185), Color.WHITE);
			btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnUpdate.setEnabled(false);
			btnUpdate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (seleccionado != null) {
						RegClientes reg = new RegClientes(seleccionado);
						reg.setModal(true);
						reg.setVisible(true);
						cargarClientesDesdeServer();
						resetBotones();
					}
				}
			});
			buttonPane.add(btnUpdate);
		}
		{
			btnDelete = new JButton("Eliminar");
			Estilos.estilarBoton(btnDelete, new Color(231, 76, 60), Color.WHITE);
			btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnDelete.setEnabled(false);
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (seleccionado != null) {
						int option = JOptionPane.showConfirmDialog(null,
								"¿Seguro que desea eliminar al paciente " + seleccionado.getNombre() + "?", "Confirmar",
								JOptionPane.YES_NO_OPTION);

						if (option == JOptionPane.YES_OPTION) {
							ClienteSocket.enviar("DELETE_CLIENTE", seleccionado.getCedula());
							cargarClientesDesdeServer();
							resetBotones();
						}
					}
				}
			});
			buttonPane.add(btnDelete);
		}
		{
			JButton btnCancelar = new JButton("Cerrar");
			Estilos.estilarBoton(btnCancelar, new Color(231, 76, 60), Color.WHITE);
			btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			buttonPane.add(btnCancelar);
		}

		cargarClientesDesdeServer();
	}

	@SuppressWarnings("unchecked")
	private void cargarClientesDesdeServer() {
		listaGlobalClientes = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_CLIENTES", null);
		if (listaGlobalClientes == null)
			listaGlobalClientes = new ArrayList<>();
		filtrarLocal("");
	}

	private void filtrarLocal(String filtro) {
		modelo.setRowCount(0);
		row = new Object[6];
		String f = filtro.toLowerCase();

		for (Cliente cli : listaGlobalClientes) {
			if (cli.isEstado()
					&& (f.isEmpty() || cli.getNombre().toLowerCase().contains(f) || cli.getCedula().contains(f))) {
				row[0] = cli.getNumExpediente();
				row[1] = cli.getCedula();
				row[2] = cli.getNombre();
				row[3] = cli.getApellido();
				row[4] = cli.getTelefono();
				row[5] = cli.isEnfermo() ? "Enfermo" : "Sano";
				modelo.addRow(row);
			}
		}
	}

	private Cliente buscarEnCachePorCedula(String cedula) {
		for (Cliente c : listaGlobalClientes) {
			if (c.getCedula() != null && c.getCedula().equals(cedula)) {
				return c;
			}
		}
		return null;
	}

	private void resetBotones() {
		seleccionado = null;
		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		txtFiltro.setText("");
		table.clearSelection();
	}

	public Cliente getClienteSeleccionado() {
		return seleccionado;
	}
}