package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LoteVacunaService {

    public boolean registrarLote(LoteVacuna lote) {
        String sql = "{call sp_crear_lote_vacuna(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

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
        String sql = "{call sp_editar_lote_vacuna(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, lote.getCodigoLote());
            stmt.setInt(2, lote.getVacuna() != null ? lote.getVacuna().getCodigoVacuna() : 0);
            stmt.setString(3, lote.getNoLote());
            stmt.setDate(4, Date.valueOf(lote.getFechaVencimiento()));
            stmt.setInt(5, lote.getCantidad());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarLoteVacuna(int codigoLote) {
        String sql = "{call sp_eliminar_lote_vacuna(?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoLote);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LoteVacuna buscarLoteVacuna(int codigoLote) {
        LoteVacuna lote = null;
        String sql = "select lote_vacuna.codigo_lote, lote_vacuna.no_lote, lote_vacuna.fechavencimiento, lote_vacuna.cantidad, vacuna.codigo_vacuna, vacuna.nombre " +
                "from lote_vacuna " +
                "left join vacuna on lote_vacuna.codigo_vacuna = vacuna.codigo_vacuna " +
                "where lote_vacuna.codigo_lote = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoLote);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fechavencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechavencimiento").toLocalDate());
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
        String sql = "select lote_vacuna.codigo_lote, lote_vacuna.no_lote, lote_vacuna.fechavencimiento, lote_vacuna.cantidad, vacuna.codigo_vacuna, vacuna.nombre " +
                "from lote_vacuna " +
                "left join vacuna on lote_vacuna.codigo_vacuna = vacuna.codigo_vacuna";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fechavencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechavencimiento").toLocalDate());
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
        String sql = "select lote_vacuna.codigo_lote, lote_vacuna.no_lote, lote_vacuna.fechavencimiento, lote_vacuna.cantidad, vacuna.codigo_vacuna, vacuna.nombre " +
                "from lote_vacuna " +
                "left join vacuna on lote_vacuna.codigo_vacuna = vacuna.codigo_vacuna " +
                "where lote_vacuna.codigo_vacuna = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoVacuna);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));
                if (rs.getDate("fechavencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechavencimiento").toLocalDate());
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

    public ArrayList<LoteVacuna> listarLotesDisponibles() {
        ArrayList<LoteVacuna> lista = new ArrayList<>();
        String sql = "select vw_inventario_vacunas_disponibles.* from vw_inventario_vacunas_disponibles";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoteVacuna lote = new LoteVacuna();
                lote.setCodigoLote(rs.getInt("codigo_lote"));
                lote.setNoLote(rs.getString("no_lote"));

                if (rs.getDate("fechavencimiento") != null) {
                    lote.setFechaVencimiento(rs.getDate("fechavencimiento").toLocalDate());
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

    @Override
    public String toString() {
        return super.toString();
    }
}