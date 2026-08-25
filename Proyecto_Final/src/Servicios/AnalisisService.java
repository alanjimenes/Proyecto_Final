package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.util.ArrayList;

public class AnalisisService {


    /**
     * PROCESO: Registra un nuevo análisis clínico en la base de datos ejecutando un procedimiento almacenado.
     * * ENTRADAS:
     * - analisis: Objeto de tipo Analisis que contiene los datos del laboratorio, consulta asociada, tipo, fechas, estado y resultado.
     * * SALIDA: Un valor booleano (true si el registro fue insertado exitosamente, false en caso de error SQL).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para establecer el enlace JDBC con la base de datos.
     * 2. Llama a conn.prepareCall() para preparar el procedimiento almacenado "{call sp_crear_analisis(?, ?, ?, ?, ?, ?)}".
     * 3. Llama a los métodos getConsulta(), getTipo(), getFechaOrden(), getFechaResultado(), getEstado() y getResultado() del objeto analisis para mapear los parámetros de entrada.
     * 4. Llama a stmt.executeUpdate() para ejecutar la inserción en SQL Server.
     */

    public boolean crearAnalisis(Analisis analisis) {
        String sql = "{call sp_crear_analisis(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, analisis.getConsulta() != null ? analisis.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(2, analisis.getTipo() != null ? analisis.getTipo().getCodigoTipo() : 0);
            stmt.setTimestamp(3, analisis.getFechaOrden() != null ? Timestamp.valueOf(analisis.getFechaOrden()) : null);
            stmt.setTimestamp(4, analisis.getFechaResultado() != null ? Timestamp.valueOf(analisis.getFechaResultado()) : null);
            stmt.setString(5, analisis.getEstado());
            stmt.setString(6, analisis.getResultado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Actualiza la información de un análisis clínico existente en la base de datos.
     * * ENTRADAS:
     * - analisis: Objeto de tipo Analisis con los datos actualizados y su código identificador.
     * * SALIDA: Un valor booleano (true si se modificó el registro correctamente, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión SQL.
     * 2. Llama a conn.prepareCall() para preparar la llamada al procedimiento "{call sp_editar_analisis(?, ?, ?, ?, ?, ?, ?)}".
     * 3. Llama a los métodos getters del objeto analisis (incluyendo getCodigoAnalisis()) para pasar los parámetros actualizados al CallableStatement.
     * 4. Llama a stmt.executeUpdate() para confirmar los cambios en SQL Server.
     */

    public boolean editAnalisis(Analisis analisis) {
        String sql = "{call sp_editar_analisis(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, analisis.getCodigoAnalisis());
            stmt.setInt(2, analisis.getConsulta() != null ? analisis.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(3, analisis.getTipo() != null ? analisis.getTipo().getCodigoTipo() : 0);
            stmt.setTimestamp(4, analisis.getFechaOrden() != null ? Timestamp.valueOf(analisis.getFechaOrden()) : null);
            stmt.setTimestamp(5, analisis.getFechaResultado() != null ? Timestamp.valueOf(analisis.getFechaResultado()) : null);
            stmt.setString(6, analisis.getEstado());
            stmt.setString(7, analisis.getResultado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Elimina un registro de análisis de la base de datos utilizando su código identificador.
     * * ENTRADAS:
     * - codigoAnalisis: Valor entero que representa la clave primaria del análisis a eliminar.
     * * SALIDA: Un valor booleano (true si la eliminación fue exitosa, false si falló o no se encontró el registro).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectarse al servidor de base de datos.
     * 2. Llama a conn.prepareCall() para invocar el procedimiento "{call sp_eliminar_analisis(?)}".
     * 3. Llama a stmt.setInt() para asignar la clave primaria como parámetro del procedimiento.
     * 4. Llama a stmt.executeUpdate() para ejecutar el borrado físico o lógico en la base de datos.
     */

    public boolean eliminarAnalisis(int codigoAnalisis) {
        String sql = "{call sp_eliminar_analisis(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoAnalisis);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Busca y construye un objeto Analisis específico filtrado por su código de identificación.
     * * ENTRADAS:
     * - codigoAnalisis: Identificador único del análisis clínico a consultar.
     * * SALIDA: Un objeto Analisis poblado con su consulta y tipo de análisis asociados, o null si no existe.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión JDBC.
     * 2. Llama a conn.prepareCall() para preparar el procedimiento "{call sp_buscar_analisis(?)}".
     * 3. Llama a stmt.executeQuery() para obtener un ResultSet con las columnas del análisis.
     * 4. Llama a rs.getTimestamp(), rs.getString() y rs.getInt() para reconstruir la entidad Analisis y sus asociaciones (Consulta y TipoAnalisis).
     */

    public Analisis buscarAnalisis(int codigoAnalisis) {
        Analisis analisis = null;
        String sql = "{call sp_buscar_analisis(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoAnalisis);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                analisis = new Analisis();
                analisis.setCodigoAnalisis(rs.getInt("codigo_analisis"));

                if (rs.getTimestamp("fechaOrden") != null) {
                    analisis.setFechaOrden(rs.getTimestamp("fechaOrden").toLocalDateTime());
                }
                if (rs.getTimestamp("fechaResultado") != null) {
                    analisis.setFechaResultado(rs.getTimestamp("fechaResultado").toLocalDateTime());
                }

                analisis.setEstado(rs.getString("estado"));
                analisis.setResultado(rs.getString("resultado"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_cons"));
                analisis.setConsulta(c);

                TipoAnalisis t = new TipoAnalisis();
                t.setCodigoTipo(rs.getInt("codigo_tipo"));
                t.setNombre(rs.getString("tipo_nombre"));
                t.setDescripcion(rs.getString("tipo_desc"));
                analisis.setTipo(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analisis;
    }



    /**
     * PROCESO: Recupera la lista completa de análisis clínicos registrados en la base de datos.
     * * ENTRADAS: N/A.
     * * SALIDA: Un ArrayList de objetos Analisis con toda la información detallada de cada registro.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para abrir el enlace a SQL Server.
     * 2. Llama a conn.prepareCall() para invocar "{call sp_listar_analisis()}".
     * 3. Llama a stmt.executeQuery() para obtener el listado completo mediante ResultSet.
     * 4. Recorre el ResultSet mediante rs.next() poblando la lista dinámica lista.add(analisis) en cada iteración.
     */

    public ArrayList<Analisis> listarAnalisis() {
        ArrayList<Analisis> lista = new ArrayList<>();
        String sql = "{call sp_listar_analisis()}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Analisis analisis = new Analisis();
                analisis.setCodigoAnalisis(rs.getInt("codigo_analisis"));

                if (rs.getTimestamp("fechaOrden") != null) {
                    analisis.setFechaOrden(rs.getTimestamp("fechaOrden").toLocalDateTime());
                }
                if (rs.getTimestamp("fechaResultado") != null) {
                    analisis.setFechaResultado(rs.getTimestamp("fechaResultado").toLocalDateTime());
                }

                analisis.setEstado(rs.getString("estado"));
                analisis.setResultado(rs.getString("resultado"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_cons"));
                analisis.setConsulta(c);

                TipoAnalisis t = new TipoAnalisis();
                t.setCodigoTipo(rs.getInt("codigo_tipo"));
                t.setNombre(rs.getString("tipo_nombre"));
                t.setDescripcion(rs.getString("tipo_desc"));
                analisis.setTipo(t);

                lista.add(analisis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }



    /**
     * PROCESO: Obtiene todos los análisis clínicos ordenados por un médico en específico a partir de su cédula.
     * * ENTRADAS:
     * - cedulaMedico: Cadena de texto que contiene la cédula identificadora del médico.
     * * SALIDA: Un ArrayList de objetos Analisis asociados a las consultas atendidas por dicho médico.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para establecer comunicación con la base de datos.
     * 2. Llama a conn.prepareCall() para preparar la llamada al procedimiento "{call sp_analisis_por_doctor(?)}".
     * 3. Llama a stmt.setString() para establecer el parámetro de la cédula del médico.
     * 4. Llama a stmt.executeQuery() y procesa el ResultSet asignando los análisis filtrados a la lista final.
     */

    public ArrayList<Analisis> getAnalisisPorDoctor(String cedulaMedico) {
        ArrayList<Analisis> lista = new ArrayList<>();
        String sql = "{call sp_analisis_por_doctor(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedulaMedico);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Analisis analisis = new Analisis();
                analisis.setCodigoAnalisis(rs.getInt("codigo_analisis"));

                if (rs.getTimestamp("fechaOrden") != null) {
                    analisis.setFechaOrden(rs.getTimestamp("fechaOrden").toLocalDateTime());
                }
                if (rs.getTimestamp("fechaResultado") != null) {
                    analisis.setFechaResultado(rs.getTimestamp("fechaResultado").toLocalDateTime());
                }

                analisis.setEstado(rs.getString("estado"));
                analisis.setResultado(rs.getString("resultado"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_cons"));
                analisis.setConsulta(c);

                TipoAnalisis t = new TipoAnalisis();
                t.setCodigoTipo(rs.getInt("codigo_tipo"));
                t.setNombre(rs.getString("tipo_nombre"));
                t.setDescripcion(rs.getString("tipo_desc"));
                analisis.setTipo(t);

                lista.add(analisis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}