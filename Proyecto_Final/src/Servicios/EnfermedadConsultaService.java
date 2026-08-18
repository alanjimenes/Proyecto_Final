package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import logico.Enfermedad;

public class EnfermedadConsultaService {

    public boolean registrarDiagnostico(Connection conn, Enfermedad enfermedad, int idConsulta) throws SQLException {
        String sql = "insert into enfermedad_consult (codigo_consulta, codigo_enfermedad) " +
                "values (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.setInt(2, enfermedad.getCodigoEnfermedad());

            return stmt.executeUpdate() > 0;
        }
    }
}