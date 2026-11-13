package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.KhachHang;

public class KhachHang_Dao {
    public KhachHang_Dao(){

    }
    public ArrayList<KhachHang> getAllKhachHang(){
        ArrayList<KhachHang> dskh = new ArrayList<>();
        try{
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * from KhachHang";
            Statement stmt = (Statement) con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String maKH= rs.getString(1);
                String tenKH= rs.getString(2);
                String sdt= rs.getString(3);
                String gmail= rs.getString(4);
                String diachi= rs.getString(5);
                String loaiKH= rs.getString(6);
                KhachHang kh = new KhachHang(maKH, tenKH, sdt, gmail, diachi, loaiKH);
                dskh.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dskh;
    }

    public ArrayList<KhachHang> timkhachHang(String maKH){
        ArrayList<KhachHang> dskh = new ArrayList<>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try{
            String sql = "SELECT * from KhachHang where maKH = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String tenKH= rs.getString(2);
                String sdt = rs.getString(3);
                String gmail = rs.getString(4);
                String diachi = rs.getString(5);
                String loaiKH = rs.getString(6);
                KhachHang  kh= new KhachHang(maKH, tenKH, sdt, gmail, diachi, loaiKH);
                dskh.add(kh);
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
        return dskh;
    }

    public boolean themKhachHang(KhachHang kh){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("insert into KhachHang values(?, ?, ?, ?, ?, ?)");
            stmt.setString(1,kh.getMaKH());
            stmt.setString(2,kh.getTenKH());
            stmt.setString(3,kh.getSdt());
            stmt.setString(4,kh.getGmail());
            stmt.setString(5,kh.getDiaChi());
            stmt.setString(6,kh.getLoaiKH());
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

    public boolean xoaKhachHang(String maKH){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("delete from KhachHang where maKH = ?");
            stmt.setString(1,maKH);
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
    public boolean suaKhachHang(KhachHang kh){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("update KhachHang set tenKH = ?, sdt = ?, gmail = ?, diaChi = ?, loaiKH = ? where maKH = ?");
            stmt.setString(1,kh.getTenKH());
            stmt.setString(2,kh.getSdt());
            stmt.setString(3,kh.getGmail());
            stmt.setString(4,kh.getDiaChi());
            stmt.setString(5,kh.getLoaiKH());
            stmt.setString(6,kh.getMaKH());
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
