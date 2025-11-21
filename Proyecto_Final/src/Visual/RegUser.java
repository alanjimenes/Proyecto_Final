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
		setIconImage(Toolkit.getDefaultToolkit().getImage("/img/seguro-de-salud.png"));
		setBounds(100, 100, 636, 412);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombreUsuario = new JLabel("Nombre Usuario:");
		lblNombreUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNombreUsuario.setForeground(Color.BLACK);
		lblNombreUsuario.setBounds(20, 26, 127, 14);
		contentPanel.add(lblNombreUsuario);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(20, 65, 127, 20);
		contentPanel.add(textField);
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setForeground(Color.BLACK);
		lblPassword.setBounds(20, 147, 97, 14);
		contentPanel.add(lblPassword);

		passField_1 = new JPasswordField();
		passField_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passField_1.setColumns(10);
		passField_1.setBounds(20, 199, 147, 20);
		contentPanel.add(passField_1);

		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTipo.setForeground(Color.BLACK);
		lblTipo.setBounds(216, 23, 97, 20);
		contentPanel.add(lblTipo);

		comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox.setModel(
				new DefaultComboBoxModel(new String[] { "<Seleccione>", "Administrador", "Asistente", "Medico" }));
		comboBox.setBounds(216, 65, 127, 20);
		contentPanel.add(comboBox);

		JLabel lblConfirmarPassword = new JLabel("Confirmar Password:");
		lblConfirmarPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblConfirmarPassword.setForeground(Color.BLACK);
		lblConfirmarPassword.setBounds(214, 147, 167, 14);
		contentPanel.add(lblConfirmarPassword);

		passField_2 = new JPasswordField();
		passField_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passField_2.setColumns(10);
		passField_2.setBounds(216, 199, 147, 20);
		contentPanel.add(passField_2);

		JLabel lblCedulaLink = new JLabel("Cédula (Solo Médicos):");
		lblCedulaLink.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCedulaLink.setBounds(20, 250, 180, 20);
		contentPanel.add(lblCedulaLink);

		txtCedulaEnlace = new JTextField();
		txtCedulaEnlace.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtCedulaEnlace.setBounds(216, 250, 147, 20);
		txtCedulaEnlace.setEnabled(false); 
		contentPanel.add(txtCedulaEnlace);

		comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String seleccionado = comboBox.getSelectedItem().toString();
                
                if (seleccionado.equalsIgnoreCase("Medico")) {
                    txtCedulaEnlace.setEnabled(true);
                } else {
                   
                    txtCedulaEnlace.setEnabled(false);
                    txtCedulaEnlace.setText(""); 
                }
            }
        });
		
		
		
		JLabel lblFondoIcon = new JLabel("");
		lblFondoIcon.setVerticalAlignment(SwingConstants.TOP);
		lblFondoIcon.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFondoIcon.setBackground(Color.WHITE);
		lblFondoIcon.setIcon(new ImageIcon("/img/logo.png"));
		lblFondoIcon.setBounds(-191, -142, 801, 533);
		contentPanel.add(lblFondoIcon);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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
							cedulaLink = txtCedulaEnlace.getText();
							if (cedulaLink.isEmpty()) {
								JOptionPane.showMessageDialog(contentPanel, "Para un usuario Médico, la cédula es obligatoria.");
								return;
							}
							if (Clinica.getInstancia().buscarMedicoCedula(cedulaLink) == null) {
								JOptionPane.showMessageDialog(contentPanel, "No existe ningún médico registrado con esa cédula.");
								return;
							}
						}
						FileInputStream usuarios = null;
						ObjectInputStream usuariosRead = null;
						try {
							usuarios = new FileInputStream("Usuarios.dat");
							usuariosRead = new ObjectInputStream(usuarios);
							Control temp = (Control) usuariosRead.readObject();
							Control.setControl(temp); 
							usuarios.close();
							usuariosRead.close();
						} catch (Exception ex) {
						}
						User user = new User(tipo, textField.getText(), pass1, cedulaLink);
						Control.getInstance().regUser(user); 
						try {
							FileOutputStream usuarios2 = new FileOutputStream("Usuarios.dat");
							ObjectOutputStream usuariosWrite = new ObjectOutputStream(usuarios2);
							usuariosWrite.writeObject(Control.getInstance());
							usuarios2.close();
							usuariosWrite.close();
							JOptionPane.showMessageDialog(contentPanel, "¡Usuario registrado con éxito!");
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(contentPanel, "Error al guardar en disco.", "Error", JOptionPane.ERROR_MESSAGE);
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
