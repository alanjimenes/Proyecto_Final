package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Cliente;
import logico.Clinica;
import java.awt.Toolkit;

public class ConsultarClientes extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static JTable table;
	public static DefaultTableModel modelo;
	public static Object[] row;
	private Cliente seleccionado = null;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JTextField txtFiltro;

	public ConsultarClientes() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultarClientes.class.getResource("/img/seguro-de-salud.png")));
		setTitle("Gestion de Clientes");
		setBounds(100, 100, 1000, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.desktop);
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			panel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				table.setFont(new Font("Tahoma", Font.PLAIN, 15));
				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						int index = table.getSelectedRow();
						if (index >= 0) {
							String codigo = table.getValueAt(index, 0).toString();
							seleccionado = Clinica.getInstancia().buscarClientePorCodigo(codigo);
							btnDelete.setEnabled(true);
							btnUpdate.setEnabled(true);
						}
					}
				});

				modelo = new DefaultTableModel() {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				String[] headers = { "Expediente", "Cédula", "Nombre", "Apellido", "Teléfono", "Estado Salud" };
				modelo.setColumnIdentifiers(headers);
				table.setModel(modelo);
			
                JTableHeader header = table.getTableHeader(); 
                header.setBackground(new Color(60, 70, 123)); 
                header.setForeground(Color.WHITE);      
                header.setOpaque(true);

                header.setReorderingAllowed(false);
                
				scrollPane.setViewportView(table);
				
			}
		}

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(60, 70, 123));
		contentPanel.add(panelNorte, BorderLayout.NORTH);
		{
			JLabel lblFiltro = new JLabel("Filtrar por Nombre:");
			lblFiltro.setForeground(Color.WHITE);
			lblFiltro.setFont(new Font("Bahnschrift", Font.BOLD, 18));
			panelNorte.add(lblFiltro);
		}
		{
			txtFiltro = new JTextField();
			txtFiltro.setFont(new Font("Tahoma", Font.PLAIN, 16));
			txtFiltro.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					cargarClientes(txtFiltro.getText());
				}
			});
			panelNorte.add(txtFiltro);
			txtFiltro.setColumns(20);
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(248, 244, 234));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			btnUpdate = new JButton("Modificar");
			btnUpdate.setForeground(Color.ORANGE);
			btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 16));
			btnUpdate.setEnabled(false);
			btnUpdate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (seleccionado != null) {
						RegClientes reg = new RegClientes(seleccionado);
						reg.setModal(true);
						reg.setVisible(true);
						cargarClientes("");
						resetBotones();
					}
				}
			});
			buttonPane.add(btnUpdate);
		}
		{
			btnDelete = new JButton("Desactivar");
			btnDelete.setForeground(Color.RED);
			btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
			btnDelete.setEnabled(false);
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (seleccionado != null) {
						int option = JOptionPane.showConfirmDialog(null, 
								"¿Seguro que desea desactivar al paciente " + seleccionado.getNombre() + "?",
								"Confirmar", JOptionPane.YES_NO_OPTION);

						if (option == JOptionPane.YES_OPTION) {
							Clinica.getInstancia().desactivarCliente(seleccionado);
							Clinica.getInstancia().guardarDatosClinica();
							cargarClientes("");
							resetBotones();
						}
					}
				}
			});
			buttonPane.add(btnDelete);
		}
		{
			JButton btnCancelar = new JButton("Cerrar");
			btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 16));
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			buttonPane.add(btnCancelar);
		}

		cargarClientes("");
	}

	private void cargarClientes(String filtro) {
		modelo.setRowCount(0);
		row = new Object[6];

		for (Cliente cli : Clinica.getInstancia().getClientes()) {
			if (cli.isActivo() && (filtro.isEmpty() || cli.getNombre().toLowerCase().contains(filtro.toLowerCase()))) {
				row[0] = cli.getNumExpediente();
				row[1] = cli.getCedula();
				row[2] = cli.getNombre();
				row[3] = cli.getApellido();
				row[4] = cli.getTelefono();
				row[5] = cli.isEnfermo() ? "Enfermo" : "Sano"; 
				modelo.addRow(row);
			}
		}
	}

	private void resetBotones() {
		seleccionado = null;
		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		table.clearSelection();
	}
}