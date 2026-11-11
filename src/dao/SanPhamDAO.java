package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.DanhMuc;
import entity.KhuyenMai;
import entity.SanPham;

public class SanPhamDAO {

    private static final String SQL_CHON = "SELECT sp.maSP, sp.tenSP, sp.giaNhap, sp.soLuongTon, sp.maKM, sp.maDM, "
            + "dm.tenDM, km.tenKM, km.tiLeGiamGia, km.loaiKM, km.ngayBatDau, km.ngayKetThuc, "
            + "km.trangThai AS trangThaiKM "
            + "FROM SanPham sp "
            + "LEFT JOIN DanhMuc dm ON sp.maDM = dm.maDM "
            + "LEFT JOIN KhuyenMai km ON sp.maKM = km.maKM ";

    public SanPhamDAO() {
    }

    private SanPham docSanPham(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSP(rs.getString("maSP"));
        sp.setTenSP(rs.getString("tenSP"));
        sp.setGiaNhap(rs.getBigDecimal("giaNhap"));
        sp.setSoLuongTon(rs.getInt("soLuongTon"));

        String maDM = rs.getString("maDM");
        if (maDM != null) {
            sp.setDanhMuc(new DanhMuc(maDM, rs.getString("tenDM")));
        }

        String maKM = rs.getString("maKM");
        if (maKM != null) {
            Date bd = rs.getDate("ngayBatDau");
            Date kt = rs.getDate("ngayKetThuc");
            sp.setKhuyenMai(new KhuyenMai(
                    maKM,
                    rs.getString("tenKM"),
                    rs.getDouble("tiLeGiamGia"),
                    rs.getString("loaiKM"),
                    bd == null ? null : bd.toLocalDate(),
                    kt == null ? null : kt.toLocalDate(),
                    rs.getString("trangThaiKM")));
        }
        return sp;
    }

    public ArrayList<SanPham> getAllSanPham() {
        ArrayList<SanPham> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = SQL_CHON + "ORDER BY sp.maSP";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(docSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<SanPham> timSanPham(String tuKhoa) {
        ArrayList<SanPham> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = SQL_CHON + "WHERE sp.maSP LIKE ? OR sp.tenSP LIKE ? ORDER BY sp.maSP";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, "%" + tuKhoa + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(docSanPham(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themSanPham(SanPham sp) {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO SanPham (maSP, tenSP, giaNhap, soLuongTon, maKM, maDM) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setBigDecimal(3, sp.getGiaNhap());
            ps.setInt(4, sp.getSoLuongTon() == null ? 0 : sp.getSoLuongTon());
            setMaTuyChon(ps, 5, sp.getKhuyenMai() == null ? null : sp.getKhuyenMai().getMaKM());
            setMaTuyChon(ps, 6, sp.getDanhMuc() == null ? null : sp.getDanhMuc().getMaDM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaSanPham(SanPham sp) {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE SanPham SET tenSP = ?, giaNhap = ?, soLuongTon = ?, maKM = ?, maDM = ? WHERE maSP = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setBigDecimal(2, sp.getGiaNhap());
            ps.setInt(3, sp.getSoLuongTon() == null ? 0 : sp.getSoLuongTon());
            setMaTuyChon(ps, 4, sp.getKhuyenMai() == null ? null : sp.getKhuyenMai().getMaKM());
            setMaTuyChon(ps, 5, sp.getDanhMuc() == null ? null : sp.getDanhMuc().getMaDM());
            ps.setString(6, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaSanPham(String maSP) {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM SanPham WHERE maSP = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Sản phẩm bán chạy trong khoảng ngày (không tính hóa đơn đã hủy).
     * Mỗi phần tử gồm {maSP, tenSP, tổng số lượng đã bán}.
     */
    public ArrayList<Object[]> getSanPhamBanChay(int top, LocalDate tuNgay, LocalDate denNgay) {
        ArrayList<Object[]> ds = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT TOP (?) sp.maSP, sp.tenSP, SUM(ct.soLuong) AS daBan "
                + "FROM ChiTietHoaDon ct "
                + "JOIN HoaDon hd ON ct.maHD = hd.maHD "
                + "JOIN SanPham sp ON ct.maSP = sp.maSP "
                + "WHERE hd.ngayLap BETWEEN ? AND ? AND hd.trangThai NOT LIKE N'%hủy%' "
                + "GROUP BY sp.maSP, sp.tenSP "
                + "ORDER BY daBan DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, top);
            ps.setDate(2, Date.valueOf(tuNgay));
            ps.setDate(3, Date.valueOf(denNgay));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new Object[] { rs.getString("maSP"), rs.getString("tenSP"), rs.getInt("daBan") });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    private void setMaTuyChon(PreparedStatement ps, int viTri, String ma) throws SQLException {
        if (ma == null) {
            ps.setNull(viTri, Types.VARCHAR);
        } else {
            ps.setString(viTri, ma);
        }
    }
}
