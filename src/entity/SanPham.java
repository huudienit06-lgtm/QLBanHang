package entity;

public class SanPham {
    private String maSP;
    private String tenSP;
    private double giaNhap;
    private Integer soLuongTon;
    private KhuyenMai khuyenMai;
    private DanhMuc danhMuc;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, double giaNhap, Integer soLuongTon,
            KhuyenMai khuyenMai, DanhMuc danhMuc) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.giaNhap = giaNhap;
        this.soLuongTon = soLuongTon;
        this.khuyenMai = khuyenMai;
        this.danhMuc = danhMuc;
    }

    public SanPham(String maSP) {
        this.maSP = maSP;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public Integer getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(Integer soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public DanhMuc getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
    }

    @Override
    public String toString() {
        return "SanPham [maSP=" + maSP + ", tenSP=" + tenSP + ", giaNhap=" + giaNhap
                + ", soLuongTon=" + soLuongTon
                + ", khuyenMai=" + (khuyenMai == null ? null : khuyenMai.getMaKM())
                + ", danhMuc=" + (danhMuc == null ? null : danhMuc.getMaDM()) + "]";
    }

    public double tinhGiaBan(){
        return this.giaNhap*(1+this.getDanhMuc().getLoiNhuan());
    }

    public double tinhGiaBanSauKhuyenMai(){
        double giaBanSauKhuyenMai=tinhGiaBan();
        if(this.getKhuyenMai() != null && this.getKhuyenMai().getDonViTinh().trim().equalsIgnoreCase("Phần trăm")){
            return  giaBanSauKhuyenMai - (giaBanSauKhuyenMai*(this.getKhuyenMai().getTiLeGiamGia() / 100));
        }
        else if(this.getKhuyenMai() != null && this.getKhuyenMai().getDonViTinh().trim().equalsIgnoreCase("Ngàn")){
            return giaBanSauKhuyenMai - (this.getKhuyenMai().getTiLeGiamGia());
        }
        return giaBanSauKhuyenMai;
    }
}
