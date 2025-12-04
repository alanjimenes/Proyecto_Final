package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Cita;
import logico.Medico;
import logico.User;

public class MisCitas extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private Object[] row;
	private Cita citaSeleccionada = null;
	private JButton btnAtender;
	private User usuarioMedico;
	private Medico medicoActual;

	public MisCitas(User usuario) {
		this.usuarioMedico = usuario;

		// Icono y Título
		setTitle("Mis Citas de Hoy");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MisCitas.class.getResource("/img/dato-de-registro.png"))); // Asegúrate de tener la imagen o quitar esta línea si da error
		setBounds(100, 100, 900, 500);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());

		// Fondo General
		contentPanel.setBackground(new Color(255, 255, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		// --- PANEL NORTE (TÍTULO) ---
		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(60, 70, 123));
		panelNorte.setPreferredSize(new Dimension(10, 50)); // Altura fija para que se vea bien
		panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 12)); // Centrar verticalmente
		contentPanel.add(panelNorte, BorderLayout.NORTH);

		// Lógica para obtener nombre del médico
		String nombreMedico = "Desconocido";
		if (usuario.getCedula() != null) {
			Object resp = ClienteSocket.enviar("BUSCAR_MEDICO", usuario.getCedula());
			if (resp instanceof Medico) {
				medicoActual = (Medico) resp;
				nombreMedico = medicoActual.getNombre() + " " + medicoActual.getApellido();
			}
		}

		JLabel lblTitulo = new JLabel("Pacientes de Hoy para Dr/a: " + nombreMedico);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
		panelNorte.add(lblTitulo);

		// --- PANEL CENTRAL (TABLA) ---
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(Color.WHITE); // Fondo blanco para el scroll
		scrollPane.setBorder(null); // Quitar borde feo del scroll
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setRowHeight(30); // Filas más altas para mejor lectura
		table.setSelectionBackground(new Color(232, 246, 255)); // Azul muy claro al seleccionar
		table.setSelectionForeground(Color.BLACK);
		table.setGridColor(new Color(230, 230, 230)); // Líneas de cuadrícula sutiles
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setShowVerticalLines(false); // Solo líneas horizontales (diseño moderno)

		// Evento Click
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					String codigo = table.getValueAt(index, 0).toString();
					citaSeleccionada = (Cita) ClienteSocket.enviar("BUSCAR_CITA", codigo);
					btnAtender.setEnabled(true);
				}
			}
		});

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.setColumnIdentifiers(new String[] { "Código", "Hora", "Paciente", "Cédula Paciente", "Estado" });
		table.setModel(model);

		// --- ESTILIZADO DEL HEADER (ENCABEZADO AZUL) ---
		JTableHeader header = table.getTableHeader();
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setBackground(new Color(60, 70, 123));
				setForeground(Color.WHITE);
				setFont(new Font("Bahnschrift", Font.BOLD, 14));
				setHorizontalAlignment(JLabel.CENTER);
				setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255))); 
				return this;
			}
		});

		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		scrollPane.setViewportView(table);

		// --- PANEL SUR (BOTONES) ---
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnAtender = new JButton("Realizar Consulta");
		Estilos.estilarBoton(btnAtender, new Color(41, 128, 185), Color.WHITE); // Azul brillante
		btnAtender.setEnabled(false);
		btnAtender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (citaSeleccionada != null) {
					// Verificamos si la ventana RealizarConsulta existe
					try {
						RealizarConsulta consulta = new RealizarConsulta(citaSeleccionada);
						consulta.setModal(true);
						consulta.setVisible(true);
						cargarCitasHoy(); // Recargar al volver
						btnAtender.setEnabled(false);
						citaSeleccionada = null;
					} catch (Exception ex) {
						// Si RealizarConsulta no existe o da error, mostramos mensaje
						javax.swing.JOptionPane.showMessageDialog(null, "Abriendo consulta... (Asegúrese de tener la clase RealizarConsulta)");
					}
				}
			}
		});
		buttonPane.add(btnAtender);

		JButton btnCerrar = new JButton("Cerrar");
		Estilos.estilarBoton(btnCerrar, new Color(231, 76, 60), Color.WHITE); // Rojo
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		cargarCitasHoy();
	}

	private void cargarCitasHoy() {
		model.setRowCount(0);
		row = new Object[5];

		if (medicoActual == null)
			return;

		
		medicoActual = (Medico) ClienteSocket.enviar("BUSCAR_MEDICO", medicoActual.getCedula());

		if (medicoActual != null && medicoActual.getCitasAsignadas() != null) {
			for (Cita cita : medicoActual.getCitasAsignadas()) {
				boolean esHoy = cita.getFechaHora().toLocalDate().equals(LocalDate.now());
				boolean esPendiente = cita.getEstado().equalsIgnoreCase("Pendiente");

				if (esHoy && esPendiente) {
					row[0] = cita.getCodigo_cita();
					row[1] = cita.getFechaHora().toLocalTime().toString();
					row[2] = cita.getCliente().getNombre() + " " + cita.getCliente().getApellido();
					row[3] = cita.getCliente().getCedula();
					row[4] = cita.getEstado();
					model.addRow(row);
				}
			}
		}
	}
}