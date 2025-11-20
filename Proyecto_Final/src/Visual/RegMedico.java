package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import logico.Clinica;
import logico.Especialidad;
import logico.Medico;

public class RegMedico extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JComboBox<String> cbxEspecialidad;
	private JDateChooser dateChooser;
	private JSpinner spnMaxCitas;
	private JButton okButton;
	private Medico medicoActual = null;

	public RegMedico() {
		initComponents();
		this.medicoActual = null;
	}

	public RegMedico(Medico medicoEditar) {
		initComponents();
		this.medicoActual = medicoEditar;
		setTitle("Modificar Médico");
		okButton.setText("Actualizar");

		txtCedula.setText(medicoEditar.getCedula());
		txtCedula.setEditable(false);
		txtNombre.setText(medicoEditar.getNombre());
		txtApellido.setText(medicoEditar.getApellido());
		txtTelefono.setText(medicoEditar.getTelefono());
		txtDireccion.setText(medicoEditar.getDireccion());

		if (medicoEditar.getEspecialidad() != null) {
			cbxEspecialidad.setSelectedItem(medicoEditar.getEspecialidad().getNombre());
		}
		spnMaxCitas.setValue(medicoEditar.getMaxCitasPorDia());
		if (medicoEditar.getFechaNacimiento() != null) {
			Date fecha = Date.from(medicoEditar.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
			dateChooser.setDate(fecha);
		}
	}

	private void initComponents() {
		setTitle("Registrar Médico");
		setBounds(100, 100, 550, 450);
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
		lblApellido.setBounds(270, 70, 80, 14);
		contentPanel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setBounds(340, 67, 150, 20);
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
		txtDireccion.setBounds(340, 107, 150, 20);
		contentPanel.add(txtDireccion);
		txtDireccion.setColumns(10);

		JLabel lblFecha = new JLabel("Fecha Nac:");
		lblFecha.setBounds(20, 150, 80, 14);
		contentPanel.add(lblFecha);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(100, 147, 150, 20);
		contentPanel.add(dateChooser);

		JLabel lblEspecialidad = new JLabel("Especialidad:");
		lblEspecialidad.setBounds(20, 210, 100, 14);
		contentPanel.add(lblEspecialidad);

		cbxEspecialidad = new JComboBox<>();
		cbxEspecialidad.setBounds(120, 207, 200, 20);
		contentPanel.add(cbxEspecialidad);

		// BOTON EXTRA PARA ESPECIALIDAD
		JButton btnAddEsp = new JButton("+");
		btnAddEsp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegEspecialidad regEsp = new RegEspecialidad();
				regEsp.setModal(true);
				regEsp.setVisible(true);
				cargarEspecialidades();
			}
		});
		btnAddEsp.setBounds(330, 206, 45, 23);
		contentPanel.add(btnAddEsp);

		JLabel lblMaxCitas = new JLabel("Citas Diarias:");
		lblMaxCitas.setBounds(20, 250, 100, 14);
		contentPanel.add(lblMaxCitas);

		spnMaxCitas = new JSpinner();
		spnMaxCitas.setModel(new SpinnerNumberModel(10, 1, 100, 1));
		spnMaxCitas.setBounds(120, 247, 60, 20);
		contentPanel.add(spnMaxCitas);

		cargarEspecialidades();

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Registrar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gestionMedico();
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
	}

	private void cargarEspecialidades() {
		cbxEspecialidad.removeAllItems();
		cbxEspecialidad.addItem("<Seleccione>");
		for (Especialidad esp : Clinica.getInstancia().getEspecialidades()) {
			cbxEspecialidad.addItem(esp.getNombre());
		}
	}

	private void gestionMedico() {
		if (txtNombre.getText().isEmpty() || dateChooser.getDate() == null || cbxEspecialidad.getSelectedIndex() <= 0) {
			JOptionPane.showMessageDialog(null, "Por favor llene los campos obligatorios.");
			return;
		}
		String nombreEsp = (String) cbxEspecialidad.getSelectedItem();
		Especialidad espSeleccionada = Clinica.getInstancia().buscarEspecialidadPorNombre(nombreEsp);
		Date date = dateChooser.getDate();
		LocalDate fechaNac = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (medicoActual == null) {
			if (txtCedula.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "La cédula es obligatoria.");
				return;
			}
			if (Clinica.getInstancia().buscarMedicoCedula(txtCedula.getText()) != null) {
				JOptionPane.showMessageDialog(null, "Ya existe un médico con esa cédula.");
				return;
			}

			Medico nuevoMedico = new Medico(txtCedula.getText(), txtNombre.getText(), txtApellido.getText(), fechaNac,
					txtTelefono.getText(), txtDireccion.getText(), true, espSeleccionada, (int) spnMaxCitas.getValue(),
					null, null);

			Clinica.getInstancia().agregarMedico(nuevoMedico);
			JOptionPane.showMessageDialog(null, "Médico registrado correctamente.");

		} else {
			medicoActual.setNombre(txtNombre.getText());
			medicoActual.setApellido(txtApellido.getText());
			medicoActual.setTelefono(txtTelefono.getText());
			medicoActual.setDireccion(txtDireccion.getText());
			medicoActual.setFechaNacimiento(fechaNac);
			medicoActual.setEspecialidad(espSeleccionada);
			medicoActual.setMaxCitasPorDia((int) spnMaxCitas.getValue());

			JOptionPane.showMessageDialog(null, "Médico actualizado correctamente.");
		}
		Clinica.getInstancia().guardarDatosClinica();
		dispose();
	}
}