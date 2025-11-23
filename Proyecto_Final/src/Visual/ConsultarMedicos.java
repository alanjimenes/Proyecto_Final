package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Cliente;
import logico.Clinica;
import logico.Medico;
import Visual.ClienteSocket;

public class ConsultarMedicos extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static JTable table;
	public static DefaultTableModel modelo = new DefaultTableModel() {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	public static Object[] row;
	private Medico seleccionado = null;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnVerDetalles;
	private JTextField txtFiltro;

	/**
	 * Create the dialog.
	 */
	public ConsultarMedicos() {
		setTitle("LISTADO DE MÉDICOS");
		setBounds(100, 100, 1065, 534);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane, BorderLayout.NORTH);
				{
					table = new JTable();
					table.setFont(new Font("Tahoma", Font.PLAIN, 15));
					table.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							int index = table.getSelectedRow();
							if (index >= 0) {
								String cedula = table.getValueAt(index, 0).toString();
								seleccionado = Clinica.getInstancia().buscarMedicoCedula(cedula);

								btnDelete.setEnabled(true);
								btnUpdate.setEnabled(true);
								btnVerDetalles.setEnabled(true);
							}
						}
					});
					String[] headers = { "Cédula", "Nombre", "Apellido", "Especialidad", "Teléfono", "Citas Máx. Día" };
					modelo.setColumnIdentifiers(headers);
					table.setModel(modelo);
					JTableHeader header = table.getTableHeader();
					header.setBackground(new Color(60, 70, 123));
					header.setForeground(Color.WHITE);
					header.setOpaque(true);

					header.setReorderingAllowed(false);
					scrollPane.setViewportView(table);
				}
			}
			table.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() == 2) {
						int fila = table.getSelectedRow();
						if (fila >= 0) {
							dispose();
						}
					}
				}
			});

		}
		{
			JPanel panel = new JPanel();
			panel.setBackground(new Color(60, 70, 123));
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblNewLabel = new JLabel("Filtrar por Nombre:");
				lblNewLabel.setForeground(Color.WHITE);
				lblNewLabel.setFont(new Font("Bahnschrift", Font.BOLD, 18));
				panel.add(lblNewLabel);
			}
			{
				txtFiltro = new JTextField();
				txtFiltro.setFont(new Font("Tahoma", Font.PLAIN, 16));
				txtFiltro.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						filtrar();
					}
				});
				panel.add(txtFiltro);
				txtFiltro.setColumns(16);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(60, 70, 123));
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnUpdate = new JButton("Modificar");
				Estilos.estilarBoton(btnUpdate, new Color(41, 128, 185), Color.WHITE);
				btnUpdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (seleccionado != null) {
							RegMedico registro = new RegMedico(seleccionado);
							registro.setModal(true);
							registro.setVisible(true);

							cargarMedicos();

							seleccionado = null;
							btnDelete.setEnabled(false);
							btnUpdate.setEnabled(false);
							btnVerDetalles.setEnabled(false);
							table.clearSelection();
						}
					}
				});
				{
					btnVerDetalles = new JButton("Ver Detalles");
					btnVerDetalles.setEnabled(false);
					btnVerDetalles.setFont(new Font("Tahoma", Font.BOLD, 16));
					Estilos.estilarBoton(btnVerDetalles, new Color(176, 206, 136), Color.WHITE);
					btnVerDetalles.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							if (seleccionado != null) {
								int consultasHechas = seleccionado.getConsultasRealizadas().size();
								int citasPendientes = seleccionado.getCitasAsignadas().size();

								String mensaje = "Detalle para Dr/a. " + seleccionado.getNombre() + " "
										+ seleccionado.getApellido() + "\n\n" + "Especialidad: \t"
										+ seleccionado.getEspecialidad().getNombre() + "\n" + "Consultas Realizadas: \t"
										+ consultasHechas + "\n" + "Citas Asignadas (Pendientes): \t" + citasPendientes;

								JOptionPane.showMessageDialog(null, mensaje, "Información del Médico",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
					});
					buttonPane.add(btnVerDetalles);
				}
				btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
				btnUpdate.setEnabled(false);
				btnUpdate.setActionCommand("OK");
				buttonPane.add(btnUpdate);
				getRootPane().setDefaultButton(btnUpdate);
			}
			{
				btnDelete = new JButton("Eliminar");
				Estilos.estilarBoton(btnDelete, new Color(231, 76, 60), Color.WHITE);
				btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));
				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (seleccionado != null) {
							int option = JOptionPane.showConfirmDialog(
									null, "¿Está seguro que desea eliminar al médico con Cédula: "
											+ seleccionado.getCedula() + "?",
											"Eliminar Médico", JOptionPane.WARNING_MESSAGE);

							if (option == JOptionPane.OK_OPTION) {
								if (seleccionado.getCitasAsignadas().isEmpty()) {
									Clinica.getInstancia().getMedicos().remove(seleccionado);
									Clinica.getInstancia().guardarDatosClinica();
									cargarMedicos();
									JOptionPane.showMessageDialog(null, "Médico eliminado correctamente.");

									btnDelete.setEnabled(false);
									btnUpdate.setEnabled(false);
									btnVerDetalles.setEnabled(false);
									seleccionado = null;
								} else {
									JOptionPane.showMessageDialog(null,
											"El médico no se puede eliminar porque tiene Citas Asignadas pendientes.",
											"Advertencia", JOptionPane.WARNING_MESSAGE);
								}
							}
						}
					}
				});
				btnDelete.setEnabled(false);
				buttonPane.add(btnDelete);
			}
			{
				JButton btnCancelar = new JButton("Cancelar");
				Estilos.estilarBoton(btnCancelar, new Color(127, 140, 141), Color.WHITE);
				btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 16));
				btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnCancelar.setActionCommand("Cancel");
				buttonPane.add(btnCancelar);
			}
		}

		cargarMedicos();
		table.getTableHeader().setReorderingAllowed(false);
	}

	public void filtrar() {
		modelo.setRowCount(0);
		row = new Object[table.getColumnCount()];

		for (Medico aux : Clinica.getInstancia().getMedicos()) {
			if (aux.getNombre().toLowerCase().contains(txtFiltro.getText().toLowerCase())
					|| txtFiltro.getText().isEmpty()) {
				row[0] = aux.getCedula();
				row[1] = aux.getNombre();
				row[2] = aux.getApellido();
				row[3] = aux.getEspecialidad().getNombre();
				row[4] = aux.getTelefono();
				row[5] = aux.getMaxCitasPorDia();
				modelo.addRow(row);
			}
		}

		seleccionado = null;
		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnVerDetalles.setEnabled(false);
	}

	public static void cargarMedicos() {
		ArrayList<Cliente> lista = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_MEDICOS", null);
	}
}