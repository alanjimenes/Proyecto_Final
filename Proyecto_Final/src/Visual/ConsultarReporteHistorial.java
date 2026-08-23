package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Servicios.HistorialService;
import Utils.Estilos;
import logico.ReporteHistorial;

public class ConsultarReporteHistorial extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCedula;
    private JLabel lblNombreCliente;
    private JLabel lblExpedienteVal;
    private JLabel lblEdadVal;
    private JTextArea txtVacunasVal;
    private JTable tableConsultas;
    private DefaultTableModel modelConsultas;

    private List<ReporteHistorial> listaReporteTemp;

    public ConsultarReporteHistorial() {
        setTitle("Reporte Completo de Historial Clínico y Vacunación");
        setBounds(100, 100, 980, 720);
        setLocationRelativeTo(null);
        setModal(true);

        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarReporteHistorial.class.getResource("/img/seguro-de-salud.png")));
        } catch (Exception e) {
        }

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // --- BÚSQUEDA DE PACIENTE ---
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBounds(15, 15, 934, 65);
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new TitledBorder(null, "Buscar Paciente", TitledBorder.LEADING, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(60, 70, 123)));
        panelBusqueda.setLayout(null);
        contentPanel.add(panelBusqueda);

        JLabel lblCed = new JLabel("Cédula:");
        lblCed.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblCed.setBounds(20, 25, 60, 25);
        panelBusqueda.add(lblCed);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtCedula.setBounds(80, 22, 170, 30);
        panelBusqueda.add(txtCedula);

        JButton btnBuscar = new JButton("Generar Reporte");
        Estilos.estilarBoton(btnBuscar, new Color(41, 128, 185), Color.WHITE);
        btnBuscar.setBounds(260, 22, 150, 30);
        panelBusqueda.add(btnBuscar);

        JButton btnBuscarSelector = new JButton("Seleccionar Paciente");
        Estilos.estilarBoton(btnBuscarSelector, new Color(60, 70, 123), Color.WHITE);
        btnBuscarSelector.setBounds(420, 22, 170, 30);
        panelBusqueda.add(btnBuscarSelector);

        lblNombreCliente = new JLabel("Paciente: (Ninguno seleccionado)");
        lblNombreCliente.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblNombreCliente.setForeground(new Color(40, 167, 69));
        lblNombreCliente.setBounds(605, 25, 315, 25);
        panelBusqueda.add(lblNombreCliente);

        // --- INFORMACIÓN GENERAL Y VACUNACIÓN ---
        JPanel panelDatos = new JPanel();
        panelDatos.setBounds(15, 90, 934, 100);
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(new TitledBorder(null, "Información General y Vacunación", TitledBorder.LEADING, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(60, 70, 123)));
        panelDatos.setLayout(null);
        contentPanel.add(panelDatos);

        JLabel lblExp = new JLabel("Expediente:");
        lblExp.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblExp.setBounds(20, 25, 80, 20);
        panelDatos.add(lblExp);

        lblExpedienteVal = new JLabel("---");
        lblExpedienteVal.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblExpedienteVal.setBounds(100, 25, 150, 20);
        panelDatos.add(lblExpedienteVal);

        JLabel lblEd = new JLabel("Edad:");
        lblEd.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblEd.setBounds(300, 25, 50, 20);
        panelDatos.add(lblEd);

        lblEdadVal = new JLabel("---");
        lblEdadVal.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEdadVal.setBounds(350, 25, 100, 20);
        panelDatos.add(lblEdadVal);

        JLabel lblVac = new JLabel("Vacunas:");
        lblVac.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblVac.setBounds(20, 55, 70, 20);
        panelDatos.add(lblVac);

        txtVacunasVal = new JTextArea();
        txtVacunasVal.setEditable(false);
        txtVacunasVal.setLineWrap(true);
        txtVacunasVal.setBackground(new Color(245, 245, 245));
        JScrollPane scrollVacunas = new JScrollPane(txtVacunasVal);
        scrollVacunas.setBounds(100, 52, 815, 38);
        panelDatos.add(scrollVacunas);

        // --- TABLA DE CONSULTAS Y DIAGNÓSTICOS ---
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBounds(15, 200, 934, 430);
        panelTabla.setBorder(new TitledBorder(null, "Historial de Consultas y Diagnósticos", TitledBorder.LEADING, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(60, 70, 123)));
        contentPanel.add(panelTabla);

        modelConsultas = new DefaultTableModel(new Object[][]{}, new String[]{
                "Fecha", "Médico Tratante", "Especialidad", "Síntomas", "Diagnóstico", "Enfermedades Diag."
        }) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableConsultas = new JTable(modelConsultas);
        tableConsultas.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tableConsultas);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // --- BOTÓN DE CIERRE ---
        JButton btnCerrar = new JButton("Cerrar");
        Estilos.estilarBoton(btnCerrar, new Color(231, 76, 60), Color.WHITE);
        btnCerrar.setBounds(839, 638, 110, 35);
        contentPanel.add(btnCerrar);

        // --- EVENTOS ---
        btnBuscar.addActionListener(e -> ejecutarReporte(txtCedula.getText().trim()));

        btnBuscarSelector.addActionListener(e -> {
            ConsultarClientes selector = new ConsultarClientes();
            selector.setLocationRelativeTo(this);
            selector.setModal(true);
            selector.setVisible(true);
            if (selector.getClienteSeleccionado() != null) {
                String cedulaSel = selector.getClienteSeleccionado().getCedula();
                txtCedula.setText(cedulaSel);
                ejecutarReporte(cedulaSel);
            }
        });

        btnCerrar.addActionListener(e -> dispose());
    }

    private void ejecutarReporte(String cedula) {
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese o seleccione la cédula del cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        listaReporteTemp = HistorialService.obtenerReporteHistorialCompleto(cedula);

        if (listaReporteTemp != null && !listaReporteTemp.isEmpty()) {
            ReporteHistorial infoGeneral = listaReporteTemp.get(0);

            lblNombreCliente.setText("Paciente: " + infoGeneral.nombrePaciente);
            lblExpedienteVal.setText(infoGeneral.numExpediente != null ? infoGeneral.numExpediente : "N/D");
            lblEdadVal.setText(infoGeneral.edad + " años");
            txtVacunasVal.setText(infoGeneral.registroVacunacion);

            modelConsultas.setRowCount(0);
            for (ReporteHistorial fila : listaReporteTemp) {
                if (fila.fechaConsulta != null) {
                    modelConsultas.addRow(new Object[]{
                            fila.fechaConsulta,
                            fila.medicoTratante,
                            fila.especialidadMedico,
                            fila.sintomas,
                            fila.diagnostico,
                            fila.enfermedadesDiagnosticadas
                    });
                }
            }

            if (infoGeneral.fechaConsulta == null) {
                JOptionPane.showMessageDialog(this, "Paciente encontrado, pero no registra consultas médicas previas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún registro para la cédula ingresada.", "Atención", JOptionPane.ERROR_MESSAGE);
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        lblNombreCliente.setText("Paciente: (Ninguno seleccionado)");
        lblExpedienteVal.setText("---");
        lblEdadVal.setText("---");
        txtVacunasVal.setText("");
        modelConsultas.setRowCount(0);
    }
}