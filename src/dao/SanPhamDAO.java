package dao;

import connectdb.ConnectDB;
import entity.DanhMuc;
import entity.KhuyenMai;
import entity.SanPham;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class SanPhamDAO {

    // Câu SELECT dùng chung: lấy sản phẩm kèm danh mục và khuyến mãi
    private static final String SQL_CHON = "SELECT sp.maSP, sp.tenSP, sp.giaNhap, sp.soLuongTon, sp.maKM, sp.maDM, "
            + "dm.tenDM, dm.loiNhuan, km.tenKM, km.tiLeGiamGia, km.donViTinh, km.ngayBatDau, km.ngayKetThuc, "
            + "km.trangThai AS trangThaiKM "
            + "FROM SanPham sp "
            + "LEFT JOIN DanhMuc dm ON sp.maDM = dm.maDM "
            + "LEFT JOIN KhuyenMai km ON sp.maKM = km.maKM ";

    public SanPhamDAO() {
    }

    // Đọc một dòng của ResultSet thành đối tượng SanPham
    private SanPham docSanPham(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSP(rs.getString("maSP"));
        sp.setTenSP(rs.getString("tenSP"));
        sp.setGiaNhap(rs.getDouble("giaNhap"));
        sp.setSoLuongTon(rs.getInt("soLuongTon"));

        String maDM = rs.getString("maDM");
        if (maDM != null) {
            DanhMuc dm = new DanhMuc(maDM, rs.getString("tenDM"), rs.getFloat("loiNhuan"));
            sp.setDanhMuc(dm);
        }

        String maKM = rs.getString("maKM");
        if (maKM != null) {
            KhuyenMai km = new KhuyenMai();
            km.setMaKM(maKM);
            km.setTenKM(rs.getString("tenKM"));
            km.setTiLeGiamGia(rs.getDouble("tiLeGiamGia"));
            km.setDonViTinh(rs.getString("donViTinh"));
            Date ngayBD = rs.getDate("ngayBatDau");
            if (ngayBD != null) {
                km.setNgayBatDau(ngayBD.toLocalDate());
            }
            Date ngayKT = rs.getDate("ngayKetThuc");
            if (ngayKT != null) {
                km.setNgayKetThuc(ngayKT.toLocalDate());
            }
            km.setTrangThai(rs.getString("trangThaiKM"));
            sp.setKhuyenMai(km);
        }
        return sp;
    }

    // Lấy toàn bộ sản phẩm, sắp xếp theo mã
    public ArrayList<SanPham> getAllSanPham() {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = SQL_CHON + "ORDER BY sp.maSP";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dsSP.add(docSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return dsSP;
    }

    // Lấy toàn bộ sản phẩm, sản phẩm tồn ít nhất đứng đầu (dùng cho thống kê)
    public ArrayList<SanPham> getAllSanPhamTheoTonKho() {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = SQL_CHON + "ORDER BY sp.soLuongTon ASC, sp.maSP";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dsSP.add(docSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return dsSP;
    }

    // Tìm sản phẩm theo mã hoặc tên
    public ArrayList<SanPham> timSanPham(String tuKhoa) {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = SQL_CHON + "WHERE sp.maSP LIKE ? OR sp.tenSP LIKE ? ORDER BY sp.maSP";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                dsSP.add(docSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return dsSP;
    }

    public boolean themSanPham(SanPham sp) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "INSERT INTO SanPham (maSP, tenSP, giaNhap, soLuongTon, maKM, maDM) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, sp.getMaSP());
            stmt.setString(2, sp.getTenSP());
            stmt.setBigDecimal(3, BigDecimal.valueOf(sp.getGiaNhap()));
            stmt.setInt(4, sp.getSoLuongTon());
            if (sp.getKhuyenMai() == null) {
                stmt.setNull(5, Types.VARCHAR);
            } else {
                stmt.setString(5, sp.getKhuyenMai().getMaKM());
            }
            if (sp.getDanhMuc() == null) {
                stmt.setNull(6, Types.VARCHAR);
            } else {
                stmt.setString(6, sp.getDanhMuc().getMaDM());
            }
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    public boolean suaSanPham(SanPham sp) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "UPDATE SanPham SET tenSP = ?, giaNhap = ?, soLuongTon = ?, maKM = ?, maDM = ? WHERE maSP = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, sp.getTenSP());
            stmt.setBigDecimal(2, BigDecimal.valueOf(sp.getGiaNhap()));
            stmt.setInt(3, sp.getSoLuongTon());
            if (sp.getKhuyenMai() == null) {
                stmt.setNull(4, Types.VARCHAR);
            } else {
                stmt.setString(4, sp.getKhuyenMai().getMaKM());
            }
            if (sp.getDanhMuc() == null) {
                stmt.setNull(5, Types.VARCHAR);
            } else {
                stmt.setString(5, sp.getDanhMuc().getMaDM());
            }
            stmt.setString(6, sp.getMaSP());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    public boolean xoaSanPham(String maSP) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "DELETE FROM SanPham WHERE maSP = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maSP);
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, null);
        }
        return n > 0;
    }

    // Sinh mã tự động: SP + 4 chữ số, ví dụ SP0001 đã có thì trả về SP0002
    public String phatSinhMa() {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int soLonNhat = 0;
        try {
            String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maSP, 3, 10) AS INT)) FROM SanPham WHERE maSP LIKE 'SP%'";
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
        return "SP" + String.format("%03d", soLonNhat + 1);
    }

    // Sản phẩm bán chạy trong khoảng ngày (bỏ qua hóa đơn đã hủy).
    // Mỗi phần tử gồm: maSP, tenSP, tổng số lượng đã bán
    public ArrayList<Object[]> getSanPhamBanChay(int top, LocalDate tuNgay, LocalDate denNgay) {
        ArrayList<Object[]> dsBanChay = new ArrayList<Object[]>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT TOP (?) sp.maSP, sp.tenSP, SUM(ct.soLuong) AS daBan "
                    + "FROM ChiTietHoaDon ct "
                    + "JOIN HoaDon hd ON ct.maHD = hd.maHD "
                    + "JOIN SanPham sp ON ct.maSP = sp.maSP "
                    + "WHERE hd.ngayLap BETWEEN ? AND ? AND hd.trangThai NOT LIKE N'%hủy%' "
                    + "GROUP BY sp.maSP, sp.tenSP "
                    + "ORDER BY daBan DESC";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, top);
            stmt.setDate(2, Date.valueOf(tuNgay));
            stmt.setDate(3, Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] dong = new Object[3];
                dong[0] = rs.getString("maSP");
                dong[1] = rs.getString("tenSP");
                dong[2] = rs.getInt("daBan");
                dsBanChay.add(dong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dongTaiNguyen(stmt, rs);
        }
        return dsBanChay;
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

    public SanPham findByID(String maSP) {
        String sql = SQL_CHON + "WHERE sp.maSP = ?";
        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return docSanPham(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean capNhatSoLuongTon( String maSP, int soLuongBan){

        String sql = """
        UPDATE SanPham
        SET soLuongTon = soLuongTon - ?
        WHERE maSP = ? AND soLuongTon >= ?
    """;
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, soLuongBan);
            ps.setString(2, maSP);
            ps.setInt(3, soLuongBan);
            return ps.executeUpdate() > 0;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
