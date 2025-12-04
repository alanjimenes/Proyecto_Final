

package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import logico.Consulta;
import logico.Enfermedad;
import logico.Historial;
import logico.RegistroVacunacion;
import logico.Vacuna;
import javax.swing.UIManager;
import java.awt.Toolkit;

public class RealizarConsulta extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Cita citaActual;
	private JTextArea txtSintomas;
	private JTextArea txtDiagnostico;
	private JTextArea txtAntecedentes;
	private JTextArea txtTratamiento;
	private ArrayList<Enfermedad> listaEnfermedadesGlobal;

	private String presion = "N/A";
	private int pulso = 0;
	private float temp = 0;
	private float peso = 0;
	private float talla = 0;
	private boolean signosRegistrados = false;

	private JList<String> listDisponibles;
	private DefaultListModel<String> modelDisponibles;
	private JList<String> listDiagnosticadas;
	private DefaultListModel<String> modelDiagnosticadas;
	private ArrayList<Enfermedad> enfermedadesSeleccionadas;
	private JCheckBox chkResumen;

	public RealizarConsulta(Cita cita) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RealizarConsulta.class.getResource("/img/dato-de-registro.png")));
		this.citaActual = cita;
		this.enfermedadesSeleccionadas = new ArrayList<>();

		setTitle("Consulta Médica - Paciente: " + cita.getCliente().getNombre());
		setBounds(100, 100, 1000, 740);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panelInfo = new JPanel();
		panelInfo.setBackground(new Color(60, 70, 123));
		panelInfo.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos del Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));
		panelInfo.setBounds(10, 11, 960, 60);
		contentPanel.add(panelInfo);
		panelInfo.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre: " + cita.getCliente().getNombre() + " " + cita.getCliente().getApellido());
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblNombre.setBounds(20, 25, 300, 20);
		panelInfo.add(lblNombre);

		JLabel lblCedula = new JLabel("Cédula: " + cita.getCliente().getCedula());
		lblCedula.setForeground(Color.WHITE);
		lblCedula.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblCedula.setBounds(350, 28, 200, 14);
		panelInfo.add(lblCedula);

		boolean tieneHistorial = !cita.getCliente().getHistorial().getConsultas().isEmpty();

		JButton btnVerHistorial = new JButton("Ver Historial");
		Estilos.estilarBoton(btnVerHistorial, new Color(41, 128, 185), Color.WHITE);
		btnVerHistorial.setEnabled(tieneHistorial);
		btnVerHistorial.setBounds(776, 18, 164, 27);
		btnVerHistorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verHistorialFiltrado();
			}
		});
		panelInfo.add(btnVerHistorial);

		JPanel panelInformacion = new JPanel();
		panelInformacion.setBackground(new Color(60, 70, 123));
		panelInformacion.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(255, 255, 255)));
		panelInformacion.setBounds(10, 80, 470, 250);
		contentPanel.add(panelInformacion);
		panelInformacion.setLayout(null);

		JLabel lblAnt = new JLabel("Antecedentes (Alergias, Cirugías):");
		lblAnt.setForeground(Color.WHITE);
		lblAnt.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblAnt.setBounds(10, 11, 283, 23);
		panelInformacion.add(lblAnt);

		JScrollPane scAnt = new JScrollPane();
		scAnt.setBounds(10, 40, 450, 60);
		panelInformacion.add(scAnt);
		txtAntecedentes = new JTextArea();
		txtAntecedentes.setLineWrap(true);
		scAnt.setViewportView(txtAntecedentes);

		JLabel lblSint = new JLabel("Enfermedad Actual (Síntomas):");
		lblSint.setForeground(Color.WHITE);
		lblSint.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblSint.setBounds(10, 110, 250, 14);
		panelInformacion.add(lblSint);

		JScrollPane scSint = new JScrollPane();
		scSint.setBounds(10, 130, 450, 100);
		panelInformacion.add(scSint);
		txtSintomas = new JTextArea();
		txtSintomas.setLineWrap(true);
		scSint.setViewportView(txtSintomas);

		JPanel panelExamen = new JPanel();
		panelExamen.setForeground(Color.WHITE);
		panelExamen.setBackground(new Color(60, 70, 123));
		panelExamen.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Examen y Diagn\u00F3stico", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));
		panelExamen.setBounds(490, 80, 480, 250);
		contentPanel.add(panelExamen);
		panelExamen.setLayout(null);

		JButton btnSignos = new JButton("Registrar Signos Vitales");
		Estilos.estilarBoton(btnSignos, new Color(41, 128, 185), Color.WHITE);
		btnSignos.setBounds(10, 25, 200, 30);
		btnSignos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarSignosVitales();
			}
		});
		panelExamen.add(btnSignos);

		JLabel lblDiag = new JLabel("Diagnóstico:");
		lblDiag.setForeground(Color.WHITE);
		lblDiag.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblDiag.setBounds(20, 66, 200, 22);
		panelExamen.add(lblDiag);

		JScrollPane scDiag = new JScrollPane();
		scDiag.setBounds(10, 90, 460, 140);
		panelExamen.add(scDiag);
		txtDiagnostico = new JTextArea();
		txtDiagnostico.setLineWrap(true);
		scDiag.setViewportView(txtDiagnostico);

		JPanel panelEnf = new JPanel();
		panelEnf.setBackground(new Color(60, 70, 123));
		panelEnf.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Enfermedades Controladas", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));
		panelEnf.setBounds(10, 340, 470, 250);
		contentPanel.add(panelEnf);
		panelEnf.setLayout(null);

		JScrollPane spDisp = new JScrollPane(); 
		spDisp.setBounds(10, 40, 180, 180); 
		panelEnf.add(spDisp);
		modelDisponibles = new DefaultListModel<>(); 
		listDisponibles = new JList<>(modelDisponibles); 
		spDisp.setViewportView(listDisponibles);

		JScrollPane spDiag = new JScrollPane(); 
		spDiag.setBounds(270, 40, 180, 180); 
		panelEnf.add(spDiag);
		modelDiagnosticadas = new DefaultListModel<>(); 
		listDiagnosticadas = new JList<>(modelDiagnosticadas); 
		spDiag.setViewportView(listDiagnosticadas);

		JButton btnRight = new JButton(">"); 
		Estilos.estilarBoton(btnRight, new Color(41, 128, 185), Color.WHITE);
		btnRight.setBounds(200, 100, 60, 25);
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverEnfermedadDerecha();
			}
		});
		panelEnf.add(btnRight);

		JButton btnLeft = new JButton("<"); 
		Estilos.estilarBoton(btnLeft, new Color(41, 128, 185), Color.WHITE);
		btnLeft.setBounds(200, 140, 60, 25);
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverEnfermedadIzquierda();
			}
		});
		panelEnf.add(btnLeft);

		cargarEnfermedades();

		JPanel panelPlan = new JPanel();
		panelPlan.setBackground(new Color(60, 70, 123));
		panelPlan.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Plan y Tratamiento", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));
		panelPlan.setBounds(490, 340, 480, 250);
		contentPanel.add(panelPlan);
		panelPlan.setLayout(null);

		JLabel lblRx = new JLabel("Receta Médica / Indicaciones:");
		lblRx.setForeground(Color.WHITE);
		lblRx.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblRx.setBounds(10, 20, 250, 14);
		panelPlan.add(lblRx);

		JScrollPane scRx = new JScrollPane();
		scRx.setBounds(10, 40, 460, 150);
		panelPlan.add(scRx);
		txtTratamiento = new JTextArea();
		txtTratamiento.setLineWrap(true);
		scRx.setViewportView(txtTratamiento);

		chkResumen = new JCheckBox("Agregar al Resumen Clínico");
		chkResumen.setBackground(new Color(60, 70, 123));
		chkResumen.setForeground(Color.WHITE);
		chkResumen.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		chkResumen.setBounds(10, 200, 250, 20);
		panelPlan.add(chkResumen);

		JButton btnTerminar = new JButton("Terminar y Generar Receta");
		Estilos.estilarBoton(btnTerminar, new Color(99, 163, 97), Color.WHITE);
		btnTerminar.setBounds(292, 649, 186, 34);
		contentPanel.add(btnTerminar);
		Estilos.estilarBoton(btnTerminar, new Color(46, 204, 113), Color.WHITE);

		JButton btnCancelar = new JButton("Cancelar");
		Estilos.estilarBoton(btnCancelar, new Color(191, 26, 26), Color.WHITE);
		btnCancelar.setBounds(522, 649, 98, 34);
		contentPanel.add(btnCancelar);
		Estilos.estilarBoton(btnCancelar, new Color(231, 76, 60), Color.WHITE);

		JButton btnHistorial = new JButton("Ver Historial Previo");
		Estilos.estilarBoton(btnHistorial, new Color(127, 140, 141), Color.WHITE);
		btnHistorial.setBounds(70, 649, 186, 34);
		contentPanel.add(btnHistorial);
		btnHistorial.setForeground(Color.BLACK);

		JButton btnVacuna = new JButton("Aplicar Vacuna");
		Estilos.estilarBoton(btnVacuna, new Color(110, 140, 251), Color.WHITE);
		btnVacuna.setBounds(673, 649, 134, 34);
		contentPanel.add(btnVacuna);
		btnVacuna.setForeground(Color.BLACK);
		btnVacuna.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				ArrayList<logico.Vacuna> vacunas = (ArrayList<logico.Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);

				if(vacunas == null || vacunas.isEmpty()) {
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
					logico.RegistroVacunacion reg = new logico.RegistroVacunacion(citaActual.getCliente(), vacunaObj, java.time.LocalDate.now(), citaActual.getMedico(), true);

					boolean exito = (boolean) ClienteSocket.enviar("APLICAR_VACUNA", reg);

					if(exito) {
						JOptionPane.showMessageDialog(null, "Vacuna " + seleccion + " aplicada y registrada en el Servidor.");
					} else {
						JOptionPane.showMessageDialog(null, "Error al registrar vacuna.");
					}
				}
			}
		});
		btnHistorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verHistorialFiltrado();
			}
		});
		btnCancelar.addActionListener(e -> dispose());
		btnTerminar.addActionListener(e -> terminarConsulta());;

		cargarEnfermedades();
	}

	private void registrarSignosVitales() {
		JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
		JTextField txtPresion = new JTextField(presion);
		JTextField txtPulso = new JTextField(String.valueOf(pulso));
		JTextField txtTemp = new JTextField(String.valueOf(temp));
		JTextField txtPeso = new JTextField(String.valueOf(peso));
		JTextField txtTalla = new JTextField(String.valueOf(talla));

		panel.add(new JLabel("Presión Arterial (mm/Hg):")); panel.add(txtPresion);
		panel.add(new JLabel("Frecuencia Cardíaca (lpm):")); panel.add(txtPulso);
		panel.add(new JLabel("Temperatura (°C):")); panel.add(txtTemp);
		panel.add(new JLabel("Peso (Kg):")); panel.add(txtPeso);
		panel.add(new JLabel("Talla (m):")); panel.add(txtTalla);

		int res = JOptionPane.showConfirmDialog(this, panel, "Registro de Signos Vitales", JOptionPane.OK_CANCEL_OPTION);
		if (res == JOptionPane.OK_OPTION) {
			try {
				presion = txtPresion.getText();
				pulso = Integer.parseInt(txtPulso.getText());
				temp = Float.parseFloat(txtTemp.getText());
				peso = Float.parseFloat(txtPeso.getText());
				talla = Float.parseFloat(txtTalla.getText());
				signosRegistrados = true;
				JOptionPane.showMessageDialog(this, "Signos vitales registrados.");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error en los datos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void terminarConsulta() {
		if (txtSintomas.getText().isEmpty() || txtDiagnostico.getText().isEmpty() || txtTratamiento.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe completar Síntomas, Diagnóstico y Tratamiento.");
			return;
		}
		if (!signosRegistrados) {
			int opt = JOptionPane.showConfirmDialog(null, "No ha registrado Signos Vitales. ¿Desea continuar?", "Advertencia", JOptionPane.YES_NO_OPTION);
			if (opt == JOptionPane.NO_OPTION) return;
		}

		Consulta consultaTemp = new Consulta("TEMP", java.time.LocalDate.now(), txtSintomas.getText(), txtDiagnostico.getText(), citaActual.getMedico(), citaActual.getCliente());
		consultaTemp.setAntecedentes(txtAntecedentes.getText());
		consultaTemp.setRecetaMedica(txtTratamiento.getText());
		consultaTemp.setEnfermedadesDiag(enfermedadesSeleccionadas);

		boolean guardado = (boolean) ClienteSocket.enviar("REG_CONSULTA", consultaTemp); 

		if (guardado) {
			int word = JOptionPane.showConfirmDialog(null, "¿Desea descargar la Receta Médica en Word?", "Receta", JOptionPane.YES_NO_OPTION);
			if (word == JOptionPane.YES_OPTION) {
				GeneradorReportes.generarReceta(consultaTemp);
			}
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Error al guardar en el sistema.");
		}
	}


	@SuppressWarnings("unchecked")
	private void cargarEnfermedades() {
		modelDisponibles.clear();
		listaEnfermedadesGlobal = (ArrayList<Enfermedad>) ClienteSocket.enviar("LISTAR_ENFERMEDADES", null);

		if(listaEnfermedadesGlobal != null) {
			for (Enfermedad enf : listaEnfermedadesGlobal) {
				modelDisponibles.addElement(enf.getNombre());
			}
		}
	}

	private void moverEnfermedadDerecha() {
		String seleccion = listDisponibles.getSelectedValue();
		if (seleccion != null) {
			modelDisponibles.removeElement(seleccion);
			modelDiagnosticadas.addElement(seleccion);

			for (Enfermedad enf : listaEnfermedadesGlobal) {
				if (enf.getNombre().equals(seleccion)) {
					enfermedadesSeleccionadas.add(enf);

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