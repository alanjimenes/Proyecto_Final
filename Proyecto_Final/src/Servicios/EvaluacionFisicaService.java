package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;

public class EvaluacionFisicaService {

    public boolean registrarEvaluacion(EvaluacionFisica evaluacion) {
        try (Connection conn = ConexionDB.getConexion()) {
            int idConsulta = (evaluacion.getConsulta() != null) ? evaluacion.getConsulta().getCodigoConsulta() : 0;
            return registrarEvaluacion(conn, evaluacion, idConsulta);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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