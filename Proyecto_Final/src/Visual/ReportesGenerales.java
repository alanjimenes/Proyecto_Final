package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Cita;
import logico.Cliente;
import logico.Consulta;
import logico.Vacuna;

public class ReportesGenerales extends JDialog {

	private JTabbedPane tabbedPane;
	private Color colorPrimario = new Color(60, 70, 123);
	private Color colorVerde = new Color(46, 204, 113);
	private Color colorRojo = new Color(231, 76, 60);

	private JTable tableCitas;
	private DefaultTableModel modelCitas;
	private JTextField txtFiltroCitas;

	private JTable tableConsultas;
	private DefaultTableModel modelConsultas;

	private JTable tableVacunas;
	private DefaultTableModel modelVacunas;

	public ReportesGenerales() {
		setTitle("Centro de Reportes - Clínica UNPHU");
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(ReportesGenerales.class.getResource("/img/dato-de-registro.png")));
		} catch (Exception e) {}

		setSize(1000, 650);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(colorPrimario);
		panelNorte.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelNorte, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("Sistema Central de Reportes");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 24));
		panelNorte.add(lblTitulo);

		JPanel panelCentral = new JPanel(new BorderLayout());
		panelCentral.setBackground(Color.WHITE);
		getContentPane().add(panelCentral, BorderLayout.CENTER);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Bahnschrift", Font.PLAIN, 14));

		JPanel panelCitas = crearPanelCitas();
		tabbedPane.addTab(" Reporte de Citas ", null, panelCitas, null);

		JPanel panelConsultas = crearPanelConsultas();
		tabbedPane.addTab(" Reporte de Consultas ", null, panelConsultas, null);

		JPanel panelVacunas = crearPanelVacunas();
		tabbedPane.addTab(" Reporte de Vacunas ", null, panelVacunas, null);

		panelCentral.add(tabbedPane, BorderLayout.CENTER);

		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelSur.setBackground(colorPrimario);
		getContentPane().add(panelSur, BorderLayout.SOUTH);

		JButton btnCerrar = new JButton("Cerrar Ventana");
		Estilos.estilarBoton(btnCerrar, new Color(127, 140, 141), Color.WHITE);
		btnCerrar.addActionListener(e -> dispose());
		panelSur.add(btnCerrar);

		cargarCitas();
		cargarConsultas();
		cargarVacunas();
	}

	private JPanel crearPanelCitas() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.setBackground(Color.WHITE);
		top.setBorder(new TitledBorder(new LineBorder(colorPrimario), "Filtros de Búsqueda", TitledBorder.LEADING, TitledBorder.TOP, null, colorPrimario));

		JLabel lblFiltro = new JLabel("Buscar (Cliente/Médico):");
		lblFiltro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		top.add(lblFiltro);

		txtFiltroCitas = new JTextField(20);
		Estilos.estilarCampo(txtFiltroCitas);
		top.add(txtFiltroCitas);

		JButton btnBuscar = new JButton("Filtrar");
		Estilos.estilarBoton(btnBuscar, colorPrimario, Color.WHITE);
		btnBuscar.addActionListener(e -> cargarCitas());
		top.add(btnBuscar);

		JButton btnPDF = new JButton("Exportar a PDF");
		Estilos.estilarBoton(btnPDF, colorRojo, Color.WHITE);
		btnPDF.addActionListener(e -> GeneradorPDF.exportarJTablePDF(tableCitas, "Reporte_Citas"));
		top.add(btnPDF);

		panel.add(top, BorderLayout.NORTH);

		modelCitas = new DefaultTableModel(new String[]{"Código", "Fecha", "Paciente", "Médico", "Estado"}, 0) {
			public boolean isCellEditable(int row, int column) { return false; }
		};
		tableCitas = new JTable(modelCitas);
		estilarTabla(tableCitas);

		JScrollPane scroll = new JScrollPane(tableCitas);
		scroll.getViewport().setBackground(Color.WHITE);
		panel.add(scroll, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelConsultas() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.setBackground(Color.WHITE);
		top.setBorder(new TitledBorder(new LineBorder(colorPrimario), "Acciones", TitledBorder.LEADING, TitledBorder.TOP, null, colorPrimario));

		JButton btnRecargar = new JButton("Recargar Todo");
		Estilos.estilarBoton(btnRecargar, colorPrimario, Color.WHITE);
		btnRecargar.addActionListener(e -> cargarConsultas());
		top.add(btnRecargar);

		JButton btnPDF = new JButton("Exportar a PDF");
		Estilos.estilarBoton(btnPDF, colorRojo, Color.WHITE);
		btnPDF.addActionListener(e -> GeneradorPDF.exportarJTablePDF(tableConsultas, "Reporte_Consultas"));
		top.add(btnPDF);

		panel.add(top, BorderLayout.NORTH);

		modelConsultas = new DefaultTableModel(new String[]{"Código", "Fecha", "Paciente", "Médico", "Diagnóstico"}, 0) {
			public boolean isCellEditable(int row, int column) { return false; }
		};
		tableConsultas = new JTable(modelConsultas);
		estilarTabla(tableConsultas);

		JScrollPane scroll = new JScrollPane(tableConsultas);
		scroll.getViewport().setBackground(Color.WHITE);
		panel.add(scroll, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelVacunas() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.setBackground(Color.WHITE);
		top.setBorder(new TitledBorder(new LineBorder(colorPrimario), "Acciones", TitledBorder.LEADING, TitledBorder.TOP, null, colorPrimario));

		JButton btnPDF = new JButton("Exportar a PDF");
		Estilos.estilarBoton(btnPDF, colorRojo, Color.WHITE);
		btnPDF.addActionListener(e -> GeneradorPDF.exportarJTablePDF(tableVacunas, "Reporte_Vacunas"));
		top.add(btnPDF);

		panel.add(top, BorderLayout.NORTH);

		modelVacunas = new DefaultTableModel(new String[]{"Código", "Nombre", "Descripción"}, 0) {
			public boolean isCellEditable(int row, int column) { return false; }
		};
		tableVacunas = new JTable(modelVacunas);
		estilarTabla(tableVacunas);

		JScrollPane scroll = new JScrollPane(tableVacunas);
		scroll.getViewport().setBackground(Color.WHITE);
		panel.add(scroll, BorderLayout.CENTER);

		return panel;
	}

	private void estilarTabla(JTable table) {
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		table.setRowHeight(25);
		table.setSelectionBackground(new Color(200, 230, 255));
		table.setSelectionForeground(Color.BLACK);

		JTableHeader header = table.getTableHeader();
		header.setBackground(colorPrimario);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		header.setOpaque(true);
		header.setReorderingAllowed(false);
	}

	@SuppressWarnings("unchecked")
	private void cargarCitas() {
		modelCitas.setRowCount(0);
		String filtro = txtFiltroCitas.getText().trim().toLowerCase();

		ArrayList<Cita> lista = (ArrayList<Cita>) ClienteSocket.enviar("LISTAR_CITAS", null);
		if (lista != null) {
			for (Cita c : lista) {
				boolean coincide = filtro.isEmpty() || 
						c.getCliente().getCedula().contains(filtro) ||
						c.getMedico().getCedula().contains(filtro) ||
						c.getCliente().getNombre().toLowerCase().contains(filtro);

				if (coincide) {
					modelCitas.addRow(new Object[]{
							c.getCodigo_cita(),
							c.getFechaHora().toString().replace("T", " "),
							c.getCliente().getNombre() + " " + c.getCliente().getApellido(),
							c.getMedico().getNombre(),
							c.getEstado()
					});
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void cargarConsultas() {
		modelConsultas.setRowCount(0);
		ArrayList<Cliente> clientes = (ArrayList<Cliente>) ClienteSocket.enviar("LISTAR_CLIENTES", null);

		if (clientes != null) {
			for (Cliente cli : clientes) {
				for (Consulta con : cli.getHistorial().getConsultas()) {
					modelConsultas.addRow(new Object[]{
							con.getCodigo_cons(),
							con.getFechaConsulta().toString(),
							cli.getNombre() + " " + cli.getApellido(),
							con.getMedico().getNombre(),
							con.getDiagnostico()
					});
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void cargarVacunas() {
		modelVacunas.setRowCount(0);
		ArrayList<Vacuna> lista = (ArrayList<Vacuna>) ClienteSocket.enviar("LISTAR_VACUNAS", null);
		if (lista != null) {
			for (Vacuna v : lista) {
				modelVacunas.addRow(new Object[]{v.getCodigo_vacun(), v.getNombre(), v.getDescripcion()});
			}
		}
	}
}