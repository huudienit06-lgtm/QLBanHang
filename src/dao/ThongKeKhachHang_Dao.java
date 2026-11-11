package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectdb.ConnectDB;

/**
 * Bảng HoaDon(maHD, ngayLap, maKH, maNV, maKM, maThue, trangThai, phuongThucThanhToan) không có cột tổng tiền,
 * nên tổng tiền mỗi hóa đơn được tính từ bảng chi tiết.
 *
 * GIẢ ĐỊNH: ChiTietHoaDon(maHD, maSP, soLuong, donGia).
 * Nếu bảng chi tiết của bạn khác tên/cột, CHỈ CẦN SỬA hằng số TONG_TIEN_HOA_DON bên dưới.
 * (Chưa trừ khuyến mãi maKM hay cộng thuế maThue; nếu cần thì sửa công thức trong SUM(...).)
 */
public class ThongKeKhachHang_Dao {

    private static final String TONG_TIEN_HOA_DON =
            "SELECT hd.maHD, hd.maKH, SUM(ct.soLuong * ct.donGia) AS tongTien "
          + "FROM HoaDon hd JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD "
          + "GROUP BY hd.maHD, hd.maKH";

    public int demKhachHang() {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM KhachHang")) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double giaTriHoaDonTrungBinh() {
        String sql = "SELECT AVG(CAST(tongTien AS FLOAT)) FROM (" + TONG_TIEN_HOA_DON + ") t";
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Mỗi dòng: {Hạng, MaKH, TenKH, SoHoaDon, TongChiTieu} */
    public List<Object[]> topKhachHang(int n) {
        List<Object[]> ds = new ArrayList<>();
        String sql = "SELECT TOP (?) kh.maKH, kh.tenKH, COUNT(t.maHD) AS soHD, SUM(t.tongTien) AS tongChi "
                   + "FROM KhachHang kh JOIN (" + TONG_TIEN_HOA_DON + ") t ON kh.maKH = t.maKH "
                   + "GROUP BY kh.maKH, kh.tenKH "
                   + "ORDER BY SUM(t.tongTien) DESC";
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, n);
                try (ResultSet rs = ps.executeQuery()) {
                    int stt = 1;
                    while (rs.next()) {
                        ds.add(new Object[]{stt++, rs.getString("maKH"), rs.getString("tenKH"),
                                rs.getInt("soHD"), rs.getDouble("tongChi")});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}