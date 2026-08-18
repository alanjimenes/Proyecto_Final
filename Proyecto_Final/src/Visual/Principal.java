package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Cita;
import logico.Medico;
import logico.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class Principal extends JFrame {

    private JPanel contentPane;
    private Dimension dim;
    private User usuarioActual;
    private JMenuBar menuBar;
    private JMenu menuCitas;
    private JMenu menuPacientes;
    private JMenu menuConsulta;
    private JMenu menuAdministracion;
    private JPanel panel_1;
    private JLabel lblUsuario;
    private JLabel lblReloj;

    private JPanel panelGrafico;
    private ChartPanel chartPanel;

    public Principal(User usuarioLogueado) {
        this.usuarioActual = usuarioLogueado;

        // CONFIGURACIÓN BÁSICA DE LA VENTANA
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/img/seguro-de-salud.png")));
        } catch (Exception e) {
        }
        setTitle("Sistema de Gestión Hospitalaria - Usuario: " + usuarioActual.getUsuario());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // BARRA DE MEN�
        menuBar = new JMenuBar();
        menuBar.setForeground(Color.WHITE);
        menuBar.setBackground(new Color(60, 70, 123));
        setJMenuBar(menuBar);

        // --- MEN� CITAS ---
        menuCitas = new JMenu("  Gestión Citas  ");
        menuCitas.setForeground(Color.WHITE);
        try {
            menuCitas.setIcon(new ImageIcon(Principal.class.getResource("/img/cita.png")));
        } catch (Exception e) {
        }
        menuCitas.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        menuBar.add(menuCitas);

        JMenuItem itemCrearCita = new JMenuItem("Crear/Modificar Cita");
        itemCrearCita.setFont(new Font("Tahoma", Font.PLAIN, 18));
        itemCrearCita.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirDialogoDeCitas();
            }
        });
        menuCitas.add(itemCrearCita);

        // --- MEN� PACIENTES ---
        menuPacientes = new JMenu("  Gestión Pacientes  ");
        menuPacientes.setForeground(Color.WHITE);
        try {
            menuPacientes.setIcon(new ImageIcon(Principal.class.getResource("/img/gestion-de-clientes.png")));
        } catch (Exception e) {
        }
        menuPacientes.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        menuBar.add(menuPacientes);

        JMenuItem itemRegPaciente = new JMenuItem("Registrar Paciente");
        itemRegPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemRegPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegClientes reg = new RegClientes();
                reg.setModal(true);
                reg.setVisible(true);
            }
        });
        menuPacientes.add(itemRegPaciente);

        JMenuItem itemListarPacientes = new JMenuItem("Listado de Pacientes");
        itemListarPacientes.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemListarPacientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConsultarClientes consulta = new ConsultarClientes();
                consulta.setModal(true);
                consulta.setVisible(true);
            }
        });
        menuPacientes.add(itemListarPacientes);

        // --- MEN� CONSULTAS ---
        menuConsulta = new JMenu("  Consultas  ");
        menuConsulta.setForeground(Color.WHITE);
        try {
            menuConsulta.setIcon(new ImageIcon(Principal.class.getResource("/img/dato-de-registro.png")));
        } catch (Exception e) {
        }
        menuConsulta.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        menuBar.add(menuConsulta);

        JMenuItem itemVerMisCitas = new JMenuItem("Ver Citas de Hoy");
        itemVerMisCitas.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemVerMisCitas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MisCitas misCitas = new MisCitas(usuarioActual);
                misCitas.setModal(true);
                misCitas.setVisible(true);
            }
        });
        menuConsulta.add(itemVerMisCitas);

        // --- MENÚ ADMINISTRACIÓN ---
        menuAdministracion = new JMenu("  Administración");
        menuAdministracion.setForeground(Color.WHITE);
        try {
            menuAdministracion.setIcon(new ImageIcon(Principal.class.getResource("/img/doctor.png")));
        } catch (Exception e) {
        }
        menuAdministracion.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        menuBar.add(menuAdministracion);

        JMenuItem itemGestionarUser = new JMenuItem("Gestionar Usuarios");
        itemGestionarUser.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemGestionarUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegUser reg = new RegUser();
                reg.setModal(true);
                reg.setVisible(true);
            }
        });
        menuAdministracion.add(itemGestionarUser);

        JMenuItem itemReportes = new JMenuItem("Reportes Generales");
        itemReportes.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemReportes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReportesGenerales rep = new ReportesGenerales();
                rep.setVisible(true);
            }
        });
        menuAdministracion.add(itemReportes);

        JMenu menuGestionMedicos = new JMenu("Gestionar Médicos");
        menuGestionMedicos.setFont(new Font("Bahnschrift", Font.PLAIN, 18));

        JMenuItem itemRegMedico = new JMenuItem("Registrar Médico");
        itemRegMedico.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemRegMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegMedico regMedico = new RegMedico();
                regMedico.setModal(true);
                regMedico.setVisible(true);
            }
        });
        menuGestionMedicos.add(itemRegMedico);

        JMenuItem itemListarMedicos = new JMenuItem("Listar Médicos");
        itemListarMedicos.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
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
        menuGestionEspecialidades.setFont(new Font("Bahnschrift", Font.PLAIN, 18));

        JMenuItem itemRegEspecialidad = new JMenuItem("Registrar Especialidad");
        itemRegEspecialidad.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemRegEspecialidad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegEspecialidad regEsp = new RegEspecialidad();
                regEsp.setModal(true);
                regEsp.setVisible(true);
            }
        });
        menuGestionEspecialidades.add(itemRegEspecialidad);

        JMenuItem itemListarEspecialidades = new JMenuItem("Consultar Especialidades");
        itemListarEspecialidades.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
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
        itemGestionarVacunas.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemGestionarVacunas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConsultarVacunas consulta = new ConsultarVacunas();
                consulta.setModal(true);
                consulta.setVisible(true);
            }
        });
        menuAdministracion.add(itemGestionarVacunas);

        JMenuItem itemGestionarEnf = new JMenuItem("Gestionar Enfermedades");
        itemGestionarEnf.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        itemGestionarEnf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConsultarEnfermedades consulta = new ConsultarEnfermedades();
                consulta.setModal(true);
                consulta.setVisible(true);
            }
        });
        menuAdministracion.add(itemGestionarEnf);

        // USUARIO LABEL (Derecha)
        menuBar.add(javax.swing.Box.createHorizontalGlue());
        lblUsuario = new JLabel("Usuario: " + usuarioActual.getUsuario() + " (" + usuarioActual.getRol() + ")  ");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        try {
            lblUsuario.setIcon(new ImageIcon(Principal.class.getResource("/img/perfil(2).png")));
        } catch (Exception e) {
        }
        menuBar.add(lblUsuario);

        // PANEL PRINCIPAL
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(null);
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // PANEL DASHBOARD CENTRAL
        JPanel panelDashboardCentral = new JPanel();
        panelDashboardCentral.setBackground(Color.WHITE);
        panelDashboardCentral.setLayout(new GridLayout(1, 1, 20, 0));
        contentPane.add(panelDashboardCentral, BorderLayout.CENTER);

        // INYECCION DE GRÁFICOS (Mantenemos la lógica pero dentro del constructor)
        panelDashboardCentral.add(crearPanelEstadistico());

        // PANEL INFERIOR (RELOJ Y LOGOUT)
        panel_1 = new JPanel();
        panel_1.setBackground(new Color(60, 70, 123));
        panel_1.setLayout(new BorderLayout());
        panel_1.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(panel_1, BorderLayout.SOUTH);

        lblReloj = new JLabel("00:00:00");
        lblReloj.setForeground(Color.WHITE);
        lblReloj.setFont(new Font("Bahnschrift", Font.BOLD, 36));
        lblReloj.setBorder(new EmptyBorder(0, 20, 0, 0));
        panel_1.add(lblReloj, BorderLayout.WEST);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setOpaque(false);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        // Aseg�rate de que Estilos exista, si no, borra esta l�nea
        try {
            Estilos.estilarBoton(btnLogout, new Color(231, 76, 60), Color.WHITE);
        } catch (Exception e) {
            btnLogout.setBackground(Color.RED);
            btnLogout.setForeground(Color.WHITE);
        }

        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea cerrar sesión?",
                        "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    Login login = new Login();
                    login.setVisible(true);
                }
            }
        });

        panelBoton.add(btnLogout);
        panel_1.add(panelBoton, BorderLayout.EAST);

        // INICIAR RELOJ E HILOS
        iniciarReloj();
        configurarAccesosPorRol();
    }

    // --------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES (LÓGICA)
    // --------------------------------------------------------------------------------

    private void configurarAccesosPorRol() {
        if (menuCitas == null)
            return;
        String rol = this.usuarioActual.getRol();

        menuCitas.setVisible(false);
        menuConsulta.setVisible(false);
        menuAdministracion.setVisible(false);
        menuPacientes.setVisible(false);

        if (rol.equalsIgnoreCase("Administrador")) {
            menuCitas.setVisible(true);
            menuConsulta.setVisible(true);
            menuAdministracion.setVisible(true);
            menuPacientes.setVisible(true);
        } else if (rol.equalsIgnoreCase("Asistente")) {
            menuCitas.setVisible(true);
        } else if (rol.equalsIgnoreCase("Medico")) {
            menuConsulta.setVisible(true);
            menuPacientes.setVisible(true);
        }
    }

    private void abrirDialogoDeCitas() {
        JDialog dialogCitas = new JDialog(Principal.this, "Gestión de Citas", true);
        GestionCitas panel = new GestionCitas();
        dialogCitas.getContentPane().add(panel);
        dialogCitas.setSize(1130, 750);
        dialogCitas.setResizable(false);
        dialogCitas.setLocationRelativeTo(Principal.this);
        try {
            dialogCitas.setIconImage(
                    Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/img/seguro-de-salud.png")));
        } catch (Exception e) {
        }
        dialogCitas.setVisible(true);
    }

    private void iniciarReloj() {
        javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hora = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("hh:mm:ss a"));
                if (lblReloj != null)
                    lblReloj.setText(hora);
            }
        });
        timer.setRepeats(true);
        timer.start();
    }


    private JPanel crearPanelEstadistico() {
        JPanel panelDashboard = new JPanel(new BorderLayout());
        panelDashboard.setBackground(Color.WHITE);

        String rol = usuarioActual.getRol();

        if (rol.equalsIgnoreCase("Médico")) {
            JPanel panelMedico = new JPanel(new GridLayout(1, 2, 20, 0));
            panelMedico.setBackground(Color.WHITE);
            panelMedico.setBorder(new EmptyBorder(20, 20, 20, 20));


            JTable tableAgenda = new JTable();
            tableAgenda.setRowHeight(30);
            tableAgenda.setShowVerticalLines(false);
            tableAgenda.setGridColor(new Color(230, 230, 230));
            tableAgenda.setSelectionBackground(new Color(232, 246, 255));
            tableAgenda.setSelectionForeground(Color.BLACK);
            tableAgenda.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            javax.swing.table.JTableHeader header = tableAgenda.getTableHeader();
            header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setBackground(new Color(60, 70, 123));
                    setForeground(Color.WHITE);
                    setFont(new Font("Bahnschrift", Font.BOLD, 14));
                    setHorizontalAlignment(JLabel.CENTER);
                    setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
                    return this;
                }
            });

            javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableModel modelAgenda = new DefaultTableModel(new String[]{"Hora", "Paciente", "Estado"}, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tableAgenda.setModel(modelAgenda);

            for (int i = 0; i < tableAgenda.getColumnCount(); i++) {
                tableAgenda.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            if (usuarioActual.getCodigoUsuario() != 0) {
                Object resp = ClienteSocket.enviar("BUSCAR_MEDICO", usuarioActual.getCodigoUsuario());
                if (resp instanceof Medico) {
                    Medico yo = (Medico) resp;
                    int completadas = 0;
                    int pendientes = 0;

                    if (yo.getCitasAsignadas() != null) {
                        for (Cita c : yo.getCitasAsignadas()) {
                            if (c.getFechaCita().toLocalDate().equals(LocalDate.now())) {
                                if (c.getEstado().equalsIgnoreCase("Pendiente")) {
                                    modelAgenda.addRow(new Object[]{
                                            c.getFechaCita().toLocalTime().toString(),
                                            c.getCliente().getNombre() + " " + c.getCliente().getApellido(),
                                            c.getEstado()
                                    });
                                    pendientes++;
                                } else if (c.getEstado().equalsIgnoreCase("Completada")) {
                                    completadas++;
                                }
                            }
                        }
                    }

                    DefaultPieDataset dataset = new DefaultPieDataset();
                    dataset.setValue("Pendientes (" + pendientes + ")", pendientes);
                    dataset.setValue("Completadas (" + completadas + ")", completadas);

                    JFreeChart chart = ChartFactory.createPieChart("Mi Progreso Diario", dataset, true, true, false);
                    chart.setBackgroundPaint(Color.WHITE);
                    chart.getTitle().setFont(new Font("Bahnschrift", Font.BOLD, 20));

                    PiePlot plot = (PiePlot) chart.getPlot();
                    plot.setBackgroundPaint(Color.WHITE);
                    plot.setOutlineVisible(false);
                    plot.setLabelFont(new Font("Bahnschrift", Font.PLAIN, 12));
                    plot.setShadowPaint(null);

                    plot.setSectionPaint("Pendientes (" + pendientes + ")", new Color(231, 76, 60)); // Rojo suave
                    plot.setSectionPaint("Completadas (" + completadas + ")", new Color(60, 70, 123)); // AZUL INSTITUCIONAL

                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setBorder(null);
                    panelMedico.add(chartPanel);
                }
            }

            JScrollPane scrollPane = new JScrollPane(tableAgenda);
            scrollPane.getViewport().setBackground(Color.WHITE);
            scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
            panelMedico.add(scrollPane);

            panelDashboard.add(panelMedico, BorderLayout.CENTER);

        } else if (rol.equalsIgnoreCase("Asistente")) {
            panelDashboard.setLayout(new BorderLayout());
            JLabel lblLogo = new JLabel("");
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            try {
                lblLogo.setIcon(new ImageIcon(Principal.class.getResource("/img/Logo-Azul.png")));
            } catch (Exception e) {
                lblLogo.setText("Bienvenido Asistente");
                lblLogo.setFont(new Font("Bahnschrift", Font.BOLD, 40));
                lblLogo.setForeground(new Color(60, 70, 123));
            }
            panelDashboard.add(lblLogo, BorderLayout.CENTER);

        } else {
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelBotones.setBackground(Color.WHITE);

            JButton btnEnf = new JButton("Enfermedades");
            try {
                Estilos.estilarBoton(btnEnf, Color.WHITE, Color.BLACK);
            } catch (Exception e) {
            }
            btnEnf.addActionListener(e -> actualizarGrafico("ENFERMEDADES"));

            JButton btnVac = new JButton("Vacunación");
            try {
                Estilos.estilarBoton(btnVac, Color.WHITE, Color.BLACK);
            } catch (Exception e) {
            }
            btnVac.addActionListener(e -> actualizarGrafico("VACUNAS"));

            panelBotones.add(btnEnf);
            panelBotones.add(btnVac);

            panelDashboard.add(panelBotones, BorderLayout.NORTH);

            panelGrafico = new JPanel(new BorderLayout());
            panelGrafico.setBackground(Color.WHITE);
            panelDashboard.add(panelGrafico, BorderLayout.CENTER);

            actualizarGrafico("ENFERMEDADES");
        }
        return panelDashboard;
    }

    @SuppressWarnings("unchecked")
    private void actualizarGrafico(String tipo) {
        if (panelGrafico == null)
            return;
        panelGrafico.removeAll();

        panelGrafico.setBorder(new EmptyBorder(20, 20, 20, 20));

        JFreeChart chart = null;
        java.util.Random random = new java.util.Random();

        Font fontTitulo = new Font("Bahnschrift", Font.BOLD, 20);
        Font fontEjes = new Font("Bahnschrift", Font.PLAIN, 14);
        Font fontEtiquetas = new Font("Bahnschrift", Font.PLAIN, 12);
        Font fontValores = new Font("Bahnschrift", Font.BOLD, 12);

        if (tipo.equals("ENFERMEDADES")) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // =============================================================
            // BLOQUE 1: DATOS DE PRUEBA (RANDOM) - ACTIVO
            // =============================================================
            String[] enfermedadesPrueba = {"Gripe A", "Covid-19", "Diabetes T2", "Hipertensi�n", "Gastritis", "Asma"};
            for (String enf : enfermedadesPrueba) {
                dataset.addValue(random.nextInt(45) + 5, "Casos", enf);
            }

            // =============================================================
            // BLOQUE 2: DATOS REALES (SERVIDOR) - COMENTADO
            // =============================================================
			/*
            ArrayList<Cliente> clientes = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_CLIENTES", null);
            java.util.HashMap<String, Integer> conteo = new java.util.HashMap<>();
            if (clientes != null) {
                for (Cliente cli : clientes) {
                    if (cli.getHistorial() != null && cli.getHistorial().getConsultas() != null) {
                        for (Consulta con : cli.getHistorial().getConsultas()) {
                            if (con.getEnfermedadesDiag() != null) {
                                for (Enfermedad enf : con.getEnfermedadesDiag()) {
                                    conteo.put(enf.getNombre(), conteo.getOrDefault(enf.getNombre(), 0) + 1);
                                }
                            }
                        }
                    }
                }
            }
            if (conteo.isEmpty()) dataset.addValue(0, "Sin Datos", "N/A");

            for (String key : conteo.keySet()) {
                dataset.addValue(conteo.get(key), "Casos", key);
            }
			 */
            // =============================================================

            chart = ChartFactory.createBarChart("Enfermedades Diagnosticada", "Enfermedad", "Casos", dataset,
                    PlotOrientation.VERTICAL, false, true, false);

            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();


            renderer.setSeriesPaint(0, new Color(60, 70, 123));

            configurarPlotYRenderer(chart, plot, renderer, fontTitulo, fontEjes, fontEtiquetas, fontValores);

        } else if (tipo.equals("VACUNAS")) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // =============================================================
            // BLOQUE 1: DATOS DE PRUEBA (RANDOM) - ACTIVO
            // =============================================================
            String[] vacunasPrueba = {"Pfizer", "Sinovac", "AstraZeneca", "Moderna", "Influenza"};
            for (String vac : vacunasPrueba) {
                dataset.addValue(random.nextInt(70) + 10, "Dosis", vac);
            }

            // =============================================================
            // BLOQUE 2: DATOS REALES (SERVIDOR) - COMENTADO
            // =============================================================
			/*
            ArrayList<Cliente> clientes = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_CLIENTES", null);
            java.util.HashMap<String, Integer> conteo = new java.util.HashMap<>();
            if (clientes != null) {
                for (Cliente cli : clientes) {
                    if (cli.getRegVacunas() != null) {
                        for (RegistroVacunacion reg : cli.getRegVacunas()) {
                            if (reg.getVacuna() != null) {
                                String nombre = reg.getVacuna().getNombre();
                                conteo.put(nombre, conteo.getOrDefault(nombre, 0) + 1);
                            }
                        }
                    }
                }
            }
            if (conteo.isEmpty()) dataset.addValue(0, "Sin Datos", "N/A");

            for (String key : conteo.keySet()) {
                dataset.addValue(conteo.get(key), "Dosis", key);
            }
			 */
            // =============================================================

            chart = ChartFactory.createBarChart("Vacunas Aplicadas (Demo)", "Vacuna", "Total Dosis", dataset,
                    PlotOrientation.HORIZONTAL, false, true, false);

            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            // COLOR VERDE QUIRÚRGICO
            renderer.setSeriesPaint(0, new Color(0, 150, 136));

            configurarPlotYRenderer(chart, plot, renderer, fontTitulo, fontEjes, fontEtiquetas, fontValores);
        }

        if (chart != null) {

            chart.getRenderingHints().put(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            chartPanel = new ChartPanel(chart);
            chartPanel.setBackground(Color.WHITE);
            panelGrafico.add(chartPanel, BorderLayout.CENTER);
        }

        panelGrafico.revalidate();
        panelGrafico.repaint();
    }

    private void configurarPlotYRenderer(JFreeChart chart, CategoryPlot plot, BarRenderer renderer, Font fTitulo,
                                         Font fEjes, Font fEtiquetas, Font fValores) {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(fTitulo);

        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));

        plot.getDomainAxis().setLabelFont(fEjes);
        plot.getDomainAxis().setTickLabelFont(fEtiquetas);
        plot.getRangeAxis().setLabelFont(fEjes);
        plot.getRangeAxis().setTickLabelFont(fEtiquetas);

        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelFont(fValores);
    }
}