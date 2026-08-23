package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import logico.Cliente;
import logico.Historial;
import logico.Vacuna;
import Utils.ConexionDB;

public class VacunaService {

    public boolean agregarVacuna(Vacuna vac) {
        String sql = "insert into vacuna (nombre, descripcion) values (?, ?)";
        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "select vacuna.codigo_vacuna, vacuna.nombre, vacuna.descripcion from vacuna";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vacuna vac = new Vacuna();
                vac.setCodigoVacuna(rs.getInt("codigo_vacuna"));
                vac.setNombre(rs.getString("nombre"));
                vac.setDescripcion(rs.getString("descripcion"));
                vac.setActivo(true);
                lista.add(vac);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizarVacuna(Vacuna vac) {
        String sql = "update vacuna set vacuna.nombre = ?, vacuna.descripcion = ? where vacuna.codigo_vacuna = ?";
        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vac.getNombre());
            stmt.setString(2, vac.getDescripcion());
            stmt.setInt(3, vac.getCodigoVacuna());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarVacuna(int codigoVacuna) {
        String sql = "delete from vacuna where vacuna.codigo_vacuna = ?";
        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoVacuna);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean aplicarVacunaCliente(String cedulaCliente, int codigoLote, int codigoPersonalLogueado, Timestamp fecha) {
        String sql = "insert into regvacuna (codigo_cliente, codigo_lote, codigo_enfermera, fecha, aplicada) " + "values ((select persona.codigo_persona from persona where persona.cedula = ?), ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaCliente);
            stmt.setInt(2, codigoLote);
            stmt.setInt(3, codigoPersonalLogueado);
            stmt.setTimestamp(4, fecha);
            stmt.setBoolean(5, true);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HashMap<String, Integer> getFrecuenciaVacunas() {
        HashMap<String, Integer> mapa = new HashMap<>();
        String sql = "select vacuna.nombre, count(regvacuna.codigo_reg) AS total from vacuna inner join regvacuna on vacuna.codigo_vacuna = regvacuna.codigo_vacuna group by vacuna.nombre";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

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
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, persona.genero, cliente.numexpediente, cliente.enfermo, cliente.antecedentes from cliente inner join persona on cliente.codigo_persona = persona.codigo_persona inner join regvacuna on cliente.codigo_persona = regvacuna.codigo_cliente inner join vacuna on regvacuna.codigo_vacuna = vacuna.codigo_vacuna where vacuna.nombre = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreVacuna);
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