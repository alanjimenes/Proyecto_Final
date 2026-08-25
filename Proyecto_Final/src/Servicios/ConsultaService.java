package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConsultaService {


    /**
     * PROCESO: Registra una consulta médica completa dentro de una transacción, integrando la resolución de cédulas de médico y cliente, evaluación física, recetas médicas y enfermedades diagnosticadas.
     * * ENTRADAS:
     * - con: Objeto Consulta que contiene la información básica (fecha, síntomas, diagnóstico) y sus listados/objetos asociados.
     * - cedulaMedico: Cédula de identidad del médico que atiende la consulta.
     * - cedulaCliente: Cédula de identidad del cliente/paciente que recibe la consulta.
     * * SALIDA: boolean (true si toda la transacción se completa con éxito, false en caso de error o rollback).
     * * FLUJO DE LLAMADAS:
     * 1. Solicita la conexión mediante ConexionDB.getConexion() y desactiva el autoCommit para iniciar una transacción.
     * 2. Prepara e ejecuta la sentencia INSERT en 'consulta' resolviendo las llaves foráneas mediante subconsultas por cédula y solicita las llaves generadas (RETURN_GENERATED_KEYS).
     * 3. Recupera el ID autogenerado de la consulta registrada. Si falla, ejecuta rollback.
     * 4. Si la consulta incluye evaluación física, delega a EvaluacionFisicaService.registrarEvaluacion().
     * 5. Si incluye recetas, itera y llama a RecetaMedicaService.registrarReceta().
     * 6. Si incluye enfermedades diagnosticadas, itera y llama a EnfermedadConsultaService.registrarDiagnostico().
     * 7. Confirma la transacción con conn.commit() y reestablece el autoCommit/cierra la conexión en el bloque finally.
     */

    public boolean registrarConsultaCompleta(Consulta con, String cedulaMedico, String cedulaCliente) {
        String sqlConsulta = "insert into consulta (fechaconsulta, sintomas, diagnostico, codigo_medico, codigo_cliente) " +
                "values (?, ?, ?, (" +
                "select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?), (" +
                "select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?))";

        Connection conn = null;
        RecetaMedicaService recetaMedicaService = new RecetaMedicaService();
        EvaluacionFisicaService evaluacionService = new EvaluacionFisicaService();
        EnfermedadConsultaService enfermedadService = new EnfermedadConsultaService();

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);
            int generatedId = -1;

            try (PreparedStatement stmtCons = conn.prepareStatement(sqlConsulta, Statement.RETURN_GENERATED_KEYS)) {
                stmtCons.setTimestamp(1, Timestamp.valueOf(con.getFechaConsulta().atStartOfDay()));
                stmtCons.setString(2, con.getSintomas());
                stmtCons.setString(3, con.getDiagnostico());
                stmtCons.setString(4, cedulaMedico);
                stmtCons.setString(5, cedulaCliente);
                stmtCons.executeUpdate();

                try (ResultSet rs = stmtCons.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }

            if (generatedId <= 0) {
                conn.rollback();
                return false;
            }

            if (con.getEvaluacion() != null) {
                evaluacionService.registrarEvaluacion(conn, con.getEvaluacion(), generatedId);
            }

            if (con.getRecetas() != null && !con.getRecetas().isEmpty()) {
                for (RecetaMedica receta : con.getRecetas()) {
                    recetaMedicaService.registrarReceta(conn, receta, generatedId);
                }
            }

            if (con.getEnfermedadesDiag() != null && !con.getEnfermedadesDiag().isEmpty()) {
                for (Enfermedad enf : con.getEnfermedadesDiag()) {
                    enfermedadService.registrarDiagnostico(conn, enf, generatedId);
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * PROCESO: Ejecuta un procedimiento almacenado ('sp_registrar_consulta') para registrar una consulta e incluir simultáneamente sus constantes vitales o evaluación física inicial.
     * * ENTRADAS:
     * - codigoMedico: Código numérico de la persona/médico.
     * - codigoCliente: Código numérico de la persona/cliente.
     * - fechaConsulta: Fecha y hora exacta en que se realiza la consulta.
     * - sintomas: Cuadro clínico expresado por el cliente.
     * - diagnostico: Juicio o dictamen clínico emitido por el médico.
     * - temperatura: Medición de temperatura corporal del paciente.
     * - frecuenciaCardiaca: Frecuencia cardíaca registrada (ppm).
     * - presionArterial: Indicador de presión arterial (ej. "120/80").
     * - peso: Peso registrado del cliente.
     * - talla: Estatura o talla registrada del cliente.
     * * SALIDA: boolean (true si la ejecución del Stored Procedure es exitosa, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Obtiene la conexión desde ConexionDB.getConexion().
     * 2. Prepara la llamada al procedimiento mediante conn.prepareCall("{CALL sp_registrar_consulta(...)}").
     * 3. Setea los 10 parámetros requeridos por el procedimiento almacenado.
     * 4. Ejecuta el CallableStatement mediante stmt.execute().
     */

    public boolean registrarConsultaSp(int codigoMedico, int codigoCliente, LocalDateTime fechaConsulta, String sintomas, String diagnostico, double temperatura, int frecuenciaCardiaca, String presionArterial, double peso, double talla) {

        String sql = "{CALL sp_registrar_consulta(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";


        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, codigoMedico);
            stmt.setInt(2, codigoCliente);
            stmt.setTimestamp(3, Timestamp.valueOf(fechaConsulta));
            stmt.setString(4, sintomas);
            stmt.setString(5, diagnostico);
            stmt.setDouble(6, temperatura);
            stmt.setInt(7, frecuenciaCardiaca);
            stmt.setString(8, presionArterial);
            stmt.setDouble(9, peso);
            stmt.setDouble(10, talla);

            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Consulta el registro global de consultas ordenadas por fecha de forma descendente, construyendo y vinculando los objetos completos de Cliente, Medico, Especialidad y Enfermedades asociadas.
     * * ENTRADAS: Ninguna.
     * * SALIDA: ArrayList de objetos Consulta completamente poblados.
     * * FLUJO DE LLAMADAS:
     * 1. Conecta a la base de datos a través de ConexionDB.getConexion().
     * 2. Prepara la sentencia SQL integrando múltiples INNER JOIN y LEFT JOIN (consulta, cliente, medico, persona, especialidad).
     * 3. En cada iteración del ResultSet, instancia y puebla las entidades Consulta, Cliente y Medico (incluyendo Especialidad si corresponde).
     * 4. Llama privadamente a obtenerEnfermedadesDeConsulta() usando la misma conexión para asociar el listado de enfermedades registradas en cada consulta.
     * 5. Agrega el objeto estructurado a la lista de retorno.
     */

    public ArrayList<Consulta> getTodasLasConsultas() {
        ArrayList<Consulta> lista = new ArrayList<>();

        String sql = "select " +
                "c.codigo_cons, " +
                "c.fechaconsulta, " +
                "c.sintomas, " +
                "c.diagnostico, " +

                // Datos del cliente
                "pc.codigo_persona as codigo_cliente, " +
                "pc.nombre as nombre_cliente, " +
                "pc.apellido as apellido_cliente, " +
                "pc.cedula as cedula_cliente, " +
                "pc.telefono as telefono_cliente, " +
                "pc.fechanacimiento as fecha_nacimiento_cliente, " +
                "pc.direccion as direccion_cliente, " +
                "pc.estado as estado_cliente, " +
                "pc.genero as genero_cliente, " +
                "cl.numexpediente, " +
                "cl.enfermo, " +
                "cl.antecedentes, " +

                // Datos del médico
                "pm.codigo_persona as codigo_medico, " +
                "pm.nombre as nombre_medico, " +
                "pm.apellido as apellido_medico, " +
                "pm.cedula as cedula_medico, " +
                "pm.telefono as telefono_medico, " +
                "pm.fechanacimiento as fecha_nacimiento_medico, " +
                "pm.direccion as direccion_medico, " +
                "pm.estado as estado_medico, " +
                "pm.genero as genero_medico, " +
                "m.maxcitaspordia, " +

                // Datos de la especialidad
                "e.codigo_especialidad, " +
                "e.nombre as nombre_especialidad " +

                "from consulta c " +

                "inner join cliente cl " +
                "on c.codigo_cliente = cl.codigo_persona " +

                "inner join persona pc " +
                "on cl.codigo_persona = pc.codigo_persona " +

                "inner join medico m " +
                "on c.codigo_medico = m.codigo_persona " +

                "inner join persona pm " +
                "on m.codigo_persona = pm.codigo_persona " +

                "left join especialidad e " +
                "on m.codigo_especialidad = e.codigo_especialidad " +

                "order by c.fechaconsulta desc";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Consulta consulta = new Consulta();

                // DATOS DE LA CONSULTA
                consulta.setCodigoConsulta(rs.getInt("codigo_cons"));
                Timestamp fechaConsulta = rs.getTimestamp("fechaconsulta");

                if (fechaConsulta != null) {
                    consulta.setFechaConsulta(fechaConsulta.toLocalDateTime().toLocalDate());
                }

                consulta.setSintomas(rs.getString("sintomas"));
                consulta.setDiagnostico(rs.getString("diagnostico"));

                // CLIENTE
                Cliente cliente = new Cliente();
                cliente.setCodigoPersona(rs.getInt("codigo_cliente"));
                cliente.setNombre(rs.getString("nombre_cliente"));
                cliente.setApellido(rs.getString("apellido_cliente"));
                cliente.setCedula(rs.getString("cedula_cliente"));
                cliente.setTelefono(rs.getString("telefono_cliente"));
                Date fechaNacimientoCliente = rs.getDate("fecha_nacimiento_cliente");

                if (fechaNacimientoCliente != null) {
                    cliente.setFechaNacimiento(fechaNacimientoCliente.toLocalDate());
                }

                cliente.setDireccion(rs.getString("direccion_cliente"));
                cliente.setEstado(rs.getBoolean("estado_cliente"));
                cliente.setGenero(rs.getString("genero_cliente"));
                cliente.setNumExpediente(rs.getString("numexpediente"));
                cliente.setEnfermo(rs.getBoolean("enfermo"));
                cliente.setAntecedentes(rs.getString("antecedentes"));
                consulta.setCliente(cliente);

                // MEDICO
                Medico medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_medico"));
                medico.setNombre(rs.getString("nombre_medico"));
                medico.setApellido(rs.getString("apellido_medico"));
                medico.setCedula(rs.getString("cedula_medico"));
                medico.setTelefono(rs.getString("telefono_medico"));
                Date fechaNacimientoMedico = rs.getDate("fecha_nacimiento_medico");

                if (fechaNacimientoMedico != null) {
                    medico.setFechaNacimiento(fechaNacimientoMedico.toLocalDate());
                }

                medico.setDireccion(rs.getString("direccion_medico"));
                medico.setEstado(rs.getBoolean("estado_medico"));
                medico.setGenero(rs.getString("genero_medico"));
                medico.setMaxCitasPorDia(rs.getInt("maxcitaspordia"));

                // ESPECIALIDAD
                if (rs.getObject("codigo_especialidad") != null) {
                    Especialidad especialidad = new Especialidad();
                    especialidad.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                    especialidad.setNombre(rs.getString("nombre_especialidad"));
                    medico.setEspecialidad(especialidad);
                }

                consulta.setMedico(medico);


                // ENFERMEDADES DIAGNOSTICADAS
                ArrayList<Enfermedad> enfermedades = obtenerEnfermedadesDeConsulta(conn, consulta.getCodigoConsulta());
                consulta.setEnfermedadesDiag(enfermedades);


                // AGREGAR CONSULTA
                lista.add(consulta);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todas las consultas completas: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }


    /**
     * PROCESO: Obtiene la lista de consultas atendidas por un médico específico filtrando por su número de cédula.
     * * ENTRADAS:
     * - cedulaDoctor: Cédula de identidad del médico.
     * * SALIDA: ArrayList de objetos Consulta asociados a dicho profesional.
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos mediante ConexionDB.getConexion().
     * 2. Prepara la sentencia SQL uniendo las tablas consulta, medico, cliente y persona.
     * 3. Setea la cédula del doctor con stmt.setString(1, cedulaDoctor).
     * 4. Mapea el ResultSet para incluir el código de la consulta, los datos básicos del cliente (nombre y apellido), fecha, síntomas y diagnóstico.
     */

    public ArrayList<Consulta> getConsultasPorDoctor(String cedulaDoctor) {
        ArrayList<Consulta> lista = new ArrayList<>();

        String sql = "select c.codigo_cons, p_cli.nombre as nombre_cliente, p_cli.apellido as apellido_cliente, " +
                "c.fechaconsulta, c.sintomas, c.diagnostico " +
                "from consulta c " +
                "inner join medico m on c.codigo_medico = m.codigo_persona " +
                "inner join persona p_med on m.codigo_persona = p_med.codigo_persona " +
                "inner join cliente cl on c.codigo_cliente = cl.codigo_persona " +
                "inner join persona p_cli on cl.codigo_persona = p_cli.codigo_persona " +
                "where p_med.cedula = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaDoctor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Consulta consulta = new Consulta();
                    consulta.setCodigoConsulta(rs.getInt("codigo_cons"));

                    Cliente cliente = new Cliente();
                    cliente.setNombre(rs.getString("nombre_cliente"));
                    cliente.setApellido(rs.getString("apellido_cliente"));
                    consulta.setCliente(cliente);

                    if (rs.getTimestamp("fechaconsulta") != null) {
                        consulta.setFechaConsulta(rs.getTimestamp("fechaconsulta").toLocalDateTime().toLocalDate());
                    }

                    consulta.setSintomas(rs.getString("sintomas"));
                    consulta.setDiagnostico(rs.getString("diagnostico"));

                    lista.add(consulta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * PROCESO: Recupera de forma estática la lista de consultas pertenecientes a un cliente dado su número de cédula, incluyendo los datos del médico tratante y las enfermedades diagnosticadas en cada evento.
     * * ENTRADAS:
     * - cedulaCliente: Cédula de identidad del paciente/cliente.
     * * SALIDA: ArrayList de objetos Consulta pertenecientes al cliente.
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos a través de ConexionDB.getConexion().
     * 2. Prepara el SQL que une las tablas consulta, cliente, medico y persona.
     * 3. Asigna el parámetro con stmt.setString(1, cedulaCliente).
     * 4. Para cada consulta recuperada, abre una segunda conexión (connEnfermedad) e invoca a obtenerEnfermedadesDeConsulta() para poblar sus diagnósticos.
     * 5. Asegura el cierre manual de connEnfermedad en el bloque finally.
     */

    public static ArrayList<Consulta> getConsultasPorCliente(String cedulaCliente) {
        ArrayList<Consulta> lista = new ArrayList<>();
        String sql = "select consulta.codigo_cons, consulta.fechaconsulta, consulta.sintomas, consulta.diagnostico, " +
                "persona_medico.nombre as nombre_medico, persona_medico.apellido as apellido_medico " +
                "from consulta " +
                "inner join cliente on consulta.codigo_cliente = cliente.codigo_persona " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "inner join medico on consulta.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_medico on medico.codigo_persona = persona_medico.codigo_persona " +
                "where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                Connection connEnfermedad = ConexionDB.getConexion();
                try {
                    while (rs.next()) {
                        Consulta consulta = new Consulta();
                        consulta.setCodigoConsulta(rs.getInt("codigo_cons"));

                        if (rs.getTimestamp("fechaconsulta") != null) {
                            consulta.setFechaConsulta(rs.getTimestamp("fechaconsulta").toLocalDateTime().toLocalDate());
                        }

                        consulta.setSintomas(rs.getString("sintomas"));
                        consulta.setDiagnostico(rs.getString("diagnostico"));

                        Medico medico = new Medico();
                        medico.setNombre(rs.getString("nombre_medico"));
                        medico.setApellido(rs.getString("apellido_medico"));
                        consulta.setMedico(medico);

                        ArrayList<Enfermedad> enfermedades = new ConsultaService().obtenerEnfermedadesDeConsulta(connEnfermedad, consulta.getCodigoConsulta());
                        consulta.setEnfermedadesDiag(enfermedades);

                        lista.add(consulta);
                    }
                } finally {
                    if (connEnfermedad != null) connEnfermedad.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * PROCESO: Método auxiliar privado que obtiene la lista de enfermedades vinculadas a una consulta médica dada utilizando una conexión activa.
     * * ENTRADAS:
     * - conn: Objeto Connection abierto previamente.
     * - codigoConsulta: Identificador único de la consulta.
     * * SALIDA: ArrayList de objetos Enfermedad asociados a dicha consulta.
     * * FLUJO DE LLAMADAS:
     * 1. Prepara la sentencia SQL uniendo 'enfermedad_consulta' y 'enfermedad' mediante el parámetro 'codigo_cons'.
     * 2. Asigna la clave primaria de la consulta mediante stmt.setInt(1, codigoConsulta).
     * 3. Recorre el ResultSet para reconstruir cada entidad Enfermedad (código, activo, nombre, vigilancia, descripción).
     * 4. Retorna la lista poblada.
     */

    private ArrayList<Enfermedad> obtenerEnfermedadesDeConsulta(Connection conn, int codigoConsulta) {

        ArrayList<Enfermedad> enfermedades = new ArrayList<>();

        String sql = "select enfermedad.codigo_enfermedad, enfermedad.activo, enfermedad.nombre, enfermedad.vigilancia, enfermedad.descripcion " +
                "from enfermedad_consulta " +
                "inner join enfermedad on enfermedad_consulta.codigo_enfermedad = enfermedad.codigo_enfermedad " +
                "where enfermedad_consulta.codigo_cons = ? " +
                "order by enfermedad.nombre";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoConsulta);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Enfermedad enfermedad = new Enfermedad();
                    enfermedad.setCodigoEnfermedad(rs.getInt("codigo_enfermedad"));
                    enfermedad.setActivo(rs.getBoolean("activo"));
                    enfermedad.setNombre(rs.getString("nombre"));
                    enfermedad.setVigilancia(rs.getBoolean("vigilancia"));
                    enfermedad.setDescripcion(rs.getString("descripcion"));
                    enfermedades.add(enfermedad);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener enfermedades de la consulta " + codigoConsulta + ": " + e.getMessage());
            e.printStackTrace();
        }

        return enfermedades;
    }
}