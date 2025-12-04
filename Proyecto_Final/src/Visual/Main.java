package Visual;

import java.awt.EventQueue;
import Servidor.Server;

public class Main {

	public static void main(String[] args) {

		Thread hiloServer = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("--- Iniciando Servidor Interno ---");
					Server.main(null); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		hiloServer.start(); 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000); 

					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}