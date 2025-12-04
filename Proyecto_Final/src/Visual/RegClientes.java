package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import logico.Cliente;

public class RegClientes extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JTextField txtTelefono;
	private JTextField txtDireccion;

	private JDateChooser txtFechaNac;
	private Cliente clienteActual = null;
	private JButton okButton;
	private JComboBox<String> cbxGenero;

	public RegClientes() {
		setResizable(false);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(RegClientes.class.getResource("/img/gestion-de-clientes.png")));
		initComponents();
		this.clienteActual = null;
	}

	public RegClientes(Cliente clienteEditar) {
		initComponents();
		this.clienteActual = clienteEditar;
		setTitle("Modificar Paciente");
		okButton.setText("Actualizar");

		txtCedula.setText(clienteEditar.getCedula());
		txtCedula.setEditable(false);
		txtNombre.setText(clienteEditar.getNombre());
		txtApellido.setText(clienteEditar.getApellido());
		txtTelefono.setText(clienteEditar.getTelefono());
		txtDireccion.setText(clienteEditar.getDireccion());
		cbxGenero.setSelectedItem(clienteEditar.getGenero());

		if (clienteEditar.getFechaNacimiento() != null) {
			Date date = Date.from(clienteEditar.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
			txtFechaNac.setDate(date);
		}
	}

	private void initComponents() {
		setTitle("Registrar Paciente");
		setBounds(100, 100, 1054, 550);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// --- FILA 1 ---
		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setForeground(Color.WHITE);
		lblCedula.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblCedula.setBounds(54, 171, 81, 14);
		contentPanel.add(lblCedula);

		txtCedula = new JTextField();
		txtCedula.setBounds(145, 169, 200, 20);
		contentPanel.add(txtCedula);
		txtCedula.setColumns(10);

		txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					e.consume();
					return;
				}
				if (txtCedula.getText().length() >= 13 && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					e.consume();
					return;
				}
				if (c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					if (txtCedula.getText().length() == 3 || txtCedula.getText().length() == 11) {
						txtCedula.setText(txtCedula.getText() + "-");
					}
				}
			}
		});

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblNombre.setBounds(391, 171, 81, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(482, 169, 200, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isLetter(c) && c != ' ' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					e.consume();
				}
			}
		});

		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setForeground(Color.WHITE);
		lblApellido.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblApellido.setBounds(723, 169, 104, 14);
		contentPanel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setBounds(820, 167, 200, 20);
		contentPanel.add(txtApellido);
		txtApellido.setColumns(10);

		txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isLetter(c) && c != ' ' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					e.consume();
				}
			}
		});

		// --- FILA 2 ---
		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setForeground(Color.WHITE);
		lblTelefono.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblTelefono.setBounds(54, 233, 81, 14);
		contentPanel.add(lblTelefono);

		txtTelefono = new JTextField();
		txtTelefono.setBounds(145, 231, 200, 20);
		contentPanel.add(txtTelefono);
		txtTelefono.setColumns(10);

		txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					e.consume();
					return;
				}
				if (txtTelefono.getText().length() >= 15 && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					e.consume();
					return;
				}
				if (c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					if (txtTelefono.getText().length() == 3 || txtTelefono.getText().length() == 7) {
						txtTelefono.setText(txtTelefono.getText() + "-");
					}
				}
			}
		});

		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setForeground(Color.WHITE);
		lblDireccion.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblDireccion.setBounds(391, 235, 93, 14);
		contentPanel.add(lblDireccion);

		txtDireccion = new JTextField();
		txtDireccion.setBounds(482, 233, 200, 20);
		contentPanel.add(txtDireccion);
		txtDireccion.setColumns(10);

		JLabel lblGenero = new JLabel("Género:");
		lblGenero.setForeground(Color.WHITE);
		lblGenero.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblGenero.setBounds(723, 237, 98, 14);
		contentPanel.add(lblGenero);

		cbxGenero = new JComboBox<>();
		cbxGenero.setModel(new DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Masculino", "Femenino" }));
		cbxGenero.setBounds(820, 233, 200, 20);
		contentPanel.add(cbxGenero);

		JLabel lblFecha = new JLabel("F. Nacimiento:");
		lblFecha.setForeground(Color.WHITE);
		lblFecha.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblFecha.setBounds(54, 295, 120, 14);
		contentPanel.add(lblFecha);

		txtFechaNac = new JDateChooser();
		txtFechaNac.setBounds(180, 293, 165, 20);
		((JTextField) txtFechaNac.getDateEditor().getUiComponent()).setEditable(false);
		contentPanel.add(txtFechaNac);

		{
			okButton = new JButton("Registrar");
			Estilos.estilarBoton(okButton, new Color(99, 163, 97), Color.WHITE);
			okButton.setBounds(234, 430, 110, 35);
			contentPanel.add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registrarCliente();
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancelar");
			Estilos.estilarBoton(cancelButton, new Color(191, 26, 26), Color.WHITE);
			cancelButton.setBounds(697, 430, 110, 35);
			contentPanel.add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
		}

		JButton btnLimpiar = new JButton("Limpiar");
		Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE);
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpiarCampos();
			}
		});
		btnLimpiar.setBounds(383, 430, 110, 35);
		contentPanel.add(btnLimpiar);

		JButton btnListado = new JButton("Listado");
		Estilos.estilarBoton(btnListado, new Color(110, 140, 251), Color.WHITE);
		btnListado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpiarCampos();
				ConsultarClientes frame = new ConsultarClientes();
				frame.setLocationRelativeTo(contentPanel);
				frame.setModal(true);
				frame.setVisible(true);
			}
		});
		btnListado.setBounds(541, 430, 110, 35);
		contentPanel.add(btnListado);

		JLabel lblRegistrarPaciente = new JLabel("Registrar Paciente");
		lblRegistrarPaciente.setForeground(Color.WHITE);
		lblRegistrarPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
		lblRegistrarPaciente.setBounds(10, 52, 517, 49);
		contentPanel.add(lblRegistrarPaciente);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 127, 646, 2);
		contentPanel.add(separator);

		JLabel lblNewlogo = new JLabel("");
		try {
			lblNewlogo.setIcon(new ImageIcon(
					RegClientes.class.getResource("/img/custom_resized_ffde04b9-ae4a-43dd-8c6e-2c67d4183e19.png")));
		} catch (Exception e) {
		}
		lblNewlogo.setForeground(Color.WHITE);
		lblNewlogo.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
		lblNewlogo.setBounds(663, 0, 366, 131);
		contentPanel.add(lblNewlogo);
	}

	private void limpiarCampos() {
		txtCedula.setText("");
		txtNombre.setText("");
		txtApellido.setText("");
		txtTelefono.setText("");
		txtDireccion.setText("");
		txtFechaNac.setDate(null);

		if (cbxGenero != null) {
			cbxGenero.setSelectedIndex(0);
		}

		clienteActual = null;
		txtCedula.setEditable(true);
		okButton.setText("Registrar");
		txtCedula.requestFocus();
	}

	private void registrarCliente() {

		if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() || txtTelefono.getText().isEmpty()
				|| cbxGenero.getSelectedIndex() == 0 || txtFechaNac.getDate() == null) {

			JOptionPane.showMessageDialog(null,
					"Por favor llene los campos obligatorios, incluyendo Fecha de Nacimiento y Género.");
			return;
		}

		LocalDate fechaNacimiento = txtFechaNac.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (clienteActual == null) {
			// VALIDAR DUPLICADO
			Object respuesta = ClienteSocket.enviar("BUSCAR_CLIENTE_CEDULA", txtCedula.getText());
			if (respuesta != null) {
				JOptionPane.showMessageDialog(null, "Ya existe un cliente con esa cédula.");
				return;
			}

			Cliente nuevoCliente = new Cliente(txtCedula.getText(), txtNombre.getText(), txtApellido.getText(),
					txtTelefono.getText(), fechaNacimiento, txtDireccion.getText(), true, null, null, false,
					new java.util.ArrayList<>(), cbxGenero.getSelectedItem().toString());

			boolean exito = (boolean) ClienteSocket.enviar("REG_CLIENTE", nuevoCliente);

			if (exito) {
				JOptionPane.showMessageDialog(null, "Paciente registrado con éxito.");
				limpiarCampos();
			} else {
				JOptionPane.showMessageDialog(null, "Error al registrar en el servidor.");
			}

		} else {
			// MODO EDICIÓN
			clienteActual.setNombre(txtNombre.getText());
			clienteActual.setApellido(txtApellido.getText());
			clienteActual.setTelefono(txtTelefono.getText());
			clienteActual.setDireccion(txtDireccion.getText());
			clienteActual.setFechaNacimiento(fechaNacimiento);
			clienteActual.setGenero(cbxGenero.getSelectedItem().toString());

			boolean exito = (boolean) ClienteSocket.enviar("UPDATE_CLIENTE", clienteActual);

			if (exito) {
				JOptionPane.showMessageDialog(null, "Datos actualizados.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al actualizar.");
			}
		}
	}
}