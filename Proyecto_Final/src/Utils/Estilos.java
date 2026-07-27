package Utils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Estilos {
    

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