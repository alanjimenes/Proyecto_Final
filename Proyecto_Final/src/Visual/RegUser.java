package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Control;
import logico.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;

public class RegUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JComboBox comboBox;

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
		textField.setBounds(20, 65, 127, 20); // y=49
		contentPanel.add(textField);
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setForeground(Color.BLACK);
		lblPassword.setBounds(20, 147, 97, 14);
		contentPanel.add(lblPassword);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_1.setColumns(10);
		textField_1.setBounds(20, 199, 147, 20); 
		contentPanel.add(textField_1);

		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTipo.setForeground(Color.BLACK);
		lblTipo.setBounds(216, 23, 97, 20);
		contentPanel.add(lblTipo);

		comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"<Seleccione>", "Administrador", "Asistente", "Medico"}));
		comboBox.setBounds(216, 65, 127, 20); // y=113
		contentPanel.add(comboBox);

		JLabel lblConfirmarPassword = new JLabel("Confirmar Password:");
		lblConfirmarPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblConfirmarPassword.setForeground(Color.BLACK);
		lblConfirmarPassword.setBounds(214, 147, 167, 14);
		contentPanel.add(lblConfirmarPassword);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_2.setColumns(10);
		textField_2.setBounds(216, 199, 147, 20);
		contentPanel.add(textField_2);

		JLabel lblFondoIcon = new JLabel("");
		lblFondoIcon.setVerticalAlignment(SwingConstants.TOP);
		lblFondoIcon.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFondoIcon.setBackground(Color.WHITE);
		lblFondoIcon.setIcon(new ImageIcon(RegUser.class.getResource("/icons/logo.png")));
		lblFondoIcon.setBounds(-181, -160, 778, 533);
		contentPanel.add(lblFondoIcon);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (!textField_1.getText().equals(textField_2.getText())) {
							JOptionPane.showMessageDialog(contentPanel, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}				        
						User user = new User(comboBox.getSelectedItem().toString(), textField.getText(), textField_1.getText());
						Control.getInstance().regUser(user);
						try {
							FileOutputStream empresa2 = new FileOutputStream("empresa.dat");
							ObjectOutputStream empresaWrite = new ObjectOutputStream(empresa2);
							empresaWrite.writeObject(Control.getInstance());
							empresa2.close();
							empresaWrite.close();				            
							JOptionPane.showMessageDialog(contentPanel, "¡Usuario registrado con éxito!");				            
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(contentPanel, "Error: No se pudo guardar el usuario en el fichero.", "Error de Fichero", JOptionPane.ERROR_MESSAGE);
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
