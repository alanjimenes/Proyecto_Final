package Utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import logico.Consulta;
import logico.RecetaMedica;

import javax.swing.*;
import java.io.FileOutputStream;

public class GeneradorReportes {

	public static void generarReceta(Consulta consulta) {
		try {
			Document documento = new Document();
			PdfWriter.getInstance(documento, new FileOutputStream("RecetaMedica_" + consulta.getCodigoConsulta() + ".pdf"));
			documento.open();

			Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
			Font fontNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);

			Paragraph titulo = new Paragraph("RECETA MEDICA - CLINICA UNPHU", fontTitulo);
			titulo.setAlignment(Element.ALIGN_CENTER);
			documento.add(titulo);
			documento.add(new Paragraph(" "));

			String nombreMedico = (consulta.getMedico() != null) ? consulta.getMedico().getNombre() + " " + consulta.getMedico().getApellido() : "N/A";
			String espMedico = (consulta.getMedico() != null && consulta.getMedico().getEspecialidad() != null) ? consulta.getMedico().getEspecialidad().getNombre() : "N/A";

			documento.add(new Paragraph("Dr./Dra.: " + nombreMedico, fontNormal));
			documento.add(new Paragraph("Especialidad: " + espMedico, fontNormal));
			documento.add(new Paragraph("--------------------------------------------------------------------------------"));

			String nombreCliente = (consulta.getCliente() != null) ? consulta.getCliente().getNombre() + " " + consulta.getCliente().getApellido() : "N/A";
			String cedulaCliente = (consulta.getCliente() != null) ? consulta.getCliente().getCedula() : "N/A";
			String fechaConsulta = (consulta.getFechaConsulta() != null) ? consulta.getFechaConsulta().toString() : "N/A";

			documento.add(new Paragraph("Paciente: " + nombreCliente, fontNormal));
			documento.add(new Paragraph("Cedula: " + cedulaCliente, fontNormal));
			documento.add(new Paragraph("Fecha: " + fechaConsulta, fontNormal));
			documento.add(new Paragraph(" "));

			documento.add(new Paragraph("DIAGNOSTICO:", fontNegrita));
			documento.add(new Paragraph(consulta.getDiagnostico(), fontNormal));
			documento.add(new Paragraph(" "));

			documento.add(new Paragraph("TRATAMIENTO / INDICACIONES:", fontNegrita));
			if (consulta.getRecetas() != null && !consulta.getRecetas().isEmpty()) {
				for (RecetaMedica receta : consulta.getRecetas()) {
					documento.add(new Paragraph("- " + receta.toString(), fontNormal));
				}
			} else {
				documento.add(new Paragraph("No hay recetas registradas.", fontNormal));
			}

			documento.close();

			JOptionPane.showMessageDialog(null, "Receta PDF generada correctamente!");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
		}
	}
}