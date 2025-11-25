package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import logico.Clinica;
import logico.Cita;

public class ReportesTotalCitas extends JDialog {

    private JTable table;
    private DefaultTableModel modelo;

    public ReportesTotalCitas() {
        setTitle("Todas las Citas");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new String[]{
                "Código", "Cliente", "Médico", "Fecha", "Motivo"
        }, 0);

        table = new JTable(modelo);
        add(new JScrollPane(table), BorderLayout.CENTER);

        cargarCitas();
    }

    private void cargarCitas() {
        modelo.setRowCount(0);

        for (Cita c : Clinica.getInstancia().getTodasLasCitas()) {
            modelo.addRow(new Object[]{
                    c.getCodigo_cita(),
                    c.getCliente().getNombre(),
                    c.getMedico().getNombre(),
                    c.getFechaHora().toString(),
            });
        }
    }
}
