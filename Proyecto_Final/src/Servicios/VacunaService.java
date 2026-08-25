package Servicios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import logico.Cliente;
import logico.Historial;
import logico.Vacuna;
import Utils.ConexionDB;

public class VacunaService {


    /**
     * PROCESO: Agrega una nueva vacuna al catálogo invocando un procedimiento almacenado.
     * <p>
     * ENTRADAS:
     * - vac: Objeto Vacuna con el nombre y la descripción descriptiva a registrar.
     * <p>
     * SALIDA: boolean (true si la adición se concretó, false en caso de falla o error de SQL).
     * <p>
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectarse a la base de datos.
     * 2. Llama a conn.prepareCall() con el procedimiento almacenado sp_crear_vacuna.
     * 3. Carga los valores de nombre y descripción mediante stmt.setString().
     * 4. Confirma la ejecución utilizando stmt.executeUpdate() > 0.
     */

    public boolean agregarVacuna(Vacuna vac) {
        String sql = "{call sp_crear_vacuna(?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, vac.getNombre());
            stmt.setString(2, vac.getDescripcion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Obtiene la lista completa de las vacunas registradas en la base de datos.
     * <p>
     * ENTRADAS: Ninguna.
     * <p>
     * SALIDA: ArrayList<Vacuna> con todas las vacunas del catálogo marcadas como activas.
     * <p>
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para habilitar la comunicación con la base de datos.
     * 2. Llama a conn.prepareStatement() con la sentencia SELECT sobre la tabla vacuna.
     * 3. Mapea en un bucle while las propiedades codigo_vacuna, nombre y descripcion.
     * 4. Asigna vac.setActivo(true) y añade el elemento al ArrayList de salida.
     */

    public ArrayList<Vacuna> listarVacunas() {
        ArrayList<Vacuna> lista = new ArrayList<>();
        String sql = "select vacuna.codigo_vacuna, vacuna.nombre, vacuna.descripcion " +
                "from vacuna";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vacuna vac = new Vacuna();
                vac.setCodigoVacuna(rs.getInt("codigo_vacuna"));
                vac.setNombre(rs.getString("nombre"));
                vac.setDescripcion(rs.getString("descripcion"));
                vac.setActivo(true);
                lista.add(vac);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * PROCESO: Actualiza la información detallada de una vacuna existente.
     * <p>
     * ENTRADAS:
     * - vac: Objeto Vacuna con los atributos actualizados.
     * <p>
     * SALIDA: boolean (true si el registro fue modificado con éxito, false en caso contrario).
     * <p>
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para solicitar la conexión.
     * 2. Llama a conn.prepareCall() invocando el procedimiento sp_editar_vacuna.
     * 3. Establece los parámetros correspondientes al código, nombre y descripción de la vacuna.
     * 4. Realiza la ejecución vía stmt.executeUpdate() notificando el resultado.
     */

    public boolean actualizarVacuna(Vacuna vac) {
        String sql = "{call sp_editar_vacuna(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, vac.getCodigoVacuna());
            stmt.setString(2, vac.getNombre());
            stmt.setString(3, vac.getDescripcion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Da de baja una vacuna del catálogo según su código identificador.
     * <p>
     * ENTRADAS:
     * - codigoVacuna: Identificador entero de la vacuna a eliminar.
     * <p>
     * SALIDA: boolean (true si la vacuna fue eliminada, false en caso de fallo).
     * <p>
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para interactuar con la base de datos.
     * 2. Llama a conn.prepareCall() sobre el procedimiento almacenado sp_eliminar_vacuna.
     * 3. Setea el identificador de la vacuna mediante stmt.setInt(1, codigoVacuna).
     * 4. Procesa la orden llamando a stmt.executeUpdate().
     */

    public boolean eliminarVacuna(int codigoVacuna) {
        String sql = "{call sp_eliminar_vacuna(?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoVacuna);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Procesa el registro de la aplicación de un lote de vacuna a un cliente especificando el personal interviniente y la fecha.
     * <p>
     * ENTRADAS:
     * - cedulaCliente: Documento de identidad del cliente receptor.
     * - codigoLote: Identificador numérico del lote de la vacuna aplicada.
     * - codigoPersonalLogueado: Código del usuario/personal de salud que aplica la dosis.
     * - fecha: Timestamp con la fecha y hora exactas del evento.
     * <p>
     * SALIDA: boolean (true si la aplicación de la vacuna fue registrada correctamente, false si falló).
     * <p>
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión a la base de datos.
     * 2. Llama a conn.prepareCall() con la firma del procedimiento almacenado sp_aplicar_vacuna.
     * 3. Carga los 5 parámetros exigidos (cédula, lote, personal, timestamp y flag booleano).
     * 4. Ejecuta el procedimiento utilizando stmt.executeUpdate() para impactar la transacción.
     */

    public boolean aplicarVacunaCliente(String cedulaCliente, int codigoLote, int codigoPersonalLogueado, Timestamp fecha) {
        String sql = "{call sp_aplicar_vacuna(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedulaCliente);
            stmt.setInt(2, codigoLote);
            stmt.setInt(3, codigoPersonalLogueado);
            stmt.setTimestamp(4, fecha);
            stmt.setBoolean(5, true);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}