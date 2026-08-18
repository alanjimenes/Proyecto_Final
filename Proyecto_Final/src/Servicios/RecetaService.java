package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import logico.RecetaMedica;

public class RecetaService {

    public boolean registrarReceta(Connection conn, RecetaMedica receta, int idConsulta) throws SQLException {
        String sql = "insert into receta_medica (codigo_consulta, codigo_medicamento, frecuencia, duracion, dosis, descripcion) " +
                "values (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.setInt(2, receta.getMedicamento().getCodigoMedicamento());
            stmt.setString(3, receta.getFrecuencia());
            stmt.setString(4, receta.getDuracion());
            stmt.setString(5, receta.getDosis());
            stmt.setString(6, receta.getDescripcion());

            return stmt.executeUpdate() > 0;
        }
    }
}