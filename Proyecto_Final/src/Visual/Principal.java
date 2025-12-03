package Visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logico.Clinica;
import logico.User;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;




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
		LocalDate hoy = LocalDate.now();
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", new Locale("es", "ES"));
		String fechaTexto = hoy.format(formateador);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/img/seguro-de-salud.png")));
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
		menuCitas.setIcon(new ImageIcon(Principal.class.getResource("/img/cita.png")));
		menuCitas.setFont(new Font("Bahnschrift", Font.BOLD, 25));
		menuBar.add(menuCitas);
		JMenuItem itemCrearCita = new JMenuItem("Crear/Modificar Cita");
		itemCrearCita.setFont(new Font("Tahoma", Font.PLAIN, 20));
		itemCrearCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionarCitas();
			}
		});
		menuCitas.add(itemCrearCita);

		/* MENU DE PACIENTES */
		menuPacientes = new JMenu("  Gesti\u00F3n de Pacientes\r\n");
		menuPacientes.setForeground(Color.WHITE);
		menuPacientes.setIcon(new ImageIcon(Principal.class.getResource("/img/gestion-de-clientes.png")));
		menuPacientes.setFont(new Font("Bahnschrift", Font.BOLD, 25));
		menuBar.add(menuPacientes);
		JMenuItem itemRegPaciente = new JMenuItem("Registrar Cliente");
		itemRegPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 20));

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
		menuConsulta.setIcon(new ImageIcon(Principal.class.getResource("/img/dato-de-registro.png")));
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
		menuAdministracion.setIcon(new ImageIcon(Principal.class.getResource("/img/doctor.png")));
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

		JMenuItem itemGestionarVacunas = new JMenuItem("Gestionar Vacunas");
		itemGestionarVacunas.setFont(new Font("Bahnschrift", Font.PLAIN, 20)); 
		//itemGestionarVacunas.setIcon(new ImageIcon(Principal.class.getResource("/img/vacuna.png"))); 
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
		panelDashboardCentral.setLayout(new GridLayout(1, 2, 20, 0)); 

		panelDashboardCentral.add(crearPanelEstadistico());     
		panelDashboardCentral.add(crearGraficoPastelEjemplo()); 


		contentPane.add(panelDashboardCentral, BorderLayout.CENTER);

		menuBar.add(javax.swing.Box.createHorizontalGlue());
		lblUsuario = new JLabel("Usuario: " + usuarioActual.getUsuario() + " (" + usuarioActual.getRol() + ")  "); 
		lblUsuario.setForeground(Color.WHITE);
		lblUsuario.setFont(new Font("Bahnschrift", Font.BOLD, 25)); 
		lblUsuario.setIcon(new ImageIcon(Principal.class.getResource("/img/perfil(2).png")));

		// 3. AÑADIRLO A LA BARRA
		menuBar.add(lblUsuario);

	}

	private void configurarAccesosPorRol() {

		if (menuCitas == null || menuConsulta == null || menuAdministracion == null || menuPacientes == null) {
			System.err.println("¡Error! Los menús no han sido inicializados.");
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

	private JPanel crearPanelEstadistico() {
		JPanel panelGrafico = new JPanel(new BorderLayout());
		panelGrafico.setBackground(Color.WHITE);

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		LocalDate hoy = LocalDate.now();
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES"));
		for (int i = 3; i >= 0; i--) {
			LocalDate fechaMes = hoy.minusMonths(i);
			String nombreMes = fechaMes.format(formateador);
			int mes = fechaMes.getMonthValue();
			int anio = fechaMes.getYear();   
			nombreMes = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1);
			int cantidadReal = Clinica.getInstancia().contarCitasPorMes(mes, anio);
			dataset.addValue(cantidadReal, "Citas Completadas", nombreMes);
		}
		JFreeChart chart = ChartFactory.createBarChart(
				"Rendimiento Mensual",       
				"Meses",                     
				"Pacientes Atendidos",       
				dataset,                     
				PlotOrientation.VERTICAL,    
				true,                        
				true,                        
				false                        
				);

		chart.setBackgroundPaint(Color.WHITE);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(new Color(245, 245, 245));
		plot.setOutlineVisible(false);


		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(60, 70, 123));
		renderer.setDrawBarOutline(false);


		ChartPanel chartPanel = new ChartPanel(chart);
		panelGrafico.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panelGrafico.add(chartPanel, BorderLayout.CENTER);


		renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
		renderer.setShadowVisible(false); 
		renderer.setSeriesPaint(0, new Color(60, 70, 123)); 

		renderer.setBaseItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		return panelGrafico;
	}
	private JPanel crearGraficoPastelEjemplo() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Consultas", 60);
		dataset.setValue("Emergencias", 25);
		dataset.setValue("Hospitalización", 15);

		JFreeChart chart = ChartFactory.createPieChart(
				"Tipos de Atención (Hoy)",
				dataset,
				true, 
				true,
				false
				);

		chart.setBackgroundPaint(Color.WHITE);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE); 
		plot.setOutlineVisible(false); 
		plot.setSectionPaint("Consultas", new Color(60, 70, 123));  
		plot.setSectionPaint("Emergencias", new Color(231, 76, 60));
		plot.setSectionPaint("Hospitalización", new Color(46, 204, 113));
		plot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator(
				"{0} ({2})", java.text.NumberFormat.getNumberInstance(), java.text.NumberFormat.getPercentInstance()
				));


		plot.setLabelBackgroundPaint(null); 
		plot.setLabelOutlinePaint(null);    
		plot.setLabelShadowPaint(null);     
		plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12)); 
		ChartPanel chartPanel = new ChartPanel(chart);
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10),
				javax.swing.BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
				));
		panel.add(chartPanel, BorderLayout.CENTER);

		return panel;
	}
}
