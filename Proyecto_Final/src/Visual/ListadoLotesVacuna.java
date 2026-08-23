package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.LoteVacuna;
import logico.Vacuna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ListadoLotesVacuna extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private Vacuna vacunaActual;
    private ArrayList<LoteVacuna> lotes;

    public ListadoLotesVacuna(Vacuna vacuna) {
        this.vacunaActual = vacuna;
        setTitle("Inventario Disponible - Vacuna: " + vacuna.getNombre());
        setResizable(false);
        setBounds(100, 100, 650, 450);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel();
        String[] headers = {"Código Lote", "No. Lote", "Vencimiento", "Cantidad"};
        model.setColumnIdentifiers(headers);
        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(60, 70, 123));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnNuevo = new JButton("Nueva");
        Estilos.estilarBoton(btnNuevo, new Color(163, 228, 215), Color.BLACK);
        btnNuevo.addActionListener(e -> {
            RegLoteVacuna regLote = new RegLoteVacuna();
            regLote.setModal(true);
            regLote.setVisible(true);
            cargarLotes();
        });
        buttonPane.add(btnNuevo);

        JButton btnModificar = new JButton("Modificar");
        Estilos.estilarBoton(btnModificar, new Color(41, 128, 185), Color.WHITE);
        btnModificar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && lotes != null) {
                JOptionPane.showMessageDialog(this, "Debes programar un constructor en RegLoteVacuna que reciba el objeto Lote para poder editar.");
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un lote de la tabla, compay.");
            }
        });
        buttonPane.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        Estilos.estilarBoton(btnEliminar, new Color(231, 76, 60), Color.WHITE);
        btnEliminar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && lotes != null) {
                LoteVacuna loteSeleccionado = lotes.get(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el lote " + loteSeleccionado.getNoLote() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Object respuesta = ClienteSocket.enviar("DELETE_LOTE_VACUNA", loteSeleccionado.getCodigoLote());
                    boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);
                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Lote eliminado con éxito.");
                        cargarLotes();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al eliminar el lote.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un lote de la tabla primero.");
            }
        });
        buttonPane.add(btnEliminar);

        JButton btnCerrar = new JButton("Cerrar");
        Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        cargarLotes();
    }

    @SuppressWarnings("unchecked")
    private void cargarLotes() {
        model.setRowCount(0);
        if (vacunaActual == null) return;

        Object respuesta = ClienteSocket.enviar("LISTAR_LOTES_DISPONIBLES_POR_VACUNA", vacunaActual.getCodigoVacuna());

        if (respuesta != null && respuesta instanceof ArrayList) {
            lotes = (ArrayList<LoteVacuna>) respuesta;
            for (LoteVacuna lote : lotes) {
                Object[] row = new Object[4];
                row[0] = lote.getCodigoLote();
                row[1] = lote.getNoLote();
                row[2] = lote.getFechaVencimiento() != null ? lote.getFechaVencimiento().toString() : "";
                row[3] = lote.getCantidad();
                model.addRow(row);
            }
        }
    }
}