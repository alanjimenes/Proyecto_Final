package Visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Servicios.TipoAnalisisService;
import Utils.ClienteSocket;
import logico.TipoAnalisis;

public class ConsultarTipoAnalisis extends JFrame {

    private JPanel contentPane;
    private JTable tableTipos;
    private DefaultTableModel model;
    private TipoAnalisisService tipoService;
    private ArrayList<TipoAnalisis> listaTipos;

    public ConsultarTipoAnalisis() {
        tipoService = new TipoAnalisisService();

        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarTipoAnalisis.class.getResource("/img/cita.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el ícono.");
        }

        setTitle("Gestión de Tipos de Análisis");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 247, 250));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 784, 70);
        panelHeader.setBackground(new Color(60, 70, 123));
        panelHeader.setLayout(null);
        contentPane.add(panelHeader);

        JLabel lblTitulo = new JLabel("Gestión de Tipos de Análisis");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblTitulo.setBounds(30, 18, 500, 35);
        panelHeader.add(lblTitulo);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 100, 720, 350);
        contentPane.add(scrollPane);

        tableTipos = new JTable();
        tableTipos.setFont(new Font("Tahoma", Font.PLAIN, 14));
        model = new DefaultTableModel(new String[]{"Código", "Nombre", "Descripción"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableTipos.setModel(model);
        scrollPane.setViewportView(tableTipos);

        // Botones
        JButton btnRegistrar = new JButton("Nuevo Tipo");
        btnRegistrar.setBackground(new Color(41, 128, 185));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBounds(300, 480, 140, 40);
        contentPane.add(btnRegistrar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(39, 174, 96));
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setBounds(450, 480, 140, 40);
        contentPane.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBounds(600, 480, 140, 40);
        contentPane.add(btnEliminar);

        cargarTabla();

        // Acciones
        btnRegistrar.addActionListener(e -> {
            RegTipoAnalisis reg = new RegTipoAnalisis();
            reg.setVisible(true);
            cargarTabla();
        });

        btnModificar.addActionListener(e -> {
            int row = tableTipos.getSelectedRow();
            if (row >= 0) {
                int codigo = (int) model.getValueAt(row, 0);
                TipoAnalisis tipo = tipoService.buscarTipoAnalisis(codigo);
                RegTipoAnalisis mod = new RegTipoAnalisis(tipo);
                mod.setVisible(true);
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un tipo de la tabla.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int row = tableTipos.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar este tipo?");
                if (confirm == JOptionPane.YES_OPTION) {
                    int codigo = (int) model.getValueAt(row, 0);
                    tipoService.eliminarTipoAnalisis(codigo);
                    cargarTabla();
                }
            }
        });
    }

    private void cargarTabla() {
        model.setRowCount(0); // Limpiar tabla

        try {
            // Solicitar la lista de tipos de análisis al Servidor a través del Socket
            ArrayList<TipoAnalisis> lista = (ArrayList<TipoAnalisis>) ClienteSocket.enviar("LISTAR_TIPOS_ANALISIS", null);

            if (lista != null) {
                for (TipoAnalisis t : lista) {
                    model.addRow(new Object[]{
                            t.getCodigoTipo(),
                            t.getNombre(),
                            t.getDescripcion()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}