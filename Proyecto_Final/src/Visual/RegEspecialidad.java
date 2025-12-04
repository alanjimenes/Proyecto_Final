package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logico.Clinica;
import logico.Especialidad;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

public class RegEspecialidad extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;

	public RegEspecialidad() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegEspecialidad.class.getResource("/img/especialidad.png")));
		setTitle("Registrar Especialidad");
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
		{
			JButton okButton = new JButton("Registrar");
			contentPanel.add(okButton);
			Estilos.estilarBoton(okButton, new Color(46, 204, 113), Color.WHITE);
			okButton.setBounds(22, 150, 110, 35);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!txtNombre.getText().isEmpty()) {
						String codigo = "ESP-" + System.currentTimeMillis() % 100000;
						Especialidad aux = new Especialidad(codigo, txtNombre.getText());

						boolean exito = (boolean) ClienteSocket.enviar("REG_ESPECIALIDAD", aux);

						if (exito) {
							JOptionPane.showMessageDialog(null, "Especialidad creada con éxito en el servidor.");
							txtNombre.setText("");
						} else {
							JOptionPane.showMessageDialog(null, "Error al registrar especialidad en el servidor.");
						}
					} else {
						JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
					}
				}
			});

			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancelar");
			Estilos.estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);

			cancelButton.setBounds(342, 150, 110, 35);
			contentPanel.add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
		}

		JLabel lblTitulo = new JLabel("Registrar Especialidad");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
		lblTitulo.setBounds(90, 11, 287, 37);
		contentPanel.add(lblTitulo);

		JButton btnLimpiar = new JButton("Limpiar");

		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtNombre.setText("");
			}
		});
		Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE);
		btnLimpiar.setBounds(184, 150, 110, 35);
		contentPanel.add(btnLimpiar);
	}
}
