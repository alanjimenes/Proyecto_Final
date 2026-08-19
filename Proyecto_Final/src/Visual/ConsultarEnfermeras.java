package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Enfermera;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ConsultarEnfermeras extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Enfermera> enfermeras;

    public ConsultarEnfermeras() {
        setTitle("Listado de Enfermeras");
        setResizable(false);
        setBounds(100, 100, 900, 500);
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
        String[] headers = {"Cédula", "Nombre", "Apellido", "Teléfono", "Turno", "Estado"};
        model.setColumnIdentifiers(headers);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(60, 70, 123));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnNuevo = new JButton("Nueva Enfermera");
        Estilos.estilarBoton(btnNuevo, new Color(46, 204, 113), Color.WHITE);
        btnNuevo.addActionListener(e -> {
            RegEnfermera reg = new RegEnfermera();
            reg.setModal(true);
            reg.setVisible(true);
            cargarEnfermeras();
        });
        buttonPane.add(btnNuevo);

        JButton btnModificar = new JButton("Modificar");
        Estilos.estilarBoton(btnModificar, new Color(41, 128, 185), Color.WHITE);
        btnModificar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && enfermeras != null) {
                Enfermera enfSeleccionada = enfermeras.get(selectedRow);
                RegEnfermera reg = new RegEnfermera(enfSeleccionada);
                reg.setModal(true);
                reg.setVisible(true);
                cargarEnfermeras();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una enfermera de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPane.add(btnModificar);

        JButton btnEliminar = new JButton("Desactivar");
        Estilos.estilarBoton(btnEliminar, new Color(231, 76, 60), Color.WHITE);
        btnEliminar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && enfermeras != null) {
                Enfermera enfSeleccionada = enfermeras.get(selectedRow);
                if (!enfSeleccionada.getEstado()) {
                    JOptionPane.showMessageDialog(this, "La enfermera ya está inactiva.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de desactivar a la enfermera " + enfSeleccionada.getNombre() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Object respuesta = ClienteSocket.enviar("DELETE_ENFERMERA", enfSeleccionada);
                    boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);
                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Enfermera desactivada con éxito.");
                        cargarEnfermeras();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al desactivar la enfermera.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una enfermera de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
                cargarEnfermeras();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void cargarEnfermeras() {
        model.setRowCount(0);
        Object respuesta = ClienteSocket.enviar("LISTAR_ENFERMERAS", null);
        if (respuesta != null && respuesta instanceof ArrayList) {
            enfermeras = (ArrayList<Enfermera>) respuesta;
            for (Enfermera enf : enfermeras) {
                Object[] row = new Object[6];
                row[0] = enf.getCedula();
                row[1] = enf.getNombre();
                row[2] = enf.getApellido();
                row[3] = enf.getTelefono();
                row[4] = enf.getTurno();
                row[5] = enf.getEstado() ? "Activa" : "Inactiva";
                model.addRow(row);
            }
        }
    }
}