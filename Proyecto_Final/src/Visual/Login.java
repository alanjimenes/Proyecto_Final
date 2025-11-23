package Visual;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Control;
import logico.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import java.awt.FlowLayout;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				FileInputStream usuarios;
				FileOutputStream usuarios2;
				ObjectInputStream usuariosRead;
				ObjectOutputStream usuariosWrite;
				try {
					usuarios = new FileInputStream("Usuarios.dat");
					usuariosRead = new ObjectInputStream(usuarios);
					Control temp = (Control) usuariosRead.readObject();
					Control.setControl(temp);
					usuarios.close();
					usuariosRead.close();
				} catch (FileNotFoundException e) {
					try {
						usuarios2 = new FileOutputStream("Usuarios.dat");
						usuariosWrite = new ObjectOutputStream(usuarios2);
						User aux = new User("Administrador", "Admin", "Admin", "Admin");
						Control.getInstance().regUser(aux);
						usuariosWrite.writeObject(Control.getInstance());
						usuarios2.close();
						usuariosWrite.close();
					} catch (FileNotFoundException e1) {
					} catch (IOException e1) {
						// TODO Auto-generated catch block
					}
				} catch (IOException e) {

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login");
		setBackground(new Color(60, 70, 123));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/img/seguro-de-salud.png")));
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
		panel.setForeground(Color.WHITE);
		panel.setBackground(new Color(60, 70, 123));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setForeground(Color.WHITE);
		lblUsuario.setFont(new Font("Bahnschrift", Font.BOLD, 20));
		lblUsuario.setBounds(102, 171, 146, 36);
		panel.add(lblUsuario);

		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
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

		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intentarLogin();
			}
		});

		passwordField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intentarLogin();
			}
		});

		JButton btnLogin = new JButton("Login");
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnLogin.setForeground(Color.BLACK);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				intentarLogin();
			}
		});

		btnLogin.setBounds(190, 370, 117, 44);
		panel.add(btnLogin);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(463, -42, 527, 580);
		panel.add(panel_1);
		panel_1.setBorder(null);
		JLabel lblPrinIcon = new JLabel("");
		panel_1.add(lblPrinIcon);
		lblPrinIcon.setIcon(new ImageIcon(Login.class.getResource("/img/logo_adaptado.png")));

		JLabel labelTitulo = new JLabel("Iniciar Sesion");
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
		User userTemp = new User(null, usuario, clave, null);
		User usuarioLogueado = (User) ClienteSocket.enviar("LOGIN", userTemp);
		if (usuarioLogueado != null) {
			Principal frame = new Principal(usuarioLogueado);
			dispose();
			frame.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(contentPane, "Usuario o clave incorrecta (o error de conexión).", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}

}