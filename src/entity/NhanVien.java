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

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    @Override
    public String toString() {
        return "NhanVien [maNV=" + maNV + ", tenNV=" + tenNV + ", gioiTinh=" + gioiTinh + ", sdt=" + sdt + ", chucVu=" + chucVu + "]";
    }

}
