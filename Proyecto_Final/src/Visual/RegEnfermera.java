package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import com.toedter.calendar.JDateChooser;
import logico.Enfermera;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RegEnfermera extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JComboBox<String> cbxGenero;
    private JComboBox<String> cbxTurno;
    private JDateChooser dateChooser;
    private JButton okButton;
    private Enfermera enfermeraActual = null;

    public RegEnfermera() {
        setResizable(false);
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(RegEnfermera.class.getResource("/img/seguro-de-salud.png")));
        } catch (Exception e) {
        }
        initComponents();
        this.enfermeraActual = null;
    }

    public RegEnfermera(Enfermera enfermeraEditar) {
        initComponents();
        this.enfermeraActual = enfermeraEditar;
        setTitle("Modificar Enfermera");
        okButton.setText("Actualizar");

        txtCedula.setText(enfermeraEditar.getCedula());
        txtCedula.setEditable(false);
        txtNombre.setText(enfermeraEditar.getNombre());
        txtApellido.setText(enfermeraEditar.getApellido());
        txtTelefono.setText(enfermeraEditar.getTelefono());
        txtDireccion.setText(enfermeraEditar.getDireccion());
        cbxGenero.setSelectedItem(enfermeraEditar.getGenero());
        cbxTurno.setSelectedItem(enfermeraEditar.getTurno());

        if (enfermeraEditar.getFechaNacimiento() != null) {
            Date fecha = Date.from(enfermeraEditar.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(fecha);
        }
    }

    private void initComponents() {
        setTitle("Registrar Enfermera");
        setBounds(100, 100, 1074, 509);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(60, 70, 123));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setForeground(Color.WHITE);
        lblCedula.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblCedula.setBounds(28, 163, 80, 14);
        contentPanel.add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setBounds(135, 163, 220, 20);
        contentPanel.add(txtCedula);
        txtCedula.setColumns(10);

        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    return;
                }
                if (txtCedula.getText().length() >= 13 && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    return;
                }
                if (c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    if (txtCedula.getText().length() == 3 || txtCedula.getText().length() == 11) {
                        txtCedula.setText(txtCedula.getText() + "-");
                    }
                }
            }
        });

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblNombre.setBounds(371, 166, 80, 14);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(476, 163, 220, 20);
        contentPanel.add(txtNombre);
        txtNombre.setColumns(10);

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && c != ' ' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setForeground(Color.WHITE);
        lblApellido.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblApellido.setBounds(723, 166, 80, 14);
        contentPanel.add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setBounds(819, 163, 220, 20);
        contentPanel.add(txtApellido);
        txtApellido.setColumns(10);

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && c != ' ' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setForeground(Color.WHITE);
        lblDireccion.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblDireccion.setBounds(28, 222, 80, 14);
        contentPanel.add(lblDireccion);

        txtDireccion = new JTextField();
        txtDireccion.setBounds(135, 219, 220, 20);
        contentPanel.add(txtDireccion);
        txtDireccion.setColumns(10);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(Color.WHITE);
        lblTelefono.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblTelefono.setBounds(371, 225, 80, 14);
        contentPanel.add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(476, 219, 220, 20);
        contentPanel.add(txtTelefono);
        txtTelefono.setColumns(10);

        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    return;
                }
                if (txtTelefono.getText().length() >= 15 && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    return;
                }
                if (c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    if (txtTelefono.getText().length() == 3 || txtTelefono.getText().length() == 7) {
                        txtTelefono.setText(txtTelefono.getText() + "-");
                    }
                }
            }
        });

        JLabel lblFecha = new JLabel("Fecha Nac:");
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblFecha.setBounds(723, 225, 80, 14);
        contentPanel.add(lblFecha);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(819, 219, 220, 20);
        contentPanel.add(dateChooser);
        dateChooser.setMaxSelectableDate(new Date());

        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setForeground(Color.WHITE);
        lblGenero.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblGenero.setBounds(28, 275, 100, 14);
        contentPanel.add(lblGenero);

        cbxGenero = new JComboBox<>();
        cbxGenero.setModel(new DefaultComboBoxModel<>(new String[]{"<Seleccione>", "Masculino", "Femenino"}));
        cbxGenero.setBounds(135, 272, 220, 20);
        contentPanel.add(cbxGenero);

        JLabel lblTurno = new JLabel("Turno:");
        lblTurno.setForeground(Color.WHITE);
        lblTurno.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblTurno.setBounds(371, 278, 100, 14);
        contentPanel.add(lblTurno);

        cbxTurno = new JComboBox<>();
        cbxTurno.setModel(new DefaultComboBoxModel<>(new String[]{"<Seleccione>", "Mañana", "Tarde", "Noche"}));
        cbxTurno.setBounds(478, 275, 220, 20);
        contentPanel.add(cbxTurno);

        okButton = new JButton("Registrar");
        Estilos.estilarBoton(okButton, new Color(0, 150, 136), Color.WHITE);
        okButton.setBounds(221, 409, 110, 35);
        contentPanel.add(okButton);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gestionEnfermera();
            }
        });
        okButton.setActionCommand("OK");
        getRootPane().setDefaultButton(okButton);

        JButton btnListado = new JButton("Listado");
        Estilos.estilarBoton(btnListado, new Color(110, 140, 251), Color.WHITE);
        btnListado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                limpiarCampos();
                ConsultarEnfermeras frame = new ConsultarEnfermeras();
                frame.setLocationRelativeTo(contentPanel);
                frame.setModal(true);
                frame.setVisible(true);
            }
        });
        btnListado.setBounds(400, 409, 110, 35);
        contentPanel.add(btnListado);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                limpiarCampos();
            }
        });
        Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE);
        btnLimpiar.setBounds(565, 409, 110, 35);
        contentPanel.add(btnLimpiar);

        JButton cancelButton = new JButton("Cancelar");
        Estilos.estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);
        cancelButton.setBounds(723, 409, 110, 35);
        contentPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");

        JLabel lblBienvenida = new JLabel("Registrar Enfermera");
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Bahnschrift", Font.BOLD, 40));
        lblBienvenida.setBounds(10, -7, 450, 100);
        contentPanel.add(lblBienvenida);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 91, 635, 2);
        contentPanel.add(separator);

        JLabel label = new JLabel("");
        try {
            label.setIcon(new ImageIcon(RegEnfermera.class.getResource("/img/custom_resized_ffde04b9-ae4a-43dd-8c6e-2c67d4183e19.png")));
        } catch (Exception e) {
        }
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
        label.setBounds(702, -7, 366, 131);
        contentPanel.add(label);
    }

    private void gestionEnfermera() {
        if (txtCedula.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() ||
                dateChooser.getDate() == null || cbxGenero.getSelectedIndex() <= 0 || cbxTurno.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(null, "Por favor llene los campos obligatorios.");
            return;
        }

        Date date = dateChooser.getDate();
        LocalDate fechaNac = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (fechaNac.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser una fecha futura.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fechaMinima = LocalDate.now().minusYears(18);

        if (fechaNac.isAfter(fechaMinima)) {
            JOptionPane.showMessageDialog(null, "La enfermera debe tener al menos 18 años de edad.", "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (enfermeraActual == null) {
            Enfermera enf = new Enfermera();
            enf.setCodigoPersona(0);
            enf.setCedula(txtCedula.getText().trim());
            enf.setNombre(txtNombre.getText().trim());
            enf.setApellido(txtApellido.getText().trim());
            enf.setTelefono(txtTelefono.getText().trim());
            enf.setDireccion(txtDireccion.getText().trim());
            enf.setGenero(cbxGenero.getSelectedItem().toString());
            enf.setTurno(cbxTurno.getSelectedItem().toString());
            enf.setFechaNacimiento(fechaNac);
            enf.setEstado(true);

            Object respuesta = ClienteSocket.enviar("REG_ENFERMERA", enf);
            boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

            if (exito) {
                JOptionPane.showMessageDialog(null, "Enfermera registrada en el Servidor.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar en servidor.");
            }
        } else {
            enfermeraActual.setNombre(txtNombre.getText().trim());
            enfermeraActual.setApellido(txtApellido.getText().trim());
            enfermeraActual.setTelefono(txtTelefono.getText().trim());
            enfermeraActual.setDireccion(txtDireccion.getText().trim());
            enfermeraActual.setGenero(cbxGenero.getSelectedItem().toString());
            enfermeraActual.setTurno(cbxTurno.getSelectedItem().toString());
            enfermeraActual.setFechaNacimiento(fechaNac);

            Object respuesta = ClienteSocket.enviar("UPDATE_ENFERMERA", enfermeraActual);
            boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

            if (exito) {
                JOptionPane.showMessageDialog(null, "Enfermera actualizada.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar.");
            }
        }
    }

    private void limpiarCampos() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");

        if (dateChooser != null)
            dateChooser.setDate(null);
        if (cbxGenero != null)
            cbxGenero.setSelectedIndex(0);
        if (cbxTurno != null)
            cbxTurno.setSelectedIndex(0);

        enfermeraActual = null;
        txtCedula.setEditable(true);
        okButton.setText("Registrar");
        txtCedula.requestFocus();
    }
}