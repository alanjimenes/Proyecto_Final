package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logico.Clinica;
import logico.Especialidad;

public class RegEspecialidad extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;

	public RegEspecialidad() {
		setTitle("Registrar Especialidad");
		setBounds(100, 100, 450, 200);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre Especialidad:");
		lblNombre.setBounds(25, 45, 130, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(160, 42, 200, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Registrar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!txtNombre.getText().isEmpty()) {
							String codigo = "ESP-" + Clinica.getInstancia().getEspecialidades().size();
							Especialidad aux = new Especialidad(codigo, txtNombre.getText());
							Clinica.getInstancia().agregarEspecialidad(aux);
							Clinica.getInstancia().guardarDatosClinica();							
							JOptionPane.showMessageDialog(null, "Especialidad creada con éxito.");
							dispose();
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
