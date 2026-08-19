package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LoteVacunaService {

    public boolean registrarLote(LoteVacuna lote) {
        String sql = "INSERT INTO lote_vacuna (codigo_vacuna, no_lote, fecha_vencimiento, cantidad) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lote.getVacuna() != null ? lote.getVacuna().getCodigoVacuna() : 0);
            stmt.setString(2, lote.getNoLote());
            stmt.setDate(3, Date.valueOf(lote.getFechaVencimiento()));
            stmt.setInt(4, lote.getCantidad());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editLoteVacuna(LoteVacuna lote) {
        String sql = "UPDATE lote_vacuna SET codigo_vacuna = ?, no_lote = ?, fecha_vencimiento = ?, cantidad = ? WHERE codigo_lote = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lote.getVacuna() != null ? lote.getVacuna().getCodigoVacuna() : 0);
            stmt.setString(2, lote.getNoLote());
            stmt.setDate(3, Date.valueOf(lote.getFechaVencimiento()));
            stmt.setInt(4, lote.getCantidad());
            stmt.setInt(5, lote.getCodigoLote());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarLoteVacuna(int codigoLote) {
        String sql = "DELETE FROM lote_vacuna WHERE codigo_lote = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoLote);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LoteVacuna buscarLoteVacuna(int codigoLote) {
        LoteVacuna lote = null;
        String sql = "SELECT l.codigo_lote, l.no_lote, l.fecha_vencimiento, l.cantidad, v.codigo_vacuna, v.nombre " +
                "FROM lote_vacuna l LEFT JOIN vacuna v ON l.codigo_vacuna = v.codigo_vacuna WHERE l.codigo_lote = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoLote);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fecha_vencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
                }
                lote.setCantidad(rs.getInt("cantidad"));

                Vacuna vac = new Vacuna();
                vac.setCodigoVacuna(rs.getInt("codigo_vacuna"));
                vac.setNombre(rs.getString("nombre"));
                lote.setVacuna(vac);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lote;
    }

    public ArrayList<LoteVacuna> listarLotes() {
        ArrayList<LoteVacuna> lista = new ArrayList<>();
        String sql = "SELECT l.codigo_lote, l.no_lote, l.fecha_vencimiento, l.cantidad, v.codigo_vacuna, v.nombre " +
                "FROM lote_vacuna l LEFT JOIN vacuna v ON l.codigo_vacuna = v.codigo_vacuna";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fecha_vencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
                }
                lote.setCantidad(rs.getInt("cantidad"));

                Vacuna vac = new Vacuna();
                vac.setCodigoVacuna(rs.getInt("codigo_vacuna"));
                vac.setNombre(rs.getString("nombre"));
                lote.setVacuna(vac);

                lista.add(lote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
