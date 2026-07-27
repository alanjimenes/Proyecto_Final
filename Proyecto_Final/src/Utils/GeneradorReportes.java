package Utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import logico.Consulta;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;

public class GeneradorReportes {

	public static void generarReceta(Consulta consulta) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File("Receta_" + consulta.getCliente().getNombre() + ".pdf"));

			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

				Document documento = new Document();
				PdfWriter.getInstance(documento, new FileOutputStream(fileChooser.getSelectedFile()));
				documento.open();

				Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
				Font fontNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
				Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);

				Paragraph titulo = new Paragraph("RECETA M�DICA - CL�NICA UNPHU", fontTitulo);
				titulo.setAlignment(Element.ALIGN_CENTER);
				documento.add(titulo);
				documento.add(new Paragraph(" ")); 

				documento.add(new Paragraph("Dr./Dra.: " + consulta.getMedico().getNombre() + " " + consulta.getMedico().getApellido(), fontNegrita));
				documento.add(new Paragraph("Especialidad: " + consulta.getMedico().getEspecialidad().getNombre(), fontNormal));
				documento.add(new Paragraph("-----------------------------------------------------------------------------"));

				documento.add(new Paragraph("Paciente: " + consulta.getCliente().getNombre() + " " + consulta.getCliente().getApellido(), fontNormal));
				documento.add(new Paragraph("C�dula: " + consulta.getCliente().getCedula(), fontNormal));
				documento.add(new Paragraph("Fecha: " + consulta.getFechaConsulta().toString(), fontNormal));
				documento.add(new Paragraph(" "));


				documento.add(new Paragraph("DIAGN�STICO:", fontNegrita));
				documento.add(new Paragraph(consulta.getDiagnostico(), fontNormal));
				documento.add(new Paragraph(" ")); 

				documento.add(new Paragraph("TRATAMIENTO / INDICACIONES:", fontNegrita));
				documento.add(new Paragraph(consulta.getRecetaMedica(), fontNormal));

				documento.close();

				JOptionPane.showMessageDialog(null, "�Receta PDF generada correctamente!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
		}
	}
}