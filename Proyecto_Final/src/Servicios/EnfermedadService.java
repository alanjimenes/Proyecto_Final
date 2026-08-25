package Servicios;

import logico.Cliente;
import logico.Enfermedad;
import logico.Historial;
import Utils.ConexionDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class EnfermedadService {


    /**
     * PROCESO: Inserta un nuevo catálogo o registro de enfermedad invocando el procedimiento almacenado 'sp_crear_enfermedad'.
     * * ENTRADAS:
     * - enf: Objeto Enfermedad con los atributos nombre, descripción y estado de vigilancia epidemiológica.
     * * SALIDA: boolean (true si el procedimiento ejecuta correctamente e inserta la fila, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos a través de ConexionDB.getConexion().
     * 2. Prepara el CallableStatement para la llamada "{call sp_crear_enfermedad(?, ?, ?)}".
     * 3. Setea nombre, descripción y el flag booleano de vigilancia.
     * 4. Ejecuta stmt.executeUpdate() y verifica si afectó al menos una fila.
     */

    public boolean agregarEnfermedad(Enfermedad enf) {
        String sql = "{call sp_crear_enfermedad(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, enf.getNombre());
            stmt.setString(2, enf.getDescripcion());
            stmt.setBoolean(3, enf.isVigilancia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Actualiza la información de una enfermedad en el catálogo mediante la ejecución del procedimiento almacenado 'sp_editar_enfermedad'.
     * * ENTRADAS:
     * - enf: Objeto Enfermedad con la clave primaria a modificar y sus nuevos atributos.
     * * SALIDA: boolean (true si se actualizó el registro con éxito, false en caso de error).
     * * FLUJO DE LLAMADAS:
     * 1. Solicita la conexión mediante ConexionDB.getConexion().
     * 2. Invoca la llamada al Stored Procedure "{call sp_editar_enfermedad(?, ?, ?, ?)}".
     * 3. Asigna los parámetros: código de enfermedad, nombre, descripción y vigilancia.
     * 4. Ejecuta la actualización devolviendo true si tuvo impacto en la base de datos.
     */

    public boolean editEnfermedad(Enfermedad enf) {
        String sql = "{call sp_editar_enfermedad(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, enf.getCodigoEnfermedad());
            stmt.setString(2, enf.getNombre());
            stmt.setString(3, enf.getDescripcion());
            stmt.setBoolean(4, enf.isVigilancia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Elimina una enfermedad del sistema ejecutando el procedimiento almacenado 'sp_eliminar_enfermedad'.
     * * ENTRADAS:
     * - codigoEnfermedad: Identificador único numérico de la enfermedad a borrar.
     * * SALIDA: boolean (true si se efectuó la eliminación correctamente, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta a la base de datos con ConexionDB.getConexion().
     * 2. Prepara la sentencia de llamado "{call sp_eliminar_enfermedad(?)}".
     * 3. Asigna el identificador mediante stmt.setInt(1, codigoEnfermedad).
     * 4. Ejecuta el procedimiento almacenado verificando su impacto con executeUpdate().
     */

    public boolean eliminarEnfermedad(int codigoEnfermedad) {
        String sql = "{call sp_eliminar_enfermedad(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoEnfermedad);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Recupera el catálogo completo de enfermedades registradas en la base de datos.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos Enfermedad.
     * * FLUJO DE LLAMADAS:
     * 1. Crea la conexión llamando a ConexionDB.getConexion().
     * 2. Prepara la consulta SELECT sobre la tabla 'enfermedad'.
     * 3. Mapea las filas del ResultSet instanciando objetos Enfermedad y seteando sus atributos.
     * 4. Retorna el listado completo.
     */

    public ArrayList<Enfermedad> listarEnfermedades() {
        ArrayList<Enfermedad> lista = new ArrayList<>();
        String sql = "select enfermedad.codigo_enfermedad, enfermedad.nombre, enfermedad.descripcion, enfermedad.vigilancia " +
                "from enfermedad";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Enfermedad enf = new Enfermedad();
                enf.setCodigoEnfermedad(rs.getInt("codigo_enfermedad"));
                enf.setNombre(rs.getString("nombre"));
                enf.setDescripcion(rs.getString("descripcion"));
                enf.setVigilancia(rs.getBoolean("vigilancia"));

                lista.add(enf);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * PROCESO: Genera un mapa de frecuencia de diagnósticos contabilizando cuántas veces ha sido diagnosticada cada enfermedad en las consultas registradas.
     * * ENTRADAS: Ninguna.
     * * SALIDA: HashMap con la clave String (nombre de la enfermedad) y el valor Integer (total de incidencias).
     * * FLUJO DE LLAMADAS:
     * 1. Obtiene la conexión desde ConexionDB.getConexion().
     * 2. Prepara la consulta con agrupamiento (GROUP BY) uniendo las tablas 'enfermedad' y 'enfermedad_consulta'.
     * 3. Mapea los pares (nombre, total) obtenidos del ResultSet en el HashMap de salida.
     */

    public HashMap<String, Integer> getFrecuenciaEnfermedades() {
        HashMap<String, Integer> mapa = new HashMap<>();
        String sql = "select enfermedad.nombre, count(enfermedad_consulta.codigo_enfermedad) AS total " +
                "from enfermedad " +
                "inner join enfermedad_consulta on enfermedad.codigo_enfermedad = enfermedad_consulta.codigo_enfermedad " +
                "group by enfermedad.nombre";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                mapa.put(rs.getString("nombre"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapa;
    }

    /**
     * PROCESO: Recupera las 5 enfermedades más frecuentemente diagnosticadas en el sistema ordenadas descendentemente por número de casos.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de cadenas (String) con los nombres de las 5 enfermedades principales.
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos a través de ConexionDB.getConexion().
     * 2. Prepara la sentencia SQL con la cláusula TOP 5, realizando un GROUP BY por nombre y ordenando por el conteo total descendente.
     * 3. Extrae los nombres del ResultSet y los almacena en el listado de retorno.
     */

    public ArrayList<String> getTop5Enfermedades() {
        ArrayList<String> top5 = new ArrayList<>();
        String sql = "select top 5 enfermedad.nombre, count(enfermedad_consulta.codigo_enfermedad) as total " +
                "from enfermedad " +
                "inner join enfermedad_consulta on enfermedad.codigo_enfermedad = enfermedad_consulta.codigo_enfermedad " +
                "group by enfermedad.nombre " +
                "order by total desc";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                top5.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top5;
    }


    /**
     * PROCESO: Obtiene una lista sin duplicados de los nombres de enfermedades que le han sido diagnosticadas a un cliente específico utilizando su número de expediente.
     * * ENTRADAS:
     * - numExpediente: Número de expediente único asignado al cliente.
     * * SALIDA: ArrayList de String conteniendo los nombres distintos de enfermedades registradas.
     * * FLUJO DE LLAMADAS:
     * 1. Conecta a la base de datos mediante ConexionDB.getConexion().
     * 2. Ejecuta un query SELECT DISTINCT uniendo las tablas enfermedad, enfermedad_consulta, consulta y cliente.
     * 3. Filtra la búsqueda con el parámetro stmt.setString(1, numExpediente).
     * 4. Itera el ResultSet y llena la lista con los nombres recuperados.
     */

    public ArrayList<String> getEnfermedadesDeCliente(String numExpediente) {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "select distinct enfermedad.nombre " +
                "from enfermedad " +
                "inner join enfermedad_consulta on enfermedad.codigo_enfermedad = enfermedad_consulta.codigo_enfermedad " +
                "inner join consulta on enfermedad_consulta.codigo_consulta = consulta.codigo_cons " +
                "inner join cliente on consulta.codigo_cliente = cliente.codigo_persona " +
                "where cliente.numexpediente = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numExpediente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Cliente> getClientesPorEnfermedad(String nombreEnfermedad) {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "select distinct persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, " +
                "persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, persona.genero, " +
                "cliente.numexpediente, cliente.enfermo, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "inner join consulta on cliente.codigo_persona = consulta.codigo_cliente " +
                "inner join enfermedad_consulta on consulta.codigo_cons = enfermedad_consulta.codigo_consulta " +
                "inner join enfermedad on enfermedad_consulta.codigo_enfermedad = enfermedad.codigo_enfermedad " +
                "where enfermedad.nombre = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreEnfermedad);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setCodigoPersona(rs.getInt("codigo_persona"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setCedula(rs.getString("cedula"));
                cliente.setTelefono(rs.getString("telefono"));

                if (rs.getDate("fechanacimiento") != null) {
                    cliente.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }

                cliente.setDireccion(rs.getString("direccion"));
                cliente.setEstado(rs.getBoolean("estado"));
                cliente.setGenero(rs.getString("genero"));
                cliente.setNumExpediente(rs.getString("numexpediente"));
                cliente.setEnfermo(rs.getBoolean("enfermo"));
                cliente.setAntecedentes(rs.getString("antecedentes"));
                cliente.setHistorial(new Historial());

                lista.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}