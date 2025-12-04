package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logico.Medico;
import logico.User;

public class RegUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passField_1;
	private JPasswordField passField_2;
	private JComboBox<String> comboBox;
	private JComboBox<String> cbMedicos;
	private JLabel lblCedulaLink;

	public static void main(String[] args) {
		try {
			RegUser dialog = new RegUser();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegUser() {
		setTitle("Registrar Usuarios");
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(RegUser.class.getResource("/img/perfil(2).png")));
		} catch (Exception e) {
		}
		setBounds(100, 100, 563, 446);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombreUsuario = new JLabel("Nombre Usuario:");
		lblNombreUsuario.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblNombreUsuario.setForeground(Color.WHITE);
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
		passField_1.setBounds(69, 179, 167, 20);
		contentPanel.add(passField_1);

		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblTipo.setForeground(Color.WHITE);
		lblTipo.setBounds(265, 24, 97, 20);
		contentPanel.add(lblTipo);

		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		comboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] { "<Seleccione>", "Administrador", "Asistente", "Medico" }));
		comboBox.setBounds(265, 66, 147, 20);
		contentPanel.add(comboBox);

		JLabel lblConfirmarPassword = new JLabel("Confirmar Password:");
		lblConfirmarPassword.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblConfirmarPassword.setForeground(Color.WHITE);
		lblConfirmarPassword.setBounds(264, 152, 167, 14);
		contentPanel.add(lblConfirmarPassword);

		passField_2 = new JPasswordField();
		passField_2.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		passField_2.setColumns(10);
		passField_2.setBounds(265, 179, 180, 20);
		contentPanel.add(passField_2);

		lblCedulaLink = new JLabel("Seleccione Médico:");
		lblCedulaLink.setForeground(Color.WHITE);
		lblCedulaLink.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		lblCedulaLink.setBounds(69, 254, 180, 20);
		lblCedulaLink.setVisible(false);
		contentPanel.add(lblCedulaLink);

		cbMedicos = new JComboBox<String>();
		cbMedicos.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		cbMedicos.setBounds(265, 255, 180, 20);
		cbMedicos.setVisible(false);
		contentPanel.add(cbMedicos);

		// --- AQUI EMPIEZA LO QUE ARREGLÉ ---
		{
			JButton okButton = new JButton("Registrar");
			Estilos.estilarBoton(okButton, new Color(99, 163, 97), Color.WHITE);
			okButton.setBounds(69, 331, 127, 35);
			contentPanel.add(okButton);

			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String userTxt = textField.getText().trim();
					String pass1 = new String(passField_1.getPassword());
					String pass2 = new String(passField_2.getPassword());

					if (userTxt.isEmpty() || pass1.isEmpty()) {
						JOptionPane.showMessageDialog(contentPanel, "Complete todos los campos.", "Aviso",
								JOptionPane.WARNING_MESSAGE);
						return;
					}

					if (!pass1.equals(pass2)) {
						JOptionPane.showMessageDialog(contentPanel, "Las contraseñas no coinciden.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (comboBox.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(contentPanel, "Seleccione un tipo de usuario.");
						return;
					}

					String tipo = comboBox.getSelectedItem().toString();
					String cedulaLink = "";

					if (tipo.equalsIgnoreCase("Medico")) {
						if (cbMedicos.getSelectedItem() == null
								|| cbMedicos.getSelectedItem().toString().startsWith("<Seleccione")) {
							JOptionPane.showMessageDialog(contentPanel, "Debe seleccionar un médico de la lista.");
							return;
						}
						String seleccion = cbMedicos.getSelectedItem().toString();
						try {
							int inicio = seleccion.lastIndexOf("(") + 1;
							int fin = seleccion.lastIndexOf(")");
							cedulaLink = seleccion.substring(inicio, fin);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(contentPanel, "Error al procesar la cédula del médico.");
							return;
						}
					}

					User user = new User(tipo, userTxt, pass1, cedulaLink);

					// ENVIAMOS AL SERVER
					Object respuestaServidor = ClienteSocket.enviar("REG_USER", user);

					// --- MANEJO DE ERRORES CORRECTO ---
					if (respuestaServidor == null) {
						JOptionPane.showMessageDialog(contentPanel,
								"Error grave: No hubo respuesta del servidor.\n",
								"Error de Conexión", JOptionPane.ERROR_MESSAGE);
					} else if (respuestaServidor instanceof Boolean) {
						if ((boolean) respuestaServidor) {
							JOptionPane.showMessageDialog(contentPanel, "¡Usuario registrado exitosamente!");
							dispose();
						} else {
							JOptionPane.showMessageDialog(contentPanel,
									"El nombre de usuario '" + userTxt + "' ya está en uso.", "Usuario Duplicado",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(contentPanel, "Respuesta desconocida del servidor.");
					}
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		// --- FIN DE LO QUE ARREGLÉ ---

		{
			JButton cancelButton = new JButton("Cancel");
			Estilos.estilarBoton(cancelButton, new Color(191, 26, 26), Color.WHITE);
			cancelButton.setBounds(285, 331, 127, 35);
			contentPanel.add(cancelButton);
			cancelButton.addActionListener(e -> dispose());
			cancelButton.setActionCommand("Cancel");
		}

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String seleccionado = comboBox.getSelectedItem().toString();
				if (seleccionado.equalsIgnoreCase("Medico")) {
					cbMedicos.setVisible(true);
					lblCedulaLink.setVisible(true);
					cargarListaMedicos();
				} else {
					cbMedicos.setVisible(false);
					lblCedulaLink.setVisible(false);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void cargarListaMedicos() {
		cbMedicos.removeAllItems();
		cbMedicos.addItem("<Seleccione Médico>");
		Object respuesta = ClienteSocket.enviar("LISTAR_MEDICOS", null);

		if (respuesta != null && respuesta instanceof java.util.ArrayList) {
			ArrayList<Medico> lista = (ArrayList<Medico>) respuesta;
			for (Medico med : lista) {
				cbMedicos.addItem(med.getNombre() + " (" + med.getCedula() + ")");
			}
		}
	}
}