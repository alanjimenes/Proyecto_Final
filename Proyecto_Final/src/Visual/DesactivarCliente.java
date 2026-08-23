package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DesactivarCliente extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCedula;

    public DesactivarCliente() {
        setTitle("Desactivar Paciente");
        setBounds(100, 100, 480, 260);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBackground(new Color(60, 70, 123));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Desactivación de Paciente");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPanel.add(lblTitulo);

        // Corrección: JSeparator en lugar de JSparator
        JSeparator separator = new JSeparator();
        separator.setBounds(30, 60, 400, 2);
        contentPanel.add(separator);

        JLabel lblCedula = new JLabel("Cédula / Expediente:");
        lblCedula.setForeground(Color.WHITE);
        lblCedula.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        lblCedula.setBounds(30, 85, 150, 25);
        contentPanel.add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtCedula.setBounds(180, 84, 250, 28);
        contentPanel.add(txtCedula);
        txtCedula.setColumns(10);

        // --- BOTONES ---
        JButton btnDesactivar = new JButton("Desactivar");
        Estilos.estilarBoton(btnDesactivar, new Color(231, 76, 60), Color.WHITE);
        btnDesactivar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnDesactivar.setBounds(180, 150, 120, 35);
        btnDesactivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ejecutarDesactivacion();
            }
        });
        contentPanel.add(btnDesactivar);

        JButton btnCancelar = new JButton("Cancelar");
        Estilos.estilarBoton(btnCancelar, new Color(127, 140, 141), Color.WHITE);
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCancelar.setBounds(310, 150, 120, 35);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        contentPanel.add(btnCancelar);
    }

    private void ejecutarDesactivacion() {
        String cedula = txtCedula.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese la cédula del paciente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de desactivar al paciente con cédula: " + cedula + "?\nEsta acción cancelará todas sus citas pendientes.", "Confirmar Desactivación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Object respuesta = ClienteSocket.enviar("DESACTIVAR_PERSONA_SP", cedula);
                boolean exito = (respuesta != null && (boolean) respuesta);

                if (exito) {
                    JOptionPane.showMessageDialog(this, "Paciente desactivado y citas canceladas con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo completar la operación. Verifique que la cédula exista.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error de comunicación con el servidor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}