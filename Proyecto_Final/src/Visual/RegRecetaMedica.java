package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Consulta;
import logico.Medicamento;
import logico.RecetaMedica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class RegRecetaMedica extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JComboBox<String> cbxMedicamento;
    private JTextField txtDosis;
    private JTextField txtFrecuencia;
    private JTextField txtDuracion;
    private JTextArea txtDescripcion;
    private ArrayList<Medicamento> listaMedicamentos;
    private RecetaMedica recetaActual = null;
    private Consulta consultaVinculada = null;
    private JButton btnRegistrar;

    public RegRecetaMedica(Consulta consulta) {
        this.consultaVinculada = consulta;
        init();
        setTitle("Añadir Receta Médica");
    }

    public RegRecetaMedica(RecetaMedica recetaEditar) {
        init();
        this.recetaActual = recetaEditar;
        this.consultaVinculada = recetaEditar.getConsulta();
        setTitle("Modificar Receta Médica");
        btnRegistrar.setText("Actualizar");

        txtDosis.setText(recetaEditar.getDosis());
        txtFrecuencia.setText(recetaEditar.getFrecuencia());
        txtDuracion.setText(recetaEditar.getDuracion());
        txtDescripcion.setText(recetaEditar.getDescripcion());

        if (recetaEditar.getMedicamento() != null) {
            for (int i = 0; i < listaMedicamentos.size(); i++) {
                if (listaMedicamentos.get(i).getCodigoMedicamento() == recetaEditar.getMedicamento().getCodigoMedicamento()) {
                    cbxMedicamento.setSelectedIndex(i + 1);
                    break;
                }
            }
        }
    }

    private void init() {
        setResizable(false);
        setBounds(100, 100, 500, 480);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(60, 70, 123));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblMedicamento = new JLabel("Medicamento:");
        lblMedicamento.setForeground(Color.WHITE);
        lblMedicamento.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblMedicamento.setBounds(30, 30, 120, 20);
        contentPanel.add(lblMedicamento);

        cbxMedicamento = new JComboBox<>();
        cbxMedicamento.setBounds(150, 30, 300, 25);
        contentPanel.add(cbxMedicamento);

        JLabel lblDosis = new JLabel("Dosis:");
        lblDosis.setForeground(Color.WHITE);
        lblDosis.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblDosis.setBounds(30, 80, 120, 20);
        contentPanel.add(lblDosis);

        txtDosis = new JTextField();
        txtDosis.setBounds(150, 80, 300, 25);
        contentPanel.add(txtDosis);

        JLabel lblFrecuencia = new JLabel("Frecuencia:");
        lblFrecuencia.setForeground(Color.WHITE);
        lblFrecuencia.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblFrecuencia.setBounds(30, 130, 120, 20);
        contentPanel.add(lblFrecuencia);

        txtFrecuencia = new JTextField();
        txtFrecuencia.setBounds(150, 130, 300, 25);
        contentPanel.add(txtFrecuencia);

        JLabel lblDuracion = new JLabel("Duración:");
        lblDuracion.setForeground(Color.WHITE);
        lblDuracion.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblDuracion.setBounds(30, 180, 120, 20);
        contentPanel.add(lblDuracion);

        txtDuracion = new JTextField();
        txtDuracion.setBounds(150, 180, 300, 25);
        contentPanel.add(txtDuracion);

        JLabel lblDescripcion = new JLabel("Indicaciones:");
        lblDescripcion.setForeground(Color.WHITE);
        lblDescripcion.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblDescripcion.setBounds(30, 230, 120, 20);
        contentPanel.add(lblDescripcion);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(150, 230, 300, 90);
        contentPanel.add(scrollPane);

        txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        scrollPane.setViewportView(txtDescripcion);

        btnRegistrar = new JButton("Registrar");
        Estilos.estilarBoton(btnRegistrar, new Color(0, 150, 136), Color.WHITE);
        btnRegistrar.setBounds(80, 360, 120, 35);
        btnRegistrar.addActionListener(e -> registrarReceta());
        contentPanel.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        Estilos.estilarBoton(btnCancelar, new Color(191, 26, 26), Color.WHITE);
        btnCancelar.setBounds(280, 360, 120, 35);
        btnCancelar.addActionListener(e -> dispose());
        contentPanel.add(btnCancelar);

        cargarMedicamentos();
    }

    @SuppressWarnings("unchecked")
    private void cargarMedicamentos() {
        cbxMedicamento.addItem("<Seleccione Medicamento>");
        Object respuesta = ClienteSocket.enviar("LISTAR_MEDICAMENTOS", null);
        if (respuesta != null && respuesta instanceof ArrayList) {
            listaMedicamentos = (ArrayList<Medicamento>) respuesta;
            for (Medicamento m : listaMedicamentos) {
                cbxMedicamento.addItem(m.getNombre() + " - " + m.getConcentracion());
            }
        }
    }

    private void registrarReceta() {
        if (cbxMedicamento.getSelectedIndex() <= 0 || txtDosis.getText().trim().isEmpty() || txtFrecuencia.getText().trim().isEmpty() || txtDuracion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Medicamento medSeleccionado = listaMedicamentos.get(cbxMedicamento.getSelectedIndex() - 1);

        if (recetaActual == null) {
            RecetaMedica receta = new RecetaMedica();
            receta.setCodigoRec(0);
            receta.setConsulta(consultaVinculada);
            receta.setMedicamento(medSeleccionado);
            receta.setDosis(txtDosis.getText().trim());
            receta.setFrecuencia(txtFrecuencia.getText().trim());
            receta.setDuracion(txtDuracion.getText().trim());
            receta.setDescripcion(txtDescripcion.getText().trim());

            Object respuesta = ClienteSocket.enviar("REG_RECETA_MEDICA", receta);
            boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Receta registrada con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la receta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            recetaActual.setMedicamento(medSeleccionado);
            recetaActual.setDosis(txtDosis.getText().trim());
            recetaActual.setFrecuencia(txtFrecuencia.getText().trim());
            recetaActual.setDuracion(txtDuracion.getText().trim());
            recetaActual.setDescripcion(txtDescripcion.getText().trim());

            Object respuesta = ClienteSocket.enviar("UPDATE_RECETA_MEDICA", recetaActual);
            boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Receta actualizada con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la receta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}