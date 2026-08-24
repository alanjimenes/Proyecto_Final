package Servicios;

import logico.Cliente;
import logico.Enfermedad;
import logico.Historial;
import Utils.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class EnfermedadService {

    public boolean agregarEnfermedad(Enfermedad enf) {
        String sql = "insert into enfermedad (nombre, descripcion, vigilancia) values (?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, enf.getNombre());
            stmt.setString(2, enf.getDescripcion());
            stmt.setBoolean(3, enf.isVigilancia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public ArrayList<String> getTop5Enfermedades() {
        ArrayList<String> top5 = new ArrayList<>();
        String sql = "select top 5 enfermedad.nombre, count(enfermedad_consulta.codigo_enfermedad) AS total " +
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