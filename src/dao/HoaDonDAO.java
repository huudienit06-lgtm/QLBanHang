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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import entity.ChiTietHoaDon;

public class HoaDonDAO {
    private KhachHang_Dao khDao = new KhachHang_Dao();
    private NhanVien_DAO nvDao = new NhanVien_DAO();
    private KhuyenMaiDAO kmDao = new KhuyenMaiDAO();

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

                KhachHang kH = khDao.timkhachHang(maKH);
                NhanVien nV = nvDao.timNhanVientheoMa(maNV);
                KhuyenMai kM = kmDao.timKhuyenMaiByID(maKM);
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

    public boolean updateHoaDonWithDetails(HoaDon hd, List<ChiTietHoaDon> details) {
        boolean autoCommit = true;
        boolean autoCommitCaptured = false;
        try {
            autoCommit = con.getAutoCommit();
            autoCommitCaptured = true;
            con.setAutoCommit(false);

            Map<String, Integer> newQuantities = new LinkedHashMap<>();
            for (ChiTietHoaDon detail : details) {
                if (detail == null || detail.getSanPham() == null
                        || detail.getSanPham().getMaSP() == null
                        || detail.getSanPham().getMaSP().trim().isEmpty()
                        || detail.getSoLuong() == null || detail.getSoLuong() <= 0) {
                    throw new SQLException("Chi tiết hóa đơn không hợp lệ");
                }
                String maSP = detail.getSanPham().getMaSP();
                newQuantities.merge(maSP, detail.getSoLuong(), Math::addExact);
            }

            Map<String, Integer> oldQuantities = new LinkedHashMap<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT maSP, soLuong FROM ChiTietHoaDon WITH (UPDLOCK, HOLDLOCK) WHERE maHD = ?")) {
                ps.setString(1, hd.getMaHD());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        oldQuantities.put(rs.getString("maSP"), rs.getInt("soLuong"));
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE HoaDon SET ngayLap = ?, maKH = ?, maNV = ?, maKM = ?, trangThai = ?, "
                    + "phuongThucThanhToan = ? WHERE maHD = ?")) {
                ps.setString(1, hd.getNgayLap().toString());
                ps.setString(2, hd.getKhachHang().getMaKH());
                ps.setString(3, hd.getNhanVien().getMaNV());
                ps.setString(4, hd.getKhuyenMai() == null ? null : hd.getKhuyenMai().getMaKM());
                ps.setString(5, hd.getTrangThai());
                ps.setString(6, hd.getPhuongThucThanhToan());
                ps.setString(7, hd.getMaHD());
                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Không tìm thấy hóa đơn cần cập nhật");
                }
            }

            Map<String, Integer> stockAdjustments = new LinkedHashMap<>(oldQuantities);
            for (Map.Entry<String, Integer> entry : newQuantities.entrySet()) {
                stockAdjustments.merge(entry.getKey(), -entry.getValue(), Integer::sum);
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE SanPham SET soLuongTon = soLuongTon + ? "
                    + "WHERE maSP = ? AND soLuongTon + ? >= 0")) {
                for (Map.Entry<String, Integer> entry : stockAdjustments.entrySet()) {
                    int adjustment = entry.getValue();
                    ps.setInt(1, adjustment);
                    ps.setString(2, entry.getKey());
                    ps.setInt(3, adjustment);
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("Không đủ tồn kho cho sản phẩm " + entry.getKey());
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM ChiTietHoaDon WHERE maHD = ?")) {
                ps.setString(1, hd.getMaHD());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong) VALUES (?, ?, ?)")) {
                for (Map.Entry<String, Integer> entry : newQuantities.entrySet()) {
                    ps.setString(1, hd.getMaHD());
                    ps.setString(2, entry.getKey());
                    ps.setInt(3, entry.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException rollbackError) {
                rollbackError.printStackTrace();
            }
            return false;
        } finally {
            if (autoCommitCaptured) {
                try {
                    con.setAutoCommit(autoCommit);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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
        HoaDon hd;
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
                KhachHang kH = khDao.timkhachHang(maKH);
                System.out.println("Mã khách hàng: " + maKH);
                System.out.println("Đối tượng khách hàng: " + kH);
                NhanVien nV = nvDao.timNhanVientheoMa(maNV);
                KhuyenMai kM = kmDao.timKhuyenMaiByID(maKM);
                hd = new HoaDon(maHD, ngayLap, kH, nV, kM, trangThai, phuongThucThanhToan);
                System.out.println("Khách hàng trong hóa đơn: " + hd.getKhachHang());
                return hd;    
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public int getSoLuong() {
        int count = 0;
        try {
            PreparedStatement ps = con.prepareStatement("select * from HoaDon");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    public int demHoaDonTheoDieuKien(LocalDate tuNgay, LocalDate denNgay, String maNV)
            throws java.sql.SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM HoaDon WHERE 1 = 1");
        if (tuNgay != null) {
            sql.append(" AND ngayLap >= ?");
        }
        if (denNgay != null) {
            sql.append(" AND ngayLap <= ?");
        }
        if (maNV != null && !maNV.trim().isEmpty()) {
            sql.append(" AND maNV = ?");
        }

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int parameterIndex = 1;
            if (tuNgay != null) {
                ps.setDate(parameterIndex++, java.sql.Date.valueOf(tuNgay));
            }
            if (denNgay != null) {
                ps.setDate(parameterIndex++, java.sql.Date.valueOf(denNgay));
            }
            if (maNV != null && !maNV.trim().isEmpty()) {
                ps.setString(parameterIndex, maNV.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public ArrayList<HoaDon> getHoaDonTheoDieuKien(LocalDate tuNgay, LocalDate denNgay, String maNV)
            throws java.sql.SQLException {
        ArrayList<HoaDon> ds = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT hd.maHD, hd.ngayLap, hd.maKH, kh.tenKH, "
                + "hd.maNV, nv.tenNV, hd.trangThai, hd.phuongThucThanhToan "
                + "FROM HoaDon hd "
                + "JOIN KhachHang kh ON kh.maKH = hd.maKH "
                + "JOIN NhanVien nv ON nv.maNV = hd.maNV "
                + "WHERE 1 = 1");
        if (tuNgay != null) {
            sql.append(" AND hd.ngayLap >= ?");
        }
        if (denNgay != null) {
            sql.append(" AND hd.ngayLap <= ?");
        }
        if (maNV != null && !maNV.trim().isEmpty()) {
            sql.append(" AND hd.maNV = ?");
        }
        sql.append(" ORDER BY hd.ngayLap DESC, hd.maHD DESC");

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int parameterIndex = 1;
            if (tuNgay != null) {
                ps.setDate(parameterIndex++, java.sql.Date.valueOf(tuNgay));
            }
            if (denNgay != null) {
                ps.setDate(parameterIndex++, java.sql.Date.valueOf(denNgay));
            }
            if (maNV != null && !maNV.trim().isEmpty()) {
                ps.setString(parameterIndex, maNV.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date ngayLap = rs.getDate("ngayLap");
                    ds.add(new HoaDon(
                            rs.getString("maHD"),
                            ngayLap == null ? null : ngayLap.toLocalDate(),
                            new KhachHang(rs.getString("maKH"), rs.getString("tenKH"),
                                    null, null, null, null),
                            new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
                                    null, null, null),
                            null,
                            rs.getString("trangThai"),
                            rs.getString("phuongThucThanhToan")));
                }
            }
        }
        return ds;
    }

    public String getMaHDTiepTheo() {
        String sql = "SELECT COALESCE(MAX(TRY_CONVERT(INT, SUBSTRING(maHD, 3, LEN(maHD)))), 0) + 1 "
                + "FROM HoaDon WHERE maHD LIKE 'HD%'";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format(Locale.ROOT, "HD%04d", rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<HoaDon> searchHoaDonByTuKhoa(String tuKhoa) {
        ArrayList<HoaDon> result = new ArrayList<>();
        String sql = "SELECT hd.maHD, hd.ngayLap, hd.maKH, kh.tenKH, hd.maNV, nv.tenNV, hd.maKM, "
                + "hd.trangThai, hd.phuongThucThanhToan "
                + "FROM HoaDon hd "
                + "JOIN KhachHang kh ON hd.maKH = kh.maKH "
                + "JOIN NhanVien nv ON hd.maNV = nv.maNV "
                + "WHERE hd.maHD LIKE ? AND kh.tenKH LIKE ? AND nv.tenNV LIKE ?";
        String tuKhoaTimKiem = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tuKhoaTimKiem);
            ps.setString(2, tuKhoaTimKiem);
            ps.setString(3, tuKhoaTimKiem);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maKM = rs.getString("maKM");
                    HoaDon hd = new HoaDon(
                            rs.getString("maHD"),
                            LocalDate.parse(rs.getString("ngayLap")),
                            new KhachHang(rs.getString("maKH"), rs.getString("tenKH"),
                                    null, null, null, null),
                            new NhanVien(rs.getString("maNV"), rs.getString("tenNV"),
                                    null, null, null),
                            maKM == null ? null : new KhuyenMai(maKM),
                            rs.getString("trangThai"),
                            rs.getString("phuongThucThanhToan"));
                    result.add(hd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<HoaDon> searchHoaDonByTuKhoa(
            String maHD, String tenKH, String tenNV) {

        ArrayList<HoaDon> result = new ArrayList<>();

        String sql = "SELECT hd.maHD, hd.ngayLap, hd.maKH, kh.tenKH, "
                + "hd.maNV, nv.tenNV, hd.maKM, hd.trangThai, "
                + "hd.phuongThucThanhToan "
                + "FROM HoaDon hd "
                + "JOIN KhachHang kh ON hd.maKH = kh.maKH "
                + "JOIN NhanVien nv ON hd.maNV = nv.maNV "
                + "WHERE hd.maHD LIKE ? "
                + "AND kh.tenKH LIKE ? "
                + "AND nv.tenNV LIKE ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + (maHD == null ? "" : maHD.trim()) + "%");
            ps.setString(2, "%" + (tenKH == null ? "" : tenKH.trim()) + "%");
            ps.setString(3, "%" + (tenNV == null ? "" : tenNV.trim()) + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maKM = rs.getString("maKM");

                    HoaDon hd = new HoaDon(
                            rs.getString("maHD"),
                            rs.getDate("ngayLap").toLocalDate(),
                            new KhachHang(
                                    rs.getString("maKH"),
                                    rs.getString("tenKH"),
                                    null, null, null, null
                            ),
                            new NhanVien(
                                    rs.getString("maNV"),
                                    rs.getString("tenNV"),
                                    null, null, null
                            ),
                            maKM == null ? null : new KhuyenMai(maKM),
                            rs.getString("trangThai"),
                            rs.getString("phuongThucThanhToan")
                    );

                    result.add(hd);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
