package Utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.FileOutputStream;

public class GeneradorPDF {


	/**
	 * PROCESO: Exporta las filas y columnas visibles de una JTable Swing hacia un archivo PDF.
	 * * ENTRADAS:
	 * - table: Instancia de JTable que contiene la información estructurada a exportar.
	 * - titulo: Cadena de texto que actuará como encabezado del reporte y nombre base del archivo.
	 * * SALIDA: Ninguna (método void).
	 * * FLUJO DE LLAMADAS:
	 * 1. Inicializa JFileChooser y asigna "titulo.pdf" como nombre por defecto.
	 * 2. Si la selección del usuario es aprobada (JFileChooser.APPROVE_OPTION):
	 *    - Instancia Document y PdfWriter vinculados al FileOutputStream del archivo seleccionado.
	 *    - document.open().
	 *    - Añade el título formateado y centrado al documento.
	 *    - Copia las cabeceras del JTable mediante table.getColumnName(i).
	 *    - Recorre las filas (getRowCount) y celdas (getValueAt) agregando los valores como cadenas.
	 *    - document.add(pdfTable) y document.close().
	 * 3. Muestra JOptionPane con el resultado de la operación.
	 */

	public static void exportarJTablePDF(JTable table, String titulo) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new java.io.File(titulo + ".pdf"));

			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(fileChooser.getSelectedFile()));
				document.open();

				Paragraph p = new Paragraph(titulo);
				p.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(p);
				document.add(new Paragraph(" ")); 

				PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

				for (int i = 0; i < table.getColumnCount(); i++) {
					pdfTable.addCell(table.getColumnName(i));
				}

				for (int rows = 0; rows < table.getRowCount(); rows++) {
					for (int cols = 0; cols < table.getColumnCount(); cols++) {
						Object val = table.getModel().getValueAt(rows, cols);
						pdfTable.addCell(val != null ? val.toString() : "");
					}
				}

				document.add(pdfTable);
				document.close();
				JOptionPane.showMessageDialog(null, "�PDF Generado con �xito!");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
		}
	}
}