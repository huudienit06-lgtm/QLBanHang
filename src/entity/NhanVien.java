package entity;
public class NhanVien {
    private String maNV;
    private String tenNV;
    private String sdt;
    private String gioiTinh;
    private String chucVu;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String sdt, String gioiTinh, String chucVu) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.gioiTinh = gioiTinh;
        this.chucVu = chucVu;
    }

    public NhanVien(String maNV) {
        this.maNV = maNV;
    }
    public NhanVien(String maNV, String tenNV, String chucVu, String soDienThoai) {
        this(maNV, tenNV, soDienThoai, null, chucVu);
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getSoDienThoai() {
        return sdt;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.sdt = soDienThoai;
    }
}