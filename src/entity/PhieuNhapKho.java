package entity;

import java.time.LocalDate;

public class PhieuNhapKho {
    private String maPNK;
    private LocalDate ngayLap;
    private NhanVien nhanVien;
    private String trangThai;

    public PhieuNhapKho() {
    }

    public PhieuNhapKho(String maPNK, LocalDate ngayLap, NhanVien nhanVien, String trangThai) {
        this.maPNK = maPNK;
        this.ngayLap = ngayLap;
        this.nhanVien = nhanVien;
        this.trangThai = trangThai;
    }

    public PhieuNhapKho(String maPNK) {
        this.maPNK = maPNK;
    }

    

    public String getMaPNK() {
        return maPNK;
    }

    public void setMaPNK(String maPNK) {
        this.maPNK = maPNK;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
