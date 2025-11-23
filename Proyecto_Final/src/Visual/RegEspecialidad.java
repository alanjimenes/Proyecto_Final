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
import java.awt.Toolkit;
import java.awt.Color;

public class RegEspecialidad extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;

	public RegEspecialidad() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegEspecialidad.class.getResource("/img/especialidad.png")));
		setTitle("Registrar Especialidad");
		setBounds(100, 100, 481, 194);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(60, 70, 123));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre Especialidad:");
		lblNombre.setBounds(28, 22, 130, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(168, 19, 200, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 129, 475, 33);
			contentPanel.add(buttonPane);
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("Registrar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (!txtNombre.getText().isEmpty()) {
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
