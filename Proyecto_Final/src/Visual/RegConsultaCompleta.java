package Visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Servicios.EnfermedadService;
import Servicios.MedicamentoService;
import Servicios.ConsultaService;
import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Analisis;
import logico.Consulta;
import logico.Enfermedad;
import logico.EvaluacionFisica;
import logico.Medico;
import logico.Medicamento;
import logico.RecetaMedica;
import logico.TipoAnalisis;

public class RegConsultaCompleta extends JFrame {

    private JPanel contentPane;
    private JTextField txtCedulaPaciente;
    private JTextField txtSintomas;
    private JTextField txtDiagnostico;

    // Signos Vitales
    private JTextField txtTemperatura;
    private JTextField txtFrecuencia;
    private JTextField txtPresion;
    private JTextField txtPeso;
    private JTextField txtTalla;

    // Tablas temporales para Recetas, Enfermedades y Análisis
    private JTable tableRecetas;
    private DefaultTableModel modeloRecetas;
    private ArrayList<RecetaMedica> listaRecetasTemp;

    private JTable tableEnfermedades;
    private DefaultTableModel modeloEnfermedades;
    private ArrayList<Enfermedad> listaEnfermedadesTemp;

    private JTable tableAnalisis;
    private DefaultTableModel modeloAnalisis;
    private ArrayList<Analisis> listaAnalisisTemp;

    private Medico medicoActual;

    public RegConsultaCompleta(Medico medicoActual) {
        this.medicoActual = medicoActual;
        this.listaRecetasTemp = new ArrayList<>();
        this.listaEnfermedadesTemp = new ArrayList<>();
        this.listaAnalisisTemp = new ArrayList<>();

        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(RegConsultaCompleta.class.getResource("/img/cita.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el ícono de la ventana.");
        }

        setTitle("Registro de Consulta Médica Avanzada");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setBounds(30, 20, 1100, 920);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(245, 247, 250));

        // --- ENCABEZADO INSTITUCIONAL ---
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 1084, 65);
        panelHeader.setBackground(new Color(60, 70, 123));
        panelHeader.setLayout(null);
        contentPane.add(panelHeader);

        JLabel lblTituloHeader = new JLabel("Registrar Consulta Médica - Dr(a). " + (medicoActual != null ? medicoActual.getNombre() : ""));
        lblTituloHeader.setForeground(Color.WHITE);
        lblTituloHeader.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        lblTituloHeader.setBounds(30, 15, 950, 35);
        panelHeader.add(lblTituloHeader);

        Font fuenteLabel = new Font("Bahnschrift", Font.BOLD, 14);
        Font fuenteTexto = new Font("Tahoma", Font.PLAIN, 14);
        Color colorTexto = new Color(50, 50, 50);

        // --- PACIENTE ---
        JLabel lblCedula = new JLabel("Cédula Paciente:");
        lblCedula.setFont(fuenteLabel);
        lblCedula.setForeground(colorTexto);
        lblCedula.setBounds(30, 85, 150, 25);
        contentPane.add(lblCedula);

        txtCedulaPaciente = new JTextField();
        txtCedulaPaciente.setFont(fuenteTexto);
        txtCedulaPaciente.setBounds(180, 82, 690, 32);
        txtCedulaPaciente.setEditable(false);
        contentPane.add(txtCedulaPaciente);

        JButton btnBuscarCliente = new JButton("Buscar Paciente");
        Estilos.estilarBoton(btnBuscarCliente, new Color(41, 128, 185), Color.WHITE);
        btnBuscarCliente.setBounds(885, 82, 170, 32);
        contentPane.add(btnBuscarCliente);

        btnBuscarCliente.addActionListener(e -> {
            ConsultarClientes selector = new ConsultarClientes();
            selector.setLocationRelativeTo(contentPane);
            selector.setModal(true);
            selector.setVisible(true);
            if (selector.getClienteSeleccionado() != null) {
                txtCedulaPaciente.setText(selector.getClienteSeleccionado().getCedula());
            }
        });

        // --- SÍNTOMAS ---
        JLabel lblSintomas = new JLabel("Síntomas:");
        lblSintomas.setFont(fuenteLabel);
        lblSintomas.setForeground(colorTexto);
        lblSintomas.setBounds(30, 127, 150, 25);
        contentPane.add(lblSintomas);

        txtSintomas = new JTextField();
        txtSintomas.setFont(fuenteTexto);
        txtSintomas.setBounds(180, 124, 875, 32);
        contentPane.add(txtSintomas);

        // --- DIAGNÓSTICO ---
        JLabel lblDiagnostico = new JLabel("Diagnóstico:");
        lblDiagnostico.setFont(fuenteLabel);
        lblDiagnostico.setForeground(colorTexto);
        lblDiagnostico.setBounds(30, 169, 150, 25);
        contentPane.add(lblDiagnostico);

        txtDiagnostico = new JTextField();
        txtDiagnostico.setFont(fuenteTexto);
        txtDiagnostico.setBounds(180, 166, 875, 32);
        contentPane.add(txtDiagnostico);

        // --- SECCIÓN EVALUACIÓN FÍSICA (SIGNOS VITALES) ---
        JSeparator separator = new JSeparator();
        separator.setBounds(30, 212, 1025, 10);
        contentPane.add(separator);

        JLabel lblSubEval = new JLabel("Evaluación Física y Signos Vitales");
        lblSubEval.setFont(new Font("Bahnschrift", Font.BOLD, 15));
        lblSubEval.setForeground(new Color(60, 70, 123));
        lblSubEval.setBounds(30, 222, 300, 25);
        contentPane.add(lblSubEval);

        JLabel lblTemp = new JLabel("Temperatura (°C):");
        lblTemp.setFont(fuenteLabel);
        lblTemp.setBounds(30, 258, 150, 25);
        contentPane.add(lblTemp);

        txtTemperatura = new JTextField();
        txtTemperatura.setFont(fuenteTexto);
        txtTemperatura.setBounds(180, 255, 180, 30);
        contentPane.add(txtTemperatura);

        JLabel lblFrec = new JLabel("Frec. Cardíaca:");
        lblFrec.setFont(fuenteLabel);
        lblFrec.setBounds(400, 258, 120, 25);
        contentPane.add(lblFrec);

        txtFrecuencia = new JTextField();
        txtFrecuencia.setFont(fuenteTexto);
        txtFrecuencia.setBounds(520, 255, 180, 30);
        contentPane.add(txtFrecuencia);

        JLabel lblPeso = new JLabel("Peso (kg):");
        lblPeso.setFont(fuenteLabel);
        lblPeso.setBounds(730, 258, 100, 25);
        contentPane.add(lblPeso);

        txtPeso = new JTextField();
        txtPeso.setFont(fuenteTexto);
        txtPeso.setBounds(825, 255, 230, 30);
        contentPane.add(txtPeso);

        JLabel lblPresion = new JLabel("Presión Arterial:");
        lblPresion.setFont(fuenteLabel);
        lblPresion.setBounds(30, 298, 150, 25);
        contentPane.add(lblPresion);

        txtPresion = new JTextField();
        txtPresion.setFont(fuenteTexto);
        txtPresion.setBounds(180, 295, 180, 30);
        contentPane.add(txtPresion);

        JLabel lblTalla = new JLabel("Talla (m):");
        lblTalla.setFont(fuenteLabel);
        lblTalla.setBounds(400, 298, 120, 25);
        contentPane.add(lblTalla);

        txtTalla = new JTextField();
        txtTalla.setFont(fuenteTexto);
        txtTalla.setBounds(520, 295, 180, 30);
        contentPane.add(txtTalla);

        // --- SECCIÓN ENFERMEDADES DIAGNOSTICADAS ---
        JSeparator separatorEnf = new JSeparator();
        separatorEnf.setBounds(30, 338, 1025, 10);
        contentPane.add(separatorEnf);

        JLabel lblSubEnf = new JLabel("Enfermedades Diagnosticadas");
        lblSubEnf.setFont(new Font("Bahnschrift", Font.BOLD, 15));
        lblSubEnf.setForeground(new Color(60, 70, 123));
        lblSubEnf.setBounds(30, 348, 300, 25);
        contentPane.add(lblSubEnf);

        JScrollPane scrollEnf = new JScrollPane();
        scrollEnf.setBounds(30, 378, 875, 80);
        contentPane.add(scrollEnf);

        modeloEnfermedades = new DefaultTableModel(new Object[][]{}, new String[]{"Nombre", "Vigilancia"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableEnfermedades = new JTable(modeloEnfermedades);
        scrollEnf.setViewportView(tableEnfermedades);

        JButton btnAgregarEnf = new JButton("<html><center>Asignar<br>Enfermedad</center></html>");
        Estilos.estilarBoton(btnAgregarEnf, new Color(40, 167, 69), Color.WHITE);
        btnAgregarEnf.setBounds(915, 378, 140, 38);
        contentPane.add(btnAgregarEnf);

        JButton btnQuitarEnf = new JButton("Quitar Enfermedad");
        Estilos.estilarBoton(btnQuitarEnf, new Color(231, 76, 60), Color.WHITE);
        btnQuitarEnf.setBounds(915, 420, 140, 38);
        contentPane.add(btnQuitarEnf);

        // --- SECCIÓN ÓRDENES DE ANÁLISIS CLÍNICOS ---
        JSeparator separatorAna = new JSeparator();
        separatorAna.setBounds(30, 470, 1025, 10);
        contentPane.add(separatorAna);

        JLabel lblSubAnalisis = new JLabel("Órdenes de Análisis Clínicos");
        lblSubAnalisis.setFont(new Font("Bahnschrift", Font.BOLD, 15));
        lblSubAnalisis.setForeground(new Color(60, 70, 123));
        lblSubAnalisis.setBounds(30, 480, 300, 25);
        contentPane.add(lblSubAnalisis);

        JScrollPane scrollAna = new JScrollPane();
        scrollAna.setBounds(30, 510, 875, 80);
        contentPane.add(scrollAna);

        modeloAnalisis = new DefaultTableModel(new Object[][]{}, new String[]{"Tipo de Análisis", "Estado", "Indicaciones / Detalle"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableAnalisis = new JTable(modeloAnalisis);
        scrollAna.setViewportView(tableAnalisis);

        JButton btnAgregarAnalisis = new JButton("<html><center>Ordenar<br>Análisis</center></html>");
        Estilos.estilarBoton(btnAgregarAnalisis, new Color(40, 167, 69), Color.WHITE);
        btnAgregarAnalisis.setBounds(915, 510, 140, 38);
        contentPane.add(btnAgregarAnalisis);

        JButton btnQuitarAnalisis = new JButton("Quitar Análisis");
        Estilos.estilarBoton(btnQuitarAnalisis, new Color(231, 76, 60), Color.WHITE);
        btnQuitarAnalisis.setBounds(915, 552, 140, 38);
        contentPane.add(btnQuitarAnalisis);

        // --- SECCIÓN RECETA MÉDICA ---
        JSeparator separatorRec = new JSeparator();
        separatorRec.setBounds(30, 600, 1025, 10);
        contentPane.add(separatorRec);

        JLabel lblSubReceta = new JLabel("Receta Médica / Medicamentos");
        lblSubReceta.setFont(new Font("Bahnschrift", Font.BOLD, 15));
        lblSubReceta.setForeground(new Color(60, 70, 123));
        lblSubReceta.setBounds(30, 610, 300, 25);
        contentPane.add(lblSubReceta);

        JScrollPane scrollRec = new JScrollPane();
        scrollRec.setBounds(30, 640, 875, 95);
        contentPane.add(scrollRec);

        modeloRecetas = new DefaultTableModel(new Object[][]{}, new String[]{"Medicamento", "Dosis", "Frecuencia", "Duración", "Indicaciones"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableRecetas = new JTable(modeloRecetas);
        scrollRec.setViewportView(tableRecetas);

        JButton btnAgregarReceta = new JButton("<html><center>Agregar<br>Medicamento</center></html>");
        Estilos.estilarBoton(btnAgregarReceta, new Color(40, 167, 69), Color.WHITE);
        btnAgregarReceta.setBounds(915, 640, 140, 44);
        contentPane.add(btnAgregarReceta);

        JButton btnQuitarReceta = new JButton("Quitar Receta");
        Estilos.estilarBoton(btnQuitarReceta, new Color(231, 76, 60), Color.WHITE);
        btnQuitarReceta.setBounds(915, 691, 140, 44);
        contentPane.add(btnQuitarReceta);

        // --- EVENTOS DE TABLAS ---
        btnAgregarEnf.addActionListener(e -> dialogoAgregarEnfermedadDesdeCatalogo());

        btnQuitarEnf.addActionListener(e -> {
            int fila = tableEnfermedades.getSelectedRow();
            if (fila >= 0) {
                listaEnfermedadesTemp.remove(fila);
                modeloEnfermedades.removeRow(fila);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una enfermedad de la tabla para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnAgregarAnalisis.addActionListener(e -> dialogoAgregarAnalisisConTipo());

        btnQuitarAnalisis.addActionListener(e -> {
            int fila = tableAnalisis.getSelectedRow();
            if (fila >= 0) {
                listaAnalisisTemp.remove(fila);
                modeloAnalisis.removeRow(fila);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un análisis de la tabla para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnAgregarReceta.addActionListener(e -> dialogoAgregarRecetaConMedicamento());

        btnQuitarReceta.addActionListener(e -> {
            int fila = tableRecetas.getSelectedRow();
            if (fila >= 0) {
                listaRecetasTemp.remove(fila);
                modeloRecetas.removeRow(fila);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un medicamento de la receta para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // --- BOTONES INFERIORES ---
        JButton btnGuardar = new JButton("Guardar Consulta");
        Estilos.estilarBoton(btnGuardar, new Color(41, 128, 185), Color.WHITE);
        btnGuardar.setBounds(775, 815, 160, 45);
        contentPane.add(btnGuardar);

        JButton btnCerrar = new JButton("Cerrar");
        Estilos.estilarBoton(btnCerrar, new Color(231, 76, 60), Color.WHITE);
        btnCerrar.setBounds(945, 815, 110, 45);
        contentPane.add(btnCerrar);

        btnGuardar.addActionListener(e -> guardarDatos());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void dialogoAgregarEnfermedadDesdeCatalogo() {
        ArrayList<Enfermedad> listaEnfCatalogo = (ArrayList<Enfermedad>) ClienteSocket.enviar("LISTAR_ENFERMEDADES", null);
        if (listaEnfCatalogo == null || listaEnfCatalogo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay enfermedades registradas en el catálogo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nombresEnf = new String[listaEnfCatalogo.size()];
        for (int i = 0; i < listaEnfCatalogo.size(); i++) {
            Enfermedad enf = listaEnfCatalogo.get(i);
            nombresEnf[i] = enf.getNombre() + (enf.isVigilancia() ? " (Bajo Vigilancia)" : "");
        }

        JComboBox<String> cmbEnfermedades = new JComboBox<>(nombresEnf);

        Object[] message = {"Seleccione Enfermedad del Catálogo:", cmbEnfermedades};

        int option = JOptionPane.showConfirmDialog(this, message, "Asignar Enfermedad", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int indexSeleccionado = cmbEnfermedades.getSelectedIndex();
            Enfermedad enfElegida = listaEnfCatalogo.get(indexSeleccionado);

            if (!listaEnfermedadesTemp.contains(enfElegida)) {
                listaEnfermedadesTemp.add(enfElegida);
                modeloEnfermedades.addRow(new Object[]{enfElegida.getNombre(), enfElegida.isVigilancia() ? "Sí" : "No"});
            } else {
                JOptionPane.showMessageDialog(this, "Esta enfermedad ya fue agregada a la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void dialogoAgregarAnalisisConTipo() {
        ArrayList<TipoAnalisis> listaTipos = (ArrayList<TipoAnalisis>) ClienteSocket.enviar("LISTAR_TIPOS_ANALISIS", null);
        if (listaTipos == null || listaTipos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tipos de análisis registrados en el sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nombresTipos = new String[listaTipos.size()];
        for (int i = 0; i < listaTipos.size(); i++) {
            TipoAnalisis t = listaTipos.get(i);
            nombresTipos[i] = t.getNombre() + " (Cod: " + t.getCodigoTipo() + ")";
        }

        JComboBox<String> cmbTipos = new JComboBox<>(nombresTipos);
        JTextField txtIndicaciones = new JTextField();

        Object[] message = {"Seleccione Tipo de Análisis a Ordenar:", cmbTipos, "Indicaciones / Observaciones opcionales:", txtIndicaciones};

        int option = JOptionPane.showConfirmDialog(this, message, "Ordenar Análisis Clínico", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int indexSeleccionado = cmbTipos.getSelectedIndex();
            TipoAnalisis tipoElegido = listaTipos.get(indexSeleccionado);

            Analisis nuevoAnalisis = new Analisis();
            nuevoAnalisis.setTipo(tipoElegido);
            nuevoAnalisis.setEstado("Pendiente");
            nuevoAnalisis.setFechaOrden(LocalDateTime.now());
            nuevoAnalisis.setResultado(txtIndicaciones.getText().trim());

            listaAnalisisTemp.add(nuevoAnalisis);
            modeloAnalisis.addRow(new Object[]{tipoElegido.getNombre(), nuevoAnalisis.getEstado(), nuevoAnalisis.getResultado() != null ? nuevoAnalisis.getResultado() : ""});
        }
    }

    private void dialogoAgregarRecetaConMedicamento() {
        ArrayList<Medicamento> listaMeds = (ArrayList<Medicamento>) ClienteSocket.enviar("LISTAR_MEDICAMENTOS", null);
        if (listaMeds == null || listaMeds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay medicamentos registrados en el sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nombresMeds = new String[listaMeds.size()];
        for (int i = 0; i < listaMeds.size(); i++) {
            Medicamento med = listaMeds.get(i);
            nombresMeds[i] = med.getNombre() + " (" + med.getConcentracion() + ")";
        }

        JComboBox<String> cmbMedicamentos = new JComboBox<>(nombresMeds);
        JTextField txtDosis = new JTextField();
        JTextField txtFrecuencia = new JTextField();
        JTextField txtDuracion = new JTextField();
        JTextField txtDesc = new JTextField();

        Object[] message = {"Seleccione Medicamento:", cmbMedicamentos, "Dosis (ej. 1 tableta):", txtDosis, "Frecuencia (ej. Cada 8 horas):", txtFrecuencia, "Duración (ej. 7 días):", txtDuracion, "Indicaciones / Desc.:", txtDesc};

        int option = JOptionPane.showConfirmDialog(this, message, "Añadir Receta Médica", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int indexSeleccionado = cmbMedicamentos.getSelectedIndex();
            Medicamento medElegido = listaMeds.get(indexSeleccionado);

            RecetaMedica receta = new RecetaMedica();
            receta.setMedicamento(medElegido);
            receta.setDosis(txtDosis.getText());
            receta.setFrecuencia(txtFrecuencia.getText());
            receta.setDuracion(txtDuracion.getText());
            receta.setDescripcion(txtDesc.getText());

            listaRecetasTemp.add(receta);
            modeloRecetas.addRow(new Object[]{medElegido.getNombre(), receta.getDosis(), receta.getFrecuencia(), receta.getDuracion(), receta.getDescripcion()});
        }
    }

    private void guardarDatos() {
        try {
            if (txtCedulaPaciente.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente usando el botón Buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar y obtener los signos vitales y datos antropométricos
            double temperatura = Double.parseDouble(txtTemperatura.getText());
            int frecuenciaCardiaca = Integer.parseInt(txtFrecuencia.getText());
            String presionArterial = txtPresion.getText();
            double peso = Double.parseDouble(txtPeso.getText());
            double talla = Double.parseDouble(txtTalla.getText());

            // Obtener el código interno (ID) del médico actual
            int codigoMedico = (medicoActual != null) ? medicoActual.getCodigoPersona() : 0;
            if (codigoMedico <= 0) {
                JOptionPane.showMessageDialog(this, "Error: El médico actual no tiene un código válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener el cliente seleccionado por la cédula para extraer su código interno
            String cedulaCliente = txtCedulaPaciente.getText();
            Object respCliente = ClienteSocket.enviar("BUSCAR_CLIENTE_CEDULA", cedulaCliente);
            if (respCliente == null || !(respCliente instanceof logico.Cliente)) {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el registro completo del paciente en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            logico.Cliente clienteSeleccionado = (logico.Cliente) respCliente;
            int codigoCliente = clienteSeleccionado.getCodigoPersona();

            // Fecha y hora actual para la consulta (LocalDateTime requerido por el procedimiento)
            LocalDateTime fechaConsulta = LocalDateTime.now();
            String sintomas = txtSintomas.getText();
            String diagnostico = txtDiagnostico.getText();

            // Empaquetar los parámetros en el orden exacto que espera el switch de Flujo.java para REG_CONSULTA_SP:
            Object[] datosSp = new Object[]{codigoMedico, codigoCliente, fechaConsulta, sintomas, diagnostico, temperatura, frecuenciaCardiaca, presionArterial, peso, talla};

            Object respuesta = ClienteSocket.enviar("REG_CONSULTA_SP", datosSp);

            // Si el servidor retorna el objeto Consulta o su ID generado, o retorna Boolean true:
            boolean exito = (respuesta != null && ((respuesta instanceof Boolean && (Boolean) respuesta) || (respuesta instanceof Consulta) || (respuesta instanceof Integer)));

            if (exito) {
                // Registrar análisis ordenados
                if (!listaAnalisisTemp.isEmpty()) {
                    Consulta consultaGenerada = null;
                    if (respuesta instanceof Consulta) {
                        consultaGenerada = (Consulta) respuesta;
                    }

                    for (Analisis ana : listaAnalisisTemp) {
                        if (consultaGenerada != null) {
                            ana.setConsulta(consultaGenerada);
                        }
                        ClienteSocket.enviar("REG_ANALISIS", ana);
                    }
                }

                JOptionPane.showMessageDialog(this, "Consulta, signos vitales y análisis registrados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error en la base de datos al ejecutar la transacción.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique que los campos numéricos (Temperatura, Frecuencia, Peso, Talla) contengan datos válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setCedulaPaciente(String cedula) {
        if (this.txtCedulaPaciente != null && cedula != null) {
            this.txtCedulaPaciente.setText(cedula);
        }
    }
}