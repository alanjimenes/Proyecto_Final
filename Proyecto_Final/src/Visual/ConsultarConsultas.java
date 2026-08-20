package Visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Utils.ClienteSocket;
import logico.Consulta;
import logico.Medico;

public class ConsultarConsultas extends JFrame {

    private JPanel contentPane;
    private JTable tableConsultas;
    private DefaultTableModel model;
    private Medico medicoActual;

    public ConsultarConsultas(Medico medicoActual) {
        this.medicoActual = medicoActual;

        setTitle("Mis Consultas Médicas - Dr(a). " + (medicoActual != null ? medicoActual.getNombre() : ""));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 950, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 247, 250));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Header
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 934, 60);
        panelHeader.setBackground(new Color(60, 70, 123));
        panelHeader.setLayout(null);
        contentPane.add(panelHeader);

        JLabel lblTitulo = new JLabel("Historial de Consultas Realizadas");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblTitulo.setBounds(30, 15, 400, 30);
        panelHeader.add(lblTitulo);

        // Tabla
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 80, 870, 380);
        contentPane.add(scrollPane);

        String[] columnas = {"Código", "Paciente", "Fecha", "Motivo", "Diagnóstico"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableConsultas = new JTable(model);
        tableConsultas.setRowHeight(25);
        scrollPane.setViewportView(tableConsultas);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(127, 140, 141));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 15));
        btnCerrar.setBounds(780, 490, 120, 40);
        btnCerrar.addActionListener(e -> dispose());
        contentPane.add(btnCerrar);

        cargarTabla();
    }

    private void cargarTabla() {
        model.setRowCount(0);
        if (medicoActual == null) return;

        try {
            Object respuesta = ClienteSocket.enviar("LISTAR_CONSULTAS_POR_DOCTOR", medicoActual.getCedula());

            if (respuesta instanceof ArrayList<?>) {
                @SuppressWarnings("unchecked")
                ArrayList<Consulta> lista = (ArrayList<Consulta>) respuesta;

                for (Consulta c : lista) {
                    Object[] row = {
                            c.getCodigoConsulta(),
                            (c.getCliente() != null ? c.getCliente().getNombre() + " " + c.getCliente().getApellido() : "N/A"),
                            c.getFechaConsulta(),
                            c.getSintomas(),
                            c.getDiagnostico()
                    };
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar consultas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}