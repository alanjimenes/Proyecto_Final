package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Especialidad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegEspecialidad extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtNombre;
    private Especialidad especialidadActual = null;
    private JButton okButton;

    public RegEspecialidad() {
        init();
        setTitle("Registrar Especialidad");
    }

    public RegEspecialidad(Especialidad esp) {
        init();
        this.especialidadActual = esp;
        setTitle("Modificar Especialidad");
        okButton.setText("Actualizar");
        txtNombre.setText(esp.getNombre());
    }

    private void init() {
        setResizable(false);
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(RegEspecialidad.class.getResource("/img/especialidad.png")));
        } catch (Exception e) {
        }

        setBounds(100, 100, 490, 235);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(60, 70, 123));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre Especialidad:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblNombre.setBounds(22, 87, 159, 14);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(201, 84, 251, 20);
        contentPanel.add(txtNombre);
        txtNombre.setColumns(10);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        okButton = new JButton("Registrar");
        Estilos.estilarBoton(okButton, new Color(0, 150, 136), Color.WHITE);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gestionar();
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancelar");
        Estilos.estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        JLabel lblTitulo = new JLabel("Gestión de Especialidades");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        lblTitulo.setBounds(90, 11, 287, 37);
        contentPanel.add(lblTitulo);
    }

    private void gestionar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
            return;
        }

        if (especialidadActual == null) {
            // Se envía 0 para que la BD/servidor genere el ID autoincrementable
            Especialidad aux = new Especialidad(0, txtNombre.getText().trim());
            Object resp = ClienteSocket.enviar("REG_ESPECIALIDAD", aux);
            boolean exito = (resp instanceof Boolean) ? (Boolean) resp : false;

            if (exito) {
                JOptionPane.showMessageDialog(null, "Registrado exitosamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar.");
            }

        } else {
            especialidadActual.setNombre(txtNombre.getText().trim());
            Object resp = ClienteSocket.enviar("UPDATE_ESPECIALIDAD", especialidadActual);
            boolean exito = (resp instanceof Boolean) ? (Boolean) resp : false;

            if (exito) {
                JOptionPane.showMessageDialog(null, "Actualizado exitosamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar.");
            }
        }
    }
}