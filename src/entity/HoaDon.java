package entity;

import dao.ChiTietHoaDonDAO;
import dao.Thue_dao;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class HoaDon {

    private String maHD;
    private LocalDate ngayLap;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private KhuyenMai khuyenMai;
    private String trangThai;
    private String phuongThucThanhToan;
    private double tongTien;

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

    public void setNgayLap(LocalDate ngayLap){
        this.ngayLap = ngayLap;
    }

    public String getNgayLapFormatted() {
        if (ngayLap == null) {
            return "";
        }
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

    public double tinhTongTienLocal(ArrayList<ChiTietHoaDon> list) {
        tongTien = 0;
        // ArrayList<ChiTietHoaDon> list = new ChiTietHoaDonDAO().getDSCTHDByID(this.maHD);
        for (ChiTietHoaDon chiTietHoaDon : list) {
            tongTien += chiTietHoaDon.tinhThanhTien();
        }
        return tongTien;
    }

    public double tinhTongTien() {
        tongTien = 0;
        ArrayList<ChiTietHoaDon> list = new ChiTietHoaDonDAO().getDSCTHDByID(this.maHD);
        for (ChiTietHoaDon chiTietHoaDon : list) {
            tongTien += chiTietHoaDon.tinhThanhTien();
        }
        return tongTien;
    }

    public double tinhKhuyenMai() {
        double tongKhuyenMai = 0;
        if (this.getKhuyenMai() == null) {
            tongKhuyenMai = 0;
        } else {
            if ("Phần trăm".equals(this.getKhuyenMai().getDonViTinh())) {
                tongKhuyenMai = tongTien * (this.getKhuyenMai().getTiLeGiamGia() / 100);
            } else if ("Ngàn".equals(this.getKhuyenMai().getDonViTinh())) {
                tongKhuyenMai = (this.getKhuyenMai().getTiLeGiamGia());
            }
        }
        return tongKhuyenMai;
    }

    public double tinhTongThue() {
        double tongThue = 0;
        ArrayList<Thue> list = new Thue_dao().getAllThue();
        for (Thue t : list) {
            tongThue += tongTien * t.getTiLeThue() / 100;
        }
        return tongThue;
    }

    public double tinhThanhTien() {
        tinhTongTien();
        return (tongTien - tinhKhuyenMai() + tinhTongThue());
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

    public String getTongTienFormatted(double tien) {
        NumberFormat nf = NumberFormat.getInstance(
                new Locale("vi", "VN")
        );

        nf.setMaximumFractionDigits(0);

        return nf.format(tien)+"VND";
    }

    @Override
    public String toString() {
        return this.maHD + getKhachHang().getTenKH() + getNhanVien().getTenNV() + String.valueOf(getNgayLapFormatted() + String.valueOf(tinhThanhTien())) + String.valueOf(getPhuongThucThanhToan()) + String.valueOf(getTrangThai());
    }
}
