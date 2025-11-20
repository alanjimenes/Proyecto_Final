package Visual;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import logico.Clinica;
import logico.Control;
import logico.User;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				FileInputStream usuarios;
				FileOutputStream usuarios2;
				ObjectInputStream usuariosRead;
				ObjectOutputStream usuariosWrite;

				try {
					usuarios = new FileInputStream("Usuarios.dat");
					usuariosRead = new ObjectInputStream(usuarios);
					Control temp = (Control) usuariosRead.readObject();
					Control.setControl(temp);
					usuarios.close();
					usuariosRead.close();

				} catch (FileNotFoundException e) {
					try {
						usuarios2 = new FileOutputStream("empresa.dat");
						usuariosWrite = new ObjectOutputStream(usuarios2);
						User aux = new User("Administrador", "Admin", "Admin");
						Control.getInstance().regUser(aux);
						usuariosWrite.writeObject(Control.getInstance());
						usuarios2.close();
						usuariosWrite.close();

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				Clinica.cargarDatos();

				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
