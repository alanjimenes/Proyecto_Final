package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import logico.Cita;
import logico.Cliente;
import logico.Vacuna;

public class ReportesGenerales extends JDialog {

    private JTabbedPane tabbedPane;

    private JTable tableCitas;
    private DefaultTableModel modelCitas;
    private JTextField txtFiltroCitas;

    private JTable tableConsultas;
    private DefaultTableModel modelConsultas;

    private JTable tableVacunas;
    private DefaultTableModel modelVacunas;

    public ReportesGenerales() {
        setTitle("Centro de Reportes - Clínica UNPHU");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        JPanel panelCitas = crearPanelCitas();
        tabbedPane.addTab("Reporte de Citas", panelCitas);
        JPanel panelConsultas = crearPanelConsultas();
        tabbedPane.addTab("Reporte de Consultas", panelConsultas);

        JPanel panelVacunas = crearPanelVacunas();
        tabbedPane.addTab("Reporte de Vacunas", panelVacunas);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);

        cargarCitas();
        cargarConsultas();
        cargarVacunas();
    }

    private JPanel crearPanelCitas() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Filtrar por Cédula (Cliente/Médico):"));
        txtFiltroCitas = new JTextField(15);
        top.add(txtFiltroCitas);
        JButton btnBuscar = new JButton("Filtrar");
        btnBuscar.addActionListener(e -> cargarCitas());
        top.add(btnBuscar);

        JButton btnPDF = new JButton("Exportar PDF");
        btnPDF.setBackground(Color.RED);
        btnPDF.setForeground(Color.WHITE);
       
        btnPDF.addActionListener(e -> GeneradorPDF.exportarJTablePDF(tableCitas, "Reporte_Citas"));
        top.add(btnPDF);

        panel.add(top, BorderLayout.NORTH);

        modelCitas = new DefaultTableModel(new String[]{"Código", "Fecha", "Paciente", "Médico", "Estado"}, 0);
        tableCitas = new JTable(modelCitas);
        panel.add(new JScrollPane(tableCitas), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelConsultas() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRecargar = new JButton("Recargar Todo");
        btnRecargar.addActionListener(e -> cargarConsultas());
        top.add(btnRecargar);

        JButton btnPDF = new JButton("Exportar PDF");
        btnPDF.setBackground(Color.RED);
        btnPDF.setForeground(Color.WHITE);
        btnPDF.addActionListener(e -> GeneradorPDF.exportarJTablePDF(tableConsultas, "Reporte_Consultas"));
        top.add(btnPDF);

        panel.add(top, BorderLayout.NORTH);

        modelConsultas = new DefaultTableModel(new String[]{"Código", "Fecha", "Paciente", "Médico", "Diagnóstico"}, 0);
        tableConsultas = new JTable(modelConsultas);
        panel.add(new JScrollPane(tableConsultas), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelVacunas() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnPDF = new JButton("Exportar PDF");
        btnPDF.setBackground(Color.RED);
        btnPDF.setForeground(Color.WHITE);
        btnPDF.addActionListener(e -> GeneradorPDF.exportarJTablePDF(tableVacunas, "Reporte_Vacunas"));
        top.add(btnPDF);

        panel.add(top, BorderLayout.NORTH);

        modelVacunas = new DefaultTableModel(new String[]{"Código", "Nombre", "Descripción"}, 0);
        tableVacunas = new JTable(modelVacunas);
        panel.add(new JScrollPane(tableVacunas), BorderLayout.CENTER);

        return panel;
    }


    @SuppressWarnings("unchecked")
    private void cargarCitas() {
        modelCitas.setRowCount(0);
        String filtro = "";
        if(txtFiltroCitas != null) {
            filtro = txtFiltroCitas.getText().trim().toLowerCase();
        }

        ArrayList<Cita> lista = (ArrayList<Cita>) ClienteSocket.enviar("LISTAR_CITAS", null);
        if(lista != null) {
            for (Cita c : lista) {
               
                if (c.getCliente() != null && c.getMedico() != null) {
                    boolean coincide = filtro.isEmpty() || 
                            c.getCliente().getCedula().contains(filtro) ||
                            c.getMedico().getCedula().contains(filtro);
                    
                    if (coincide) {
                        modelCitas.addRow(new Object[]{
                                c.getCodigo_cita(),
                                c.getFechaHora().toString(),
                                c.getCliente().getNombre(),
                                c.getMedico().getNombre(),
                                c.getEstado()
                        });
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarConsultas() {
        modelConsultas.setRowCount(0);
        ArrayList<Cliente> clientes = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_CLIENTES", null);

        if(clientes != null) {
            for(Cliente cli : clientes) {
                
                if(cli.getHistorial() != null && cli.getHistorial().getConsultas() != null) {
                    
                    for(logico.Consulta con : cli.getHistorial().getConsultas()) {
                       
                        String nombreMedico = (con.getMedico() != null) ? con.getMedico().getNombre() : "N/A";
                        
                        modelConsultas.addRow(new Object[]{
                                con.getCodigo_cons(),
                                con.getFechaConsulta().toString(),
                                cli.getNombre(),
                                nombreMedico,
                                con.getDiagnostico()
                        });
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarVacunas() {
        modelVacunas.setRowCount(0);
        ArrayList<Vacuna> lista = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);
        if(lista != null) {
            for (Vacuna v : lista) {
                modelVacunas.addRow(new Object[]{v.getCodigo_vacun(), v.getNombre(), v.getDescripcion()});
            }
        }
    }
}