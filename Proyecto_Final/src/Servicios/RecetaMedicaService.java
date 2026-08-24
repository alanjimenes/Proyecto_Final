package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.util.ArrayList;

public class RecetaMedicaService {

    public boolean crearRecetaMedica(RecetaMedica receta) {
        String sql = "{call sp_crear_receta_medica(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

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

    public boolean registrarReceta(Connection conn, RecetaMedica receta, int idConsulta) throws SQLException {
        String sql = "{call sp_crear_receta_medica(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.setInt(2, receta.getMedicamento() != null ? receta.getMedicamento().getCodigoMedicamento() : 0);
            stmt.setString(3, receta.getFrecuencia());
            stmt.setString(4, receta.getDuracion());
            stmt.setString(5, receta.getDosis());
            stmt.setString(6, receta.getDescripcion());

            return stmt.executeUpdate() > 0;
        }
    }

    public RecetaMedica buscarRecetaMedica(int codigoRec) {
        RecetaMedica receta = null;
        String sql = "select receta_medica.codigo_rec, receta_medica.codigo_cons, receta_medica.frecuencia, " +
                "receta_medica.duracion, receta_medica.dosis, receta_medica.descripcion, medicamento.codigo_medicamento, " +
                "medicamento.nombre, medicamento.concentracion " +
                "from receta_medica " +
                "left join medicamento on receta_medica.codigo_medicamento = medicamento.codigo_medicamento " +
                "where receta_medica.codigo_rec = ?";

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
                c.setCodigoConsulta(rs.getInt("codigo_cons"));
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
        String sql = "select receta_medica.codigo_rec, receta_medica.codigo_cons, receta_medica.frecuencia, " +
                "receta_medica.duracion, receta_medica.dosis, receta_medica.descripcion, medicamento.codigo_medicamento, " +
                "medicamento.nombre, medicamento.concentracion " +
                "from receta_medica " +
                "left join medicamento on receta_medica.codigo_medicamento = medicamento.codigo_medicamento";

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
                c.setCodigoConsulta(rs.getInt("codigo_cons"));
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

    public ArrayList<RecetaMedica> getRecetasPorConsulta(int codigoConsulta) {
        ArrayList<RecetaMedica> lista = new ArrayList<>();
        String sql = "select receta_medica.codigo_rec, receta_medica.codigo_cons, receta_medica.frecuencia, " +
                "receta_medica.duracion, receta_medica.dosis, receta_medica.descripcion, medicamento.codigo_medicamento, " +
                "medicamento.nombre, medicamento.concentracion " +
                "from receta_medica " +
                "left join medicamento on receta_medica.codigo_medicamento = medicamento.codigo_medicamento " +
                "where receta_medica.codigo_cons = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoConsulta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RecetaMedica receta = new RecetaMedica();
                    receta.setCodigoRec(rs.getInt("codigo_rec"));
                    receta.setFrecuencia(rs.getString("frecuencia"));
                    receta.setDuracion(rs.getString("duracion"));
                    receta.setDosis(rs.getString("dosis"));
                    receta.setDescripcion(rs.getString("descripcion"));

                    Consulta c = new Consulta();
                    c.setCodigoConsulta(rs.getInt("codigo_cons"));
                    receta.setConsulta(c);

                    Medicamento m = new Medicamento();
                    m.setCodigoMedicamento(rs.getInt("codigo_medicamento"));
                    m.setNombre(rs.getString("nombre"));
                    m.setConcentracion(rs.getString("concentracion"));
                    receta.setMedicamento(m);

                    lista.add(receta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}