package entity;

import java.text.NumberFormat;
import java.util.Locale;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private SanPham sanPham;
    private Integer soLuong;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, SanPham sanPham, Integer soLuong) {
        this.hoaDon = hoaDon;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public double tinhThanhTien(){
        return this.getSanPham().tinhGiaBanSauKhuyenMai() * this.getSoLuong();
    }

    public String getThanhTienFormatted() {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        nf.setMaximumFractionDigits(0);
        return nf.format(tinhThanhTien()) + "VND";
    }
}
