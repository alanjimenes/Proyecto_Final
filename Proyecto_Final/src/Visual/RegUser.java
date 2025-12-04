package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Clinica;
import logico.Control;
import logico.Medico;
import logico.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import java.awt.Toolkit;

public class RegUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passField_1;
	private JPasswordField passField_2;
	private JComboBox comboBox;
	private JTextField txtCedulaEnlace;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegUser dialog = new RegUser();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegUser() {
		setTitle("Registrar Usuarios");
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegUser.class.getResource("/img/perfil(2).png")));
		setBounds(100, 100, 563, 446);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombreUsuario = new JLabel("Nombre Usuario:");
		lblNombreUsuario.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblNombreUsuario.setForeground(Color.WHITE ) ;
		lblNombreUsuario.setBounds(69, 27, 127, 14);
		contentPanel.add(lblNombreUsuario);

		textField = new JTextField();
		textField.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		textField.setBounds(69, 66, 147, 20);
		contentPanel.add(textField);
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setBounds(69, 152, 97, 14);
		contentPanel.add(lblPassword);

		passField_1 = new JPasswordField();
		passField_1.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		passField_1.setColumns(10);
		passField_1.setBounds(69, 204, 147, 20);
		contentPanel.add(passField_1);

		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblTipo.setForeground(Color.WHITE);
		lblTipo.setBounds(265, 24, 97, 20);
		contentPanel.add(lblTipo);

		comboBox = new JComboBox();
		comboBox.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "<Seleccione>", "Administrador", "Asistente", "Medico" }));
		comboBox.setBounds(265, 66, 147, 20);
		contentPanel.add(comboBox);

		JLabel lblConfirmarPassword = new JLabel("Confirmar Password:");
		lblConfirmarPassword.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblConfirmarPassword.setForeground(Color.WHITE);
		lblConfirmarPassword.setBounds(263, 152, 167, 14);
		contentPanel.add(lblConfirmarPassword);

		passField_2 = new JPasswordField();
		passField_2.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		passField_2.setColumns(10);
		passField_2.setBounds(265, 204, 147, 20);
		contentPanel.add(passField_2);

		JLabel lblCedulaLink = new JLabel("Cédula (Solo Médicos):");
		lblCedulaLink.setForeground(Color.WHITE);
		lblCedulaLink.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblCedulaLink.setBounds(69, 255, 180, 20);
		contentPanel.add(lblCedulaLink);

		txtCedulaEnlace = new JTextField();
		txtCedulaEnlace.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		txtCedulaEnlace.setBounds(265, 255, 147, 20);
		txtCedulaEnlace.setVisible(false); 
		lblCedulaLink.setVisible(false);
		contentPanel.add(txtCedulaEnlace);
				{
					JButton okButton = new JButton("Registrar");
					Estilos.estilarBoton(okButton, new Color(99, 163, 97), Color.WHITE);

					okButton.setBounds(69, 331, 127, 35);
					contentPanel.add(okButton);
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String pass1 = new String(passField_1.getPassword());
							String pass2 = new String(passField_2.getPassword());
		
							if (!pass1.equals(pass2)) {
								JOptionPane.showMessageDialog(contentPanel, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							String tipo = comboBox.getSelectedItem().toString();
							String cedulaLink = "";
		
							if (tipo.equalsIgnoreCase("Medico")) {
								cedulaLink = txtCedulaEnlace.getText().trim();
								if (cedulaLink.isEmpty()) {
									JOptionPane.showMessageDialog(contentPanel, "Cédula obligatoria para médicos.");
									return;
								}
								Medico med = (Medico) ClienteSocket.enviar("BUSCAR_MEDICO", cedulaLink);
								if (med == null) {
									JOptionPane.showMessageDialog(contentPanel, "No existe médico con esa cédula en el Servidor.");
									return;
								}
							}
							User user = new User(tipo, textField.getText(), pass1, cedulaLink);
							boolean respuesta = (boolean) ClienteSocket.enviar("REG_USER", user);
		
							if (respuesta) {
								JOptionPane.showMessageDialog(contentPanel, "¡Usuario registrado en el Servidor!");
								dispose();
							} else {
								JOptionPane.showMessageDialog(contentPanel, "Error al guardar en el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					});
					okButton.setActionCommand("OK");
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("Cancel");
					Estilos.estilarBoton(cancelButton, new Color(191, 26, 26), Color.WHITE);
					cancelButton.setBounds(285, 331, 127, 35);
					contentPanel.add(cancelButton);
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					cancelButton.setActionCommand("Cancel");
				}

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String seleccionado = comboBox.getSelectedItem().toString();

				if (seleccionado.equalsIgnoreCase("Medico")) {
					txtCedulaEnlace.setVisible(true);
					lblCedulaLink.setVisible(true);
				} else {
					txtCedulaEnlace.setVisible(false);
					lblCedulaLink.setVisible(false);
					txtCedulaEnlace.setText(""); 
				}
			}
		});
	}
}
