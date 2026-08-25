package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LoteVacunaService {

    /**
     * PROCESO: Registra un nuevo lote de vacunas en el sistema mediante la ejecución del procedimiento almacenado 'sp_crear_lote_vacuna'.
     * * ENTRADAS:
     * - lote: Objeto LoteVacuna que incluye la vacuna asociada, número de lote, fecha de vencimiento y cantidad inicial.
     * * SALIDA: boolean (true si el registro del lote fue exitoso, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos llamando a ConexionDB.getConexion().
     * 2. Prepara la sentencia "{call sp_crear_lote_vacuna(?, ?, ?, ?)}".
     * 3. Setea el código de la vacuna, número de lote, fecha de vencimiento y cantidad.
     * 4. Ejecuta stmt.executeUpdate() para guardar el registro.
     */

    public boolean registrarLote(LoteVacuna lote) {
        String sql = "{call sp_crear_lote_vacuna(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, lote.getVacuna() != null ? lote.getVacuna().getCodigoVacuna() : 0);
            stmt.setString(2, lote.getNoLote());
            stmt.setDate(3, Date.valueOf(lote.getFechaVencimiento()));
            stmt.setInt(4, lote.getCantidad());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Actualiza los datos de un lote de vacunas existente llamando al procedimiento almacenado 'sp_editar_lote_vacuna'.
     * * ENTRADAS:
     * - lote: Objeto LoteVacuna con el código único del lote y los datos actualizados.
     * * SALIDA: boolean (true si los cambios se aplicaron correctamente, false en caso de fallo).
     * * FLUJO DE LLAMADAS:
     * 1. Solicita la conexión mediante ConexionDB.getConexion().
     * 2. Prepara la llamada al Stored Procedure "{call sp_editar_lote_vacuna(?, ?, ?, ?, ?)}".
     * 3. Asigna el código del lote, código de vacuna, número de lote, fecha de vencimiento y cantidad.
     * 4. Ejecuta stmt.executeUpdate() para persistir las modificaciones.
     */

    public boolean editLoteVacuna(LoteVacuna lote) {
        String sql = "{call sp_editar_lote_vacuna(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, lote.getCodigoLote());
            stmt.setInt(2, lote.getVacuna() != null ? lote.getVacuna().getCodigoVacuna() : 0);
            stmt.setString(3, lote.getNoLote());
            stmt.setDate(4, Date.valueOf(lote.getFechaVencimiento()));
            stmt.setInt(5, lote.getCantidad());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Elimina un lote de vacunas del inventario invocando el procedimiento almacenado 'sp_eliminar_lote_vacuna'.
     * * ENTRADAS:
     * - codigoLote: Identificador numérico único del lote a eliminar.
     * * SALIDA: boolean (true si la eliminación fue exitosa, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Abre la conexión llamando a ConexionDB.getConexion().
     * 2. Invocación del procedimiento "{call sp_eliminar_lote_vacuna(?)}".
     * 3. Asigna el identificador mediante stmt.setInt(1, codigoLote).
     * 4. Ejecuta la sentencia con stmt.executeUpdate().
     */

    public boolean eliminarLoteVacuna(int codigoLote) {
        String sql = "{call sp_eliminar_lote_vacuna(?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoLote);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * PROCESO: Recupera la lista general de todos los lotes de vacunas registrados.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos LoteVacuna.
     * * FLUJO DE LLAMADAS:
     * 1. Solicita la conexión vía ConexionDB.getConexion().
     * 2. Ejecuta un SQL SELECT combinando 'lote_vacuna' con 'vacuna' mediante LEFT JOIN.
     * 3. Itera sobre el ResultSet creando y configurando las instancias de LoteVacuna y Vacuna.
     * 4. Retorna el listado completo.
     */

    public ArrayList<LoteVacuna> listarLotes() {
        ArrayList<LoteVacuna> lista = new ArrayList<>();
        String sql = "select lote_vacuna.codigo_lote, lote_vacuna.no_lote, lote_vacuna.fechavencimiento, lote_vacuna.cantidad, vacuna.codigo_vacuna, vacuna.nombre " +
                "from lote_vacuna " +
                "left join vacuna on lote_vacuna.codigo_vacuna = vacuna.codigo_vacuna";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fechavencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechavencimiento").toLocalDate());
                }
                lote.setCantidad(rs.getInt("cantidad"));

                Vacuna vac = new Vacuna();
                vac.setCodigoVacuna(rs.getInt("codigo_vacuna"));
                vac.setNombre(rs.getString("nombre"));
                lote.setVacuna(vac);

                lista.add(lote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * PROCESO: Consulta y filtra los lotes de vacunas asociados a un tipo de vacuna en específico.
     * * ENTRADAS:
     * - codigoVacuna: Identificador numérico de la vacuna.
     * * SALIDA: ArrayList de objetos LoteVacuna pertenecientes a la vacuna filtrada.
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con ConexionDB.getConexion().
     * 2. Prepara el SELECT filtrando por 'lote_vacuna.codigo_vacuna = ?'.
     * 3. Asigna el parámetro mediante stmt.setInt(1, codigoVacuna).
     * 4. Mapea y agrega cada elemento del ResultSet al listado devuelto.
     */

    public ArrayList<LoteVacuna> listarLotesPorVacuna(int codigoVacuna) {
        ArrayList<LoteVacuna> lista = new ArrayList<>();
        String sql = "select lote_vacuna.codigo_lote, lote_vacuna.no_lote, lote_vacuna.fechavencimiento, lote_vacuna.cantidad, vacuna.codigo_vacuna, vacuna.nombre " +
                "from lote_vacuna " +
                "left join vacuna on lote_vacuna.codigo_vacuna = vacuna.codigo_vacuna " +
                "where lote_vacuna.codigo_vacuna = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoVacuna);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fechavencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechavencimiento").toLocalDate());
                }
                lote.setCantidad(rs.getInt("cantidad"));

                Vacuna vac = new Vacuna();
                vac.setCodigoVacuna(rs.getInt("codigo_vacuna"));
                vac.setNombre(rs.getString("nombre"));
                lote.setVacuna(vac);

                lista.add(lote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * PROCESO: Consulta la vista de inventario de vacunas disponibles ('vw_inventario_vacunas_disponibles') para obtener los lotes habilitados.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos LoteVacuna con stock o estado disponible.
     * * FLUJO DE LLAMADAS:
     * 1. Establece la conexión vía ConexionDB.getConexion().
     * 2. Prepara y ejecuta la consulta SELECT sobre la vista 'vw_inventario_vacunas_disponibles'.
     * 3. Mapea los resultados construyendo las entidades LoteVacuna y Vacuna.
     * 4. Retorna la lista resultante.
     */

    public ArrayList<LoteVacuna> listarLotesDisponibles() {
        ArrayList<LoteVacuna> lista = new ArrayList<>();
        String sql = "select vw_inventario_vacunas_disponibles.* from vw_inventario_vacunas_disponibles";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));

                if (rs.getDate("fechavencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechavencimiento").toLocalDate());
                }

                lote.setCantidad(rs.getInt("cantidad"));

                Vacuna vac = new Vacuna();
                vac.setCodigoVacuna(rs.getInt("codigo_vacuna"));
                vac.setNombre(rs.getString("nombre_vacuna"));
                lote.setVacuna(vac);

                lista.add(lote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}