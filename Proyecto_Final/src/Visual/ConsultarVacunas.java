package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Vacuna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ConsultarVacunas extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private Vacuna seleccionado = null;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnVerLotes;

    public ConsultarVacunas() {
        try {
            //setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarVacunas.class.getResource("/img/seguro-de-salud.png")));
        } catch (Exception e) {
        }

        setTitle("Gestión de Vacunas");
        setBounds(100, 100, 850, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.desktop);
        panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        contentPanel.add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    String codigo = table.getValueAt(index, 0).toString();
                    seleccionado = buscarVacunaLocal(codigo);

                    btnUpdate.setEnabled(true);
                    btnDelete.setEnabled(true);
                    if (btnVerLotes != null) {
                        btnVerLotes.setEnabled(true);
                    }
                }
            }
        });

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] headers = {"Código", "Nombre", "Descripción"};
        model.setColumnIdentifiers(headers);
        table.setModel(model);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(60, 70, 123));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setReorderingAllowed(false);

        scrollPane.setViewportView(table);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(60, 70, 123));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        {
            btnVerLotes = new JButton("Ver Lotes");
            Estilos.estilarBoton(btnVerLotes, new Color(241, 196, 15), Color.BLACK);
            btnVerLotes.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnVerLotes.setEnabled(false);
            btnVerLotes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (seleccionado != null) {
                        ListadoLotesVacuna listadoLotes = new ListadoLotesVacuna(seleccionado);
                        listadoLotes.setModal(true);
                        listadoLotes.setVisible(true);
                        resetBotones();
                    }
                }
            });
            buttonPane.add(btnVerLotes);
        }

        {
            JButton btnNuevo = new JButton("Nueva");
            Estilos.estilarBoton(btnNuevo, new Color(176, 206, 136), Color.WHITE);
            btnNuevo.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnNuevo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    RegVacuna reg = new RegVacuna();
                    reg.setModal(true);
                    reg.setVisible(true);
                    cargarVacunas();
                    resetBotones();
                }
            });
            buttonPane.add(btnNuevo);
        }

        {
            btnUpdate = new JButton("Modificar");
            Estilos.estilarBoton(btnUpdate, new Color(41, 128, 185), Color.WHITE);
            btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnUpdate.setEnabled(false);
            btnUpdate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (seleccionado != null) {
                        RegVacuna reg = new RegVacuna(seleccionado);
                        reg.setModal(true);
                        reg.setVisible(true);

                        cargarVacunas();
                        resetBotones();
                    }
                }
            });
            buttonPane.add(btnUpdate);
        }

        {
            btnDelete = new JButton("Eliminar");
            Estilos.estilarBoton(btnDelete, new Color(231, 76, 60), Color.WHITE);
            btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnDelete.setEnabled(false);
            btnDelete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (seleccionado != null) {
                        int opt = JOptionPane.showConfirmDialog(null, "¿Seguro desea eliminar la vacuna " + seleccionado.getNombre() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                        if (opt == JOptionPane.YES_OPTION) {
                            boolean exito = (boolean) ClienteSocket.enviar("DELETE_VACUNA", seleccionado);
                            if (exito) {
                                JOptionPane.showMessageDialog(null, "Eliminada correctamente.");
                                cargarVacunas();
                                resetBotones();
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al eliminar.");
                            }
                        }
                    }
                }
            });
            buttonPane.add(btnDelete);
        }

        {
            JButton btnCerrar = new JButton("Cerrar");
            Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
            btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnCerrar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            buttonPane.add(btnCerrar);
        }

        cargarVacunas();
    }

    @SuppressWarnings("unchecked")
    private void cargarVacunas() {
        model.setRowCount(0);

        ArrayList<Vacuna> lista = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);

        if (lista != null) {
            for (Vacuna v : lista) {
                model.addRow(new Object[]{v.getCodigoVacuna(), v.getNombre(), v.getDescripcion()});
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Vacuna buscarVacunaLocal(String codigo) {
        ArrayList<Vacuna> lista = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);

        if (lista != null) {
            for (Vacuna v : lista) {
                if (String.valueOf(v.getCodigoVacuna()).equals(codigo)) {
                    return v;
                }
            }
        }
        return null;
    }

    private void resetBotones() {
        seleccionado = null;
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        if (btnVerLotes != null) {
            btnVerLotes.setEnabled(false);
        }
        table.clearSelection();
    }
}