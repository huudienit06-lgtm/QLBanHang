package entity;

import java.text.NumberFormat;
import java.util.Locale;

public class Thue {
    private String maThue;
    private String tenThue;
    private Double tiLeThue;

    public Thue() {
    }

    public Thue(String maThue, String tenThue, Double tiLeThue) {
        this.maThue = maThue;
        this.tenThue = tenThue;
        this.tiLeThue = tiLeThue;
    }

    public Thue(String maThue) {
        this.maThue = maThue;
    }

    
    public String getMaThue() {
        return maThue;
    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public String getTenThue() {
        return tenThue;
    }

    public void setTenThue(String tenThue) {
        this.tenThue = tenThue;
    }

    public Double getTiLeThue() {
        return tiLeThue;
    }

    public void setTiLeThue(Double tiLeThue) {
        this.tiLeThue = tiLeThue;
    }


    @Override
    public String toString() {
        return this.tenThue+"Mức thuế:"+this.tiLeThue+"%";
    }
    public double tinhThue(double tongTien) {
        return tongTien * this.getTiLeThue() / 100.0;
    }

    public String getTienThueFormatted(double tongTien) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        nf.setMaximumFractionDigits(0);
        return nf.format(tinhThue(tongTien)) + "VND";
    }

}
