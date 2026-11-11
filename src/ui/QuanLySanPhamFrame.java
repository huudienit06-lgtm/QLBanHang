package ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class QuanLySanPhamFrame extends JFrame {

    public QuanLySanPhamFrame() {
        setTitle("Quản lý Sản phẩm - Danh mục - Khuyến mãi");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        SanPhamPanel pnlSanPham = new SanPhamPanel();
        DanhMucPanel pnlDanhMuc = new DanhMucPanel();
        KhuyenMaiPanel pnlKhuyenMai = new KhuyenMaiPanel();
        ThongKeSanPhamPanel pnlThongKe = new ThongKeSanPhamPanel();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Sản phẩm", pnlSanPham);
        tabs.addTab("Danh mục", pnlDanhMuc);
        tabs.addTab("Khuyến mãi", pnlKhuyenMai);
        tabs.addTab("Thống kê sản phẩm", pnlThongKe);

        tabs.addChangeListener(e -> {
            java.awt.Component c = tabs.getSelectedComponent();
            if (c instanceof SanPhamPanel) {
                ((SanPhamPanel) c).lamMoi();
            } else if (c instanceof DanhMucPanel) {
                ((DanhMucPanel) c).lamMoi();
            } else if (c instanceof KhuyenMaiPanel) {
                ((KhuyenMaiPanel) c).lamMoi();
            } else if (c instanceof ThongKeSanPhamPanel) {
                ((ThongKeSanPhamPanel) c).lamMoi();
            }
        });

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuanLySanPhamFrame().setVisible(true));
    }
}
