package Visual;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.xwpf.usermodel.*;

import logico.Consulta;

public class GeneradorReportes {

	public static void generarReceta(Consulta consulta) {
		try {
			XWPFDocument documento = new XWPFDocument();

			XWPFParagraph titulo = documento.createParagraph();
			titulo.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun runTitulo = titulo.createRun();
			runTitulo.setBold(true);
			runTitulo.setFontSize(20);
			runTitulo.setText("RECETA MÉDICA - CLÍNICA UNPHU");
			runTitulo.addBreak();

			XWPFParagraph info = documento.createParagraph();
			XWPFRun runInfo = info.createRun();
			runInfo.setText("Dr./Dra.: " + consulta.getMedico().getNombre() + " " + consulta.getMedico().getApellido());
			runInfo.addBreak();
			runInfo.setText("Especialidad: " + consulta.getMedico().getEspecialidad().getNombre());
			runInfo.addBreak();
			runInfo.setText("----------------------------------------------------------");
			runInfo.addBreak();
			runInfo.setText("Paciente: " + consulta.getCliente().getNombre() + " " + consulta.getCliente().getApellido());
			runInfo.addBreak();
			runInfo.setText("Cédula: " + consulta.getCliente().getCedula());
			runInfo.addBreak();
			runInfo.setText("Fecha: " + consulta.getFechaConsulta().toString());
			runInfo.addBreak();
			runInfo.addBreak();

			XWPFParagraph diag = documento.createParagraph();
			XWPFRun runDiag = diag.createRun();
			runDiag.setBold(true);
			runDiag.setText("DIAGNÓSTICO:");
			runDiag.addBreak();
			XWPFRun runDiagText = diag.createRun();
			runDiagText.setText(consulta.getDiagnostico());
			runDiagText.addBreak();
			runDiagText.addBreak();

			XWPFParagraph rx = documento.createParagraph();
			XWPFRun runRx = rx.createRun();
			runRx.setBold(true);
			runRx.setText("TRATAMIENTO / INDICACIONES:");
			runRx.addBreak();
			XWPFRun runRxText = rx.createRun();
			runRxText.setText(consulta.getRecetaMedica()); 

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File("Receta_" + consulta.getCliente().getNombre() + ".docx"));

			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				FileOutputStream out = new FileOutputStream(fileChooser.getSelectedFile());
				documento.write(out);
				out.close();
				documento.close();
				JOptionPane.showMessageDialog(null, "Receta generada correctamente.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al generar Word: " + e.getMessage());
		}
	}

}