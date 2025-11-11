package logico;

public class Vivienda {

	private String sector;
	private String calle;
	private String numero;
	private String tipoVivienda; 

	public Vivienda(String sector, String calle, String numero) {
		this.sector = sector;
		this.calle = calle;
		this.numero = numero;
	}

	public String getSector() {
		return sector;
	}

	public String getCalle() {
		return calle;
	}

	public String getNumero() {
		return numero;
	}
}