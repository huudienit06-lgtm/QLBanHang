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
import entity.NhanVien;
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

    public boolean themChiTietPhieuNhapKho(ChiTietPhieuNhapKho pnk){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("insert into ChiTietPhieuNhapKho values(?, ?, ?)");
            stmt.setString(1,pnk.getPhieuNhapKho().getMaPNK());
            stmt.setString(2,pnk.getSanPham().getMaSP());
            stmt.setInt(3,pnk.getSoLuong());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    public boolean xoaChiTietPhieuNhapKho(String maPNK){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("delete from ChiTietPhieuNhapKho where maPNK = ?");
            stmt.setString(1,maPNK);
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }
    public boolean suaChiTietPhieuNhapKho(ChiTietPhieuNhapKho pnk){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("update ChiTietPhieuNhapKho set maSP = ?, soLuong = ? where maPNK = ?");
            stmt.setString(1,pnk.getSanPham().getMaSP());
            stmt.setInt(2,pnk.getSoLuong());
            stmt.setString(4,pnk.getPhieuNhapKho().getMaPNK());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }
}