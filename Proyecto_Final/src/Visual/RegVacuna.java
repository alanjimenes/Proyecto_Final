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
	private JButton btnLoteVacuna;

	public RegVacuna() {
		init();
		setTitle("Registrar Vacuna");
		vacunaActual = null;
		btnLoteVacuna.setEnabled(false);
	}

	public RegVacuna(Vacuna vacuna) {
		init();
		this.vacunaActual = vacuna;
		setTitle("Modificar Vacuna");
		okButton.setText("Actualizar");
		btnLoteVacuna.setEnabled(true);

		txtNombre.setText(vacuna.getNombre());
		txtDescripcion.setText(vacuna.getDescripcion());
	}

	private void init() {
		setResizable(false);
		setBounds(100, 100, 450, 340);
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

		JLabel lblDesc = new JLabel("Descripción:");
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

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(e -> {
			txtNombre.setText("");
			txtDescripcion.setText("");
		});
		Estilos.estilarBoton(btnLimpiar, new Color(127, 140, 141), Color.WHITE);
		btnLimpiar.setBounds(295, 210, 110, 35);
		contentPanel.add(btnLimpiar);

		btnLoteVacuna = new JButton("LoteVacuna");
		Estilos.estilarBoton(btnLoteVacuna, new Color(41, 128, 185), Color.WHITE);
		btnLoteVacuna.setBounds(25, 210, 150, 35);
		btnLoteVacuna.addActionListener(e -> {
			ListadoLotesVacuna listadoLotes = new ListadoLotesVacuna(vacunaActual);
			listadoLotes.setModal(true);
			listadoLotes.setVisible(true);
		});
		contentPanel.add(btnLoteVacuna);

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
	}

	private void registrarVacuna() {
		if(txtNombre.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
			return;
		}

		if (vacunaActual == null) {
			Vacuna aux = new Vacuna();
			aux.setCodigoVacuna(0);
			aux.setNombre(txtNombre.getText().trim());
			aux.setDescripcion(txtDescripcion.getText().trim());
			aux.setActivo(true);

			Object respuesta = ClienteSocket.enviar("REG_VACUNA", aux);
			boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

			if(exito) {
				JOptionPane.showMessageDialog(null, "Vacuna creada en el Servidor.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al registrar vacuna.");
			}
		} else {
			vacunaActual.setNombre(txtNombre.getText().trim());
			vacunaActual.setDescripcion(txtDescripcion.getText().trim());

			Object respuesta = ClienteSocket.enviar("UPDATE_VACUNA", vacunaActual);
			boolean exito = (respuesta != null && respuesta instanceof Boolean && (boolean) respuesta);

			if(exito) {
				JOptionPane.showMessageDialog(null, "Vacuna actualizada.");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Error al actualizar.");
			}
		}
	}
}