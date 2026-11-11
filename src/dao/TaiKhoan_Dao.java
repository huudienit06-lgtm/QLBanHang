package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        TaiKhoan tk = null;

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "SELECT * FROM TaiKhoan "
                       + "WHERE MaNV = ? AND MatKhau = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tenDangNhap);
                ps.setString(2, matKhau);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        NhanVien nv = new NhanVien();
                        nv.setMaNV(rs.getString("MaNV"));

                        String mk = rs.getString("MatKhau");
                        String tt = rs.getString("TrangThai");

                        tk = new TaiKhoan(nv, mk, tt);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tk;
    }

    public boolean doiMatKhau(String maNV, String matKhauMoi) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "UPDATE TaiKhoan "
                       + "SET MatKhau = ? WHERE MaNV = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, matKhauMoi);
                ps.setString(2, maNV);

                int n = ps.executeUpdate();

                return n > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean themTaiKhoan(TaiKhoan tk) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "INSERT INTO TaiKhoan "
                       + "(MaNV, MatKhau, TrangThai) "
                       + "VALUES (?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tk.getNhanVien().getMaNV());
                ps.setString(2, tk.getMatKhau());
                ps.setString(3, tk.getTrangThai());

                int n = ps.executeUpdate();

                return n > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean suaTaiKhoan(String maNV, TaiKhoan tkMoi) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "UPDATE TaiKhoan "
                       + "SET MatKhau = ?, TrangThai = ? "
                       + "WHERE MaNV = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tkMoi.getMatKhau());
                ps.setString(2, tkMoi.getTrangThai());
                ps.setString(3, maNV);

                int n = ps.executeUpdate();

                return n > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaTaiKhoan(String maNV) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "DELETE FROM TaiKhoan WHERE MaNV = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maNV);

                int n = ps.executeUpdate();

                return n > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
