package dao;

import connectdb.ConnectDB;
import entity.DanhMuc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DanhMucDAO {

    public DanhMucDAO() {
    }

    // Lấy toàn bộ danh mục
    public ArrayList<DanhMuc> getAllDanhMuc() {
        ArrayList<DanhMuc> dsDM = new ArrayList<DanhMuc>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT maDM, tenDM, loiNhuan FROM DanhMuc ORDER BY maDM";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                DanhMuc dm = new DanhMuc(rs.getString("maDM"), rs.getString("tenDM"),
                        rs.getDouble("loiNhuan"));
                dsDM.add(dm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return dsDM;
    }

    // Tìm danh mục theo mã hoặc tên
    public ArrayList<DanhMuc> timDanhMuc(String tuKhoa) {
        ArrayList<DanhMuc> dsDM = new ArrayList<DanhMuc>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT maDM, tenDM, loiNhuan FROM DanhMuc "
                    + "WHERE maDM LIKE ? OR tenDM LIKE ? ORDER BY maDM";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                DanhMuc dm = new DanhMuc(rs.getString("maDM"), rs.getString("tenDM"),
                        rs.getDouble("loiNhuan"));
                dsDM.add(dm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return dsDM;
    }

    public boolean themDanhMuc(DanhMuc dm) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "INSERT INTO DanhMuc (maDM, tenDM, loiNhuan) VALUES (?, ?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dm.getMaDM());
            stmt.setString(2, dm.getTenDM());
            stmt.setDouble(3, dm.getLoiNhuan());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    public boolean suaDanhMuc(DanhMuc dm) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "UPDATE DanhMuc SET tenDM = ?, loiNhuan = ? WHERE maDM = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dm.getTenDM());
            stmt.setDouble(2, dm.getLoiNhuan());
            stmt.setString(3, dm.getMaDM());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    public boolean xoaDanhMuc(String maDM) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "DELETE FROM DanhMuc WHERE maDM = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maDM);
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    // Sinh mã tự động: DM + 4 chữ số, ví dụ DM0001 đã có thì trả về DM0002
    public String phatSinhMa() {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int soLonNhat = 0;
        try {
            String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maDM, 3, 10) AS INT)) FROM DanhMuc WHERE maDM LIKE 'DM%'";
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
        return "DM" + String.format("%03d", soLonNhat + 1);
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
