package Visual;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Cliente;
import logico.Enfermera;
import logico.LoteVacuna;
import logico.RegistroVacunacion;
import logico.User;
import logico.Vacuna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AplicarVacuna extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCedula;
    private JLabel lblNombreCliente;
    private JComboBox<Vacuna> cmbVacunas;
    private JComboBox<LoteVacuna> cmbLotes;
    private Cliente clienteEncontrado = null;
    private User usuarioActual; // Almacenamos el usuario logueado

    public AplicarVacuna(User usuarioLogueado) {
        this.usuarioActual = usuarioLogueado;

        setTitle("Aplicar Vacuna a Paciente");
        setResizable(false);
        setBounds(100, 100, 680, 380);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // --- PANEL DE BÚSQUEDA DE PACIENTE ---
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBounds(15, 15, 638, 95);
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new TitledBorder(null, "Buscar Paciente", TitledBorder.LEADING, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(60, 70, 123)));
        panelBusqueda.setLayout(null);
        contentPanel.add(panelBusqueda);

        JLabel lblCed = new JLabel("Cédula:");
        lblCed.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblCed.setBounds(20, 25, 70, 25);
        panelBusqueda.add(lblCed);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtCedula.setBounds(90, 22, 170, 30);
        panelBusqueda.add(txtCedula);

        JButton btnBuscar = new JButton("Buscar");
        Estilos.estilarBoton(btnBuscar, new Color(41, 128, 185), Color.WHITE);
        btnBuscar.setBounds(270, 22, 90, 30);
        panelBusqueda.add(btnBuscar);

        JButton btnBuscarSelector = new JButton("Seleccionar Paciente");
        Estilos.estilarBoton(btnBuscarSelector, new Color(60, 70, 123), Color.WHITE);
        btnBuscarSelector.setBounds(370, 22, 165, 30);
        panelBusqueda.add(btnBuscarSelector);

        lblNombreCliente = new JLabel("Paciente: (Ninguno seleccionado)");
        lblNombreCliente.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblNombreCliente.setForeground(new Color(40, 167, 69));
        lblNombreCliente.setBounds(20, 60, 500, 25);
        panelBusqueda.add(lblNombreCliente);

        // --- PANEL DE SELECCIÓN DE INMUNIZACIÓN ---
        JPanel panelVacuna = new JPanel();
        panelVacuna.setBounds(15, 125, 638, 135);
        panelVacuna.setBackground(Color.WHITE);
        panelVacuna.setBorder(new TitledBorder(null, "Selección de Inmunización", TitledBorder.LEADING, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(60, 70, 123)));
        panelVacuna.setLayout(null);
        contentPanel.add(panelVacuna);

        JLabel lblVacuna = new JLabel("Vacuna:");
        lblVacuna.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblVacuna.setBounds(20, 30, 95, 25);
        panelVacuna.add(lblVacuna);

        cmbVacunas = new JComboBox<>();
        cmbVacunas.setFont(new Font("Tahoma", Font.PLAIN, 13));
        cmbVacunas.setBounds(120, 28, 490, 30);
        panelVacuna.add(cmbVacunas);

        JLabel lblLote = new JLabel("Lote Vigente:");
        lblLote.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblLote.setBounds(20, 80, 95, 25);
        panelVacuna.add(lblLote);

        cmbLotes = new JComboBox<>();
        cmbLotes.setFont(new Font("Tahoma", Font.PLAIN, 13));
        cmbLotes.setBounds(120, 78, 490, 30);
        panelVacuna.add(cmbLotes);

        // --- PANEL INFERIOR DE BOTONES ---
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(60, 70, 123));
        buttonPane.setBounds(0, 275, 674, 70);
        contentPanel.add(buttonPane);
        buttonPane.setLayout(null);

        JButton btnAplicar = new JButton("Aplicar Vacuna");
        Estilos.estilarBoton(btnAplicar, new Color(46, 204, 113), Color.WHITE);
        btnAplicar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAplicar.setBounds(395, 18, 145, 35);
        buttonPane.add(btnAplicar);

        JButton btnCancelar = new JButton("Cancelar");
        Estilos.estilarBoton(btnCancelar, new Color(127, 140, 141), Color.WHITE);
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCancelar.setBounds(548, 18, 105, 35);
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        // --- EVENTOS Y LÓGICA ---

        // 1. Buscar Cliente por Cédula tipeada
        btnBuscar.addActionListener(e -> {
            String cedula = txtCedula.getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una cédula para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            buscarClientePorCedula(cedula);
        });

        // 2. Selector visual de clientes
        btnBuscarSelector.addActionListener(e -> {
            ConsultarClientes selector = new ConsultarClientes();
            selector.setLocationRelativeTo(this);
            selector.setModal(true);
            selector.setVisible(true);
            if (selector.getClienteSeleccionado() != null) {
                clienteEncontrado = selector.getClienteSeleccionado();
                txtCedula.setText(clienteEncontrado.getCedula());
                lblNombreCliente.setText("Paciente: " + clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellido());
            }
        });

        // 3. Cargar Combo de Vacunas al iniciar
        cargarVacunas();

        // 4. Cuando cambia la vacuna seleccionada, cargar sus lotes disponibles
        cmbVacunas.addActionListener(e -> {
            Vacuna seleccionada = (Vacuna) cmbVacunas.getSelectedItem();
            if (seleccionada != null) {
                cargarLotesDisponiblesPorVacuna(seleccionada.getCodigoVacuna());
            }
        });

        // 5. Acción del botón Aplicar
        btnAplicar.addActionListener(e -> {
            if (clienteEncontrado == null) {
                JOptionPane.showMessageDialog(this, "Debe buscar y seleccionar un cliente válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LoteVacuna loteSeleccionado = (LoteVacuna) cmbLotes.getSelectedItem();
            if (loteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "No hay lotes disponibles para aplicar de esta vacuna.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Confirma la aplicación de la vacuna al paciente " + clienteEncontrado.getNombre() + "?\nSe descontará 1 unidad del lote: " + loteSeleccionado.getNoLote(), "Confirmar Aplicación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Buscamos el objeto Enfermera asociado a la usuaria logueada mediante su cédula
                Object respEnfermera = ClienteSocket.enviar("BUSCAR_ENFERMERA", usuarioActual.getCedula());

                if (!(respEnfermera instanceof Enfermera)) {
                    JOptionPane.showMessageDialog(this, "No se encontró un perfil de enfermera asociado a este usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Enfermera enfermeraLogueada = (Enfermera) respEnfermera;

                // Creamos el registro y le inyectamos la enfermera encontrada
                RegistroVacunacion reg = new RegistroVacunacion();
                reg.setCliente(clienteEncontrado);
                reg.setLote(loteSeleccionado);
                reg.setEnfermera(enfermeraLogueada); // <-- Esto evita el conflicto de llave foránea
                reg.setFecha(LocalDateTime.now());

                Object respuesta = ClienteSocket.enviar("APLICAR_VACUNA", reg);
                boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

                if (exito) {
                    JOptionPane.showMessageDialog(this, "¡Vacuna aplicada con éxito! Stock descontado automáticamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar la aplicación de la vacuna.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void buscarClientePorCedula(String cedula) {
        Object respuesta = ClienteSocket.enviar("BUSCAR_CLIENTE_CEDULA", cedula);
        if (respuesta instanceof Cliente) {
            clienteEncontrado = (Cliente) respuesta;
            lblNombreCliente.setText("Paciente: " + clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellido());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarCliente();
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarVacunas() {
        cmbVacunas.removeAllItems();
        Object respuesta = ClienteSocket.enviar("LISTAR_VACUNAS", null);
        if (respuesta instanceof ArrayList) {
            ArrayList<Vacuna> lista = (ArrayList<Vacuna>) respuesta;
            for (Vacuna v : lista) {
                cmbVacunas.addItem(v);
            }
            if (!lista.isEmpty()) {
                cargarLotesDisponiblesPorVacuna(lista.get(0).getCodigoVacuna());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarLotesDisponiblesPorVacuna(int codigoVacuna) {
        cmbLotes.removeAllItems();
        Object respuesta = ClienteSocket.enviar("LISTAR_LOTES_DISPONIBLES_POR_VACUNA", codigoVacuna);
        if (respuesta instanceof ArrayList) {
            ArrayList<LoteVacuna> lista = (ArrayList<LoteVacuna>) respuesta;
            for (LoteVacuna l : lista) {
                cmbLotes.addItem(l);
            }
        }
    }

    private void limpiarCliente() {
        clienteEncontrado = null;
        lblNombreCliente.setText("Paciente: (Ninguno seleccionado)");
        txtCedula.setText("");
    }
}