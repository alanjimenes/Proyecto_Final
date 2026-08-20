package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Consulta;
import logico.RecetaMedica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ConsultarRecetas extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<RecetaMedica> recetas;
    private Consulta consultaActual;

    public ConsultarRecetas(Consulta consulta) {
        this.consultaActual = consulta;
        setTitle("Recetas Médicas - Consulta N° " + consultaActual.getCodigoConsulta());
        setResizable(false);
        setBounds(100, 100, 850, 450);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] headers = {"ID", "Medicamento", "Dosis", "Frecuencia", "Duración"};
        model.setColumnIdentifiers(headers);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(60, 70, 123));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnModificar = new JButton("Modificar");
        Estilos.estilarBoton(btnModificar, new Color(41, 128, 185), Color.WHITE);
        btnModificar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && recetas != null) {
                RecetaMedica recetaSeleccionada = recetas.get(selectedRow);
                RegRecetaMedica reg = new RegRecetaMedica(recetaSeleccionada);
                reg.setModal(true);
                reg.setVisible(true);
                cargarRecetasDeConsulta();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una receta de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPane.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        Estilos.estilarBoton(btnEliminar, new Color(231, 76, 60), Color.WHITE);
        btnEliminar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && recetas != null) {
                RecetaMedica recetaSeleccionada = recetas.get(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta receta?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Object respuesta = ClienteSocket.enviar("DELETE_RECETA_MEDICA", recetaSeleccionada.getCodigoRec());
                    boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);
                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Receta eliminada con éxito.");
                        cargarRecetasDeConsulta();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al eliminar la receta.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una receta de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPane.add(btnEliminar);

        JButton btnCerrar = new JButton("Cerrar");
        Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarRecetasDeConsulta();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void cargarRecetasDeConsulta() {
        model.setRowCount(0);
        Object respuesta = ClienteSocket.enviar("LISTAR_RECETAS_POR_CONSULTA", consultaActual.getCodigoConsulta());
        if (respuesta != null && respuesta instanceof ArrayList) {
            recetas = (ArrayList<RecetaMedica>) respuesta;
            for (RecetaMedica r : recetas) {
                Object[] row = new Object[5];
                row[0] = r.getCodigoRec();
                row[1] = r.getMedicamento() != null ? r.getMedicamento().getNombre() : "N/A";
                row[2] = r.getDosis();
                row[3] = r.getFrecuencia();
                row[4] = r.getDuracion();
                model.addRow(row);
            }
        }
    }
}