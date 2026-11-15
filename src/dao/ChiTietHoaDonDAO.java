package dao;

import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChiTietHoaDonDAO {

    ArrayList<ChiTietHoaDon> listCTHD;
    Connection con = ConnectDB.getConnection();
    public ChiTietHoaDonDAO() {
        listCTHD = new ArrayList<ChiTietHoaDon>();
    }

    public ArrayList<ChiTietHoaDon> getDSCTHDByID(String maHD) {
        ArrayList<ChiTietHoaDon> list = new ArrayList<ChiTietHoaDon>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ChiTietHoaDon WHERE maHD = ?");
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("maSP");
                int soLuong = rs.getInt("soLuong");
                SanPham sanPham = new SanPhamDAO().findByID(maSP);
                if (sanPham == null) {
                    throw new java.sql.SQLException("Không tìm thấy sản phẩm " + maSP);
                }
                ChiTietHoaDon ct = new ChiTietHoaDon(new HoaDon(maHD), sanPham, soLuong);
                list.add(ct);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertCTHH(ChiTietHoaDon ct){
        // if (ct == null || ct.getHoaDon() == null || ct.getHoaDon().getMaHD() == null
        //         || ct.getHoaDon().getMaHD().trim().isEmpty()
        //         || ct.getSanPham() == null || ct.getSanPham().getMaSP() == null
        //         || ct.getSanPham().getMaSP().trim().isEmpty()) {
        //     return false;
        // }
        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong) VALUES (?, ?, ?)");
            ps.setString(1, ct.getHoaDon().getMaHD());
            ps.setString(2, ct.getSanPham().getMaSP());
            ps.setInt(3, ct.getSoLuong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCTHD(ChiTietHoaDon ct){
        try {
            PreparedStatement ps = con.prepareStatement("update ChiTietHoaDon set soLuong = ? where maHD = ? and maSP = ?");
            ps.setInt(1, ct.getSoLuong());
            ps.setString(2, ct.getHoaDon().getMaHD());
            ps.setString(3, ct.getSanPham().getMaSP());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCTHD(String mHD,String maSP)
    {
        try {
            PreparedStatement ps = con.prepareStatement("delete from ChiTietHoaDon where maHD = ? and maSP = ?");
            ps.setString(1, mHD);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    } 
    public boolean deleteAllByMaHD(String maHD) {
    String sql = "DELETE FROM ChiTietHoaDon WHERE maHD = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, maHD);

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
} 
}
