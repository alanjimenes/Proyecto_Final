package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.util.ArrayList;

public class MedicamentoService {


    /**
     * PROCESO: Crea un nuevo registro de medicamento invocando el procedimiento almacenado 'sp_crear_medicamento'.
     * * ENTRADAS:
     * - medicamento: Objeto Medicamento conteniendo el nombre, concentración y descripción.
     * * SALIDA: boolean (true si el medicamento fue registrado con éxito, false en caso de fallo).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos llamando a ConexionDB.getConexion().
     * 2. Prepara la sentencia "{call sp_crear_medicamento(?, ?, ?)}".
     * 3. Setea los valores de nombre, concentración y descripción.
     * 4. Ejecuta stmt.executeUpdate() evaluando las filas afectadas.
     */

    public boolean crearMedicamento(Medicamento medicamento) {
        String sql = "{call sp_crear_medicamento(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, medicamento.getNombre());
            stmt.setString(2, medicamento.getConcentracion());
            stmt.setString(3, medicamento.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Modifica la información de un medicamento existente mediante el procedimiento almacenado 'sp_editar_medicamento'.
     * * ENTRADAS:
     * - medicamento: Objeto Medicamento con el código identificador y la información actualizada.
     * * SALIDA: boolean (true si la edición se completó con éxito, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Obtiene la conexión vía ConexionDB.getConexion().
     * 2. Prepara la llamada al Stored Procedure "{call sp_editar_medicamento(?, ?, ?, ?)}".
     * 3. Asigna el código, nombre, concentración y descripción.
     * 4. Ejecuta stmt.executeUpdate() para guardar los cambios.
     */

    public boolean editMedicamento(Medicamento medicamento) {
        String sql = "{call sp_editar_medicamento(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, medicamento.getCodigoMedicamento());
            stmt.setString(2, medicamento.getNombre());
            stmt.setString(3, medicamento.getConcentracion());
            stmt.setString(4, medicamento.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Elimina un medicamento del catálogo llamando al procedimiento almacenado 'sp_eliminar_medicamento'.
     * * ENTRADAS:
     * - codigoMedicamento: Identificador numérico único del medicamento a eliminar.
     * * SALIDA: boolean (true si la eliminación fue exitosa, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos llamando a ConexionDB.getConexion().
     * 2. Prepara la sentencia "{call sp_eliminar_medicamento(?)}".
     * 3. Asigna el identificador con stmt.setInt(1, codigoMedicamento).
     * 4. Ejecuta stmt.executeUpdate() para efectuar el borrado.
     */

    public boolean eliminarMedicamento(int codigoMedicamento) {
        String sql = "{call sp_eliminar_medicamento(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoMedicamento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Obtiene el catálogo completo de medicamentos registrados.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos Medicamento.
     * * FLUJO DE LLAMADAS:
     * 1. Abre la conexión con ConexionDB.getConexion().
     * 2. Prepara y ejecuta la sentencia SELECT sobre la tabla 'medicamento'.
     * 3. Itera sobre el ResultSet creando y añadiendo cada Medicamento al listado.
     * 4. Retorna la lista resultante.
     */

    public ArrayList<Medicamento> listarMedicamentos() {
        ArrayList<Medicamento> lista = new ArrayList<>();
        String sql = "select medicamento.codigo_medicamento, medicamento.nombre, medicamento.concentracion, " +
                "medicamento.descripcion " +
                "from medicamento";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medicamento med = new Medicamento();
                med.setCodigoMedicamento(rs.getInt("codigo_medicamento"));
                med.setNombre(rs.getString("nombre"));
                med.setConcentracion(rs.getString("concentracion"));
                med.setDescripcion(rs.getString("descripcion"));

                lista.add(med);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}