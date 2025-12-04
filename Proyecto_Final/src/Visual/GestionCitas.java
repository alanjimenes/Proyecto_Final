package Visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import logico.Cita;
import logico.Cliente;
import logico.Medico;

public class GestionCitas extends JPanel {

	private JTable tablaCitas;
	private DefaultTableModel model;
	private JTextField txtCodCliente;
	private JTextField txtCedulaMedico;
	private JLabel lblNombreCliente;
	private JLabel lblNombreMedico;
	private JDateChooser dateChooser;
	private JComboBox<String> cbxHora;
	private JButton btnCrearCita;
	private JButton btnModificarCita;
	private JButton btnCancelarCita;
	private JButton btnLimpiar;
	private JButton btnBuscarCliente;
	private JButton btnBuscarMedico;
	private Cliente clienteSeleccionado = null;
	private Medico medicoSeleccionado = null;
	private Cita citaSeleccionada = null;
	private JTextArea txtMotivo;

	public GestionCitas() {
		setBackground(new Color(60, 70, 123));
		setLayout(null);
		JPanel panelFormulario = new JPanel();
		panelFormulario.setBackground(new Color(60, 70, 123));
		panelFormulario.setBounds(10, 10, 1096, 269);
		panelFormulario.setLayout(null);
		add(panelFormulario);

<<<<<<< HEAD
		// CLIENTE
		JLabel lblCodCliente = new JLabel("Cód. Cliente:");
		lblCodCliente.setForeground(Color.WHITE);
		lblCodCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblCodCliente.setBounds(10, 45, 100, 14);
		panelFormulario.add(lblCodCliente);
=======
		 JLabel lblCodCliente = new JLabel("Cód. Cliente:");
		 lblCodCliente.setForeground(Color.WHITE);
		 lblCodCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		 lblCodCliente.setBounds(10, 45, 100, 14);
		 panelFormulario.add(lblCodCliente);
>>>>>>> branch 'master' of https://github.com/alanjimenes/Proyecto_Final.git

		txtCodCliente = new JTextField();
		txtCodCliente.setBounds(122, 42, 142, 20);
		panelFormulario.add(txtCodCliente);

		btnBuscarCliente = new JButton("Buscar");
		Estilos.estilarBoton(btnBuscarCliente, new Color(127, 140, 141), Color.WHITE);
		btnBuscarCliente.setBounds(274, 41, 102, 23);
		panelFormulario.add(btnBuscarCliente);

		lblNombreCliente = new JLabel("Paciente: (Busque un cliente)");
		lblNombreCliente.setForeground(Color.WHITE);
		lblNombreCliente.setBounds(386, 45, 300, 14);
		panelFormulario.add(lblNombreCliente);

		// MEDICO
		JLabel lblCedMedico = new JLabel("Cédula Médico:");
		lblCedMedico.setForeground(Color.WHITE);
		lblCedMedico.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblCedMedico.setBounds(10, 85, 100, 14);
		panelFormulario.add(lblCedMedico);

		txtCedulaMedico = new JTextField();
		txtCedulaMedico.setBounds(120, 82, 149, 20);
		panelFormulario.add(txtCedulaMedico);

		btnBuscarMedico = new JButton("Buscar");
		Estilos.estilarBoton(btnBuscarMedico, new Color(127, 140, 141), Color.WHITE);
		btnBuscarMedico.setBounds(274, 81, 102, 23);
		panelFormulario.add(btnBuscarMedico);

		lblNombreMedico = new JLabel("Médico: (Busque un médico)");
		lblNombreMedico.setForeground(Color.WHITE);
		lblNombreMedico.setBounds(386, 85, 300, 14);
		panelFormulario.add(lblNombreMedico);

		// FECHA Y HORA
		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setForeground(Color.WHITE);
		lblFecha.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblFecha.setBounds(10, 123, 80, 14);
		panelFormulario.add(lblFecha);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(120, 120, 149, 20);
		dateChooser.getJCalendar().setMinSelectableDate(new Date());
		panelFormulario.add(dateChooser);

		JLabel lblHora = new JLabel("Hora:");
		lblHora.setForeground(Color.WHITE);
		lblHora.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblHora.setBounds(10, 163, 40, 14);
		panelFormulario.add(lblHora);

		cbxHora = new JComboBox<>();
		cbxHora.setBounds(120, 163, 149, 20);
		for (int h = 8; h <= 17; h++) {
			String ampm = (h < 12) ? "AM" : "PM";
			int hora12 = (h > 12) ? (h - 12) : ((h == 0 || h == 12) ? 12 : h);
			cbxHora.addItem(String.format("%02d:00 %s", hora12, ampm));
			cbxHora.addItem(String.format("%02d:30 %s", hora12, ampm));
		}
		panelFormulario.add(cbxHora);

		JLabel lblMotivo = new JLabel("Motivo:");
		lblMotivo.setForeground(Color.WHITE);
		lblMotivo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblMotivo.setBounds(593, 29, 60, 14);
		panelFormulario.add(lblMotivo);

		JScrollPane scrollMotivo = new JScrollPane();
		scrollMotivo.setBounds(593, 54, 450, 123);
		panelFormulario.add(scrollMotivo);

		txtMotivo = new JTextArea();
		scrollMotivo.setViewportView(txtMotivo);
		txtMotivo.setLineWrap(true);

		// BOTONES
		btnCancelarCita = new JButton("Cancelar Cita");
		Estilos.estilarBoton(btnCancelarCita, new Color(231, 76, 60), Color.WHITE);
		btnCancelarCita.setBounds(593, 213, 155, 45);
		btnCancelarCita.setEnabled(false);
		panelFormulario.add(btnCancelarCita);

		btnLimpiar = new JButton("Limpiar");
		Estilos.estilarBoton(btnLimpiar, new Color(110, 140, 251), Color.WHITE);
		btnLimpiar.setBounds(888, 213, 155, 45);
		panelFormulario.add(btnLimpiar);

		btnModificarCita = new JButton("Modificar Cita");
		Estilos.estilarBoton(btnModificarCita, new Color(41, 128, 185), Color.WHITE);
		btnModificarCita.setBounds(322, 213, 149, 45);
		btnModificarCita.setEnabled(false);
		panelFormulario.add(btnModificarCita);

		btnCrearCita = new JButton("Crear Cita");
		Estilos.estilarBoton(btnCrearCita, new Color(99, 163, 97), Color.WHITE);
		btnCrearCita.setBounds(51, 213, 155, 45);
		panelFormulario.add(btnCrearCita);

		// ACCIONES
		btnBuscarCliente.addActionListener(e -> {
			ConsultarClientes dialog = new ConsultarClientes();
			dialog.setModal(true);
			dialog.setVisible(true);

			if (ConsultarClientes.table.getSelectedRow() >= 0) {
				String codigo = ConsultarClientes.table.getValueAt(ConsultarClientes.table.getSelectedRow(), 0)
						.toString();
				clienteSeleccionado = (Cliente) ClienteSocket.enviar("BUSCAR_CLIENTE", codigo);
				if (clienteSeleccionado != null) {
					txtCodCliente.setText(clienteSeleccionado.getNumExpediente());
					lblNombreCliente.setText(
							"Paciente: " + clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellido());
				}
			}
		});

		btnBuscarMedico.addActionListener(e -> {
			ConsultarMedicos dialog = new ConsultarMedicos();
			dialog.setModal(true);
			dialog.setVisible(true);

			if (ConsultarMedicos.table.getSelectedRow() >= 0) {
				String cedula = ConsultarMedicos.table.getValueAt(ConsultarMedicos.table.getSelectedRow(), 0)
						.toString();
				medicoSeleccionado = (Medico) ClienteSocket.enviar("BUSCAR_MEDICO", cedula);
				if (medicoSeleccionado != null) {
					txtCedulaMedico.setText(medicoSeleccionado.getCedula());
					lblNombreMedico.setText("Médico: " + medicoSeleccionado.getNombre() + " ("
							+ medicoSeleccionado.getEspecialidad().getNombre() + ")");
				}
			}
		});

		btnCrearCita.addActionListener(e -> crearCita());
		btnModificarCita.addActionListener(e -> modificarCita());
		btnCancelarCita.addActionListener(e -> cancelarCita());
		btnLimpiar.addActionListener(e -> limpiarCampos());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 290, 1096, 399);
		add(scrollPane);

		tablaCitas = new JTable();
		tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaCitas.getTableHeader().setReorderingAllowed(false);
		tablaCitas.getTableHeader().setBackground(new Color(80, 88, 156));
		tablaCitas.getTableHeader().setForeground(Color.WHITE);
		tablaCitas.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 14));

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.setColumnIdentifiers(new String[] { "Código", "Paciente", "Médico", "Fecha y Hora", "Estado" });
		tablaCitas.setModel(model);
		scrollPane.setViewportView(tablaCitas);

		tablaCitas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				seleccionarDeTabla();
			}
		});

		cargarTablaCitas();
	}

	private void crearCita() {
		if (clienteSeleccionado == null || medicoSeleccionado == null || dateChooser.getDate() == null) {
			JOptionPane.showMessageDialog(null, "Debe buscar un cliente, un médico y seleccionar una fecha.");
			return;
		}
		if (txtMotivo.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe especificar el motivo.");
			return;
		}

		LocalDateTime fechaHora = construirFechaHora();
		if (fechaHora == null)
			return;

		if (fechaHora.isBefore(LocalDateTime.now())) {
			JOptionPane.showMessageDialog(null, "La fecha no puede ser en el pasado.");
			return;
		}

		Cita tempCita = new Cita(fechaHora, clienteSeleccionado, medicoSeleccionado, "Pendiente", txtMotivo.getText());
		Object respuesta = ClienteSocket.enviar("REG_CITA", tempCita);
		boolean exito = (respuesta instanceof Boolean) ? (Boolean) respuesta : false;

		if (exito) {
			JOptionPane.showMessageDialog(null, "¡Cita agendada exitosamente!");
			cargarTablaCitas();
			limpiarCampos();
		} else {
			JOptionPane.showMessageDialog(null, "Error: Médico no disponible en ese horario.");
		}
	}

	private void modificarCita() {
		if (citaSeleccionada == null || dateChooser.getDate() == null || medicoSeleccionado == null) {
			JOptionPane.showMessageDialog(null, "Faltan datos para modificar.");
			return;
		}

		LocalDateTime nuevaFecha = construirFechaHora();
		if (nuevaFecha == null)
			return;

		citaSeleccionada.setFechaHora(nuevaFecha);
		citaSeleccionada.setMedico(medicoSeleccionado);
		citaSeleccionada.setMotivo(txtMotivo.getText());

		Object respuesta = ClienteSocket.enviar("EDIT_CITA", citaSeleccionada);
		boolean exito = (respuesta instanceof Boolean) ? (Boolean) respuesta : false;

		if (exito) {
			JOptionPane.showMessageDialog(null, "Cita modificada correctamente.");
			cargarTablaCitas();
			limpiarCampos();
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo modificar (Conflicto de horario).");
		}
	}

	private void cancelarCita() {
		if (citaSeleccionada == null)
			return;

		int confirm = JOptionPane.showConfirmDialog(null,
				"¿Cancelar la cita " + citaSeleccionada.getCodigo_cita() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			Object respuesta = ClienteSocket.enviar("CANCEL_CITA", citaSeleccionada);
			boolean exito = (respuesta instanceof Boolean) ? (Boolean) respuesta : false;

			if (exito) {
				JOptionPane.showMessageDialog(null, "Cita cancelada.");
				cargarTablaCitas();
				limpiarCampos();
			} else {
				JOptionPane.showMessageDialog(null, "Error al cancelar (Quizás ya pasó la fecha).");
			}
		}
	}

	private LocalDateTime construirFechaHora() {
		try {
			LocalDate fecha = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String seleccion = cbxHora.getSelectedItem().toString();
			String[] parts = seleccion.split(" ");
			String[] timeParts = parts[0].split(":");

			int h = Integer.parseInt(timeParts[0]);
			int m = Integer.parseInt(timeParts[1]);
			String ampm = parts[1];

			if (ampm.equalsIgnoreCase("PM") && h != 12)
				h += 12;
			if (ampm.equalsIgnoreCase("AM") && h == 12)
				h = 0;

			return LocalDateTime.of(fecha, LocalTime.of(h, m));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en el formato de fecha/hora.");
			return null;
		}
	}

	private void seleccionarDeTabla() {
		int fila = tablaCitas.getSelectedRow();
		if (fila >= 0) {
			String codigo = model.getValueAt(fila, 0).toString();
			citaSeleccionada = (Cita) ClienteSocket.enviar("BUSCAR_CITA", codigo);

			if (citaSeleccionada != null) {
				clienteSeleccionado = citaSeleccionada.getCliente();
				medicoSeleccionado = citaSeleccionada.getMedico();

				txtCodCliente.setText(clienteSeleccionado.getNumExpediente());
				txtCedulaMedico.setText(medicoSeleccionado.getCedula());
				lblNombreCliente.setText("Paciente: " + clienteSeleccionado.getNombre());
				lblNombreMedico.setText("Médico: " + medicoSeleccionado.getNombre());
				txtMotivo.setText(citaSeleccionada.getMotivo());

				dateChooser
						.setDate(Date.from(citaSeleccionada.getFechaHora().atZone(ZoneId.systemDefault()).toInstant()));

				boolean activa = !citaSeleccionada.getEstado().equalsIgnoreCase("Completada")
						&& !citaSeleccionada.getEstado().equalsIgnoreCase("Cancelada");
				btnModificarCita.setEnabled(activa);
				btnCancelarCita.setEnabled(activa);
				btnCrearCita.setEnabled(false);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void cargarTablaCitas() {
		model.setRowCount(0);
		ArrayList<Cita> citas = (ArrayList<Cita>) ClienteSocket.enviar("LISTAR_CITAS", null);

		if (citas != null) {
			for (Cita c : citas) {
				String cliente = (c.getCliente() != null) ? c.getCliente().getNombre() : "N/A";
				String medico = (c.getMedico() != null) ? c.getMedico().getNombre() : "N/A";
				String fecha = (c.getFechaHora() != null) ? c.getFechaHora().toString().replace("T", " ") : "N/A";

<<<<<<< HEAD
				model.addRow(new Object[] { c.getCodigo_cita(), cliente, medico, fecha, c.getEstado() });
			}
		}
	}

	private void limpiarCampos() {
		txtCodCliente.setText("");
		txtCedulaMedico.setText("");
		lblNombreCliente.setText("Paciente: (Busque un cliente)");
		lblNombreMedico.setText("Médico: (Busque un médico)");
		dateChooser.setDate(null);
		cbxHora.setSelectedIndex(0);
		txtMotivo.setText("");

		clienteSeleccionado = null;
		medicoSeleccionado = null;
		citaSeleccionada = null;
		tablaCitas.clearSelection();
=======
	 private void limpiarCampos() {
		 txtCodCliente.setText("");
		 txtCedulaMedico.setText("");
		 lblNombreCliente.setText("Paciente: (Busque un cliente)");
		 lblNombreMedico.setText("Médico: (Busque un médico)");
		 dateChooser.setDate(null);
		 cbxHora.setSelectedIndex(0);
	
		 txtMotivo.setText("");
		 clienteSeleccionado = null;
		 medicoSeleccionado = null;
		 citaSeleccionada = null;
		 tablaCitas.clearSelection();
>>>>>>> branch 'master' of https://github.com/alanjimenes/Proyecto_Final.git

<<<<<<< HEAD
		btnCrearCita.setEnabled(true);
		btnModificarCita.setEnabled(false);
		btnCancelarCita.setEnabled(false);
	}
=======
		 
		 btnCrearCita.setEnabled(true);
		 btnModificarCita.setEnabled(false); 
		 btnCancelarCita.setEnabled(false); 
	 }
>>>>>>> branch 'master' of https://github.com/alanjimenes/Proyecto_Final.git
}