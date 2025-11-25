package Visual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import logico.Cliente;
import logico.Clinica;

public class ReportesVacunas extends JDialog {

    private JTable tableFrecuencia;
    private JTable tableClientes;

    private DefaultTableModel modeloFrecuencia;
    private DefaultTableModel modeloClientes;

    public ReportesVacunas() {

        setTitle("Reporte de Vacunas Aplicadas");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBorder(BorderFactory.createTitledBorder("Frecuencia de Vacunas Aplicadas"));
        getContentPane().add(panelTop, BorderLayout.NORTH);

        modeloFrecuencia = new DefaultTableModel(
                new String[]{"Vacuna", "Veces Aplicada"}, 0);

        tableFrecuencia = new JTable(modeloFrecuencia);
        panelTop.add(new JScrollPane(tableFrecuencia), BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBorder(BorderFactory.createTitledBorder("Clientes que recibieron esta vacuna"));
        getContentPane().add(panelBottom, BorderLayout.CENTER);

        modeloClientes = new DefaultTableModel(
                new String[]{"Cédula", "Nombre", "Apellido"}, 0);

        tableClientes = new JTable(modeloClientes);
        panelBottom.add(new JScrollPane(tableClientes), BorderLayout.CENTER);
        JPanel panelBtns = new JPanel(new FlowLayout());
        getContentPane().add(panelBtns, BorderLayout.SOUTH);

        JButton btnVerClientes = new JButton("Ver Clientes");
        JButton btnCerrar = new JButton("Cerrar");

        panelBtns.add(btnVerClientes);
        panelBtns.add(btnCerrar);

        btnCerrar.addActionListener(e -> dispose());
        btnVerClientes.addActionListener(e -> cargarClientesPorVacuna());

        cargarFrecuencia();
    }

    private void cargarFrecuencia() {
        modeloFrecuencia.setRowCount(0);

        HashMap<String, Integer> mapa =
                Clinica.getInstancia().getFrecuenciaVacunas();

        for (String vac : mapa.keySet()) {
            modeloFrecuencia.addRow(new Object[]{vac, mapa.get(vac)});
        }
    }

    private void cargarClientesPorVacuna() {
        int fila = tableFrecuencia.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una vacuna.");
            return;
        }

        String vacuna = (String) modeloFrecuencia.getValueAt(fila, 0);

        modeloClientes.setRowCount(0);

        ArrayList<Cliente> lista =
                Clinica.getInstancia().getClientesPorVacuna(vacuna);

        for (Cliente c : lista) {
            modeloClientes.addRow(new Object[]{
                    c.getCedula(),
                    c.getNombre(),
                    c.getApellido()
            });
        }
    }
}
