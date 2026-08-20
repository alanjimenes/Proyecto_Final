package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.util.ArrayList;

public class MedicamentoService {

    public boolean crearMedicamento(Medicamento medicamento) {
        String sql = "insert into medicamento (nombre, concentracion, descripcion) values (?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicamento.getNombre());
            stmt.setString(2, medicamento.getConcentracion());
            stmt.setString(3, medicamento.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editMedicamento(Medicamento medicamento) {
        String sql = "update medicamento set medicamento.nombre = ?, medicamento.concentracion = ?, medicamento.descripcion = ? where medicamento.codigo_medicamento = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicamento.getNombre());
            stmt.setString(2, medicamento.getConcentracion());
            stmt.setString(3, medicamento.getDescripcion());
            stmt.setInt(4, medicamento.getCodigoMedicamento());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarMedicamento(int codigoMedicamento) {
        String sql = "delete from medicamento where medicamento.codigo_medicamento = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoMedicamento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Medicamento buscarMedicamento(int codigoMedicamento) {
        Medicamento med = null;
        String sql = "select medicamento.codigo_medicamento, medicamento.nombre, medicamento.concentracion, medicamento.descripcion from medicamento where medicamento.codigo_medicamento = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoMedicamento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                med = new Medicamento();
                med.setCodigoMedicamento(rs.getInt("codigo_medicamento"));
                med.setNombre(rs.getString("nombre"));
                med.setConcentracion(rs.getString("concentracion"));
                med.setDescripcion(rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return med;
    }

    public ArrayList<Medicamento> listarMedicamentos() {
        ArrayList<Medicamento> lista = new ArrayList<>();
        String sql = "select medicamento.codigo_medicamento, medicamento.nombre, medicamento.concentracion, medicamento.descripcion from medicamento";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medicamento med = new Medicamento();
                med.setCodigoMedicamento(rs.getInt("codigo_medicamento"));
                med.setNombre(rs.getString("nombre"));
                med.setConcentracion(rs.getString("concentracion"));
                med.setDescripcion(rs.getString("descripcion"));

                lista.add(med);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}