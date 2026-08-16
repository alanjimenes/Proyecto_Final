package logico;

import java.io.Serializable;

public class EvaluacionFisica implements Serializable {
    private int codigoEvaluacion;
    private Consulta consulta;
    private float temperatura;
    private int frecuenciaCardiaca;
    private String persionArterial;
    private float peso;
    private float talla;

    public EvaluacionFisica(int codigoEvaluacion, Consulta consulta, float temperatura, int frecuenciaCardiaca, String persionArterial, float peso, float talla) {
        this.codigoEvaluacion = codigoEvaluacion;
        this.consulta = consulta;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.persionArterial = persionArterial;
        this.peso = peso;
        this.talla = talla;
    }

    public int getCodigoEvaluacion() { return codigoEvaluacion; }
    public void setCodigoEvaluacion(int codigoEvaluacion) { this.codigoEvaluacion = codigoEvaluacion; }
    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }
    public float getTemperatura() { return temperatura; }
    public void setTemperatura(float temperatura) { this.temperatura = temperatura; }
    public int getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(int frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }
    public String getPersionArterial() { return persionArterial; }
    public void setPersionArterial(String persionArterial) { this.persionArterial = persionArterial; }
    public float getPeso() { return peso; }
    public void setPeso(float peso) { this.peso = peso; }
    public float getTalla() { return talla; }
    public void setTalla(float talla) { this.talla = talla; }
}