package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import logico.Enfermedad;

public class EnfermedadConsultaService {


    /**
     * PROCESO: Inserta un registro en la tabla intermedia 'enfermedad_consulta' para asociar una enfermedad diagnosticada a una consulta médica específica utilizando una conexión JDBC externa.
     * * ENTRADAS:
     * - conn: Conexión SQL activa compartida por el flujo transaccional.
     * - enfermedad: Objeto Enfermedad que contiene la clave primaria del diagnóstico.
     * - idConsulta: Identificador numérico de la consulta médica.
     * * SALIDA: boolean (true si el registro fue insertado exitosamente, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Prepara la instrucción SQL INSERT sobre la tabla 'enfermedad_consulta'.
     * 2. Asigna el código de la consulta con stmt.setInt(1, idConsulta).
     * 3. Asigna el código de la enfermedad con stmt.setInt(2, enfermedad.getCodigoEnfermedad()).
     * 4. Ejecuta stmt.executeUpdate() devolviendo true si el número de filas modificadas es mayor a cero.
     */

    public boolean registrarDiagnostico(Connection conn, Enfermedad enfermedad, int idConsulta) throws SQLException {
        String sql = "insert into enfermedad_consulta (codigo_cons, codigo_enfermedad) " +
                "values (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.setInt(2, enfermedad.getCodigoEnfermedad());

            return stmt.executeUpdate() > 0;
        }
    }
}