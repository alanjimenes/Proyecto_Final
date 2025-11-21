package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logico.Cita;
import logico.Clinica;
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

	public MisCitas(User usuario) {
		this.usuarioMedico = usuario;
		setTitle("Mis Citas de Hoy");
		setBounds(100, 100, 900, 500);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(60, 70, 123));
		contentPanel.add(panelNorte, BorderLayout.NORTH);

		String nombreMedico = "Desconocido";
		if(usuario.getCedula() != null && !usuario.getCedula().isEmpty()) {
			Medico m = Clinica.getInstancia().buscarMedicoCedula(usuario.getCedula());
			if (m != null) {
				nombreMedico = m.getNombre() + " " + m.getApellido();
			}
		}

		JLabel lblTitulo = new JLabel("Pacientes de Hoy para Dr/a: " + nombreMedico);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
		panelNorte.add(lblTitulo);

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					String codigo = table.getValueAt(index, 0).toString();
					citaSeleccionada = Clinica.getInstancia().buscarCita(codigo);
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

		String[] headers = {"Código", "Hora", "Paciente", "Cédula Paciente", "Estado"};
		model.setColumnIdentifiers(headers);
		table.setModel(model);
		scrollPane.setViewportView(table);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnAtender = new JButton("Realizar Consulta");
		btnAtender.setEnabled(false);
		btnAtender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (citaSeleccionada != null) {
					RealizarConsulta consulta = new RealizarConsulta(citaSeleccionada);
					consulta.setModal(true);
					consulta.setVisible(true);
					cargarCitasHoy();
					btnAtender.setEnabled(false);
					citaSeleccionada = null;
				}
			}
		});
		buttonPane.add(btnAtender);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCerrar);

		cargarCitasHoy();
	}

	private void cargarCitasHoy() {
		model.setRowCount(0);
		row = new Object[5];
		String cedulaMedico = usuarioMedico.getCedula();
		if(cedulaMedico == null || cedulaMedico.isEmpty()) {
			return;
		}
		Medico medicoActual = Clinica.getInstancia().buscarMedicoCedula(cedulaMedico);

		if (medicoActual == null) {
			return;
		}

		for (Cita cita : medicoActual.getCitasAsignadas()) {
			boolean esHoy = cita.getFechaHora().toLocalDate().equals(LocalDate.now());
			boolean esPendiente = cita.getEstado().equalsIgnoreCase("Pendiente");
			if (esHoy && esPendiente) {
				row[0] = cita.getCodigo_cita();
				row[1] = cita.getFechaHora().format(DateTimeFormatter.ofPattern("hh:mm a")); 
				row[2] = cita.getCliente().getNombre() + " " + cita.getCliente().getApellido();
				row[3] = cita.getCliente().getCedula();
				row[4] = cita.getEstado();
				model.addRow(row);
			}
		}
	}
}