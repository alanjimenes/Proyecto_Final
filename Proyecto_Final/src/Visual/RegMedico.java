package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import com.toedter.calendar.JDateChooser;
import logico.Especialidad;
import logico.Medico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class RegMedico extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JComboBox<String> cbxEspecialidad;
    private JDateChooser dateChooser;
    private JSpinner spnMaxCitas;
    private JButton okButton;
    private Medico medicoActual = null;

    public RegMedico() {
        setResizable(false);
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(RegMedico.class.getResource("/img/doctor.png")));
        } catch (Exception e) {
        }
        initComponents();
        this.medicoActual = null;
    }

    public RegMedico(Medico medicoEditar) {
        initComponents();
        this.medicoActual = medicoEditar;
        setTitle("Modificar Médico");
        okButton.setText("Actualizar");

        txtCedula.setText(medicoEditar.getCedula());
        txtCedula.setEditable(false);
        txtNombre.setText(medicoEditar.getNombre());
        txtApellido.setText(medicoEditar.getApellido());
        txtTelefono.setText(medicoEditar.getTelefono());
        txtDireccion.setText(medicoEditar.getDireccion());

        if (medicoEditar.getEspecialidad() != null) {
            cbxEspecialidad.setSelectedItem(medicoEditar.getEspecialidad().getNombre());
        }
        spnMaxCitas.setValue(medicoEditar.getMaxCitasPorDia());
        if (medicoEditar.getFechaNacimiento() != null) {
            Date fecha = Date.from(medicoEditar.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(fecha);
        }
    }

    private void initComponents() {
        setTitle("Registrar Médico");
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

        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setForeground(Color.WHITE);
        lblDireccion.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblDireccion.setBounds(28, 222, 80, 14);
        contentPanel.add(lblDireccion);

        txtDireccion = new JTextField();
        txtDireccion.setBounds(135, 219, 220, 20);
        contentPanel.add(txtDireccion);
        txtDireccion.setColumns(10);

        JLabel lblFecha = new JLabel("Fecha Nac:");
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblFecha.setBounds(723, 225, 80, 14);
        contentPanel.add(lblFecha);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(819, 219, 220, 20);
        contentPanel.add(dateChooser);

        dateChooser.setMaxSelectableDate(new Date());

        JLabel lblEspecialidad = new JLabel("Especialidad:");
        lblEspecialidad.setForeground(Color.WHITE);
        lblEspecialidad.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblEspecialidad.setBounds(371, 278, 100, 14);
        contentPanel.add(lblEspecialidad);

        cbxEspecialidad = new JComboBox<>();
        cbxEspecialidad.setBounds(478, 275, 220, 20);
        contentPanel.add(cbxEspecialidad);

        JButton btnAddEsp = new JButton("+");
        Estilos.estilarBoton(btnAddEsp, new Color(127, 140, 141), Color.WHITE);
        btnAddEsp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegEspecialidad regEsp = new RegEspecialidad();
                regEsp.setModal(true);
                regEsp.setVisible(true);
                cargarEspecialidades();
            }
        });
        btnAddEsp.setBounds(723, 274, 45, 23);
        contentPanel.add(btnAddEsp);

        JLabel lblMaxCitas = new JLabel("Citas Diarias:");
        lblMaxCitas.setForeground(Color.WHITE);
        lblMaxCitas.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblMaxCitas.setBounds(28, 275, 100, 14);
        contentPanel.add(lblMaxCitas);

        spnMaxCitas = new JSpinner();
        spnMaxCitas.setModel(new SpinnerNumberModel(10, 1, 100, 1));
        spnMaxCitas.setBounds(135, 272, 67, 20);
        contentPanel.add(spnMaxCitas);
        {
            okButton = new JButton("Registrar");
            Estilos.estilarBoton(okButton, new Color(0, 150, 136), Color.WHITE);
            okButton.setBounds(221, 409, 110, 35);
            contentPanel.add(okButton);
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gestionMedico();
                }
            });
            okButton.setActionCommand("OK");
            getRootPane().setDefaultButton(okButton);
        }
        {
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
        }

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                limpiarCampos();
            }
        });
        Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE);
        btnLimpiar.setBounds(565, 409, 110, 35);
        contentPanel.add(btnLimpiar);

        JButton btnListado = new JButton("Listado");
        Estilos.estilarBoton(btnListado, new Color(110, 140, 251), Color.WHITE);
        btnListado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                limpiarCampos();
                ConsultarMedicos frame = new ConsultarMedicos();
                frame.setLocationRelativeTo(contentPanel);
                frame.setModal(true);
                frame.setVisible(true);
            }
        });
        btnListado.setBounds(400, 409, 110, 35);
        contentPanel.add(btnListado);

        JLabel lblBienvenida = new JLabel("Registrar Medico");
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Bahnschrift", Font.BOLD, 40));
        lblBienvenida.setBounds(10, -7, 342, 100);
        contentPanel.add(lblBienvenida);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 91, 635, 2);
        contentPanel.add(separator);

        JLabel label = new JLabel("");
        try {
            label.setIcon(new ImageIcon(
                    RegMedico.class.getResource("/img/custom_resized_ffde04b9-ae4a-43dd-8c6e-2c67d4183e19.png")));
        } catch (Exception e) {
        }
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
        label.setBounds(702, -7, 366, 131);
        contentPanel.add(label);

        cargarEspecialidades();
    }

    @SuppressWarnings("unchecked")
    private void cargarEspecialidades() {
        cbxEspecialidad.removeAllItems();
        cbxEspecialidad.addItem("<Seleccione>");
        Object resp = ClienteSocket.enviar("LISTAR_ESPECIALIDADES", null);
        if (resp != null && resp instanceof ArrayList) {
            ArrayList<Especialidad> lista = (ArrayList<Especialidad>) resp;
            for (Especialidad esp : lista) {
                cbxEspecialidad.addItem(esp.getNombre());
            }
        }
    }

    private void gestionMedico() {
        if (txtNombre.getText().isEmpty() || dateChooser.getDate() == null || cbxEspecialidad.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(null, "Por favor llene los campos obligatorios.");
            return;
        }
        String nombreEsp = (String) cbxEspecialidad.getSelectedItem();
        Especialidad espSeleccionada = (Especialidad) ClienteSocket.enviar("BUSCAR_ESPECIALIDAD_NOMBRE", nombreEsp);

        Date date = dateChooser.getDate();
        LocalDate fechaNac = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (fechaNac.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser una fecha futura.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fechaMinima = LocalDate.now().minusYears(23);

        if (fechaNac.isAfter(fechaMinima)) {
            JOptionPane.showMessageDialog(null, "El médico debe tener al menos 23 años de edad.", "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (medicoActual == null) {
            if (txtCedula.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "La cédula es obligatoria.");
                return;
            }
            Medico existe = (Medico) ClienteSocket.enviar("BUSCAR_MEDICO", txtCedula.getText());
            if (existe != null) {
                JOptionPane.showMessageDialog(null, "Ya existe un médico con esa cédula.");
                return;
            }

            //Medico nuevoMedico = new Medico(0, fechaNac, txtNombre.getText(), txtApellido.getText(), txtCedula.getText(), txtTelefono.getText(), true,
            //		txtDireccion.getText(), cbxGenero.getSelectedItem().toString(), (int) spnMaxCitas.getValue(), usuario, espSeleccionada);

            Medico nuevoMedico = new Medico(0, fechaNac, txtNombre.getText(), txtApellido.getText(), txtCedula.getText(), txtTelefono.getText(), true, txtDireccion.getText(),
                    "No especificado", (int) spnMaxCitas.getValue(), null, espSeleccionada);
            boolean exito = (boolean) ClienteSocket.enviar("REG_MEDICO", nuevoMedico);

            if (exito) {
                JOptionPane.showMessageDialog(null, "Médico registrado en el Servidor.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar en servidor.");
            }
        } else {
            medicoActual.setNombre(txtNombre.getText());
            medicoActual.setApellido(txtApellido.getText());
            medicoActual.setTelefono(txtTelefono.getText());
            medicoActual.setDireccion(txtDireccion.getText());
            medicoActual.setFechaNacimiento(fechaNac);
            medicoActual.setEspecialidad(espSeleccionada);
            medicoActual.setMaxCitasPorDia((int) spnMaxCitas.getValue());

            boolean exito = (boolean) ClienteSocket.enviar("UPDATE_MEDICO", medicoActual);

            if (exito) {
                JOptionPane.showMessageDialog(null, "Médico actualizado.");
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
        if (cbxEspecialidad != null)
            cbxEspecialidad.setSelectedIndex(0);
        if (spnMaxCitas != null)
            spnMaxCitas.setValue(10);

        medicoActual = null;
        txtCedula.setEditable(true);
        okButton.setText("Registrar");
        txtCedula.requestFocus();
    }
}