package Visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.User;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;;

public class Principal extends JFrame {

	private JPanel contentPane;
	private Dimension dim;
	private static User usuarioActual;
	private JMenuBar menuBar;
	private JMenu menuCitas;
	private JMenu menuPacientes;
	private JMenu menuConsulta;
	private JMenu menuAdministracion;
	private JLabel labelUsuario;

	/**
	 * Launch the application.
	 */
	public Principal(User usuarioLogueado) {
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
		menuBar.add(menuCitas);
		JMenuItem itemCrearCita = new JMenuItem("Crear/Modificar Cita");
		menuCitas.add(itemCrearCita);

		/* MENU DE PACIENTES
		 * AGREGAR LAS COSAS DE PACIENTES AQUI
		 */
		menuPacientes = new JMenu("Gestión Pacientes");
		menuBar.add(menuPacientes);
		JMenuItem itemRegPaciente = new JMenuItem("Registrar Paciente");
		menuPacientes.add(itemRegPaciente);

		/* MENU DE CONSULTAS
		 * AGREGAR LAS COSAS DE CONSULTAS AQUI
		 */
		menuConsulta = new JMenu("Consultas");
		menuBar.add(menuConsulta);
		JMenuItem itemVerMisCitas = new JMenuItem("Ver Citas de Hoy");
		menuConsulta.add(itemVerMisCitas);

		/* MENU DE ADMINISTRACION
		 * AGREGAR LAS COSAS DE ADMINISTRACION AQUI
		 */
		menuAdministracion = new JMenu("Administración");
		menuBar.add(menuAdministracion);
		JMenuItem itemGestionarUser = new JMenuItem("Gestionar Usuarios");
		menuAdministracion.add(itemGestionarUser);
		JMenuItem itemGestionarMedicos = new JMenuItem("Gestionar Médicos");
		menuAdministracion.add(itemGestionarMedicos);


		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		labelUsuario = new JLabel("Cargando...");
		contentPane.add(labelUsuario, BorderLayout.SOUTH); 
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

}


