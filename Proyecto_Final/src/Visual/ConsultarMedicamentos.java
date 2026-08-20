package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Medicamento;
import logico.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ConsultarMedicamentos extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Medicamento> medicamentos;

    public ConsultarMedicamentos(User usuarioActual) {
        setTitle("Listado de Medicamentos");
        setResizable(false);
        setBounds(100, 100, 700, 450);
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
        String[] headers = {"Código", "Nombre", "Concentración", "Descripción"};
        model.setColumnIdentifiers(headers);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(60, 70, 123));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnNuevo = new JButton("Nuevo");
        Estilos.estilarBoton(btnNuevo, new Color(46, 204, 113), Color.WHITE);
        btnNuevo.addActionListener(e -> {
            RegMedicamento reg = new RegMedicamento();
            reg.setModal(true);
            reg.setVisible(true);
            cargarMedicamentos();
        });
        buttonPane.add(btnNuevo);

        JButton btnModificar = new JButton("Modificar");
        Estilos.estilarBoton(btnModificar, new Color(41, 128, 185), Color.WHITE);
        btnModificar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && medicamentos != null) {
                Medicamento medSeleccionado = medicamentos.get(selectedRow);
                RegMedicamento reg = new RegMedicamento(medSeleccionado);
                reg.setModal(true);
                reg.setVisible(true);
                cargarMedicamentos();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un medicamento de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPane.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        Estilos.estilarBoton(btnEliminar, new Color(231, 76, 60), Color.WHITE);
        btnEliminar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && medicamentos != null) {
                Medicamento medSeleccionado = medicamentos.get(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el medicamento " + medSeleccionado.getNombre() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Object respuesta = ClienteSocket.enviar("DELETE_MEDICAMENTO", medSeleccionado.getCodigoMedicamento());
                    boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);
                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Medicamento eliminado con éxito.");
                        cargarMedicamentos();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al eliminar el medicamento.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un medicamento de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPane.add(btnEliminar);

        if (usuarioActual != null && usuarioActual.getRol().equalsIgnoreCase("Medico")) {
            btnNuevo.setVisible(false);
            btnModificar.setVisible(false);
            btnEliminar.setVisible(false);
        }

        JButton btnCerrar = new JButton("Cerrar");
        Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarMedicamentos();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void cargarMedicamentos() {
        model.setRowCount(0);
        Object respuesta = ClienteSocket.enviar("LISTAR_MEDICAMENTOS", null);
        if (respuesta != null && respuesta instanceof ArrayList) {
            medicamentos = (ArrayList<Medicamento>) respuesta;
            for (Medicamento med : medicamentos) {
                Object[] row = new Object[4];
                row[0] = med.getCodigoMedicamento();
                row[1] = med.getNombre();
                row[2] = med.getConcentracion();
                row[3] = med.getDescripcion();
                model.addRow(row);
            }
        }
    }
}