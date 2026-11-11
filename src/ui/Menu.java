package ui;

import entity.NhanVien;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menu extends JPanel implements ActionListener{

    private final Main main;
    private final JMenuBar menuBar;
    private final JMenu menuHeThong;
    private final JMenu menuDanhMuc;
    private final JMenu menuXuLy;
    private final JMenu menuThongKe;

    private final JMenuItem itemTrangChu;
    private final JMenuItem itemDangXuat;
    private final JMenuItem itemKhachHang;
    private final JMenuItem itemNhanVien;
    private final JMenuItem itemSanPham;
    private final JMenuItem itemDanhMuc;
    private JMenuItem itemKhuyenMai;
    private JMenuItem itemThue;
    private JMenuItem itemTaiKhoan;
    private final JMenuItem itemLapHoaDon;
    private JMenuItem itemLapPhieuNhapKho;
    private JMenuItem itemThongKeDoanhThu;
    private final JMenuItem itemThongKeSanPham;
    private final JMenuItem itemThongKeHoaDon;
    private JMenuItem itemThongKeNhapKho;

    public Menu(NhanVien nv, Main main) {
        this.main = main;
        boolean laQuanLy = nv != null
            && "Quản lý".equalsIgnoreCase(nv.getChucVu());
        setLayout(new GridBagLayout());

        menuBar = new JMenuBar();
        menuBar.setLayout(new GridLayout(1, 0));
        menuHeThong = new JMenu("Hệ Thống");
        menuDanhMuc = new JMenu("Danh Mục");
        menuXuLy = new JMenu("Xử Lý");
        menuThongKe = new JMenu("Thống Kê");
        menuHeThong.setHorizontalAlignment(SwingConstants.CENTER);
        menuDanhMuc.setHorizontalAlignment(SwingConstants.CENTER);
        menuXuLy.setHorizontalAlignment(SwingConstants.CENTER);
        menuThongKe.setHorizontalAlignment(SwingConstants.CENTER);

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
        if (laQuanLy) {
            itemKhuyenMai = new JMenuItem("Khuyến Mãi");
            itemThue = new JMenuItem("Thuế");
            itemTaiKhoan = new JMenuItem("Tài khoản");
            menuDanhMuc.add(itemKhuyenMai);
            menuDanhMuc.add(itemThue);
            menuDanhMuc.add(itemTaiKhoan);
        }

        menuDanhMuc.add(itemKhachHang);
        menuDanhMuc.add(itemNhanVien);
        menuDanhMuc.add(itemSanPham);
        menuDanhMuc.add(itemDanhMuc);

        menuBar.add(menuXuLy);
        itemLapHoaDon = new JMenuItem("Lập Hóa Đơn");
        if (laQuanLy) {
            itemLapPhieuNhapKho = new JMenuItem("Lập phiếu nhập kho");
            menuXuLy.add(itemLapPhieuNhapKho);
        }
        menuXuLy.add(itemLapHoaDon);
        menuBar.add(menuThongKe);
        if (laQuanLy) {
            itemThongKeDoanhThu = new JMenuItem("Thống kê doanh thu");
            itemThongKeNhapKho = new JMenuItem("Thống kê nhập kho");
            menuThongKe.add(itemThongKeDoanhThu);
            menuThongKe.add(itemThongKeNhapKho);
        }
        itemThongKeSanPham = new JMenuItem("Thống kê sản phẩm");
        itemThongKeHoaDon = new JMenuItem("Thống kê hóa đơn");

        menuThongKe.add(itemThongKeSanPham);
        menuThongKe.add(itemThongKeHoaDon);

        GridBagConstraints menuConstraints = new GridBagConstraints();
        menuConstraints.gridx = 0;
        menuConstraints.gridy = 0;
        menuConstraints.weightx = 2;
        menuConstraints.weighty = 1;
        menuConstraints.fill = GridBagConstraints.BOTH;
        menuConstraints.anchor = GridBagConstraints.WEST;
        add(menuBar, menuConstraints);

        JPanel pnlThongTinNhanVien = new JPanel(new GridLayout(2, 1));
        JLabel lblTenNhanVien = new JLabel(
                nv != null && nv.getTenNV() != null ? nv.getTenNV() : "Nhân viên");
        JLabel lblMaVaChucVu = new JLabel(
                "Mã: " + (nv != null && nv.getMaNV() != null ? nv.getMaNV() : "")
                + " | Chức vụ: " + (nv != null && nv.getChucVu() != null ? nv.getChucVu() : ""));
        lblTenNhanVien.setHorizontalAlignment(SwingConstants.RIGHT);
        lblMaVaChucVu.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlThongTinNhanVien.add(lblTenNhanVien);
        pnlThongTinNhanVien.add(lblMaVaChucVu);

        GridBagConstraints infoConstraints = new GridBagConstraints();
        infoConstraints.gridx = 1;
        infoConstraints.gridy = 0;
        infoConstraints.weightx = 1;
        infoConstraints.weighty = 1;
        infoConstraints.fill = GridBagConstraints.BOTH;
        infoConstraints.anchor = GridBagConstraints.EAST;
        infoConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        add(pnlThongTinNhanVien, infoConstraints);

        itemDangXuat.addActionListener(this);
        itemDanhMuc.addActionListener(this);
        itemKhachHang.addActionListener(this);
        itemLapHoaDon.addActionListener(this);
        itemNhanVien.addActionListener(this);
        itemSanPham.addActionListener(this);
        itemThongKeHoaDon.addActionListener(this);
        itemTrangChu.addActionListener(this);
        if (laQuanLy) {
            itemKhuyenMai.addActionListener(this);
            itemThue.addActionListener(this);
            itemTaiKhoan.addActionListener(this);
            itemLapPhieuNhapKho.addActionListener(this);
            itemThongKeDoanhThu.addActionListener(this);
            itemThongKeNhapKho.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        main.xuLyChucNang(e.getActionCommand());
    }
    
}
