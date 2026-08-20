package Visual;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Utils.ClienteSocket;
import logico.Analisis;
import logico.Consulta;
import logico.Medico;
import logico.TipoAnalisis;

public class RegAnalisis extends JDialog {

    private JPanel contentPane;
    private JComboBox<Consulta> comboConsulta;
    private JComboBox<TipoAnalisis> comboTipo;
    private JComboBox<String> comboEstado;
    private JTextField txtResultado;

    private Analisis analisisModificar;
    private boolean esModificacion = false;
    private Medico medicoActual; // Guardar el médico actual

    // Constructor para Registrar Nuevo
    public RegAnalisis(Medico medicoActual) {
        this.medicoActual = medicoActual;
        inicializarComponentes();
        setTitle("Registrar Análisis Clínico");
        cargarListasComboBox();
    }

    // Constructor para Modificar
    public RegAnalisis(Analisis analisis, Medico medicoActual) {
        this.analisisModificar = analisis;
        this.medicoActual = medicoActual;
        this.esModificacion = true;
        inicializarComponentes();
        setTitle("Modificar Análisis Clínico");
        cargarListasComboBox();

        // Cargar datos actuales
        txtResultado.setText(analisis.getResultado() != null ? analisis.getResultado() : "");
        comboEstado.setSelectedItem(analisis.getEstado());

        // Seleccionar en ComboBox el tipo correspondiente
        if (analisis.getTipo() != null) {
            for (int i = 0; i < comboTipo.getItemCount(); i++) {
                TipoAnalisis t = comboTipo.getItemAt(i);
                if (t.getCodigoTipo() == analisis.getTipo().getCodigoTipo()) {
                    comboTipo.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Seleccionar en ComboBox la consulta correspondiente por su código
        if (analisis.getConsulta() != null) {
            for (int i = 0; i < comboConsulta.getItemCount(); i++) {
                Consulta c = comboConsulta.getItemAt(i);
                if (c.getCodigoConsulta() == analisis.getConsulta().getCodigoConsulta()) {
                    comboConsulta.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void inicializarComponentes() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(RegAnalisis.class.getResource("/img/cita.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el ícono de la ventana.");
        }

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setBounds(100, 100, 540, 480);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(245, 247, 250));

        // Header Panel
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 524, 70);
        panelHeader.setBackground(new Color(60, 70, 123));
        panelHeader.setLayout(null);
        contentPane.add(panelHeader);

        JLabel lblTituloHeader = new JLabel(esModificacion ? "Modificar Análisis Clínico" : "Registrar Nuevo Análisis Clínico");
        lblTituloHeader.setForeground(Color.WHITE);
        lblTituloHeader.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        lblTituloHeader.setBounds(25, 18, 480, 35);
        panelHeader.add(lblTituloHeader);

        // Componentes del formulario
        JLabel lblConsulta = new JLabel("Seleccionar Consulta (Código):");
        lblConsulta.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblConsulta.setBounds(30, 90, 220, 25);
        contentPane.add(lblConsulta);

        comboConsulta = new JComboBox<>();
        comboConsulta.setFont(new Font("Tahoma", Font.PLAIN, 13));
        comboConsulta.setBounds(30, 115, 460, 30);

        comboConsulta.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Consulta) {
                    Consulta c = (Consulta) value;
                    setText("Consulta Código: " + c.getCodigoConsulta() + " - Síntomas: " + c.getSintomas());
                }
                return this;
            }
        });
        contentPane.add(comboConsulta);

        JLabel lblTipo = new JLabel("Tipo de Análisis:");
        lblTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblTipo.setBounds(30, 155, 150, 25);
        contentPane.add(lblTipo);

        comboTipo = new JComboBox<>();
        comboTipo.setFont(new Font("Tahoma", Font.PLAIN, 13));
        comboTipo.setBounds(30, 180, 460, 30);

        comboTipo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoAnalisis) {
                    TipoAnalisis t = (TipoAnalisis) value;
                    setText(t.getNombre() + " (Cod: " + t.getCodigoTipo() + ")");
                }
                return this;
            }
        });
        contentPane.add(comboTipo);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblEstado.setBounds(30, 220, 100, 25);
        contentPane.add(lblEstado);

        comboEstado = new JComboBox<>(new String[]{"Pendiente", "Completado", "Cancelado"});
        comboEstado.setFont(new Font("Tahoma", Font.PLAIN, 13));
        comboEstado.setBounds(30, 245, 460, 30);
        contentPane.add(comboEstado);

        JLabel lblResultado = new JLabel("Resultado:");
        lblResultado.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblResultado.setBounds(30, 285, 100, 25);
        contentPane.add(lblResultado);

        txtResultado = new JTextField();
        txtResultado.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtResultado.setBounds(30, 310, 460, 30);
        contentPane.add(txtResultado);

        // Botones
        JButton btnGuardar = new JButton(esModificacion ? "Actualizar" : "Guardar");
        btnGuardar.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(41, 128, 185));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBounds(280, 375, 110, 38);
        contentPane.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(127, 140, 141));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBounds(400, 375, 90, 38);
        contentPane.add(btnCancelar);

        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(e -> {
            Consulta consultaSeleccionada = (Consulta) comboConsulta.getSelectedItem();
            TipoAnalisis tipoSeleccionado = (TipoAnalisis) comboTipo.getSelectedItem();
            String estadoSeleccionado = (String) comboEstado.getSelectedItem();
            String resultadoTexto = txtResultado.getText().trim();

            if (consultaSeleccionada == null || tipoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una consulta y un tipo de análisis válidos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (esModificacion) {
                analisisModificar.setConsulta(consultaSeleccionada);
                analisisModificar.setTipo(tipoSeleccionado);
                analisisModificar.setEstado(estadoSeleccionado);
                analisisModificar.setResultado(resultadoTexto.isEmpty() ? null : resultadoTexto);

                if ("Completado".equals(estadoSeleccionado) && analisisModificar.getFechaResultado() == null) {
                    analisisModificar.setFechaResultado(LocalDateTime.now());
                }

                Boolean exito = (Boolean) ClienteSocket.enviar("UPDATE_ANALISIS", analisisModificar);
                if (exito != null && exito) {
                    JOptionPane.showMessageDialog(this, "Análisis actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el análisis en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Analisis nuevoAnalisis = new Analisis();
                nuevoAnalisis.setConsulta(consultaSeleccionada);
                nuevoAnalisis.setTipo(tipoSeleccionado);
                nuevoAnalisis.setEstado(estadoSeleccionado);
                nuevoAnalisis.setFechaOrden(LocalDateTime.now());
                nuevoAnalisis.setResultado(resultadoTexto.isEmpty() ? null : resultadoTexto);

                if ("Completado".equals(estadoSeleccionado)) {
                    nuevoAnalisis.setFechaResultado(LocalDateTime.now());
                }

                Boolean exito = (Boolean) ClienteSocket.enviar("REG_ANALISIS", nuevoAnalisis);
                if (exito != null && exito) {
                    JOptionPane.showMessageDialog(this, "Análisis registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar el análisis en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void cargarListasComboBox() {
        // Cargar Tipos de Análisis
        try {
            ArrayList<TipoAnalisis> listaTipos = (ArrayList<TipoAnalisis>) ClienteSocket.enviar("LISTAR_TIPOS_ANALISIS", null);
            comboTipo.removeAllItems();
            if (listaTipos != null) {
                for (TipoAnalisis t : listaTipos) {
                    comboTipo.addItem(t);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar tipos de análisis: " + e.getMessage());
        }

        // Cargar SOLO las consultas del médico actual utilizando su cédula
        try {
            if (medicoActual != null && medicoActual.getCedula() != null) {
                ArrayList<Consulta> listaConsultas = (ArrayList<Consulta>) ClienteSocket.enviar("LISTAR_CONSULTAS_POR_DOCTOR", medicoActual.getCedula());
                comboConsulta.removeAllItems();

                if (listaConsultas != null) {
                    for (Consulta consulta : listaConsultas) {
                        comboConsulta.addItem(consulta);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las consultas del doctor:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}