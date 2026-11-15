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

public class PhieuNhapKho_dao {
    public PhieuNhapKho_dao(){

    }
    public ArrayList<PhieuNhapKho> getAllPhieuNhapKho(){
        ArrayList<PhieuNhapKho> dsPNK = new ArrayList<>();
        try{
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * from PhieuNhapKho";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String maPNK= rs.getString(1);
                LocalDate ngayLap= rs.getDate(2).toLocalDate();
                NhanVien nv = new NhanVien(rs.getString(3));
                String trangThai = rs.getString(4);
                PhieuNhapKho pnk = new PhieuNhapKho(maPNK,ngayLap,nv,trangThai);
                dsPNK.add(pnk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsPNK;
    }

    public ArrayList<PhieuNhapKho> timPhieuNhapKho(String maPNK){
        ArrayList<PhieuNhapKho> dsPNK = new ArrayList<>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try{
            String sql = "SELECT * from PhieuNhapKho where maPNK = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maPNK);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                LocalDate ngayLap = rs.getDate(2).toLocalDate();
                NhanVien nv  = new NhanVien(rs.getString(3));
                String trangThai = rs.getString(4);
                PhieuNhapKho  pnk = new PhieuNhapKho(maPNK,ngayLap,nv,trangThai);
                dsPNK.add(pnk);
            }
        } catch(SQLException e){
            e.printStackTrace();
        } finally{
            try{
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dsPNK;
    }

    public boolean themPhieuNhapKho(PhieuNhapKho pnk){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("insert into PhieuNhapKho values(?, ?, ?, ?)");
            stmt.setString(1,pnk.getMaPNK());
            stmt.setDate(2,Date.valueOf(pnk.getNgayLap()));
            stmt.setString(3,pnk.getNhanVien().getMaNV());
            stmt.setString(4,pnk.getTrangThai());
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

    public boolean xoaPhieuNhapKho(String maPNK){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("delete from PhieuNhapKho where maPNK = ?");
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
    public boolean suaPhieuNhapKho(PhieuNhapKho pnk){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("update PhieuNhapKho set ngayLap = ?, maNV = ?, trangThai = ? where maPNK = ?");
            stmt.setDate(1,Date.valueOf(pnk.getNgayLap()));
            stmt.setString(2,pnk.getNhanVien().getMaNV());
            stmt.setString(3,pnk.getTrangThai());
            stmt.setString(4,pnk.getMaPNK());
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