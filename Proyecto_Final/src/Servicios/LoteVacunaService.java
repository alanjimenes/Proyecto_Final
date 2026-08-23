package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LoteVacunaService {

    public boolean registrarLote(LoteVacuna lote) {
        String sql = "insert into lote_vacuna (codigo_vacuna, no_lote, fechaVencimiento, cantidad) values (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "update lote_vacuna set codigo_vacuna = ?, no_lote = ?, fechaVencimiento = ?, cantidad = ? where codigo_lote = ?";
        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "delete from lote_vacuna where codigo_lote = ?";
        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoLote);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LoteVacuna buscarLoteVacuna(int codigoLote) {
        LoteVacuna lote = null;
        String sql = "select l.codigo_lote, l.no_lote, l.fechaVencimiento, l.cantidad, v.codigo_vacuna, v.nombre " + "from lote_vacuna l left join vacuna v on l.codigo_vacuna = v.codigo_vacuna where l.codigo_lote = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoLote);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fechaVencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechaVencimiento").toLocalDate());
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
        String sql = "select l.codigo_lote, l.no_lote, l.fechaVencimiento, l.cantidad, v.codigo_vacuna, v.nombre " + "from lote_vacuna l left join vacuna v on l.codigo_vacuna = v.codigo_vacuna";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fechaVencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechaVencimiento").toLocalDate());
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

    public ArrayList<LoteVacuna> listarLotesPorVacuna(int codigoVacuna) {
        ArrayList<LoteVacuna> lista = new ArrayList<>();
        String sql = "select l.codigo_lote, l.no_lote, l.fechaVencimiento, l.cantidad, v.codigo_vacuna, v.nombre " + "from lote_vacuna l left join vacuna v on l.codigo_vacuna = v.codigo_vacuna where l.codigo_vacuna = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoVacuna);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fechaVencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechaVencimiento").toLocalDate());
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

    // Método optimizado utilizando la vista estratégica de inventario útil
    public ArrayList<LoteVacuna> listarLotesDisponibles() {
        ArrayList<LoteVacuna> lista = new ArrayList<>();
        String sql = "SELECT * FROM vw_inventario_vacunas_disponibles";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));

                if (rs.getDate("fechaVencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechaVencimiento").toLocalDate());
                }

                lote.setCantidad(rs.getInt("cantidad"));

                Vacuna vac = new Vacuna();
                vac.setCodigoVacuna(rs.getInt("codigo_vacuna"));
                vac.setNombre(rs.getString("nombre_vacuna"));
                lote.setVacuna(vac);

                lista.add(lote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}