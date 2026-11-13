package dao;

import java.sql.Statement;
import java.sql.Connection;
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

    public ArrayList<ChiTietPhieuNhapKho> timChiTietPhieuNhapKho(String maPNK){
        ArrayList<ChiTietPhieuNhapKho> dsCTPNK = new ArrayList<>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try{
            String sql = "SELECT * from ChiTietPhieuNhapKho where maPNK = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maPNK);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            	 PhieuNhapKho pnk = new PhieuNhapKho(maPNK);
                 SanPham sp = new SanPham(rs.getString(2));
                 Integer soLuong = rs.getInt(3);
                 ChiTietPhieuNhapKho ctPNK = new ChiTietPhieuNhapKho(pnk ,sp,soLuong);
                 dsCTPNK.add(ctPNK);
            }
        } catch(SQLException e){
            e.printStackTrace();
        } finally{
            try{
            	if(stmt != null) {
            		stmt.close();
            	}
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
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


}