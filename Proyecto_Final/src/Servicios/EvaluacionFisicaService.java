package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;

public class EvaluacionFisicaService {

    /**
     * PROCESO: Abre una conexión independiente para registrar los signos vitales y la evaluación física de un paciente.
     * * ENTRADAS:
     * - evaluacion: Objeto EvaluacionFisica con la información métrica (temperatura, frecuencia cardíaca, presión arterial, peso, talla) y su consulta médica vinculada.
     * * SALIDA: boolean (true si se registra correctamente, false si hay un fallo de SQL).
     * * FLUJO DE LLAMADAS:
     * 1. Abre una conexión propia mediante ConexionDB.getConexion().
     * 2. Extrae el ID de la consulta asociada del objeto EvaluacionFisica.
     * 3. Delega el trabajo a la sobrecarga registrarEvaluacion(Connection, EvaluacionFisica, int).
     */

    public boolean registrarEvaluacion(EvaluacionFisica evaluacion) {
        try (Connection conn = ConexionDB.getConexion()) {
            int idConsulta = (evaluacion.getConsulta() != null) ? evaluacion.getConsulta().getCodigoConsulta() : 0;
            return registrarEvaluacion(conn, evaluacion, idConsulta);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Ejecuta el procedimiento almacenado 'sp_crear_evaluacion_fisica' reutilizando un contexto transaccional o conexión preexistente.
     * * ENTRADAS:
     * - conn: Conexión SQL activa compartida.
     * - evaluacion: Objeto EvaluacionFisica con los datos de las constantes vitales.
     * - idConsulta: Identificador único de la consulta médica asociada.
     * * SALIDA: boolean (true si la inserción se ejecutó con éxito, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Prepara la instrucción "{call sp_crear_evaluacion_fisica(?, ?, ?, ?, ?, ?)}".
     * 2. Inyecta en el CallableStatement el ID de la consulta junto con temperatura, frecuencia cardíaca, presión arterial, peso y talla.
     * 3. Ejecuta la actualización devolviendo true si afectó al menos una fila.
     */

    public boolean registrarEvaluacion(Connection conn, EvaluacionFisica evaluacion, int idConsulta) throws SQLException {
        String sql = "{call sp_crear_evaluacion_fisica(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.setFloat(2, evaluacion.getTemperatura());
            stmt.setInt(3, evaluacion.getFrecuenciaCardiaca());
            stmt.setString(4, evaluacion.getPresionArterial());
            stmt.setFloat(5, evaluacion.getPeso());
            stmt.setFloat(6, evaluacion.getTalla());

            return stmt.executeUpdate() > 0;
        }
    }

}