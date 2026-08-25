package Servicios;

import java.sql.*;
import java.util.ArrayList;

import logico.*;
import Utils.ConexionDB;


public class ClienteService {


    /**
     * PROCESO: Registra un nuevo paciente/cliente insertando simultáneamente en la jerarquía de tablas Persona y Cliente.
     * * ENTRADAS:
     * - cli: Objeto Cliente con la información demográfica, expediente, estado de salud y antecedentes.
     * * SALIDA: Valor booleano (true si la transacción en SQL Server fue exitosa, false en caso de error).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión activa.
     * 2. Llama a conn.prepareCall() invocando el procedimiento almacenado "{call sp_crear_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}".
     * 3. Setea los parámetros correspondientes mediante los métodos getters del cliente (incluyendo conversión de LocalDate a Date SQL).
     * 4. Ejecuta stmt.executeUpdate() para persisitr al nuevo cliente.
     */

    public boolean registrarNuevoCliente(Cliente cli) {
        String sql = "{call sp_crear_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(cli.getFechaNacimiento()));
            stmt.setString(2, cli.getNombre());
            stmt.setString(3, cli.getApellido());
            stmt.setString(4, cli.getCedula());
            stmt.setString(5, cli.getTelefono());
            stmt.setBoolean(6, cli.getEstado());
            stmt.setString(7, cli.getDireccion());
            stmt.setString(8, cli.getGenero());
            stmt.setString(9, cli.getNumExpediente());
            stmt.setBoolean(10, cli.isEnfermo());
            stmt.setString(11, cli.getAntecedentes());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Modifica la información personal, de contacto o clínica de un cliente registrado.
     * * ENTRADAS:
     * - cli: Objeto Cliente con los campos actualizados.
     * * SALIDA: Valor booleano (true si la modificación en la base de datos se ejecutó con éxito).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para abrir el canal de persistencia.
     * 2. Llama a conn.prepareCall() invocando "{call sp_editar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}".
     * 3. Pasa los parámetros actualizados al CallableStatement.
     * 4. Llama a stmt.executeUpdate() para actualizar las tuplas correspondientes.
     */

    public boolean actualizarCliente(Cliente cli) {
        String sql = "{call sp_editar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(cli.getFechaNacimiento()));
            stmt.setString(2, cli.getNombre());
            stmt.setString(3, cli.getApellido());
            stmt.setString(4, cli.getTelefono());
            stmt.setString(5, cli.getDireccion());
            stmt.setBoolean(6, cli.getEstado());
            stmt.setString(7, cli.getGenero());
            stmt.setString(8, cli.getCedula());
            stmt.setBoolean(9, cli.isEnfermo());
            stmt.setString(10, cli.getNumExpediente());
            stmt.setString(11, cli.getAntecedentes());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * PROCESO: Inactiva o elimina la ficha clínica de un paciente identificándolo por su cédula.
     * * ENTRADAS:
     * - cedula: Cadena de texto con la cédula de la persona/cliente.
     * * SALIDA: Valor booleano (true si la desactivación fue efectuada con éxito).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectarse con la base de datos.
     * 2. Llama a conn.prepareCall() con el procedimiento "{call sp_eliminar_cliente(?)}".
     * 3. Asigna la cédula con stmt.setString(1, cedula).
     * 4. Ejecuta stmt.executeUpdate() para procesar la baja.
     */
    public boolean desactivarCliente(String cedula) {
        String sql = "{call sp_eliminar_cliente(?)}";

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
     * PROCESO: Busca los datos completos de un cliente utilizando su código de expediente médico.
     * * ENTRADAS:
     * - codigoExpediente: Identificador de expediente del paciente.
     * * SALIDA: Objeto Cliente poblado con sus datos personales e historial instanciado, o null si no existe.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para abrir el enlace a SQL Server.
     * 2. Llama a conn.prepareStatement() uniendo las tablas cliente y persona.
     * 3. Establece el expediente mediante stmt.setString(1, codigoExpediente).
     * 4. Procesa el ResultSet mapeando los campos hacia el nuevo objeto Cliente.
     */

    public Cliente buscarClientePorCodigo(String codigoExpediente) {
        Cliente cliente = null;
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.fechanacimiento, " +
                "persona.direccion, persona.estado, cliente.numexpediente, cliente.enfermo, " +
                "persona.genero, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where cliente.numexpediente = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoExpediente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }



    /**
     * PROCESO: Busca un cliente registrado filtrando por su número de cédula de identidad.
     * * ENTRADAS:
     * - cedula: Cadena de texto con la cédula a consultar.
     * * SALIDA: Objeto Cliente correspondiente, o null si no se encuentra en el registro.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión JDBC.
     * 2. Llama a conn.prepareStatement() ejecutando el JOIN por la columna persona.cedula.
     * 3. Asigna la cédula como parámetro con stmt.setString(1, cedula).
     * 4. Mapea el ResultSet construyendo el objeto Cliente.
     */

    public Cliente buscarClientePorCedula(String cedula) {
        Cliente cliente = null;
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.fechanacimiento, " +
                "persona.direccion, persona.estado, cliente.numexpediente, " +
                "cliente.enfermo, persona.genero, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }



    /**
     * PROCESO: Retorna la lista básica de todos los clientes vinculados con su información personal de la tabla Persona.
     * * ENTRADAS: N/A.
     * * SALIDA: ArrayList de objetos Cliente registrados en la base de datos.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para iniciar el canal de comunicación.
     * 2. Llama a conn.prepareStatement() ejecutando la consulta SELECT relacional.
     * 3. Itera sobre el ResultSet mediante rs.next().
     * 4. Instancia y añade los objetos a la lista dinámica devuelta.
     */

    public ArrayList<Cliente> getClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, " +
                "persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, " +
                "cliente.numexpediente, cliente.enfermo, persona.genero, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

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



    /**
     * PROCESO: Recupera la cabecera del historial clínico perteneciente a un cliente mediante su código único.
     * * ENTRADAS:
     * - codigoCliente: Identificador numérico de la persona/cliente.
     * * SALIDA: Objeto Historial vinculado con su respectiva entidad Cliente, o null si no existe.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para conectarse a la base de datos.
     * 2. Llama a con.prepareStatement() uniendo las tablas historial, cliente y persona.
     * 3. Asigna el código del cliente mediante psHist.setInt(1, codigoCliente).
     * 4. Reconstruye el objeto Historial a partir de la tupla retornada por ResultSet.
     */

    public Historial obtenerHistorialPorCliente(int codigoCliente) {
        Historial historial = null;

        String sqlHistorial = "select historial.codigo_historial, persona.codigo_persona, persona.nombre, " +
                "persona.apellido, persona.cedula, cliente.antecedentes " +
                "from historial " +
                "inner join cliente on historial.codigo_cliente = cliente.codigo_persona " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where historial.codigo_cliente = ?";

        try (Connection con = ConexionDB.getConexion(); PreparedStatement psHist = con.prepareStatement(sqlHistorial)) {

            psHist.setInt(1, codigoCliente);
            ResultSet rsHist = psHist.executeQuery();

            if (rsHist.next()) {
                historial = new Historial();
                historial.setCodigoHistorial(rsHist.getInt("codigo_historial"));

                Cliente cliente = new Cliente();
                cliente.setCodigoPersona(rsHist.getInt("codigo_persona"));
                cliente.setNombre(rsHist.getString("nombre"));
                cliente.setApellido(rsHist.getString("apellido"));
                cliente.setCedula(rsHist.getString("cedula"));
                cliente.setAntecedentes(rsHist.getString("antecedentes"));

                historial.setCliente(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historial;
    }



    /**
     * PROCESO: Método auxiliar privado que obtiene las consultas médicas históricas recibidas por un cliente.
     * * ENTRADAS:
     * - con: Objeto Connection de JDBC que mantiene la transacción activa.
     * - codigoCliente: Identificador numérico del paciente.
     * * SALIDA: ArrayList de objetos Consulta ordenados descendentemente por fecha.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a con.prepareStatement() preparando la consulta relacional entre las tablas consulta, medico y persona.
     * 2. Asigna el identificador mediante ps.setInt(1, codigoCliente).
     * 3. Itera sobre el ResultSet mapeando las consultas y los médicos tratantes correspondientes.
     */

    private ArrayList<Consulta> obtenerConsultasPorCliente(Connection con, int codigoCliente) {
        ArrayList<Consulta> lista = new ArrayList<>();

        String sqlConsultas = "select consulta.codigo_cons, consulta.fecha, consulta.sintomas, consulta.diagnostico, " +
                "medico.codigo_persona, persona.nombre AS nombremedico, persona.apellido AS apellidomedico " +
                "from consulta " +
                "inner join medico on consulta.codigo_medico = medico.codigo_persona " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "where consulta.codigo_cliente = ? " +
                "order by consulta.fecha desc";

        try (PreparedStatement ps = con.prepareStatement(sqlConsultas)) {
            ps.setInt(1, codigoCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Consulta consulta = new Consulta();
                consulta.setCodigoConsulta(rs.getInt("codigo_cons"));

                if (rs.getDate("fecha") != null) {
                    consulta.setFechaConsulta(rs.getDate("fecha").toLocalDate());
                }

                consulta.setSintomas(rs.getString("sintomas"));
                consulta.setDiagnostico(rs.getString("diagnostico"));

                Medico medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_persona"));
                medico.setNombre(rs.getString("nombremedico"));
                medico.setApellido(rs.getString("apellidomedico"));
                consulta.setMedico(medico);

                lista.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    /**
     * PROCESO: Desactiva a un cliente en el sistema ejecutando un procedimiento almacenado por su cédula.
     * * ENTRADAS:
     * - cedula: Cadena de texto que identifica a la persona.
     * * SALIDA: Valor booleano (true si la ejecución en el procedimiento almacenado fue exitosa).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para abrir el enlace JDBC.
     * 2. Llama a conn.prepareCall() invocando el procedimiento "{call sp_desactivar_persona(?)}".
     * 3. Setea la cédula mediante stmt.setString(1, cedula).
     * 4. Llama a stmt.execute() para aplicar la desactivación.
     */

    public boolean desactivarPersonaSp(String cedula) {
        String sql = "{call sp_desactivar_persona(?)}";

        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cedula);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Reactiva el estado activo de una persona/cliente en la base de datos mediante procedimiento almacenado.
     * * ENTRADAS:
     * - cedula: Cadena de texto de la cédula del usuario.
     * * SALIDA: Valor booleano (true si se completó la reactivación en SQL Server).
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para obtener la conexión SQL.
     * 2. Llama a conn.prepareCall() invocando el procedimiento "{call sp_activar_persona(?)}".
     * 3. Asigna el parámetro mediante stmt.setString(1, cedula).
     * 4. Ejecuta stmt.execute() notificando el cambio de estado.
     */

    public boolean activarPersonaSp(String cedula) {
        String sql = "{call sp_activar_persona(?)}";

        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cedula);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * PROCESO: Carga de manera completa todos los clientes del sistema enlazando dinámicamente sus historiales clínicos asociados.
     * * ENTRADAS: N/A.
     * * SALIDA: ArrayList de objetos Cliente estructurados de forma exhaustiva con su objeto Historial inyectado.
     * * FLUJO DE LLAMADAS:
     * 1. Llama a ConexionDB.getConexion() para realizar la consulta SQL principal de clientes.
     * 2. Llama a ps.executeQuery() construyendo la lista inicial de objetos Cliente.
     * 3. En un bucle for-each final, invoca a HistorialService.obtenerHistorialPorCedula(cli.getCedula()) para adjuntar a cada cliente su expediente histórico.
     */

    public ArrayList<Cliente> obtenerTodosLosClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.fechanacimiento, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.estado, persona.direccion, persona.genero, " +
                "cliente.numexpediente, cliente.enfermo, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona";

        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCodigoPersona(rs.getInt("codigo_persona"));

                java.sql.Date fechaSql = rs.getDate("fechanacimiento");
                if (fechaSql != null) {
                    cli.setFechaNacimiento(fechaSql.toLocalDate());
                }

                cli.setNombre(rs.getString("nombre"));
                cli.setApellido(rs.getString("apellido"));
                cli.setCedula(rs.getString("cedula"));
                cli.setTelefono(rs.getString("telefono"));
                cli.setEstado(rs.getBoolean("estado"));
                cli.setDireccion(rs.getString("direccion"));
                cli.setGenero(rs.getString("genero"));
                cli.setNumExpediente(rs.getString("numexpediente"));
                cli.setEnfermo(rs.getBoolean("enfermo"));
                cli.setAntecedentes(rs.getString("antecedentes"));

                lista.add(cli);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de clientes: " + e.getMessage());
            e.printStackTrace();
        }

        for (Cliente cli : lista) {
            Historial h = HistorialService.obtenerHistorialPorCedula(cli.getCedula());
            if (h != null) {
                cli.setHistorial(h);
            }
        }

        return lista;
    }
}