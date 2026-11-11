package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.DanhMuc;

public class DanhMucDAO {

    public DanhMucDAO() {
    }

    public ArrayList<DanhMuc> getAllDanhMuc() {
        ArrayList<DanhMuc> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT maDM, tenDM FROM DanhMuc ORDER BY maDM";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new DanhMuc(rs.getString("maDM"), rs.getString("tenDM")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<DanhMuc> timDanhMuc(String tuKhoa) {
        ArrayList<DanhMuc> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT maDM, tenDM FROM DanhMuc WHERE maDM LIKE ? OR tenDM LIKE ? ORDER BY maDM";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, "%" + tuKhoa + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new DanhMuc(rs.getString("maDM"), rs.getString("tenDM")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themDanhMuc(DanhMuc dm) {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO DanhMuc (maDM, tenDM) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dm.getMaDM());
            ps.setString(2, dm.getTenDM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaDanhMuc(DanhMuc dm) {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE DanhMuc SET tenDM = ? WHERE maDM = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dm.getTenDM());
            ps.setString(2, dm.getMaDM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaDanhMuc(String maDM) {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM DanhMuc WHERE maDM = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maDM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
