package entity;

public class DanhMuc {

    private String maDM;
    private String tenDM;
    private float loiNhuan;

    public DanhMuc(String maDM, String tenDM, float loiNhuan) {
        this.maDM = maDM;
        this.tenDM = tenDM;
        this.loiNhuan = loiNhuan;
    }

    public DanhMuc(String maDM) {
        this.maDM = maDM;
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

    public float getLoiNhuan() {
        return loiNhuan;
    }

    public void setLoiNhuan(float loiNhuan) {
        this.loiNhuan = loiNhuan;
    }

}
