package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import logico.Cliente;
import logico.Clinica;
import logico.Historial;
import logico.RegistroVacunacion;

public class RegClientes extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JDateChooser dateChooser;
	private Cliente clienteActual = null;
	private JButton okButton;


	public RegClientes() {
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
		if (clienteEditar.getFechaNacimiento() != null) {
			Date date = Date.from(clienteEditar.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
			dateChooser.setDate(date);
		}
	}

	private void initComponents() {
		setTitle("Registrar Paciente");
		setBounds(100, 100, 520, 350);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setBounds(20, 30, 80, 14);
		contentPanel.add(lblCedula);

		txtCedula = new JTextField();
		txtCedula.setBounds(100, 27, 150, 20);
		contentPanel.add(txtCedula);
		txtCedula.setColumns(10);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(20, 70, 80, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(100, 67, 150, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setBounds(270, 70, 60, 14);
		contentPanel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setBounds(330, 67, 150, 20);
		contentPanel.add(txtApellido);
		txtApellido.setColumns(10);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setBounds(20, 110, 80, 14);
		contentPanel.add(lblTelefono);

		txtTelefono = new JTextField();
		txtTelefono.setBounds(100, 107, 150, 20);
		contentPanel.add(txtTelefono);
		txtTelefono.setColumns(10);

		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setBounds(270, 110, 80, 14);
		contentPanel.add(lblDireccion);

		txtDireccion = new JTextField();
		txtDireccion.setBounds(330, 107, 150, 20);
		contentPanel.add(txtDireccion);
		txtDireccion.setColumns(10);

		JLabel lblFecha = new JLabel("Fecha Nac:");
		lblFecha.setBounds(20, 150, 80, 14);
		contentPanel.add(lblFecha);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(100, 147, 150, 20);
		contentPanel.add(dateChooser);

		// BOTONES 
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			okButton = new JButton("Registrar");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registrarCliente();
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancelar");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
	}

	private void registrarCliente() {
		if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() || txtTelefono.getText().isEmpty() || dateChooser.getDate() == null) {
			JOptionPane.showMessageDialog(null, "Por favor llene los campos obligatorios (Cédula, Nombre, Teléfono, Fecha).");
			return;
		}

		Date date = dateChooser.getDate();
		LocalDate fechaNac = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (clienteActual == null) {
			if (Clinica.getInstancia().buscarIndiceClientePorCedula(txtCedula.getText()) != -1) {
				JOptionPane.showMessageDialog(null, "Ya existe un cliente con esa cédula.");
				return;
			}
			Historial nuevoHistorial = new Historial("HIST-" + txtCedula.getText());
			ArrayList<RegistroVacunacion> vacunasVacias = new ArrayList<>();

			Cliente nuevoCliente = new Cliente(
					txtCedula.getText(), 
					txtNombre.getText(), 
					txtApellido.getText(), 
					fechaNac, 
					txtTelefono.getText(), 
					txtDireccion.getText(),true,null,nuevoHistorial,false,vacunasVacias,true);

			Clinica.getInstancia().insertarCliente(nuevoCliente);
			JOptionPane.showMessageDialog(null, "Paciente registrado con éxito.");

		} else {
			clienteActual.setNombre(txtNombre.getText());
			clienteActual.setApellido(txtApellido.getText());
			clienteActual.setTelefono(txtTelefono.getText());
			clienteActual.setDireccion(txtDireccion.getText());
			clienteActual.setFechaNacimiento(fechaNac);

			JOptionPane.showMessageDialog(null, "Datos actualizados.");
		}
		Clinica.getInstancia().guardarDatosClinica();
		dispose();
	}
}