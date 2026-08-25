package Servicios;

import logico.Especialidad;
import logico.Medico;
import Utils.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MedicoService {


    /**
     * PROCESO: Registra un nuevo médico en la base de datos ejecutando el procedimiento almacenado 'sp_crear_medico', vinculándolo con un usuario y una especialidad.
     * * ENTRADAS:
     * - med: Objeto Medico con la información personal y configuración del número máximo de citas por día.
     * - codigoUsuario: Identificador numérico de la cuenta de usuario asociada.
     * - codigoEspecialidad: Identificador numérico de la especialidad asignada.
     * * SALIDA: boolean (true si el registro fue exitoso, false en caso de error).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con ConexionDB.getConexion().
     * 2. Prepara la llamada al procedimiento "{call sp_crear_medico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}".
     * 3. Setea los datos personales del médico junto con 'codigoUsuario', 'codigoEspecialidad' y 'maxCitasPorDia'.
     * 4. Ejecuta stmt.executeUpdate() para registrar la entidad.
     */
    public boolean agregarMedico(Medico med, int codigoUsuario, int codigoEspecialidad) {
        String sql = "{call sp_crear_medico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(med.getFechaNacimiento()));
            stmt.setString(2, med.getNombre());
            stmt.setString(3, med.getApellido());
            stmt.setString(4, med.getCedula());
            stmt.setString(5, med.getTelefono());
            stmt.setBoolean(6, med.getEstado());
            stmt.setString(7, med.getDireccion());
            stmt.setString(8, med.getGenero());
            stmt.setInt(9, codigoUsuario);
            stmt.setInt(10, codigoEspecialidad);
            stmt.setInt(11, med.getMaxCitasPorDia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Modifica la información personal, especialidad y límite diario de citas de un médico invocando 'sp_editar_medico'.
     * * ENTRADAS:
     * - med: Objeto Medico con la información actualizada a persistir.
     * * SALIDA: boolean (true si se actualizaron los datos, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta mediante ConexionDB.getConexion().
     * 2. Invocación de la sentencia "{call sp_editar_medico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}".
     * 3. Asigna los parámetros según la firma del procedimiento almacenado.
     * 4. Llama a stmt.executeUpdate() para guardar los cambios.
     */

    public boolean actualizarMedico(Medico med) {
        String sql = "{call sp_editar_medico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(med.getFechaNacimiento()));
            stmt.setString(2, med.getNombre());
            stmt.setString(3, med.getApellido());
            stmt.setString(4, med.getTelefono());
            stmt.setString(5, med.getDireccion());
            stmt.setBoolean(6, med.getEstado());
            stmt.setString(7, med.getGenero());
            stmt.setString(8, med.getCedula());
            stmt.setInt(9, med.getEspecialidad().getCodigoEspecialidad());
            stmt.setInt(10, med.getMaxCitasPorDia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Desactiva a un médico en el sistema ejecutando el procedimiento almacenado 'sp_desactivar_medico'.
     * * ENTRADAS:
     * - cedula: Documento de identidad único del médico a inhabilitar.
     * * SALIDA: boolean (true si el cambio de estado fue exitoso, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Conecta a la base de datos vía ConexionDB.getConexion().
     * 2. Prepara la sentencia "{call sp_desactivar_medico(?)}".
     * 3. Setea la cédula con stmt.setString(1, cedula).
     * 4. Ejecuta stmt.executeUpdate().
     */

    public boolean desactivarMedico(String cedula) {
        String sql = "{call sp_desactivar_medico(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedula);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Busca un médico por su número de cédula, relacionando sus datos personales con su especialidad correspondiente.
     * * ENTRADAS:
     * - cedula: Cédula de identidad del médico.
     * * SALIDA: Objeto Medico instanciado o null si no existe.
     * * FLUJO DE LLAMADAS:
     * 1. Solicita la conexión vía ConexionDB.getConexion().
     * 2. Prepara el SQL SELECT relacionando 'medico', 'persona' y 'especialidad' mediante INNER JOIN.
     * 3. Setea la cédula en stmt.setString(1, cedula).
     * 4. Construye el objeto Medico junto a su objeto Especialidad embebido a partir del ResultSet.
     */

    public Medico buscarMedicoCedula(String cedula) {
        Medico medico = null;
        String sql = "select persona.codigo_persona, persona.fechanacimiento, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.estado, persona.direccion, persona.genero, medico.maxcitaspordia, " +
                "especialidad.codigo_especialidad, especialidad.nombre AS nombre_esp " +
                "from medico " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "inner join especialidad on medico.codigo_especialidad = especialidad.codigo_especialidad " +
                "where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                esp.setNombre(rs.getString("nombre_esp"));

                medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_persona"));
                if (rs.getDate("fechanacimiento") != null) {
                    medico.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                medico.setCedula(rs.getString("cedula"));
                medico.setTelefono(rs.getString("telefono"));
                medico.setDireccion(rs.getString("direccion"));
                medico.setEstado(rs.getBoolean("estado"));
                medico.setGenero(rs.getString("genero"));
                medico.setMaxCitasPorDia(rs.getInt("maxcitaspordia"));
                medico.setEspecialidad(esp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medico;
    }


    /**
     * PROCESO: Retorna la lista de todos los médicos registrados en el sistema, uniendo sus datos personales y especialidad.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos Medico.
     * * FLUJO DE LLAMADAS:
     * 1. Conecta mediante ConexionDB.getConexion().
     * 2. Prepara la consulta SELECT uniendo las tablas 'medico', 'persona' y 'especialidad'.
     * 3. Itera sobre el ResultSet instanciando cada Medico con su respectiva Especialidad.
     * 4. Retorna la lista poblada.
     */

    public ArrayList<Medico> listarMedicos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.fechanacimiento, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.estado, persona.direccion, persona.genero, medico.maxcitaspordia, " +
                "especialidad.codigo_especialidad, especialidad.nombre AS nombre_esp " +
                "from medico " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "inner join especialidad on medico.codigo_especialidad = especialidad.codigo_especialidad";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                esp.setNombre(rs.getString("nombre_esp"));

                Medico medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_persona"));
                if (rs.getDate("fechanacimiento") != null) {
                    medico.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                medico.setCedula(rs.getString("cedula"));
                medico.setTelefono(rs.getString("telefono"));
                medico.setDireccion(rs.getString("direccion"));
                medico.setEstado(rs.getBoolean("estado"));
                medico.setGenero(rs.getString("genero"));
                medico.setMaxCitasPorDia(rs.getInt("maxcitaspordia"));
                medico.setEspecialidad(esp);

                lista.add(medico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * PROCESO: Verifica la disponibilidad de agenda de un médico para evitar solapamientos de citas en un rango de horario dado.
     * * ENTRADAS:
     * - cedula: Cédula de identidad del médico.
     * - fechaHora: Fecha y hora de inicio proyectada para la cita.
     * - finHora: Fecha y hora límite para verificar colisiones en la agenda.
     * * SALIDA: boolean (true si el médico está disponible sin solapamientos en estado 'Pendiente', false si ya tiene una cita reservada).
     * * FLUJO DE LLAMADAS:
     * 1. Abre la conexión llamando a ConexionDB.getConexion().
     * 2. Prepara una consulta SQL COUNT sobre 'cita' filtrando por médico, estado 'Pendiente' y rango temporal.
     * 3. Asigna la cédula y las marcas de tiempo (Timestamp) inicial/final.
     * 4. Evalúa si el contador de solapamientos es igual a 0 para determinar la disponibilidad.
     */

    public boolean verificarDisponibilidad(String cedula, LocalDateTime fechaHora, LocalDateTime finHora) {
        boolean disponible = false;
        String sql = "select count(cita.codigo_cita) AS solapamientos " +
                "from cita " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ? and cita.estado = 'Pendiente' and (cita.fechacita < ? and dateadd(minute, 30, cita.fechacita) > ?)";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            stmt.setTimestamp(2, Timestamp.valueOf(finHora));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaHora));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt("solapamientos") == 0) {
                    disponible = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disponible;
    }



    /**
     * PROCESO: Obtiene el directorio de médicos activos mediante la consulta de la vista 'vw_directorio_medico'.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos Medico que se encuentran actualmente activos.
     * * FLUJO DE LLAMADAS:
     * 1. Abre la conexión vía ConexionDB.getConexion().
     * 2. Prepara y ejecuta la consulta SELECT sobre 'vw_directorio_medico'.
     * 3. Mapea las tuplas retornadas construyendo los objetos Medico y Especialidad.
     * 4. Retorna el listado activo.
     */

    public ArrayList<Medico> listarMedicosActivos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "select vw_directorio_medico.* from vw_directorio_medico";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                esp.setNombre(rs.getString("nombre_esp"));

                Medico medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_persona"));
                if (rs.getDate("fechanacimiento") != null) {
                    medico.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                medico.setCedula(rs.getString("cedula"));
                medico.setTelefono(rs.getString("telefono"));
                medico.setDireccion(rs.getString("direccion"));
                medico.setEstado(rs.getBoolean("estado"));
                medico.setGenero(rs.getString("genero"));
                medico.setMaxCitasPorDia(rs.getInt("maxcitaspordia"));
                medico.setEspecialidad(esp);

                lista.add(medico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}