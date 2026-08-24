package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConsultaService {

    public boolean registrarConsultaCompleta(Consulta con, String cedulaMedico, String cedulaCliente) {
        String sqlConsulta = "insert into consulta (fechaconsulta, sintomas, diagnostico, codigo_medico, codigo_cliente) values (?, ?, ?, (select persona.codigo_persona from persona where persona.cedula = ?), (select persona.codigo_persona from persona where persona.cedula = ?))";

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

    public int iniciarConsulta(Consulta con, int codigoMedico, int codigoCliente) {
        String sql = "insert into consulta (fechaconsulta, sintomas, diagnostico, codigo_medico, codigo_cliente) values (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(con.getFechaConsulta().atStartOfDay()));
            stmt.setString(2, con.getSintomas());
            stmt.setString(3, con.getDiagnostico());
            stmt.setInt(4, codigoMedico);
            stmt.setInt(5, codigoCliente);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public boolean guardarConsulta(int codigoConsulta, String sintomas, String diagnostico, ArrayList<Enfermedad> enfermedades) {
        String sqlUpdate = "update consulta set consulta.sintomas = ?, consulta.diagnostico = ? where consulta.codigo_cons = ?";
        String sqlEnfermedad = "insert into enfermedad_consulta (codigo_enfermedad, codigo_consulta) values (?, ?)";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, sintomas);
                stmtUpdate.setString(2, diagnostico);
                stmtUpdate.setInt(3, codigoConsulta);
                stmtUpdate.executeUpdate();
            }

            if (enfermedades != null && !enfermedades.isEmpty()) {
                try (PreparedStatement stmtEnf = conn.prepareStatement(sqlEnfermedad)) {
                    for (Enfermedad enf : enfermedades) {
                        stmtEnf.setInt(1, enf.getCodigoEnfermedad());
                        stmtEnf.setInt(2, codigoConsulta);
                        stmtEnf.addBatch();
                    }
                    stmtEnf.executeBatch();
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

                // ==========================================
                // DATOS DE LA CONSULTA
                // ==========================================

                consulta.setCodigoConsulta(rs.getInt("codigo_cons"));

                Timestamp fechaConsulta = rs.getTimestamp("fechaconsulta");

                if (fechaConsulta != null) {
                    consulta.setFechaConsulta(fechaConsulta.toLocalDateTime().toLocalDate());
                }

                consulta.setSintomas(rs.getString("sintomas"));

                consulta.setDiagnostico(rs.getString("diagnostico"));

                // ==========================================
                // CLIENTE
                // ==========================================

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

                // ==========================================
                // MEDICO
                // ==========================================

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

                // ==========================================
                // ESPECIALIDAD
                // ==========================================

                if (rs.getObject("codigo_especialidad") != null) {

                    Especialidad especialidad = new Especialidad();

                    especialidad.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));

                    especialidad.setNombre(rs.getString("nombre_especialidad"));

                    medico.setEspecialidad(especialidad);
                }

                consulta.setMedico(medico);

                // ==========================================
                // ENFERMEDADES DIAGNOSTICADAS
                // ==========================================

                ArrayList<Enfermedad> enfermedades = obtenerEnfermedadesDeConsulta(conn, consulta.getCodigoConsulta());
                consulta.setEnfermedadesDiag(enfermedades);

                // ==========================================
                // AGREGAR CONSULTA
                // ==========================================

                lista.add(consulta);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todas las consultas completas: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<Consulta> getConsultasPorDoctor(String cedulaDoctor) {
        ArrayList<Consulta> lista = new ArrayList<>();

        String sql = "select c.codigo_cons, p_cli.nombre as nombre_cliente, p_cli.apellido as apellido_cliente, " + "c.fechaconsulta, c.sintomas, c.diagnostico " + "from consulta c " + "inner join medico m on c.codigo_medico = m.codigo_persona " + "inner join persona p_med on m.codigo_persona = p_med.codigo_persona " + "inner join cliente cl on c.codigo_cliente = cl.codigo_persona " + // <--- Ajustado según tu tabla
                "inner join persona p_cli on cl.codigo_persona = p_cli.codigo_persona " + "where p_med.cedula = ?";

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

    public static ArrayList<Consulta> getConsultasPorCliente(String cedulaCliente) {
        ArrayList<Consulta> lista = new ArrayList<>();
        String sql = "select consulta.codigo_cons, consulta.fechaconsulta, consulta.sintomas, consulta.diagnostico from consulta inner join cliente on consulta.codigo_cliente = cliente.codigo_persona inner join persona on cliente.codigo_persona = persona.codigo_persona where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Consulta consulta = new Consulta();
                    consulta.setCodigoConsulta(rs.getInt("codigo_cons"));
                    consulta.setFechaConsulta(rs.getTimestamp("fechaconsulta").toLocalDateTime().toLocalDate());
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

    private ArrayList<Enfermedad> obtenerEnfermedadesDeConsulta(Connection conn, int codigoConsulta) {

        ArrayList<Enfermedad> enfermedades = new ArrayList<>();

        String sql = "select enfermedad.codigo_enfermedad, enfermedad.activo, enfermedad.nombre, enfermedad.vigilancia, enfermedad.descripcion " +
                "from enfermedad_consulta " +
                "inner join enfermedad e on enfermedad_consulta.codigo_enfermedad = enfermedad.codigo_enfermedad " +
                "where enfermedad_consulta.codigo_cons= ? " +
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