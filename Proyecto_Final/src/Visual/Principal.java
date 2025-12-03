package Visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import logico.Cita;
import logico.Cliente;
import logico.Consulta;
import logico.Enfermedad;
import logico.User;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.FlowLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import Servidor.Server;

public class Principal extends JFrame {

	private JPanel contentPane;
	private Dimension dim;
	private User usuarioActual;
	private JMenuBar menuBar;
	private JMenu menuCitas;
	private JMenu menuPacientes;
	private JMenu menuConsulta;
	private JMenu menuAdministracion;
	private Panel panel_1;
	private JLabel lblUsuario;
	private JLabel lblReloj;

	private JPanel panelGrafico;
	private ChartPanel chartPanel;

	/**
	 * Launch the application.
	 */
	public Principal(User usuarioLogueado) {
		LocalDate hoy = LocalDate.now();
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", new Locale("es", "ES"));
		String fechaTexto = hoy.format(formateador);

		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/img/seguro-de-salud.png")));
		} catch (Exception e) { }

		this.usuarioActual = usuarioLogueado;

		iniciarTodo();
		configurarAccesosPorRol();

		panel_1 = new Panel();
		panel_1.setBackground(new Color(60, 70, 123));
		panel_1.setLayout(new BorderLayout());
		contentPane.add(panel_1, BorderLayout.SOUTH);

		lblReloj = new JLabel("00:00:00");
		lblReloj.setForeground(Color.WHITE);
		lblReloj.setFont(new Font("Bahnschrift", Font.BOLD, 36));
		panel_1.add(lblReloj, BorderLayout.EAST);
		fechaTexto = fechaTexto.substring(0, 1).toUpperCase() + fechaTexto.substring(1);
		JLabel lblBienvenido = new JLabel("¡Bienvenido! Hoy es " + fechaTexto);
		lblBienvenido.setForeground(Color.WHITE);
		lblBienvenido.setFont(new Font("Bahnschrift", Font.BOLD, 36));
		panel_1.add(lblBienvenido, BorderLayout.WEST);

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
		try { menuCitas.setIcon(new ImageIcon(Principal.class.getResource("/img/cita.png"))); } catch (Exception e) {}
		menuCitas.setFont(new Font("Bahnschrift", Font.BOLD, 25));
		menuBar.add(menuCitas);

		JMenuItem itemCrearCita = new JMenuItem("Crear/Modificar Cita");
		itemCrearCita.setFont(new Font("Tahoma", Font.PLAIN, 20));
		itemCrearCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirDialogoDeCitas();
			}
		});
		menuCitas.add(itemCrearCita);

		/* MENU DE PACIENTES */
		menuPacientes = new JMenu("  Gesti\u00F3n de Pacientes\r\n");
		menuPacientes.setForeground(Color.WHITE);
		try { menuPacientes.setIcon(new ImageIcon(Principal.class.getResource("/img/gestion-de-clientes.png"))); } catch (Exception e) {}
		menuPacientes.setFont(new Font("Bahnschrift", Font.BOLD, 25));
		menuBar.add(menuPacientes);

		JMenuItem itemRegPaciente = new JMenuItem("Registrar Paciente");
		itemRegPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		itemRegPaciente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegClientes reg = new RegClientes();
				reg.setModal(true);
				reg.setVisible(true);
			}
		});
		menuPacientes.add(itemRegPaciente);

		JMenuItem itemListarPacientes = new JMenuItem("Listado de Pacientes");
		itemListarPacientes.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
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
		try { menuConsulta.setIcon(new ImageIcon(Principal.class.getResource("/img/dato-de-registro.png"))); } catch (Exception e) {}
		menuConsulta.setFont(new Font("Bahnschrift", Font.BOLD, 25));
		menuBar.add(menuConsulta);

		JMenuItem itemVerMisCitas = new JMenuItem("Ver Citas de Hoy");
		itemVerMisCitas.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
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
		try { menuAdministracion.setIcon(new ImageIcon(Principal.class.getResource("/img/doctor.png"))); } catch (Exception e) {}
		menuAdministracion.setFont(new Font("Bahnschrift", Font.BOLD, 25));
		menuBar.add(menuAdministracion);

		// USUARIOS
		JMenuItem itemGestionarUser = new JMenuItem("Gestionar Usuarios");
		itemGestionarUser.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		itemGestionarUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegUser reg = new RegUser();
				reg.setModal(true); 
				reg.setVisible(true);
			}
		});		
		menuAdministracion.add(itemGestionarUser);

		// REPORTES GENERALES
		JMenuItem itemReportes = new JMenuItem("Reportes Generales");
		itemReportes.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		itemReportes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportesGenerales rep = new ReportesGenerales();
				rep.setVisible(true);
			}
		});
		menuAdministracion.add(itemReportes);

		// MEDICOS
		JMenu menuGestionMedicos = new JMenu("Gestionar Médicos");
		menuGestionMedicos.setFont(new Font("Bahnschrift", Font.PLAIN, 20));

		JMenuItem itemRegMedico = new JMenuItem("Registrar Médico");
		itemRegMedico.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		itemRegMedico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegMedico regMedico = new RegMedico();
				regMedico.setModal(true);
				regMedico.setVisible(true);
			}
		});
		menuGestionMedicos.add(itemRegMedico);

		JMenuItem itemListarMedicos = new JMenuItem("Listar Médicos");
		itemListarMedicos.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		itemListarMedicos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarMedicos consulta = new ConsultarMedicos();
				consulta.setModal(true);
				consulta.setVisible(true);
			}
		});
		menuGestionMedicos.add(itemListarMedicos);
		menuAdministracion.add(menuGestionMedicos);

		// ESPECIALIDADES
		JMenu menuGestionEspecialidades = new JMenu("Gestionar Especialidades");
		menuGestionEspecialidades.setFont(new Font("Bahnschrift", Font.PLAIN, 20));

		JMenuItem itemRegEspecialidad = new JMenuItem("Registrar Especialidad");
		itemRegEspecialidad.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		itemRegEspecialidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegEspecialidad regEsp = new RegEspecialidad();
				regEsp.setModal(true);
				regEsp.setVisible(true);
			}
		});
		menuGestionEspecialidades.add(itemRegEspecialidad);

		JMenuItem itemListarEspecialidades = new JMenuItem("Consultar Especialidades");
		itemListarEspecialidades.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		itemListarEspecialidades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarEspecialidades consulta = new ConsultarEspecialidades();
				consulta.setModal(true);
				consulta.setVisible(true);
			}
		});
		menuGestionEspecialidades.add(itemListarEspecialidades);
		menuAdministracion.add(menuGestionEspecialidades);

		// VACUNAS
		JMenuItem itemGestionarVacunas = new JMenuItem("Gestionar Vacunas");
		itemGestionarVacunas.setFont(new Font("Bahnschrift", Font.PLAIN, 20)); 
		itemGestionarVacunas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarVacunas consulta = new ConsultarVacunas();
				consulta.setModal(true);
				consulta.setVisible(true);
			}
		});
		menuAdministracion.add(itemGestionarVacunas);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelDashboardCentral = new JPanel();
		panelDashboardCentral.setBackground(Color.WHITE);
		panelDashboardCentral.setLayout(new GridLayout(1, 1, 20, 0));

		panelDashboardCentral.add(crearPanelEstadistico());      

		contentPane.add(panelDashboardCentral, BorderLayout.CENTER);

		menuBar.add(javax.swing.Box.createHorizontalGlue());
		lblUsuario = new JLabel("Usuario: " + usuarioActual.getUsuario() + " (" + usuarioActual.getRol() + ")  "); 
		lblUsuario.setForeground(Color.WHITE);
		lblUsuario.setFont(new Font("Bahnschrift", Font.BOLD, 25)); 
		try { lblUsuario.setIcon(new ImageIcon(Principal.class.getResource("/img/perfil(2).png"))); } catch (Exception e) {}

		menuBar.add(lblUsuario);
	}

	private void configurarAccesosPorRol() {
		if (menuCitas == null || menuConsulta == null || menuAdministracion == null || menuPacientes == null) {
			return;
		}
		String rol = this.usuarioActual.getRol();
		menuCitas.setVisible(false);
		menuConsulta.setVisible(false);
		menuAdministracion.setVisible(false);
		menuPacientes.setVisible(false);

		switch (rol) {
		case "Administrador":
			menuCitas.setVisible(true);
			menuConsulta.setVisible(true);
			menuAdministracion.setVisible(true);
			menuPacientes.setVisible(true);
			break;
		case "Asistente":
			menuCitas.setVisible(true);
			break;
		case "Medico":
			menuPacientes.setVisible(true);
			menuConsulta.setVisible(true);
			break;
		default:
			JOptionPane.showMessageDialog(this, "Rol desconocido. Saliendo del sistema.");
			System.exit(0);
			break;
		}
	}

	private void abrirDialogoDeCitas() {
		JDialog dialogCitas = new JDialog(Principal.this, "Gestión de Citas", true);
		GestionCitas panel = new GestionCitas();
		dialogCitas.getContentPane().add(panel);
		dialogCitas.setSize(1130, 750);
		dialogCitas.setResizable(false);
		dialogCitas.setLocationRelativeTo(Principal.this);
		try { dialogCitas.setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/img/seguro-de-salud.png"))); } catch (Exception e) {}
		dialogCitas.setVisible(true);
	}

	private void iniciarReloj() {
		javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
			String hora = java.time.LocalDateTime.now()
					.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm:ss a"));
			if (lblReloj != null) lblReloj.setText(hora);
		});
		timer.setRepeats(true);
		timer.start();
	}

	private JPanel crearPanelEstadistico() {
		JPanel panelDashboard = new JPanel(new BorderLayout());
		panelDashboard.setBackground(Color.WHITE);
		panelDashboard.setBorder(new TitledBorder("Estadísticas en Tiempo Real"));

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBotones.setBackground(Color.WHITE);

		JButton btnCitas = new JButton("Estado de Citas");
		Estilos.estilarBoton(btnCitas, new Color(60, 70, 123), Color.WHITE);
		btnCitas.addActionListener(e -> actualizarGrafico("CITAS"));

		JButton btnEnfermedades = new JButton("Enfermedades Comunes");
		Estilos.estilarBoton(btnEnfermedades, new Color(231, 76, 60), Color.WHITE);
		btnEnfermedades.addActionListener(e -> actualizarGrafico("ENFERMEDADES"));

		panelBotones.add(btnCitas);
		panelBotones.add(btnEnfermedades);

		panelDashboard.add(panelBotones, BorderLayout.NORTH);

		panelGrafico = new JPanel(new BorderLayout());
		panelGrafico.setBackground(Color.WHITE);
		panelDashboard.add(panelGrafico, BorderLayout.CENTER);

		actualizarGrafico("CITAS");

		return panelDashboard;
	}

	@SuppressWarnings("unchecked")
	private void actualizarGrafico(String tipo) {
		panelGrafico.removeAll();
		JFreeChart chart = null;

		if (tipo.equals("CITAS")) {
			ArrayList<Cita> citas = (ArrayList<Cita>) ClienteSocket.enviar("LISTAR_CITAS", null);
			DefaultPieDataset dataset = new DefaultPieDataset();
			int pendientes = 0;
			int completadas = 0;

			if (citas != null) {
				for (Cita c : citas) {

					if (c.getEstado() != null) {
						if (c.getEstado().equalsIgnoreCase("Pendiente")) pendientes++;
						if (c.getEstado().equalsIgnoreCase("Completada")) completadas++;
					}
				}
			}
			dataset.setValue("Pendientes", pendientes);
			dataset.setValue("Completadas", completadas);

			chart = ChartFactory.createPieChart("Estado de Citas (Total)", dataset, true, true, false);

			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setBackgroundPaint(Color.WHITE);

			plot.setSectionPaint("Pendientes", new Color(231, 76, 60));
			plot.setSectionPaint("Completadas", new Color(46, 204, 113));

		} else if (tipo.equals("ENFERMEDADES")) {
			ArrayList<Cliente> clientes = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_CLIENTES", null);
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();

			java.util.HashMap<String, Integer> conteo = new java.util.HashMap<>();

			if (clientes != null) {
				for (Cliente cli : clientes) {

					if (cli.getHistorial() != null) {

						if (cli.getHistorial().getConsultas() != null) {

							for (Consulta con : cli.getHistorial().getConsultas()) {
								if (con.getEnfermedadesDiag() != null) {

									for (Enfermedad enf : con.getEnfermedadesDiag()) {
										String nombre = enf.getNombre();
										conteo.put(nombre, conteo.getOrDefault(nombre, 0) + 1);
									}
								}
							}
						}
					}
				
				}
			}

			for (String key : conteo.keySet()) {
				dataset.addValue(conteo.get(key), "Casos", key);
			}

			chart = ChartFactory.createBarChart("Enfermedades Diagnosticadas", "Enfermedad", "Casos", dataset, PlotOrientation.VERTICAL, false, true, false);
			CategoryPlot plot = chart.getCategoryPlot();
			plot.setBackgroundPaint(Color.WHITE);
			BarRenderer renderer = (BarRenderer) plot.getRenderer();
			renderer.setSeriesPaint(0, new Color(60, 70, 123));
		}

		if (chart != null) {
			chart.setBackgroundPaint(Color.WHITE);
			chartPanel = new ChartPanel(chart);
			panelGrafico.add(chartPanel, BorderLayout.CENTER);
		}

		panelGrafico.revalidate();
		panelGrafico.repaint();
	}
}

