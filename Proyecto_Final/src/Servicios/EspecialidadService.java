package Servicios;

import Utils.ConexionDB;
import logico.Especialidad;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EspecialidadService {


    /**
     * PROCESO: Registra una nueva especialidad médica invocando el procedimiento almacenado 'sp_crear_especialidad'.
     * * ENTRADAS:
     * - esp: Objeto Especialidad conteniendo el nombre descriptivo de la misma.
     * * SALIDA: boolean (true si la creación se efectúa con éxito, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta a la base de datos vía ConexionDB.getConexion().
     * 2. Invocación de la instrucción "{call sp_crear_especialidad(?)}".
     * 3. Setea el nombre mediante stmt.setString(1, esp.getNombre()).
     * 4. Ejecuta la llamada evaluando las filas afectadas.
     */

    public boolean registrarEspecialidad(Especialidad esp) {
        String sql = "{call sp_crear_especialidad(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, esp.getNombre());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Retorna el catálogo completo de especialidades médicas registradas en el sistema.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos Especialidad.
     * * FLUJO DE LLAMADAS:
     * 1. Obtiene la conexión con ConexionDB.getConexion().
     * 2. Prepara e ejecuta la sentencia SQL SELECT sobre la tabla 'especialidad'.
     * 3. Transforma cada tupla del ResultSet a una instancia de Especialidad.
     * 4. Retorna la lista construida.
     */

    public ArrayList<Especialidad> listarEspecialidades() {
        ArrayList<Especialidad> lista = new ArrayList<>();
        String sql = "select especialidad.codigo_especialidad, especialidad.nombre " +
                "from especialidad";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                esp.setNombre(rs.getString("nombre"));
                lista.add(esp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }



    /**
     * PROCESO: Busca los datos de una especialidad médica mediante su nombre exacto.
     * * ENTRADAS:
     * - nombre: Cadena de texto con el nombre de la especialidad.
     * * SALIDA: Objeto Especialidad correspondiente o null si no se encuentra.
     * * FLUJO DE LLAMADAS:
     * 1. Abre la conexión invocando a ConexionDB.getConexion().
     * 2. Prepara la sentencia SQL buscando coincidencia exacta en 'especialidad.nombre'.
     * 3. Asigna la condición con stmt.setString(1, nombre).
     * 4. Retorna la entidad Especialidad mapeada a partir del ResultSet.
     */

    public Especialidad buscarEspecialidadPorNombre(String nombre) {
        Especialidad especialidad = null;
        String sql = "select especialidad.codigo_especialidad, especialidad.nombre " +
                "from especialidad " +
                "where especialidad.nombre = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                especialidad = new Especialidad();
                especialidad.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                especialidad.setNombre(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return especialidad;
    }


    /**
     * PROCESO: Edita los atributos de una especialidad médica existente mediante el procedimiento almacenado 'sp_editar_especialidad'.
     * * ENTRADAS:
     * - esp: Objeto Especialidad con el código único y la información actualizada.
     * * SALIDA: boolean (true si la modificación fue exitosa, false en caso de error).
     * * FLUJO DE LLAMADAS:
     * 1. Crea la conexión con ConexionDB.getConexion().
     * 2. Invocación de la consulta "{call sp_editar_especialidad(?, ?)}".
     * 3. Pasa el identificador y el nuevo nombre como parámetros.
     * 4. Valida la actualización con stmt.executeUpdate().
     */

    public boolean actualizarEspecialidad(Especialidad esp) {
        String sql = "{call sp_editar_especialidad(?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, esp.getCodigoEspecialidad());
            stmt.setString(2, esp.getNombre());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Elimina una especialidad de la base de datos utilizando el procedimiento almacenado 'sp_eliminar_especialidad'.
     * * ENTRADAS:
     * - codigo: Representación en texto (String) del identificador numérico de la especialidad.
     * * SALIDA: boolean (true si la eliminación fue satisfactoria, false en caso de fallo).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con ConexionDB.getConexion().
     * 2. Invocación de la sentencia "{call sp_eliminar_especialidad(?)}".
     * 3. Convierte 'codigo' a entero mediante Integer.parseInt() y lo setea en la consulta.
     * 4. Ejecuta la eliminación comprobando si modificó filas.
     */

    public boolean eliminarEspecialidad(String codigo) {
        String sql = "{call sp_eliminar_especialidad(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, Integer.parseInt(codigo));
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}