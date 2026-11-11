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
import entity.Thue;

public class Thue_dao {
    public Thue_dao() {

    }
    public ArrayList<Thue> getAllThue(){
        ArrayList<Thue> dsThue = new ArrayList<>();
        try{
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * from Thue";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String maThue= rs.getString(1);
                String tenThue = rs.getString(2);
                Double tiLeThue = rs.getDouble(3);
                Thue thue = new Thue(maThue,tenThue,tiLeThue);
                dsThue.add(thue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsThue;
    }

    public ArrayList<Thue> timThue(String maThue){
        ArrayList<Thue> dsThue = new ArrayList<>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try{
            String sql = "SELECT * from Thue where maThue = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maThue);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            	String tenThue = rs.getString(2);
                Double tiLeThue = rs.getDouble(3);
                Thue thue = new Thue(maThue,tenThue,tiLeThue);
                dsThue.add(thue);
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
        return dsThue;
    }

    public boolean themThue(Thue thue){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("insert into Thue values(?, ?, ?)");
            stmt.setString(1,thue.getMaThue());
            stmt.setString(2,thue.getTenThue());
            stmt.setDouble(3,thue.getTiLeThue());
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

    public boolean xoaThue(String maThue){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("delete from Thue where maThue = ?");
            stmt.setString(1,maThue);
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
    public boolean suaThue(Thue thue){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt= null;

        int n =0;
        try{
            stmt = con.prepareStatement("update Thue set tenThue = ?, tiLeThue = ? where maThue = ?");
            stmt.setString(1,thue.getTenThue());
            stmt.setDouble(2,thue.getTiLeThue());
            stmt.setString(3,thue.getMaThue());
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