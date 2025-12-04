package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Enfermedad;

public class ConsultarEnfermedades extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private Enfermedad seleccionado = null;
	private JButton btnUpdate;
	private JButton btnDelete;

	public ConsultarEnfermedades() {
		try {
			//setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarEnfermedades.class.getResource("/img/seguro-de-salud.png")));
		} catch (Exception e) {}

		setTitle("Catálogo de Enfermedades");
		setBounds(100, 100, 700, 500);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.desktop);
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					String codigo = table.getValueAt(index, 0).toString();
					seleccionado = buscarEnfermedadLocal(codigo);

					btnUpdate.setEnabled(true);
					btnDelete.setEnabled(true);
				}
			}
		});

		model = new DefaultTableModel(new Object[] { "Código", "Nombre", "Vigilancia" }, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);

		JTableHeader header = table.getTableHeader();
		header.setBackground(new Color(60, 70, 123));
		header.setForeground(Color.WHITE);
		header.setOpaque(true);

		scrollPane.setViewportView(table);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(60, 70, 123));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnNueva = new JButton("Nueva");
		Estilos.estilarBoton(btnNueva, new Color(176, 206, 136), Color.WHITE);
		btnNueva.addActionListener(e -> {
			RegEnfermedades reg = new RegEnfermedades();
			reg.setModal(true);
			reg.setVisible(true);
			cargarEnfermedades();
			resetBotones();
		});
		buttonPane.add(btnNueva);

		btnUpdate = new JButton("Modificar");
		Estilos.estilarBoton(btnUpdate, new Color(41, 128, 185), Color.WHITE);
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(e -> {
			if(seleccionado != null) {
				RegEnfermedades reg = new RegEnfermedades(seleccionado);
				reg.setModal(true);
				reg.setVisible(true);
				cargarEnfermedades();
				resetBotones();
			}
		});
		buttonPane.add(btnUpdate);

		btnDelete = new JButton("Eliminar");
		Estilos.estilarBoton(btnDelete, new Color(231, 76, 60), Color.WHITE);
		btnDelete.setText("Desactivar");
		btnDelete.addActionListener(e -> {
			if(seleccionado != null) {
				int opt = JOptionPane.showConfirmDialog(null, 
						"¿Seguro desea desactivar la enfermedad " + seleccionado.getNombre() + "?", 
						"Confirmar", JOptionPane.YES_NO_OPTION);
				if(opt == JOptionPane.YES_OPTION) {
					seleccionado.setActivo(false); 
					boolean exito = (boolean) ClienteSocket.enviar("UPDATE_ENFERMEDAD", seleccionado);

					if(exito) {
						JOptionPane.showMessageDialog(null, "Enfermedad desactivada.");
						cargarEnfermedades();
						resetBotones();
					}
				}
			}
		});
		buttonPane.add(btnDelete);

		JButton btnCerrar = new JButton("Cerrar");
		Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		cargarEnfermedades();
	}

	@SuppressWarnings("unchecked")
	private void cargarEnfermedades() {
		model.setRowCount(0);
		ArrayList<Enfermedad> lista = (ArrayList<Enfermedad>) ClienteSocket.enviar("LISTAR_ENFERMEDADES", null);

		if (lista != null) {
			for (Enfermedad enf : lista) {
				if (enf.isActivo()) {
					String vig = enf.isVigilancia() ? "SÍ (ALERTA)" : "No";
					model.addRow(new Object[] { enf.getCodigo_sick(), enf.getNombre(), vig });
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Enfermedad buscarEnfermedadLocal(String codigo) {
		ArrayList<Enfermedad> lista = (ArrayList<Enfermedad>) ClienteSocket.enviar("LISTAR_ENFERMEDADES", null);
		if(lista != null) {
			for(Enfermedad e : lista) {
				if(e.getCodigo_sick().equals(codigo)) return e;
			}
		}
		return null;
	}

	private void resetBotones() {
		seleccionado = null;
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		table.clearSelection();
	}
}