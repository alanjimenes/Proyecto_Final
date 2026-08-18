package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Medico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ConsultarMedicos extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static JTable table;
	public static DefaultTableModel modelo;
	public static Object[] row;
	private Medico seleccionado = null;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnVerDetalles;
	private JTextField txtFiltro;
	private ArrayList<Medico> listaMedicosGlobal;

	public ConsultarMedicos() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarMedicos.class.getResource("/img/doctor.png")));
		setTitle("Listado de Médicos");
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

					modelo = new DefaultTableModel() {
						@Override
						public boolean isCellEditable(int row, int column) {
							return false;
						}
					};
					String[] headers = { "Cédula", "Nombre", "Apellido", "Especialidad", "Teléfono", "Citas Máx. Díaa" };
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
								String cedula = table.getValueAt(index, 0).toString();
								seleccionado = buscarEnCache(cedula);

								btnDelete.setEnabled(true);
								btnUpdate.setEnabled(true);
								btnVerDetalles.setEnabled(true);

								if (e.getClickCount() == 2) {
									dispose();
								}
							}
						}
					});
				}
			}
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
				txtFiltro.setColumns(16);
				txtFiltro.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						filtrarLocal(txtFiltro.getText());
					}
				});
				panel.add(txtFiltro);
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
				btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
				btnUpdate.setEnabled(false);
				btnUpdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (seleccionado != null) {
							RegMedico registro = new RegMedico(seleccionado);
							registro.setModal(true);
							registro.setVisible(true);

							cargarMedicosServer();
							resetBotones();
						}
					}
				});

				btnVerDetalles = new JButton("Ver Detalles");
				btnVerDetalles.setEnabled(false);
				btnVerDetalles.setFont(new Font("Tahoma", Font.BOLD, 16));
				Estilos.estilarBoton(btnVerDetalles, new Color(176, 206, 136), Color.WHITE);
				btnVerDetalles.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (seleccionado != null) {
							int consultasHechas = seleccionado.getConsultasRealizadas() != null
									? seleccionado.getConsultasRealizadas().size()
									: 0;
							int citasPendientes = seleccionado.getCitasAsignadas() != null
									? seleccionado.getCitasAsignadas().size()
									: 0;
							String espNombre = seleccionado.getEspecialidad() != null
									? seleccionado.getEspecialidad().getNombre()
									: "N/A";

							String mensaje = "Detalle para Dr/a. " + seleccionado.getNombre() + " "
									+ seleccionado.getApellido() + "\n\n" + "Especialidad: \t" + espNombre + "\n"
									+ "Consultas Realizadas: \t" + consultasHechas + "\n"
									+ "Citas Asignadas (Pendientes): \t" + citasPendientes;

							JOptionPane.showMessageDialog(null, mensaje, "Información del Médico",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
				buttonPane.add(btnVerDetalles);
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
							int option = JOptionPane.showConfirmDialog(
									null, "¿Está seguro que desea eliminar al médico con Cédula: "
											+ seleccionado.getCedula() + "?",
									"Eliminar Médico", JOptionPane.WARNING_MESSAGE);

							if (option == JOptionPane.OK_OPTION) {
								Object resp = ClienteSocket.enviar("DELETE_MEDICO", seleccionado);
								boolean exito = (resp instanceof Boolean) ? (Boolean) resp : false;

								if (exito) {
									cargarMedicosServer();
									JOptionPane.showMessageDialog(null, "Médico eliminado correctamente.");
									resetBotones();
								} else {
									JOptionPane.showMessageDialog(null,
											"No se pudo eliminar el médico (¿Tiene citas pendientes?).", "Advertencia",
											JOptionPane.WARNING_MESSAGE);
								}
							}
						}
					}
				});
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
				buttonPane.add(btnCancelar);
			}
		}

		cargarMedicosServer();
	}

	@SuppressWarnings("unchecked")
	public void cargarMedicosServer() {
		listaMedicosGlobal = (ArrayList<Medico>) ClienteSocket.enviar("LISTAR_MEDICOS", null);
		if (listaMedicosGlobal == null)
			listaMedicosGlobal = new ArrayList<>();
		filtrarLocal("");
	}

	private void filtrarLocal(String texto) {
		modelo.setRowCount(0);
		row = new Object[6];
		String f = texto.toLowerCase();

		for (Medico med : listaMedicosGlobal) {
			if (med.getEstado() && (f.isEmpty() || med.getNombre().toLowerCase().contains(f))) {
				row[0] = med.getCedula();
				row[1] = med.getNombre();
				row[2] = med.getApellido();
				row[3] = (med.getEspecialidad() != null) ? med.getEspecialidad().getNombre() : "Sin Especialidad";
				row[4] = med.getTelefono();
				row[5] = med.getMaxCitasPorDia();
				modelo.addRow(row);
			}
		}
	}

	private Medico buscarEnCache(String cedula) {
		for (Medico m : listaMedicosGlobal) {
			if (m.getCedula().equals(cedula))
				return m;
		}
		return null;
	}

	private void resetBotones() {
		seleccionado = null;
		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnVerDetalles.setEnabled(false);
		txtFiltro.setText("");
		table.clearSelection();
	}
}