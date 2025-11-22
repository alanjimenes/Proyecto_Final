package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegClientes.class.getResource("/img/seguro-de-salud.png")));
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
		setTitle("Registrar Cliente");
		setBounds(100, 100, 763, 431);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setForeground(Color.WHITE);
		lblCedula.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblCedula.setBounds(10, 29, 81, 14);
		contentPanel.add(lblCedula);

		txtCedula = new JTextField();
		txtCedula.setBounds(112, 27, 200, 20);
		contentPanel.add(txtCedula);
		txtCedula.setColumns(10);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblNombre.setBounds(10, 83, 81, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(112, 81, 200, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setForeground(Color.WHITE);
		lblApellido.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblApellido.setBounds(10, 134, 104, 14);
		contentPanel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setBounds(112, 132, 200, 20);
		contentPanel.add(txtApellido);
		txtApellido.setColumns(10);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setForeground(Color.WHITE);
		lblTelefono.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblTelefono.setBounds(10, 190, 81, 14);
		contentPanel.add(lblTelefono);

		txtTelefono = new JTextField();
		txtTelefono.setBounds(112, 188, 200, 20);
		contentPanel.add(txtTelefono);
		txtTelefono.setColumns(10);

		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setForeground(Color.WHITE);
		lblDireccion.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblDireccion.setBounds(10, 253, 93, 14);
		contentPanel.add(lblDireccion);

		txtDireccion = new JTextField();
		txtDireccion.setBounds(112, 251, 200, 20);
		contentPanel.add(txtDireccion);
		txtDireccion.setColumns(10);

		JLabel lblFecha = new JLabel("Fecha Nac:");
		lblFecha.setForeground(Color.WHITE);
		lblFecha.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblFecha.setBounds(10, 322, 98, 14);
		contentPanel.add(lblFecha);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(112, 322, 200, 20);
		contentPanel.add(dateChooser);
		{
            okButton = new JButton("Registrar");
            
            estilarBoton(okButton, new Color(46, 204, 113), Color.WHITE);
            
            okButton.setBounds(346, 353, 110, 35); 
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
           
            estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);
            
            cancelButton.setBounds(631, 353, 110, 35);
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

		estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE); 

		btnLimpiar.setBounds(492, 354, 110, 35);
		contentPanel.add(btnLimpiar);

		JLabel lblBienvenido = new JLabel("Registrar Cliente");
		lblBienvenido.setForeground(Color.WHITE);
		lblBienvenido.setFont(new Font("Bahnschrift", Font.BOLD, 22));
		lblBienvenido.setBounds(444, -16, 281, 100);
		contentPanel.add(lblBienvenido);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(RegClientes.class.getResource("/img/familia.png")));
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Bahnschrift", Font.BOLD, 22));
		label.setBounds(419, 62, 256, 273);
		contentPanel.add(label);
	}

	private void registrarCliente() {
		if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() || txtTelefono.getText().isEmpty()
				|| dateChooser.getDate() == null) {
			JOptionPane.showMessageDialog(null,
					"Por favor llene los campos obligatorios (Cédula, Nombre, Teléfono, Fecha).");
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

			Cliente nuevoCliente = new Cliente(txtCedula.getText(), txtNombre.getText(), txtApellido.getText(),
					fechaNac, txtTelefono.getText(), txtDireccion.getText(), true, null, nuevoHistorial, false,
					vacunasVacias, true);

			Clinica.getInstancia().insertarCliente(nuevoCliente);
			JOptionPane.showMessageDialog(null, "Paciente registrado con éxito.");
			txtCedula.setText("");
			txtNombre.setText("");
			txtApellido.setText("");
			txtTelefono.setText("");
			txtDireccion.setText("");
			dateChooser.setDate(null);

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

	private void estilarBoton(JButton boton, Color colorFondo, Color colorTexto) {
	    boton.setBackground(colorFondo);
	    boton.setForeground(colorTexto);
	    boton.setFont(new Font("Bahnschrift", Font.BOLD, 14)); 
	    boton.setFocusPainted(false); 
	    boton.setBorderPainted(false); 
	    boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 

	    boton.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	       
	            boton.setBackground(colorFondo.darker());
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	      
	            boton.setBackground(colorFondo);
	        }
	    });
	}
}