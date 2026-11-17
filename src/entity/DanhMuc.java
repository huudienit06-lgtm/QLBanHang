package entity;

public class DanhMuc {

    private String maDM;
    private String tenDM;
    private double loiNhuan;

    public DanhMuc() {
    }

    public DanhMuc(String maDM) {
        this.maDM = maDM;
    }

    public DanhMuc(String maDM, String tenDM) {
        this(maDM, tenDM, 0);
    }

    public DanhMuc(String maDM, String tenDM, double loiNhuan) {
        this.maDM = maDM;
        this.tenDM = tenDM;
        setLoiNhuan(loiNhuan);
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public String getTenDM() {
        return tenDM;
    }

    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }

    public double getLoiNhuan() {
        return loiNhuan;
    }

    public void setLoiNhuan(double loiNhuan) {
        if (loiNhuan < 0) {
            throw new IllegalArgumentException("Lợi nhuận không được âm");
        }
        this.loiNhuan = loiNhuan;
    }
}
