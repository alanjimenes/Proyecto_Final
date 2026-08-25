package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Utils.ConexionDB;
import logico.Cliente;
import logico.Consulta;
import logico.Historial;
import logico.ReporteHistorial;

public class HistorialService {


    /**
     * PROCESO: Obtiene o crea el registro de cabecera del historial clínico de un cliente utilizando su cédula, vinculando además el historial de sus consultas registradas.
     * * ENTRADAS:
     * - cedula: Cédula de identidad del paciente/cliente.
     * * SALIDA: Objeto Historial instanciado con sus consultas asociadas, o null si el cliente no existe.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ClienteService.buscarClientePorCedula() para validar y recuperar al paciente.
     * 2. Conecta a la base de datos vía ConexionDB.getConexion().
     * 3. Ejecuta un SELECT en 'historial' por 'codigo_cliente' para verificar si posee un historial.
     * 4. Si no tiene historial registrado, ejecuta un INSERT con RETURN_GENERATED_KEYS para crearlo de forma implícita.
     * 5. Instancia el objeto Historial y recupera sus consultas llamando a ConsultaService.getConsultasPorCliente(cedula).
     */

    public static Historial obtenerHistorialPorCedula(String cedula) {
        ClienteService clienteService = new ClienteService();
        Cliente cliente = clienteService.buscarClientePorCedula(cedula);

        if (cliente == null) {
            return null;
        }

        Historial historial = null;
        int codigoHistorial = 0;

        Connection con = ConexionDB.getConexion();

        try {
            if (con == null || con.isClosed()) {
                con = ConexionDB.getConexion();
            }

            String sqlSelect = "select codigo_historial " +
                    "from historial " +
                    "where codigo_cliente = ?";
            try (PreparedStatement psSelect = con.prepareStatement(sqlSelect)) {
                psSelect.setInt(1, cliente.getCodigoPersona());

                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        codigoHistorial = rs.getInt("codigo_historial");
                    }
                }
            }

            if (codigoHistorial == 0) {
                String sqlInsert = "insert into historial (codigo_cliente) " +
                        "values (?)";
                try (PreparedStatement psInsert = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                    psInsert.setInt(1, cliente.getCodigoPersona());
                    psInsert.executeUpdate();

                    try (ResultSet rsKeys = psInsert.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            codigoHistorial = rsKeys.getInt(1);
                        }
                    }
                }
            }

            if (codigoHistorial != 0) {
                historial = new Historial(codigoHistorial, cliente);

                ConsultaService consultaService = new ConsultaService();
                ArrayList<Consulta> consultas = consultaService.getConsultasPorCliente(cedula);
                historial.setConsultas(consultas);
            }

        } catch (SQLException e) {
            System.err.println("Error SQL en HistorialService: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return historial;
    }


    /**
     * PROCESO: Genera un reporte detallado e integral del historial médico del paciente reuniendo datos personales, edad calculada, consultas, diagnósticos (concatenados vía string_agg) y registros de vacunación.
     * * ENTRADAS:
     * - cedula: Documento de identidad único del paciente.
     * * SALIDA: List de objetos ReporteHistorial estructurados para presentación o exportación.
     * * FLUJO DE LLAMADAS:
     * 1. Conecta con la base de datos a través de ConexionDB.getConexion().
     * 2. Prepara la consulta compleja uniendo 'persona', 'cliente', 'consulta', 'medico', 'especialidad' e invocando subconsultas con 'string_agg' para agrupar enfermedades y vacunas.
     * 3. Setea la cédula con ps.setString(1, cedula).
     * 4. Mapea cada tupla del ResultSet creando instancias de ReporteHistorial y asignando valores por defecto si los listados de vacunas o diagnósticos son nulos.
     */

    public static List<ReporteHistorial> obtenerReporteHistorialCompleto(String cedula) {
        List<ReporteHistorial> listaReporte = new ArrayList<>();
        Connection con = ConexionDB.getConexion();

        String sql = "select " +
                "persona.cedula, persona.nombre + ' ' + persona.apellido as nombre_paciente, cliente.numexpediente, datediff(year, persona.fechanacimiento, getdate()) as edad, " +
                "consulta.fechaconsulta, persona_medico.nombre + ' ' + persona_medico.apellido as medico, especialidad.nombre as especialidad_medico," +
                "consulta.sintomas, consulta.diagnostico, consulta.addresumen, " +

                "(select string_agg(enfermedad.nombre, ', ') " +
                "from enfermedad_consulta " +
                "inner join enfermedad on enfermedad_consulta.codigo_enfermedad = enfermedad.codigo_enfermedad " +
                "where enfermedad_consulta.codigo_cons = consulta.codigo_cons) as enfermedades_diagnosticadas," +

                "(select string_agg(vacuna.nombre, ' | ') " +
                "from regvacuna " +
                "inner join lote_vacuna on regvacuna.codigo_lote = lote_vacuna.codigo_lote " +
                "inner join vacuna on lote_vacuna.codigo_vacuna = vacuna.codigo_vacuna " +
                "where regvacuna.codigo_cliente = cliente.codigo_persona " + "    ) as registro_vacunacion " +

                "from persona " +
                "inner join cliente on persona.codigo_persona = cliente.codigo_persona " +
                "left join consulta on cliente.codigo_persona = consulta.codigo_cliente " +
                "left join medico on consulta.codigo_medico = medico.codigo_persona " +
                "left join persona persona_medico on medico.codigo_persona = persona_medico.codigo_persona " +
                "left join especialidad on medico.codigo_especialidad = especialidad.codigo_especialidad " +
                "where persona.cedula = ? " +
                "order by consulta.fechaconsulta desc;";

        try {
            if (con == null || con.isClosed()) {
                con = ConexionDB.getConexion();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, cedula);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ReporteHistorial fila = new ReporteHistorial();

                        fila.cedula = rs.getString("cedula");
                        fila.nombrePaciente = rs.getString("nombre_paciente");
                        fila.numExpediente = rs.getString("numexpediente");
                        fila.edad = rs.getInt("edad");
                        fila.fechaConsulta = rs.getTimestamp("fechaconsulta");
                        fila.medicoTratante = rs.getString("medico");
                        fila.especialidadMedico = rs.getString("especialidad_medico");
                        fila.sintomas = rs.getString("sintomas");
                        fila.diagnostico = rs.getString("diagnostico");
                        fila.addResumen = rs.getBoolean("addresumen");

                        String enf = rs.getString("enfermedades_diagnosticadas");
                        fila.enfermedadesDiagnosticadas = (enf != null) ? enf : "Ninguna registrada";

                        String vac = rs.getString("registro_vacunacion");
                        fila.registroVacunacion = (vac != null) ? vac : "Sin vacunas registradas";

                        listaReporte.add(fila);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta compleja de historial:");
            e.printStackTrace();
        }

        return listaReporte;
    }
}
