package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Reportes extends JDialog {

    private final JPanel contentPanel = new JPanel();

    public static void main(String[] args) {
        try {
            Reportes dialog = new Reportes();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Reportes() {
        setTitle("Reportes del Sistema");
        setBounds(100, 100, 450, 220);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        // BOTÓN: REPORTE DE ENFERMEDADES
        JButton btnEnfermedades = new JButton("Reporte de Enfermedades");
        btnEnfermedades.addActionListener(e -> {
            ReporteEnfermedades rep = new ReporteEnfermedades();
            rep.setVisible(true);
        });
        contentPanel.add(btnEnfermedades);

        // BOTÓN: REPORTE DE VACUNAS
        JButton btnVacunas = new JButton("Reporte de Vacunas");
        btnVacunas.addActionListener(e -> {
            ReporteVacunas rep = new ReporteVacunas();
            rep.setVisible(true);
        });
        contentPanel.add(btnVacunas);

        // BOTONES OK / CANCEL
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("Cerrar");
        okButton.addActionListener(e -> dispose());
        buttonPane.add(okButton);
    }
}
