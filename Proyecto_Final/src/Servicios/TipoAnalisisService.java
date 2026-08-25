package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.util.ArrayList;

public class TipoAnalisisService {


    /**
     * PROCESO: Inserta un nuevo registro de tipo de análisis clínico invocando un procedimiento almacenado.
     *
     * ENTRADAS:
     * - tipo: Objeto de tipo TipoAnalisis que contiene el nombre y la descripción a registrar.
     *
     * SALIDA: boolean (true si el registro fue insertado exitosamente, false en caso contrario).
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión a la base de datos.
     * 2. Llama a conn.prepareCall() especificando el procedimiento almacenado sp_crear_tipo_analisis.
     * 3. Asigna los parámetros del procedimiento mediante stmt.setString(1, ...) y stmt.setString(2, ...).
     * 4. Ejecuta la sentencia mediante stmt.executeUpdate() y retorna true si afectó al menos una fila.
     */

    public boolean crearTipoAnalisis(TipoAnalisis tipo) {
        String sql = "{call sp_crear_tipo_analisis(?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, tipo.getNombre());
            stmt.setString(2, tipo.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Modifica los datos de un tipo de análisis existente mediante un procedimiento almacenado.
     *
     * ENTRADAS:
     * - tipo: Objeto de tipo TipoAnalisis con el código único, nuevo nombre y nueva descripción.
     *
     * SALIDA: boolean (true si el registro fue actualizado correctamente, false si falló o no se afectaron filas).
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para establecer la conexión a la base de datos.
     * 2. Llama a conn.prepareCall() invocando el procedimiento sp_editar_tipo_analisis.
     * 3. Setea los parámetros del código, nombre y descripción en el CallableStatement.
     * 4. Ejecuta stmt.executeUpdate() para persisiti los cambios en la base de datos.
     */

    public boolean editTipoAnalisis(TipoAnalisis tipo) {
        String sql = "{call sp_editar_tipo_analisis(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, tipo.getCodigoTipo());
            stmt.setString(2, tipo.getNombre());
            stmt.setString(3, tipo.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Elimina un tipo de análisis clínico registrado en el sistema según su código identificador.
     *
     * ENTRADAS:
     * - codigoTipo: Identificador numérico del tipo de análisis a remover.
     *
     * SALIDA: boolean (true si la eliminación fue exitosa, false en caso de error SQL o falta de coincidencia).
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión activa.
     * 2. Llama a conn.prepareCall() referenciando el procedimiento sp_eliminar_tipo_analisis.
     * 3. Setea el parámetro identificador mediante stmt.setInt(1, codigoTipo).
     * 4. Ejecuta stmt.executeUpdate() para efectuar la baja.
     */

    public boolean eliminarTipoAnalisis(int codigoTipo) {
        String sql = "{call sp_eliminar_tipo_analisis(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoTipo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Consulta y construye la entidad de un tipo de análisis clínico filtrando por su código clave.
     *
     * ENTRADAS:
     * - codigoTipo: Código clave numérico de la entidad buscada.
     *
     * SALIDA: Objeto TipoAnalisis populado con la información recuperada, o null si no se encuentra en la base de datos.
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectarse a la base de datos.
     * 2. Llama a conn.prepareStatement() parametrizando la consulta SQL sobre la tabla tipo_analisis.
     * 3. Setea el filtro del código de tipo mediante stmt.setInt(1, codigoTipo).
     * 4. Evalúa rs.next() para instanciar TipoAnalisis y mapear sus columnas (codigo_tipo, nombre, descripcion).
     */

    public TipoAnalisis buscarTipoAnalisis(int codigoTipo) {
        TipoAnalisis tipo = null;
        String sql = "select tipo_analisis.codigo_tipo, tipo_analisis.nombre, tipo_analisis.descripcion " +
                "from tipo_analisis " +
                "where tipo_analisis.codigo_tipo = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoTipo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tipo = new TipoAnalisis();
                tipo.setCodigoTipo(rs.getInt("codigo_tipo"));
                tipo.setNombre(rs.getString("nombre"));
                tipo.setDescripcion(rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipo;
    }

    /**
     * PROCESO: Obtiene la lista completa de los tipos de análisis disponibles en la base de datos.
     *
     * ENTRADAS: Ninguna.
     *
     * SALIDA: ArrayList<TipoAnalisis> conteniendo todas las entidades registradas.
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para abrir la conexión.
     * 2. Llama a conn.prepareStatement() ejecutando un SELECT sobre la tabla tipo_analisis.
     * 3. Recorre los resultados con un bucle while(rs.next()).
     * 4. Construye una instancia de TipoAnalisis por cada fila y la agrega a la lista de retorno.
     */

    public ArrayList<TipoAnalisis> listarTiposAnalisis() {
        ArrayList<TipoAnalisis> lista = new ArrayList<>();
        String sql = "select tipo_analisis.codigo_tipo, tipo_analisis.nombre, tipo_analisis.descripcion " +
                "from tipo_analisis";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TipoAnalisis tipo = new TipoAnalisis();
                tipo.setCodigoTipo(rs.getInt("codigo_tipo"));
                tipo.setNombre(rs.getString("nombre"));
                tipo.setDescripcion(rs.getString("descripcion"));

                lista.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}