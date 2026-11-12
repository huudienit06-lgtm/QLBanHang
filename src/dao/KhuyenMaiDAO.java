package dao;

import connectdb.ConnectDB;
import entity.KhuyenMai;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KhuyenMaiDAO {

    public KhuyenMaiDAO() {
    }

    private KhuyenMai docKhuyenMai(ResultSet rs) throws SQLException {
        Date bd = rs.getDate("ngayBatDau");
        Date kt = rs.getDate("ngayKetThuc");
        return new KhuyenMai(
                rs.getString("maKM"),
                rs.getString("tenKM"),
                rs.getDouble("tiLeGiamGia"),
                rs.getString("donViTinh"),
                bd == null ? null : bd.toLocalDate(),
                kt == null ? null : kt.toLocalDate(),
                rs.getString("trangThai"));
    }

    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM KhuyenMai ORDER BY maKM";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(docKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<KhuyenMai> timKhuyenMai(String tuKhoa) {
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM KhuyenMai WHERE maKM LIKE ? OR tenKM LIKE ? ORDER BY maKM";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, "%" + tuKhoa + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(docKhuyenMai(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO KhuyenMai (maKM, tenKM, tiLeGiamGia, loaiKM, ngayBatDau, ngayKetThuc, trangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setDouble(3, km.getTiLeGiamGia());
            ps.setString(4, km.getDonViTinh());
            ps.setDate(5, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(6, Date.valueOf(km.getNgayKetThuc()));
            ps.setString(7, km.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaKhuyenMai(KhuyenMai km) {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE KhuyenMai SET tenKM = ?, tiLeGiamGia = ?, loaiKM = ?, ngayBatDau = ?, "
                + "ngayKetThuc = ?, trangThai = ? WHERE maKM = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setDouble(2, km.getTiLeGiamGia());
            ps.setString(3, km.getDonViTinh());
            ps.setDate(4, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(5, Date.valueOf(km.getNgayKetThuc()));
            ps.setString(6, km.getTrangThai());
            ps.setString(7, km.getMaKM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaKhuyenMai(String maKM) {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
