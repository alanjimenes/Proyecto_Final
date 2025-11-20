package Visual;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import logico.Cita;
import logico.Cliente;
import logico.Clinica;
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

	/**
	 * Create the panel.
	 */
	public GestionCitas() {
		setLayout(null);
		JPanel panelFormulario = new JPanel();
		panelFormulario.setBounds(10, 13, 860, 200);
		panelFormulario.setLayout(null);
		add(panelFormulario);

		//CLIENTE
		JLabel lblCodCliente = new JLabel("Cód. Cliente:");
		lblCodCliente.setBounds(10, 15, 80, 14);
		panelFormulario.add(lblCodCliente);

		txtCodCliente = new JTextField();
		txtCodCliente.setBounds(90, 12, 100, 20);
		panelFormulario.add(txtCodCliente);

		btnBuscarCliente = new JButton("Buscar");
		btnBuscarCliente.setBounds(200, 11, 80, 23);
		panelFormulario.add(btnBuscarCliente);

		lblNombreCliente = new JLabel("Paciente: (Busque un cliente)");
		lblNombreCliente.setBounds(290, 15, 250, 14);
		panelFormulario.add(lblNombreCliente);

		//MEDICO
		JLabel lblCedMedico = new JLabel("Cédula Médico:");
		lblCedMedico.setBounds(10, 45, 80, 14);
		panelFormulario.add(lblCedMedico);

		txtCedulaMedico = new JTextField();
		txtCedulaMedico.setBounds(90, 42, 100, 20);
		panelFormulario.add(txtCedulaMedico);

		btnBuscarMedico = new JButton("Buscar");
		btnBuscarMedico.setBounds(200, 41, 80, 23);
		panelFormulario.add(btnBuscarMedico);

		lblNombreMedico = new JLabel("Médico: (Busque un médico)");
		lblNombreMedico.setBounds(290, 45, 250, 14);
		panelFormulario.add(lblNombreMedico);

		//FECHA Y HORA
		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setBounds(10, 75, 80, 14);
		panelFormulario.add(lblFecha);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(90, 72, 130, 20);
		panelFormulario.add(dateChooser);

		JLabel lblHora = new JLabel("Hora:");
		lblHora.setBounds(230, 75, 40, 14);
		panelFormulario.add(lblHora);

		cbxHora = new JComboBox<>();
		cbxHora.setBounds(270, 72, 80, 20);
		for (int h = 8; h <= 17; h++) {
		    String ampm = (h < 12) ? "AM" : "PM";
		    int hora12 = (h == 12) ? 12 : (h % 12);

		    cbxHora.addItem(String.format("%02d:00 %s", hora12, ampm));
		    cbxHora.addItem(String.format("%02d:30 %s", hora12, ampm));
		}
		panelFormulario.add(cbxHora);

		//BOTONES
		btnCrearCita = new JButton("Crear Cita");
		btnCrearCita.setBounds(10, 142, 138, 45);
		panelFormulario.add(btnCrearCita);

		btnModificarCita = new JButton("Modificar Cita");
		btnModificarCita.setBounds(177, 142, 138, 45);
		panelFormulario.add(btnModificarCita);

		btnCancelarCita = new JButton("Cancelar Cita");
		btnCancelarCita.setBounds(343, 142, 138, 45);
		panelFormulario.add(btnCancelarCita);

		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setBounds(507, 142, 138, 45);
		panelFormulario.add(btnLimpiar);


		/*LO IMPORTANTE DEL CODIGO (LA TABLA DE CITAS)
		 * CUIDAO AL TOPAR AQUI
		 */
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 220, 860, 67);
		add(scrollPane);

		tablaCitas = new JTable();
		tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] {"Código", "Paciente", "Médico", "Fecha y Hora", "Estado"});
		tablaCitas.setModel(model);
		scrollPane.setViewportView(tablaCitas);
		cargarTablaCitas();

		btnBuscarCliente.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        ConsultarClientes dialog = new ConsultarClientes();
		        dialog.setModal(true);
		        dialog.setVisible(true);
		        
		        int fila = ConsultarClientes.table.getSelectedRow();
		        if (fila >= 0) {
		            String codigo = ConsultarClientes.table.getValueAt(fila, 0).toString();
		            clienteSeleccionado = Clinica.getInstancia().buscarClientePorCodigo(codigo);

		            if (clienteSeleccionado != null) {
		                txtCodCliente.setText(clienteSeleccionado.getNumExpediente());
		                lblNombreCliente.setText("Paciente: " + clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellido());
		            }
		        }
		    }
		});

		btnBuscarMedico.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        ConsultarMedicos dialog = new ConsultarMedicos();
		        dialog.setModal(true);
		        dialog.setVisible(true);

		        int fila = ConsultarMedicos.table.getSelectedRow();
		        if (fila >= 0) {
		            String cedula = ConsultarMedicos.table.getValueAt(fila, 0).toString();
		            medicoSeleccionado = Clinica.getInstancia().buscarMedicoCedula(cedula);

		            if (medicoSeleccionado != null) {
		                txtCedulaMedico.setText(medicoSeleccionado.getCedula());
		                lblNombreMedico.setText("Médico: " + medicoSeleccionado.getNombre() + 
		                   " (" + medicoSeleccionado.getEspecialidad().getNombre() + ")");
		            }
		        }
		    }
		});


		btnCrearCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(clienteSeleccionado == null || medicoSeleccionado == null || dateChooser.getDate() == null) {
					JOptionPane.showMessageDialog(null, "Debe buscar un cliente, un médico y seleccionar una fecha.");
					return;
				}
				LocalDate fecha = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				String seleccion = cbxHora.getSelectedItem().toString();  // Ej: "02:30 PM"

				String horaStr = seleccion.substring(0, 2);
				String minStr  = seleccion.substring(3,5);
				String ampm    = seleccion.substring(6);

				int hora = Integer.parseInt(horaStr);

				if (ampm.equals("PM") && hora != 12) hora += 12;
				if (ampm.equals("AM") && hora == 12) hora = 0;

				LocalTime horaConvertida = LocalTime.of(hora, Integer.parseInt(minStr));


				LocalDateTime fechaHora = LocalDateTime.of(fecha, horaConvertida);


				if (fechaHora.isBefore(LocalDateTime.now())) {
				    JOptionPane.showMessageDialog(null, "No puede crear una cita en una fecha y hora pasadas.");
				    return;
				}

				boolean exito = Clinica.getInstancia().crearCita(
				        fechaHora, 
				        medicoSeleccionado.getCedula(), 
				        clienteSeleccionado.getNumExpediente()
				);

				if(exito) {
					JOptionPane.showMessageDialog(null, "¡Cita creada con éxito!");
					cargarTablaCitas();
					limpiarCampos();
				} else {
					JOptionPane.showMessageDialog(null, "Error: El médico no está disponible a esa hora o ya alcanzó su límite diario.");
				}
			}
		});

		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiarCampos();
			}
		});


		tablaCitas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int filaSeleccionada = tablaCitas.getSelectedRow();
				if (filaSeleccionada >= 0) {
					String codigoCita = (String) model.getValueAt(filaSeleccionada, 0);
					citaSeleccionada = Clinica.getInstancia().buscarCita(codigoCita); 

					if (citaSeleccionada != null) {
						clienteSeleccionado = citaSeleccionada.getCliente(); 
						medicoSeleccionado = citaSeleccionada.getMedico(); 
						txtCodCliente.setText(clienteSeleccionado.getNumExpediente());
						txtCedulaMedico.setText(medicoSeleccionado.getCedula());
						lblNombreCliente.setText("Paciente: " + clienteSeleccionado.getNombre());
						lblNombreMedico.setText("Médico: " + medicoSeleccionado.getNombre());	
						dateChooser.setDate(Date.from(citaSeleccionada.getFechaHora().atZone(ZoneId.systemDefault()).toInstant())); 
						cbxHora.setSelectedItem(String.format("%02d:%02d", citaSeleccionada.getFechaHora().getHour(), citaSeleccionada.getFechaHora().getMinute())); 
					}
				}
			}
		});

		btnModificarCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(citaSeleccionada == null) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar una cita de la tabla primero.");
					return;
				}
				LocalDate fecha = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				String[] horaMinuto = cbxHora.getSelectedItem().toString().split(":");
				LocalTime hora = LocalTime.of(Integer.parseInt(horaMinuto[0]), Integer.parseInt(horaMinuto[1]));
				LocalDateTime nuevaFechaHora = LocalDateTime.of(fecha, hora);
				boolean exito = Clinica.getInstancia().editCita(citaSeleccionada, nuevaFechaHora); 
				if(exito) {
					JOptionPane.showMessageDialog(null, "¡Cita modificada!");
					cargarTablaCitas();
					limpiarCampos();
				} else {
					JOptionPane.showMessageDialog(null, "Error: No se pudo modificar. El médico no está disponible a esa nueva hora o la cita ya pasó.");
				}
			}
		});

		btnCancelarCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(citaSeleccionada == null) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar una cita de la tabla.");
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea cancelar la cita " + citaSeleccionada.getCodigo_cita() 
				+ "?", "Confirmar cancelación", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					boolean exito = Clinica.getInstancia().cancelCita(citaSeleccionada); 

					if(exito) {
						JOptionPane.showMessageDialog(null, "¡Cita cancelada!");
						cargarTablaCitas();
						limpiarCampos();
					} else {
						JOptionPane.showMessageDialog(null, "Error: No se puede cancelar una cita que ya pasó.");
					}
				}
			}
		});
	}

	private void cargarTablaCitas() {
		model.setRowCount(0); 
		ArrayList<Cita> citas = Clinica.getInstancia().getCitas(); 
		for(Cita c : citas) {
			Object[] fila = new Object[5];
			fila[0] = c.getCodigo_cita();
			fila[1] = c.getCliente().getNombre() + " " + c.getCliente().getApellido();
			fila[2] = c.getMedico().getNombre();
			fila[3] = c.getFechaHora().toString();
			fila[4] = c.getEstado();
			model.addRow(fila);
		}
	}

	private void limpiarCampos() {
		txtCodCliente.setText("");
		txtCedulaMedico.setText("");
		lblNombreCliente.setText("Paciente: (Busque un cliente)");
		lblNombreMedico.setText("Médico: (Busque un médico)");
		dateChooser.setDate(null);
		cbxHora.setSelectedIndex(0);
		clienteSeleccionado = null;
		medicoSeleccionado = null;
		citaSeleccionada = null;
		tablaCitas.clearSelection();
	}
}