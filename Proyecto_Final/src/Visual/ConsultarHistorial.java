package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Consulta;
import logico.Enfermedad;
import logico.Historial;
import logico.RecetaMedica;

public class ConsultarHistorial extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCedula;
    private JLabel lblNombreCliente;
    private JTable tableConsultas;
    private DefaultTableModel modelConsultas;
    private JTextArea txtSintomas;
    private JTextArea txtDiagnostico;
    private JTextArea txtDetalleReceta;

    private Historial historialActual;
    private ArrayList<Consulta> listaConsultasTemp;

    public ConsultarHistorial() {
        setTitle("Historial Clínico del Paciente");
        setBounds(100, 100, 950, 680);
        setLocationRelativeTo(null);
        setModal(true);

        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarHistorial.class.getResource("/img/seguro-de-salud.png")));
        } catch (Exception e) {
        }

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // --- BÚSQUEDA DE PACIENTE ---
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBounds(15, 15, 904, 65);
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new TitledBorder(null, "Buscar Paciente", TitledBorder.LEADING, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(60, 70, 123)));
        panelBusqueda.setLayout(null);
        contentPanel.add(panelBusqueda);

        JLabel lblCed = new JLabel("Cédula:");
        lblCed.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblCed.setBounds(20, 25, 70, 25);
        panelBusqueda.add(lblCed);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtCedula.setBounds(90, 22, 200, 30);
        panelBusqueda.add(txtCedula);

        JButton btnBuscar = new JButton("Buscar Historial");
        Estilos.estilarBoton(btnBuscar, new Color(41, 128, 185), Color.WHITE);
        btnBuscar.setBounds(300, 22, 150, 30);
        panelBusqueda.add(btnBuscar);

        JButton btnBuscarSelector = new JButton("Seleccionar Paciente");
        Estilos.estilarBoton(btnBuscarSelector, new Color(60, 70, 123), Color.WHITE);
        btnBuscarSelector.setBounds(460, 22, 170, 30);
        panelBusqueda.add(btnBuscarSelector);

        lblNombreCliente = new JLabel("Paciente: (Ninguno seleccionado)");
        lblNombreCliente.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblNombreCliente.setForeground(new Color(40, 167, 69));
        lblNombreCliente.setBounds(640, 25, 250, 25);
        panelBusqueda.add(lblNombreCliente);

        // --- TABLA DE CONSULTAS PASADAS ---
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBounds(15, 90, 904, 200);
        panelTabla.setBorder(new TitledBorder(null, "Consultas Registradas", TitledBorder.LEADING, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(60, 70, 123)));
        contentPanel.add(panelTabla);

        modelConsultas = new DefaultTableModel(new Object[][]{}, new String[]{"Fecha", "Médico", "Síntomas Básicos", "Enfermedades Diag."}) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableConsultas = new JTable(modelConsultas);
        tableConsultas.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tableConsultas);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // --- DETALLES DE LA CONSULTA SELECCIONADA ---
        JPanel panelDetalles = new JPanel();
        panelDetalles.setBounds(15, 300, 904, 280);
        panelDetalles.setBackground(Color.WHITE);
        panelDetalles.setBorder(new TitledBorder(null, "Detalle de Consulta Seleccionada", TitledBorder.LEADING, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(60, 70, 123)));
        panelDetalles.setLayout(null);
        contentPanel.add(panelDetalles);

        JLabel lblSint = new JLabel("Síntomas:");
        lblSint.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblSint.setBounds(15, 25, 100, 20);
        panelDetalles.add(lblSint);

        txtSintomas = new JTextArea();
        txtSintomas.setEditable(false);
        txtSintomas.setLineWrap(true);
        JScrollPane scrollSint = new JScrollPane(txtSintomas);
        scrollSint.setBounds(15, 48, 420, 90);
        panelDetalles.add(scrollSint);

        JLabel lblDiag = new JLabel("Diagnóstico:");
        lblDiag.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblDiag.setBounds(460, 25, 100, 20);
        panelDetalles.add(lblDiag);

        txtDiagnostico = new JTextArea();
        txtDiagnostico.setEditable(false);
        txtDiagnostico.setLineWrap(true);
        JScrollPane scrollDiag = new JScrollPane(txtDiagnostico);
        scrollDiag.setBounds(460, 48, 425, 90);
        panelDetalles.add(scrollDiag);

        JLabel lblRec = new JLabel("Receta / Prescripción Médica:");
        lblRec.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblRec.setBounds(15, 145, 200, 20);
        panelDetalles.add(lblRec);

        txtDetalleReceta = new JTextArea();
        txtDetalleReceta.setEditable(false);
        txtDetalleReceta.setLineWrap(true);
        JScrollPane scrollRec = new JScrollPane(txtDetalleReceta);
        scrollRec.setBounds(15, 168, 870, 95);
        panelDetalles.add(scrollRec);

        // --- BOTÓN DE CIERRE ---
        JButton btnCerrar = new JButton("Cerrar");
        Estilos.estilarBoton(btnCerrar, new Color(231, 76, 60), Color.WHITE);
        btnCerrar.setBounds(809, 592, 110, 35);
        contentPanel.add(btnCerrar);

        // --- EVENTOS ---
        btnBuscar.addActionListener(e -> cargarHistorial(txtCedula.getText().trim()));

        btnBuscarSelector.addActionListener(e -> {
            ConsultarClientes selector = new ConsultarClientes();
            selector.setLocationRelativeTo(this);
            selector.setModal(true);
            selector.setVisible(true);
            if (selector.getClienteSeleccionado() != null) {
                txtCedula.setText(selector.getClienteSeleccionado().getCedula());
                cargarHistorial(selector.getClienteSeleccionado().getCedula());
            }
        });

        tableConsultas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesConsulta();
            }
        });

        btnCerrar.addActionListener(e -> dispose());
    }

    private void cargarHistorial(String cedula) {
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese o seleccione la cédula del cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object resp = ClienteSocket.enviar("OBTENER_HISTORIAL", cedula);
        if (resp instanceof Historial) {
            this.historialActual = (Historial) resp;
            if (historialActual.getCliente() != null) {
                lblNombreCliente.setText(historialActual.getCliente().getNombre() + " " + historialActual.getCliente().getApellido());
            }

            modelConsultas.setRowCount(0);
            this.listaConsultasTemp = historialActual.getConsultas();

            if (listaConsultasTemp != null && !listaConsultasTemp.isEmpty()) {
                for (Consulta c : listaConsultasTemp) {
                    StringBuilder enfCad = new StringBuilder();
                    if (c.getEnfermedadesDiag() != null) {
                        for (Enfermedad enf : c.getEnfermedadesDiag()) {
                            if (enfCad.length() > 0) enfCad.append(", ");
                            enfCad.append(enf.getNombre());
                        }
                    }

                    String medicoNombre = c.getMedico() != null ? c.getMedico().getNombre() : "N/D";
                    modelConsultas.addRow(new Object[]{c.getFechaConsulta(), medicoNombre, c.getSintomas(), enfCad.toString()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "El paciente no registra consultas médicas previas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
            limpiarDetalles();

        } else {
            JOptionPane.showMessageDialog(this, "No se encontró historial clínico para la cédula ingresada.", "Atención", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallesConsulta() {
        int fila = tableConsultas.getSelectedRow();
        if (fila >= 0 && listaConsultasTemp != null && fila < listaConsultasTemp.size()) {
            Consulta c = listaConsultasTemp.get(fila);
            txtSintomas.setText(c.getSintomas());
            txtDiagnostico.setText(c.getDiagnostico());

            StringBuilder sbReceta = new StringBuilder();
            if (c.getRecetas() != null && !c.getRecetas().isEmpty()) {
                for (RecetaMedica r : c.getRecetas()) {
                    sbReceta.append("• ").append(r.getMedicamento() != null ? r.getMedicamento().getNombre() : "Medicamento").append(" | Dosis: ").append(r.getDosis()).append(" | Frecuencia: ").append(r.getFrecuencia()).append(" | Duración: ").append(r.getDuracion()).append("\n  Indicaciones: ").append(r.getDescripcion()).append("\n");
                }
            } else {
                sbReceta.append("Sin prescripción médica en esta consulta.");
            }
            txtDetalleReceta.setText(sbReceta.toString());
        }
    }

    private void limpiarDetalles() {
        txtSintomas.setText("");
        txtDiagnostico.setText("");
        txtDetalleReceta.setText("");
    }
}