package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logico.Cita;
import logico.Clinica;
import logico.Enfermedad;
import logico.Historial;
import logico.Vacuna;

public class RealizarConsulta extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Cita citaActual;
	private JTextArea txtSintomas;
	private JTextArea txtDiagnostico;
	private JCheckBox chkResumen;
	private JList<String> listDisponibles;
	private DefaultListModel<String> modelDisponibles;
	private JList<String> listDiagnosticadas;
	private DefaultListModel<String> modelDiagnosticadas;
	private ArrayList<Enfermedad> enfermedadesSeleccionadas;

	public RealizarConsulta(Cita cita) {
		this.citaActual = cita;
		this.enfermedadesSeleccionadas = new ArrayList<>();

		setTitle("Consulta Médica - Paciente: " + cita.getCliente().getNombre());
		setBounds(100, 100, 850, 600);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panelInfo = new JPanel();
		panelInfo.setBorder(new TitledBorder(null, "Información del Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInfo.setBounds(10, 11, 814, 70);
		contentPanel.add(panelInfo);
		panelInfo.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre: " + cita.getCliente().getNombre() + " " + cita.getCliente().getApellido());
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNombre.setBounds(20, 25, 300, 20);
		panelInfo.add(lblNombre);

		JLabel lblCedula = new JLabel("Cédula: " + cita.getCliente().getCedula());
		lblCedula.setBounds(350, 28, 200, 14);
		panelInfo.add(lblCedula);

		JLabel lblSintomas = new JLabel("Síntomas:");
		lblSintomas.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSintomas.setBounds(20, 92, 100, 14);
		contentPanel.add(lblSintomas);

		JScrollPane scrollSintomas = new JScrollPane();
		scrollSintomas.setBounds(20, 117, 380, 100);
		contentPanel.add(scrollSintomas);

		txtSintomas = new JTextArea();
		scrollSintomas.setViewportView(txtSintomas);

		JLabel lblDiagnostico = new JLabel("Diagnóstico / Observaciones:");
		lblDiagnostico.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDiagnostico.setBounds(20, 228, 200, 14);
		contentPanel.add(lblDiagnostico);

		JScrollPane scrollDiagnostico = new JScrollPane();
		scrollDiagnostico.setBounds(20, 253, 380, 100);
		contentPanel.add(scrollDiagnostico);

		txtDiagnostico = new JTextArea();
		scrollDiagnostico.setViewportView(txtDiagnostico);

		JPanel panelEnfermedades = new JPanel();
		panelEnfermedades.setBorder(new TitledBorder(null, "Diagnosticar Enfermedad", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelEnfermedades.setBounds(420, 92, 404, 261);
		contentPanel.add(panelEnfermedades);
		panelEnfermedades.setLayout(null);

		JScrollPane scrollDisp = new JScrollPane();
		scrollDisp.setBounds(10, 45, 150, 200);
		panelEnfermedades.add(scrollDisp);

		modelDisponibles = new DefaultListModel<>();
		listDisponibles = new JList<>(modelDisponibles);
		scrollDisp.setViewportView(listDisponibles);

		JLabel lblDisp = new JLabel("Disponibles");
		lblDisp.setBounds(10, 25, 100, 14);
		panelEnfermedades.add(lblDisp);

		JScrollPane scrollDiag = new JScrollPane();
		scrollDiag.setBounds(234, 45, 150, 200);
		panelEnfermedades.add(scrollDiag);

		modelDiagnosticadas = new DefaultListModel<>();
		listDiagnosticadas = new JList<>(modelDiagnosticadas);
		scrollDiag.setViewportView(listDiagnosticadas);

		JLabel lblSelect = new JLabel("Seleccionadas");
		lblSelect.setBounds(234, 25, 100, 14);
		panelEnfermedades.add(lblSelect);

		JButton btnAdd = new JButton(">");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverEnfermedadDerecha();
			}
		});
		btnAdd.setBounds(170, 100, 54, 23);
		panelEnfermedades.add(btnAdd);

		JButton btnRemove = new JButton("<");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverEnfermedadIzquierda();
			}
		});
		btnRemove.setBounds(170, 140, 54, 23);
		panelEnfermedades.add(btnRemove);

		chkResumen = new JCheckBox("Marcar para Resumen Clínico");
		chkResumen.setFont(new Font("Tahoma", Font.BOLD, 12));
		chkResumen.setBounds(20, 370, 250, 23);
		contentPanel.add(chkResumen);

		cargarEnfermedades();

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnTerminar = new JButton("Terminar Consulta");
		btnTerminar.setBackground(new Color(60, 179, 113));
		btnTerminar.setForeground(Color.BLACK);
		btnTerminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminarConsulta();
			}
		});
		buttonPane.add(btnTerminar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCancelar);

		JButton btnHistorial = new JButton("Ver Historial Previo");
		btnHistorial.setForeground(Color.BLACK);
		btnHistorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verHistorialFiltrado();
			}
		});
		buttonPane.add(btnHistorial);

		JButton btnVacuna = new JButton("Aplicar Vacuna");
		btnVacuna.setForeground(Color.BLACK);
		btnVacuna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<logico.Vacuna> vacunas = logico.Clinica.getInstancia().getVacunas();
				if(vacunas.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No hay vacunas en el sistema.");
					return;
				}
				String[] nombresVacunas = new String[vacunas.size()];
				for(int i=0; i<vacunas.size(); i++) {
					nombresVacunas[i] = vacunas.get(i).getNombre();
				}

				String seleccion = (String) JOptionPane.showInputDialog(
						null, 
						"Seleccione la vacuna a aplicar:", 
						"Vacunación", 
						JOptionPane.QUESTION_MESSAGE, 
						null, 
						nombresVacunas, 
						nombresVacunas[0]);

				if(seleccion != null) {
					logico.Vacuna vacunaObj = null;
					for(logico.Vacuna v : vacunas) {
						if(v.getNombre().equals(seleccion)) {
							vacunaObj = v; break;
						}
					}
					logico.Clinica.getInstancia().aplicarVacunaCliente(citaActual.getCliente(), vacunaObj, citaActual.getMedico());
					logico.Clinica.getInstancia().guardarDatosClinica();
					JOptionPane.showMessageDialog(null, "Vacuna " + seleccion + " aplicada y registrada.");
				}
			}
		});
		buttonPane.add(btnVacuna);
	}

	private void cargarEnfermedades() {
		modelDisponibles.clear();
		for (Enfermedad enf : Clinica.getInstancia().getEnfermedades()) {
			modelDisponibles.addElement(enf.getNombre());
		}
	}

	private void moverEnfermedadDerecha() {
		String seleccion = listDisponibles.getSelectedValue();
		if (seleccion != null) {
			modelDisponibles.removeElement(seleccion);
			modelDiagnosticadas.addElement(seleccion);
			for (Enfermedad enf : Clinica.getInstancia().getEnfermedades()) {
				if (enf.getNombre().equals(seleccion)) {
					enfermedadesSeleccionadas.add(enf);
					/* MEJORA IMPLEMENTADA
					 * Si esta bajo vigilancia marca la casilla de resumen y avisa al medico
					 */
					if (enf.isVigilancia()) {
						chkResumen.setSelected(true);
						chkResumen.setEnabled(false); 
						chkResumen.setText("Marcar para Resumen (OBLIGATORIO POR VIGILANCIA)");
						chkResumen.setForeground(Color.RED);
					}
					break;
				}
			}
		}
	}

	private void moverEnfermedadIzquierda() {
		String seleccion = listDiagnosticadas.getSelectedValue();
		if (seleccion != null) {
			modelDiagnosticadas.removeElement(seleccion);
			modelDisponibles.addElement(seleccion);
			enfermedadesSeleccionadas.removeIf(enf -> enf.getNombre().equals(seleccion));
		}
	}

	private void terminarConsulta() {
		if (txtSintomas.getText().isEmpty() || txtDiagnostico.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe detallar los síntomas y el diagnóstico.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que desea finalizar la consulta?", "Confirmar", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			boolean iniciada = Clinica.getInstancia().iniciarConsulta(citaActual, txtSintomas.getText(), txtDiagnostico.getText());

			if (iniciada) {
				int totalConsultas = citaActual.getCliente().getHistorial().getConsultas().size();
				logico.Consulta consultaObj = citaActual.getCliente().getHistorial().getConsultas().get(totalConsultas - 1);
				consultaObj.setEnfermedadesDiag(enfermedadesSeleccionadas);
				consultaObj.setAgregarAlResumen(chkResumen.isSelected());
				if (!enfermedadesSeleccionadas.isEmpty()) {
					citaActual.getCliente().setEnfermo(true);
				}
				Clinica.getInstancia().guardarDatosClinica();

				JOptionPane.showMessageDialog(null, "Consulta finalizada con éxito.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al guardar la consulta.");
			}
		}
	}

	private void verHistorialFiltrado() {
		logico.Historial historial = citaActual.getCliente().getHistorial();
		ArrayList<logico.Consulta> consultasVisibles = historial.getConsultasVisibleMedico(citaActual.getMedico());

		if (consultasVisibles.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay consultas previas visibles para este médico.");
			return;
		}

		JDialog dialogHistorial = new JDialog();
		dialogHistorial.setTitle("Historial Clínico Filtrado - " + citaActual.getCliente().getNombre());
		dialogHistorial.setSize(800, 400);
		dialogHistorial.setLocationRelativeTo(this);
		dialogHistorial.getContentPane().setLayout(new BorderLayout());

		String[] headers = {"Fecha", "Síntomas", "Diagnóstico", "Médico"};
		javax.swing.table.DefaultTableModel modelHist = new javax.swing.table.DefaultTableModel();
		modelHist.setColumnIdentifiers(headers);

		for (logico.Consulta c : consultasVisibles) {
			Object[] row = new Object[4];
			row[0] = c.getFechaConsulta().toString();
			row[1] = c.getSintomas();
			row[2] = c.getDiagnostico();
			row[3] = c.getMedico().getNombre() + " (" + c.getMedico().getEspecialidad().getNombre() + ")";

			if (c.bajoVigilancia()) {
				row[2] = "[VIGILANCIA] " + row[2];
			}
			modelHist.addRow(row);
		}

		JTable tableHist = new JTable(modelHist);
		tableHist.setEnabled(false);
		JScrollPane scroll = new JScrollPane(tableHist);

		dialogHistorial.getContentPane().add(scroll, BorderLayout.CENTER);
		dialogHistorial.setModal(true);
		dialogHistorial.setVisible(true);
	}
}