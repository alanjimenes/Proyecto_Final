package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


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
        String sql = "insert into evaluacionfisica (codigo_cons, temperatura, frecuenciacardiaca, presionarterial, peso, talla) " +
                "values (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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