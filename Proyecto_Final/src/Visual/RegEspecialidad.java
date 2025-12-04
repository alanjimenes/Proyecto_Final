package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logico.Especialidad;
import java.awt.Font;
import java.awt.Toolkit;

public class RegEspecialidad extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private Especialidad especialidadActual = null;
	private JButton okButton;

	public RegEspecialidad() {
		init();
		setTitle("Registrar Especialidad");
	}

	public RegEspecialidad(Especialidad esp) {
		init();
		this.especialidadActual = esp;
		setTitle("Modificar Especialidad");
		okButton.setText("Actualizar");
		txtNombre.setText(esp.getNombre());
	}

	private void init() {
		setResizable(false);
		try { setIconImage(Toolkit.getDefaultToolkit().getImage(RegEspecialidad.class.getResource("/img/especialidad.png"))); } catch (Exception e) {}

		setBounds(100, 100, 490, 235);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre Especialidad:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblNombre.setBounds(22, 87, 159, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(201, 84, 251, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("Registrar");
		Estilos.estilarBoton(okButton, new Color(46, 204, 113), Color.WHITE);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestionar();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancelar");
		Estilos.estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);
		cancelButton.addActionListener(e -> dispose());
		buttonPane.add(cancelButton);

		JLabel lblTitulo = new JLabel("Gestión de Especialidades");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
		lblTitulo.setBounds(90, 11, 287, 37);
		contentPanel.add(lblTitulo);
	}

	private void gestionar() {
		if (txtNombre.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
			return;
		}

		if(especialidadActual == null) {
			ArrayList<Especialidad> lista = (ArrayList<Especialidad>) ClienteSocket.enviar("LISTAR_ESPECIALIDADES", null);
			String codigo = "ESP-" + (lista != null ? lista.size() + 1 : 1);

			Especialidad aux = new Especialidad(codigo, txtNombre.getText());
			boolean exito = (boolean) ClienteSocket.enviar("REG_ESPECIALIDAD", aux);

			if(exito) {
				JOptionPane.showMessageDialog(null, "Registrado.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al registrar.");
			}

		} else {
			especialidadActual.setNombre(txtNombre.getText());
			boolean exito = (boolean) ClienteSocket.enviar("UPDATE_ESPECIALIDAD", especialidadActual);

			if(exito) {
				JOptionPane.showMessageDialog(null, "Actualizado.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al actualizar.");
			}
		}
	}
}