package Visual;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Utils.ClienteSocket;
import logico.TipoAnalisis;

public class RegTipoAnalisis extends JDialog {

    private JPanel contentPane;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private TipoAnalisis tipoModificar;
    private boolean esModificacion = false;

    // Constructor para Registrar Nuevo
    public RegTipoAnalisis() {
        inicializarComponentes();
        setTitle("Registrar Tipo de Análisis");
    }

    // Constructor para Modificar Existente
    public RegTipoAnalisis(TipoAnalisis tipo) {
        this.tipoModificar = tipo;
        this.esModificacion = true;
        inicializarComponentes();
        setTitle("Modificar Tipo de Análisis");

        // Cargar datos en los campos
        txtNombre.setText(tipo.getNombre());
        txtDescripcion.setText(tipo.getDescripcion());
    }

    private void inicializarComponentes() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(RegTipoAnalisis.class.getResource("/img/cita.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el ícono de la ventana.");
        }

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setBounds(100, 100, 480, 380);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(245, 247, 250));

        // Header Panel
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 464, 70);
        panelHeader.setBackground(new Color(60, 70, 123));
        panelHeader.setLayout(null);
        contentPane.add(panelHeader);

        JLabel lblTituloHeader = new JLabel(esModificacion ? "Modificar Tipo de Análisis" : "Registrar Nuevo Tipo de Análisis");
        lblTituloHeader.setForeground(Color.WHITE);
        lblTituloHeader.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        lblTituloHeader.setBounds(25, 18, 420, 35);
        panelHeader.add(lblTituloHeader);

        // Campos del formulario
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblNombre.setBounds(30, 100, 100, 25);
        contentPane.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtNombre.setBounds(30, 130, 400, 30);
        contentPane.add(txtNombre);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblDescripcion.setBounds(30, 180, 100, 25);
        contentPane.add(lblDescripcion);

        txtDescripcion = new JTextField();
        txtDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtDescripcion.setBounds(30, 210, 400, 30);
        contentPane.add(txtDescripcion);

        // Botones de acción
        JButton btnGuardar = new JButton(esModificacion ? "Actualizar" : "Guardar");
        btnGuardar.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(41, 128, 185));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBounds(220, 275, 110, 38);
        contentPane.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(127, 140, 141));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBounds(340, 275, 90, 38);
        contentPane.add(btnCancelar);

        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del tipo de análisis es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (esModificacion) {
                tipoModificar.setNombre(nombre);
                tipoModificar.setDescripcion(descripcion);
                boolean exito = (boolean) ClienteSocket.enviar("UPDATE_TIPO_ANALISIS", tipoModificar);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Tipo de análisis actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el tipo de análisis.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                TipoAnalisis nuevoTipo = new TipoAnalisis();
                nuevoTipo.setNombre(nombre);
                nuevoTipo.setDescripcion(descripcion);
                boolean exito = (boolean) ClienteSocket.enviar("REG_TIPO_ANALISIS", nuevoTipo);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Tipo de análisis registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar el tipo de análisis.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}