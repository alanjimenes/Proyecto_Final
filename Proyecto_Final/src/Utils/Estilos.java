package Utils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Estilos {

    /**
     * PROCESO: Aplica estilos modernos y comportamiento visual hover a botones Swing.
     * * ENTRADAS:
     * - boton: Componente JButton al que se le aplicará el estilo.
     * - colorFondo: Instancia de Color para el fondo predeterminado del botón.
     * - colorTexto: Instancia de Color para la fuente tipográfica.
     * * SALIDA: Ninguna (método void).
     * * FLUJO DE LLAMADAS:
     * 1. Configura la fuente a 'Bahnschrift', quita los bordes/enfoque por defecto y asigna HAND_CURSOR.
     * 2. Agrega un MouseListener para oscurecer el fondo al pasar el cursor sobre el botón (mouseEntered) y restaurarlo al salir (mouseExited).
     */
    public static void estilarBoton(JButton boton, Color colorFondo, Color colorTexto) {
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFont(new Font("Bahnschrift", Font.BOLD, 15)); 
        boton.setFocusPainted(false); 
        boton.setBorderPainted(false); 
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 

        if(boton.getMouseListeners().length > 0) {
        }

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorFondo.darker()); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorFondo); 
            }
        });
    }

    /**
     * PROCESO: Ajusta la presentación gráfica y padding de los campos de texto de la interfaz.
     * * ENTRADAS:
     * - campo: Componente JTextField a personalizar.
     * * SALIDA: Ninguna (método void).
     * * FLUJO DE LLAMADAS:
     * 1. Asigna un borde compuesto (CompoundBorder) combinando una línea suave exterior y un margen interno (EmptyBorder).
     * 2. Define la tipografía 'Bahnschrift' regular con un alto estándar de 30px.
     */

    public static void estilarCampo(JTextField campo) {

        campo.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(0, 10, 0, 10) 
        ));

        campo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        campo.setForeground(Color.DARK_GRAY);
        campo.setBounds(campo.getX(), campo.getY(), campo.getWidth(), 30);
    }
}