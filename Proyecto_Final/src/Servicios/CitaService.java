package Servicios;

import Utils.ConexionDB;
import logico.Cita;
import logico.Cliente;
import logico.Medico;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class CitaService {


    /**
     * PROCESO: Agenda una nueva cita médica asociando la cédula del médico y la cédula del paciente en la base de datos.
     * * ENTRADAS:
     * - cita: Objeto de tipo Cita que almacena la fecha/hora, estado y motivo del encuentro clínico.
     * - cedulaMedico: Cadena de texto con la cédula del médico asignado.
     * - cedulaCliente: Cadena de texto con la cédula del cliente/paciente que solicita la cita.
     * * SALIDA: Valor booleano (true si la cita fue creada exitosamente, false en caso de fallo SQL).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para abrir el enlace a SQL Server.
     * 2. Llama a conn.prepareCall() para preparar la llamada al procedimiento "{call sp_crear_cita(?, ?, ?, ?, ?)}".
     * 3. Asigna los parámetros de entrada mediante stmt.setString() y stmt.setTimestamp().
     * 4. Llama a stmt.executeUpdate() para confirmar el registro en la base de datos.
     */

    public boolean crearCita(Cita cita, String cedulaMedico, String cedulaCliente) {
        String sql = "{call sp_crear_cita(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedulaMedico);
            stmt.setString(2, cedulaCliente);
            stmt.setTimestamp(3, Timestamp.valueOf(cita.getFechaCita()));
            stmt.setString(4, cita.getEstado());
            stmt.setString(5, cita.getMotivo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Reprograma la fecha y hora de una cita existente o reasigna el médico tratante.
     * * ENTRADAS:
     * - codigoCita: Clave primaria de la cita que se desea modificar.
     * - nuevaFechaHora: Objeto LocalDateTime con el nuevo horario programado.
     * - cedulaMedico: Cédula del médico al que se le reasigna la cita.
     * * SALIDA: Valor booleano (true si la modificación fue exitosa, false en caso contrario).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener el enlace a la base de datos.
     * 2. Llama a conn.prepareCall() para invocar "{call sp_editar_cita(?, ?, ?)}".
     * 3. Establece los nuevos datos mediante stmt.setInt(), stmt.setTimestamp() y stmt.setString().
     * 4. Llama a stmt.executeUpdate() para guardar los cambios en la base de datos.
     */

    public boolean editCita(int codigoCita, LocalDateTime nuevaFechaHora, String cedulaMedico) {
        String sql = "{call sp_editar_cita(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoCita);
            stmt.setTimestamp(2, Timestamp.valueOf(nuevaFechaHora));
            stmt.setString(3, cedulaMedico);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Cancela una cita médica agendada ejecutando un procedimiento almacenado.
     * * ENTRADAS:
     * - codigoCita: Identificador único de la cita a cancelar.
     * * SALIDA: Valor booleano (true si el estado fue cambiado a cancelado, false en caso de error SQL).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectarse a SQL Server.
     * 2. Llama a conn.prepareCall() para preparar la llamada a "{call sp_cancelar_cita(?)}".
     * 3. Llama a stmt.setInt() para pasar la clave primaria como parámetro.
     * 4. Llama a stmt.executeUpdate() para actualizar el estado de la cita.
     */

    public boolean cancelCita(int codigoCita) {
        String sql = "{call sp_cancelar_cita(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoCita);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Contabiliza el número total de citas agendadas para un médico específico en una fecha dada.
     * * ENTRADAS:
     * - codigoMedico: Clave primaria del médico.
     * - fecha: Objeto LocalDate que representa el día a consultar.
     * * SALIDA: Valor entero con el total de citas agendadas ese día.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para establecer la conexión a la base de datos.
     * 2. Llama a conn.prepareStatement() para preparar la consulta de agregación (COUNT).
     * 3. Asigna la clave del médico y castea la fecha utilizando java.sql.Date.valueOf(fecha).
     * 4. Llama a stmt.executeQuery() y recupera la cantidad agregada mediante rs.getInt("total").
     */
    public int contarCitasPorDia(int codigoMedico, LocalDate fecha) {
        int total = 0;
        String sql = "select count(cita.codigo_cita) AS total " +
                "from cita " +
                "where cita.codigo_medico = ? and cast(cita.fechacita AS date) = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoMedico);
            stmt.setDate(2, java.sql.Date.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }



    /**
     * PROCESO: Calcula el total de citas completadas satisfactoriamente durante un mes y año determinados.
     * * ENTRADAS:
     * - mes: Número entero que representa el mes del año (1-12).
     * - anio: Número entero que representa el año de consulta (ej. 2026).
     * * SALIDA: Valor entero con el total de citas en estado 'Completada' acumuladas en dicho período.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para abrir la conexión SQL.
     * 2. Llama a conn.prepareStatement() para preparar la consulta con las funciones month() y year().
     * 3. Llama a stmt.setInt() para pasar el mes y el año requeridos.
     * 4. Llama a stmt.executeQuery() y obtiene el valor procesado mediante rs.getInt("total").
     */

    public int contarCitasPorMes(int mes, int anio) {
        int total = 0;
        String sql = "select count(cita.codigo_cita) AS total " +
                "from cita " +
                "where month(cita.fechacita) = ? and year(cita.fechacita) = ? and cita.estado = 'Completada'";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mes);
            stmt.setInt(2, anio);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }



    /**
     * PROCESO: Busca una cita por su código identificador reconstruyendo las entidades del Cliente y Médico implicados.
     * * ENTRADAS:
     * - codigoCita: Clave primaria de la cita médica.
     * * SALIDA: Objeto Cita completo con sus sub-objetos Cliente y Medico poblados, o null si no existe.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectarse con la base de datos.
     * 2. Llama a conn.prepareStatement() ejecutando los JOINs entre las tablas cita, cliente, medico y persona.
     * 3. Llama a stmt.executeQuery() para procesar la fila de resultados.
     * 4. Instancia las entidades Cliente, Medico y Cita mapeando los campos desde el ResultSet.
     */

    public Cita buscarCita(int codigoCita) {
        Cita cita = null;
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, persona_cli.apellido AS cli_ape, " +
                "cliente.numexpediente, persona_med.cedula AS med_ced, persona_med.nombre AS med_nom, persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona " +
                "where cita.codigo_cita = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoCita);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCedula(rs.getString("cli_ced"));
                cli.setNombre(rs.getString("cli_nom"));
                cli.setApellido(rs.getString("cli_ape"));
                cli.setNumExpediente(rs.getString("numexpediente"));

                Medico med = new Medico();
                med.setCedula(rs.getString("med_ced"));
                med.setNombre(rs.getString("med_nom"));
                med.setApellido(rs.getString("med_ape"));

                cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setCliente(cli);
                cita.setMedico(med);
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cita;
    }



    /**
     * PROCESO: Obtiene el listado completo de citas médicas registradas en la plataforma.
     * * ENTRADAS: N/A.
     * * SALIDA: Un ArrayList de objetos Cita conteniendo la información completa de cada agendamiento.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para establecer la conexión a la base de datos.
     * 2. Llama a conn.prepareStatement() ejecutando la consulta relacional general.
     * 3. Itera sobre el ResultSet mediante un bucle while(rs.next()).
     * 4. Mapea cada tupla a un objeto Cita (con sus objetos Cliente y Medico asociados) y lo añade a lista.add(cita).
     */
    public ArrayList<Cita> getTodasLasCitas() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, persona_cli.apellido AS cli_ape, " +
                "cliente.numexpediente, persona_med.cedula AS med_ced, persona_med.nombre AS med_nom, " +
                "persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCedula(rs.getString("cli_ced"));
                cli.setNombre(rs.getString("cli_nom"));
                cli.setApellido(rs.getString("cli_ape"));
                cli.setNumExpediente(rs.getString("numexpediente"));

                Medico med = new Medico();
                med.setCedula(rs.getString("med_ced"));
                med.setNombre(rs.getString("med_nom"));
                med.setApellido(rs.getString("med_ape"));

                Cita cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setCliente(cli);
                cita.setMedico(med);
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));

                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * PROCESO: Consulta la agenda de citas filtrada por la cédula de un médico específico.
     * * ENTRADAS:
     * - cedulaMedico: Cadena de texto con la cédula del profesional médico.
     * * SALIDA: Un ArrayList con las citas asignadas al médico consultado.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión JDBC.
     * 2. Llama a conn.prepareStatement() enviando la consulta con la cláusula WHERE filtrando por cedula de medico.
     * 3. Asigna la cédula del médico mediante stmt.setString(1, cedulaMedico).
     * 4. Ejecuta stmt.executeQuery() y construye la lista de citas resultantes.
     */
    public ArrayList<Cita> getCitasPorMedico(String cedulaMedico) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, " +
                "persona_cli.apellido AS cli_ape, cliente.numexpediente, persona_med.cedula AS med_ced, " +
                "persona_med.nombre AS med_nom, persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona " +
                "where persona_med.cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaMedico);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCedula(rs.getString("cli_ced"));
                cli.setNombre(rs.getString("cli_nom"));
                cli.setApellido(rs.getString("cli_ape"));
                cli.setNumExpediente(rs.getString("numexpediente"));

                Medico med = new Medico();
                med.setCedula(rs.getString("med_ced"));
                med.setNombre(rs.getString("med_nom"));
                med.setApellido(rs.getString("med_ape"));

                Cita cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setCliente(cli);
                cita.setMedico(med);
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));

                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * PROCESO: Obtiene el historial de citas asociadas a un cliente especificando su número de expediente.
     * * ENTRADAS:
     * - numExpediente: Identificador único del expediente del paciente.
     * * SALIDA: Un ArrayList con las citas agendadas o atendidas de dicho paciente.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para establecer la conexión a SQL Server.
     * 2. Llama a conn.prepareStatement() con la sentencia parametrizada por número de expediente.
     * 3. Establece el expediente mediante stmt.setString(1, numExpediente).
     * 4. Recorre el ResultSet mediante rs.next() para construir y retornar la lista de citas.
     */
    public ArrayList<Cita> getCitasDeCliente(String numExpediente) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, " +
                "persona_cli.apellido AS cli_ape, cliente.numexpediente, persona_med.cedula AS med_ced, " +
                "persona_med.nombre AS med_nom, persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona " +
                "where cliente.numexpediente = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numExpediente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCedula(rs.getString("cli_ced"));
                cli.setNombre(rs.getString("cli_nom"));
                cli.setApellido(rs.getString("cli_ape"));
                cli.setNumExpediente(rs.getString("numexpediente"));

                Medico med = new Medico();
                med.setCedula(rs.getString("med_ced"));
                med.setNombre(rs.getString("med_nom"));
                med.setApellido(rs.getString("med_ape"));

                Cita cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setCliente(cli);
                cita.setMedico(med);
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));
                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }



    /**
     * PROCESO: Filtra las citas agendadas dentro de un intervalo temporal (fecha de inicio y fecha de fin).
     * * ENTRADAS:
     * - desde: Fecha y hora de inicio del rango de búsqueda.
     * - hasta: Fecha y hora límite del rango de búsqueda.
     * * SALIDA: Un ArrayList de objetos Cita que se encuentran comprendidas dentro del rango especificado.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para abrir el canal de comunicación SQL.
     * 2. Llama a conn.prepareStatement() ejecutando el filtro mediante operadores de comparación (>= y <=).
     * 3. Asigna los Timestamp límite mediante stmt.setTimestamp(1, ...) y stmt.setTimestamp(2, ...).
     * 4. Procesa el ResultSet mapeando las tuplas hacia la lista final.
     */

    public ArrayList<Cita> getCitasPorRango(LocalDateTime desde, LocalDateTime hasta) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula as cli_ced, persona_cli.nombre as cli_nom, " +
                "persona_cli.apellido as cli_ape, cliente.numexpediente, persona_med.cedula as med_ced, " +
                "persona_med.nombre as med_nom, persona_med.apellido as med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona " +
                "where cita.fechacita >= ? and cita.fechacita <= ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(desde));
            stmt.setTimestamp(2, Timestamp.valueOf(hasta));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCedula(rs.getString("cli_ced"));
                cli.setNombre(rs.getString("cli_nom"));
                cli.setApellido(rs.getString("cli_ape"));
                cli.setNumExpediente(rs.getString("numexpediente"));

                Medico med = new Medico();
                med.setCedula(rs.getString("med_ced"));
                med.setNombre(rs.getString("med_nom"));
                med.setApellido(rs.getString("med_ape"));

                Cita cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setCliente(cli);
                cita.setMedico(med);
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));
                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}