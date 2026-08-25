package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import Utils.GeneradorPDF;
import com.toedter.calendar.JDateChooser;
import logico.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ReportesGenerales extends JDialog {

    private JTabbedPane tabbedPane;
    private Color colorPrimario = new Color(60, 70, 123);
    private Color colorRojo = new Color(231, 76, 60);

    private ArrayList<Cita> listaCitasGlobal;
    private ArrayList<Cliente> listaClientesGlobal;
    private ArrayList<Medico> listaMedicosGlobal;
    private ArrayList<Vacuna> listaVacunasGlobal;
    private ArrayList<Enfermedad> listaEnfermedadesGlobal;
    private ArrayList<Consulta> listaConsultasGlobal;

    private DefaultTableModel modelCitasFecha, modelConsultasFecha, modelMedicosEsp, modelVacunas;
    private DefaultTableModel modelEnfEsp, modelConsMedFecha, modelEnfFecha, modelDiasPico, modelSexoFecha;
    private DefaultTableModel modelEdades, modelAuditoria;

    private JDateChooser d1Citas, d2Citas, d1Cons, d2Cons, d1ConsMed, d2ConsMed;
    private JDateChooser d1Enf, d2Enf, d1Pico, d2Pico, d1Sexo, d2Sexo;
    private JComboBox<String> cbEnfermedad, cbMedico, cbEnfFiltro;
    private JCheckBox chkVigilanciaOnly;

    public ReportesGenerales() {
        setTitle("Centro de Reportes Estadísticos");
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(ReportesGenerales.class.getResource("/img/dato-de-registro.png")));
        } catch (Exception e) {
        }
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
        tabbedPane.addTab("8. Días Pico", crearPanelDiasPico());
        tabbedPane.addTab("9. Demografía (Sexo)", crearPanelSexoFecha());
        tabbedPane.addTab("10. Demografía (Edad)", crearPanelEdades());
        tabbedPane.addTab("11. Auditoría Clínica", crearPanelAuditoria());

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

        listaVacunasGlobal = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);

        listaEnfermedadesGlobal = (ArrayList<Enfermedad>) ClienteSocket.enviar("LISTAR_ENFERMEDADES", null);

        listaClientesGlobal = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_CLIENTES", null);

        listaMedicosGlobal = (ArrayList<Medico>) ClienteSocket.enviar("LISTAR_MEDICOS", null);

        listaConsultasGlobal = (ArrayList<Consulta>) ClienteSocket.enviar("LISTAR_CONSULTAS", null);

        // Evitar listas null
        if (listaCitasGlobal == null) {
            listaCitasGlobal = new ArrayList<>();
        }

        if (listaVacunasGlobal == null) {
            listaVacunasGlobal = new ArrayList<>();
        }

        if (listaEnfermedadesGlobal == null) {
            listaEnfermedadesGlobal = new ArrayList<>();
        }

        if (listaClientesGlobal == null) {
            listaClientesGlobal = new ArrayList<>();
        }

        if (listaMedicosGlobal == null) {
            listaMedicosGlobal = new ArrayList<>();
        }

        if (listaConsultasGlobal == null) {
            listaConsultasGlobal = new ArrayList<>();
        }
    }

    private JPanel crearPanelCitasPorFecha() {
        modelCitasFecha = new DefaultTableModel(new String[]{"Fecha", "Total Citas"}, 0);
        d1Citas = new JDateChooser();
        d2Citas = new JDateChooser();

        JButton btnGenerar = new JButton("Generar");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarCitasPorFecha());

        return armarPanelFiltroTabla(modelCitasFecha, "Resumen de Citas por Fecha", new Object[]{new JLabel("Desde:"), d1Citas, new JLabel("Hasta:"), d2Citas, btnGenerar});
    }

    private void generarCitasPorFecha() {
        if (validarFechas(d1Citas, d2Citas)) {
            modelCitasFecha.setRowCount(0);
            Map<String, Integer> conteo = new TreeMap<>();
            LocalDate inicio = getFecha(d1Citas);
            LocalDate fin = getFecha(d2Citas);

            int totalRango = 0;
            if (listaCitasGlobal != null) {
                for (Cita c : listaCitasGlobal) {
                    if (c.getFechaCita() == null) continue; // Protección
                    LocalDate fechaCita = c.getFechaCita().toLocalDate();
                    if (!fechaCita.isBefore(inicio) && !fechaCita.isAfter(fin)) {
                        String key = fechaCita.toString();
                        conteo.put(key, conteo.getOrDefault(key, 0) + 1);
                        totalRango++;
                    }
                }
            }
            for (String fecha : conteo.keySet()) {
                modelCitasFecha.addRow(new Object[]{fecha, conteo.get(fecha)});
            }
            modelCitasFecha.addRow(new Object[]{"TOTAL EN RANGO", totalRango});
        }
    }

    private JPanel crearPanelConsultasPorFecha() {
        modelConsultasFecha = new DefaultTableModel(new String[]{"Fecha", "Total Consultas"}, 0);
        d1Cons = new JDateChooser();
        d2Cons = new JDateChooser();

        JButton btnGenerar = new JButton("Generar");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarConsultasPorFecha());

        return armarPanelFiltroTabla(modelConsultasFecha, "Resumen de Consultas Realizadas", new Object[]{new JLabel("Desde:"), d1Cons, new JLabel("Hasta:"), d2Cons, btnGenerar});
    }

    private void generarConsultasPorFecha() {
        if (validarFechas(d1Cons, d2Cons)) {
            modelConsultasFecha.setRowCount(0);
            Map<String, Integer> conteo = new TreeMap<>();
            LocalDate inicio = getFecha(d1Cons);
            LocalDate fin = getFecha(d2Cons);

            int totalRango = 0;
            if (listaConsultasGlobal != null) {
                for (Consulta con : listaConsultasGlobal) {
                    LocalDate fechaCon = con.getFechaConsulta();
                    if (fechaCon != null && !fechaCon.isBefore(inicio) && !fechaCon.isAfter(fin)) {
                        String key = fechaCon.toString();
                        conteo.put(key, conteo.getOrDefault(key, 0) + 1);
                        totalRango++;
                    }
                }
            }
            for (String fecha : conteo.keySet()) {
                modelConsultasFecha.addRow(new Object[]{fecha, conteo.get(fecha)});
            }
            modelConsultasFecha.addRow(new Object[]{"TOTAL EN RANGO", totalRango});
        }
    }

    private JPanel crearPanelMedicosEspecialidad() {
        modelMedicosEsp = new DefaultTableModel(new String[]{"Especialidad", "Cantidad de Médicos"}, 0);
        JButton btnGenerar = new JButton("Cargar Médicos");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarMedicosEspecialidad());

        JPanel panel = armarPanelFiltroTabla(modelMedicosEsp, "Distribución de Médicos", new Object[]{btnGenerar});
        generarMedicosEspecialidad(); // Intento de carga inicial
        return panel;
    }

    private void generarMedicosEspecialidad() {
        modelMedicosEsp.setRowCount(0);
        Map<String, Integer> conteo = new HashMap<>();
        int total = 0;

        if (listaMedicosGlobal != null) {
            for (Medico m : listaMedicosGlobal) {
                if (m.getEspecialidad() == null) continue;
                String esp = m.getEspecialidad().getNombre();
                conteo.put(esp, conteo.getOrDefault(esp, 0) + 1);
                total++;
            }
        }
        for (String esp : conteo.keySet()) {
            modelMedicosEsp.addRow(new Object[]{esp, conteo.get(esp)});
        }
        modelMedicosEsp.addRow(new Object[]{"TOTAL CLÍNICA", total});
    }

    private JPanel crearPanelVacunas() {
        modelVacunas = new DefaultTableModel(new String[]{"Código", "Nombre", "Descripción"}, 0);
        JButton btnGenerar = new JButton("Cargar Catálogo");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarVacunas());

        JPanel panel = armarPanelFiltroTabla(modelVacunas, "Catálogo de Vacunas", new Object[]{btnGenerar});
        generarVacunas();
        return panel;
    }

    private void generarVacunas() {
        modelVacunas.setRowCount(0);
        if (listaVacunasGlobal != null) {
            for (Vacuna v : listaVacunasGlobal) {
                modelVacunas.addRow(new Object[]{v.getCodigoVacuna(), v.getNombre(), v.getDescripcion()});
            }
        }
    }

    private JPanel crearPanelEnfermedadesActuales() {
        modelEnfEsp = new DefaultTableModel(new String[]{"Cédula", "Paciente", "Enfermedad", "Fecha Diagnóstico", "Vigilancia"}, 0);

        cbEnfermedad = new JComboBox<>();
        cbEnfermedad.addItem("<Todas>");
        if (listaEnfermedadesGlobal != null) listaEnfermedadesGlobal.forEach(e -> cbEnfermedad.addItem(e.getNombre()));

        chkVigilanciaOnly = new JCheckBox("Solo Vigiladas");
        chkVigilanciaOnly.setBackground(Color.WHITE);

        JButton btnGenerar = new JButton("Buscar Casos Recientes (2 Meses)");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarEnfermedadesActuales());

        return armarPanelFiltroTabla(modelEnfEsp, "Pacientes Enfermos (Actuales)", new Object[]{new JLabel("Enfermedad:"), cbEnfermedad, chkVigilanciaOnly, btnGenerar});
    }

    private void generarEnfermedadesActuales() {
        if (cbEnfermedad.getSelectedItem() == null) return;

        modelEnfEsp.setRowCount(0);
        String enfSeleccionada = cbEnfermedad.getSelectedItem().toString();
        boolean soloVigilancia = chkVigilanciaOnly.isSelected();

        if (listaConsultasGlobal != null) {
            for (Consulta con : listaConsultasGlobal) {
                if (con.getEnfermedadesDiag() != null && con.getCliente() != null) {
                    for (Enfermedad enf : con.getEnfermedadesDiag()) {
                        boolean pasaNombre = enfSeleccionada.equals("<Todas>") || enf.getNombre().equalsIgnoreCase(enfSeleccionada);
                        boolean pasaVigilancia = !soloVigilancia || enf.isVigilancia();

                        if (pasaNombre && pasaVigilancia) {
                            modelEnfEsp.addRow(new Object[]{con.getCliente().getCedula(), con.getCliente().getNombre() + " " + con.getCliente().getApellido(), enf.getNombre(), con.getFechaConsulta() != null ? con.getFechaConsulta() : "S/F", enf.isVigilancia() ? "Sí" : "NO"});
                        }
                    }
                }
            }
        }
    }

    private JPanel crearPanelConsultasMedicoFecha() {
        modelConsMedFecha = new DefaultTableModel(new String[]{"Fecha", "Paciente", "Diagnóstico"}, 0);
        d1ConsMed = new JDateChooser();
        d2ConsMed = new JDateChooser();

        cbMedico = new JComboBox<>();
        cbMedico.addItem("<Seleccione>");
        if (listaMedicosGlobal != null)
            listaMedicosGlobal.forEach(m -> cbMedico.addItem(m.getCedula() + " - " + m.getNombre()));

        JButton btnGenerar = new JButton("Generar");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarConsMedFecha());

        return armarPanelFiltroTabla(modelConsMedFecha, "Consultas por Médico y Rango", new Object[]{new JLabel("Médico:"), cbMedico, new JLabel("Desde:"), d1ConsMed, new JLabel("Hasta:"), d2ConsMed, btnGenerar});
    }

    private void generarConsMedFecha() {
        if (cbMedico.getSelectedIndex() <= 0 || !validarFechas(d1ConsMed, d2ConsMed)) return;

        modelConsMedFecha.setRowCount(0);
        String seleccion = cbMedico.getSelectedItem().toString();
        String cedulaMed = seleccion.split(" - ")[0].trim();
        LocalDate inicio = getFecha(d1ConsMed);
        LocalDate fin = getFecha(d2ConsMed);
        int total = 0;

        if (listaConsultasGlobal != null) {
            for (Consulta con : listaConsultasGlobal) {
                boolean coincideMedico = false;
                if (con.getMedico() != null && con.getMedico().getCedula() != null) {
                    coincideMedico = con.getMedico().getCedula().trim().equalsIgnoreCase(cedulaMed);
                }

                if (coincideMedico) {
                    LocalDate f = con.getFechaConsulta();
                    if (f != null && !f.isBefore(inicio) && !f.isAfter(fin)) {
                        String nombreCliente = (con.getCliente() != null) ? con.getCliente().getNombre() + " " + con.getCliente().getApellido() : "Paciente N/A";

                        modelConsMedFecha.addRow(new Object[]{f, nombreCliente, con.getDiagnostico()});
                        total++;
                    }
                }
            }
        }
        modelConsMedFecha.addRow(new Object[]{"TOTAL", total, ""});
    }

    private JPanel crearPanelEnfermedadFecha() {
        modelEnfFecha = new DefaultTableModel(new String[]{"Fecha", "Cantidad Casos"}, 0);
        d1Enf = new JDateChooser();
        d2Enf = new JDateChooser();

        cbEnfFiltro = new JComboBox<>();
        cbEnfFiltro.addItem("<Seleccione>");
        if (listaEnfermedadesGlobal != null) listaEnfermedadesGlobal.forEach(e -> cbEnfFiltro.addItem(e.getNombre()));

        JButton btnGenerar = new JButton("Contar");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarEnfFecha());

        return armarPanelFiltroTabla(modelEnfFecha, "Evolución de Enfermedad por Fecha", new Object[]{new JLabel("Enfermedad:"), cbEnfFiltro, new JLabel("Desde:"), d1Enf, new JLabel("Hasta:"), d2Enf, btnGenerar});
    }


    private void generarEnfFecha() {

        if (cbEnfFiltro.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una enfermedad.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarFechas(d1Enf, d2Enf)) {
            return;
        }

        modelEnfFecha.setRowCount(0);

        String nombreEnfermedad = cbEnfFiltro.getSelectedItem().toString().trim();

        LocalDate inicio = getFecha(d1Enf);
        LocalDate fin = getFecha(d2Enf);

        Map<LocalDate, Integer> conteo = new TreeMap<>();

        if (listaConsultasGlobal == null || listaConsultasGlobal.isEmpty()) {

            JOptionPane.showMessageDialog(this, "No existen consultas registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        for (Consulta consulta : listaConsultasGlobal) {

            if (consulta == null) {
                continue;
            }

            LocalDate fecha = consulta.getFechaConsulta();

            if (fecha == null) {
                continue;
            }

            if (fecha.isBefore(inicio) || fecha.isAfter(fin)) {
                continue;
            }

            boolean encontrada = false;

            /*
             * =====================================================
             * 1. BUSCAR EN LAS ENFERMEDADES RELACIONADAS
             * =====================================================
             */
            if (consulta.getEnfermedadesDiag() != null) {

                for (Enfermedad enfermedad : consulta.getEnfermedadesDiag()) {

                    if (enfermedad == null || enfermedad.getNombre() == null) {
                        continue;
                    }

                    String nombreRegistrado = enfermedad.getNombre().trim();

                    if (nombreRegistrado.equalsIgnoreCase(nombreEnfermedad)) {

                        encontrada = true;
                        break;
                    }
                }
            }

            /*
             * =====================================================
             * 2. SI NO ESTÁ RELACIONADA, BUSCAR EN DIAGNOSTICO
             * =====================================================
             *
             * Esto permite trabajar con las consultas antiguas
             * que tienen la enfermedad escrita directamente
             * en el campo diagnostico.
             */
            if (!encontrada && consulta.getDiagnostico() != null) {

                String diagnostico = consulta.getDiagnostico().trim().toLowerCase();

                String enfermedadBuscada = nombreEnfermedad.trim().toLowerCase();

                if (diagnostico.contains(enfermedadBuscada)) {
                    encontrada = true;
                }
            }

            /*
             * =====================================================
             * 3. CONTABILIZAR
             * =====================================================
             */
            if (encontrada) {

                conteo.put(fecha, conteo.getOrDefault(fecha, 0) + 1);
            }
        }

        /*
         * =====================================================
         * MOSTRAR RESULTADOS
         * =====================================================
         */
        for (Map.Entry<LocalDate, Integer> entrada : conteo.entrySet()) {

            modelEnfFecha.addRow(new Object[]{entrada.getKey(), entrada.getValue()});
        }

        if (conteo.isEmpty()) {

            JOptionPane.showMessageDialog(this, "No se encontraron casos de " + nombreEnfermedad + " en el rango seleccionado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel crearPanelDiasPico() {
        modelDiasPico = new DefaultTableModel(new String[]{"Fecha", "Total Pacientes Atendidos"}, 0);
        d1Pico = new JDateChooser();
        d2Pico = new JDateChooser();

        JButton btnGenerar = new JButton("Buscar Picos");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarDiasPico());

        return armarPanelFiltroTabla(modelDiasPico, "Días de Mayor Afluencia", new Object[]{new JLabel("Desde:"), d1Pico, new JLabel("Hasta:"), d2Pico, btnGenerar});
    }

    private void generarDiasPico() {
        if (!validarFechas(d1Pico, d2Pico)) return;
        modelDiasPico.setRowCount(0);

        LocalDate inicio = getFecha(d1Pico);
        LocalDate fin = getFecha(d2Pico);
        Map<String, Integer> conteo = new HashMap<>();

        if (listaConsultasGlobal != null) {
            for (Consulta con : listaConsultasGlobal) {
                LocalDate f = con.getFechaConsulta();
                if (f != null && !f.isBefore(inicio) && !f.isAfter(fin)) {
                    String key = f.toString();
                    conteo.put(key, conteo.getOrDefault(key, 0) + 1);
                }
            }
        }

        ArrayList<Map.Entry<String, Integer>> lista = new ArrayList<>(conteo.entrySet());
        lista.sort((a, b) -> b.getValue() - a.getValue());

        for (Map.Entry<String, Integer> entry : lista) {
            modelDiasPico.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private JPanel crearPanelSexoFecha() {
        modelSexoFecha = new DefaultTableModel(new String[]{"Fecha", "Masculino", "Femenino"}, 0);
        d1Sexo = new JDateChooser();
        d2Sexo = new JDateChooser();

        JButton btnGenerar = new JButton("Generar");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarSexoFecha());

        return armarPanelFiltroTabla(modelSexoFecha, "Demografía de Atención por Fecha", new Object[]{new JLabel("Desde:"), d1Sexo, new JLabel("Hasta:"), d2Sexo, btnGenerar});
    }


    private void generarSexoFecha() {
        if (!validarFechas(d1Sexo, d2Sexo)) return;
        modelSexoFecha.setRowCount(0);
        LocalDate inicio = getFecha(d1Sexo);
        LocalDate fin = getFecha(d2Sexo);

        Map<String, int[]> conteo = new TreeMap<>();

        if (listaConsultasGlobal != null) {
            for (Consulta con : listaConsultasGlobal) {
                LocalDate f = con.getFechaConsulta();
                if (f != null && !f.isBefore(inicio) && !f.isAfter(fin)) {
                    String genero = "Desconocido";
                    if (con.getCliente() != null && con.getCliente().getGenero() != null) {
                        genero = con.getCliente().getGenero().trim();
                    }

                    String key = f.toString();
                    conteo.putIfAbsent(key, new int[]{0, 0});

                    if (genero.equalsIgnoreCase("Masculino") || genero.equalsIgnoreCase("M")) {
                        conteo.get(key)[0]++;
                    } else if (genero.equalsIgnoreCase("Femenino") || genero.equalsIgnoreCase("F")) {
                        conteo.get(key)[1]++;
                    }
                }
            }
        }

        for (String fecha : conteo.keySet()) {
            modelSexoFecha.addRow(new Object[]{fecha, conteo.get(fecha)[0], conteo.get(fecha)[1]});
        }
    }

    private JPanel crearPanelEdades() {
        modelEdades = new DefaultTableModel(new String[]{"Rango de Edad", "Cantidad Pacientes", "Porcentaje"}, 0);

        JButton btnGenerar = new JButton("Calcular Demografía");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarEdades());

        JPanel panel = armarPanelFiltroTabla(modelEdades, "Distribución Demográfica por Edad", new Object[]{btnGenerar});
        generarEdades();
        return panel;
    }

    private void generarEdades() {
        modelEdades.setRowCount(0);
        int[] contadores = new int[5];
        String[] etiquetas = {"Niños (0-12)", "Adolescentes (13-19)", "Jóvenes (20-35)", "Adultos (36-59)", "Adultos Mayores (60+)"};
        int totalPacientes = 0;

        if (listaClientesGlobal != null) {
            for (Cliente cli : listaClientesGlobal) {
                if (cli.getFechaNacimiento() != null) {
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

        for (int i = 0; i < 5; i++) {
            String porcentaje = totalPacientes > 0 ? String.format("%.1f%%", (contadores[i] * 100.0 / totalPacientes)) : "0%";
            modelEdades.addRow(new Object[]{etiquetas[i], contadores[i], porcentaje});
        }
        modelEdades.addRow(new Object[]{"TOTAL REGISTRADOS", totalPacientes, "100%"});
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
        if (d1.getDate() == null || d2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione ambas fechas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        LocalDate inicio = getFecha(d1);
        LocalDate fin = getFecha(d2);

        if (inicio.isAfter(fin)) {
            JOptionPane.showMessageDialog(this, "La fecha 'Desde' no puede ser mayor que la fecha 'Hasta'.", "Error de Rango", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private LocalDate getFecha(JDateChooser d) {
        return d.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private JPanel crearPanelAuditoria() {
        modelAuditoria = new DefaultTableModel(new String[]{"Fecha", "Paciente", "Médico", "Especialidad", "Enfermedades Detectadas", "Síntomas Reportados"}, 0);

        JButton btnGenerar = new JButton("Cargar Auditoría Completa");
        Estilos.estilarBoton(btnGenerar, colorPrimario, Color.WHITE);
        btnGenerar.addActionListener(e -> generarAuditoria());

        JPanel panel = armarPanelFiltroTabla(modelAuditoria, "Auditoría Clínica", new Object[]{btnGenerar});
        generarAuditoria();
        return panel;
    }

    private void generarAuditoria() {
        modelAuditoria.setRowCount(0);

        if (listaConsultasGlobal != null) {
            for (Consulta con : listaConsultasGlobal) {
                String fecha = con.getFechaConsulta() != null ? con.getFechaConsulta().toString() : "N/A";

                String paciente = "Desconocido";
                if (con.getCliente() != null) {
                    paciente = con.getCliente().getNombre() + " " + con.getCliente().getApellido();
                }

                String medico = "Desconocido";
                String especialidad = "N/A";
                if (con.getMedico() != null) {
                    medico = con.getMedico().getNombre() + " " + con.getMedico().getApellido();
                    if (con.getMedico().getEspecialidad() != null) {
                        especialidad = con.getMedico().getEspecialidad().getNombre();
                    }
                }

                String enfermedades = "Ninguna";
                if (con.getEnfermedadesDiag() != null && !con.getEnfermedadesDiag().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < con.getEnfermedadesDiag().size(); i++) {
                        sb.append(con.getEnfermedadesDiag().get(i).getNombre());
                        if (i < con.getEnfermedadesDiag().size() - 1) {
                            sb.append(" | ");
                        }
                    }
                    enfermedades = sb.toString();
                } else if (con.getDiagnostico() != null && !con.getDiagnostico().isEmpty()) {
                    enfermedades = con.getDiagnostico();
                }

                String sintomas = con.getSintomas() != null ? con.getSintomas() : "No registrados";

                modelAuditoria.addRow(new Object[]{fecha, paciente, medico, especialidad, enfermedades, sintomas});
            }
        }
    }
}