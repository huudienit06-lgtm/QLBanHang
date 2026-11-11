package entity;

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
}
