package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectdb.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoan_Dao {
    public TaiKhoan_Dao(){

    }
    public ArrayList<TaiKhoan> getallTaiKhoan(){
        ArrayList<TaiKhoan> dstk = new ArrayList<>();
        try{
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * from TaiKhoan";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String matk = rs.getString(1);
                String matkhau = rs.getString(2);
                String trangthai = rs.getString(3);

                NhanVien nv = new NhanVien();
                nv.setMaNV(matk);
                TaiKhoan tk = new TaiKhoan(nv, matkhau, trangthai);
                dstk.add(tk);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return dstk;
    }
}
