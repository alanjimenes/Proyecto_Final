package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import com.toedter.calendar.JDateChooser;
import logico.LoteVacuna;
import logico.Vacuna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class RegLoteVacuna extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtNoLote;
    private JSpinner spnCantidad;
    private JDateChooser txtFechaVencimiento;
    private JComboBox<String> cbxVacuna;
    private ArrayList<Vacuna> listaVacunas = new ArrayList<>();
    private LoteVacuna loteActual = null;
    private JButton btnRegistrar;

    public RegLoteVacuna() {
        init();
        setTitle("Registrar Lote de Vacuna");
    }

    public RegLoteVacuna(LoteVacuna loteEditar) {
        init();
        this.loteActual = loteEditar;
        setTitle("Modificar Lote de Vacuna");
        btnRegistrar.setText("Actualizar");

        txtNoLote.setText(loteActual.getNoLote());
        spnCantidad.setValue(loteActual.getCantidad());
        if (loteActual.getFechaVencimiento() != null) {
            java.util.Date date = java.util.Date.from(loteActual.getFechaVencimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
            txtFechaVencimiento.setDate(date);
        }

        if (loteActual.getVacuna() != null) {
            for (int i = 0; i < listaVacunas.size(); i++) {
                if (listaVacunas.get(i).getCodigoVacuna() == loteActual.getVacuna().getCodigoVacuna()) {
                    cbxVacuna.setSelectedIndex(i + 1);
                    break;
                }
            }
        }
    }

    private void init() {
        setResizable(false);
        setBounds(100, 100, 500, 400);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(60, 70, 123));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblVacuna = new JLabel("Vacuna:");
        lblVacuna.setForeground(Color.WHITE);
        lblVacuna.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblVacuna.setBounds(40, 40, 100, 20);
        contentPanel.add(lblVacuna);

        cbxVacuna = new JComboBox<>();
        cbxVacuna.setBounds(150, 40, 280, 25);
        contentPanel.add(cbxVacuna);

        JLabel lblNoLote = new JLabel("No. Lote:");
        lblNoLote.setForeground(Color.WHITE);
        lblNoLote.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblNoLote.setBounds(40, 90, 100, 20);
        contentPanel.add(lblNoLote);

        txtNoLote = new JTextField();
        txtNoLote.setBounds(150, 90, 280, 25);
        contentPanel.add(txtNoLote);

        JLabel lblFecha = new JLabel("Vencimiento:");
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblFecha.setBounds(40, 140, 100, 20);
        contentPanel.add(lblFecha);

        txtFechaVencimiento = new JDateChooser();
        txtFechaVencimiento.setBounds(150, 140, 280, 25);
        ((JTextField) txtFechaVencimiento.getDateEditor().getUiComponent()).setEditable(false);
        contentPanel.add(txtFechaVencimiento);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setForeground(Color.WHITE);
        lblCantidad.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        lblCantidad.setBounds(40, 190, 100, 20);
        contentPanel.add(lblCantidad);

        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spnCantidad.setBounds(150, 190, 100, 25);
        contentPanel.add(spnCantidad);

        btnRegistrar = new JButton("Registrar");
        Estilos.estilarBoton(btnRegistrar, new Color(0, 150, 136), Color.WHITE);
        btnRegistrar.setBounds(80, 280, 120, 35);
        btnRegistrar.addActionListener(e -> registrarLote());
        contentPanel.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        Estilos.estilarBoton(btnCancelar, new Color(191, 26, 26), Color.WHITE);
        btnCancelar.setBounds(280, 280, 120, 35);
        btnCancelar.addActionListener(e -> dispose());
        contentPanel.add(btnCancelar);

        cargarVacunas();
    }

    @SuppressWarnings("unchecked")
    private void cargarVacunas() {
        cbxVacuna.addItem("<Seleccione Vacuna>");
        Object respuesta = ClienteSocket.enviar("LISTAR_VACUNAS", null);
        if (respuesta != null && respuesta instanceof ArrayList) {
            listaVacunas = (ArrayList<Vacuna>) respuesta;
            for (Vacuna v : listaVacunas) {
                cbxVacuna.addItem(v.getNombre());
            }
        }
    }

    private void registrarLote() {
        if (cbxVacuna.getSelectedIndex() <= 0 || txtNoLote.getText().trim().isEmpty() || txtFechaVencimiento.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (listaVacunas == null || listaVacunas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay vacunas cargadas en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int index = cbxVacuna.getSelectedIndex() - 1;
        if (index < 0 || index >= listaVacunas.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione una vacuna válida de la lista.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vacuna vacunaSeleccionada = listaVacunas.get(index);
        if (vacunaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "La vacuna seleccionada es inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.Date utilDate = txtFechaVencimiento.getDate();
        if (utilDate == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de vencimiento.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fechaVenc = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (fechaVenc.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "La fecha de vencimiento no puede ser en el pasado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad = (int) spnCantidad.getValue();

        if (loteActual == null) {
            LoteVacuna lote = new LoteVacuna();
            lote.setCodigoLote(0);
            lote.setVacuna(vacunaSeleccionada);
            lote.setNoLote(txtNoLote.getText().trim());
            lote.setFechaVencimiento(fechaVenc);
            lote.setCantidad(cantidad);

            Object respuesta = ClienteSocket.enviar("REG_LOTE_VACUNA", lote);
            boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Lote registrado con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el lote en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            loteActual.setVacuna(vacunaSeleccionada);
            loteActual.setNoLote(txtNoLote.getText().trim());
            loteActual.setFechaVencimiento(fechaVenc);
            loteActual.setCantidad(cantidad);

            Object respuesta = ClienteSocket.enviar("UPDATE_LOTE_VACUNA", loteActual);
            boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Lote actualizado con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el lote en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}