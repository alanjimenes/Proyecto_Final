package Visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import logico.User;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.Font;;

public class Principal extends JFrame {

	private JPanel contentPane;
	private Dimension dim;
	private User usuarioActual;
	private JMenuBar menuBar;
	private JMenu menuCitas;
	private JMenu menuPacientes;
	private JMenu menuConsulta;
	private JMenu menuAdministracion;
	private JLabel labelUsuario;
	private JLabel lblFondoIcon;

	/**
	 * Launch the application.
	 */
	public Principal(User usuarioLogueado) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("src/icons/icon.png")));
		this.usuarioActual = usuarioLogueado;
		iniciarTodo(); 
		configurarAccesosPorRol(); 
		labelUsuario.setText("Usuario: " + usuarioActual.getUsuario() + " (Rol: " + usuarioActual.getRol() + ")");
	}

	/**
	 * Create the frame.
	 */
	private void iniciarTodo() {
		setTitle("Hospital");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim.width, dim.height);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		/* MENU DE CITAS
		 * AGREGAR LAS COSAS DE CITAS AQUI
		 */
		menuCitas = new JMenu("Gestión Citas");
		menuCitas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuBar.add(menuCitas);
		JMenuItem itemCrearCita = new JMenuItem("Crear/Modificar Cita");
		itemCrearCita.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemCrearCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionarCitas(); 
			}
		});
		menuCitas.add(itemCrearCita);

		/* MENU DE PACIENTES
		 * AGREGAR LAS COSAS DE PACIENTES AQUI
		 */
		menuPacientes = new JMenu("Gestión Pacientes");
		menuPacientes.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuBar.add(menuPacientes);
		JMenuItem itemRegPaciente = new JMenuItem("Registrar Paciente");
		itemRegPaciente.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuPacientes.add(itemRegPaciente);

		/* MENU DE CONSULTAS
		 * AGREGAR LAS COSAS DE CONSULTAS AQUI
		 */
		menuConsulta = new JMenu("Consultas");
		menuConsulta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuBar.add(menuConsulta);
		JMenuItem itemVerMisCitas = new JMenuItem("Ver Citas de Hoy");
		itemVerMisCitas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuConsulta.add(itemVerMisCitas);

		/* MENU DE ADMINISTRACION
		 * AGREGAR LAS COSAS DE ADMINISTRACION AQUI
		 */
		menuAdministracion = new JMenu("Administración");
		menuAdministracion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuBar.add(menuAdministracion);
		JMenuItem itemGestionarUser = new JMenuItem("Gestionar Usuarios");
		itemGestionarUser.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuAdministracion.add(itemGestionarUser);
		JMenuItem itemGestionarMedicos = new JMenuItem("Gestionar Médicos");
		itemGestionarMedicos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuAdministracion.add(itemGestionarMedicos);


		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		labelUsuario = new JLabel("Cargando...");
		labelUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(labelUsuario, BorderLayout.SOUTH);

		lblFondoIcon = new JLabel("");
		lblFondoIcon.setIcon(new ImageIcon(Principal.class.getResource("src/icons/logo.png")));
		lblFondoIcon.setHorizontalAlignment(SwingConstants.CENTER); 
		contentPane.add(lblFondoIcon, BorderLayout.CENTER);

	}

	private void configurarAccesosPorRol() {

		if (menuCitas == null || menuConsulta == null || menuAdministracion == null || menuPacientes == null) {
			System.err.println("¡Error! Los menús no han sido inicializados.");
			return;
		}
		String rol = this.usuarioActual.getRol();
		menuCitas.setEnabled(false);
		menuConsulta.setEnabled(false);
		menuAdministracion.setEnabled(false);
		menuPacientes.setEnabled(false);

		switch (rol) {
		case "Administrador":
			menuCitas.setEnabled(true);
			menuConsulta.setEnabled(true);
			menuAdministracion.setEnabled(true);
			menuPacientes.setEnabled(true);
			break;

		case "Asistente": 
			menuCitas.setEnabled(true);
			menuPacientes.setEnabled(true);
			break;

		case "Medico":
			menuConsulta.setEnabled(true);
			break;

		default:
			JOptionPane.showMessageDialog(this, "Rol desconocido. Saliendo del sistema.");
			System.exit(0);
			break;
		}
	}

	private void GestionarCitas() {
		JDialog dialogCitas = new JDialog(Principal.this, "Gestión de Citas", true);
		GestionCitas panel = new GestionCitas();
		dialogCitas.getContentPane().add(panel);
		dialogCitas.setSize(900, 650);
		dialogCitas.setLocationRelativeTo(Principal.this);
		dialogCitas.setVisible(true);
	}
}
