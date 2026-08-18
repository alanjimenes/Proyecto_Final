package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Utils.ClienteSocket;
import logico.User;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		setTitle("Login - Sistema Clínico");
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/img/seguro-de-salud.png")));
		} catch (Exception e) {
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1006, 562);
		setResizable(false);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(new Color(60, 70, 123));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(60, 70, 123));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setForeground(Color.WHITE);
		lblUsuario.setFont(new Font("Bahnschrift", Font.BOLD, 20));
		lblUsuario.setBounds(102, 171, 146, 36);
		panel.add(lblUsuario);

		JLabel lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setForeground(Color.WHITE);
		lblContrasea.setFont(new Font("Bahnschrift", Font.BOLD, 20));
		lblContrasea.setBounds(102, 265, 123, 23);
		panel.add(lblContrasea);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(102, 218, 298, 36);
		panel.add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setBounds(102, 299, 298, 36);
		panel.add(passwordField);

		ActionListener actionLogin = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intentarLogin();
			}
		};

		textField.addActionListener(actionLogin);
		passwordField.addActionListener(actionLogin);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnLogin.setForeground(Color.BLACK);
		btnLogin.addActionListener(actionLogin);
		btnLogin.setBounds(190, 370, 117, 44);
		panel.add(btnLogin);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(463, -42, 527, 580);
		panel.add(panel_1);

		JLabel lblPrinIcon = new JLabel("");
		panel_1.add(lblPrinIcon);
		try {
			lblPrinIcon.setIcon(new ImageIcon(Login.class.getResource("/img/logo_adaptado.png")));
		} catch (Exception e) {
		}

		JLabel labelTitulo = new JLabel("Iniciar Sesión");
		labelTitulo.setForeground(Color.WHITE);
		labelTitulo.setFont(new Font("Monospaced", Font.BOLD, 32));
		labelTitulo.setBounds(102, 58, 270, 55);
		panel.add(labelTitulo);

		JSeparator separator = new JSeparator();
		separator.setBounds(102, 121, 264, 2);
		panel.add(separator);
	}

	private void intentarLogin() {
		String usuario = textField.getText().trim();
		String clave = new String(passwordField.getPassword());

		if (usuario.isEmpty() || clave.isEmpty()) {
			JOptionPane.showMessageDialog(contentPane, "Complete todos los campos.", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		User userTemp = new User(0, usuario, clave, "");
		Object respuesta = ClienteSocket.enviar("LOGIN", userTemp);

		if (respuesta instanceof User) {
			User usuarioLogueado = (User) respuesta;
			Principal frame = new Principal(usuarioLogueado);
			dispose();
			frame.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(contentPane, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}