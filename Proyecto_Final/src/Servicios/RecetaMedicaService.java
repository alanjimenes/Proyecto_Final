package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class RecetaMedicaService {

    public boolean crearRecetaMedica(RecetaMedica receta) {
        String sql = "INSERT INTO receta_medica (codigo_consulta, codigo_medicamento, frecuencia, duracion, dosis, descripcion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, receta.getConsulta() != null ? receta.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(2, receta.getMedicamento() != null ? receta.getMedicamento().getCodigoMedicamento() : 0);
            stmt.setString(3, receta.getFrecuencia());
            stmt.setString(4, receta.getDuracion());
            stmt.setString(5, receta.getDosis());
            stmt.setString(6, receta.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editRecetaMedica(RecetaMedica receta) {
        String sql = "UPDATE receta_medica SET codigo_consulta = ?, codigo_medicamento = ?, frecuencia = ?, duracion = ?, dosis = ?, descripcion = ? WHERE codigo_rec = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, receta.getConsulta() != null ? receta.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(2, receta.getMedicamento() != null ? receta.getMedicamento().getCodigoMedicamento() : 0);
            stmt.setString(3, receta.getFrecuencia());
            stmt.setString(4, receta.getDuracion());
            stmt.setString(5, receta.getDosis());
            stmt.setString(6, receta.getDescripcion());
            stmt.setInt(7, receta.getCodigoRec());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarRecetaMedica(int codigoRec) {
        String sql = "DELETE FROM receta_medica WHERE codigo_rec = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoRec);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public RecetaMedica buscarRecetaMedica(int codigoRec) {
        RecetaMedica receta = null;
        String sql = "SELECT r.codigo_rec, r.codigo_consulta, r.frecuencia, r.duracion, r.dosis, r.descripcion, " +
                "m.codigo_medicamento, m.nombre, m.concentracion " +
                "FROM receta_medica r " +
                "LEFT JOIN medicamento m ON r.codigo_medicamento = m.codigo_medicamento " +
                "WHERE r.codigo_rec = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoRec);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                receta = new RecetaMedica();
                receta.setCodigoRec(rs.getInt("codigo_rec"));
                receta.setFrecuencia(rs.getString("frecuencia"));
                receta.setDuracion(rs.getString("duracion"));
                receta.setDosis(rs.getString("dosis"));
                receta.setDescripcion(rs.getString("descripcion"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_consulta"));
                receta.setConsulta(c);

                Medicamento m = new Medicamento();
                m.setCodigoMedicamento(rs.getInt("codigo_medicamento"));
                m.setNombre(rs.getString("nombre"));
                m.setConcentracion(rs.getString("concentracion"));
                receta.setMedicamento(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receta;
    }

    public ArrayList<RecetaMedica> getTodasLasRecetas() {
        ArrayList<RecetaMedica> lista = new ArrayList<>();
        String sql = "SELECT r.codigo_rec, r.codigo_consulta, r.frecuencia, r.duracion, r.dosis, r.descripcion, " +
                "m.codigo_medicamento, m.nombre, m.concentracion " +
                "FROM receta_medica r " +
                "LEFT JOIN medicamento m ON r.codigo_medicamento = m.codigo_medicamento";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RecetaMedica receta = new RecetaMedica();
                receta.setCodigoRec(rs.getInt("codigo_rec"));
                receta.setFrecuencia(rs.getString("frecuencia"));
                receta.setDuracion(rs.getString("duracion"));
                receta.setDosis(rs.getString("dosis"));
                receta.setDescripcion(rs.getString("descripcion"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_consulta"));
                receta.setConsulta(c);

                Medicamento m = new Medicamento();
                m.setCodigoMedicamento(rs.getInt("codigo_medicamento"));
                m.setNombre(rs.getString("nombre"));
                m.setConcentracion(rs.getString("concentracion"));
                receta.setMedicamento(m);

                lista.add(receta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
