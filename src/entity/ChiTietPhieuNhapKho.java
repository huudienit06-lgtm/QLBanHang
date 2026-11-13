package entity;

public class ChiTietPhieuNhapKho {
    private PhieuNhapKho phieuNhapKho;
    private SanPham sanPham;
    private Integer soLuong;

    public ChiTietPhieuNhapKho() {
    }

    public ChiTietPhieuNhapKho(PhieuNhapKho phieuNhapKho, SanPham sanPham, Integer soLuong) {
        this.phieuNhapKho = phieuNhapKho;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }



    public PhieuNhapKho getPhieuNhapKho() {
        return phieuNhapKho;
    }

    public void setPhieuNhapKho(PhieuNhapKho phieuNhapKho) {
        this.phieuNhapKho = phieuNhapKho;
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
}
