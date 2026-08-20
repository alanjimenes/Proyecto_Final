package logico;

import java.io.Serializable;

public class PaqueteDeDatos implements Serializable {
    private static final long serialVersionUID = 1L;
    private String comando;
    private Object objeto;
    private Object respuesta;

    public PaqueteDeDatos() {
    }

    public PaqueteDeDatos(String comando, Object objeto) {
        this.comando = comando;
        this.objeto = objeto;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public Object getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Object respuesta) {
        this.respuesta = respuesta;
    }
}