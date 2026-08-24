package Utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import logico.Consulta;
import logico.RecetaMedica;
import Servicios.RecetaMedicaService; // Asegúrate de tener este import disponible

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GeneradorReportes {

    public static void generarReceta(Consulta consulta) {
        try {
            if (consulta == null) {
                JOptionPane.showMessageDialog(null, "No hay datos de consulta para generar la receta.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscador de recetas adicionales desde la BD usando el servicio
            RecetaMedicaService recetaService = new RecetaMedicaService();
            ArrayList<RecetaMedica> recetasBD = recetaService.getRecetasPorConsulta(consulta.getCodigoConsulta());

            // Si la consulta traía recetas en memoria pero la BD tiene más o son más actualizadas,
            // podemos asignarlas o combinarlas. Aquí usamos las obtenidas de la BD si están disponibles.
            if (recetasBD != null && !recetasBD.isEmpty()) {
                consulta.setRecetas(recetasBD);
            }

            Document documento = new Document();
            String nombreArchivo = "RecetaMedica_Consulta_" + consulta.getCodigoConsulta() + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Font fontNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);

            // Título Principal
            Paragraph titulo = new Paragraph("RECETA MÉDICA - CLÍNICA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new Paragraph(" "));

            // Datos del Médico
            String nombreMedico = "N/A";
            String espMedico = "N/A";
            if (consulta.getMedico() != null) {
                nombreMedico = consulta.getMedico().getNombre() + " " + consulta.getMedico().getApellido();
                if (consulta.getMedico().getEspecialidad() != null) {
                    espMedico = consulta.getMedico().getEspecialidad().getNombre();
                }
            }

            documento.add(new Paragraph("Dr./Dra.: " + nombreMedico, fontNormal));
            documento.add(new Paragraph("Especialidad: " + espMedico, fontNormal));
            documento.add(new Paragraph("--------------------------------------------------------------------------------"));

            // Datos del Cliente / Paciente
            String nombreCliente = "N/A";
            String cedulaCliente = "N/A";
            if (consulta.getCliente() != null) {
                nombreCliente = consulta.getCliente().getNombre() + " " + consulta.getCliente().getApellido();
                cedulaCliente = consulta.getCliente().getCedula();
            }

            String fechaConsulta = (consulta.getFechaConsulta() != null) ? consulta.getFechaConsulta().toString() : "N/A";

            documento.add(new Paragraph("Paciente: " + nombreCliente, fontNormal));
            documento.add(new Paragraph("Cédula: " + cedulaCliente, fontNormal));
            documento.add(new Paragraph("Fecha de Consulta: " + fechaConsulta, fontNormal));
            documento.add(new Paragraph(" "));

            // Diagnóstico
            documento.add(new Paragraph("DIAGNÓSTICO:", fontNegrita));
            documento.add(new Paragraph(consulta.getDiagnostico() != null ? consulta.getDiagnostico() : "Sin diagnóstico registrado.", fontNormal));
            documento.add(new Paragraph(" "));

            // Recetas / Tratamiento médico asociado (ya sincronizadas desde la BD)
            documento.add(new Paragraph("TRATAMIENTO / INDICACIONES:", fontNegrita));
            if (consulta.getRecetas() != null && !consulta.getRecetas().isEmpty()) {
                for (RecetaMedica receta : consulta.getRecetas()) {
                    documento.add(new Paragraph("- " + receta.toString(), fontNormal));
                }
            } else {
                documento.add(new Paragraph("No hay recetas registradas para esta consulta.", fontNormal));
            }

            documento.close();

            JOptionPane.showMessageDialog(null, "¡Receta PDF generada correctamente!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}