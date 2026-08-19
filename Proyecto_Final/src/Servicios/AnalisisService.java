package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AnalisisService {

    public boolean crearAnalisis(Analisis analisis) {
        String sql = "insert into analisis (codigo_consulta, codigo_tipo, fecha_orden, fecha_resultado, estado, resultado) values (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, analisis.getConsulta() != null ? analisis.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(2, analisis.getTipo() != null ? analisis.getTipo().getCodigoTipo() : 0);
            stmt.setTimestamp(3, analisis.getFechaOrden() != null ? Timestamp.valueOf(analisis.getFechaOrden()) : null);
            stmt.setTimestamp(4, analisis.getFechaResultado() != null ? Timestamp.valueOf(analisis.getFechaResultado()) : null);
            stmt.setString(5, analisis.getEstado());
            stmt.setString(6, analisis.getResultado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editAnalisis(Analisis analisis) {
        String sql = "update analisis set codigo_consulta = ?, codigo_tipo = ?, fecha_orden = ?, fecha_resultado = ?, estado = ?, resultado = ? WHERE codigo_analisis = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, analisis.getConsulta() != null ? analisis.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(2, analisis.getTipo() != null ? analisis.getTipo().getCodigoTipo() : 0);
            stmt.setTimestamp(3, analisis.getFechaOrden() != null ? Timestamp.valueOf(analisis.getFechaOrden()) : null);
            stmt.setTimestamp(4, analisis.getFechaResultado() != null ? Timestamp.valueOf(analisis.getFechaResultado()) : null);
            stmt.setString(5, analisis.getEstado());
            stmt.setString(6, analisis.getResultado());
            stmt.setInt(7, analisis.getCodigoAnalisis());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarAnalisis(int codigoAnalisis) {
        String sql = "delete from analisis where codigo_analisis = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoAnalisis);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Analisis buscarAnalisis(int codigoAnalisis) {
        Analisis analisis = null;
        String sql = "select a.codigo_analisis, a.codigo_consulta, a.fecha_orden, a.fecha_resultado, a.estado, a.resultado, " +
                "t.codigo_tipo, t.nombre as tipo_nombre, t.descripcion as tipo_desc " +
                "from analisis a " +
                "left join tipo_analisis t ON a.codigo_tipo = t.codigo_tipo " +
                "where a.codigo_analisis = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoAnalisis);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                analisis = new Analisis();
                analisis.setCodigoAnalisis(rs.getInt("codigo_analisis"));

                if (rs.getTimestamp("fecha_orden") != null) {
                    analisis.setFechaOrden(rs.getTimestamp("fecha_orden").toLocalDateTime());
                }
                if (rs.getTimestamp("fecha_resultado") != null) {
                    analisis.setFechaResultado(rs.getTimestamp("fecha_resultado").toLocalDateTime());
                }

                analisis.setEstado(rs.getString("estado"));
                analisis.setResultado(rs.getString("resultado"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_consulta"));
                analisis.setConsulta(c);

                TipoAnalisis t = new TipoAnalisis();
                t.setCodigoTipo(rs.getInt("codigo_tipo"));
                t.setNombre(rs.getString("tipo_nombre"));
                t.setDescripcion(rs.getString("tipo_desc"));
                analisis.setTipo(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analisis;
    }

    public ArrayList<Analisis> listarAnalisis() {
        ArrayList<Analisis> lista = new ArrayList<>();
        String sql = "select a.codigo_analisis, a.codigo_consulta, a.fecha_orden, a.fecha_resultado, a.estado, a.resultado, " +
                "t.codigo_tipo, t.nombre AS tipo_nombre, t.descripcion AS tipo_desc " +
                "from analisis a " +
                "left join tipo_analisis t on a.codigo_tipo = t.codigo_tipo";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Analisis analisis = new Analisis();
                analisis.setCodigoAnalisis(rs.getInt("codigo_analisis"));

                if (rs.getTimestamp("fecha_orden") != null) {
                    analisis.setFechaOrden(rs.getTimestamp("fecha_orden").toLocalDateTime());
                }
                if (rs.getTimestamp("fecha_resultado") != null) {
                    analisis.setFechaResultado(rs.getTimestamp("fecha_resultado").toLocalDateTime());
                }

                analisis.setEstado(rs.getString("estado"));
                analisis.setResultado(rs.getString("resultado"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_consulta"));
                analisis.setConsulta(c);

                TipoAnalisis t = new TipoAnalisis();
                t.setCodigoTipo(rs.getInt("codigo_tipo"));
                t.setNombre(rs.getString("tipo_nombre"));
                t.setDescripcion(rs.getString("tipo_desc"));
                analisis.setTipo(t);

                lista.add(analisis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
