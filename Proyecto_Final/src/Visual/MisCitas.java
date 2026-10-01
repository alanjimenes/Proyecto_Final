package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Cita;
import logico.Medico;
import logico.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class MisCitas extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private Object[] row;
    private Cita citaSeleccionada = null;
    private JButton btnAtender;
    private User usuarioMedico;
    private Medico medicoActual;

    public MisCitas(User usuario) {
        this.usuarioMedico = usuario;

        setTitle("Mis Citas de Hoy");
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(MisCitas.class.getResource("/img/dato-de-registro.png")));
        } catch (Exception e) {}
        setBounds(100, 100, 900, 500);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(new Color(60, 70, 123));
        panelNorte.setPreferredSize(new Dimension(10, 50));
        panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 12));
        contentPanel.add(panelNorte, BorderLayout.NORTH);

        String nombreMedico = "Desconocido";

        if (usuario.getCedula() != null && !usuario.getCedula().isEmpty()) {
            Object resp = ClienteSocket.enviar("BUSCAR_MEDICO", usuario.getCedula());
            if (resp instanceof Medico) {
                medicoActual = (Medico) resp;
            }
        }

        if (medicoActual == null) {
            String cedulaManual = JOptionPane.showInputDialog(this,
                    "Tu usuario no esta conectado a ningun medico.\nIngresa tu CEDULA DE MEDICO para cargar tus citas:",
                    "Identificacion Requerida", JOptionPane.WARNING_MESSAGE);

            if (cedulaManual != null && !cedulaManual.trim().isEmpty()) {
                Object resp = ClienteSocket.enviar("BUSCAR_MEDICO", cedulaManual.trim());
                if (resp instanceof Medico) {
                    medicoActual = (Medico) resp;
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontro un medico con esa cedula.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        if (medicoActual != null) {
            nombreMedico = medicoActual.getNombre() + " " + medicoActual.getApellido();
        }

        JLabel lblTitulo = new JLabel("Pacientes de Hoy para Dr/a: " + nombreMedico);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        panelNorte.add(lblTitulo);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(232, 246, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    String codigo = table.getValueAt(index, 0).toString();
                    citaSeleccionada = (Cita) ClienteSocket.enviar("BUSCAR_CITA", codigo);
                    btnAtender.setEnabled(true);
                }
            }
        });

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.setColumnIdentifiers(new String[]{"Código", "Hora", "Paciente", "Cédula Paciente", "Estado"});
        table.setModel(model);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(60, 70, 123));
                setForeground(Color.WHITE);
                setFont(new Font("Bahnschrift", Font.BOLD, 14));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255)));
                return this;
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane.setViewportView(table);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(Color.WHITE);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnAtender = new JButton("Realizar Consulta");
        Estilos.estilarBoton(btnAtender, new Color(41, 128, 185), Color.WHITE);
        btnAtender.setEnabled(false);
        btnAtender.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (citaSeleccionada != null) {
                    try {
                        RegConsultaCompleta consulta = new RegConsultaCompleta(medicoActual);

                        if (citaSeleccionada.getCliente() != null) {
                            consulta.setCedulaPaciente(citaSeleccionada.getCliente().getCedula());
                        }
                        consulta.setVisible(true);
                        dispose();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Asegúrese de tener la clase RegConsultaCompleta", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });
        buttonPane.add(btnAtender);

        JButton btnCerrar = new JButton("Cerrar");
        Estilos.estilarBoton(btnCerrar, new Color(231, 76, 60), Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        cargarCitasHoy();
    }

    @SuppressWarnings("unchecked")
    private void cargarCitasHoy() {
        model.setRowCount(0);
        row = new Object[5];

        if (medicoActual == null) {
            return;
        }

        Object respuesta = ClienteSocket.enviar("LISTAR_CITAS", null);

        if (respuesta != null && respuesta instanceof ArrayList) {
            ArrayList<Cita> todasLasCitas = (ArrayList<Cita>) respuesta;
            for (Cita cita : todasLasCitas) {
                if (cita.getMedico() != null && cita.getMedico().getCedula().equals(medicoActual.getCedula())) {
                    boolean esHoy = cita.getFechaCita().toLocalDate().equals(LocalDate.now());
                    boolean esPendiente = cita.getEstado().equalsIgnoreCase("Pendiente");

                    if (esHoy && esPendiente) {
                        row[0] = cita.getCodigoCita();
                        row[1] = cita.getFechaCita().toLocalTime().toString();
                        row[2] = cita.getCliente().getNombre() + " " + cita.getCliente().getApellido();
                        row[3] = cita.getCliente().getCedula();
                        row[4] = cita.getEstado();
                        model.addRow(row);
                    }
                }
            }
        }
    }
}