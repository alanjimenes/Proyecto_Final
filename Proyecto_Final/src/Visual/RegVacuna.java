package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logico.Vacuna;

public class RegVacuna extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextArea txtDescripcion;

	public RegVacuna() {
		setResizable(false);
		setTitle("Registrar Vacuna");
		setBounds(100, 100, 490, 350);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre Vacuna:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblNombre.setBounds(22, 70, 159, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(150, 67, 300, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblDesc = new JLabel("Descripción:");
		lblDesc.setForeground(Color.WHITE);
		lblDesc.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblDesc.setBounds(22, 110, 159, 14);
		contentPanel.add(lblDesc);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 130, 428, 100);
		contentPanel.add(scrollPane);

		txtDescripcion = new JTextArea();
		txtDescripcion.setLineWrap(true);
		scrollPane.setViewportView(txtDescripcion);

		{
			JButton okButton = new JButton("Registrar");
			contentPanel.add(okButton);
			Estilos.estilarBoton(okButton, new Color(46, 204, 113), Color.WHITE);
			okButton.setBounds(22, 250, 110, 35);
			okButton.addActionListener(new ActionListener() {
				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					if (!txtNombre.getText().isEmpty()) {
						Object resp = ClienteSocket.enviar("LISTAR_VACUNAS", null);
						ArrayList<Vacuna> lista = (resp instanceof ArrayList) ? (ArrayList<Vacuna>) resp
								: new ArrayList<>();

						int id = lista.size() + 1;
						String codigo = "VAC-" + id;

						Vacuna aux = new Vacuna(codigo, txtNombre.getText(), txtDescripcion.getText());
						boolean exito = (boolean) ClienteSocket.enviar("REG_VACUNA", aux);

						if (exito) {
							JOptionPane.showMessageDialog(null, "Vacuna creada con éxito.");
							dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Error al registrar.");
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

			cancelButton.setBounds(340, 250, 110, 35);
			contentPanel.add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
		}

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtNombre.setText("");
				txtDescripcion.setText("");
			}
		});
		Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE);
		btnLimpiar.setBounds(181, 250, 110, 35);
		contentPanel.add(btnLimpiar);

		JLabel lblTitulo = new JLabel("Registrar Vacuna");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 18));
		lblTitulo.setBounds(90, 11, 287, 37);
		contentPanel.add(lblTitulo);
	}
}