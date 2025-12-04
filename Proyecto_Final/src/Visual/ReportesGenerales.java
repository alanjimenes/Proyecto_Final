package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.time.LocalDate;
import java.time.Period; // ¡IMPORTANTE PARA CALCULAR EDAD!
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import logico.Cita;
import logico.Cliente;
import logico.Consulta;
import logico.Enfermedad;
import logico.Medico;
import logico.RegistroVacunacion;
import logico.Vacuna;

public class ReportesGenerales extends JDialog {

	private JTabbedPane tabbedPane;
	private Color colorPrimario = new Color(60, 70, 123);
	private Color colorRojo = new Color(231, 76, 60);

	private ArrayList<Cita> listaCitasGlobal;
	private ArrayList<Cliente> listaClientesGlobal;
	private ArrayList<Medico> listaMedicosGlobal;
	private ArrayList<Vacuna> listaVacunasGlobal;
	private ArrayList<Enfermedad> listaEnfermedadesGlobal;

	private DefaultTableModel modelCitasFecha, modelConsultasFecha, modelMedicosEsp, modelVacunas;
	private DefaultTableModel modelEnfEsp, modelConsMedFecha, modelEnfFecha, modelDiasPico, modelSexoFecha;
	private DefaultTableModel modelEdades;

	private JDateChooser d1Citas, d2Citas, d1Cons, d2Cons, d1ConsMed, d2ConsMed;
	private JDateChooser d1Enf, d2Enf, d1Pico, d2Pico, d1Sexo, d2Sexo;
	private JComboBox<String> cbEnfermedad, cbMedico, cbEnfFiltro;
	private JCheckBox chkVigilanciaOnly;

	public ReportesGenerales() {
		setTitle("Centro de Reportes Estadísticos");
		try { setIconImage(Toolkit.getDefaultToolkit().getImage(ReportesGenerales.class.getResource("/img/dato-de-registro.png"))); } catch (Exception e) {}
		setSize(1250, 700);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		cargarDatosGlobales();

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(colorPrimario);
		panelNorte.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel lblTitulo = new JLabel("Reportes e Indicadores de Gestión");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 24));
		panelNorte.add(lblTitulo);
		getContentPane().add(panelNorte, BorderLayout.NORTH);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Bahnschrift", Font.PLAIN, 14));

		tabbedPane.addTab("1. Citas x Fecha", crearPanelCitasPorFecha());
		tabbedPane.addTab("2. Consultas x Fecha", crearPanelConsultasPorFecha());
		tabbedPane.addTab("3. Médicos x Esp.", crearPanelMedicosEspecialidad());
		tabbedPane.addTab("4. Catálogo Vacunas", crearPanelVacunas());
		tabbedPane.addTab("5. Casos Activos", crearPanelEnfermedadesActuales());
		tabbedPane.addTab("6. Rendimiento Médico", crearPanelConsultasMedicoFecha());
		tabbedPane.addTab("7. Epidemiología", crearPanelEnfermedadFecha());
		tabbedPane.addTab("9. Días Pico", crearPanelDiasPico());
		tabbedPane.addTab("10. Demografía (Sexo)", crearPanelSexoFecha());
		tabbedPane.addTab("11. Demografía (Edad)", crearPanelEdades());

		getContentPane().add(tabbedPane, BorderLayout.CENTER);


		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelSur.setBackground(colorPrimario);
		JButton btnCerrar = new JButton("Cerrar");
		Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
		btnCerrar.addActionListener(e -> dispose());
		panelSur.add(btnCerrar);
		getContentPane().add(panelSur, BorderLayout.SOUTH);
	}

	@SuppressWarnings("unchecked")
	private void cargarDatosGlobales() {
		listaCitasGlobal = (ArrayList<Cita>) ClienteSocket.enviar("LISTAR_CITAS", null);
		listaClientesGlobal = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_CLIENTES", null);
		listaMedicosGlobal = (ArrayList<Medico>) ClienteSocket.enviar("LISTAR_MEDICOS", null);
		listaVacunasGlobal = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);
		listaEnfermedadesGlobal = (ArrayList<Enfermedad>) ClienteSocket.enviar("LISTAR_ENFERMEDADES", null);
	}

	private JPanel crearPanelCitasPorFecha() {
		modelCitasFecha = new DefaultTableModel(new String[]{"Fecha", "Total Citas"}, 0);
		d1Citas = new JDateChooser(); d2Citas = new JDateChooser();

		JButton btnGenerar = new JButton("Generar");
		Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
		btnGenerar.addActionListener(e -> generarCitasPorFecha());

		return armarPanelFiltroTabla(modelCitasFecha, "Resumen de Citas por Fecha", 
				new Object[]{new JLabel("Desde:"), d1Citas, new JLabel("Hasta:"), d2Citas, btnGenerar});
	}

	private void generarCitasPorFecha() {
		if(validarFechas(d1Citas, d2Citas)) {
			modelCitasFecha.setRowCount(0);
			Map<String, Integer> conteo = new TreeMap<>();
			LocalDate inicio = getFecha(d1Citas);
			LocalDate fin = getFecha(d2Citas);

			int totalRango = 0;
			if(listaCitasGlobal != null) {
				for(Cita c : listaCitasGlobal) {
					LocalDate fechaCita = c.getFechaHora().toLocalDate();
					if(!fechaCita.isBefore(inicio) && !fechaCita.isAfter(fin)) {
						String key = fechaCita.toString();
						conteo.put(key, conteo.getOrDefault(key, 0) + 1);
						totalRango++;
					}
				}
			}
			for(String fecha : conteo.keySet()) {
				modelCitasFecha.addRow(new Object[]{fecha, conteo.get(fecha)});
			}
			modelCitasFecha.addRow(new Object[]{"TOTAL EN RANGO", totalRango});
		}
	}

	private JPanel crearPanelConsultasPorFecha() {
		modelConsultasFecha = new DefaultTableModel(new String[]{"Fecha", "Total Consultas"}, 0);
		d1Cons = new JDateChooser(); d2Cons = new JDateChooser();

		JButton btnGenerar = new JButton("Generar");
		Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
		btnGenerar.addActionListener(e -> generarConsultasPorFecha());

		return armarPanelFiltroTabla(modelConsultasFecha, "Resumen de Consultas Realizadas", 
				new Object[]{new JLabel("Desde:"), d1Cons, new JLabel("Hasta:"), d2Cons, btnGenerar});
	}

	private void generarConsultasPorFecha() {
		if(validarFechas(d1Cons, d2Cons)) {
			modelConsultasFecha.setRowCount(0);
			Map<String, Integer> conteo = new TreeMap<>();
			LocalDate inicio = getFecha(d1Cons);
			LocalDate fin = getFecha(d2Cons);

			int totalRango = 0;
			if(listaClientesGlobal != null) {
				for(Cliente cli : listaClientesGlobal) {
					for(Consulta con : cli.getHistorial().getConsultas()) {
						LocalDate fechaCon = con.getFechaConsulta();
						if(!fechaCon.isBefore(inicio) && !fechaCon.isAfter(fin)) {
							String key = fechaCon.toString();
							conteo.put(key, conteo.getOrDefault(key, 0) + 1);
							totalRango++;
						}
					}
				}
			}
			for(String fecha : conteo.keySet()) {
				modelConsultasFecha.addRow(new Object[]{fecha, conteo.get(fecha)});
			}
			modelConsultasFecha.addRow(new Object[]{"TOTAL EN RANGO", totalRango});
		}
	}

	private JPanel crearPanelMedicosEspecialidad() {
		modelMedicosEsp = new DefaultTableModel(new String[]{"Especialidad", "Cantidad de Médicos"}, 0);
		JPanel panel = armarPanelFiltroTabla(modelMedicosEsp, "Distribución de Médicos", new Object[]{});

		Map<String, Integer> conteo = new HashMap<>();
		int total = 0;
		if(listaMedicosGlobal != null) {
			for(Medico m : listaMedicosGlobal) {
				String esp = m.getEspecialidad().getNombre();
				conteo.put(esp, conteo.getOrDefault(esp, 0) + 1);
				total++;
			}
		}
		for(String esp : conteo.keySet()) {
			modelMedicosEsp.addRow(new Object[]{esp, conteo.get(esp)});
		}
		modelMedicosEsp.addRow(new Object[]{"TOTAL CLÍNICA", total});

		return panel;
	}

	private JPanel crearPanelVacunas() {
		modelVacunas = new DefaultTableModel(new String[]{"Código", "Nombre", "Descripción"}, 0);
		JPanel panel = armarPanelFiltroTabla(modelVacunas, "Catálogo de Vacunas", new Object[]{});

		if(listaVacunasGlobal != null) {
			for(Vacuna v : listaVacunasGlobal) {
				modelVacunas.addRow(new Object[]{v.getCodigo_vacun(), v.getNombre(), v.getDescripcion()});
			}
		}
		return panel;
	}

	private JPanel crearPanelEnfermedadesActuales() {
		modelEnfEsp = new DefaultTableModel(new String[]{"Cédula", "Paciente", "Enfermedad", "Fecha Diagnóstico", "Vigilancia"}, 0);

		cbEnfermedad = new JComboBox<>();
		cbEnfermedad.addItem("<Todas>");
		if(listaEnfermedadesGlobal != null) listaEnfermedadesGlobal.forEach(e -> cbEnfermedad.addItem(e.getNombre()));

		chkVigilanciaOnly = new JCheckBox("Solo Vigiladas");
		chkVigilanciaOnly.setBackground(Color.WHITE);

		JButton btnGenerar = new JButton("Buscar Casos Recientes (2 Meses)");
		Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
		btnGenerar.addActionListener(e -> generarEnfermedadesActuales());

		return armarPanelFiltroTabla(modelEnfEsp, "Pacientes Enfermos (Actuales)", 
				new Object[]{new JLabel("Enfermedad:"), cbEnfermedad, chkVigilanciaOnly, btnGenerar});
	}

	private void generarEnfermedadesActuales() {
		modelEnfEsp.setRowCount(0);
		String enfSeleccionada = cbEnfermedad.getSelectedItem().toString();
		boolean soloVigilancia = chkVigilanciaOnly.isSelected();
		LocalDate fechaLimite = LocalDate.now().minusMonths(2);

		if(listaClientesGlobal != null) {
			for(Cliente cli : listaClientesGlobal) {
				for(Consulta con : cli.getHistorial().getConsultas()) {
					if(con.getFechaConsulta().isAfter(fechaLimite)) {
						for(Enfermedad enf : con.getEnfermedadesDiag()) {
							boolean pasaNombre = enfSeleccionada.equals("<Todas>") || enf.getNombre().equals(enfSeleccionada);
							boolean pasaVigilancia = !soloVigilancia || enf.isVigilancia();

							if(pasaNombre && pasaVigilancia) {
								modelEnfEsp.addRow(new Object[]{
										cli.getCedula(),
										cli.getNombre() + " " + cli.getApellido(),
										enf.getNombre(),
										con.getFechaConsulta(),
										enf.isVigilancia() ? "SÍ" : "NO"
								});
							}
						}
					}
				}
			}
		}
	}

	private JPanel crearPanelConsultasMedicoFecha() {
		modelConsMedFecha = new DefaultTableModel(new String[]{"Fecha", "Paciente", "Diagnóstico"}, 0);
		d1ConsMed = new JDateChooser(); d2ConsMed = new JDateChooser();

		cbMedico = new JComboBox<>();
		cbMedico.addItem("<Seleccione>");
		if(listaMedicosGlobal != null) listaMedicosGlobal.forEach(m -> cbMedico.addItem(m.getCedula() + " - " + m.getNombre()));

		JButton btnGenerar = new JButton("Generar");
		Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
		btnGenerar.addActionListener(e -> generarConsMedFecha());

		return armarPanelFiltroTabla(modelConsMedFecha, "Consultas por Médico y Rango", 
				new Object[]{new JLabel("Médico:"), cbMedico, new JLabel("Desde:"), d1ConsMed, new JLabel("Hasta:"), d2ConsMed, btnGenerar});
	}

	private void generarConsMedFecha() {
		if(cbMedico.getSelectedIndex() == 0 || !validarFechas(d1ConsMed, d2ConsMed)) return;

		modelConsMedFecha.setRowCount(0);
		String cedulaMed = cbMedico.getSelectedItem().toString().split(" - ")[0];
		LocalDate inicio = getFecha(d1ConsMed);
		LocalDate fin = getFecha(d2ConsMed);
		int total = 0;

		if(listaClientesGlobal != null) {
			for(Cliente cli : listaClientesGlobal) {
				for(Consulta con : cli.getHistorial().getConsultas()) {
					if(con.getMedico().getCedula().equals(cedulaMed)) {
						LocalDate f = con.getFechaConsulta();
						if(!f.isBefore(inicio) && !f.isAfter(fin)) {
							modelConsMedFecha.addRow(new Object[]{f, cli.getNombre(), con.getDiagnostico()});
							total++;
						}
					}
				}
			}
		}
		modelConsMedFecha.addRow(new Object[]{"TOTAL", total, ""});
	}

	private JPanel crearPanelEnfermedadFecha() {
		modelEnfFecha = new DefaultTableModel(new String[]{"Fecha", "Cantidad Casos"}, 0);
		d1Enf = new JDateChooser(); d2Enf = new JDateChooser();

		cbEnfFiltro = new JComboBox<>();
		if(listaEnfermedadesGlobal != null) listaEnfermedadesGlobal.forEach(e -> cbEnfFiltro.addItem(e.getNombre()));

		JButton btnGenerar = new JButton("Contar");
		Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
		btnGenerar.addActionListener(e -> generarEnfFecha());

		return armarPanelFiltroTabla(modelEnfFecha, "Evolución de Enfermedad por Fecha", 
				new Object[]{new JLabel("Enfermedad:"), cbEnfFiltro, new JLabel("Desde:"), d1Enf, new JLabel("Hasta:"), d2Enf, btnGenerar});
	}

	private void generarEnfFecha() {
		if(!validarFechas(d1Enf, d2Enf)) return;
		modelEnfFecha.setRowCount(0);
		String nombreEnf = (String) cbEnfFiltro.getSelectedItem();
		LocalDate inicio = getFecha(d1Enf);
		LocalDate fin = getFecha(d2Enf);

		Map<String, Integer> conteo = new TreeMap<>();

		if(listaClientesGlobal != null) {
			for(Cliente cli : listaClientesGlobal) {
				for(Consulta con : cli.getHistorial().getConsultas()) {
					LocalDate f = con.getFechaConsulta();
					if(!f.isBefore(inicio) && !f.isAfter(fin)) {
						for(Enfermedad enf : con.getEnfermedadesDiag()) {
							if(enf.getNombre().equals(nombreEnf)) {
								String key = f.toString();
								conteo.put(key, conteo.getOrDefault(key, 0) + 1);
							}
						}
					}
				}
			}
		}
		for(String fecha : conteo.keySet()) {
			modelEnfFecha.addRow(new Object[]{fecha, conteo.get(fecha)});
		}
	}

	private JPanel crearPanelDiasPico() {
		modelDiasPico = new DefaultTableModel(new String[]{"Fecha", "Total Pacientes Atendidos"}, 0);
		d1Pico = new JDateChooser(); d2Pico = new JDateChooser();

		JButton btnGenerar = new JButton("Buscar Picos");
		Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
		btnGenerar.addActionListener(e -> generarDiasPico());

		return armarPanelFiltroTabla(modelDiasPico, "Días de Mayor Afluencia", 
				new Object[]{new JLabel("Desde:"), d1Pico, new JLabel("Hasta:"), d2Pico, btnGenerar});
	}

	private void generarDiasPico() {
		if(!validarFechas(d1Pico, d2Pico)) return;
		modelDiasPico.setRowCount(0);

		LocalDate inicio = getFecha(d1Pico);
		LocalDate fin = getFecha(d2Pico);
		Map<String, Integer> conteo = new HashMap<>();

		if(listaClientesGlobal != null) {
			for(Cliente cli : listaClientesGlobal) {
				for(Consulta con : cli.getHistorial().getConsultas()) {
					LocalDate f = con.getFechaConsulta();
					if(!f.isBefore(inicio) && !f.isAfter(fin)) {
						String key = f.toString();
						conteo.put(key, conteo.getOrDefault(key, 0) + 1);
					}
				}
			}
		}

		ArrayList<Map.Entry<String, Integer>> lista = new ArrayList<>(conteo.entrySet());
		lista.sort((a, b) -> b.getValue() - a.getValue());

		for(Map.Entry<String, Integer> entry : lista) {
			modelDiasPico.addRow(new Object[]{entry.getKey(), entry.getValue()});
		}
	}

	private JPanel crearPanelSexoFecha() {
		modelSexoFecha = new DefaultTableModel(new String[]{"Fecha", "Masculino", "Femenino"}, 0);
		d1Sexo = new JDateChooser(); d2Sexo = new JDateChooser();

		JButton btnGenerar = new JButton("Generar");
		Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
		btnGenerar.addActionListener(e -> generarSexoFecha());

		return armarPanelFiltroTabla(modelSexoFecha, "Demografía de Atención por Fecha", 
				new Object[]{new JLabel("Desde:"), d1Sexo, new JLabel("Hasta:"), d2Sexo, btnGenerar});
	}

	private void generarSexoFecha() {
		if(!validarFechas(d1Sexo, d2Sexo)) return;
		modelSexoFecha.setRowCount(0);
		LocalDate inicio = getFecha(d1Sexo);
		LocalDate fin = getFecha(d2Sexo);

		Map<String, int[]> conteo = new TreeMap<>();

		if(listaClientesGlobal != null) {
			for(Cliente cli : listaClientesGlobal) {
				String genero = cli.getGenero(); 
				if(genero == null) genero = "Desconocido";

				for(Consulta con : cli.getHistorial().getConsultas()) {
					LocalDate f = con.getFechaConsulta();
					if(!f.isBefore(inicio) && !f.isAfter(fin)) {
						String key = f.toString();
						conteo.putIfAbsent(key, new int[]{0, 0});

						if(genero.equalsIgnoreCase("Masculino")) conteo.get(key)[0]++;
						else if(genero.equalsIgnoreCase("Femenino")) conteo.get(key)[1]++;
					}
				}
			}
		}

		for(String fecha : conteo.keySet()) {
			modelSexoFecha.addRow(new Object[]{fecha, conteo.get(fecha)[0], conteo.get(fecha)[1]});
		}
	}

	private JPanel crearPanelEdades() {
		modelEdades = new DefaultTableModel(new String[]{"Rango de Edad", "Cantidad Pacientes", "Porcentaje"}, 0);
		JPanel panel = armarPanelFiltroTabla(modelEdades, "Distribución Demográfica por Edad", new Object[]{});

		int[] contadores = new int[5]; 
		String[] etiquetas = {"Niños (0-12)", "Adolescentes (13-19)", "Jóvenes (20-39)", "Adultos (40-59)", "Adultos Mayores (60+)"};
		int totalPacientes = 0;

		if(listaClientesGlobal != null) {
			for(Cliente cli : listaClientesGlobal) {
				if(cli.getFechaNacimiento() != null) {
					int edad = Period.between(cli.getFechaNacimiento(), LocalDate.now()).getYears();
					totalPacientes++;

					if (edad <= 12) contadores[0]++;
					else if (edad <= 19) contadores[1]++;
					else if (edad <= 39) contadores[2]++;
					else if (edad <= 59) contadores[3]++;
					else contadores[4]++;
				}
			}
		}

		for(int i=0; i<5; i++) {
			String porcentaje = totalPacientes > 0 ? String.format("%.1f%%", (contadores[i] * 100.0 / totalPacientes)) : "0%";
			modelEdades.addRow(new Object[]{etiquetas[i], contadores[i], porcentaje});
		}
		modelEdades.addRow(new Object[]{"TOTAL REGISTRADOS", totalPacientes, "100%"});

		return panel;
	}

	private JPanel armarPanelFiltroTabla(DefaultTableModel model, String tituloPDF, Object[] filtros) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.setBackground(Color.WHITE);
		top.setBorder(new TitledBorder(new LineBorder(colorPrimario), "Filtros", TitledBorder.LEADING, TitledBorder.TOP, null, colorPrimario));

		for (Object obj : filtros) {
			if (obj instanceof javax.swing.JComponent) top.add((javax.swing.JComponent) obj);
		}

		JButton btnPDF = new JButton("Exportar PDF");
		Estilos.estilarBoton(btnPDF, colorRojo, Color.WHITE);
		btnPDF.addActionListener(e -> {
			JTable tempTable = new JTable(model); 
			GeneradorPDF.exportarJTablePDF(tempTable, tituloPDF);
		});
		top.add(btnPDF);

		panel.add(top, BorderLayout.NORTH);

		JTable table = new JTable(model);
		estilarTabla(table);
		JScrollPane scroll = new JScrollPane(table);
		scroll.getViewport().setBackground(Color.WHITE);
		panel.add(scroll, BorderLayout.CENTER);

		return panel;
	}

	private void estilarTabla(JTable table) {
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		table.setRowHeight(25);
		JTableHeader header = table.getTableHeader();
		header.setBackground(colorPrimario);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		header.setOpaque(true);
	}

	private boolean validarFechas(JDateChooser d1, JDateChooser d2) {
		if(d1.getDate() == null || d2.getDate() == null) {
			JOptionPane.showMessageDialog(this, "Seleccione las fechas.");
			return false;
		}
		return true;
	}

	private LocalDate getFecha(JDateChooser d) {
		return d.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}