package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Enfermedad;
import java.awt.Toolkit;

public class RegEnfermedades extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextArea txtDescripcion;
	private JCheckBox chkVigilancia;
	private Enfermedad enfermedadActual = null;

	public RegEnfermedades() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegEnfermedades.class.getResource("/img/receta.png")));
		init();
	}

	public RegEnfermedades(Enfermedad enf) {
		init();
		this.enfermedadActual = enf;
		setTitle("Modificar Enfermedad");
		txtNombre.setText(enf.getNombre());
		txtDescripcion.setText(enf.getDescripcion());
		chkVigilancia.setSelected(enf.isVigilancia());
	}

	private void init() {
		setResizable(false);
		setTitle("Registrar Enfermedad");
		setBounds(100, 100, 450, 320);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblNombre.setBounds(25, 60, 130, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(25, 80, 380, 25);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblDesc = new JLabel("Descripción / Síntomas:");
		lblDesc.setForeground(Color.WHITE);
		lblDesc.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblDesc.setBounds(25, 120, 200, 14);
		contentPanel.add(lblDesc);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 140, 380, 60);
		contentPanel.add(scrollPane);

		txtDescripcion = new JTextArea();
		txtDescripcion.setLineWrap(true);
		scrollPane.setViewportView(txtDescripcion);

		chkVigilancia = new JCheckBox("Enfermedad bajo Vigilancia Epidemiol�gica");
		chkVigilancia.setBackground(new Color(60, 70, 123));
		chkVigilancia.setForeground(Color.ORANGE);
		chkVigilancia.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		chkVigilancia.setBounds(25, 210, 300, 23);
		contentPanel.add(chkVigilancia);

		JLabel lblTitulo = new JLabel("Gestión de Enfermedades");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 20));
		lblTitulo.setBounds(0, 11, 434, 30);
		contentPanel.add(lblTitulo);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(60,70,123));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton(enfermedadActual == null ? "Registrar" : "Actualizar");
		Estilos.estilarBoton(okButton, new Color(0, 150, 136), Color.WHITE);
		okButton.addActionListener(e -> registrar());
		buttonPane.add(okButton);

		JButton cancelButton = new JButton("Cancelar");
		Estilos.estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);
		cancelButton.addActionListener(e -> dispose());
		buttonPane.add(cancelButton);
	}

	private void registrar() {
		if (txtNombre.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre es obligatorio.");
			return;
		}

		if (enfermedadActual == null) {
			String codigo = "ENF-" + System.currentTimeMillis() % 10000;
			Enfermedad nueva = new Enfermedad(codigo, txtNombre.getText(), txtDescripcion.getText(),
					chkVigilancia.isSelected());
			Object respuesta = ClienteSocket.enviar("REG_ENFERMEDAD", nueva);
			boolean exito = (respuesta != null && (boolean) respuesta);

			if (exito) {
				JOptionPane.showMessageDialog(null, "Enfermedad registrada.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al registrar en el servidor.");
			}
		} else {
			enfermedadActual.setNombre(txtNombre.getText());
			enfermedadActual.setDescripcion(txtDescripcion.getText());
			enfermedadActual.setVigilancia(chkVigilancia.isSelected());

			boolean exito = (boolean) ClienteSocket.enviar("UPDATE_ENFERMEDAD", enfermedadActual);
			if (exito) {
				JOptionPane.showMessageDialog(null, "Enfermedad actualizada.");
				dispose();
			}
		}
	}
}