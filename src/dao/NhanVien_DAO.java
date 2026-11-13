package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.NhanVien;

public class NhanVien_DAO {

    public NhanVien_DAO(){

    }
    public ArrayList<NhanVien> getAllNhanVien(){
        ArrayList<NhanVien> dsnv = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhanVien";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));
                nv.setSdt(rs.getString("sdt"));
                nv.setGioiTinh(rs.getString("gioiTinh"));
                nv.setChucVu(rs.getString("chucVu"));
                dsnv.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return dsnv;
    }

    public ArrayList<NhanVien> timNhanVientheoMa(String maNV){
        ArrayList<NhanVien> dsnv = new ArrayList<>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try{
            String sql = "Select * from NhanVien where maNV = ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, maNV);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                String tennv= rs.getString(2);
                String gt= rs.getString(4);
                String sdt= rs.getString(3);
                String chucvu= rs.getString(5);
                NhanVien nv = new NhanVien(maNV, tennv,sdt,gt,chucvu);
                dsnv.add(nv);

        }
    }catch(SQLException e){
        e.printStackTrace();
    }finally{
        try{
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     return dsnv;
    }

    public boolean themNhanVien(NhanVien nv){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("insert into NhanVien values (?, ?, ?, ?, ?)");
            stmt.setString(1,nv.getMaNV());
            stmt.setString(2,nv.getTenNV());
            stmt.setString(3,nv.getSdt());
            stmt.setString(4,nv.getGioiTinh());
            stmt.setString(5,nv.getChucVu());
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
    public boolean xoaNhanVien(String maNV){
        ConnectDB.getInstance();
        Connection con= ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try{
            String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
            stmt= con.prepareStatement(sql);
            stmt.setString(1, maNV);
            int n = stmt.executeUpdate();
            return n>0;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt != null){
                    stmt.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean suaNhanVien(String maNV, NhanVien nvMoi){
        ConnectDB.getInstance();
        Connection con= ConnectDB.getConnection();
        PreparedStatement stmt= null;
        try{
            String sql="UPDATE NhanVien SET tenNV = ?, sdt = ?, gioiTinh = ?, chucVu = ? WHERE maNV = ?";
            stmt= con.prepareStatement(sql);
            stmt.setString(1, nvMoi.getTenNV());
            stmt.setString(2, nvMoi.getSdt());
            stmt.setString(3, nvMoi.getGioiTinh());
            stmt.setString(4, nvMoi.getChucVu());
            stmt.setString(5, maNV);
            int n = stmt.executeUpdate();
            return n>0;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt != null){
                    stmt.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
