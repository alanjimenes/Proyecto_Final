package Servicios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Utils.ConexionDB;
import logico.User;

public class UserService {

    /**
     * PROCESO: Autentica las credenciales de un usuario y recupera su información de perfil incluyendo su cédula asociada.
     *
     * ENTRADAS:
     * - usuario: Nombre de usuario/cuenta con el que se intenta iniciar sesión.
     * - password: Contraseña asociada al usuario.
     *
     * SALIDA: Objeto User populado si las credenciales son válidas, o null si la autenticación falla.
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para iniciar la comunicación con la base de datos.
     * 2. Llama a conn.prepareStatement() ejecutando la consulta SELECT con JOIN a las tablas usuario, medico, enfermera y persona.
     * 3. Asigna las credenciales mediante stmt.setString(1, usuario) y stmt.setString(2, password).
     * 4. Lee el ResultSet y, de ser válido, mapea sus datos (código, nombre de usuario, contraseña, rol y cédula resultante).
     */

    public User login(String usuario, String password) {
        User user = null;
        String sql = "select usuario.codigo_usuario, usuario.nombreusuario, usuario.password, usuario.rol, " +
                "coalesce(p_med.cedula, p_enf.cedula) as cedula " +
                "from usuario " +
                "left join medico on usuario.codigo_usuario = medico.codigo_usuario " +
                "left join persona p_med on medico.codigo_persona = p_med.codigo_persona " +
                "left join enfermera on usuario.codigo_usuario = enfermera.codigo_usuario " +
                "left join persona p_enf on enfermera.codigo_persona = p_enf.codigo_persona " +
                "where usuario.nombreusuario = ? and usuario.password = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setCodigoUsuario(rs.getInt("codigo_usuario"));
                    user.setNombreUsuario(rs.getString("nombreusuario"));
                    user.setPassword(rs.getString("password"));
                    user.setRol(rs.getString("rol"));
                    user.setCedula(rs.getString("cedula"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }



    /**
     * PROCESO: Verifica si ya existe un nombre de usuario registrado en el sistema.
     *
     * ENTRADAS:
     * - usuario: Nombre de usuario a comprobar en la base de datos.
     *
     * SALIDA: boolean (true si el usuario ya existe, false si está disponible).
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener una conexión activa.
     * 2. Llama a conn.prepareStatement() sobre la tabla usuario buscando por nombreusuario.
     * 3. Asigna la cadena del usuario mediante stmt.setString(1, usuario).
     * 4. Retorna el resultado directo de rs.next() para validar la presencia de la tupla.
     */

    public boolean existeUsuario(String usuario) {
        String sql = "select usuario.codigo_usuario " +
                "from usuario " +
                "where usuario.nombreusuario = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * PROCESO: Registra un nuevo usuario en el sistema vinculando sus credenciales, rol y cédula de identidad.
     *
     * ENTRADAS:
     * - user: Objeto User con los campos necesarios para la creación (nombreUsuario, password, rol y cedula).
     *
     * SALIDA: boolean (true si el registro fue insertado con éxito, false en caso contrario).
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para realizar la conexión a la base de datos.
     * 2. Llama a conn.prepareCall() invocando el procedimiento almacenado sp_crear_usuario.
     * 3. Completa los 4 parámetros del statement con la información del objeto User.
     * 4. Ejecuta stmt.executeUpdate() para impactar los datos en el sistema.
     */

    public boolean registrarUsuario(User user) {
        String sql = "{call sp_crear_usuario(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, user.getNombreUsuario());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRol());
            stmt.setString(4, user.getCedula());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Recupera el listado general de usuarios de la aplicación integrando la cédula según su rol (Médico o Enfermera).
     *
     * ENTRADAS: Ninguna.
     *
     * SALIDA: ArrayList<User> ordenado alfabéticamente por el nombre de usuario.
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectar con la base de datos.
     * 2. Llama a conn.prepareStatement() con la sentencia SELECT que incluye LEFT JOINs sobre medico, enfermera y persona.
     * 3. Itera las filas del ResultSet asignando las propiedades requeridas al objeto User.
     * 4. Agrega cada usuario a la lista que será retornada.
     */

    public ArrayList<User> listarUsuarios() {
        ArrayList<User> lista = new ArrayList<>();
        String sql = "select usuario.codigo_usuario, usuario.nombreusuario, usuario.password, usuario.rol, " +
                "coalesce(p_med.cedula, p_enf.cedula) as cedula " +
                "from usuario " +
                "left join medico on usuario.codigo_usuario = medico.codigo_usuario " +
                "left join persona p_med on medico.codigo_persona = p_med.codigo_persona " +
                "left join enfermera on usuario.codigo_usuario = enfermera.codigo_usuario " +
                "left join persona p_enf on enfermera.codigo_persona = p_enf.codigo_persona " +
                "order by usuario.nombreusuario";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setCodigoUsuario(rs.getInt("codigo_usuario"));
                user.setNombreUsuario(rs.getString("nombreusuario"));
                user.setPassword(rs.getString("password"));
                user.setRol(rs.getString("rol"));
                user.setCedula(rs.getString("cedula"));
                lista.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * PROCESO: Elimina la cuenta de un usuario específico del sistema según su nombre de usuario.
     *
     * ENTRADAS:
     * - usuario: Nombre de cuenta del usuario a remover.
     *
     * SALIDA: boolean (true si fue removido correctamente, false si ocurrió un error o no existía).
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectarse con la base de datos.
     * 2. Llama a conn.prepareCall() especificando el procedimiento almacenado sp_eliminar_usuario.
     * 3. Asigna la clave única de usuario en stmt.setString(1, usuario).
     * 4. Ejecuta stmt.executeUpdate() retornando el estado de la operación.
     */

    public boolean eliminarUsuario(String usuario) {
        String sql = "{call sp_eliminar_usuario(?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * PROCESO: Actualiza las credenciales y el rol de un usuario existente en la base de datos.
     *
     * ENTRADAS:
     * - user: Objeto User con las credenciales y el rol modificados.
     *
     * SALIDA: boolean (true si la edición se procesó de forma correcta, false en caso contrario).
     *
     * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para interactuar con la base de datos.
     * 2. Llama a conn.prepareCall() ejecutando el procedimiento sp_editar_usuario.
     * 3. Setea los parámetros del nombre de usuario, nueva contraseña y rol.
     * 4. Realiza el stmt.executeUpdate() verificando si el número de registros alterados es superior a 0.
     */

    public boolean actualizarUsuario(User user) {
        String sql = "{call sp_editar_usuario(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, user.getNombreUsuario());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRol());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}