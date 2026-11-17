package dao;

import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.PhieuNhapKho;
import entity.SanPham;
import entity.ChiTietPhieuNhapKho;

public class ChiTietPhieuNhapKho_dao {
    public ChiTietPhieuNhapKho_dao() {
	}
    public ArrayList<ChiTietPhieuNhapKho> getAllChiTietPhieuNhapKho_dao(){
        ArrayList<ChiTietPhieuNhapKho> dsCTPNK = new ArrayList<>();
        try{
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * from ChiTietPhieuNhapKho";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                PhieuNhapKho pnk = new PhieuNhapKho(rs.getString(1));
                SanPham sp = new SanPham(rs.getString(2));
                Integer soLuong = rs.getInt(3);
                ChiTietPhieuNhapKho ctPNK = new ChiTietPhieuNhapKho(pnk,sp,soLuong);
                dsCTPNK.add(ctPNK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCTPNK;
    }

    public ArrayList<ChiTietPhieuNhapKho> timChiTietPhieuNhapKho(String maPNK) {

        ArrayList<ChiTietPhieuNhapKho> dsCTPNK = new ArrayList<>();

        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();

        String sql = "SELECT * FROM ChiTietPhieuNhapKho WHERE maPNK = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPNK);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    PhieuNhapKho pnk = new PhieuNhapKho(rs.getString(1));
                    SanPham sp = new SanPham(rs.getString(2));
                    int soLuong = rs.getInt(3);

                    ChiTietPhieuNhapKho ctPNK =
                            new ChiTietPhieuNhapKho(pnk, sp, soLuong);

                    dsCTPNK.add(ctPNK);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsCTPNK;
    }

    public boolean themChiTietPhieuNhapKho(ChiTietPhieuNhapKho ct) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();

        try {
            con.setAutoCommit(false);

            // Tăng số lượng tồn kho
            String sqlUpdate = "UPDATE SanPham "
                             + "SET soLuongTon = soLuongTon + ? "
                             + "WHERE maSP = ?";

            try (PreparedStatement stmt = con.prepareStatement(sqlUpdate)) {
                stmt.setInt(1, ct.getSoLuong());
                stmt.setString(2, ct.getSanPham().getMaSP());

                if (stmt.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            // Thêm chi tiết phiếu nhập kho
            String sqlInsert = "INSERT INTO ChiTietPhieuNhapKho "
                             + "VALUES (?, ?, ?)";

            try (PreparedStatement stmt = con.prepareStatement(sqlInsert)) {
                stmt.setString(1, ct.getPhieuNhapKho().getMaPNK());
                stmt.setString(2, ct.getSanPham().getMaSP());
                stmt.setInt(3, ct.getSoLuong());

                if (stmt.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean xoaChiTietPhieuNhapKho(String maPNK, String maSP) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();

        try {
            con.setAutoCommit(false);

            // Lấy số lượng cần trừ
            int soLuong = 0;

            String sqlSelect = "SELECT soLuong FROM ChiTietPhieuNhapKho "
                             + "WHERE maPNK = ? AND maSP = ?";

            try (PreparedStatement stmt = con.prepareStatement(sqlSelect)) {
                stmt.setString(1, maPNK);
                stmt.setString(2, maSP);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    soLuong = rs.getInt("soLuong");
                }
            }

            // Trừ số lượng tồn kho
            // Không cho phép tồn kho bị âm
            String sqlUpdateSP = "UPDATE SanPham "
                               + "SET soLuongTon = soLuongTon - ? "
                               + "WHERE maSP = ? AND soLuongTon >= ?";

            try (PreparedStatement stmt = con.prepareStatement(sqlUpdateSP)) {
                stmt.setInt(1, soLuong);
                stmt.setString(2, maSP);
                stmt.setInt(3, soLuong);

                if (stmt.executeUpdate() == 0) {
                    con.rollback();

                    System.out.println(
                        "Không thể xóa vì số lượng tồn kho hiện tại không đủ!"
                    );

                    return false;
                }
            }

            // Xóa chi tiết phiếu nhập kho
            String sqlDelete = "DELETE FROM ChiTietPhieuNhapKho "
                             + "WHERE maPNK = ? AND maSP = ?";

            try (PreparedStatement stmt = con.prepareStatement(sqlDelete)) {
                stmt.setString(1, maPNK);
                stmt.setString(2, maSP);

                if (stmt.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean suaChiTietPhieuNhapKho(String maPNK, String maSP, int soLuongMoi) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();

        try {
            con.setAutoCommit(false);

            // Lấy số lượng cũ
            int soLuongCu = 0;

            String sqlSelect = "SELECT soLuong FROM ChiTietPhieuNhapKho "
                             + "WHERE maPNK = ? AND maSP = ?";

            try (PreparedStatement stmt = con.prepareStatement(sqlSelect)) {
                stmt.setString(1, maPNK);
                stmt.setString(2, maSP);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    soLuongCu = rs.getInt("soLuong");
                }
            }

            // Tính số lượng chênh lệch
            int chenhLech = soLuongMoi - soLuongCu;

            // Cập nhật số lượng tồn kho
            String sqlUpdateSP = "UPDATE SanPham "
                               + "SET soLuongTon = soLuongTon + ? "
                               + "WHERE maSP = ?";

            try (PreparedStatement stmt = con.prepareStatement(sqlUpdateSP)) {
                stmt.setInt(1, chenhLech);
                stmt.setString(2, maSP);

                if (stmt.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            // Cập nhật chi tiết phiếu nhập kho
            String sqlUpdateCT = "UPDATE ChiTietPhieuNhapKho "
                               + "SET soLuong = ? "
                               + "WHERE maPNK = ? AND maSP = ?";

            try (PreparedStatement stmt = con.prepareStatement(sqlUpdateCT)) {
                stmt.setInt(1, soLuongMoi);
                stmt.setString(2, maPNK);
                stmt.setString(3, maSP);

                if (stmt.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList<ChiTietPhieuNhapKho> getThongKeNhapKho(LocalDate tuNgay, LocalDate denNgay) {
        ArrayList<ChiTietPhieuNhapKho> dsCTPNK = new ArrayList<>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT ct.maPNK, ct.maSP, sp.tenSP, ct.soLuong, sp.soLuongTon, sp.giaNhap, pnk.ngayLap "
        + "FROM ChiTietPhieuNhapKho ct "
        + "JOIN PhieuNhapKho pnk ON ct.maPNK = pnk.maPNK "
        + "JOIN SanPham sp ON ct.maSP = sp.maSP "
        + "WHERE pnk.ngayLap BETWEEN ? AND ? "
        + "ORDER BY pnk.ngayLap ASC, ct.maPNK";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(tuNgay));
            stmt.setDate(2, Date.valueOf(denNgay));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PhieuNhapKho pnk = new PhieuNhapKho(rs.getString("maPNK"),
                            rs.getDate("ngayLap").toLocalDate(), null, null);
                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("maSP"));
                    sp.setTenSP(rs.getString("tenSP"));
                    sp.setGiaNhap(rs.getDouble("giaNhap"));
                    Integer soLuong = rs.getInt("soLuong");
                    sp.setSoLuongTon(rs.getInt("soLuongTon"));  
                    dsCTPNK.add(new ChiTietPhieuNhapKho(pnk, sp, soLuong));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsCTPNK;
}
public boolean themPhieuNhapKhoVaNhieuChiTiet(
        PhieuNhapKho pnk,
        ArrayList<ChiTietPhieuNhapKho> dsChiTiet) {

    ConnectDB.getInstance();
    Connection con = ConnectDB.getConnection();

    boolean autoCommitCu = true;

    try {
        autoCommitCu = con.getAutoCommit();
        con.setAutoCommit(false);

        // 1. Thêm phiếu nhập kho
        String sqlPNK = "INSERT INTO PhieuNhapKho "
                + "(maPNK, ngayLap, maNV, trangThai) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sqlPNK)) {
            ps.setString(1, pnk.getMaPNK());
            ps.setDate(2, Date.valueOf(pnk.getNgayLap()));
            ps.setString(3, pnk.getNhanVien().getMaNV());
            ps.setString(4, pnk.getTrangThai());

            ps.executeUpdate();
        }

        // 2. Thêm từng sản phẩm vào chi tiết phiếu nhập
        String sqlCT = "INSERT INTO ChiTietPhieuNhapKho "
                + "(maPNK, maSP, soLuong) VALUES (?, ?, ?)";

        String sqlSP = "UPDATE SanPham "
                + "SET soLuongTon = soLuongTon + ? "
                + "WHERE maSP = ?";

        try (PreparedStatement psCT = con.prepareStatement(sqlCT);
             PreparedStatement psSP = con.prepareStatement(sqlSP)) {

            for (ChiTietPhieuNhapKho ct : dsChiTiet) {

                // Thêm chi tiết phiếu nhập
                psCT.setString(1, pnk.getMaPNK());
                psCT.setString(2, ct.getSanPham().getMaSP());
                psCT.setInt(3, ct.getSoLuong());
                psCT.executeUpdate();

                // Cập nhật tồn kho
                psSP.setInt(1, ct.getSoLuong());
                psSP.setString(2, ct.getSanPham().getMaSP());

                if (psSP.executeUpdate() == 0) {
                    throw new SQLException("Không tìm thấy sản phẩm: "
                            + ct.getSanPham().getMaSP());
                }
            }
        }

        con.commit();
        return true;

    } catch (SQLException e) {
        try {
            con.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        e.printStackTrace();
        return false;

    } finally {
        try {
            con.setAutoCommit(autoCommitCu);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}