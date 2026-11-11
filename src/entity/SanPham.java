package entity;

import java.math.BigDecimal;

public class SanPham {
    private String maSP;
    private String tenSP;
    private BigDecimal giaNhap;
    private Integer soLuongTon;
    private KhuyenMai khuyenMai;
    private DanhMuc danhMuc;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, BigDecimal giaNhap, Integer soLuongTon,
            KhuyenMai khuyenMai, DanhMuc danhMuc) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.giaNhap = giaNhap;
        this.soLuongTon = soLuongTon;
        this.khuyenMai = khuyenMai;
        this.danhMuc = danhMuc;
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

    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(BigDecimal giaNhap) {
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
}
