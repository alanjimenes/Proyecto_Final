package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.util.ArrayList;

public class RecetaMedicaService {

    /**
     * PROCESO: Registra una nueva receta médica asociada a una consulta prescribiendo un medicamento mediante el procedimiento almacenado 'sp_crear_receta_medica'.
     * * ENTRADAS:
     * - receta: Objeto RecetaMedica con la consulta, medicamento, frecuencia, duración, dosis y descripción.
     * * SALIDA: boolean (true si la receta fue registrada con éxito, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Solicita la conexión mediante ConexionDB.getConexion().
     * 2. Prepara la llamada al Stored Procedure "{call sp_crear_receta_medica(?, ?, ?, ?, ?, ?)}".
     * 3. Extrae e inyecta los IDs de consulta y medicamento, así como frecuencia, duración, dosis y descripción.
     * 4. Ejecuta stmt.executeUpdate() para guardar el registro.
     */

    public boolean crearRecetaMedica(RecetaMedica receta) {
        String sql = "{call sp_crear_receta_medica(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, receta.getConsulta() != null ? receta.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(2, receta.getMedicamento() != null ? receta.getMedicamento().getCodigoMedicamento() : 0);
            stmt.setString(3, receta.getFrecuencia());
            stmt.setString(4, receta.getDuracion());
            stmt.setString(5, receta.getDosis());
            stmt.setString(6, receta.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarReceta(Connection conn, RecetaMedica receta, int idConsulta) throws SQLException {
        String sql = "{call sp_crear_receta_medica(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.setInt(2, receta.getMedicamento() != null ? receta.getMedicamento().getCodigoMedicamento() : 0);
            stmt.setString(3, receta.getFrecuencia());
            stmt.setString(4, receta.getDuracion());
            stmt.setString(5, receta.getDosis());
            stmt.setString(6, receta.getDescripcion());

            return stmt.executeUpdate() > 0;
        }
    }



    /**
     * PROCESO: Obtiene el listado completo de todas las recetas médicas expedidas en el sistema.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos RecetaMedica.
     * * FLUJO DE LLAMADAS:
     * 1. Abre la conexión con ConexionDB.getConexion().
     * 2. Prepara la consulta SQL SELECT uniendo 'receta_medica' y 'medicamento'.
     * 3. Recorre el ResultSet reconstruyendo cada objeto RecetaMedica y sus referencias.
     * 4. Retorna el listado general.
     */

    public ArrayList<RecetaMedica> getRecetasPorConsulta(int codigoConsulta) {
        ArrayList<RecetaMedica> lista = new ArrayList<>();
        String sql = "select receta_medica.codigo_rec, receta_medica.codigo_cons, receta_medica.frecuencia, " +
                "receta_medica.duracion, receta_medica.dosis, receta_medica.descripcion, medicamento.codigo_medicamento, " +
                "medicamento.nombre, medicamento.concentracion " +
                "from receta_medica " +
                "left join medicamento on receta_medica.codigo_medicamento = medicamento.codigo_medicamento " +
                "where receta_medica.codigo_cons = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoConsulta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RecetaMedica receta = new RecetaMedica();
                    receta.setCodigoRec(rs.getInt("codigo_rec"));
                    receta.setFrecuencia(rs.getString("frecuencia"));
                    receta.setDuracion(rs.getString("duracion"));
                    receta.setDosis(rs.getString("dosis"));
                    receta.setDescripcion(rs.getString("descripcion"));

                    Consulta c = new Consulta();
                    c.setCodigoConsulta(rs.getInt("codigo_cons"));
                    receta.setConsulta(c);

                    Medicamento m = new Medicamento();
                    m.setCodigoMedicamento(rs.getInt("codigo_medicamento"));
                    m.setNombre(rs.getString("nombre"));
                    m.setConcentracion(rs.getString("concentracion"));
                    receta.setMedicamento(m);

                    lista.add(receta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}