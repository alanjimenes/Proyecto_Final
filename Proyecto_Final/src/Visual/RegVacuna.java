package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.border.EmptyBorder;

import Utils.ClienteSocket;
import Utils.Estilos;
import logico.Vacuna;

public class RegVacuna extends JDialog {

	private final JPanel contentPanel = new JPanel(); 
	private JTextField txtNombre;
	private JTextArea txtDescripcion;
	private Vacuna vacunaActual = null; 
	private JButton okButton;

	public RegVacuna() {
		init();
		setTitle("Registrar Vacuna");
		vacunaActual = null;
	}

	public RegVacuna(Vacuna vacuna) {
		init();
		this.vacunaActual = vacuna;
		setTitle("Modificar Vacuna");
		okButton.setText("Actualizar");

		txtNombre.setText(vacuna.getNombre());
		txtDescripcion.setText(vacuna.getDescripcion());
	}

	private void init() {
		setResizable(false);
		try { 
			//setIconImage(Toolkit.getDefaultToolkit().getImage(RegVacuna.class.getResource("/img/vacuna.png")));
		} catch (Exception e) {}

		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre Vacuna:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblNombre.setBounds(25, 30, 130, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(25, 50, 380, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblDesc = new JLabel("Descripci�n:");
		lblDesc.setForeground(Color.WHITE);
		lblDesc.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblDesc.setBounds(25, 90, 130, 14);
		contentPanel.add(lblDesc);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 110, 380, 80);
		contentPanel.add(scrollPane);

		txtDescripcion = new JTextArea();
		txtDescripcion.setLineWrap(true);
		scrollPane.setViewportView(txtDescripcion);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("Registrar");
		Estilos.estilarBoton(okButton, new Color(46, 204, 113), Color.WHITE);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarVacuna();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancelar");
		Estilos.estilarBoton(cancelButton, new Color(231, 76, 60), Color.WHITE);
		cancelButton.addActionListener(e -> dispose());
		buttonPane.add(cancelButton);

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(e -> {
			txtNombre.setText("");
			txtDescripcion.setText("");
		});
		Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE);
		btnLimpiar.setBounds(181, 250, 110, 35);
		contentPanel.add(btnLimpiar); 
	}

	private void registrarVacuna() {
		if(txtNombre.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre no puede estar vac�o.");
			return;
		}

		if (vacunaActual == null) {
			ArrayList<Vacuna> lista = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);
			int id = (lista != null) ? lista.size() + 1 : 1;
			String codigo = "VAC-" + id;

			Vacuna aux = new Vacuna(codigo, txtNombre.getText(), txtDescripcion.getText());
			Object respuesta = ClienteSocket.enviar("REG_VACUNA", aux);
			boolean exito = (respuesta != null && (boolean) respuesta);

			if(exito) {
				JOptionPane.showMessageDialog(null, "Vacuna creada en el Servidor.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al registrar vacuna.");
			}
		} else {
			vacunaActual.setNombre(txtNombre.getText());
			vacunaActual.setDescripcion(txtDescripcion.getText());

			boolean exito = (boolean) ClienteSocket.enviar("UPDATE_VACUNA", vacunaActual);

			if(exito) {
				JOptionPane.showMessageDialog(null, "Vacuna actualizada.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al actualizar.");
			}
		}
	}
}