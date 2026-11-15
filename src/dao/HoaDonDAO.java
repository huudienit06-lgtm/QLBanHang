package dao;

import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class HoaDonDAO {

    ArrayList<HoaDon> listHD;
    Connection con = ConnectDB.getConnection();

    public HoaDonDAO() {
        listHD = new ArrayList<HoaDon>();
    }

    public ArrayList<HoaDon> getDSHoaDon() {
        ArrayList<HoaDon> listHD = new ArrayList<HoaDon>();
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT maHD, ngayLap, maKH, maNV, maKM, trangThai, phuongThucThanhToan FROM HoaDon");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maHD = rs.getString("maHD");
                LocalDate ngayLap = LocalDate.parse(rs.getString("ngayLap"));
                String maKH = rs.getString("maKH");
                String maNV = rs.getString("maNV");
                String maKM = rs.getString("maKM");
                String trangThai = rs.getString("trangThai");
                String phuongThucThanhToan = rs.getString("phuongThucThanhToan");

                KhachHang kH = new KhachHang(maKH);
                NhanVien nV = new NhanVien(maNV);
                KhuyenMai kM = new KhuyenMai(maKM);
                HoaDon hd = new HoaDon(maHD, ngayLap, kH, nV, kM, trangThai, phuongThucThanhToan);
                listHD.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listHD;
    }

    public boolean addHoaDon(HoaDon hd) {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO HoaDon (maHD, ngayLap, maKH, maNV, maKM, trangThai, phuongThucThanhToan) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getNgayLap().toString());
            ps.setString(3, hd.getKhachHang().getMaKH());
            ps.setString(4, hd.getNhanVien().getMaNV());
            ps.setString(5, hd.getKhuyenMai() == null ? null : hd.getKhuyenMai().getMaKM());
            ps.setString(6, hd.getTrangThai());
            ps.setString(7, hd.getPhuongThucThanhToan());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateHoaDon(HoaDon hd) {
        try {

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE HoaDon SET ngayLap = ?, maKH = ?, maNV = ?, maKM = ?, trangThai = ?, "
                    + "phuongThucThanhToan = ? WHERE maHD = ?");
            ps.setString(1, hd.getNgayLap().toString());
            ps.setString(2, hd.getKhachHang().getMaKH());
            ps.setString(3, hd.getNhanVien().getMaNV());
            ps.setString(4, hd.getKhuyenMai() == null ? null : hd.getKhuyenMai().getMaKM());
            ps.setString(5, hd.getTrangThai());
            ps.setString(6, hd.getPhuongThucThanhToan());
            ps.setString(7, hd.getMaHD());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteHoaDon(String maHD) {
        try {

            PreparedStatement ps = con.prepareStatement("DELETE FROM HoaDon WHERE maHD = ?");
            ps.setString(1, maHD);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public HoaDon getHoaDonById(String maHD) {
        try {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT maHD, ngayLap, maKH, maNV, maKM, trangThai, phuongThucThanhToan "
                    + "FROM HoaDon WHERE maHD = ?");
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalDate ngayLap = LocalDate.parse(rs.getString("ngayLap"));
                String maKH = rs.getString("maKH");
                String maNV = rs.getString("maNV");
                String maKM = rs.getString("maKM");
                String trangThai = rs.getString("trangThai");
                String phuongThucThanhToan = rs.getString("phuongThucThanhToan");

                KhachHang kH = new KhachHang(maKH);
                NhanVien nV = new NhanVien(maNV);
                KhuyenMai kM = new KhuyenMai(maKM);
                HoaDon hd = new HoaDon(maHD, ngayLap, kH, nV, kM, trangThai, phuongThucThanhToan);
                return hd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<HoaDon> searchHoaDonByMaHD(String maHD) {
        ArrayList<HoaDon> result = new ArrayList<>();
        try {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT maHD, ngayLap, maKH, maNV, maKM, trangThai, phuongThucThanhToan "
                    + "FROM HoaDon WHERE maHD LIKE ?");
            ps.setString(1, "%" + maHD + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate ngayLap = LocalDate.parse(rs.getString("ngayLap"));
                String maKH = rs.getString("maKH");
                String maNV = rs.getString("maNV");
                String maKM = rs.getString("maKM");
                String trangThai = rs.getString("trangThai");
                String phuongThucThanhToan = rs.getString("phuongThucThanhToan");

                HoaDon hd = new HoaDon(rs.getString("maHD"), ngayLap, new KhachHang(maKH),
                        new NhanVien(maNV), new KhuyenMai(maKM), trangThai, phuongThucThanhToan);
                result.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getSoLuong(){
        int count = 0 ;
        try {
            PreparedStatement ps = con.prepareStatement("select * from HoaDon");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return  count;
    }

}
