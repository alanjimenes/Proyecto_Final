package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Toolkit;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegMedico.class.getResource("/img/doctor.png")));
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
		setBounds(100, 100, 769, 433);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new  Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setForeground(Color.WHITE);
		lblCedula.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblCedula.setBounds(10, 30, 80, 14);
		contentPanel.add(lblCedula);

		txtCedula = new JTextField();
		txtCedula.setBounds(116, 30, 220, 20);
		contentPanel.add(txtCedula);
		txtCedula.setColumns(10);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblNombre.setBounds(10, 70, 80, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(116, 70, 220, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setForeground(Color.WHITE);
		lblApellido.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblApellido.setBounds(10, 110, 80, 14);
		contentPanel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setBounds(116, 108, 220, 20);
		contentPanel.add(txtApellido);
		txtApellido.setColumns(10);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setForeground(Color.WHITE);
		lblTelefono.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblTelefono.setBounds(10, 195, 80, 14);
		contentPanel.add(lblTelefono);

		txtTelefono = new JTextField();
		txtTelefono.setBounds(116, 190, 220, 20);
		contentPanel.add(txtTelefono);
		txtTelefono.setColumns(10);

		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setForeground(Color.WHITE);
		lblDireccion.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblDireccion.setBounds(10, 150, 80, 14);
		contentPanel.add(lblDireccion);

		txtDireccion = new JTextField();
		txtDireccion.setBounds(116, 148, 220, 20);
		contentPanel.add(txtDireccion);
		txtDireccion.setColumns(10);

		JLabel lblFecha = new JLabel("Fecha Nac:");
		lblFecha.setForeground(Color.WHITE);
		lblFecha.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblFecha.setBounds(10, 235, 80, 14);
		contentPanel.add(lblFecha);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(116, 230, 220, 20);
		contentPanel.add(dateChooser);

		JLabel lblEspecialidad = new JLabel("Especialidad:");
		lblEspecialidad.setForeground(Color.WHITE);
		lblEspecialidad.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblEspecialidad.setBounds(10, 275, 100, 14);
		contentPanel.add(lblEspecialidad);

		cbxEspecialidad = new JComboBox<>();
		cbxEspecialidad.setBounds(116, 270, 220, 20);
		contentPanel.add(cbxEspecialidad);

		// BOTON EXTRA PARA ESPECIALIDAD
		JButton btnAddEsp = new JButton("+");


		Estilos.estilarBoton(btnAddEsp, new Color(127, 140, 141), Color.WHITE); 


		btnAddEsp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegEspecialidad regEsp = new RegEspecialidad();
				regEsp.setModal(true);
				regEsp.setVisible(true);
				cargarEspecialidades();
			}
		});
		btnAddEsp.setBounds(346, 270, 45, 23);
		contentPanel.add(btnAddEsp);

		JLabel lblMaxCitas = new JLabel("Citas Diarias:");
		lblMaxCitas.setForeground(Color.WHITE);
		lblMaxCitas.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblMaxCitas.setBounds(10, 315, 100, 14);
		contentPanel.add(lblMaxCitas);

		spnMaxCitas = new JSpinner();
		spnMaxCitas.setModel(new SpinnerNumberModel(10, 1, 100, 1));
		spnMaxCitas.setBounds(120, 310, 60, 20);
		contentPanel.add(spnMaxCitas);
		{
			   okButton = new JButton("Registrar");
	            
	            Estilos.estilarBoton(okButton, new Color(46, 204, 113), Color.WHITE);
	     
	            okButton.setBounds(361, 340, 110, 35); 
	            contentPanel.add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gestionMedico();
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		{
            JButton cancelButton = new JButton("Cancelar");
            
            Estilos.estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);
            
            cancelButton.setBounds(631, 340, 110, 35);
            contentPanel.add(cancelButton);

			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
		}
		JButton btnLimpiar = new JButton("Limpiar");

		btnLimpiar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		        txtCedula.setText("");
		        txtNombre.setText("");
		        txtApellido.setText("");
		        txtTelefono.setText("");
		        txtDireccion.setText("");
		        dateChooser.setDate(null);
		    }
		});

		Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE); 

		btnLimpiar.setBounds(492, 340, 110, 35);
		contentPanel.add(btnLimpiar);
		
		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setIcon(new ImageIcon(RegMedico.class.getResource("/img/receta.png")));
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Bahnschrift", Font.BOLD, 22));
		label.setBounds(472, 49, 226, 273);
		contentPanel.add(label);
		
		JLabel lblBienvenida = new JLabel("Registrar Medico");
		lblBienvenida.setForeground(Color.WHITE);
		lblBienvenida.setFont(new Font("Bahnschrift", Font.BOLD, 22));
		lblBienvenida.setBounds(462, -16, 281, 100);
		contentPanel.add(lblBienvenida);

		cargarEspecialidades();
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