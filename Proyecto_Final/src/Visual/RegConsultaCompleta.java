package Visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Utils.ClienteSocket;
import logico.Consulta;
import logico.EvaluacionFisica;
import logico.Medico;

public class RegConsultaCompleta extends JFrame {

    private JPanel contentPane;
    private JTextField txtCedulaPaciente;
    private JTextField txtSintomas;
    private JTextField txtDiagnostico;
    private JTextField txtTemperatura;
    private JTextField txtFrecuencia;
    private JTextField txtPresion;
    private JTextField txtPeso;
    private JTextField txtTalla;
    private Medico medicoActual;

    public RegConsultaCompleta(Medico medicoActual) {
        this.medicoActual = medicoActual;

        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(RegConsultaCompleta.class.getResource("/img/cita.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el ícono de la ventana.");
        }

        setTitle("Registro de Consulta Médica Avanzada");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Ventana masiva y de gran formato (1050 x 780 píxeles)
        setBounds(50, 50, 1050, 780);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(245, 247, 250));

        // --- ENCABEZADO INSTITUCIONAL EXPANDIDO ---
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 1034, 70);
        panelHeader.setBackground(new Color(60, 70, 123)); // Azul oscuro institucional
        panelHeader.setLayout(null);
        contentPane.add(panelHeader);

        JLabel lblTituloHeader = new JLabel("Registrar Consulta Médica - Dr(a). " + (medicoActual != null ? medicoActual.getNombre() : ""));
        lblTituloHeader.setForeground(Color.WHITE);
        lblTituloHeader.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblTituloHeader.setBounds(40, 18, 950, 35);
        panelHeader.add(lblTituloHeader);

        // --- FUENTES Y ESTILOS ---
        Font fuenteLabel = new Font("Bahnschrift", Font.BOLD, 15);
        Font fuenteTexto = new Font("Tahoma", Font.PLAIN, 15);
        Color colorTexto = new Color(50, 50, 50);

        // --- PACIENTE ---
        JLabel lblCedula = new JLabel("Cédula Paciente:");
        lblCedula.setFont(fuenteLabel);
        lblCedula.setForeground(colorTexto);
        lblCedula.setBounds(50, 110, 180, 30);
        contentPane.add(lblCedula);

        txtCedulaPaciente = new JTextField();
        txtCedulaPaciente.setFont(fuenteTexto);
        txtCedulaPaciente.setBounds(220, 108, 630, 38);
        txtCedulaPaciente.setEditable(false);
        contentPane.add(txtCedulaPaciente);

        JButton btnBuscarCliente = new JButton("Buscar Paciente");
        btnBuscarCliente.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        btnBuscarCliente.setBackground(new Color(60, 120, 180));
        btnBuscarCliente.setForeground(Color.WHITE);
        btnBuscarCliente.setBounds(865, 108, 140, 38);
        contentPane.add(btnBuscarCliente);

        btnBuscarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConsultarClientes selector = new ConsultarClientes();
                selector.setLocationRelativeTo(contentPane);
                selector.setModal(true);
                selector.setVisible(true);
                if (selector.getClienteSeleccionado() != null) {
                    txtCedulaPaciente.setText(selector.getClienteSeleccionado().getCedula());
                }
            }
        });

        // --- SÍNTOMAS ---
        JLabel lblSintomas = new JLabel("Síntomas:");
        lblSintomas.setFont(fuenteLabel);
        lblSintomas.setForeground(colorTexto);
        lblSintomas.setBounds(50, 185, 180, 30);
        contentPane.add(lblSintomas);

        txtSintomas = new JTextField();
        txtSintomas.setFont(fuenteTexto);
        txtSintomas.setBounds(220, 183, 785, 38);
        contentPane.add(txtSintomas);

        // --- DIAGNÓSTICO ---
        JLabel lblDiagnostico = new JLabel("Diagnóstico:");
        lblDiagnostico.setFont(fuenteLabel);
        lblDiagnostico.setForeground(colorTexto);
        lblDiagnostico.setBounds(50, 260, 180, 30);
        contentPane.add(lblDiagnostico);

        txtDiagnostico = new JTextField();
        txtDiagnostico.setFont(fuenteTexto);
        txtDiagnostico.setBounds(220, 258, 785, 38);
        contentPane.add(txtDiagnostico);

        // --- SECCIÓN EVALUACIÓN FÍSICA (SIGNOS VITALES) ---
        JSeparator separator = new JSeparator();
        separator.setBounds(50, 340, 955, 10);
        contentPane.add(separator);

        JLabel lblSubEval = new JLabel("Evaluación Física y Signos Vitales");
        lblSubEval.setFont(new Font("Bahnschrift", Font.BOLD, 17));
        lblSubEval.setForeground(new Color(60, 70, 123));
        lblSubEval.setBounds(50, 365, 400, 30);
        contentPane.add(lblSubEval);

        // Fila 1 Evaluación
        JLabel lblTemp = new JLabel("Temperatura (°C):");
        lblTemp.setFont(fuenteLabel);
        lblTemp.setBounds(50, 430, 160, 30);
        contentPane.add(lblTemp);

        txtTemperatura = new JTextField();
        txtTemperatura.setFont(fuenteTexto);
        txtTemperatura.setBounds(220, 428, 220, 38);
        contentPane.add(txtTemperatura);

        JLabel lblFrec = new JLabel("Frec. Cardíaca:");
        lblFrec.setFont(fuenteLabel);
        lblFrec.setBounds(530, 430, 160, 30);
        contentPane.add(lblFrec);

        txtFrecuencia = new JTextField();
        txtFrecuencia.setFont(fuenteTexto);
        txtFrecuencia.setBounds(685, 428, 320, 38);
        contentPane.add(txtFrecuencia);

        // Fila 2 Evaluación
        JLabel lblPresion = new JLabel("Presión Arterial:");
        lblPresion.setFont(fuenteLabel);
        lblPresion.setBounds(50, 500, 160, 30);
        contentPane.add(lblPresion);

        txtPresion = new JTextField();
        txtPresion.setFont(fuenteTexto);
        txtPresion.setBounds(220, 498, 220, 38);
        contentPane.add(txtPresion);

        JLabel lblPeso = new JLabel("Peso (kg):");
        lblPeso.setFont(fuenteLabel);
        lblPeso.setBounds(530, 500, 160, 30);
        contentPane.add(lblPeso);

        txtPeso = new JTextField();
        txtPeso.setFont(fuenteTexto);
        txtPeso.setBounds(685, 498, 320, 38);
        contentPane.add(txtPeso);

        // Fila 3 Evaluación
        JLabel lblTalla = new JLabel("Talla (m):");
        lblTalla.setFont(fuenteLabel);
        lblTalla.setBounds(50, 570, 160, 30);
        contentPane.add(lblTalla);

        txtTalla = new JTextField();
        txtTalla.setFont(fuenteTexto);
        txtTalla.setBounds(220, 568, 220, 38);
        contentPane.add(txtTalla);

        // --- BOTONES INFERIORES AMPLIADOS ---
        JButton btnGuardar = new JButton("Guardar Consulta");
        btnGuardar.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        btnGuardar.setBackground(new Color(41, 128, 185)); // Azul brillante de acción
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBounds(670, 660, 180, 48);
        contentPane.add(btnGuardar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        btnCerrar.setBackground(new Color(231, 76, 60)); // Rojo anaranjado
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBounds(865, 660, 140, 48);
        contentPane.add(btnCerrar);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarDatos();
            }
        });

        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void guardarDatos() {
        try {
            if (txtCedulaPaciente.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente usando el botón Buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            EvaluacionFisica eval = new EvaluacionFisica();
            eval.setTemperatura(Float.parseFloat(txtTemperatura.getText()));
            eval.setFrecuenciaCardiaca(Integer.parseInt(txtFrecuencia.getText()));
            eval.setPresionArterial(txtPresion.getText());
            eval.setPeso(Float.parseFloat(txtPeso.getText()));
            eval.setTalla(Float.parseFloat(txtTalla.getText()));

            Consulta consulta = new Consulta();
            consulta.setFechaConsulta(LocalDate.now());
            consulta.setSintomas(txtSintomas.getText());
            consulta.setDiagnostico(txtDiagnostico.getText());
            consulta.setEvaluacion(eval);

            consulta.setMedico(medicoActual);

            logico.Cliente clienteTemp = new logico.Cliente();
            clienteTemp.setCedula(txtCedulaPaciente.getText());
            consulta.setCliente(clienteTemp);

            Object respuesta = ClienteSocket.enviar("REG_CONSULTA", consulta);

            if (respuesta != null && (boolean) respuesta) {
                JOptionPane.showMessageDialog(this, "Consulta guardada con éxito en la base de datos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al registrar la consulta en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique que los campos numéricos (Temperatura, Frecuencia, Peso, Talla) contengan datos válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }
}