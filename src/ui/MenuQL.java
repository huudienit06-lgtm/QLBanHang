package ui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuQL extends JPanel {
    private final JMenuBar menuBar;
    private final JMenu menuHeThong ;
    private final JMenu menuDanhMuc;
    private final JMenu menuXuLy;
    private final JMenu menuThongKe;

    private final JMenuItem itemTrangChu;
    private final JMenuItem itemDangXuat;
    private final JMenuItem itemKhachHang;
    private final JMenuItem itemNhanVien;
    private final JMenuItem itemSanPham;
    private final JMenuItem itemDanhMuc;
    private final JMenuItem itemKhuyenMai;
    private final JMenuItem itemThue;
    private final JMenuItem itemTaiKhoan;
    private final JMenuItem itemLapHoaDon;
    private final JMenuItem itemLapPhieuNhapKho;
    private final JMenuItem itemThongKeDoanhThu;
    private final JMenuItem itemThongKeSanPham;
    private final JMenuItem itemThongKeHoaDong;
    private final JMenuItem itemThongKeNhapKho;


    public MenuQL() {
        JPanel pnlMenu = new JPanel();
        menuBar = new JMenuBar();
        pnlMenu.add(menuBar);
        menuHeThong = new JMenu("Hệ Thống");
        menuDanhMuc = new JMenu("Danh Mục");
        menuXuLy = new JMenu("Xử Lý");
        menuThongKe = new JMenu("Thống Kê");

        menuBar.add(menuHeThong);
        itemTrangChu = new JMenuItem("Trang Chủ");
        itemDangXuat = new JMenuItem("Đăng Xuất");
        menuHeThong.add(itemTrangChu);
        menuHeThong.add(itemDangXuat);

        menuBar.add(menuDanhMuc);
        itemKhachHang = new JMenuItem("Khách Hàng");
        itemNhanVien = new JMenuItem("Nhân Viên");
        itemSanPham = new JMenuItem("Sản Phẩm");
        itemDanhMuc = new JMenuItem("Danh Mục");
        itemKhuyenMai = new JMenuItem("Khuyến Mãi");
        itemThue = new JMenuItem("Thuế");
        itemTaiKhoan = new JMenuItem("Tài khoản");
        menuDanhMuc.add(itemKhachHang);
        menuDanhMuc.add(itemNhanVien);
        menuDanhMuc.add(itemSanPham);
        menuDanhMuc.add(itemDanhMuc);
        menuDanhMuc.add(itemKhuyenMai);
        menuDanhMuc.add(itemThue);
        menuDanhMuc.add(itemTaiKhoan);

        menuBar.add(menuXuLy);
        itemLapHoaDon = new JMenuItem("Lập Hóa Đơn");   
        itemLapPhieuNhapKho = new JMenuItem("Lập phiếu nhập kho");
        menuXuLy.add(itemLapHoaDon);
        menuXuLy.add(itemLapPhieuNhapKho);

        menuBar.add(menuThongKe);
        itemThongKeDoanhThu = new JMenuItem("Thống kê doanh thu");
        itemThongKeSanPham = new JMenuItem("Thống kê sản phẩm");
        itemThongKeHoaDong = new JMenuItem("Thống kê hóa đơn");
        itemThongKeNhapKho = new JMenuItem("Thống kê nhập kho");
        menuThongKe.add(itemThongKeDoanhThu);
        menuThongKe.add(itemThongKeSanPham);    
        menuThongKe.add(itemThongKeHoaDong);
        menuThongKe.add(itemThongKeNhapKho);
    }
}
