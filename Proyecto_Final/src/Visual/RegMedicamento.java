package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Medicamento;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegMedicamento extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtNombre;
    private JTextField txtConcentracion;
    private JTextArea txtDescripcion;
    private Medicamento medicamentoActual = null;
    private JButton okButton;

    public RegMedicamento() {
        init();
        setTitle("Registrar Medicamento");
    }

    public RegMedicamento(Medicamento medicamento) {
        init();
        this.medicamentoActual = medicamento;
        setTitle("Modificar Medicamento");
        okButton.setText("Actualizar");

        txtNombre.setText(medicamento.getNombre());
        txtConcentracion.setText(medicamento.getConcentracion());
        txtDescripcion.setText(medicamento.getDescripcion());
    }

    private void init() {
        setResizable(false);
        setBounds(100, 100, 450, 360);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(60, 70, 123));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblNombre.setBounds(25, 30, 130, 14);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(25, 50, 380, 25);
        contentPanel.add(txtNombre);

        JLabel lblConcentracion = new JLabel("Concentración:");
        lblConcentracion.setForeground(Color.WHITE);
        lblConcentracion.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblConcentracion.setBounds(25, 90, 130, 14);
        contentPanel.add(lblConcentracion);

        txtConcentracion = new JTextField();
        txtConcentracion.setBounds(25, 110, 380, 25);
        contentPanel.add(txtConcentracion);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblDesc.setBounds(25, 150, 130, 14);
        contentPanel.add(lblDesc);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 170, 380, 70);
        contentPanel.add(scrollPane);

        txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        scrollPane.setViewportView(txtDescripcion);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        okButton = new JButton("Registrar");
        Estilos.estilarBoton(okButton, new Color(46, 204, 113), Color.WHITE);
        okButton.addActionListener(e -> registrarMedicamento());
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton btnLimpiar = new JButton("Limpiar");
        Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE);
        btnLimpiar.addActionListener(e -> limpiarCampos());
        buttonPane.add(btnLimpiar);

        JButton cancelButton = new JButton("Cancelar");
        Estilos.estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
    }

    private void registrarMedicamento() {
        if (txtNombre.getText().trim().isEmpty() || txtConcentracion.getText().trim().isEmpty() || txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (medicamentoActual == null) {
            Medicamento med = new Medicamento();
            med.setCodigoMedicamento(0);
            med.setNombre(txtNombre.getText().trim());
            med.setConcentracion(txtConcentracion.getText().trim());
            med.setDescripcion(txtDescripcion.getText().trim());

            Object respuesta = ClienteSocket.enviar("REG_MEDICAMENTO", med);
            boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Medicamento registrado con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el medicamento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            medicamentoActual.setNombre(txtNombre.getText().trim());
            medicamentoActual.setConcentracion(txtConcentracion.getText().trim());
            medicamentoActual.setDescripcion(txtDescripcion.getText().trim());

            Object respuesta = ClienteSocket.enviar("UPDATE_MEDICAMENTO", medicamentoActual);
            boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Medicamento actualizado con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el medicamento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtConcentracion.setText("");
        txtDescripcion.setText("");
    }
}