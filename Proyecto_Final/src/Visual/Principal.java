package Visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

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
import java.awt.Font;
import java.awt.Panel;;

public class Principal extends JFrame {

	private JPanel contentPane;
	private Dimension dim;
	private User usuarioActual;
	private JMenuBar menuBar;
	private JMenu menuCitas;
	private JMenu menuPacientes;
	private JMenu menuConsulta;
	private JMenu menuAdministracion;
	private JLabel lblFondoIcon;
	private Panel panel_1;
	private JLabel lblUsuario;
	private JLabel lblReloj;

	/**
	 * Launch the application.
	 */
	public Principal(User usuarioLogueado) {

		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/img/seguro-de-salud.png")));
		this.usuarioActual = usuarioLogueado;

		iniciarTodo();
		configurarAccesosPorRol();

		panel_1 = new Panel();
		panel_1.setBackground(new Color(60, 70, 123));
		panel_1.setLayout(new BorderLayout());
		contentPane.add(panel_1, BorderLayout.SOUTH);

		lblUsuario = new JLabel("Usuario: " + usuarioActual.getUsuario() + " (Rol: " + usuarioActual.getRol() + ")");
		lblUsuario.setForeground(Color.WHITE);
		lblUsuario.setFont(new Font("Bahnschrift", Font.BOLD, 28));
		lblUsuario.setIcon(new ImageIcon(Principal.class.getResource("/img/hospital.png")));
		panel_1.add(lblUsuario, BorderLayout.WEST);

		lblReloj = new JLabel("00:00:00");
		lblReloj.setForeground(Color.WHITE);
		lblReloj.setFont(new Font("Bahnschrift", Font.BOLD, 28));
		panel_1.add(lblReloj, BorderLayout.EAST);

		iniciarReloj();
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
		menuBar.setForeground(Color.WHITE);
		menuBar.setBackground(new Color(60, 70, 123));
		setJMenuBar(menuBar);

		/* MENU DE CITAS */
		menuCitas = new JMenu("  Gesti\u00F3n Citas  ");
		menuCitas.setForeground(Color.WHITE);
		menuCitas.setIcon(new ImageIcon(Principal.class.getResource("/img/cita.png")));
		menuCitas.setFont(new Font("Bahnschrift", Font.BOLD, 20));
		menuBar.add(menuCitas);
		JMenuItem itemCrearCita = new JMenuItem("Crear/Modificar Cita");
		itemCrearCita.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemCrearCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionarCitas();
			}
		});
		menuCitas.add(itemCrearCita);

		/* MENU DE PACIENTES */
		menuPacientes = new JMenu("  Gesti\u00F3n de Clientes\r\n");
		menuPacientes.setForeground(Color.WHITE);
		menuPacientes.setIcon(new ImageIcon(Principal.class.getResource("/img/gestion-de-clientes.png")));
		menuPacientes.setFont(new Font("Bahnschrift", Font.BOLD, 20));
		menuBar.add(menuPacientes);
		JMenuItem itemRegPaciente = new JMenuItem("Registrar Cliente");
		itemRegPaciente.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JMenuItem itemRegCliente = new JMenuItem("Registrar Paciente");
		itemRegPaciente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegClientes reg = new RegClientes();
				reg.setModal(true);
				reg.setVisible(true);
			}
		});
		menuPacientes.add(itemRegPaciente);

		JMenuItem itemListarPacientes = new JMenuItem("Listado de Clientes");
		itemListarPacientes.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemListarPacientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarClientes consulta = new ConsultarClientes();
				consulta.setModal(true);
				consulta.setVisible(true);
			}
		});
		menuPacientes.add(itemListarPacientes);

		/* MENU DE CONSULTAS */
		menuConsulta = new JMenu("  Consultas  ");
		menuConsulta.setForeground(Color.WHITE);
		menuConsulta.setIcon(new ImageIcon(Principal.class.getResource("/img/dato-de-registro.png")));
		menuConsulta.setFont(new Font("Bahnschrift", Font.BOLD, 20));
		menuBar.add(menuConsulta);
		JMenuItem itemVerMisCitas = new JMenuItem("Ver Citas de Hoy");
		itemVerMisCitas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemVerMisCitas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MisCitas misCitas = new MisCitas(usuarioActual);
				misCitas.setModal(true);
				misCitas.setVisible(true);
			}
		});
		menuConsulta.add(itemVerMisCitas);

		/* MENU DE ADMINISTRACION */
		menuAdministracion = new JMenu("  Administraci\u00F3n");
		menuAdministracion.setForeground(Color.WHITE);
		menuAdministracion.setIcon(new ImageIcon(Principal.class.getResource("/img/doctor.png")));
		menuAdministracion.setFont(new Font("Bahnschrift", Font.BOLD, 20));
		menuBar.add(menuAdministracion);

		// USUARIOS
		JMenuItem itemGestionarUser = new JMenuItem("Gestionar Usuarios");
		itemGestionarUser.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemGestionarUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegUser reg = new RegUser();
				reg.setModal(true); 
				reg.setVisible(true);
			}
		});		
		menuAdministracion.add(itemGestionarUser);

		// MEDICOS
		JMenu menuGestionMedicos = new JMenu("Gestionar Médicos");
		menuGestionMedicos.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JMenuItem itemRegMedico = new JMenuItem("Registrar Médico");
		itemRegMedico.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemRegMedico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegMedico regMedico = new RegMedico();
				regMedico.setModal(true);
				regMedico.setVisible(true);
			}
		});
		menuGestionMedicos.add(itemRegMedico);

		JMenuItem itemListarMedicos = new JMenuItem("Listar Médicos");
		itemListarMedicos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemListarMedicos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarMedicos consulta = new ConsultarMedicos();
				consulta.setModal(true);
				consulta.setVisible(true);
			}
		});
		menuGestionMedicos.add(itemListarMedicos);

		menuAdministracion.add(menuGestionMedicos);

		JMenu menuGestionEspecialidades = new JMenu("Gestionar Especialidades");
		menuGestionEspecialidades.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JMenuItem itemRegEspecialidad = new JMenuItem("Registrar Especialidad");
		itemRegEspecialidad.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemRegEspecialidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegEspecialidad regEsp = new RegEspecialidad();
				regEsp.setModal(true);
				regEsp.setVisible(true);
			}
		});
		menuGestionEspecialidades.add(itemRegEspecialidad);

		JMenuItem itemListarEspecialidades = new JMenuItem("Consultar Especialidades");
		itemListarEspecialidades.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemListarEspecialidades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarEspecialidades consulta = new ConsultarEspecialidades();
				consulta.setModal(true);
				consulta.setVisible(true);
			}
		});
		menuGestionEspecialidades.add(itemListarEspecialidades);

		menuAdministracion.add(menuGestionEspecialidades);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		lblFondoIcon = new JLabel("");
		lblFondoIcon.setIcon(new ImageIcon(Principal.class.getResource("/img/logo.png")));
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

	private void iniciarReloj() {
		javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
			String hora = java.time.LocalDateTime.now()
					.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm:ss a"));

			javax.swing.SwingUtilities.invokeLater(() -> {
				lblReloj.setText(hora);
			});
		});

		timer.setRepeats(true);
		timer.start();
	}

}
