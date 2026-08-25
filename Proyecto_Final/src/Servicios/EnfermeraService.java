package Servicios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

import logico.*;
import Utils.ConexionDB;

public class EnfermeraService {

    /**
     * PROCESO: Registra una nueva enfermera en el sistema mediante la invocación del procedimiento almacenado 'sp_crear_enfermera'.
     * * ENTRADAS:
     * - enfermera: Objeto Enfermera que contiene los datos personales (nombre, apellido, cédula, teléfono, dirección, género, fecha de nacimiento, estado) y el turno asignado.
     * * SALIDA: boolean (true si la creación se completó con éxito, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos a través de ConexionDB.getConexion().
     * 2. Prepara la llamada al procedimiento "{call sp_crear_enfermera(?, ?, ?, ?, ?, ?, ?, ?, ?)}".
     * 3. Mapea los atributos de la entidad a los parámetros requeridos por el procedimiento almacenado.
     * 4. Ejecuta la sentencia mediante stmt.executeUpdate() comprobando que devuelva un contador mayor a cero.
     */

    public boolean crearEnfermera(Enfermera enfermera) {
        String sql = "{call sp_crear_enfermera(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(enfermera.getFechaNacimiento()));
            stmt.setString(2, enfermera.getNombre());
            stmt.setString(3, enfermera.getApellido());
            stmt.setString(4, enfermera.getCedula());
            stmt.setString(5, enfermera.getTelefono());
            stmt.setBoolean(6, enfermera.getEstado());
            stmt.setString(7, enfermera.getDireccion());
            stmt.setString(8, enfermera.getGenero());
            stmt.setString(9, enfermera.getTurno());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Actualiza la información personal y el turno de una enfermera registrada ejecutando el procedimiento almacenado 'sp_editar_enfermera'.
     * * ENTRADAS:
     * - enfermera: Objeto Enfermera con los datos actualizados a persistir.
     * * SALIDA: boolean (true si los cambios fueron guardados exitosamente, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Establece la conexión a la base de datos llamando a ConexionDB.getConexion().
     * 2. Invocación de la llamada al Stored Procedure "{call sp_editar_enfermera(?, ?, ?, ?, ?, ?, ?, ?, ?)}".
     * 3. Asigna cada valor en el orden definido por la firma del procedimiento almacenado.
     * 4. Llama a stmt.executeUpdate() para efectuar la actualización.
     */

    public boolean editEnfermera(Enfermera enfermera) {
        String sql = "{call sp_editar_enfermera(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(enfermera.getFechaNacimiento()));
            stmt.setString(2, enfermera.getNombre());
            stmt.setString(3, enfermera.getApellido());
            stmt.setString(4, enfermera.getTelefono());
            stmt.setString(5, enfermera.getDireccion());
            stmt.setBoolean(6, enfermera.getEstado());
            stmt.setString(7, enfermera.getGenero());
            stmt.setString(8, enfermera.getCedula());
            stmt.setString(9, enfermera.getTurno());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Busca una enfermera registrada mediante su número de cédula única, uniendo la información de la tabla persona con la tabla enfermera.
     * * ENTRADAS:
     * - cedula: Documento de identidad único de la enfermera.
     * * SALIDA: Objeto Enfermera completamente instanciado o null si no se encuentra coincidencia.
     * * FLUJO DE LLAMADAS:
     * 1. Abre la conexión mediante ConexionDB.getConexion().
     * 2. Ejecuta un SQL SELECT uniendo la tabla 'enfermera' con la tabla 'persona' a través de 'codigo_persona'.
     * 3. Setea la cédula en stmt.setString(1, cedula).
     * 4. Construye el objeto Enfermera asignando cada atributo a partir del ResultSet retornado.
     */

    public Enfermera buscarEnfermera(String cedula) {
        Enfermera enfermera = null;
        String sql = "select persona.codigo_persona, persona.cedula, persona.nombre, persona.apellido, " +
                "persona.fechanacimiento, persona.telefono, persona.direccion, persona.estado, " +
                "persona.genero, enfermera.turno " +
                "from enfermera " +
                "inner join persona on enfermera.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                enfermera = new Enfermera();
                enfermera.setCodigoPersona(rs.getInt("codigo_persona"));
                enfermera.setCedula(rs.getString("cedula"));
                enfermera.setNombre(rs.getString("nombre"));
                enfermera.setApellido(rs.getString("apellido"));
                if (rs.getDate("fechanacimiento") != null) {
                    enfermera.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                enfermera.setTelefono(rs.getString("telefono"));
                enfermera.setDireccion(rs.getString("direccion"));
                enfermera.setEstado(rs.getBoolean("estado"));
                enfermera.setGenero(rs.getString("genero"));
                enfermera.setTurno(rs.getString("turno"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enfermera;
    }


    /**
     * PROCESO: Consulta y recupera el listado general de todas las enfermeras registradas en la base de datos.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos Enfermera.
     * * FLUJO DE LLAMADAS:
     * 1. Crea la conexión vía ConexionDB.getConexion().
     * 2. Prepara la sentencia SELECT con INNER JOIN entre 'enfermera' y 'persona'.
     * 3. Itera sobre las filas devueltas instanciando y seteando cada objeto Enfermera.
     * 4. Retorna el listado poblado.
     */

    public ArrayList<Enfermera> listarEnfermeras() {
        ArrayList<Enfermera> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.cedula, persona.nombre, persona.apellido, persona.telefono, " +
                "persona.direccion, persona.genero, persona.estado, persona.fechanacimiento, enfermera.turno " +
                "from enfermera " +
                "inner join persona on enfermera.codigo_persona = persona.codigo_persona";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Enfermera enfermera = new Enfermera();
                enfermera.setCodigoPersona(rs.getInt("codigo_persona"));
                enfermera.setCedula(rs.getString("cedula"));
                enfermera.setNombre(rs.getString("nombre"));
                enfermera.setApellido(rs.getString("apellido"));
                enfermera.setTelefono(rs.getString("telefono"));
                enfermera.setDireccion(rs.getString("direccion"));
                enfermera.setGenero(rs.getString("genero"));
                enfermera.setEstado(rs.getBoolean("estado"));
                if (rs.getDate("fechanacimiento") != null) {
                    enfermera.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                enfermera.setTurno(rs.getString("turno"));

                lista.add(enfermera);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }



    /**
     * PROCESO: Desactiva o inhabilita a una enfermera en la base de datos invocando el procedimiento almacenado 'sp_desactivar_enfermera'.
     * * ENTRADAS:
     * - cedula: Documento de identidad único de la enfermera a desactivar.
     * * SALIDA: boolean (true si se aplicó el cambio de estado con éxito, false en caso de error).
     * * FLUJO DE LLAMADAS:
     * 1. Solicita una conexión a ConexionDB.getConexion().
     * 2. Prepara la llamada al procedimiento almacenado "{call sp_desactivar_enfermera(?)}".
     * 3. Asigna la cédula en stmt.setString(1, cedula).
     * 4. Ejecuta stmt.executeUpdate() para cambiar el estado.
     */

    public boolean desactivarEnfermera(String cedula) {
        String sql = "{call sp_desactivar_enfermera(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedula);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}