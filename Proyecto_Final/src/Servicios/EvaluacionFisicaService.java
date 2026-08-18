package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import logico.EvaluacionFisica;

public class EvaluacionFisicaService {

    public boolean registrarEvaluacion(Connection conn, EvaluacionFisica evaluacion, int idConsulta) throws SQLException {
        String sql = "insert into evaluacionfisica (codigo_consulta, temperatura, frecuenciacardiaca, persionarterial, peso, talla) " +
                "values (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.setFloat(2, evaluacion.getTemperatura());
            stmt.setInt(3, evaluacion.getFrecuenciaCardiaca());
            stmt.setString(4, evaluacion.getPersionArterial());
            stmt.setFloat(5, evaluacion.getPeso());
            stmt.setFloat(6, evaluacion.getTalla());

            return stmt.executeUpdate() > 0;
        }
    }
}