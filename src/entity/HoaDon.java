package entity;

import dao.ChiTietHoaDonDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HoaDon {

    private String maHD;
    private LocalDate ngayLap;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private KhuyenMai khuyenMai;
    private String trangThai;
    private String phuongThucThanhToan;

    public HoaDon() {
    }

    public HoaDon(String maHD, LocalDate ngayLap, KhachHang khachHang, NhanVien nhanVien,
            KhuyenMai khuyenMai, String trangThai, String phuongThucThanhToan) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.khuyenMai = khuyenMai;
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

    public String getNgayLapFormatted() {
        DateTimeFormatter dtf
                = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return ngayLap.format(dtf);
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

    public boolean kiemTraKhuyenMai(KhuyenMai khuyenMai) {
        if (khuyenMai != null) {

        }
        return false;
    }

    public double tinhTongTien() {
        double tongTien = 0;
        ArrayList<ChiTietHoaDon> list = new ChiTietHoaDonDAO().getDSCTHDByID(this.maHD);
        for (ChiTietHoaDon chiTietHoaDon : list) {
            tongTien += chiTietHoaDon.tinhThanhTien();
        }
        return tongTien;
    }

    public double tinhKhuyenMai() {
        double tongKhuyenMai = 0;
        if (this.getKhuyenMai() != null && this.getKhuyenMai().getDonViTinh().trim().equalsIgnoreCase("Phần trăm")) {
            tongKhuyenMai = tinhTongTien() * (this.getKhuyenMai().getTiLeGiamGia() / 100);
        } else if (this.getKhuyenMai() != null && this.getKhuyenMai().getDonViTinh().trim().equalsIgnoreCase("Ngàn")) {
            tongKhuyenMai = (this.getKhuyenMai().getTiLeGiamGia());
        }
        return tongKhuyenMai;
    }

    public double tinhThanhTien() {
        return tinhTongTien() - tinhKhuyenMai();
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
                + ", khuyenMai=" + khuyenMai + ", trangThai=" + trangThai + ", phuongThucThanhToan="
                + phuongThucThanhToan + "]";
    }

}
