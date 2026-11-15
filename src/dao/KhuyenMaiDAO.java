package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.KhuyenMai;

public class KhuyenMaiDAO {

    public KhuyenMaiDAO() {
    }

    // Đọc một dòng của ResultSet thành đối tượng KhuyenMai
    private KhuyenMai docKhuyenMai(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();
        km.setMaKM(rs.getString("maKM"));
        km.setTenKM(rs.getString("tenKM"));
        km.setTiLeGiamGia(rs.getDouble("tiLeGiamGia"));
        km.setLoaiKM(rs.getString("loaiKM"));
        Date ngayBD = rs.getDate("ngayBatDau");
        if (ngayBD != null) {
            km.setNgayBatDau(ngayBD.toLocalDate());
        }
        Date ngayKT = rs.getDate("ngayKetThuc");
        if (ngayKT != null) {
            km.setNgayKetThuc(ngayKT.toLocalDate());
        }
        km.setTrangThai(rs.getString("trangThai"));
        return km;
    }

    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        ArrayList<KhuyenMai> dsKM = new ArrayList<KhuyenMai>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM KhuyenMai ORDER BY maKM";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dsKM.add(docKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return dsKM;
    }

    public ArrayList<KhuyenMai> timKhuyenMai(String tuKhoa) {
        ArrayList<KhuyenMai> dsKM = new ArrayList<KhuyenMai>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM KhuyenMai WHERE maKM LIKE ? OR tenKM LIKE ? ORDER BY maKM";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                dsKM.add(docKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return dsKM;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "INSERT INTO KhuyenMai (maKM, tenKM, tiLeGiamGia, loaiKM, ngayBatDau, ngayKetThuc, trangThai) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, km.getMaKM());
            stmt.setString(2, km.getTenKM());
            stmt.setDouble(3, km.getTiLeGiamGia());
            stmt.setString(4, km.getLoaiKM());
            stmt.setDate(5, Date.valueOf(km.getNgayBatDau()));
            stmt.setDate(6, Date.valueOf(km.getNgayKetThuc()));
            stmt.setString(7, km.getTrangThai());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    public boolean suaKhuyenMai(KhuyenMai km) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "UPDATE KhuyenMai SET tenKM = ?, tiLeGiamGia = ?, loaiKM = ?, ngayBatDau = ?, "
                    + "ngayKetThuc = ?, trangThai = ? WHERE maKM = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, km.getTenKM());
            stmt.setDouble(2, km.getTiLeGiamGia());
            stmt.setString(3, km.getLoaiKM());
            stmt.setDate(4, Date.valueOf(km.getNgayBatDau()));
            stmt.setDate(5, Date.valueOf(km.getNgayKetThuc()));
            stmt.setString(6, km.getTrangThai());
            stmt.setString(7, km.getMaKM());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    public boolean xoaKhuyenMai(String maKM) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maKM);
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    // Sinh mã tự động: KM + 4 chữ số, ví dụ KM0001 đã có thì trả về KM0002
    public String phatSinhMa() {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int soLonNhat = 0;
        try {
            String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maKM, 3, 10) AS INT)) FROM KhuyenMai WHERE maKM LIKE 'KM%'";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                soLonNhat = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return "KM" + String.format("%04d", soLonNhat + 1);
    }

    private void dongTaiNguyen(PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
