package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import logico.Cliente;
import logico.Vacuna;
import Utils.ConexionDB;

public class VacunaService {

    public boolean agregarVacuna(Vacuna vac) {
        String sql = "insert into vacuna (nombre, descripcion) values " +
                "(?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vac.getNombre());
            stmt.setString(2, vac.getDescripcion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Vacuna> listarVacunas() {
        ArrayList<Vacuna> lista = new ArrayList<>();
        String sql = "select vacuna.codigo_vacuna, vacuna.nombre, vacuna.descripcion " +
                "from vacuna";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Vacuna(
                        String.valueOf(rs.getInt("codigo_vacuna")),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean aplicarVacunaCliente(String cedulaCliente, int codigoVacuna, Timestamp fecha) {
        String sql = "insert into regvacuna (codigo_cliente, codigo_vacuna, fecha, aplicada) values ((" +
                "select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?), ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaCliente);
            stmt.setInt(2, codigoVacuna);
            stmt.setTimestamp(3, fecha);
            stmt.setBoolean(4, true);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HashMap<String, Integer> getFrecuenciaVacunas() {
        HashMap<String, Integer> mapa = new HashMap<>();
        String sql = "select vacuna.nombre, count(regvacuna.codigo_reg) AS total " +
                "from vacuna " +
                "inner join regvacuna on vacuna.codigo_vacuna = regvacuna.codigo_vacuna " +
                "group by vacuna.nombre";

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

    public ArrayList<Cliente> getClientesPorVacuna(String nombreVacuna) {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, persona.telefono, " +
                "persona.fechanacimiento, persona.direccion, persona.estado, cliente.numexpediente, cliente.enfermo " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "inner join regvacuna on cliente.codigo_persona = regvacuna.codigo_cliente " +
                "inner join vacuna on regvacuna.codigo_vacuna = vacuna.codigo_vacuna " +
                "where vacuna.nombre = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreVacuna);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getDate("fechanacimiento").toLocalDate(),
                        rs.getString("direccion"),
                        rs.getBoolean("estado"),
                        rs.getString("numexpediente"),
                        null,
                        rs.getBoolean("enfermo"),
                        new ArrayList<>(),
                        "N/A"
                );
                lista.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}