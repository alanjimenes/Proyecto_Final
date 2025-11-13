package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.LineBorder;

import logico.Clinica;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Button;
import java.awt.Panel;

public class LoginUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCedula;
	private JPasswordField txtPassword;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginUser dialog = new LoginUser();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LoginUser() {
		setBounds(100, 100, 663, 483);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblUsuario = new JLabel("Ingrese el Usuario (Cedula): ");
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUsuario.setBounds(12, 29, 229, 35);
		contentPanel.add(lblUsuario);

		txtCedula = new JTextField();
		txtCedula.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtCedula.setBounds(238, 36, 200, 22);
		contentPanel.add(txtCedula);
		txtCedula.setColumns(10);

		JLabel lblPassword = new JLabel("Ingrese la Contrase\u00F1a:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(12, 89, 229, 35);
		contentPanel.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(235, 96, 203, 22);
		contentPanel.add(passwordField);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		{
			JButton btnEntrar = new JButton("Entrar");
			btnEntrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String cedula = txtCedula.getText(); 
					String password = new String(txtPassword.getPassword()); 
					if (cedula.isEmpty() || password.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Debe ingresar su cédula y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
						return; 
					}
					boolean exito = Clinica.getInstance().loginMedico(cedula, password);
					if (exito) {
						JOptionPane.showMessageDialog(null, "Bienvenido Dr. " + Clinica.getMedicoLogueado().getApellido(), "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
						dispose(); 
					} else {
						JOptionPane.showMessageDialog(null, "Cédula o contraseña incorrecta.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
						txtPassword.setText(""); 
					}
				}
			});

			buttonPane.add(btnEntrar);
			getRootPane().setDefaultButton(btnEntrar);
		}
		{
			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose(); 
				}
			});

			buttonPane.add(btnCancelar);
		}

	}
}
