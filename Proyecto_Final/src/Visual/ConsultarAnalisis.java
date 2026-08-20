package Visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Utils.ClienteSocket;
import logico.Analisis;
import logico.Medico;

public class ConsultarAnalisis extends JFrame {

    private JPanel contentPane;
    private JTable tableAnalisis;
    private DefaultTableModel model;
    private Medico medicoActual; // Variable para almacenar el médico logueado

    public ConsultarAnalisis(Medico medicoActual) {
        this.medicoActual = medicoActual;

        // Configuración de la ventana
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarAnalisis.class.getResource("/img/cita.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el ícono de la ventana.");
        }

        setTitle("Gestión de Análisis Clínicos - Dr(a). " + (medicoActual != null ? medicoActual.getNombre() : ""));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(50, 50, 1050, 780);
        setLocationRelativeTo(null);

        // Panel principal
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(245, 247, 250));

        setContentPane(contentPane);

        // =========================
        // ENCABEZADO
        // =========================

        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 1034, 70);
        panelHeader.setBackground(new Color(60, 70, 123));
        panelHeader.setLayout(null);

        contentPane.add(panelHeader);

        JLabel lblTituloHeader = new JLabel("Listado y Gestión de Análisis Clínicos");

        lblTituloHeader.setForeground(Color.WHITE);
        lblTituloHeader.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblTituloHeader.setBounds(40, 18, 950, 35);

        panelHeader.add(lblTituloHeader);

        // =========================
        // TABLA
        // =========================

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(40, 100, 955, 530);

        contentPane.add(scrollPane);

        tableAnalisis = new JTable();

        tableAnalisis.setFont(new Font("Tahoma", Font.PLAIN, 14));

        tableAnalisis.setRowHeight(25);

        tableAnalisis.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        String[] columnas = {"Código", "Consulta", "Tipo de Análisis", "Fecha Orden", "Fecha Resultado", "Estado", "Resultado"};

        model = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableAnalisis.setModel(model);

        scrollPane.setViewportView(tableAnalisis);

        // =========================
        // BOTÓN REGISTRAR
        // =========================

        JButton btnRegistrar = new JButton("Nuevo Análisis");

        btnRegistrar.setFont(new Font("Bahnschrift", Font.BOLD, 15));

        btnRegistrar.setBackground(new Color(41, 128, 185));

        btnRegistrar.setForeground(Color.WHITE);

        btnRegistrar.setBounds(470, 660, 170, 48);

        contentPane.add(btnRegistrar);

        // =========================
        // BOTÓN MODIFICAR
        // =========================

        JButton btnModificar = new JButton("Modificar");

        btnModificar.setFont(new Font("Bahnschrift", Font.BOLD, 15));

        btnModificar.setBackground(new Color(39, 174, 96));

        btnModificar.setForeground(Color.WHITE);

        btnModificar.setBounds(655, 660, 140, 48);

        contentPane.add(btnModificar);

        // =========================
        // BOTÓN ELIMINAR
        // =========================

        JButton btnEliminar = new JButton("Eliminar");

        btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 15));

        btnEliminar.setBackground(new Color(231, 76, 60));

        btnEliminar.setForeground(Color.WHITE);

        btnEliminar.setBounds(805, 660, 110, 48);

        contentPane.add(btnEliminar);

        // =========================
        // BOTÓN CERRAR
        // =========================

        JButton btnCerrar = new JButton("Cerrar");

        btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 15));

        btnCerrar.setBackground(new Color(127, 140, 141));

        btnCerrar.setForeground(Color.WHITE);

        btnCerrar.setBounds(925, 660, 90, 48);

        contentPane.add(btnCerrar);

        // =========================
        // CARGAR DATOS
        // =========================

        cargarTabla();

        // =========================
        // EVENTO CERRAR
        // =========================

        btnCerrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // =========================
        // EVENTO REGISTRAR
        // =========================

        btnRegistrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Pasamos el médico actual al constructor de RegAnalisis
                RegAnalisis reg = new RegAnalisis(medicoActual);
                reg.setVisible(true);
                cargarTabla();
            }
        });

        // =========================
        // EVENTO MODIFICAR
        // =========================

        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableAnalisis.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(ConsultarAnalisis.this, "Debe seleccionar un análisis de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int codigoAnalisis = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

                    // Pedir el análisis al servidor a través del Socket
                    Analisis analToEdit = (Analisis) ClienteSocket.enviar("BUSCAR_ANALISIS", codigoAnalisis);

                    if (analToEdit != null) {
                        // Pasamos el análisis y el médico actual
                        RegAnalisis mod = new RegAnalisis(analToEdit, medicoActual);
                        mod.setVisible(true);
                        cargarTabla();
                    } else {
                        JOptionPane.showMessageDialog(ConsultarAnalisis.this, "No se pudo recuperar la información del análisis seleccionado desde el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ConsultarAnalisis.this, "Error al modificar el análisis:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // =========================
        // EVENTO ELIMINAR
        // =========================

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableAnalisis.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(ConsultarAnalisis.this, "Debe seleccionar un análisis de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int codigoAnalisis = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

                    int confirm = JOptionPane.showConfirmDialog(ConsultarAnalisis.this, "¿Está seguro de eliminar este análisis?", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }

                    // Enviar la orden de eliminación al servidor a través del Socket
                    Boolean eliminado = (Boolean) ClienteSocket.enviar("DELETE_ANALISIS", codigoAnalisis);

                    if (eliminado != null && eliminado) {
                        JOptionPane.showMessageDialog(ConsultarAnalisis.this, "Análisis eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarTabla();
                    } else {
                        JOptionPane.showMessageDialog(ConsultarAnalisis.this, "Error al intentar eliminar el análisis en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ConsultarAnalisis.this, "Error al eliminar el análisis:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    // ==========================================================
    // CARGAR TABLA FILTRADA POR MÉDICO
    // ==========================================================

    private void cargarTabla() {
        model.setRowCount(0);

        try {
            // Validamos que tengamos la cédula del médico para filtrar
            if (medicoActual == null || medicoActual.getCedula() == null) {
                return;
            }

            // Enviamos la cédula del médico al servidor para solicitar solo sus análisis
            Object respuesta = ClienteSocket.enviar("LISTAR_ANALISIS_POR_DOCTOR", medicoActual.getCedula());

            if (respuesta == null) {
                return;
            }

            if (!(respuesta instanceof ArrayList<?>)) {
                System.out.println("Tipo recibido: " + respuesta.getClass().getName());
                return;
            }

            @SuppressWarnings("unchecked")
            ArrayList<Analisis> lista = (ArrayList<Analisis>) respuesta;

            for (Analisis a : lista) {
                if (a == null) {
                    continue;
                }

                Object codigoConsulta = "N/D";
                if (a.getConsulta() != null) {
                    codigoConsulta = a.getConsulta().getCodigoConsulta();
                }

                Object tipoAnalisis = "N/D";
                if (a.getTipo() != null) {
                    tipoAnalisis = a.getTipo().getNombre();
                }

                Object fechaResultado = "Pendiente";
                if (a.getFechaResultado() != null) {
                    fechaResultado = a.getFechaResultado();
                }

                Object resultado = "Sin resultado";
                if (a.getResultado() != null && !a.getResultado().toString().trim().isEmpty()) {
                    resultado = a.getResultado();
                }

                Object[] row = {a.getCodigoAnalisis(), codigoConsulta, tipoAnalisis, a.getFechaOrden(), fechaResultado, a.getEstado(), resultado};

                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los análisis:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarTabla() {
        cargarTabla();
    }
}