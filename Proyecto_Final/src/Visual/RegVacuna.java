package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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

import logico.Vacuna;

public class RegVacuna extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextArea txtDescripcion;

	public RegVacuna() {
		setTitle("Registrar Vacuna");
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre Vacuna:");
		lblNombre.setBounds(25, 30, 130, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(25, 50, 380, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblDesc = new JLabel("Descripción:");
		lblDesc.setBounds(25, 90, 130, 14);
		contentPanel.add(lblDesc);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 110, 380, 80);
		contentPanel.add(scrollPane);

		txtDescripcion = new JTextArea();
		scrollPane.setViewportView(txtDescripcion);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Registrar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!txtNombre.getText().isEmpty()) {
							ArrayList<Vacuna> lista = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);
							int id = (lista != null) ? lista.size() + 1 : 1;

							String codigo = "VAC-" + id;
							Vacuna aux = new Vacuna(codigo, txtNombre.getText(), txtDescripcion.getText());
							boolean exito = (boolean) ClienteSocket.enviar("REG_VACUNA", aux);

							if(exito) {
								JOptionPane.showMessageDialog(null, "Vacuna creada en el Servidor.");
								dispose();
							} else {
								JOptionPane.showMessageDialog(null, "Error al registrar vacuna.");
							}
						} else {
							JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
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