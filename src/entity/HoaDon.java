package entity;

import java.time.LocalDate;

public class HoaDon {

    private String maHD;
    private LocalDate ngayLap;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private KhuyenMai khuyenMai;
    private Thue thue;
    private String trangThai;
    private String phuongThucThanhToan;

    public HoaDon() {
    }

    public HoaDon(String maHD, LocalDate ngayLap, KhachHang khachHang, NhanVien nhanVien,
            KhuyenMai khuyenMai, Thue thue, String trangThai, String phuongThucThanhToan) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.khuyenMai = khuyenMai;
        this.thue = thue;
        this.trangThai = trangThai;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public HoaDon(String maHD) {
        this.maHD = maHD;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public Thue getThue() {
        return thue;
    }

    public void setThue(Thue thue) {
        this.thue = thue;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maHD == null) ? 0 : maHD.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HoaDon other = (HoaDon) obj;
        if (maHD == null) {
            if (other.maHD != null) {
                return false;
            }
        } else if (!maHD.equals(other.maHD)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HoaDon [maHD=" + maHD + ", ngayLap=" + ngayLap + ", khachHang=" + khachHang + ", nhanVien=" + nhanVien
                + ", khuyenMai=" + khuyenMai + ", thue=" + thue + ", trangThai=" + trangThai + ", phuongThucThanhToan="
                + phuongThucThanhToan + "]";
    }

    // public double tinhThanhTien(String maHD){
    //     double tongTien = 0 ;
    //     ArrayList<ChiTietHoaDon> chiTietHoaDons = new ChiTietHoaDonDAO().getChiTietHoaDonByMaHD(maHD);
    //     for (ChiTietHoaDon chiTietHoaDon : chiTietHoaDons) {
    //         tongTien += chiTietHoaDon.tinhThanhTien();
    //     }
    //     return tongTien;
    // }
}
