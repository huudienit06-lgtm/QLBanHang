package entity;

public class TaiKhoan {
    private NhanVien nhanVien;
    private String matKhau;
    private String trangThai;

    public TaiKhoan() {
    }

    public TaiKhoan(NhanVien nhanVien, String matKhau, String trangThai) {
        this.nhanVien = nhanVien;
        this.matKhau = matKhau;
        this.trangThai = trangThai;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "TaiKhoan [nhanVien=" + nhanVien + ", matKhau=" + matKhau + ", trangThai=" + trangThai + "]";
    }
    
}
